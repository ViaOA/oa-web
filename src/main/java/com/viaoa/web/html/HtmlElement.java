package com.viaoa.web.html;

import static com.viaoa.web.html.OAHtmlComponent.CursorType;
import static com.viaoa.web.html.OAHtmlComponent.EventType;
import static com.viaoa.web.html.OAHtmlComponent.OverflowType;

import java.io.OutputStream;
import java.util.*;

import com.viaoa.object.OAObject;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.server.OASession;

/**
 * Base class for all HTML Elements.
 * <p>
 * This uses an internal OAHtmlComponent, and overwrites some of it's public methods to call
 * protected methods in this class (for method "hiding").
 * 
 */
public class HtmlElement {
    // use this to manage any of the HTML elements
    protected final OAHtmlComponentPlus htmlComponent;
    private List<HtmlElement> alHtmlElement;
    
    public HtmlElement(String id) {
        this.htmlComponent = new OAHtmlComponentPlus(id) {
            /**
             * Override all of these public methods and have them use the protected method in this class.
             */
            
            @Override
            public void onSubmitPrecheck(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitPrecheck(formSubmitEvent);
                if (formSubmitEvent.getSubmitOAHtmlComponent() == HtmlElement.this.htmlComponent) {
                    formSubmitEvent.setSubmitHtmlElement(HtmlElement.this);;
                }
                HtmlElement.this.onSubmitPrecheck(formSubmitEvent);
            }

            @Override
            public void onSubmitBeforeLoadValues(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitBeforeLoadValues(formSubmitEvent);
                HtmlElement.this.onSubmitBeforeLoadValues(formSubmitEvent);
            }
            
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitLoadValues(formSubmitEvent);
                HtmlElement.this.onSubmitLoadValues(formSubmitEvent);
            }
            
            @Override
            public void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitAfterLoadValues(formSubmitEvent);
                HtmlElement.this.onSubmitAfterLoadValues(formSubmitEvent);
            }
            
            
            @Override
            public void onSubmit(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmit(formSubmitEvent);
                HtmlElement.this.onSubmit(formSubmitEvent);
            }

            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitCompleted(formSubmitEvent);
                HtmlElement.this.onSubmitCompleted(formSubmitEvent);
            }
            
            // ==============
            
            @Override
            public void beforePageLoad() {
                super.beforePageLoad();
                HtmlElement.this.beforePageLoad();
            }
            
            @Override
            public void afterPageLoad() {
                super.afterPageLoad();
                HtmlElement.this.afterPageLoad();
            }

            // ==============
            
            @Override
            public String onGetJson(OASession session) {
                return HtmlElement.this.onGetJson(session);
            }

            // ==============

            @Override
            public void beforeGetScript() {
                HtmlElement.this.beforeGetScript();
            }
            
            @Override
            public String getInitializeScript() {
                String js = HtmlElement.this.getInitializeScript();
                String js2 = super.getInitializeScript();
                if (js == null) return js2;
                if (js2 == null) return js;
                return js2 + "\n" + js;
            }

            @Override
            public String getVerifyScript() {
                String js = HtmlElement.this.getVerifyScript();
                String js2 = super.getVerifyScript();
                if (js == null) return js2;
                if (js2 == null) return js;
                return js2 + "\n" + js;
            }

            @Override
            public String getAjaxScript(final boolean bIsInitializing) {
                String js = HtmlElement.this.getAjaxScript(bIsInitializing);
                String js2 = super.getAjaxScript(bIsInitializing);
                if (js == null) return js2;
                if (js2 == null) return js;
                return js2 + "\n" + js;
            }
            
            @Override
            public OutputStream onSubmitGetFileOutputStream(OAFormSubmitEvent formSubmitEvent, String fileName, String contentType) {
                OutputStream os = super.onSubmitGetFileOutputStream(formSubmitEvent, fileName, contentType);
                OutputStream os2 = HtmlElement.this.onSubmitGetFileOutputStream(fileName, contentType);
                if (os2 != null) os = os2;
                return os;
            }
            
            @Override
            public void getRequiredCssNames(final Set<String> hsCssName) {
                super.getRequiredCssNames(hsCssName);
                HtmlElement.this.getRequiredCssNames(hsCssName);
            }
            @Override
            public void getRequiredJsNames(final Set<String> hsJsName) {
                super.getRequiredJsNames(hsJsName);
                HtmlElement.this.getRequiredJsNames(hsJsName);
            }
            
            @Override
            public boolean isSupported(String name) {
                return HtmlElement.this.isSupported(name);
            }
        };
    }

    /**
     * Internal component used bo control any/all HTML elements, attributes, etc.
     */
    public OAHtmlComponent getOAHtmlComponent() {
        return htmlComponent;
    }
    
    public OAForm getForm() {
        return htmlComponent.getForm();
    }

    public void setForm(OAForm form) {
        htmlComponent.setForm(form);
    }
    
    public String getId() {
        return htmlComponent.getId();
    }

    public boolean getHidden() {
        return htmlComponent.getHidden();
    }
    public boolean isHidden() {
        return htmlComponent.getHidden();
    }
    public void setHidden(boolean b) {
        htmlComponent.setHidden(b);
    }

    public boolean getVisible() {
        return htmlComponent.getVisible();
    }
    public boolean isVisible() {
        return htmlComponent.getVisible();
    }
    public void setVisible(boolean b) {
        htmlComponent.setVisible(b);
    }

    public String getForwardUrl() {
        return htmlComponent.getForwardUrl();
    }

    public void setForwardUrl(String forwardUrl) {
        htmlComponent.setForwardUrl(forwardUrl);
    }

    public boolean getSubmit() {
        return htmlComponent.getSubmit();
    }

    public void setSubmit(boolean b) {
        htmlComponent.setSubmit(b);
    }

    public boolean getAjaxSubmit() {
        return htmlComponent.getAjaxSubmit();
    }

    public void setAjaxSubmit(boolean b) {
        htmlComponent.setAjaxSubmit(b);
    }

    public String getToolTip() {
        return htmlComponent.getToolTipText();
    }

    public void setToolTip(String tooltip) {
        htmlComponent.setToolTipText(tooltip);
    }

    public String getToolTipText() {
        return htmlComponent.getToolTipText();
    }

    public void setToolTipText(String tooltip) {
        htmlComponent.setToolTipText(tooltip);
    }

    public boolean getPlainText() {
        return htmlComponent.getPlainText();
    }

    public boolean isPlainText() {
        return htmlComponent.getPlainText();
    }

    public void setPlainText(boolean b) {
        htmlComponent.setPlainText(b);
    }

    public String getTitle() {
        return htmlComponent.getTitle();
    }

    public void setTitle(String title) {
        htmlComponent.setTitle(title);
    }

    
    
    public String getStyle(String name) {
        return htmlComponent.getStyle(name);
    }

    public List<String> getStyles() {
        return htmlComponent.getStyles();
    }
    
    public void addStyle(String name, String value) {
        htmlComponent.addStyle(name, value);
    }

    public void removeStyle(String name) {
        htmlComponent.removeStyle(name);
    }

    public List<String> getClasses() {
        return htmlComponent.getClasses();
    }
    public void addClass(String name) {
        htmlComponent.addClass(name);
    }

    public void removeClass(String name) {
        htmlComponent.removeClass(name);
    }

    
    public String getConfirmMessage() {
        return htmlComponent.getConfirmMessage();
    }

    public void setConfirmMessage(String msg) {
        htmlComponent.setConfirmMessage(msg);
    }

    public String getHeight() {
        return htmlComponent.getHeight();
    }

    public void setHeight(String val) {
        htmlComponent.setHeight(val);
    }

    public String getWidth() {
        return htmlComponent.getWidth();
    }

    public void setWidth(String val) {
        htmlComponent.setWidth(val);
    }
    
    public String getMinHeight() {
        return htmlComponent.getMinHeight();
    }

    public void setMinHeight(String val) {
        htmlComponent.setMinHeight(val);
    }

    public String getMinWidth() {
        return htmlComponent.getMinWidth();
    }

    public void setMinWidth(String val) {
        htmlComponent.setMinWidth(val);
    }

    public String getMaxHeight() {
        return htmlComponent.getMaxHeight();
    }

    public void setMaxHeight(String val) {
        htmlComponent.setMaxHeight(val);
    }

    public String getMaxWidth() {
        return htmlComponent.getMaxWidth();
    }

    public void setMaxWidth(String val) {
        htmlComponent.setMaxWidth(val);
    }

    public String getOverflow() {
        return htmlComponent.getOverflow();
    }

    public void setOverflow(String overflow) {
        htmlComponent.setOverflow(overflow);
    }

    public void setOverflow(OverflowType overflowType) {
        htmlComponent.setOverflow(overflowType);
    }

    public String getEventName() {
        return htmlComponent.getEventName();
    }

    public void setEventName(String name) {
        htmlComponent.setEventName(name);
    }

    public void setEventType(EventType eventType) {
        htmlComponent.setEventType(eventType);
    }

    public String getCursor() {
        return htmlComponent.getCursor();
    }

    public void setCursor(String cursorName) {
        htmlComponent.setCursor(cursorName);
    }

    public void setCursor(CursorType cursorType) {
        setCursor(cursorType == null ? CursorType.Default.getDisplay() : cursorType.getDisplay());
    }

    public String getInnerHtml() {
        return htmlComponent.getInnerHtml();
    }

    public void setInnerHtml(String html) {
        htmlComponent.setInnerHtml(html);
    }
    
    public char getAccessKey() {
        return htmlComponent.getAccessKey();
    }

    public void setAccessKey(char ch) {
        htmlComponent.setAccessKey(ch);
    }
    
    public int getTabIndex() {
        return htmlComponent.getTabIndex();
    }

    public void setTabIndex(int val) {
        htmlComponent.setTabIndex(val);
    }
    

    public boolean getDebug() {
        return htmlComponent.getDebug();
    }
    public void setDebug(boolean b) {
        htmlComponent.setDebug(b);
    }

    public boolean isChanged() {
        return htmlComponent.isChanged();
    }

    protected String getRenderHtml(OAObject obj) {
        return htmlComponent.getEditorHtml(obj);
    }

    public boolean getNeedsReloaded() {
        return htmlComponent.getNeedsRefreshed();
    }

    
    // These are all originated from OAForm --------------------------

    // called before calling getInitializeScript, and before ajaxScript (when not initializing)
    protected void beforeGetScript() {
    }
    
    protected String getInitializeScript() {
        return null;
    }
    protected String getVerifyScript() {
        return null;
    }

    protected String getAjaxScript(final boolean bIsInitializing) {
        return null;
    }
    
    
    protected void beforePageLoad() {
    }
    
    // called by OAForm
    protected void afterPageLoad() {
    }

    
    protected void getRequiredCssNames(final Set<String> hsCssName) {
    }
    protected void getRequiredJsNames(final Set<String> hsJsName) {
    }
    

    protected void onSubmitPrecheck(OAFormSubmitEvent formSubmitEvent) {
    }    
    protected void onSubmitBeforeLoadValues(OAFormSubmitEvent formSubmitEvent) {
    }
    protected void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
    }
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
    }
    
    
    
    /**
     * Only called for the component that submitted.
     */
    protected void onSubmit(OAFormSubmitEvent formSubmitEvent) {
    }
    
    protected void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
    }

    protected OutputStream onSubmitGetFileOutputStream(String fileName, String contentType) {
        return null;
    }

    protected String onGetJson(OASession session)  {
        return null;
    }

    

    /**
     * Embedded HtmlComponents that are not added to OAForm, but
     * are used inside a container component.  ex: OAHtmlTable has internal components for the colums.
     */
    public void add(HtmlElement he) {
        if (he == null) return;
        if (alHtmlElement == null) alHtmlElement = new ArrayList<>();
        if (!alHtmlElement.contains(he)) alHtmlElement.add(he);
    }
    public void remove(HtmlElement he) {
        if (he == null) return;
        if (alHtmlElement == null) return;
        alHtmlElement.remove(he);
    }
    public List<HtmlElement> getHtmlElements() {
        return alHtmlElement;
    }
    
    
    
    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        hsSupported.add("id");
        hsSupported.add("hidden");
        hsSupported.add("visible");
        hsSupported.add("forwardurl");
        hsSupported.add("submit");
        hsSupported.add("ajaxsubmit");
        hsSupported.add("tooltip");
        hsSupported.add("tooltiptext");
        hsSupported.add("tooltiptemplate");
        hsSupported.add("tooltiptexttemplate");
        hsSupported.add("plaintext");
        hsSupported.add("title");
        hsSupported.add("style");
        hsSupported.add("class");
        hsSupported.add("confirmmessage");
        hsSupported.add("confirmmessagetemplate");
        hsSupported.add("height");
        hsSupported.add("width");
        hsSupported.add("minheight");
        hsSupported.add("minwidth");
        hsSupported.add("maxheight");
        hsSupported.add("maxwidth");
        hsSupported.add("overflow");
        hsSupported.add("eventname");
        hsSupported.add("cursor");
        hsSupported.add("innerhtml");
        hsSupported.add("accesskey");
        hsSupported.add("tabindex");
        hsSupported.add("debug");
    }
    
    /**
     * Overwritten from OAHtmlComponent.isSupported (public method) to call this (as protected).
     */
    protected boolean isSupported(String name) {
        if (name == null) return false;
        return hsSupported.contains(name.toLowerCase());
    }
}
