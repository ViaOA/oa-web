package com.viaoa.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;

import com.viaoa.annotation.OAClassFilter;
import com.viaoa.context.OAContext;
import com.viaoa.context.OAUserAccess;
import com.viaoa.ds.OASelect;
import com.viaoa.hub.CustomHubFilter;
import com.viaoa.hub.Hub;
import com.viaoa.object.OAFinder;
import com.viaoa.object.OAObject;
import com.viaoa.object.OAObjectEditQueryDelegate;
import com.viaoa.object.OAObjectInfo;
import com.viaoa.object.OAObjectInfoDelegate;
import com.viaoa.object.OAThreadLocalDelegate;
import com.viaoa.util.Base64;
import com.viaoa.util.OAConv;
import com.viaoa.util.OAFilter;
import com.viaoa.util.OAJaxb;
import com.viaoa.util.OAPropertyPath;
import com.viaoa.util.OAReflect;
import com.viaoa.util.OAString;
import com.viaoa.util.filter.OAAndFilter;

/*
?query=
?pp=
?ppx=
?from={class}&id={id}[&path={path}]
?filter=
?useRefId[=bool]
*/

/**
 * Servlet that allows REST API calls using HTTP for the OAModel. Has support for user access to object graph using property paths.
 * <p>
 * GET ===========<br>
 * 1) read object using name and Id<br>
 * http://localhost/oarest/{className/{id}<br>
 * http://localhost/oarest/{className/{id}?pp={x}&pp1={x}&ppN={x} pp = PropertyPath(s) from the ClassName of reference data to send.<br>
 * http://localhost/oarest/client/1<br>
 * http://localhost/oarest/clients?fromClass=company&fromId=1&fromPath=clients&pp=products<br>
 * http://localhost/oarest/order/1?pp=lines.item.vendor&pp=client.contacts<br>
 * <p>
 * 2) get objects using a property path from a base object<br>
 * http://localhost/oarest/{pluralClassName}?fromClass={fromClass&fromId={id}?pp={x}&pp1={x}&ppN={x}<br>
 * http://localhost:/oarest/clients?fromClass=company&fromId=1&fromPath=clients&pp=products<br>
 * <p>
 * 3) query objects using where clause<br>
 * http://localhost/oarest/{pluralClassName}?query={whereClause}&pp1={x}&ppN={x}<br>
 * http://localhost/oarest/clients?query=company.name like 'AAA*'&pp=products<br>
 * <p>
 * 4) filter support<br>
 * http://localhost/oarest/campaigns?filter=unassigned<br>
 * <p>
 * Also:<br>
 * useRefId[=bool] can be used if client supports managing refId in json response/body.<br>
 * <p>
 * POST ===========<br>
 * create new object<br>
 * http://localhost/oarest/{className}
 * <p>
 * PUT =========== <br>
 * update an existing object<br>
 * http://localhost/oarest/{className}[/{id}]
 * <p>
 * DELETE ===========<br>
 * delete existing object<br>
 * http://localhost/oarest/{className}/{id}
 * <p>
 *
 * @author vvia
 */
