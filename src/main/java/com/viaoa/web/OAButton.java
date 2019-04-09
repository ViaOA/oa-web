/*  Copyright 1999-2015 Vince Via vvia@viaoa.com
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.viaoa.web;


import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.hub.Hub;
import com.viaoa.object.OAObject;
import com.viaoa.util.*;
import com.viaoa.web.swing.JButton;


/**
 * Used to control an html button
 * show/hide
 * enable
 * ajax submit option
 * forward url
 * @author vvia
 *
 */
public class OAButton implements OAJspComponent, OAJspRequirementsInterface {
    private static final long serialVersionUID = 1L;

    public static ButtonCommand OTHER = ButtonCommand.Other;
    public static ButtonCommand UP = ButtonCommand.Up;
    public static ButtonCommand DOWN = ButtonCommand.Down;
    public static ButtonCommand SAVE = ButtonCommand.Save;
    public static ButtonCommand CANCEL = ButtonCommand.Cancel;
    public static ButtonCommand FIRST = ButtonCommand.First;
    public static ButtonCommand LAST = ButtonCommand.Last;
    public static ButtonCommand NEXT = ButtonCommand.Next;
    public static ButtonCommand PREVIOUS = ButtonCommand.Previous;
    public static ButtonCommand DELETE = ButtonCommand.Delete;
    public static ButtonCommand REMOVE = ButtonCommand.Remove;
    public static ButtonCommand NEW = ButtonCommand.New;
    public static ButtonCommand INSERT = ButtonCommand.Insert;
    public static ButtonCommand Add = ButtonCommand.Add;
    public static ButtonCommand CUT = ButtonCommand.Cut;
    public static ButtonCommand COPY = ButtonCommand.Copy;
    public static ButtonCommand PASTE = ButtonCommand.Paste;
    public static ButtonCommand NEW_MANUAL = ButtonCommand.NewManual;
    public static ButtonCommand ADD_MANUAL = ButtonCommand.AddManual;
    public static ButtonCommand CLEARAO = ButtonCommand.ClearAO;
    
    public enum ButtonCommand {
        Other, Up, Down, Save, Cancel, First, Last, 
        Next, Previous, Delete, Remove, New, Insert, Add, Cut, Copy, Paste,
        NewManual, AddManual, ClearAO
    }
    
    public static ButtonEnabledMode ALWAYS = ButtonEnabledMode.Always;
    public enum ButtonEnabledMode {
        UsesIsEnabled,
        Always,
        ActiveObjectNotNull,
        ActiveObjectNull,
        HubIsValid,
        HubIsNotEmpty,
        HubIsEmpty,
        AOPropertyIsNotEmpty,
        AOPropertyIsEmpty,
        SelectHubIsNotEmpty,
        SelectHubIsEmpty,
    }
    public static ButtonEnabledMode UsesIsEnabled = ButtonEnabledMode.UsesIsEnabled;
    public static ButtonEnabledMode Always = ButtonEnabledMode.Always;
    public static ButtonEnabledMode ActiveObjectNotNull = ButtonEnabledMode.ActiveObjectNotNull;
    public static ButtonEnabledMode ActiveObjectNull = ButtonEnabledMode.ActiveObjectNull;
    public static ButtonEnabledMode HubIsValid = ButtonEnabledMode.HubIsValid;
    public static ButtonEnabledMode HubIsNotEmpty = ButtonEnabledMode.HubIsNotEmpty;
    public static ButtonEnabledMode HubIsEmpty = ButtonEnabledMode.HubIsEmpty;
    public static ButtonEnabledMode AOPropertyIsNotEmpty = ButtonEnabledMode.AOPropertyIsNotEmpty;
    public static ButtonEnabledMode AOPropertyIsEmpty = ButtonEnabledMode.AOPropertyIsEmpty;
    public static ButtonEnabledMode SelectHubIsNotEmpty = ButtonEnabledMode.SelectHubIsNotEmpty;
    public static ButtonEnabledMode SelectHubIsEmpty = ButtonEnabledMode.SelectHubIsEmpty;
    

//qqqqqqq get command types/logic from OAButton
    
