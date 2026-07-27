package com.viaoa.web.filter;

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

import com.viaoa.secure.Base64;
import com.viaoa.session.OASessionUser;
import com.viaoa.hub.Hub;
import com.viaoa.lang.OAString;
import com.viaoa.oa.OA;
import com.viaoa.object.OAObject;

/**
 * Manages user access for OARestServlet, etc. This will manage the OAContext user and userAccess for the current thread.
 *
 * @author vvia
 */
public abstract class OAUserAccessFilter<M extends OAObject, S extends OAObject> implements Filter {

	public static final String KEY_HubModelUser = "HubModelUser";
	public static final String KEY_OASessionUser = "OASessionUser";

	private AuthType authType = AuthType.None;
	public static enum AuthType {
		None, HttpBasic, JWT;
	}

	private final OA oa;
	private String jwtHeaderName; // name of http header if using json web token for user auth.
	private String jwtKeyName; // name of http header if using json web token for user auth.

	public OAUserAccessFilter(OA oa) {
		this.oa = oa;
	}
	
	public OA getOA() {
		return this.oa;
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;

		final HttpSession session = request.getSession(true);

		Hub<M> hubModelUser = (Hub<M>) session.getAttribute(KEY_HubModelUser);
		OASessionUser<S> sessionUser = (OASessionUser<S>) session.getAttribute(KEY_OASessionUser);

		if (sessionUser == null || sessionUser.getHub() == null || sessionUser.getHub().getAO() == null) {
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

			S obj = getLoginSessionObject(userId, pw);
			
			if (obj == null) {
				if (getAuthType() == AuthType.HttpBasic) {
					response.setHeader("WWW-Authenticate", "BASIC realm=\"OAUserAccess\"");
				}
				response.sendError(response.SC_UNAUTHORIZED);
				return;
			} 

			if (sessionUser == null) sessionUser = createSessionUser(obj);
			if (hubModelUser == null) hubModelUser = createModelUserHub();

			onSetUsers(hubModelUser, sessionUser);
			
			session.setAttribute(KEY_HubModelUser, hubModelUser);
			session.setAttribute(KEY_OASessionUser, sessionUser);
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

	protected abstract S getLoginSessionObject(String userId, String password);
	
	protected abstract OASessionUser<S> createSessionUser(S sessonObj);
	protected abstract Hub<M> createModelUserHub();
	
	protected abstract void onSetUsers(Hub<M> hubModelUser, OASessionUser<S> su);

}
