package com.viaoa.web.html.form;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.server.OASession;

/**
 * Event used to track Form submit and ajaxSubmit.
 *  
 */
public class OAFormSubmitEvent {
    private final OAForm form;
    private final OASession session;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private OAHtmlComponent submitOAHtmlComponent;
    private HtmlElement submitHtmlElement;
    private final Map<String, String[]> hmNameValue = new HashMap(); 
    private boolean bCancel;
    private String forwardUrl;
    private boolean bAjax;
    private int imageClickX, imageClickY;
    
    
    private final List<String> alMessage = new ArrayList();

    public OAFormSubmitEvent(OAForm form, OASession session, HttpServletRequest request, HttpServletResponse response, boolean isAjax) {
        this.form = form;
        this.session = session;
        this.request = request;
        this.response = response;
        this.bAjax = isAjax;
    }

    public Map<String, String[]> getNameValueMap() {
        return hmNameValue;
    }
    
    public void setCancel(boolean b) {
        this.bCancel = b;
    }
    public void cancel() {
        this.bCancel = true;
    }
    public boolean getCancel() {
        return this.bCancel;
    }

    public OAForm getForm() {
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

    public OAHtmlComponent getSubmitOAHtmlComponent() {
        return submitOAHtmlComponent;
    }
    public void setSubmitOAHtmlComponent(OAHtmlComponent comp) {
        this.submitOAHtmlComponent = comp;
    }

    public HtmlElement getSubmitHtmlElement() {
        return submitHtmlElement;
    }
    public void setSubmitHtmlElement(HtmlElement he) {
        this.submitHtmlElement = he;
    }
    
    public String getForwardUrl() {
        return forwardUrl;
    }
    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }
    
    public void setAjax(boolean b) {
        this.bAjax = b;
    }
    public boolean getAjax() {
        return this.bAjax;
    }

    public int getImageClickX() {
        return imageClickX;
    }
    public void setImageClickX(int x) {
        imageClickX = x;
    }
    
    public int getImageClickY() {
        return imageClickY;
    }
    public void setImageClickY(int y) {
        imageClickY = y;
    }
}
