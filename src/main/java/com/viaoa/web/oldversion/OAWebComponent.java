/*  Copyright 1999-2023 Vince Via vince@viaoa.com
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.viaoa.web.oldversion;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.annotation.OAOne;
import com.viaoa.hub.Hub;
import com.viaoa.hub.HubChangeListener;
import com.viaoa.hub.HubDetailDelegate;
import com.viaoa.hub.HubEvent;
import com.viaoa.hub.HubLinkDelegate;
import com.viaoa.hub.HubListenerAdapter;
import com.viaoa.hub.HubTemp;
import com.viaoa.hub.HubChangeListener.HubProp;
import com.viaoa.model.oa.VString;
import com.viaoa.object.OAFinder;
import com.viaoa.object.OALinkInfo;
import com.viaoa.object.OAObject;
import com.viaoa.object.OAObjectCallbackDelegate;
import com.viaoa.object.OAObjectReflectDelegate;
import com.viaoa.object.OAThreadLocalDelegate;
import com.viaoa.template.OATemplate;
import com.viaoa.undo.OAUndoableEdit;
import com.viaoa.util.*;
import com.viaoa.web.control.OAWebController;
import com.viaoa.web.util.OAWebUtil;

/**
 * Class for OAWeb UI controls, to be contained by OAForm.  
 * OAForm handles the html form and interacts with the components.
 * 
 * @author vvia
 */
