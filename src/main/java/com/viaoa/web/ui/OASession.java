/* Copyright 1999 Vince Via vvia@viaoa.com Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed
 * to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License. */
package com.viaoa.web.ui;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.servlet.http.*;

import com.viaoa.util.*;

/**
 * Object used by one user session.
 * 
 * @see OAApplication#createSession()
 */
public class OASession extends OABase {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(OASession.class.getName());

    protected OAApplication application;

    protected transient WeakReference<HttpServletRequest> wrefRequest; // set by oaform.jsp
    protected transient WeakReference<HttpServletResponse> wrefResponse;

    protected transient ArrayList<OAForm> alBreadCrumbForm = new ArrayList<OAForm>();
    protected transient final ArrayList<OAForm> alForm = new ArrayList<OAForm>();
    protected transient final HashMap<String, OAForm> hmForm = new HashMap<>();
    protected OAForm currentForm;

    private TimeZone timeZone;

    private int jsLibrary = OAApplication.JSLibrary_JQueryUI;

    /**
     * set the preferred js library to use.
     * 
     * @param type
     *            see {@link OAApplication#JSLibrary_JQueryUI} {@link OAApplication#JSLibrary_Bootstrap}
     */
    public void setDefaultJsLibrary(int type) {
        this.jsLibrary = type;
    }

    public int getDefaultJsLibrary() {
        if (this.jsLibrary != 0 || getApplication() == null) return this.jsLibrary;
        return getApplication().getDefaultJsLibrary();
    }

    public OASession() {
    }

    public void removeAll() {
        super.removeAll();
        synchronized (alForm) {
            alForm.clear();
            hmForm.clear();
        }
        alBreadCrumbForm.clear();
    }

    public void setApplication(OAApplication app) {
        this.application = app;
    }

    public OAApplication getApplication() {
        return application;
    }

    public OAForm getCurrentForm() {
        return currentForm;
    }

    public void setCurrentForm(OAForm f) {
        currentForm = f;
        setCurrentBreadCrumbForm(f);
    }

    /**
     * This can be used to find the return to page/form.
     */
    public OAForm getPreviousForm() {
        if (currentForm == null) return null;
        int x = alBreadCrumbForm.size();
        if (x == 1) return null;
        return alBreadCrumbForm.get(x - 2);
    }

    public OAForm getReturnForm() {
        return getPreviousForm();
    }

    public void setCurrentBreadCrumbForm(OAForm f) {
        int x = alBreadCrumbForm.indexOf(f);
        if (x < 0) alBreadCrumbForm.add(f);
        else {
            while (alBreadCrumbForm.size() > (x + 1)) {
                alBreadCrumbForm.remove(x + 1);
            }
        }
    }

    public void clearBreadCrumbForms() {
        alBreadCrumbForm.clear();
    }

    public OAForm[] getBreadCrumbForms() {
        return (OAForm[]) alBreadCrumbForm.toArray(new OAForm[alBreadCrumbForm.size()]);
    }

    public OAForm createForm(String id) {
        removeForm(getForm(id));
        OAForm f = new OAForm(id, null);
        addForm(f);
        return f;
    }

    /**
     * @param id
     *            ex: "employee"
     * @param page
     *            ex: "employee.jsp"
     */
    public OAForm createForm(String id, String page) {
        removeForm(getForm(id));
        OAForm f = new OAForm(id, page);
        addForm(f);
        return f;
    }

    public void addForm(OAForm form) {
        if (form == null) return;
        String id = form.getId();
        if (id == null) return;

        if (OAString.isEmpty(form.getUrl())) {
            HttpServletRequest req = getRequest();
            if (req != null) {
                String s = req.getServletPath();
                if (s.length() > 1) {
                    if (s.charAt(0) == '/') s = s.substring(1);
                    form.setUrl(s);
                }
            }
        }

        HttpServletRequest req = getRequest();
        if (req != null && req.getParameterMap().containsKey("oadebug")) {
            form.setDebug(!form.getDebug());
        }
        
        OAForm f = getForm(id);
        if (f != null) {
            LOG.fine("replacing form, id=" + id + ", from=" + f + ", to=" + form);
            removeForm(f);
        }

        synchronized (alForm) {
            alForm.add(form);
            hmForm.put(id, form);
        }
        form.setSession(this);
    }

    public OAForm getForm(String id) {
        if (id == null) return null;
        OAForm form;
        synchronized (alForm) {
            form = hmForm.get(id);
            if (form != null) {
                for (OAForm fx : alForm) {
                    if (id.equalsIgnoreCase(fx.getId())) {
                        form = fx;
                        break;
                    }
                }
            }
        }
        if (form != null) {
            HttpServletRequest req = getRequest();
            if (req != null && req.getParameterMap().containsKey("oadebug")) {
                form.setDebug(!form.getDebug());
            }
        }
        
        return form;
    }

    public OAForm getForm(OAForm form) {
        if (form == null) return null;
        if (alForm.indexOf(form) >= 0) return form;
        OAForm f = getForm(form.getId());
        return f;
    }

