package com.viaoa.web.html;

import static com.viaoa.web.html.OAHtmlComponent.CursorType;
import static com.viaoa.web.html.OAHtmlComponent.EventType;
import static com.viaoa.web.html.OAHtmlComponent.OverflowType;

import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.*;

import com.viaoa.object.OAObject;
import com.viaoa.util.OAStr;
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

    private HtmlElement heParent;
    private List<HtmlElement> alChildren;
    
    private boolean bLazyLoad;
    protected String templateName;
    protected String templateUrl;
    

    public HtmlElement(String elementIdentifier) {
        this.htmlComponent = new OAHtmlComponentPlus(elementIdentifier) {
            /**
             * Override all of these public methods and have them use the protected method in this class.
             */
            
            @Override
            public void onSubmitPrecheck(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitPrecheck(formSubmitEvent);
                if (formSubmitEvent.getSubmitOAHtmlComponent() == HtmlElement.this.htmlComponent) {
                    formSubmitEvent.setSubmitHtmlElement(HtmlElement.this);;
                }
                //qqqqqq HtmlElement.this.onSubmitPrecheck(formSubmitEvent);
            }

            @Override
            public void onSubmitBeforeLoadValues(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitBeforeLoadValues(formSubmitEvent);
                //qqqqqq HtmlElement.this.onSubmitBeforeLoadValues(formSubmitEvent);
            }
            
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitLoadValues(formSubmitEvent);
                //qqqqqq HtmlElement.this.onSubmitLoadValues(formSubmitEvent);
            }
            
            @Override
            public void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitAfterLoadValues(formSubmitEvent);
              //qqqqqq HtmlElement.this.onSubmitAfterLoadValues(formSubmitEvent);
            }
            
            
            @Override
            public void onSubmit(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmit(formSubmitEvent);
              //qqqqqq HtmlElement.this.onSubmit(formSubmitEvent);
            }

            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitCompleted(formSubmitEvent);
              //qqqqqq HtmlElement.this.onSubmitCompleted(formSubmitEvent);
            }
            
            // ==============
            
            @Override
            public void beforePageLoad() {
                super.beforePageLoad();
              //qqqqqq HtmlElement.this.beforePageLoad();
            }
            
            @Override
            public void afterPageLoad() {
                super.afterPageLoad();
              //qqqqqq  HtmlElement.this.afterPageLoad();
            }

            // ==============
            
            @Override
            public String onGetJson(OASession session) {
                return HtmlElement.this.onGetJson(session);
            }

            // ==============

            @Override
            public void beforeGetScript() {
              //qqqqqq  HtmlElement.this.beforeGetScript();
            }
            
/*was qqqqqqqqqqq            
            @Override
            public String getInitializeScript() {
                String js = HtmlElement.this.getInitializeScript();
                String js2 = super.getInitializeScript();
                if (js == null) return js2;
                if (js2 == null) return js;
                return js2 + "\n" + js;
            }
*/
            