public class OAWebComponent extends HubListenerAdapter implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private static Logger LOG = Logger.getLogger(OAWebComponent.class.getName());

    public boolean DEBUG; // used for debugging a single component. ex: ((OALabel)lbl).setDebug(true)
    public static boolean DEBUGUI = false; // used by debug() to show info
    
    
    protected String id;

    protected Hub hub;
    protected final boolean bAoOnly;
    protected String propertyPath;
    protected OAPropertyPath oaPropertyPath;
    private final boolean bUseObjectCallback;
    protected boolean bUseLinkHub;

    protected Object hubObject; // single object, that will be put in temp hub

    protected Hub hubSelect;

    
    // all of these are used internally 
    protected Hub hubTemp;
    protected Hub hubLink; // link hub for Hub
    protected String linkPropertyName;
    protected Class endPropertyFromClass; // oaObj class (same as hub, or class for pp end)
    protected String endPropertyName;
    protected Class endPropertyClass;
    protected String hubListenerPropertyName;
    protected HubChangeListener.Type hubChangeListenerType;
    protected boolean bIsHubCalc;
    protected boolean bListenToHubSize;
    protected boolean bEnableUndo = true;
    private boolean bDefaultFormat;
    private String defaultFormat;
    private HubEvent lastUpdateHubEvent;
    private boolean bLastUpdateEnabled;
    
    
    private OAForm form;
    private String name;
    private OALabel label;

    private String toolTip;
    private OATemplate templateToolTip;
    protected boolean bHadToolTip;

    protected String confirmMessage;
    protected OATemplate templateConfirmMessage;
    protected String undoDescription;
    
    private OAWebComponent parent;
    private final List<OAWebComponent> alChildren = new ArrayList();
    
    private boolean bVisible;
    private boolean bEnabled;
    private boolean bFocus;
    protected boolean bRequired;

    protected boolean bSubmit;
    protected boolean bAjaxSubmit;
    protected String forwardUrl;

    
    protected String height; // ex:  200px,  12em
    protected String minHeight; // ex:  200px,  12em
    protected String maxHeight; // ex:  200px,  12em

    protected String width; // ex:  200px,  12em
    protected String minWidth; // ex:  200px,  12em
    protected String maxWidth; // ex:  200px,  12em

    protected String overflow; // ex: visible, hidden, scroll, auto, etc

    protected int columns;
    protected int rows;
    
    protected int displayInputLength, minInputLength, maxInputLength;

    protected String format;

    
    private Map<String, String> hmStyle;
    private Set<String> hsClassAdd;
    private Set<String> hsClassRemove;
    
    protected List<OAHtmlAttribute> alAttribute; 
    
    private MyHubChangeListener changeListener; // listens for any/all hub+propPaths needed for component
    private MyHubChangeListener changeListenerEnabled;
    private MyHubChangeListener changeListenerVisible;
    
    protected String lastAjaxSent;
    

    public OAWebComponent(Hub hub, Object object, String propertyPath, boolean bAoOnly, HubChangeListener.Type type,
            final boolean bUseLinkHub, final boolean bUseObjectCallback) {
        this.hub = hub;
        this.hubObject = object;
        this.propertyPath = propertyPath;
        this.bAoOnly = bAoOnly;
        this.bUseLinkHub = bUseLinkHub;
        this.bUseObjectCallback = bUseObjectCallback;
        this.hubChangeListenerType = type;

        reset();
    }
    
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public Hub getHub() {
        return hub;
    }
    public void setHub(Hub hub) {
        this.hub = hub;
    }
    
    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String pp) {
        this.propertyPath = pp;
    }
    
    public void setForm(OAForm form) {
        this.form = form;
    }
    public OAForm getForm() {
        return form;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    
    /**
     * Returns the Hub that this component will work with.
     */
    public Hub getSelectHub() {
        return hubSelect;
    }

    public Hub getMultiSelectHub() {
        return hubSelect;
    }
    
    
    public void setToolTip(String tooltip) {
        this.toolTip = tooltip;
        templateToolTip = null;
    }
    public String getToolTip() {
        return this.toolTip;
    }

    public void setToolTipText(String tooltip) {
        this.toolTip = tooltip;
        templateToolTip = null;
    }
    public String getToolTipText() {
        return this.toolTip;
    }

    
    
    public String getProcessedToolTip() {
        if (OAString.isEmpty(toolTip) || toolTip.indexOf("<") < 0) return toolTip;
        if (templateToolTip == null) {
            templateToolTip = new OATemplate();
            templateToolTip.setTemplate(getToolTip());
        }
        OAObject obj = null;
        if (hub != null) {
            Object objx = hub.getAO();
            if (objx instanceof OAObject) obj = (OAObject) objx;
        }
        String s = templateToolTip.process(obj, hub, null);
        return s;
    }

    public void setLabel(OALabel lbl) {
        this.label = lbl;
    }

    public OALabel getLabel() {
        return this.label;
    }

    
    
    
    public void setConfirmMessage(String msg) {
        this.confirmMessage = msg;
        templateConfirmMessage = null;
    }
    public String getConfirmMessage() {
        return confirmMessage;
    }
    public String getProcessedConfirmMessage(OAObject obj) {
        if (OAString.isEmpty(confirmMessage) || confirmMessage.indexOf("<") < 0) return confirmMessage;
        
        if (templateConfirmMessage == null) {
            templateConfirmMessage = new OATemplate();
            templateConfirmMessage.setTemplate(getConfirmMessage());
        }
        String s = templateConfirmMessage.process(obj);
        return s;
    }

    public void setUndoDescription(String s) {
        undoDescription = s;
    }
    public String getUndoDescription() {
        return undoDescription;
    }
    
    public void setParent(OAWebComponent comp) {
        this.parent = comp;
        if (comp != null) comp.getChildren().add(comp);
    }
    public OAWebComponent getParent() {
        return this.parent;
    }
    public List<OAWebComponent> getChildren() {
        return alChildren;
    }

    
    
    public boolean isVisible() {
        return this.bVisible;
    }
    public void setVisible(boolean b) {
        this.bVisible = b;
    }
    public boolean getVisible() {
        return this.bVisible;
    }

    public boolean isEnabled() {
        return this.bEnabled;
    }
    public void setEnabled(boolean b) {
        this.bEnabled = b;
    }
    public boolean getEnabled() {
        return this.bEnabled;
    }

    public void setFocus(boolean b) {
        this.bFocus = b;
    }
    public boolean getFocus() {
        return this.bFocus;
    }
    public void requestFocus() {
        setFocus(true);
    }
    
    
    public boolean isRequired() {
        return bRequired;
    }
    public boolean getRequired() {
        return bRequired;
    }
    public void setRequired(boolean required) {
        this.bRequired = required;
    }
    
    
    public void setSubmit(boolean b) {
        bSubmit = b;
        if (b) setAjaxSubmit(false);
    }
    public boolean getSubmit() {
        return bSubmit;
    }
    
    
    public void setAjaxSubmit(boolean b) {
        bAjaxSubmit = b;
        if (b) setSubmit(false);
    }
    public boolean getAjaxSubmit() {
        return bAjaxSubmit;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }
    public String getForwardUrl() {
        return this.forwardUrl;
    }
    
    public void setHeight(String val) {
        this.height = val;
    }
    public String getHeight() {
        return this.height;
    }

    public void setMinHeight(String val) {
        this.minHeight = val;
    }
    public String getMinHeight() {
        return this.minHeight;
    }
    
    public void setMaxHeight(String val) {
        this.maxHeight = val;
    }
    public String getMaxHeight() {
        return this.maxHeight;
    }
    
    public void setWidth(String val) {
        this.width = val;
    }
    public String getWidth() {
        return this.width;
    }
    
    public void setMinWidth(String val) {
        this.minWidth = val;
    }
    public String getMinWidth() {
        return this.minWidth;
    }

    public void setMaxWidth(String val) {
        this.maxWidth = val;
    }
    public String getMaxWidth() {
        return this.maxWidth;
    }
    
    /**
     * @param overflow visible, hidden, scroll, auto, etc 
     */
    public void setOverflow(String overflow) {
        this.overflow = overflow;
    }
    public String getOverflow() {
        return overflow;
    }

    public void setColumns(int cols) {
        this.columns = cols;
    }
    public int getColumns() {
        return columns;
    }
    
    public void setRows(int rows) {
        this.rows = rows;
    }
    public int getRows() {
        return rows;
    }
    
    
    public void setDisplayInputLength(int val) {
        this.displayInputLength = val;
    }
    public int getDisplayInputLength() {
        return this.displayInputLength;
    }
    
    public void setMinInputLength(int val) {
        this.minInputLength = val;
    }
    public int getMinInputLength() {
        return this.minInputLength;
    }

    public void setMaxInputLength(int val) {
        this.maxInputLength = val;
    }
    public int getMaxInputLength() {
        return this.maxInputLength;
    }
    
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }

    public void addStyle(String name, String value) {
        if (name == null) return;
        if (value == null) value = "";
        if (hmStyle == null) hmStyle = new HashMap<String, String>();
        hmStyle.put(name, value);
    }
    public void removeStyle(String name) {
        addStyle(name, "inherit");
    }
    
    public void addClass(String name) {
        if (name == null) return;
        if (hsClassAdd == null) hsClassAdd = new HashSet<>();
        hsClassAdd.add(name);
    }
    public void removeClass(String name) {
        if (name == null) return;
        if (hsClassAdd != null) {
            hsClassAdd.remove(name);
        }
        if (hsClassRemove == null) hsClassRemove = new HashSet<>();
        hsClassRemove.add(name);
    }

    
    public void addAttribute(OAHtmlAttribute attr) {
        if (attr == null) return;
        if (alAttribute == null) alAttribute = new ArrayList<OAHtmlAttribute>();
        alAttribute.add(attr);
    }

    public void removeAttribute(String name) {
        if (OAString.isEmpty(name)) return;
        if (alAttribute == null) return;
        OAHtmlAttribute attrFound = null;
        for (OAHtmlAttribute attr : alAttribute) {
            if (name.equalsIgnoreCase(attr.getAttrName())) {
                attrFound = attr;
                break;
            }
        }
        if (attrFound != null) alAttribute.remove(attrFound);
    }
    
    
    /**
     * Used to listen to additional changes that will then call this.update()
     */
    public HubChangeListener getChangeListener() {
        if (changeListener != null) {
            return changeListener;
        }
        changeListener = new MyHubChangeListener() {
            @Override
            protected void onChange() {
                OAWebComponent.this.update();
            }
        };
        return changeListener;
    }
    
    public HubChangeListener getEnabledChangeListener() {
        if (changeListenerEnabled != null) {
            return changeListenerEnabled;
        }
        changeListenerEnabled = new MyHubChangeListener() {
            @Override
            protected void onChange() {
                OAWebComponent.this.update();
            }
        };
        return changeListenerEnabled;
    }
    public HubProp addEnabledObjectCallbackCheck(Hub hub, String propertyName) {
        return getEnabledChangeListener().addObjectCallbackEnabled(hub, propertyName);
    }

    
    
    public HubChangeListener getVisibleChangeListener() {
        if (changeListenerVisible != null) {
            return changeListenerVisible;
        }
        changeListenerVisible = new MyHubChangeListener() {
            @Override
            protected void onChange() {
                OAWebComponent.this.update();
            }
        };
        return changeListenerVisible;
    }
    protected void update() {
        // TODO Auto-generated method stub
        //qqqqqqqqqqqqqqq
    }

    public HubProp addVisibleObjectCallbackCheck(Hub hub, String propertyName) {
        return getVisibleChangeListener().addObjectCallbackEnabled(hub, propertyName);
    }
    
    
    public boolean isChanged() {
        return false;
    }
    
    /**
     * Called by form.beforeSubmit for every jspcomponent   
     * @return true to continue, false cancel the processing of the request 
     */
    public boolean _beforeFormSubmitted() {
        return true;
    }
    
    /** 
     * Called after _beforeFormSubmitted to find out which component called the submit.
     * 
     * returns true if this component caused the form submit 
     * @see #onSubmit(String) to have the code ran for the component that submitted the form.
     */
    public boolean _onFormSubmitted(HttpServletRequest req, HttpServletResponse resp, HashMap<String, String[]> hashNameValue) {
        return false;
    }

    /** 
     * only called on the component that was responsible for the submit, and called before {@link #onSubmit(String)} 
     */
    public void _beforeOnSubmit() {
    }

    /** 
     * This is only called on the component that was responsible for the submit 
     */
    public String _onSubmit(String forwardUrl) {
        return forwardUrl;
    }

    /**
     * Called by _onSubmit, to allow for subclassing.  
     * This is only called on the component that was responsible for the submit
     */
    public String onSubmit(String forwardUrl) {
        return forwardUrl;
    }
    
    /** 
     * Called by form.processSubmit for every jspcomponent, after onSubmit is called.   
     * return forward url 
     */
    public String _afterFormSubmitted(String forwardUrl) {
        return forwardUrl;
    }
    public String afterFormSubmitted(String forwardUrl) {
        return forwardUrl;
    }
    
    
    
    public String getTableEditorHtml() {
        return null;
    }

    

    /**
     * Called by containers to get html to edit.
     */
    public String getEditorHtml(OAObject obj) {
        return null;
    }
    
    /**
     * Called by containers to get html to render a view only version.
     * This is used to display the non-activeObjects, instead of using the actual editor component.
     */
    public String getRenderHtml(OAObject obj) {
        return null;
    }
    
    public void customizeTableRenderer(OALabel lbl, OATable table, Object value, boolean isSelected, boolean hasFocus, int row, int column,
            boolean wasChanged, boolean wasMouseOver) {
        // TODO Auto-generated method stub
        
    }


    
    protected String getStyleScript() {
        ArrayList<String> al = new ArrayList<String>();
        
        if (hmStyle != null) {
            for (Map.Entry<String, String> ex : hmStyle.entrySet()) {
                String sx = ex.getKey();
                String v = ex.getValue();
                al.add("'"+sx + "':'" + v + "'");
            }
        }

        String s1 = getHeight();
        String s2 = getMinHeight();
        String s3 = getMaxHeight();

        if (OAString.isNotEmpty(s1)) {
            if (OAString.isEmpty(s2)) al.add("'min-height':'"+s1+"'");
            if (OAString.isEmpty(s3)) al.add("'max-height':'"+s1+"'");
        }
        if (OAString.isNotEmpty(s2)) {
            al.add("'min-height':'"+s2+"'");
        }
        if (OAString.isNotEmpty(s3)) {
            al.add("'max-height':'"+s3+"'");
        }

        s1 = getWidth();
        s2 = getMinWidth();
        s3 = getMaxWidth();
        boolean bWidth = OAString.isNotEmpty(s1) || OAString.isNotEmpty(s2) || OAString.isNotEmpty(s3);
        
        if (OAString.isNotEmpty(s1)) {
            if (OAString.isEmpty(s2)) al.add("'min-width':'"+s1+"'");
            if (OAString.isEmpty(s3)) al.add("'max-width':'"+s1+"'");
        }
        if (OAString.isNotEmpty(s2)) {
            al.add("'min-width':'"+s2+"'");
        }
        if (OAString.isNotEmpty(s3)) {
            al.add("'max-width':'"+s3+"'");
        }

        if (OAString.isNotEmpty(overflow)) {
            al.add("overflow:'"+overflow+"'");
            if (overflow.equalsIgnoreCase("hidden")) {
                al.add("'text-overflow':'ellipsis'");
            }
        }

        String css = null;
        for (String s : al) {
            if (css == null) css = "{";
            else css += ",";
            css += s;
        }
        if (css != null) css += "}";
        return css;
    }

    
    protected String getClassJs() {
        String s = null;
        Iterator itx;
        if (hsClassAdd != null) {
            itx = hsClassAdd.iterator();
            for ( ; itx.hasNext() ;  ) {
                String sx = (String) itx.next();
                if (s == null) s = "";
                s += "$('#"+getId()+"').addClass('"+sx+"');";
            }
        }
        
        if (hsClassRemove != null) {
            itx = hsClassRemove.iterator();
            for ( ; itx.hasNext() ;  ) {
                String sx = (String) itx.next();
                if (s == null) s = "";
                s += "$('#"+getId()+"').removeClass('"+sx+"');";
            }
        }        
        return s;
    }


    
    public String[] getRequiredJsNames() {
        ArrayList<String> al = new ArrayList<>();

        al.add(OAJspDelegate.JS_jquery);
        if (OAString.isNotEmpty(getToolTip())) {
            al.add(OAJspDelegate.JS_bootstrap);
        }

        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }

    public String[] getRequiredCssNames() {
        ArrayList<String> al = new ArrayList<>();

        if (OAString.isNotEmpty(getToolTip())) {
            al.add(OAJspDelegate.CSS_bootstrap);
        }

        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }
    


    public String getScript() {
        lastAjaxSent = null;

        StringBuilder sb = new StringBuilder(1024);
        
        String furl = getForwardUrl();

        
        // tooltip
        String prefix = null;
        String tt = getProcessedToolTip();
        if (OAString.isNotEmpty(tt)) {
            if (!bHadToolTip) {
                bHadToolTip = true;
                prefix = "$('#"+id+"').tooltip();\n";
            }

            tt = OAWebUtil.createJsString(tt, '\'');
            sb.append("$('#"+id+"').data('bs.tooltip').options.title = '"+tt+"';\n");
            sb.append("$('#"+id+"').data('bs.tooltip').options.placement = 'top';\n");
        }
        else {
            if (bHadToolTip) {
                sb.append("$('#"+id+"').tooltip('destroy');\n");
                bHadToolTip = false;
            }
        }
        
        
        String confirm = getConfirmMessage();
        if (OAString.isNotEmpty(confirm)) {
            confirm = OAWebUtil.createJsString(confirm, '\"');
            confirm = "if (!window.confirm(\""+confirm+"\")) return false;";
        }
        else confirm = "";
        
        if (bSubmit || bAjaxSubmit) {
            if (bAjaxSubmit) {
                sb.append("$('#"+getId()+"').click(function() {"+confirm+"$('#oacommand').val('"+getId()+"');ajaxSubmit();return false;});\n");
            }
            else {
                sb.append("$('#"+getId()+"').click(function() {"+confirm+"$('#oacommand').val('"+getId()+"'); $('form').submit(); $('#oacommand').val(''); return false;});\n");
            }
            sb.append("$('#"+getId()+"').addClass('oaSubmit');\n");
        }
        else if (!OAString.isEmpty(furl)) {
            sb.append("$('#"+getId()+"').click(function() {"+confirm+"window.location = 'oaforward.jsp?oaform="+getForm().getId()+"&oacommand="+getId()+"';return false;});\n");
            //was: sb.append("$('#"+id+"').click(function() {$('#oacommand').val('"+id+"');window.location = '"+furl+"';return false;});\n");
        }
        
        String s = getAjaxScript();
        if (s != null) sb.append(s);
        String js = sb.toString();
        
        return js;
    }

    
    protected String getEnabledScript(boolean b) {
        // qqqqqqqqq this will be overwritten by textfield to be readonly instead of disabled 
        String js;
        if (b) {
            js = "$('#" + id + "').removeAttr('disabled');";
        } else {
            js = "$('#" + id + "').attr('disabled', 'disabled');";
        }
        return js;
    }
    
    //qqqqqqq compare with last ajax script that was sent, return null if same
    public String getAjaxScript() {
        // to update the page on an ajax update
        String s;
        if (OAString.isEmpty(getId())) return null;
        StringBuilder sb = new StringBuilder(1024);

        boolean b = getEnabledChangeListener().getValue();
        sb.append(getEnabledScript(b) + "\n");
        
        if (getVisible()) sb.append("$('#"+getId()+"').show();\n");
        else sb.append("$('#"+getId()+"').hide();\n");
        
        if (getFocus()) {
            sb.append("$('#" + id + "').focus();\n");
            bFocus = false;
        }
        
        // tooltip
        String prefix = null;
        String tt = getProcessedToolTip();
        if (OAString.isNotEmpty(tt)) {
            if (!bHadToolTip) {
                bHadToolTip = true;
                prefix = "$('#"+getId()+"').tooltip();\n";
            }
            tt = OAWebUtil.createJsString(tt, '\'');
            
            sb.append("$('#"+getId()+"').data('bs.tooltip').options.title = '"+tt+"';\n");
            sb.append("$('#"+getId()+"').data('bs.tooltip').options.placement = 'top';\n");
        }
        else {
            if (bHadToolTip) {
                sb.append("$('#"+getId()+"').tooltip('destroy');\n");
                bHadToolTip = false;
            }
        }
        
        if (alAttribute != null) {
            for (OAHtmlAttribute at : alAttribute) {
                s = at.getScript(getId());
                if (!OAString.isEmpty(s)) sb.append(s+"\n");
            }
        }
 
        s = getStyleScript();
        if (OAString.isNotEmpty(s)) sb.append("$('#"+getId()+"').css("+s+");\n");

        s = getClassJs();
        if (s != null) sb.append(s+"\n");
        
        String js = sb.toString();
        if (lastAjaxSent != null && lastAjaxSent.equals(js)) js = null;
        else lastAjaxSent = js;
        
        if (prefix != null) {
            js = prefix + OAString.notNull(js);
        }
        
        return js;
    }
    
    
    public String getVerifyScript() {
        return null;
    }


    // used to track the last values used by reset
    private Hub hubLast;
    private Object hubObjectLast;
    private HubChangeListener.HubProp hubChangeListenerTypeLast;
    private boolean bIgnoreUpdate;

    protected void reset() {
        try {
            bIgnoreUpdate = true;
            _reset();
        } finally {
            bIgnoreUpdate = false;
        }
        update();
    }
    
    // called when hub, property, etc is changed.
    // does not include resetting HubChangeListeners (changeListener, visibleChangeListener, enabledChangeListener)
    protected void _reset() {
        // note: dont call close, want to keep visibleChangeListener, enabledChangeListener
        if (hubLast != null) {
            hubLast.removeHubListener(this);
        }
        if (hubObjectLast != null) {
            HubTemp.deleteHub(hubObjectLast);
        }
        if (changeListenerEnabled != null && hubChangeListenerTypeLast != null) {
            changeListenerEnabled.remove(hubChangeListenerTypeLast);
            hubChangeListenerTypeLast = null;
        }
        if (changeListener != null) {
            changeListener.close();
            changeListener = null;
        }

        if (hub != null) {
            this.hubTemp = null;
            this.hubObject = null;
        } else {
            if (hubObject == null) {
                this.hub = null;
                this.hubTemp = null;
            } else {
                this.hub = this.hubTemp = HubTemp.createHub(hubObject);
            }
        }

        hubObjectLast = hubObject;
        hubLast = this.hub;

        if (this.hub == null) {
            return;
        }

        if (propertyPath != null && propertyPath.indexOf('.') >= 0) {
            hubListenerPropertyName = propertyPath.replace('.', '_'); // (com.cdi.model.oa.WebItem)B_WebPart_Title
            hub.addHubListener(this, hubListenerPropertyName, new String[] { propertyPath }, bAoOnly);
        } else {
            hubListenerPropertyName = propertyPath;
            if (OAString.isNotEmpty(hubListenerPropertyName)) {
                hub.addHubListener(this, hubListenerPropertyName, bAoOnly);
            } else {
                hub.addHubListener(this);
            }
        }

        oaPropertyPath = new OAPropertyPath(hub.getObjectClass(), propertyPath);
        final String[] properties = oaPropertyPath.getProperties();
        endPropertyName = (properties == null || properties.length == 0) ? null : properties[properties.length - 1];

        if (hubChangeListenerType != null) { // else: this class already is listening to hub
            if (hubChangeListenerType == HubChangeListener.Type.HubNotEmpty || hubChangeListenerType == HubChangeListener.Type.HubEmpty) {
                bListenToHubSize = true;
            }
            hubChangeListenerTypeLast = getEnabledChangeListener().add(hub, hubChangeListenerType);
        }

        if (oaPropertyPath.getEndLinkInfo() != null && properties != null && properties.length == 1) {
            OAOne oaOne = oaPropertyPath.getOAOneAnnotation();
            if (oaOne != null) {
                if (OAString.isNotEmpty(oaOne.defaultPropertyPath())) {
                    if (!oaOne.defaultPropertyPathCanBeChanged()) {
                        getEnabledChangeListener().addPropertyNull(hub, properties[0]);
                    }
                }
            }
        }

        Method[] ms = oaPropertyPath.getMethods();
        endPropertyFromClass = hub.getObjectClass();
        if (ms != null && ms.length > 0) {
            Class[] cs = ms[ms.length - 1].getParameterTypes();
            bIsHubCalc = cs.length == 1 && cs[0].equals(Hub.class);
            endPropertyClass = ms[ms.length - 1].getReturnType();

            if (ms.length > 1) {
                endPropertyFromClass = ms[ms.length - 2].getReturnType();
            }
        } else {
            bIsHubCalc = false;
            endPropertyClass = String.class;
        }
        bDefaultFormat = false;

        if (!bUseLinkHub) {
            if (bUseObjectCallback) {
                Class cz = hub.getObjectClass();
                String ppPrefix = "";
                int cnt = 0;
                for (String prop : properties) {
                    if (cnt == 0) {
                        addEnabledObjectCallbackCheck(hub, prop);
                        addVisibleObjectCallbackCheck(hub, prop);
                    } else {
                        OAObjectCallbackDelegate.addObjectCallbackChangeListeners(  hub, cz, prop, ppPrefix, getEnabledChangeListener(),
                                                                                    true);
                        OAObjectCallbackDelegate.addObjectCallbackChangeListeners(  hub, cz, prop, ppPrefix, getVisibleChangeListener(),
                                                                                    false);
                    }
                    ppPrefix += prop + ".";
                    cz = oaPropertyPath.getClasses()[cnt++];
                }

                if (cnt == 0) {
                    addEnabledObjectCallbackCheck(hub, null);
                    addVisibleObjectCallbackCheck(hub, null);
                }
            }
        } else {
            hubLink = hub.getLinkHub(true);

            if (hubLink != null) {
                linkPropertyName = hub.getLinkPath(true);
            } else {
                Hub hubx = HubDetailDelegate.getMasterHub(hub);
                if (hubx != null) {
                    OALinkInfo li = HubDetailDelegate.getLinkInfoFromMasterToDetail(hub);
                    if (li != null && li.getType() == li.TYPE_ONE) {
                        hubLink = hubx;
                        linkPropertyName = li.getName();
                    }
                }
            }

            if (hubLink != null) {
                getEnabledChangeListener().add(hubLink, HubChangeListener.Type.AoNotNull);
                if (bUseObjectCallback) {
                    addEnabledObjectCallbackCheck(hubLink, linkPropertyName);
                    addVisibleObjectCallbackCheck(hubLink, linkPropertyName);
                }
            }
        }
    }
    
    
    
    
    
    
