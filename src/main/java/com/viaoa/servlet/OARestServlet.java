package com.viaoa.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.viaoa.annotation.OAClassFilter;
import com.viaoa.context.OAContext;
import com.viaoa.context.OAUserAccess;
import com.viaoa.datasource.OASelect;
import com.viaoa.filter.OAAndFilter;
import com.viaoa.filter.OAUserAccessFilter;
import com.viaoa.hub.CustomHubFilter;
import com.viaoa.hub.Hub;
import com.viaoa.json.OAJson;
import com.viaoa.object.OAFinder;
import com.viaoa.object.OAObject;
import com.viaoa.object.OAObjectCallbackDelegate;
import com.viaoa.object.OAObjectInfo;
import com.viaoa.object.OAObjectInfoDelegate;
import com.viaoa.object.OAPropertyInfo;
import com.viaoa.object.OAThreadLocalDelegate;
import com.viaoa.servlet.exception.OAServletException;
import com.viaoa.util.OAConv;
import com.viaoa.util.OAFilter;
import com.viaoa.util.OAPropertyPath;
import com.viaoa.util.OAReflect;
import com.viaoa.util.OAString;

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
 * http://localhost/oarest/clients?query=company.name like 'AAA*'&orderBy=id&pp=products<br>
 * http://localhost/oarest/clients?query=company.id>?'&queryParam=1&orderBy=id&pp=products<br>
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
 * call a Remote method from a registered object:<br>
 * oarest/oaremote?remoteClassName=employeedelegate&remoteClassMethod=giveRaise<br>
 * <p>
 * Call a method on an Object:<br>
 * oarest/employee?objectMethodName=giveRaise<br>
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
	private boolean bJaxbIncludeOwnedReferences = true;

	protected HashMap<String, Object> hmRemoteObject = new HashMap<>();

	/** default setting for JAXB marshalling to use refIds */
	private boolean bJaxbUseReferences;

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

	public void setJaxbIncludeOwnedReferences(boolean b) {
		this.bJaxbIncludeOwnedReferences = b;
	}

	public boolean getJaxbIncludeOwnedReferences() {
		return bJaxbIncludeOwnedReferences;
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

				String s = oi.getPluralName().toLowerCase();
				hmClassPluralName.put(s, c);

				if (!s.equals(fn.toLowerCase() + "s")) {
					hmClassPluralName.put(fn.toLowerCase() + "s", c);
					s = ", (also: " + c.getName() + "s)";
				} else {
					s = "";
				}

				LOG.fine("adding class=" + c.getName() + ", plural name=" + oi.getPluralName() + s);
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
		} catch (OAServletException ex) {
			LOG.log(Level.FINE, "OAServletException OAREST", ex);
			resp.setStatus(ex.getHttpStatusCode());
			onException(resp, ex);
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "error processing OAREST", ex);
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
		pw.print("    \"message\": \"" + OAString.convert(exception.toString(), "\"", "") + "\"");
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

		//qqqqqqqqqqqqqqqqqqqqqqq
		final boolean bOARemote = "oaremote".equalsIgnoreCase(className);
		if (bOARemote) {
			className = null;
		}

		final String originalRequest = requestUrl + (OAString.isEmpty(httpQueryString) ? "" : ("?" + httpQueryString)); //  "http://localhost:8088/pi/api/ui/campaigns/123?filter=open&max=3&flag"

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
		boolean bUseOwned = getJaxbIncludeOwnedReferences();

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
				if (key.equalsIgnoreCase("owned")) {
					String s = req.getParameter(key);
					bUseOwned = (s == null) || OAConv.toBoolean(s);
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
		String orderBy = hmParam.get("orderBy");

		final String[] queryParams = req.getParameterValues("queryParam");

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

		if (clazz == null && !bOARemote) {
			onException(resp, new Exception("Class not found, name=" + className));
			httpStatus = HttpServletResponse.SC_BAD_REQUEST;
			return httpStatus;
		}

		OAJson oaj = null;
		if (!bOARemote) {
			oaj = new OAJson();
			oaj.addPropertyPaths(alPropertyPath);
			oaj.setIncludeOwned(bUseOwned);
			LOG.fine("clazz=" + clazz.getName());
		}

		/*was
		OAJaxb jaxb = null;
		if (!bOARemote) {
			LOG.fine("clazz=" + clazz.getName());
			jaxb = new OAJaxb<>(clazz);
			jaxb.setUseReferences(bUseRefId);
			jaxb.setIncludeGuids(false);
			jaxb.setIncludeOwned(bUseOwned);
			jaxb.setIncludeNewChangedDeletedFlags(true);

			for (String s : alPropertyPath) {
				jaxb.addPropertyPath(s);
			}
		}
		*/

		String jsonInput = null;
		byte[] bsInput = null;
		if ("application/octet-stream".equalsIgnoreCase(req.getContentType())) {
			int length = req.getContentLength();
			bsInput = new byte[length];
			req.getInputStream().read(bsInput);
		} else {
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
		}

		String jsonOutput = null;

		// name in method to call
		final String objectMethodName = hmParam.get("objectMethodName".toLowerCase());

		if (bOARemote) {
			// ../oaremote?remoteClass=asdfa&remoteMethod=asdfs&pp=asd.ewr.wers
			String remoteClassName = hmParam.get("remoteClassName".toLowerCase());
			String remoteMethodName = hmParam.get("remoteMethodName".toLowerCase());

			Object objResult = callRemoteMethod(remoteClassName, remoteMethodName, jsonInput, bsInput);

			/*was
			jaxb = null;
			if (objResult instanceof OAObject) {
				jaxb = new OAJaxb<>(objResult.getClass());
			} else if (objResult instanceof Hub) {
				jaxb = new OAJaxb<>(((Hub) objResult).getObjectClass());
			}
			*/

			if (oaj != null) {
				if (objResult instanceof OAObject) {
					jsonOutput = oaj.write((OAObject) objResult);
				} else if (objResult instanceof Hub) {
					jsonOutput = oaj.write(objResult);
				} else {
					jsonOutput = oaj.write((Hub) objResult);
				}
			}

			/*was:
			if (jaxb != null) {
				jaxb.setIncludeNewChangedDeletedFlags(true);
				jaxb.setUseReferences(bUseRefId);
				jaxb.setIncludeGuids(false);
				jaxb.setIncludeOwned(bUseOwned);
				for (String s : alPropertyPath) {
					jaxb.addPropertyPath(s);
				}
			
				if (objResult instanceof OAObject) {
					jsonOutput = jaxb.convertToJSON((OAObject) objResult);
				} else {
					jsonOutput = jaxb.convertToJSON((Hub) objResult);
				}
			} else {
				jsonOutput = OAJsonMapper.convertObjectToJson(objResult);
			}
			*/
		} else if ("put".equalsIgnoreCase(methodType) && !bIsMany) {
			// ========== PUT ===========

			/*was: qqqq need to do this
			jaxb.setLoadingMode(OAJaxb.LoadingMode.UpdateRootOnly);
			
			OAObject obj = (OAObject) jaxb.convertFromJSON(jsonInput);
			*/

			OAObject obj = (OAObject) oaj.readObject(jsonInput, clazz, true);

			if (obj != null) {
				obj.save();
				jsonOutput = oaj.write((OAObject) obj);
				// /was: jsonOutput = jaxb.convertToJSON((OAObject) obj);
			} else {
				httpStatus = HttpServletResponse.SC_NOT_FOUND;
			}
		} else if (objectMethodName != null) {
			Object objResult = callObjectMethod(clazz, pathInfo, objectMethodName, jsonInput);
			jsonOutput = oaj.write(objResult);
			//was: jsonOutput = OAJsonMapper.convertObjectToJson(objResult);

		} else if ("post".equalsIgnoreCase(methodType) && !bIsMany) {
			// ========== POST ===========
			//was: qqqqq ??need to do this: jaxb.setLoadingMode(OAJaxb.LoadingMode.CreateNewRootOnly);

			OAObject obj = (OAObject) oaj.readObject(jsonInput, clazz, true);
			//was: OAObject obj = (OAObject) jaxb.convertFromJSON(jsonInput);

			if (obj != null) {
				obj.save();
			}

			jsonOutput = oaj.write((OAObject) obj);
			//was: jsonOutput = jaxb.convertToJSON((OAObject) obj);
		} else if ("delete".equalsIgnoreCase(methodType) && !bIsMany) {
			// ========== DELETE =========== qqqqqqqqqqq todo qqqqqqqqqq
			// call objectCallback to validate qqqqqqqqq
		} else if ("get".equalsIgnoreCase(methodType)) {
			// ========== GET ===========
			final String id = OAString.field(pathInfo, "/", 3);
			if (!bIsMany || OAString.isNotEmpty(id)) {
				Object obj = null;

				// might be multipart id
				OAObjectInfo oi = OAObjectInfoDelegate.getOAObjectInfo(clazz);
				String sql = "";

				ArrayList<String> al = new ArrayList();
				for (String idName : oi.getIdProperties()) {
					OAPropertyInfo pi = oi.getPropertyInfo(idName);
					String s = OAString.field(pathInfo, "/", 3 + al.size());

					if (OAString.isEmpty(s)) {
						if (al.size() > 0) {
							s = al.get(0); // see if it's one field separated by '-'
							s = OAString.field(s, "-", al.size() + 1);
						}
						if (OAString.isEmpty(s)) {
							sql = null;
							break;
						}
					}
					al.add(s);
					if (OAString.isNotEmpty(sql)) {
						sql += " AND ";
					}
					sql += pi.getName() + " = ?";
				}
				if (OAString.isNotEmpty(sql)) {
					OASelect sel = new OASelect(clazz);
					sel.select(sql, al.toArray(new String[0]));
					obj = sel.next();
				}

				if (obj != null) {
					if (!OAObjectCallbackDelegate.getAllowVisible(null, (OAObject) obj, null)) {
						httpStatus = HttpServletResponse.SC_UNAUTHORIZED;
					} else {
						jsonOutput = oaj.write((OAObject) obj);
						//was: jsonOutput = jaxb.convertToJSON((OAObject) obj);
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
					select.setParams(queryParams);
					select.setFilter(filterQuery);
					select.setOrder(orderBy);
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
					if (!OAObjectCallbackDelegate.getAllowVisible(null, (OAObject) obj, null)) {
						b = false;
						httpStatus = HttpServletResponse.SC_UNAUTHORIZED;
						break;
					}

				}
				if (b) {
					jsonOutput = oaj.write(h);
					//was: jsonOutput = jaxb.convertToJSON(h);
				}
			}
		}

		if (jsonOutput == null) {
			if (httpStatus == HttpServletResponse.SC_OK) {
				httpStatus = HttpServletResponse.SC_NOT_FOUND;
			}
		} else {
			pw.write(jsonOutput);
		}

		resp.setStatus(httpStatus);
		pw.flush(); // but dont close
		return httpStatus;
	}

	// not used
	// todo: qqqqqqq get code for Multipart and x-www-form-urlencoded
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

	private String allXMLPackages;

	/**
	 * List of packages (name spaces) used for XML.
	 *
	 * @param allXMLPackages separated by ":"
	 */
	public void setAllXMLPackageNames(String allXMLPackages) {
		this.allXMLPackages = allXMLPackages;
	}

	/**
	 * This is always called first.
	 */
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		final HttpSession session = request.getSession(true);

		// verify that user has been set
		OAObject user = (OAObject) session.getAttribute(OAUserAccessFilter.KEY_OAWebUser);

		if (user == null) {
			throw new RuntimeException(
					"Session web user (" + OAUserAccessFilter.KEY_OAWebUser + ") is not defined");
		}

		OAObject user2 = OAContext.getContextObject();
		if (user != user2) {
			throw new RuntimeException("Session context user (" + OAUserAccessFilter.KEY_OAContextUser + ") is not defined");
		}

		super.service(request, response);
	}

	protected Object callObjectMethod(final Class clazz, final String pathInfo, final String methodName, final String jsonBody)
			throws Exception {

		//	id could be multipart with "-" seperator

		final String id = OAString.field(pathInfo, "/", 3);

		Object obj = null;

		// might be multipart id
		OAObjectInfo oi = OAObjectInfoDelegate.getOAObjectInfo(clazz);
		String sql = "";

		ArrayList<String> al = new ArrayList();
		for (OAPropertyInfo pi : oi.getPropertyInfos()) {
			if (!pi.getId()) {
				continue;
			}
			String s = OAString.field(pathInfo, "/", 3 + al.size());

			if (OAString.isEmpty(s)) {
				if (al.size() > 0) {
					s = al.get(0); // see if it's one field separated by '-'
					s = OAString.field(s, "-", al.size() + 1);
				}
				if (OAString.isEmpty(s)) {
					sql = null;
					break;
				}
			}
			al.add(s);
			if (OAString.isNotEmpty(sql)) {
				sql += " AND ";
			}
			sql += pi.getName() + " = ?";
		}
		if (OAString.isNotEmpty(sql)) {
			OASelect sel = new OASelect(clazz);
			sel.select(sql, al.toArray(new String[0]));
			obj = sel.next();
		}

		int httpStatus;

		if (obj == null) {
			String s = String.format(	"Object not found, class=%s, method=%s, id=%s, sql to find it=%s",
										clazz.getSimpleName(), methodName, id, sql);
			throw new OAServletException(s, HttpServletResponse.SC_NOT_FOUND, null);
		}

		if (!OAObjectCallbackDelegate.getAllowVisible(null, (OAObject) obj, methodName)) {
			String s = String.format(	"method not authorized (visible=false), class=%s, method=%s, id=%s",
										clazz.getSimpleName(), methodName, id);
			throw new OAServletException(s, HttpServletResponse.SC_UNAUTHORIZED, null);
		}

		Method method = OAObjectInfoDelegate.getMethod(oi, methodName);

		if (method == null) {
			throw new RuntimeException("method " + methodName + " not found in class " + clazz.getSimpleName());
		}

		Object[] args = OAJson.convertJsonToMethodArguments(jsonBody, method);

		Object objResult = method.invoke(obj, args);
		return objResult;
	}

	protected Object callRemoteMethod(final String className, final String methodName, final String jsonBody, final byte[] bsBody)
			throws Exception {

		Object obj = hmRemoteObject.get(className.toUpperCase());

		if (obj == null) {
			throw new OAServletException("remote object for className=" + className + " not found", HttpServletResponse.SC_NOT_FOUND);
		}

		Method method = OAReflect.getMethod(obj.getClass(), methodName);

		int[] iSkip = null;
		//qqqqqqqqqqqqqqqqqqqq
		/* todo:  find out which method params to skip qqqqqqqqqqq
				Parameter[] ps = method.getParameters();
				if (ps != null) {
					for (Parameter p : ps) {
						if (s.equals(p.getType()))
					}
				}
				*/

		// OAJson oajson;
		// OAJsonArrayNode nodeArray;
		OAJson oaj;
		ArrayNode nodeArray;

		if (OAString.isNotEmpty(jsonBody)) {
			oaj = new OAJson();
			ObjectMapper om = oaj.createObjectMapper();
			nodeArray = om.createArrayNode();
			/*
			oajson = new OAJson();
			nodeArray = oajson.loadArray(jsonBody);
			*/
		} else {
			oaj = null;
			nodeArray = null;
		}

		Object[] objs;
		if (bsBody != null) {
			Parameter[] mps = method.getParameters();
			if (mps == null) {
				return null;
			}
			objs = new Object[mps.length];
			int i = -1;
			for (Parameter param : mps) {
				i++;
				Class paramClass = param.getType();
				if (!paramClass.isArray() || !paramClass.getComponentType().equals(byte.class)) {
					continue;
				}
				objs[i] = bsBody;
				break;
			}
		} else {
			objs = OAJson.convertJsonToMethodArguments(nodeArray, method, iSkip);
		}

		Object result = method.invoke(obj, objs);

		return result;
	}

	public void registerRemoteObject(Object obj) throws Exception {
		//qqqqqqqqqq need to load meta data ... *Info classes ... to get which method params are used qqqqqqqqqqqqq
		if (obj == null) {
			throw new NullPointerException("remote is required");
		}
		hmRemoteObject.put(obj.getClass().getSimpleName().toUpperCase(), obj);

		Class[] cs = obj.getClass().getInterfaces();
		if (cs != null) {
			for (Class c : cs) {
				hmRemoteObject.put(c.getSimpleName().toUpperCase(), obj);
			}
		}

	}

}