    public void removeForm(OAForm form) {
        if (form != null) {
            synchronized (alForm) {
                if (alForm.remove(form)) {
                    String id = form.getId();
                    if (id != null) hmForm.remove(id);
                }
            }
        }
    }

    public boolean isReset() {
        HttpServletRequest req = getRequest();
        return req != null && req.getParameterMap().containsKey("oareset");
    }

    public HttpServletRequest getRequest() {
        if (wrefRequest == null) return null;
        HttpServletRequest req = wrefRequest.get();
        return req;
    }

    /** set by oabeans.jsp */
    public void setRequest(HttpServletRequest r) {
        if (r == null) wrefRequest = null;
        else wrefRequest = new WeakReference<HttpServletRequest>(r);
    }

    /** set by oabeans.jsp */
    public HttpServletResponse getResponse() {
        if (wrefResponse == null) return null;
        HttpServletResponse res = wrefResponse.get();
        return res;
    }

    /** set by oabeans.jsp */
    public void setResponse(HttpServletResponse r) {
        if (r == null) wrefResponse = null;
        else wrefResponse = new WeakReference<HttpServletResponse>(r);
    }

    /** For this to work oaheader.jsp needs to call setResponse() &amp; setRequest() */
    public void putCookie(String name, String value) {
        setCookie(name, value);
    }

    public void putCookie(String name, int x) {
        setCookie(name, x + "");
    }

    /**
     * For this to work oaheader.jsp needs to call setResponse() &amp; setRequest(). Uses
     * Servlet.Response to create a cookie. MaxAge is set to max.
     */
    public void setCookie(String name, String value) {
        HttpServletResponse resp = getResponse();
        if (resp == null) return;
        Cookie c = new Cookie(name, value);
        c.setMaxAge(Integer.MAX_VALUE);
        resp.addCookie(c);
    }

    /**
     * uses Servlet.Request to get a cookie. For this to work oaform.jsp &amp; oabeans.jsp need to call
     * setResponse() &amp; setRequest()
     */
    public String getCookie(String name) {
        HttpServletRequest req = getRequest();
        if (req == null) return null;
        Cookie[] cookies = req.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(name)) return cookies[i].getValue();
        }
        return null;
    }

    /** same as calling request.getHeader("User-Agent") */
    public String getBrowserName() {
        HttpServletRequest req = getRequest();
        if (req == null) return null;
        return req.getHeader("User-Agent");
    }

    // javascript data sent from browser (see OAForm)
    private String browserDate; // js Date.toString()    EEE MMM dd yyyy '00:00:00' 'GMT'Z '('z')'
    private int browserRawTzOffset; // in minutes
    private boolean browserSupportsDST;
    private String browserAcceptLanguage;

    public void setBrowserInfo(String date, int rawTzOffset, boolean bSupportsDST, String acceptLanguage) {
        if (rawTzOffset != browserRawTzOffset || bSupportsDST != browserSupportsDST) {
            timeZone = null;
        }
        this.browserDate = date;
        this.browserRawTzOffset = rawTzOffset;
        this.browserSupportsDST = bSupportsDST;
        this.browserAcceptLanguage = acceptLanguage;
    }

    private final ConcurrentHashMap<Integer, TimeZone> hmTzDst = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, TimeZone> hmTzNoDst = new ConcurrentHashMap<>();

    public TimeZone getBrowserTimeZone() {
        if (timeZone != null) return timeZone;

        final int msRawTzOffset = -(browserRawTzOffset * 60 * 1000);

        if (browserSupportsDST) {
            timeZone = hmTzDst.get(msRawTzOffset);
        }
        else {
            timeZone = hmTzNoDst.get(msRawTzOffset);
        }
        if (timeZone != null) return timeZone;

        String[] ss = TimeZone.getAvailableIDs(msRawTzOffset);
        if (ss != null && ss.length > 0) {
            TimeZone tz = null;
            for (int i = 0; ss != null && i < ss.length; i++) {
                tz = TimeZone.getTimeZone(ss[i]);
                if (tz.useDaylightTime() == browserSupportsDST) {
                    timeZone = tz;
                    break;
                }
                if (tz != null) timeZone = tz; // fallback
            }
        }

        if (timeZone != null) {
            if (browserSupportsDST) {
                hmTzDst.put(msRawTzOffset, timeZone);
            }
            else {
                hmTzNoDst.put(msRawTzOffset, timeZone);
            }
        }
        return timeZone;
    }

    protected OADateTime dtCreated;
    protected String sessionId;
    protected OADateTime dtLastAccessed;
    protected int maxInactiveSeconds;

    public OADateTime getCreated() {
        return dtCreated;
    }

    public String getSessionId() {
        return sessionId;
    }

    public OADateTime getLastAccessed() {
        return dtLastAccessed;
    }

    public int getMaxInactiveSeconds() {
        return maxInactiveSeconds;
    }

    protected void update(HttpSession session) {
        long ts = session.getCreationTime();
        this.dtCreated = new OADateTime(ts);
        this.sessionId = session.getId();
        long tsLast = session.getLastAccessedTime();
        this.dtLastAccessed = new OADateTime(tsLast);
        this.maxInactiveSeconds = session.getMaxInactiveInterval();
    }
}