/*qqqqqqqqqqqqqqqqqqqqqqqqqqqq    
    public void update(Object object, boolean bIncudeToolTip) {
        object = getRealObject(object);
        Font font = getFont(object);
        if (font != null) {
            comp.setFont(font);
        }

        OALabel lblThis;
        if (comp instanceof OALabel) {
            lblThis = (OALabel) comp;
        } else {
            lblThis = null;
        }

        if (lblThis != null) {
            lblThis.setIcon(getIcon(object));
        }
        Color c = getBackgroundColor(object);
        if (c != null) {
            comp.setBackground(c);
        }
        c = getForegroundColor(object);

        if (c != null) {
            comp.setForeground(c);
        }

        // tooltip
        if (bIncudeToolTip) {
            String tt = component.getToolTipText();
            //was: String tt = comp.getToolTipText();
            tt = getToolTipText(object, tt);
            component.setToolTipText(tt);
            //was: comp.setToolTipText(tt);
            if (comp == component && label != null) {
                label.setToolTipText(tt);
            }
        }

        if (lblThis != null && (getPropertyPath() != null || object instanceof String) && !HubLinkDelegate.getLinkedOnPos(hub)) {
            String text;
            if (object == null) {
                text = "";
            } else {
                OATemplate temp = getTemplateForDisplay();
                if (temp != null && (object instanceof OAObject)) {
                    text = templateDisplay.process((OAObject) object);
                    if (text != null && text.indexOf('<') >= 0 && text.toLowerCase().indexOf("<html>") < 0) {
                        text = "<html>" + text;
                    }
                } else {
                    if ((object instanceof OAObject) && oaPropertyPath != null && oaPropertyPath.getHasHubProperty()) {
                        // 20190110 useFinder for pp with hubs
                        VString vs = new VString();
                        OAFinder finder = new OAFinder(oaPropertyPath.getPropertyPathLinksOnly()) {
                            @Override
                            protected void onFound(OAObject obj) {
                                Object objx = obj.getProperty(oaPropertyPath.getLastPropertyName());
                                String s = OAConv.toString(objx, getFormat());
                                vs.setValue(OAString.concat(vs.getValue(), s, ", "));
                            }
                        };
                        finder.find((OAObject) object);
                        text = vs.getValue();
                    } else {
                        Object obj = getValue(object);
                        text = OAConv.toString(obj, getFormat());
                    }
                }
                if (text == null) {
                    String s = getFormat();
                    if (OAString.isNotEmpty(s)) {
                        text = OAConv.toString(null, s);
                    }
                }
            }
            lblThis.setText(text);

            if (object != null) {
                if (bUseObjectCallback) {
                    try {
                        if (object instanceof OAObject) {
                            Object objx = object;
                            if (oaPropertyPath != null && oaPropertyPath.hasLinks()) {
                                objx = oaPropertyPath.getLastLinkValue(objx);
                            }
                            if (objx instanceof OAObject) {
                              //qqqqqqqqq OAObjectCallbackDelegate.renderLabel((OAObject) objx, endPropertyName, lblThis);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("OAJfcController.update exception: " + e);
                    }
                }
                if (lblThis instanceof OAWebComponent) {
                    int pos = getHub().getPos(object);
                    //qqqqqqqqq ((OAWebComponent) lblThis).customizeRenderer(lblThis, object, object, false, false, pos, false, false);
                }
            }
        }
    }
    
*/
    
    
    public Object getValue(Object obj) {
        obj = getRealObject(obj);
        if (obj == null) {
            return null;
        }

        if (OAString.isEmpty(propertyPath) && hubSelect != null) {
            return hubSelect.contains(obj);
        }

        if (bIsHubCalc) {
            obj = OAObjectReflectDelegate.getProperty(getHub(), propertyPath);
        } else {
            if (OAString.isEmpty(propertyPath)) {
                return obj;
            }
            if (!(obj instanceof OAObject)) {
                return obj;
            }
            if (obj instanceof OAObject) {
                obj = ((OAObject) obj).getProperty(propertyPath);
            }
        }
        return obj;
    }

    public String getValueAsString(Object obj) {
        return getValueAsString(obj, getFormat());
    }

    public String getValueAsString(Object obj, String fmt) {
        obj = getValue(obj);
        String s = OAConv.toString(obj, fmt);
        return s;
    }

    /**
     * This will find the real object in this hub to use, in cases where a comp is added to a table, and the table.hub is different then the
     * comp.hub, which could be a detail or link type relationship to the table.hub
     */
    private Class fromParentClass;
    private String fromParentPropertyPath;

    protected Object getRealObject(Object fromObject) {
        if (fromObject == null || hub == null) {
            return fromObject;
        }
        Class c = hub.getObjectClass();
        if (c == null || c.isAssignableFrom(fromObject.getClass())) {
            return fromObject;
        }
        if (!(fromObject instanceof OAObject)) {
            return fromObject;
        }
        if (!OAObject.class.isAssignableFrom(getHub().getObjectClass())) {
            return fromObject;
        }

        if (fromParentClass == null || !fromParentClass.equals(fromObject.getClass())) {
            fromParentClass = fromObject.getClass();
            fromParentPropertyPath = OAObjectReflectDelegate.getPropertyPathFromMaster((OAObject) fromObject, getHub());
        }
        return OAObjectReflectDelegate.getProperty((OAObject) fromObject, fromParentPropertyPath);
    }
    
    
    
    
    // calls the set method on the actualHub.ao
    public void setValue(Object value) {
        String fmt = getFormat();
        Object obj = getHub().getAO();
        setValue(obj, value, fmt);
    }

    public void setValue(Object obj, Object value) {
        String fmt = getFormat();
        setValue(obj, value, fmt);
    }

    public void setValue(Object obj, Object value, String fmt) {
        if (obj == null) {
            return;
        }
        if (obj instanceof OAObject) {
            ((OAObject) obj).setProperty(propertyPath, value, fmt);
        }
    }
    
    

    protected void debug() {
        if (!DEBUGUI && !DEBUG) {
            return;
        }


        String tt = "Comp=" + getClass().getSimpleName();

        tt += "<br>Hub=" + OAString.trunc(getHub() + "", 80);
        if (OAString.isNotEmpty(propertyPath)) {
            tt += "<br>Prop=" + propertyPath;
        }

        if (changeListener != null) {
            String s = changeListener.getToolTipText();
            if (OAString.isNotEmpty(s)) {
                tt += "<br>" + "hcl=" + s;
            }
        }
        if (changeListenerEnabled != null) {
            String s = changeListenerEnabled.getToolTipText();
            if (OAString.isNotEmpty(s)) {
                tt += "<br>" + "Enabled=" + s;
            }
        }
        if (changeListenerVisible != null) {
            String s = changeListenerVisible.getToolTipText();
            if (OAString.isNotEmpty(s)) {
                tt += "<br>" + "Visible=" + s;
            }
        }
        if (label != null) {
            label.setToolTipText("<html>" + tt);
        } else {
            setToolTipText("<html>" + tt);
        }
    }
    



    protected boolean isListeningTo(Hub hub, Object object, String prop) {
        if (hub == null || object == null || prop == null) {
            return false;
        }

        if (getHub() == hub && prop.equalsIgnoreCase(getPropertyPath())) {
            if (!bAoOnly || hub.getAO() == object) {
                return true;
            }
        }

        final MyHubChangeListener[] mcls = new MyHubChangeListener[] { changeListener, changeListenerEnabled,
                changeListenerVisible };
        for (MyHubChangeListener mcl : mcls) {
            if (mcl == null) {
                continue;
            }
            if (mcl.originalIsListeningTo(hub, object, prop)) {
                return true;
            }
        }
        return false;
    }
    
    
    // Shares hubListeners between hub and all 3 hubChangeListeners
    protected abstract class MyHubChangeListener extends HubChangeListener {
        @Override
        public boolean isListeningTo(Hub hub, Object object, String property) {
            // need to check others, since the hubListener is shared across the changeListeners
            return OAWebComponent.this.isListeningTo(hub, object, property);
        }

        public boolean originalIsListeningTo(Hub hub, Object object, String property) {
            // just check this one by calling the super
            return super.isListeningTo(hub, object, property);
        }

        @Override
        public void remove(Hub hub, String prop) {
            final MyHubChangeListener[] mcls = new MyHubChangeListener[] { changeListener, changeListenerEnabled, changeListenerVisible };

            HubProp hp = null;
            for (MyHubChangeListener mcl : mcls) {
                if (mcl == null) {
                    continue;
                }

                for (HubProp hpx : mcl.hubProps) {
                    if (hpx.hub != hub) {
                        continue;
                    }
                    if (hpx.hubListener == null) {
                        continue;
                    }
                    if (!OAString.equals(prop, hpx.propertyPath)) {
                        continue;
                    }
                    hp = hpx;
                    break;
                }
            }
            if (hp == null) {
                return;
            }

            int cnt = 0;
            for (MyHubChangeListener mcl : mcls) {
                if (mcl == null) {
                    continue;
                }
                for (HubProp hpx : mcl.hubProps) {
                    if (hpx.hubListener == hp.hubListener) {
                        cnt++;
                    }
                }
            }

            if (cnt == 1) {
                hp.hub.removeHubListener(hp.hubListener);
            }
            hp.hubListener = null;
        }

        @Override
        public void close() {
            final MyHubChangeListener[] mcls = new MyHubChangeListener[] { changeListener, changeListenerEnabled, changeListenerVisible };

            for (final HubProp hp : hubProps) {
                if (hp.bIgnore) {
                    continue;
                }
                if (hp.hubListener == null) {
                    continue;
                }
                if (hp.hub == OAWebComponent.this.hub) {
                    hp.hubListener = null;
                    continue;
                }

                boolean b = false;
                for (MyHubChangeListener mcl : mcls) {
                    if (mcl == null) {
                        continue;
                    }
                    if (mcl == this) {
                        continue;
                    }
                    for (HubProp hpx : mcl.hubProps) {
                        if (hpx.hubListener == hp.hubListener) {
                            b = true;
                            break;
                        }
                    }
                }
                if (!b && hp.hub != null) {
                    hp.hub.removeHubListener(hp.hubListener);
                }
                for (HubProp hpx : hubProps) {
                    if (hpx == hp) {
                        continue;
                    }
                    if (hpx.hubListener == hp.hubListener) {
                        hpx.hubListener = null;
                    }
                }
                hp.hubListener = null;
            }
        }

        @Override
        protected void assignHubListener(HubProp newHubProp) {
            if (!_assignHubListener(newHubProp)) {
                super.assignHubListener(newHubProp);
            }
        }

        protected boolean _assignHubListener(HubProp newHubProp) {
            if (OAWebComponent.this.hub == newHubProp.hub) {
                if (newHubProp.propertyPath == null) {
                    return true;
                }
                if (newHubProp.propertyPath.indexOf('.') < 0) {
                    if (newHubProp.hub.getOAObjectInfo().getCalcInfo(newHubProp.propertyPath) == null) {
                        // 20221011
                        OALinkInfo lix = newHubProp.hub.getOAObjectInfo().getLinkInfo(newHubProp.propertyPath);
                        if (lix == null || lix.getType() == OALinkInfo.ONE) {
                            return true;
                        }
                    }
                }
                if (newHubProp.propertyPath.equalsIgnoreCase(OAWebComponent.this.propertyPath)) {
                    return true;
                }
            }

            Hub h = OAWebComponent.this.hub;
            if (h != null) {
                h = h.getLinkHub(true);
                if (h != null && h == newHubProp.hub) {
                    if (newHubProp.propertyPath == null) {
                        return true;
                    }
                }
            }

            final MyHubChangeListener[] mcls = new MyHubChangeListener[] { changeListener, changeListenerEnabled, changeListenerVisible };
            for (MyHubChangeListener mcl : mcls) {
                if (mcl == null) {
                    continue;
                }
                for (HubProp hp : mcl.hubProps) {
                    if (hp.bIgnore) {
                        continue;
                    }
                    if (hp.hub != newHubProp.hub) {
                        continue;
                    }
                    if (hp.hubListener == null) {
                        continue;
                    }
                    if (newHubProp.propertyPath == null) {
                        newHubProp.hubListener = hp.hubListener;
                        return true;
                    }
                    if (newHubProp.propertyPath.indexOf('.') < 0) {
                        if (newHubProp.hub != null && newHubProp.hub.getOAObjectInfo().getCalcInfo(newHubProp.propertyPath) == null) {
                            newHubProp.hubListener = hp.hubListener;
                            return true;
                        }
                    }
                    if (!newHubProp.propertyPath.equalsIgnoreCase(hp.propertyPath)) {
                        continue;
                    }
                    newHubProp.hubListener = hp.hubListener;
                    return true;
                }
            }
            return false;
        }

    }


    @Override
    public void afterAdd(HubEvent e) {
        if (bIsHubCalc) {
            update();
        } else if (bListenToHubSize) {
            if (getHub().size() == 1) {
                update();
            }
        }
    }

    @Override
    public void afterRemove(HubEvent e) {
        if (bIsHubCalc) {
            update();
        } else if (bListenToHubSize) {
            if (getHub().size() == 0) {
                update();
            }
        }
    }

    @Override
    public void afterRemoveAll(HubEvent e) {
        if (bIsHubCalc) {
            update();
        } else if (bListenToHubSize) {
            update();
        }
    }

    @Override
    public void onNewList(HubEvent e) {
        update();
    }

    @Override
    public void afterInsert(HubEvent e) {
        if (bIsHubCalc) {
            update();
        } else if (bListenToHubSize) {
            if (getHub().size() == 1) {
                update();
            }
        }
    }

    private final AtomicBoolean abAfterChangeAO = new AtomicBoolean(false);

    @Override
    public void afterChangeActiveObject(HubEvent e) {
        afterChangeActiveObject();
    }

    @Override
    public void afterNewList(HubEvent e) {
        update();
    }

    private final AtomicInteger aiAfterPropChange = new AtomicInteger();

    @Override
    public void afterPropertyChange(final HubEvent e) {

        // 20190918
        Object ao = getHub().getAO();
        if (bAoOnly) {
            if (ao == null || e.getObject() != ao) {
                return;
            }
        }
        if (!isListeningTo(hub, e.getObject(), e.getPropertyName())) {
            return;
        }

        _afterPropertyChange(e);
    }

    private void _afterPropertyChange(HubEvent e) {
        afterPropertyChange();
        update();
    }

    // called if the actual property is changed in the actualHub.activeObject
    protected void afterPropertyChange() {
    }

    protected void afterChangeActiveObject() {
        update();
    }
}