public class OARestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = Logger.getLogger(OARestServlet.class.getName());
	private String packageName;
	private ServletConfig servletConfig;
	private String contextPath;
	private HashMap<String, Class> hmClassName = new HashMap<String, Class>();
	private HashMap<String, Class> hmClassPluralName = new HashMap<String, Class>();
	private String httpCORS; // "*" for all
	private String jwtHeaderName; // name of http header if using json web token for user auth.
	private String jwtKeyName; // name of http header if using json web token for user auth.

	/** default setting for JAXB marshelling to use refIds */
	private boolean bJaxbUseReferences;

	private AuthType authType = AuthType.None;

	public static enum AuthType {
		None, HttpBasic, JWT;
	}

	public OARestServlet(String packageName) {
		LOG.fine("Started packageName=" + packageName);
		this.packageName = packageName;
		setupMappings();
	}

	public void setJaxbUseReferences(boolean b) {
		this.bJaxbUseReferences = b;
	}

	public boolean getJaxbUseReferences() {
		return bJaxbUseReferences;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		this.servletConfig = config;
		contextPath = config.getServletContext().getContextPath();
		try {
			//qqqqqqqqqqqqqqqqqqqqq needs to include package names for other oaobjects ... search, inputs, etc qqqqqqqqqqqqqqqq
			String[] fnames = OAReflect.getClasses(packageName);
			for (String fn : fnames) {
				Class c = Class.forName(packageName + "." + fn);
				OAObjectInfo oi = OAObjectInfoDelegate.getObjectInfo(c);
				hmClassName.put(fn.toLowerCase(), c);
				hmClassPluralName.put(oi.getPluralName().toLowerCase(), c);
				LOG.fine("adding class=" + c.getName() + ", plural name=" + oi.getPluralName());
			}
		} catch (Exception e) {
			ServletException se = new ServletException("Exception getting class infos for package", e);
			LOG.log(Level.WARNING, "Could not initialize REST Servlet", se);
			throw se;
		}
		LOG.fine("init called, contextPath=" + contextPath);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		process(req, resp);
	}

	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		process(req, resp);
	}

	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		process(req, resp);
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String s2 = req.getHeader("Access-Control-Request-Headers");
		if (OAString.isNotEmpty(s2)) {
			resp.setHeader("Access-Control-Allow-Headers", s2);
		}

		s2 = req.getHeader("Access-Control-Request-Method");
		if (OAString.isNotEmpty(s2)) {
			resp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		}

		/*qqqqqqqqqqqqqqqqq finish CORS, let it be configured  ... create model object "RESTServlet"
		
		https://dev.to/effingkay/cors-preflighted-requests--options-method-3024
		header('Access-Control-Allow-Origin: *');
		header("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept");
		header('Access-Control-Allow-Methods: GET, POST, PUT');
		
		
		https://developer.mozilla.org/en-US/docs/Glossary/preflight_request
		ex: request
		OPTIONS /resource/foo
		Access-Control-Request-Method: DELETE
		Access-Control-Request-Headers: origin, x-requested-with
		Origin: https://foo.bar.org
		
		
		HTTP/1.1 204 No Content
		Connection: keep-alive
		Access-Control-Allow-Origin: https://foo.bar.org
		Access-Control-Allow-Methods: POST, GET, OPTIONS, DELETE
		Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept
		Access-Control-Allow-Headers: Authorization,Content-Type,Accept,Origin,User-Agent,DNT,Cache-Control,X-Mx-ReqToken,Keep-Alive,X-Requested-With,If-Modified-Since
		Access-Control-Max-Age: 86400
		*/

		process(req, resp);
	}

	/**
	 * Set the HTTP Header "Access-Control-Allow-Origin" (ex: "http://localhost:4200"), default = "*" (for now)
	 */
	public String getHttpCORS() {
		return httpCORS;
	}

	public void setHttpCORS(String val) {
		this.httpCORS = val;
	}

	/*qqqqqqqqq
	private AppUser appUser;
	protected AppUser getRestAppUser() {
	    if (appUser != null) return appUser;
	    AppUser au = (AppUser) ModelDelegate.getAppUsers().find(AppUserPP.loginId(), "restapi");
	    if (au == null) {
	        au = new AppUser();
	        au.setLoginId("restapi");
	        au.setPassword("restapi#3");
	        au.setAdmin(false);
	        ModelDelegate.getAppUsers().add(au);
	        appUser = au;
	    }
	    OAContext.setContext(this, appUser);
	    return appUser;
	}
	**/

	private OAUserAccess userAccess;

	protected OAUserAccess getUserAccess() {
		if (userAccess != null) {
			return userAccess;
		}

		//   http://localhost:8088/pi/api/clients
		userAccess = new OAUserAccess(true, true);

		/*
		userAccess = new OAUserAccess(false, true);
		userAccess.setValidPackage(Campaign.class.getPackage());  // only for PI project
		OAContext.setContextUserAccess(this, userAccess);
		
		Class[] classes = new Class[] {
		    AppUser.class,
		    AppUserLogin.class,
		    AppServer.class,
		    CompanyPlatformAccess.class,
		    ClientPlatformInfo.class
		};
		for (Class c : classes) {
		    userAccess.addNotVisible(c);
		    userAccess.addNotEnabled(c);
		}
		
		//qqqqqqqqqq this needs to use users Company ... and store in concurrentHM by company
		Company company = ModelDelegate.getCompanies().find(CompanyPP.name(), "*dent*");
		
		OAUserAccess userAccess2 = new OAUserAccess();
		userAccess.addUserAccess(userAccess2);
		
		// userAccess2.addVisible(company, CompanyPP.clients().products().campaigns().campaignLinks().platformCampaign().platformVendor().pp);
		userAccess2.addEnabled(company, CompanyPP.clients().products().campaigns().pp, null, true);
		userAccess2.addEnabled(company, CompanyPP.clients().products().campaigns().campaignLinks().pp, null, true);
		userAccess2.addEnabled(company, CompanyPP.clients().products().campaigns().campaignLinks().platformCampaign().pp, null, true);
		
		//qqqqqqqqq :  check for servlet session, add HTTP basic auth
		
		*/
		return userAccess;
	}

	/**
	 * Process the http request
	 */
	protected void process(final HttpServletRequest req, final HttpServletResponse resp) {

		HttpSession httpSession = req.getSession();
		System.out.println("Session => " + httpSession);

		//qqqqqqqqq check basic auth

		String s = getHttpCORS();
		if (OAString.isNotEmpty(s)) {
			resp.addHeader("Access-Control-Allow-Origin", s); // ex:  "http://localhost:4200");
		}
		try {
			//qqqq            getRestAppUser(); // make sure that it's initialized
			getUserAccess(); //    "   "
			//qqqq            OAThreadLocalDelegate.setContext(this);

			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			int status = _process(req, resp);
			resp.setStatus(status);
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "error processing REST", ex);
			resp.setStatus(resp.SC_INTERNAL_SERVER_ERROR);
			onException(resp, ex);
		} finally {
			OAThreadLocalDelegate.setContext(null);
		}
	}

	protected void onException(final HttpServletResponse resp, final Exception exception) {
		if (resp == null) {
			return;
		}
		OutputStream out;
		try {
			out = resp.getOutputStream();
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "error getting outputstream while processing REST", ex);
			resp.setStatus(resp.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		PrintWriter pw = new PrintWriter(out);
		resp.setContentType("application/json");
		pw.print("{");
		pw.print("  \"Exception\": {");
		pw.print("    \"message\": \"" + OAString.convert(exception.toString(), "\"", "") + "\",");
		pw.print("  }");
		pw.print("}");
		pw.flush(); // but dont close
	}

	/**
	 * used to MAP a REST endpoint to an OAModel API call.
	 */
	static public class Mapping {
		String description;
		String pathInfo; //  "/ui/campaigns"

		// value from query to match
		String name; // "filter"
		String value; // "open", "closed"
		boolean bDefault;

		String methodType; // get,post,put,etc

		String className;
		final ArrayList<String> alPropertyPath = new ArrayList<>();
		// String cors;

		String query; // null=no query, a "" (blank) is to select all.
		OAFilter filter;
	}

	private ArrayList<Mapping> alMapping = new ArrayList<OARestServlet.Mapping>();

	public void addMapping(Mapping mapping) {
		alMapping.add(mapping);
	}

	/**
	 * Create custom REST endpoints for FrontEnd. todo: move to controller to allow configuration.
	 */
	public void setupMappings() {
		/*qqqqqqqqqqq
		Mapping mapx = new Mapping();
		mapx.description = "array of unassigned campaigns";
		mapx.methodType = "get";
		mapx.pathInfo = "/ui/campaigns";
		mapx.name = "type";
		mapx.value = "unassigned";
		mapx.bDefault = true;
		mapx.className = "campaigns";
		mapx.alPropertyPath.add("product");
		mapx.alPropertyPath.add("client.company");
		mapx.alPropertyPath.add("campaignLinks.platformCampaign.platformVendor");
		mapx.alPropertyPath.add("buyer");
		mapx.query = "";  // all
		mapx.filter = new OAFilter() {
		    @Override
		    public boolean isUsed(Object obj) {
		        Campaign camp = (Campaign) obj;
		        if (camp == null) return false;
		        return camp.getCampaignLinks().size() == 0;
		    }
		};
		addMapping(mapx);
		
		mapx = new Mapping();
		mapx.description = "array of assigned campaigns";
		mapx.methodType = "get";
		mapx.pathInfo = "/ui/campaigns";
		mapx.name = "type";
		mapx.value = "assigned";
		mapx.bDefault = false;
		mapx.className = "campaigns";
		mapx.alPropertyPath.add("product");
		mapx.alPropertyPath.add("client.company");
		mapx.alPropertyPath.add("campaignLinks.platformCampaign.platformVendor");
		mapx.alPropertyPath.add("buyer");
		mapx.query = "";  // all
		mapx.filter = new OAFilter() {
		    @Override
		    public boolean isUsed(Object obj) {
		        Campaign camp = (Campaign) obj;
		        if (camp == null) return false;
		        return camp.getCampaignLinks().size() > 0;
		    }
		};
		addMapping(mapx);
		
		mapx = new Mapping();
		mapx.description = "campaign and detail";
		mapx.methodType = "get";
		mapx.pathInfo = "/ui/campaignDetail";
		mapx.className = "campaigns";
		mapx.alPropertyPath.add("product");
		mapx.alPropertyPath.add("client.company");
		mapx.alPropertyPath.add("campaignLinks.platformCampaign.platformVendor");
		mapx.alPropertyPath.add("buyer");
		mapx.query = "";  // all
		mapx.filter = new OAFilter() {
		    @Override
		    public boolean isUsed(Object obj) {
		        Campaign camp = (Campaign) obj;
		        if (camp == null) return false;
		        return camp.getCampaignLinks().size() > 0;
		    }
		};
		addMapping(mapx);
		
		//
		mapx = new Mapping();
		mapx.description = "unassigned platform campaigns";
		mapx.methodType = "get";
		mapx.pathInfo = "/ui/unassignedPlatformCampaigns";
		mapx.className = "platformCampaigns";
		mapx.alPropertyPath.add(PlatformCampaignPP.platformVendor().pp);
		mapx.query = "";  // all
		mapx.filter = new OAFilter() {
		    @Override
		    public boolean isUsed(Object obj) {
		        PlatformCampaign camp = (PlatformCampaign) obj;
		        if (camp == null) return false;
		        OADate date = new OADate();
		        for (CampaignLink cl : camp.getCampaignLinks()) {
		            if (date.between(cl.getStartDate(), cl.getEndDate())) return false;
		        }
		        return true;
		    }
		};
		addMapping(mapx);
		*/
	}

	protected int _process(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
		int httpStatus = HttpServletResponse.SC_OK;

		final String methodType = req.getMethod();
		final String contextPath = req.getContextPath(); //  "/pi"
		final String requestUrl = req.getRequestURL().toString(); //  "http://localhost:8088/pi/api/ui/campaigns/123"
		final String url = req.getRequestURI(); //  "/pi/api/ui/campaigns/123"
		String httpQueryString = req.getQueryString(); //  "filter=open&max=3&flag"
		final String authType = req.getAuthType();

		final String pathInfo = req.getPathInfo(); //  "/ui/campaigns/123"
		final String contentType = OAString.notNull(req.getContentType()).toLowerCase();

		final OutputStream out = resp.getOutputStream();
		final PrintWriter pw = new PrintWriter(out);
		String className = OAString.field(pathInfo, "/", 2);

		final String originalRequest = requestUrl + (OAString.isEmpty(httpQueryString) ? "" : httpQueryString); //  "http://localhost:8088/pi/api/ui/campaigns/123filter=open&max=3&flag"

		LOG.fine(String.format("Method=%s, url=%s, query=%s", methodType, requestUrl, httpQueryString));

		Enumeration enumx = req.getHeaderNames();
		for (; enumx.hasMoreElements();) {
			String key = (String) enumx.nextElement();
			LOG.finer("Header: " + key + ", " + req.getHeader(key));
		}

		enumx = req.getParameterNames();
		HashMap<String, String> hmParam = new HashMap<>();
		for (; enumx.hasMoreElements();) {
			String key = (String) enumx.nextElement();
			hmParam.put(key.toLowerCase(), req.getParameter(key));
		}

		boolean bUseRefId = bJaxbUseReferences;

		// get list of extra propertyPaths to include, for OAJaxb
		ArrayList<String> alPropertyPath = new ArrayList<>();
		enumx = req.getParameterNames();
		for (; enumx.hasMoreElements();) {
			String key = (String) enumx.nextElement();
			if (!key.toLowerCase().startsWith("pp")) {
				if (key.equalsIgnoreCase("useRefId")) {
					String s = req.getParameter(key);
					bUseRefId = (s == null) || OAConv.toBoolean(s);
				}
				continue;
			}
			if (key.length() > 2) {
				String s = key.substring(2);
				if (!OAString.isInteger(s)) {
					continue;
				}
			}
			String[] ss = req.getParameterValues(key);
			if (ss == null) {
				continue;
			}
			for (String s : ss) {
				if (s != null) {
					alPropertyPath.add(s);
				}
			}
		}

		// check to see if there is a custom Mapping for this URL
		OAFilter filterQuery = null;
		String description = originalRequest;
		String query = hmParam.get("query");

		for (Mapping map : alMapping) {
			if (!methodType.equalsIgnoreCase(map.methodType)) {
				continue;
			}
			if (pathInfo == null || map.pathInfo == null) {
				continue;
			}
			if (!pathInfo.equalsIgnoreCase(map.pathInfo)) {
				continue;
			}
			if (map.name != null) {
				String s = hmParam.get(map.name);
				if (map.value != null) {
					if (!map.value.equalsIgnoreCase(s)) {
						continue;
					}
				} else {
					if (!map.bDefault) {
						continue;
					}
				}
			}
			for (String s : map.alPropertyPath) {
				alPropertyPath.add(s);
			}
			description = map.description;
			className = map.className;
			query = map.query;
			filterQuery = map.filter;
			break;
		}

		LOG.fine(String.format("description=%s, query=%s", description, query));

		boolean bIsMany = false;
		Class clazz = null;
		if (className != null) {
			clazz = hmClassName.get(className.toLowerCase());
			if (clazz == null) {
				hmClassName.get("detail" + className.toLowerCase());
			}
			if (clazz == null) {
				clazz = hmClassPluralName.get(className.toLowerCase());
				if (clazz == null) {
					hmClassPluralName.get("detail" + className.toLowerCase());
				}
				bIsMany = true;
			}
		}

		if (clazz == null) {
			onException(resp, new Exception("Class not found, name=" + className));
			httpStatus = HttpServletResponse.SC_BAD_REQUEST;
			return httpStatus;
		}

		LOG.fine("clazz=" + clazz.getName());

		final OAJaxb jaxb = new OAJaxb<>(clazz);
		jaxb.setUseReferences(bUseRefId);
		jaxb.setIncludeGuids(false);

		for (String s : alPropertyPath) {
			jaxb.addPropertyPath(s);
		}
		String jsonInput = null;
		String jsonOutput = null;

		if ("put".equalsIgnoreCase(methodType) && !bIsMany) {
			// ========== PUT ===========
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = req.getReader();
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append('\n');
				}
			} finally {
				reader.close();
			}
			jsonInput = sb.toString();

			jaxb.setLoadingMode(OAJaxb.LoadingMode.UpdateRootOnly);

			OAObject obj = jaxb.convertFromJSON(jsonInput);

			if (obj != null) {
				obj.save();
				jsonOutput = jaxb.convertToJSON((OAObject) obj);
			} else {
				httpStatus = HttpServletResponse.SC_NOT_FOUND;
			}
		} else if ("post".equalsIgnoreCase(methodType) && !bIsMany) {
			// ========== POST ===========
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = req.getReader();
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append('\n');
				}
			} finally {
				reader.close();
			}
			jsonInput = sb.toString();

			jaxb.setLoadingMode(OAJaxb.LoadingMode.CreateNewRootOnly);

			OAObject obj = jaxb.convertFromJSON(jsonInput);

			if (obj != null) {
				obj.save();
			}

			jsonOutput = jaxb.convertToJSON((OAObject) obj);
		} else if ("delete".equalsIgnoreCase(methodType) && !bIsMany) {
			// ========== DELETE =========== qqqqqqqqqqq todo qqqqqqqqqq
			// call editQuery to validate qqqqqqqqq
		} else if ("get".equalsIgnoreCase(methodType)) {
			// ========== GET ===========
			if (!bIsMany) {
				String id = OAString.field(pathInfo, "/", 3);

				Object obj = null;

				if (OAString.isNotEmpty(id)) {
					OASelect sel = new OASelect(clazz);
					sel.select("id == " + id);
					obj = sel.next();
				}

				if (obj != null) {
					if (!OAObjectEditQueryDelegate.getAllowVisible(null, (OAObject) obj, null)) {
						httpStatus = HttpServletResponse.SC_UNAUTHORIZED;
					} else {
						jsonOutput = jaxb.convertToJSON((OAObject) obj);
					}
				}
			} else {
				Hub h = new Hub(clazz);

				if (OAString.isNotEmpty(query)) {
					if (query.equals("\"\"") || query.equals("''")) {
						query = null;
					}
					if (query != null && query.length() > 2 && (query.charAt(0) == '\"' || query.charAt(0) == '\'')) {
						query = query.substring(1, query.length() - 1); // remove quotes from quoted string
					}
				}
				String fromClass = hmParam.get("fromclass");
				if (OAString.isEmpty(fromClass)) {
					fromClass = hmParam.get("from");
				}
				String fromId = hmParam.get("fromid");
				if (OAString.isEmpty(fromId)) {
					fromId = hmParam.get("id");
				}
				String fromPath = hmParam.get("frompath");
				if (OAString.isEmpty(fromPath)) {
					fromPath = hmParam.get("path");
					if (OAString.isEmpty(fromPath)) {
						fromPath = className;
					}
				}

				String filterName = hmParam.get("filter");
				if (OAString.isNotEmpty(filterName)) {
					OAPropertyPath propertyPath = new OAPropertyPath(clazz, ":" + filterName);

					// match filters
					String[] names = propertyPath.getFilterNames();
					Object[] values = propertyPath.getFilterParamValues();
					Constructor[] constructors = propertyPath.getFilterConstructors();
					if (constructors.length != 1 || constructors[0] == null) {
						throw new Exception("Filter not found, name=" + filterName);
					}
					try {
						if (values[0] == null) {
							// propertyPath will use the constructor (hub,hub), need to change to no params constructor
							Parameter[] params = constructors[0].getParameters();
							if (params != null && params.length == 2 && params[0].getType().equals(Hub.class)
									&& params[1].getType().equals(Hub.class)) {
								constructors[0] = constructors[0].getDeclaringClass().getConstructor(null);
							}
						}
						CustomHubFilter customHubFilter;
						if (values[0] == null) {
							customHubFilter = (CustomHubFilter) constructors[0].newInstance(null);
						} else {
							customHubFilter = (CustomHubFilter) constructors[0].newInstance(values[0]);
						}

						OAFilter filter = new OAFilter() {
							@Override
							public boolean isUsed(Object obj) {
								return customHubFilter.isUsed(obj);
							}
						};

						OAClassFilter classFilter = customHubFilter.getClass().getAnnotation(OAClassFilter.class);

						String s = classFilter.query();
						if (OAString.isNotEmpty(s)) {
							if (OAString.isNotEmpty(query)) {
								query += " AND ";
							}
							query += s;
						}

						if (filterQuery == null) {
							filterQuery = filter;
						} else {
							filterQuery = new OAAndFilter(filterQuery, filter);
						}
					} catch (Exception e) {
						throw new IllegalArgumentException("Filter " + names[0] + " can not be created", e);
					}
				}

				OASelect select;
				if (OAString.isNotEmpty(query)) {
					select = new OASelect(clazz);
					select.setWhere(query);
					select.setFilter(filterQuery);
					h.select(select);
				} else if (OAString.isEmpty(fromClass)) {
					select = new OASelect(clazz);
					select.setFilter(filterQuery);
					h.select(select);
				} else {
					clazz = hmClassName.get(fromClass.toLowerCase());
					if (clazz == null) {
						onException(resp, new Exception("Class not found, name=" + className));
						return httpStatus;
					}
					select = new OASelect(clazz);
					select.select("id == ?", new Object[] { fromId });
					Object obj = select.next();
					if (obj != null) {
						if (fromPath.indexOf(".") < 0) {
							obj = ((OAObject) obj).getProperty(fromPath);
							if (obj instanceof Hub) {
								h = (Hub) obj;
							} else {
								h.add(obj);
							}
						} else {
							OAFinder finder = new OAFinder((OAObject) obj, fromPath);
							ArrayList al = finder.find();
							for (Object objx : al) {
								h.add(objx);
							}
						}
					}
				}

				boolean b = true;
				for (Object obj : h) {
					if (!OAObjectEditQueryDelegate.getAllowVisible(null, (OAObject) obj, null)) {
						b = false;
						httpStatus = HttpServletResponse.SC_UNAUTHORIZED;
						break;
					}

				}
				if (b) {
					jsonOutput = jaxb.convertToJSON(h);
				}
			}
		}

		if (jsonOutput == null) {
			if (httpStatus == HttpServletResponse.SC_OK) {
				httpStatus = HttpServletResponse.SC_NO_CONTENT;
			}
		} else {
			pw.write(jsonOutput);
		}

		resp.setStatus(httpStatus);
		pw.flush(); // but dont close
		return httpStatus;
	}

	// not used qqqqqqq get code for Multipart and x-www-form-urlencoded
	protected int _process(final String methodName, final String url, String contentType, final Map<String, String[]> mapParam,
			final PrintWriter printWriter, final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
		if (contentType == null) {
			contentType = "";
		} else {
			contentType = contentType.toLowerCase();
		}

		//qqqqqqqqqqqqqqqqqqqqqqq
		//        req.getReader().;

		//qqqqqqqqqqqqqq  payload, only used if not using params

		//qqqqqqqqq https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
		//        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");

		boolean bUseParams = false;
		boolean bUseReader = false;
		boolean bUseMultipart = false;
		boolean bUseInputStream = false;
		boolean bUseXML = false;
		boolean bUseJSON = false;

		//qqqqqqqqqq get User, could be null

		// need to know if req.getParameter should be used, or InputStream/Reader
		if ("GET".equalsIgnoreCase(methodName)) {
			bUseParams = true;

			// parse URL

		} else {
			// But only if the POST data is encoded as key-value pairs of
			//    content type: "application/x-www-form-urlencoded" like when you use a standard HTML form.
			if (contentType.indexOf("x-www-form-urlencoded") >= 0) {
				bUseParams = true;
			} else if (contentType.indexOf("multipart/form-data") >= 0) {
				bUseParams = true;
				bUseMultipart = true;
			} else {
				bUseInputStream = true;
			}
		}

		/* if JSON, XML, text
		 else allow inputStream to be sent to method
		 could be multi-part ????
		    multipart/form-data
		*/
		if (contentType.indexOf("xml") >= 0) {
			bUseXML = true;
			if (bUseInputStream) {
				bUseInputStream = false;
				if (!bUseParams) {
					bUseReader = true;
				}
			}
		} else if (contentType.indexOf("json") >= 0) {
			bUseJSON = true;
			if (bUseInputStream) {
				bUseInputStream = false;
				if (!bUseParams) {
					bUseReader = true;
				}
			}
		} else if (contentType.indexOf("text") >= 0) {
			if (bUseInputStream) {
				bUseInputStream = false;
				if (!bUseParams) {
					bUseReader = true;
				}
			}
		}

		LOG.fine(String.format("request methodName=%s, url=%s, contentType=%s, bUseParams=%b", methodName, url, contentType, bUseParams));

		String ignoreName = null;
		if (req != null) {
			ignoreName = req.getServletContext().getContextPath();
			String s = req.getServletPath();
			if (OAString.isNotEmpty(s)) {
				if (!s.startsWith("/")) {
					ignoreName += "/";
				}
				ignoreName += s;
			}
			if (OAString.isNotEmpty(s) && !s.endsWith("/")) {
				ignoreName += "/";
			}
		}
		if (ignoreName == null) {
			ignoreName = "/vendor-api/";
		}

		String s = url.substring(ignoreName.length());
		String requestName = OAString.field(s, "/", 1);

		LOG.fine("requestName=" + requestName);

		String argsURI = null;
		if (requestName.indexOf("?") > 0) {
			requestName = OAString.field(requestName, "?", 1);
		} else {
			argsURI = OAString.field(s, "/", 2, 99);
		}
		final int dcntURI = OAString.dcount(argsURI, "/");

		JAXBContext jaxbContext = null;
		String valString = null;

		int result = HttpServletResponse.SC_OK;
		//        result = HttpServletResponse.SC_ACCEPTED;
		//        result = HttpServletResponse.SC_NO_CONTENT;

		//        resp.setContentType("text/plain");
		/*
		    if (jaxbContext == null) {
		//                if (allXMLPackages == null) jaxbContext = JAXBContext.newInstance(objResult.getClass());
		        jaxbContext = JAXBContext.newInstance(allXMLPackages);
		    }
		
		    Marshaller marshaller = jaxbContext.createMarshaller();
		
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		    // https://timjansen.github.io/jarfiller/guide/jaxb/xmlfragments.xhtml
		    // marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		
		    StringWriter sw = new StringWriter();
		*/
		/*
		    String qname = objResult.getClass().getSimpleName();
		    qname = convertXMLQName(qname);
		
		    JAXBElement jele = new JAXBElement(new QName(qname), objResult.getClass(), objResult);
		
		    marshaller.marshal(jele, sw);
		    objResult = sw.toString();
		
		    if (allXMLPackages == null) jaxbContext = null;  // dont reuse
		*/

		//      printWriter.print(objResult + "");
		return result;
	}

	protected String convertXMLQName(String qname) {
		if (qname != null && qname.toUpperCase().endsWith("TYPE")) {
			qname = qname.substring(0, qname.length() - 4);
		}
		return qname;
	}

	// find the WSO2 set UserId from the header
	private String lastJwt;
	private String lastJwtUserId;
	private final ReadWriteLock rwLockJwt = new ReentrantReadWriteLock();

	protected String getUserId(HttpServletRequest servletRequest) {
		if (servletRequest == null) {
			return null;
		}
		final String jwt = servletRequest.getHeader("HTTP_X_JWT_ASSERTION");
		if (jwt == null) {
			return null;
		}

		try {
			rwLockJwt.readLock().lock();
			if (jwt.equals(lastJwt)) {
				return lastJwtUserId;
			}
		} finally {
			rwLockJwt.readLock().unlock();
		}

		String s = OAString.field(jwt, ".", 2);
		String sz = Base64.decode(s);
		/*
		byte[] bs = Base64.decode(s);
		String sz = new String(bs);
		*/

		/* ex:
		    http://wso2.org/claims/enduser
		    admin@carbon.user
		*/

		final JsonParser parser = Json.createParser(new StringReader(sz));
		String key = null;
		String value = null;
		while (parser.hasNext()) {
			final Event event = parser.next();
			if (event == Event.KEY_NAME) {
				key = parser.getString();
			} else if (event == Event.VALUE_STRING) {
				if ("http://wso2.org/claims/enduser".equalsIgnoreCase(key)) {
					value = parser.getString();
					break;
				}
			}
		}
		parser.close();

		try {
			rwLockJwt.writeLock().lock();
			lastJwt = jwt;
			lastJwtUserId = value;
		} finally {
			rwLockJwt.writeLock().unlock();
		}

		return value;
	}

	private String allXMLPackages;

	/**
	 * List of packages (name spaces) used for XML.
	 *
	 * @param allXMLPackages separated by ":"
	 */
	public void setAllXMLPackageNames(String allXMLPackages) {
		this.allXMLPackages = allXMLPackages;
	}

	private static final String KEY_OARestUser = "OARestUser";
	private static final String KEY_OARestUserAccess = "OARestUserAccess";

	/**
	 * This is always called first.
	 */
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		final HttpSession session = request.getSession(true);

		OAObject user = (OAObject) session.getAttribute(KEY_OARestUser);
		OAUserAccess userAccess = (OAUserAccess) session.getAttribute(KEY_OARestUserAccess);

		if (userAccess == null || user == null) {
			String userId = null;
			String pw = null;

			if (getAuthType() == AuthType.JWT) {
				String auth = getJWTHeaderName();
				if (OAString.isEmpty(auth)) {
					String jwt = request.getHeader(auth);
					if (jwt != null) {
						String s = OAString.field(jwt, ".", 2); // json data that includes user
						String sz = Base64.decode(s);

						final JsonParser parser = Json.createParser(new StringReader(sz));
						String key = null;
						String value = null;
						while (parser.hasNext()) {
							final Event event = parser.next();
							if (event == Event.KEY_NAME) {
								key = parser.getString();
							} else if (event == Event.VALUE_STRING) {
								if (key != null && key.equalsIgnoreCase(getJWTKeyName())) {
									value = parser.getString();
									break;
								}
							}
						}
						parser.close();
						if (OAString.isNotEmpty(userId)) {
							user = getJwtUser(userId);
						}
					}
				}
			} else if (getAuthType() == AuthType.HttpBasic) {
				String auth = request.getHeader("Authorization");
				if (auth != null && auth.toUpperCase().startsWith("BASIC ")) {
					String userpassEncoded = auth.substring(6);
					String s = Base64.decode(userpassEncoded);
					int pos = s.indexOf(':');
					userId = s.substring(0, pos);
					pw = s.substring(pos + 1);

					user = getUser(userId, pw);
				}
			} else if (getAuthType() == AuthType.None) {
				user = getUser(userId, pw);
			}

			if (user == null) {
				if (getAuthType() == AuthType.HttpBasic) {
					response.setHeader("WWW-Authenticate", "BASIC realm=\"OARest\"");
				}
				response.sendError(response.SC_UNAUTHORIZED);
				return;
			} else {
				session.setAttribute(KEY_OARestUser, user);
				userAccess = getUserAccess(user);
				session.setAttribute(KEY_OARestUserAccess, userAccess);
			}
		}

		try {
			OAThreadLocalDelegate.setContext(session);
			OAContext.setContext(session, user);
			OAContext.setContextUserAccess(session, userAccess);

			super.service(request, response);
		} finally {
			OAThreadLocalDelegate.setContext(null);
			OAContext.setContext(session, null);
			OAContext.setContextUserAccess(session, null);
		}
	}

	public void setAuthType(AuthType authType) {
		this.authType = authType;
	}

	public AuthType getAuthType() {
		return authType;
	}

	/**
	 * Called to get the UserAccess for OAContext.setUserContext
	 */
	protected OAUserAccess getUserAccess(Object user) {
		return null;
	}

	public String getJWTHeaderName() {
		return jwtHeaderName;
	}

	public void setJWTHeaderName(String name) {
		//ex:  "HTTP_X_JWT_ASSERTION"
		this.jwtHeaderName = name;
	}

	public String getJWTKeyName() {
		return jwtKeyName;
	}

	public void setJWTKeyName(String key) {
		this.jwtKeyName = key;
	}

	/**
	 * Should be overwritten to supply the valid user.
	 *
	 * @param userId   could be null if AuthType.None
	 * @param password could be null if AuthType.None
	 * @return User to use for setting OAContext.user
	 */
	protected OAObject getUser(String userId, String password) {
		return null;
	}

	/**
	 * Called using AuthType.JWT
	 * 
	 * @param userId from the JWT
	 * @return User to use for setting OAContext.user
	 */
	protected OAObject getJwtUser(String userId) {
		return null;
	}
}