    protected Hub hub;
    protected String id;
    protected OAForm form;
    protected boolean bEnabled = true;
    protected boolean bVisible = true;
    protected boolean bAjaxSubmit;
    protected String forwardUrl;
    protected boolean bSubmit;
    protected boolean bSpinner;
    protected String toolTip;
    protected OATemplate templateToolTip;
    private boolean bHadToolTip;
    protected String confirmMessage;
    protected OATemplate templateConfirmMessage;
    protected String enablePropertyPath;
    protected String visiblePropertyPath;

    
    public OAButton(String id, Hub hub) {
        this.id = id;
        this.hub = hub;
        setSubmit(true);
    }

    public OAButton(String id) {
        this.id = id;
        setSubmit(true);
    }
    
    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }
    public String getForwardUrl() {
        return this.forwardUrl;
    }
    
    @Override
    public boolean isChanged() {
        return false;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setAjaxSubmit(boolean b) {
        bAjaxSubmit = b;
        if (b) setSubmit(false);
    }
    public boolean getAjaxSubmit() {
        return bAjaxSubmit;
    }

    /** if the html is not a submit, then use this to have form submitted. */
    public void setSubmit(boolean b) {
        bSubmit = b;
        if (b) setAjaxSubmit(false);
    }
    public boolean getSubmit() {
        return bSubmit;
    }
    
    @Override
    public void reset() {
    }

    @Override
    public void setForm(OAForm form) {
        this.form = form;
    }
    public OAForm getForm() {
        return form;
    }

    @Override
    public boolean _beforeFormSubmitted() {
        return true;
    }

    @Override
    public boolean _onFormSubmitted(HttpServletRequest req, HttpServletResponse resp, HashMap<String, String[]> hmNameValue) {
        boolean bWasSubmitted = (req.getParameterValues(id) != null);
        if (!bWasSubmitted && hmNameValue != null) {
            bWasSubmitted = hmNameValue.get(id) != null;
        }
            
        if (!bWasSubmitted) {
            String s = req.getParameter("oacommand");
            if (s == null && hmNameValue != null) {
                String[] ss = hmNameValue.get("oacommand");
                if (ss != null && ss.length > 0) s = ss[0];
            }
            bWasSubmitted  = (id != null && id.equals(s));
        }
        if (bWasSubmitted && getSpinner()) {
            lastAjaxSent = null;
        }
        
        return bWasSubmitted; // true if this caused the form submit
    }

    @Override
    public String _afterFormSubmitted(String forwardUrl) {
        return afterFormSubmitted(forwardUrl);
    }
    @Override
    public String afterFormSubmitted(String forwardUrl) {
        return forwardUrl;
    }

    
    @Override
    public void _beforeOnSubmit() {
    }
    
    @Override
    public String _onSubmit(String forwardUrl) {
        return onSubmit(forwardUrl);
    }
    
    @Override
    public String onSubmit(String forwardUrl) {
        return forwardUrl;
    }
    
    private String lastAjaxSent;
    
    @Override
    public String getScript() {
        lastAjaxSent = null;
        bHadToolTip = false;
        StringBuilder sb = new StringBuilder(1024);
        sb.append("$('#"+id+"').attr('name', '"+id+"');\n");
        
        if (getSubmit() || getAjaxSubmit()) {
            sb.append("$('#"+id+"').addClass('oaSubmit');\n");
        }

        if (getSpinner()) {
            sb.append("$('#"+id+"').addClass('ladda-button');\n");
            sb.append("$('#"+id+"').attr('data-style', 'slide-right');\n");
            sb.append("$('#"+id+"').html(\"<span class='ladda-label'>\"+$('#"+id+"').html()+\"</span>\");\n");
            //see:  file:///C:/Projects/metronic_v4.7.5_copy/theme/admin_1/ui_buttons_spinner.html
        }

        String confirm = getConfirmMessage();
        if (OAString.isNotEmpty(confirm)) {
            confirm = OAJspUtil.createJsString(confirm, '\"');
            confirm = "if (!window.confirm(\""+confirm+"\")) return false;";
        }
        else confirm = "";
        
        if (bAjaxSubmit) {
            if (getSpinner()) {
                sb.append("$('#"+id+"').click(function() {"+confirm+"$('#oacommand').val('"+id+"');Ladda.create(this).start();ajaxSubmit();return false;});\n");
            }
            else {
                sb.append("$('#"+id+"').click(function() {"+confirm+"$('#oacommand').val('"+id+"');ajaxSubmit();return false;});\n");
            }
        }
        else if (getSubmit()) {
            if (getSpinner()) {
                sb.append("$('#"+id+"').click(function() {"+confirm+"$('#oacommand').val('"+id+"');var spinner = Ladda.create(this); spinner.start(); $('form').submit(); $('#oacommand').val(''); if (oaSubmitCancelled) spinner.stop(); return false;});\n");
            }
            else {
                sb.append("$('#"+id+"').click(function() {"+confirm+"$('#oacommand').val('"+id+"'); $('form').submit(); $('#oacommand').val(''); return false;});\n");
            }
        }
        
        sb.append(getAjaxScript(true));
        String js = sb.toString();
        return js;
    }

    @Override
    public String getVerifyScript() { 
        return null;
    }

    @Override
    public String getAjaxScript() {
        return getAjaxScript(false);
    }
    public String getAjaxScript(boolean bInit) {
        StringBuilder sb = new StringBuilder(1024);

        if (getSpinner()) { // before disable
            sb.append("Ladda.create($('#"+id+"')[0]).stop();\n");
        }
        
        if (getEnabled()) sb.append("$('#"+id+"').removeAttr('disabled');\n");
        else sb.append("$('#"+id+"').attr('disabled', 'disabled');\n");
        if (getVisible()) sb.append("$('#"+id+"').show();\n");
        else sb.append("$('#"+id+"').hide();\n");
        
        // tooltip
        String prefix = null;
        String tt = getProcessedToolTip();
        if (OAString.isNotEmpty(tt)) {
            if (!bHadToolTip) {
                bHadToolTip = true;
                prefix = "$('#"+id+"').tooltip();\n";
            }

            tt = OAJspUtil.createJsString(tt, '\'');
            sb.append("$('#"+id+"').data('bs.tooltip').options.title = '"+tt+"';\n");
            sb.append("$('#"+id+"').data('bs.tooltip').options.placement = 'top';\n");
        }
        else {
            if (bHadToolTip) {
                sb.append("$('#"+id+"').tooltip('destroy');\n");
                bHadToolTip = false;
            }
        }
        
        String js = sb.toString();
        if (lastAjaxSent != null && lastAjaxSent.equals(js)) js = null;
        else lastAjaxSent = js;
        
        if (prefix != null) {
            js = prefix + OAString.notNull(js);
        }
        return js;
    }


    @Override
    public void setEnabled(boolean b) {
        this.bEnabled = b;
    }
    @Override
    public boolean getEnabled() {
        if (!bEnabled) return false;
        if (hub == null) return true;

        if (!hub.isValid()) return false;
        
        if (OAString.isEmpty(enablePropertyPath)) return true;

        OAObject obj = (OAObject) hub.getAO();
        if (obj == null) return false;
        
        Object value = obj.getProperty(enablePropertyPath);
        boolean b;
        if (value instanceof Hub) {
            b = ((Hub) value).size() > 0;
        }
        else b = OAConv.toBoolean(value);
        return b;
    }
    public String getEnablePropertyPath() {
        return enablePropertyPath;
    }
    public void setEnablePropertyPath(String enablePropertyPath) {
        this.enablePropertyPath = enablePropertyPath;
    }

    
    @Override
    public void setVisible(boolean b) {
        this.bVisible = b;
    }
    @Override
    public boolean getVisible() {
        if (!bVisible) return false;
        if (hub == null) return true;

        if (OAString.isEmpty(visiblePropertyPath)) return true;
        
        OAObject obj = (OAObject) hub.getAO();
        if (obj == null) return false;
        
        Object value = obj.getProperty(visiblePropertyPath);
        boolean b;
        if (value instanceof Hub) {
            b = ((Hub) value).size() > 0;
        }
        else b = OAConv.toBoolean(value);
        return b;
    }
    public String getVisiblePropertyPath() {
        return visiblePropertyPath;
    }
    public void setVisiblePropertyPath(String visiblePropertyPath) {
        this.visiblePropertyPath = visiblePropertyPath;
    }
    
    
    /**
     * see: https://github.com/msurguy/ladda-bootstrap
     * @param b
     */
    public void setSpinner(boolean b) {
        this.bSpinner = b;
    }
    public boolean getSpinner() {
        return bSpinner;
    }

    @Override
    public String[] getRequiredCssNames() {
        ArrayList<String> al = new ArrayList<>();
        if (getSpinner()) {
            al.add(OAJspDelegate.CSS_bootstrap_ladda);
        }
        if (OAString.isNotEmpty(getToolTip())) {
            al.add(OAJspDelegate.CSS_bootstrap);
        }

        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }

    @Override
    public String[] getRequiredJsNames() {
        ArrayList<String> al = new ArrayList<>();
        al.add(OAJspDelegate.JS_jquery);
        if (getSpinner()) {
            al.add(OAJspDelegate.JS_bootstrap_spin);
            al.add(OAJspDelegate.JS_bootstrap_ladda);
        }
        if (OAString.isNotEmpty(getToolTip())) {
            al.add(OAJspDelegate.JS_bootstrap);
        }
        
        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }

    public Hub getHub() {
        return hub;
    }
    
    public void setToolTip(String tooltip) {
        this.toolTip = tooltip;
        templateToolTip = null;
    }
    public String getToolTip() {
        return this.toolTip;
    }
    public String getProcessedToolTip() {
        if (OAString.isEmpty(toolTip)) return toolTip;
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
    
    @Override
    public String getRenderHtml(OAObject obj) {
        String txt = getRenderText(obj);
        if (txt == null) txt = "";

        String classz = getRenderClass(obj);
        if (OAString.isEmpty(classz)) classz = "";
        else classz = " class='"+classz+"' ";
        
        String style = getRenderStyle(obj);
        if (OAString.isEmpty(style)) style = "";
        else style = " style='"+style+"' ";
        
        String s = "<button type='button' "+classz+style+getRenderOnClick(obj)+">"+txt+"</button>";
        return s;
    }
    public String getRenderText(OAObject obj) {
        String s = "Text";
        return s;
    }
    public String getRenderStyle(OAObject obj) {
        return "";
    }
    public String getRenderClass(OAObject obj) {
        return "";
    }
    
    public String getRenderOnClick(OAObject obj) {
        String js = "";
        String s = getProcessedConfirmMessage(obj);
        if (OAString.isNotEmpty(s)) {
            s = OAJspUtil.createEmbeddedJsString(s, '\"');
            js += "if (!window.confirm(\""+s+"\")) return false;";  
        }
        js += "$(\"#oacommand\").val(\""+id+"\");"; 
        
        js = "onClick='" + js + "'";
        
        return js;
    }

    @Override
    public String getEditorHtml(OAObject obj) {
        return getRenderHtml(obj);
    }

    public void setConfirmMessage(String msg) {
        this.confirmMessage = msg;
    }
    public String getConfirmMessage() {
        return confirmMessage;
    }
    public String getProcessedConfirmMessage(OAObject obj) {
        if (OAString.isEmpty(confirmMessage)) return confirmMessage;
        if (templateConfirmMessage == null) {
            templateConfirmMessage = new OATemplate();
            templateConfirmMessage.setTemplate(getConfirmMessage());
        }
        String s = templateConfirmMessage.process(obj);
        return s;
    }

    public static void setup(JButton cmd) {
        // TODO Auto-generated method stub
        
    }
}