/*qqqq            
            @Override
            public String getVerifyScript() {
                String js = HtmlElement.this.getVerifyScript();
                String js2 = super.getVerifyScript();
                if (js == null) return js2;
                if (js2 == null) return js;
                return js2 + "\n" + js;
            }
*/            
/* was qqqqqqqqq
            @Override
            public String getAjaxScript(final boolean bIsInitializing) {
                String js = HtmlElement.this.getAjaxScript(bIsInitializing);
                String js2 = super.getAjaxScript(bIsInitializing);
                if (js == null) return js2;
                if (js2 == null) return js;
                return js2 + "\n" + js;
            }
  */          
            @Override
            public OutputStream onSubmitGetFileOutputStream(OAFormSubmitEvent formSubmitEvent, String fileName, String contentType) {
                OutputStream os = super.onSubmitGetFileOutputStream(formSubmitEvent, fileName, contentType);
              //qqqqqq OutputStream os2 = HtmlElement.this.onSubmitGetFileOutputStream(fileName, contentType);
              //qqqqqq if (os2 != null) os = os2;
              //qqqqqq  return os;
                return null;
            }
            
            @Override
            public void getRequiredCssNames(final Set<String> hsCssName) {
                super.getRequiredCssNames(hsCssName);
              //qqqqqq HtmlElement.this.getRequiredCssNames(hsCssName);
            }
            @Override
            public void getRequiredJsNames(final Set<String> hsJsName) {
                super.getRequiredJsNames(hsJsName);
              //qqqqqq HtmlElement.this.getRequiredJsNames(hsJsName);
            }
            
            @Override
            public boolean isSupported(String name) {
                return HtmlElement.this.isSupported(name);
            }
            
            @Override
            protected String getJavaScriptForGetElement() {
                return HtmlElement.this.getJavaScriptForGetElement();
            }

            @Override
            public String getDataOANameFullPath() {
                return HtmlElement.this.getDataOANameFullPath();
            }
        };
        
        Class c = this.getClass();

        for (; c!=null || !c.equals(Object.class); c=c.getSuperclass()) {
            String s = c.getPackage().getName();
            if (s.startsWith("com.viaoa.web.")) {
                s = c.getSimpleName();
                if (OAStr.isNotEmpty(s)) {
                    int x = c.getModifiers();
                    if (!Modifier.isFinal(x) && !Modifier.isPrivate(x)) {
                        break;
                    }
                }
            }
        }
        String s = c.getSimpleName();
