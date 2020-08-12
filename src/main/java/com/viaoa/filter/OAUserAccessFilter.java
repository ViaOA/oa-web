package com.viaoa.filter;

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.viaoa.context.OAContext;
import com.viaoa.context.OAUserAccess;
import com.viaoa.object.OAObject;
import com.viaoa.object.OAThreadLocalDelegate;
import com.viaoa.util.Base64;
import com.viaoa.util.OAString;

/**
 * Manages user access for OARestServlet, etc. This will manage the OAContext user and userAccess for the current thread.
 *
 * @author vvia
 */
public abstract class OAUserAccessFilter implements Filter {

	// user for session, ex: Employee
	public static final String KEY_OAWebUser = "OAWebUser";
	// context user that is used by OAContext, ex: AppUser
	public static final String KEY_OAContextUser = "OAContextUser";
	// context user access that is used by OAContext
	public static final String KEY_OAContextUserAccess = "OAContextUserAccess";

	private AuthType authType = AuthType.None;

	public static enum AuthType {
		None, HttpBasic, JWT;
	}

	private String jwtHeaderName; // name of http header if using json web token for user auth.
	private String jwtKeyName; // name of http header if using json web token for user auth.

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		final HttpSession session = request.getSession(true);

		OAObject webUser = (OAObject) session.getAttribute(KEY_OAWebUser);
		OAObject contextUser = (OAObject) session.getAttribute(KEY_OAContextUser);
		OAUserAccess userAccess = (OAUserAccess) session.getAttribute(KEY_OAContextUserAccess);

		if (webUser == null) {
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
				}
			} else if (getAuthType() == AuthType.None) {
				// guest?
			}

			webUser = getWebUser(userId, pw);

			if (webUser == null) {
				if (getAuthType() == AuthType.HttpBasic) {
					response.setHeader("WWW-Authenticate", "BASIC realm=\"OARest\"");
				}
				response.sendError(response.SC_UNAUTHORIZED);
				return;
			} else {
				session.setAttribute(KEY_OAWebUser, webUser);

				contextUser = getContextUser(webUser);
				session.setAttribute(KEY_OAContextUser, contextUser);

				userAccess = getContextUserAccess(webUser, contextUser);
				session.setAttribute(KEY_OAContextUserAccess, userAccess);
			}
		}

		try {
			OAThreadLocalDelegate.setContext(session);
			OAContext.setContext(session, contextUser);
			OAContext.setContextUserAccess(session, userAccess);

			filterChain.doFilter(request, response);

		} finally {
			OAThreadLocalDelegate.setContext(null);
			OAContext.setContext(session, null);
			OAContext.setContextUserAccess(session, null);
		}

	}

	@Override
	public void destroy() {
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
	protected OAUserAccess getUserAccess(OAObject user) {
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

	// user Object for the web user.  Ex: Employee, Customer, AppUser
	protected abstract OAObject getWebUser(String userId, String password);

	// Context Object to use for the webUser.  Ex: AppUser
	protected abstract OAObject getContextUser(OAObject webUser);

	// get the user access object for a web user.
	protected abstract OAUserAccess getContextUserAccess(OAObject webUser, OAObject contextUser);

}
