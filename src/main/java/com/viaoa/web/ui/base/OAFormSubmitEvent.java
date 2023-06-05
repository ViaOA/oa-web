package com.viaoa.web.ui.base;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OAFormSubmitEvent {
    private final BaseForm form;
    private final OASession session;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private BaseComponent submitComponent;
    private final Map<String, String[]> hmNameValue = new HashMap(); 
    private boolean bCancel;
    private String forwardUrl;
    
    
    private final List<String> alMessage = new ArrayList();

    public OAFormSubmitEvent(BaseForm form, OASession session, HttpServletRequest request, HttpServletResponse response) {
        this.form = form;
        this.session = session;
        this.request = request;
        this.response = response;
    }

    public Map<String, String[]> getNameValueMap() {
        return hmNameValue;
    }
    
    public void setCancel(boolean b) {
        this.bCancel = b;
    }
    public boolean getCancel() {
        return this.bCancel;
    }

    public BaseForm getForm() {
        return form;
    }

    public OASession getSession() {
        return session;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public BaseComponent getSubmitComponent() {
        return submitComponent;
    }
    public void setSubmitComponent(BaseComponent comp) {
        this.submitComponent = comp;
    }

    public String getForwardUrl() {
        return forwardUrl;
    }
    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }
    
    
}