/*qqqqq        
        if (OAStr.isEmpty(s)) {
            c = c.getSuperclass();
            s = c.getSimpleName();
        }
*/        
        this.htmlComponent.setComponentClassName(s);
    }

    private boolean bTemplateSent;

    /**
     * 
     * @param hsVars list of JS variables needed.
     * @param bHasChanges true if this component knows it needs to have JS vars: comp and ele set up.
     */
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        String js = null;
        if (!getOAHtmlComponent().isInitialized()) {
            if (getLazyLoad()) {
                js = OAStr.concat(js, "new OA.OAVisibleObserverElement(ele, "+getOAHtmlComponent().getServerAssignedId()+");\n", "\n");
            }
        }

        if (OAStr.isNotEmpty(getTemplateURL()) && OAStr.isNotEmpty(getTemplateName())) {
            //was: if (!getLazyLoad() && !bTemplateSent) {
            if (!bTemplateSent) {
                String s = "await OAClient.loadTemplateFromServer(ele, '" + getTemplateURL() + "', '" + getTemplateName() + "');\n";        
                js = OAStr.concat(js, s, "\n");
                bTemplateSent = true;
            }
        }
        
        bHasChanges |= OAStr.isNotEmpty(js);
        String s = getOAHtmlComponent().getJavaScriptForClient(hsVars, bHasChanges);
        s = OAStr.concat(s, js, "\n");
        
        return s;
    }
    
    
    public String getJavaScriptForClientRecursive(final Set<String> hsVars) {
        StringBuilder sb = new StringBuilder();
        String js = getJavaScriptForClient(hsVars, false);
        if (OAStr.isNotEmpty(js)) sb.append(js);
        
//js = getVerifyScript();
//if (OAStr.isNotEmpty(js)) sb.append(js);
        
        List<HtmlElement> al = getChildren();
        if (al != null && !getLazyLoad()) {
            for (HtmlElement he : al.toArray(new HtmlElement[0])) {
                js = he.getJavaScriptForClientRecursive(hsVars);
                if (OAStr.isNotEmpty(js)) {
                    sb.append(js);
                }
            }
        }
        return sb.toString();
    }
    

    
    
    
    public String getHtmlSelector() {
        return htmlComponent.getHtmlSelector();
    }
    public void setSelector(String sel) {
        htmlComponent.setHtmlSelector(sel);
    }

    public String getId() {
        return htmlComponent.getId();
    }
    public void setId(String id) {
        htmlComponent.setId(id);
    }

    public String getDataOAName() {
        return htmlComponent.getDataOAName();
    }
    public void setDataOAName(String dn) {
        htmlComponent.setDataOAName(dn);
    }
    
    public String getDataOANameFullPath() {
        String dn = "";
        HtmlElement he = this;
        for (;he != null; he=he.getParent()) {
            dn = OAStr.concat(he.getDataOAName(), dn, ".");
        }
        return dn;
    }
    
    
    public String getComponentClassName() {
        return htmlComponent.getComponentClassName();
    }
    public void setComponentClassName(String ccn) {
        htmlComponent.setComponentClassName(ccn);
    }

    public void setTemplate(String url, String name) {
        setTemplateURL(url);
        setTemplateName(name);
    }
    
    public void setTemplateName(String name) {
        this.templateName = name;
    }
    public String getTemplateName() {
        return this.templateName;
    }
    public void setTemplateURL(String url) {
        this.templateUrl = url;
    }
    public String getTemplateURL() {
        return this.templateUrl;
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
//    protected void beforeGetScript() {
//    }

    //qqqqqqqqqqq temp ... remove after new OApp class has access to package protected
public void beforeGetJavaScriptForClient() {

}

public void beforeGetJavaScriptForClientRecursive() {
    beforeGetJavaScriptForClient();
    
    List<HtmlElement> al = getChildren();
    if (al != null) {
        for (HtmlElement he : al) {
            he.beforeGetJavaScriptForClient();
        }
    }
}



/*qqqq was
    protected String getInitializeScript() {
        return null;
    }
*/    
    protected String getVerifyScriptXX() {
//qqqqqqqqq get the getJavaScriptForClient .. call this        
        return null;
    }

/*qqq was    
    protected String getAjaxScript(final boolean bIsInitializing) {
        return null;
    }
*/    
    
/*    
    protected void beforePageLoad() {
    }
    
    // called by OAForm
    protected void afterPageLoad() {
    }
*/
    
    
/*qqqqqqqqq from old ... will want to have recursive methods
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
    
    
    / **
     * Only called for the component that submitted.
     * /
    protected void onSubmit(OAFormSubmitEvent formSubmitEvent) {
    }
    
    protected void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
    }

    protected OutputStream onSubmitGetFileOutputStream(String fileName, String contentType) {
        return null;
    }
****/    

    protected String onGetJson(OASession session)  {
        return null;
    }

    

    /**
     * Embedded HtmlComponents that are not added to OAForm, but
     * are used inside a container component.  ex: OAHtmlTable has internal components for the columns.
     */
    public void add(HtmlElement he) {
        if (he == null) return;
        if (alChildren == null) alChildren = new ArrayList<>();
        if (!alChildren.contains(he)) alChildren.add(he);
        if (he.getParent() != this) he.setParent(this);
    }
    public void remove(HtmlElement he) {
        if (he == null) return;
        if (alChildren == null) return;
        alChildren.remove(he);
        if (he.getParent() == this) he.setParent(null);
    }
    public List<HtmlElement> getChildren() {
        return alChildren;
    }
  
    public HtmlElement getParent() {
        return heParent;
    }
    public void setParent(HtmlElement he) {
        if (this.heParent == he) return;
        if (this.heParent != null) {
            this.heParent.remove(this);
        }
        this.heParent = he;
        if (this.heParent != null) {
            this.heParent.add(this);
        }
    }
 
    public void setLazyLoad(boolean b) {
        bLazyLoad = b; 
    }
    public boolean getLazyLoad() {
        return bLazyLoad;
    }
    
    public String getJavaScriptForGetElement() {
        if (OAStr.isNotEmpty(htmlComponent.id)) return String.format("document.getElementById('%s')", htmlComponent.id);

        HtmlElement he = getParent();
        if (he != null) {
            int id = he.getOAHtmlComponent().getServerAssignedId();
            if (id > 0) {
                String dn = getDataOAName();
                if (OAStr.isNotEmpty(dn) && dn.indexOf('.') < 0 && dn.indexOf('#') < 0) {
                    return String.format("OAClient.getElement(%d, '%s')", id, getDataOAName());
                }
            }
        }
        
        if (!OAStr.isEmpty(htmlComponent.getDataOAName())) {
            String dnp = "";
            HtmlElement ele = this;
            for ( ;ele != null; ele=ele.getParent()) {
                if (OAStr.isNotEmpty(ele.htmlComponent.getId())) {
                    dnp = OAStr.prepend(dnp, "#"+ele.htmlComponent.getId(), ".");
                    break;
                }
                else dnp = OAStr.prepend(dnp, ele.htmlComponent.getDataOAName(), "."); 
            }
            return String.format("OAClient.getElement('%s')", dnp);
        }
        if (OAStr.isNotEmpty(htmlComponent.htmlSelector)) return String.format("document.querySelector('%s')", htmlComponent.htmlSelector);
        return null;
    }

    
    
    
    public HtmlElement findHtmlElement(final int serverAssignedId) {
        if (this.htmlComponent.getServerAssignedId() == serverAssignedId) return this;
        HtmlElement hex = null;
        if (alChildren != null) {
            for (HtmlElement he : alChildren) {
                hex = he.findHtmlElement(serverAssignedId);
                if (hex != null) break;
            }
        }
        return hex;
    }
    
    
//    dataOANamePath - '.' separated using the html data-oa-name.  Can use Id instead, by prefixing with '#'
    
    public HtmlElement findHtmlElement(final String dataOANamePath) {
        if (OAStr.isEmpty(dataOANamePath)) return null;
        
        String f1 = OAStr.field(dataOANamePath, ".", 1);
        if (f1.startsWith("#")) {
            if (!OAStr.isEqual(OAStr.substring(f1,  2), this.getId())) return null;
        }
        else {
            if (!OAStr.isEqual(f1, this.getDataOAName())) return null;
        }

        String f2 = OAStr.field(dataOANamePath, ".", 2, 99);
        if (OAStr.isEmpty(f2)) return this;

        HtmlElement hex = null;
        if (alChildren != null) {
            for (HtmlElement he : alChildren) {
                hex = he.findHtmlElement(f2);
                if (hex != null) break;
            }
        }
        return hex;
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

    public static String Event_Visible = "visible";  // OAVisibleObserverElement, HtmlElement (lazyLoad)
    public static String Event_Change = "change";  // InputText, InputCheckBox, OAInputCheckBox, HtmlSelect, OATabPanel
    public static String Event_Checked = "checked";  // InputRadio, OAInputRadio
    public static String Event_Show = "show";  // OAAccordionBar 
    public static String Event_Hide = "hide";  // OAAccordionBar
    public static String Event_Click = "click";  // HtmlImg, OAHtmlButton
    public static String Event_Selected = "selected";  // OATree, OATreeNode
    public static String Event_LoadChildren = "loadchildren";  // OATree
    public static String Event_CheckBoxClicked = "checkboxclicked";  // OATree
    
    public static String Event_Search = "search";  // OATypeAheadInputText 
    public static String Event_Select = "select";  // OATypeAheadInputText 
    public static String Event_SetActiveRow = "setactiverow"; // OATable
    public static String Event_ClickHeaderCheckBox = "clickheadercheckbox"; // OATable
    
    
    public void onClientEvent(final String type, final Map<String, String> map) {
        if (OAStr.isEqual(type, Event_Visible)) {
            if (getLazyLoad()) {
                setLazyLoad(false);
                onLazyLoadEvent();
            }
        }
    }
 
    
    protected void onLazyLoadEvent() {
    }

    public void close() {
        List<HtmlElement> al = getChildren();
        if (al == null) return;
        for (HtmlElement he : al) {
            he.close();
        }
    }
}
