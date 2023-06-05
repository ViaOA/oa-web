/* Copyright 1999 Vince Via vvia@viaoa.com Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed
 * to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License. */
package com.viaoa.web.html;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.web.ui.*;
import com.viaoa.web.ui.util.OAJspUtil;
import com.viaoa.object.OAObject;
import com.viaoa.template.OATemplate;
import com.viaoa.util.*;


/**
 * Base HTML Component that works with OAForm.
 *
 * @author vvia
 */
public class HtmlComponent {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected BaseForm form;
    private String name;
    protected String labelId;
    protected String floatLabel;

    protected boolean bEnabled = true;
    protected boolean bVisible = true;
    
    protected boolean bRequired;
    private boolean bFocus;
    protected String value;
    protected String forwardUrl;
    protected boolean bSubmit, bAjaxSubmit;
    protected String toolTip;

    // flags
    private String lastAjaxSent;
    private boolean bHadToolTip;
    private boolean bFloatLabelInit;
    
    
    public HtmlComponent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setForm(BaseForm form) {
        this.form = form;
    }
    public BaseForm getForm() {
        return this.form;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLabelId() {
        return labelId;
    }
    public void setLabelId(String id) {
        this.labelId = id;
    }
    
    public void setFloatLabel(String floatLabel) {
        this.floatLabel = floatLabel;
        this.bFloatLabelInit = false;
    }
    public String getFloatLabel() {
        return this.floatLabel;
    }
    
    public void setEnabled(boolean b) {
        this.bEnabled = b;
    }
    public boolean getEnabled() {
        return bEnabled;
    }

    public void setVisible(boolean b) {
        this.bVisible = b;
    }
    public boolean getVisible() {
        return bVisible;
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

    public void setFocus(boolean b) {
        this.bFocus = b;
    }
    public void setFocus() {
        this.bFocus = true;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public String getForwardUrl() {
        return this.forwardUrl;
    }
    
    public void setSubmit(boolean b) {
        bSubmit = b;
    }
    public boolean getSubmit() {
        return bSubmit;
    }
    
    public void setAjaxSubmit(boolean b) {
        bAjaxSubmit = b;
    }
    public boolean getAjaxSubmit() {
        return bAjaxSubmit;
    }

    public void setToolTip(String tooltip) {
        this.toolTip = tooltip;
    }
    public String getToolTip() {
        return this.toolTip;
    }

    public void setToolTipText(String tooltip) {
        this.toolTip = tooltip;
    }
    public String getToolTipText() {
        return this.toolTip;
    }

    public String getProcessedToolTip() {
        return getToolTipText();
    }
    
    public void reset() {
        value = null;
        lastAjaxSent = null;
    }
    
    
    
    
    public String getScript() {
        lastAjaxSent = null;
        StringBuilder sb = new StringBuilder(1024);

        if (isRequired()) {
            sb.append("$('#" + id + "').addClass('oaRequired');\n");
            sb.append("$('#" + id + "').prop('required', true);\n");
            sb.append("$('#" + id + "').attr('required', 'required');\n");
        }
        else {
            sb.append("$('#" + id + "').removeClass('oaRequired');\n");
            sb.append("$('#" + id + "').prop('required', false);\n");
            sb.append("$('#" + id + "').removeAttr('required');\n");
        }
        sb.append("$('#" + id + "').blur(function() {$(this).removeClass('oaError');}); \n");

        if (getSubmit() || getAjaxSubmit()) {
            sb.append("$('#" + id + "').addClass('oaSubmit');\n");
        }

        sb.append(getAjaxScript(true));

        String s = getVerifyScript();
        if (OAString.isNotEmpty(s)) {
            sb.append("\n");
            sb.append(s);
        }
        
        String js = sb.toString();
        return js;
    }

    public String getVerifyScript() {
        return null;
    }

    public String getAjaxScript() {
        return getAjaxScript(false);
    }

    protected String getAjaxScript(final boolean bIsInitializing) {
        StringBuilder sb = new StringBuilder(1024);

        if (!bFloatLabelInit) {
            getFloatLabelJs(sb);
        }

        // tooltip
        String prefix = null;
        String tt = getProcessedToolTip();
        if (OAString.isNotEmpty(tt)) {
            if (bIsInitializing) {
                prefix = "$('#" + id + "').tooltip();\n";
            }
            tt = OAJspUtil.createJsString(tt, '\'');

            sb.append("$('#" + id + "').data('bs.tooltip').options.title = '" + tt + "';\n");
            sb.append("$('#" + id + "').data('bs.tooltip').options.placement = 'top';\n");
            bHadToolTip = true;
        }
        else {
            if (bHadToolTip) {
                sb.append("$('#" + id + "').tooltip('destroy');\n");
                bHadToolTip = false;
            }
        }

        if (bFocus) {
            sb.append("$('#" + id + "').focus();\n");
            bFocus = false;
        }

        getEnabledScript();
        getVisibleScript();
        
        String js = sb.toString();
        if (lastAjaxSent != null && lastAjaxSent.equals(js)) {
            js = null;
        }
        else {
            lastAjaxSent = js;
        }

        if (prefix != null) {
            js = prefix + OAString.notNull(js);
        }
        return js;
    }

    protected void getFloatLabelJs(StringBuilder sb) {
        bFloatLabelInit = true;

        sb.append("$('#" + id + "').addClass('oaFloatLabel');\n");

        if (OAString.isEmpty(getFloatLabel())) {
            sb.append("$('#" + id + "').after('<span class='active'></span>');\n");
        }
        else {
            sb.append("$('#" + id + "').after('<span></span>');\n");
            sb.append("$('#" + id + "').on('propertychange change keyup paste input', function() { if ($('#" + id
                    + "').val().length > 0) $('#" + id + " + span').addClass('active'); else $('#" + id
                    + " + span').removeClass('active'); });\n");
            sb.append("if ($('#" + id + "').val().length > 0) $('#" + id + " + span').addClass('active');\n");
        }

        sb.append("$('#" + id + " + span').html(\"" + OAJspUtil.createJsString(getFloatLabel(), '\"') + "\");\n");
    }


    protected String getEnabledScript() {
        StringBuilder sb = new StringBuilder(64);
        final String lblId = getLabelId();
        if (getEnabled()) {
            if (OAString.isNotEmpty(lblId)) {
                sb.append("$('#" + lblId + "').prop('disabled', false);\n");
            }
            sb.append("$('#" + id + "').prop('disabled', false);\n");
        }
        else {
            if (OAString.isNotEmpty(lblId)) {
                sb.append("$('#" + lblId + "').prop('disabled', true);\n");
            }
            sb.append("$('#" + id + "').prop('disabled', true);\n");
        }
        return sb.toString();
    }

    protected String getVisibleScript() {
        StringBuilder sb = new StringBuilder(64);
        final String lblId = getLabelId();
        if (getVisible()) {
            if (OAString.isNotEmpty(lblId)) {
                sb.append("$('#" + lblId + "').show();\n");
            }
            sb.append("$('#" + id + "').show();\n");
        }
        else {
            if (OAString.isNotEmpty(lblId)) {
                sb.append("$('#" + lblId + "').hide();\n");
            }
            sb.append("$('#" + id + "').hide();\n");
        }
        return sb.toString();
    }


    
    
    /*
    
submitBefore

boolean submitShouldCancel


submit

  
submitAfter
    
    
    
    */
    
    public boolean _beforeFormSubmitted() {
        return true;
    }

    
    //qqqqqqq only called by component that did the submit
    public void _beforeOnSubmit() {
    }
    
    
    public String _afterFormSubmitted(String forwardUrl) {
        return afterFormSubmitted(forwardUrl);
    }

    public String afterFormSubmitted(String forwardUrl) {
        return forwardUrl;
    }

    public String _onSubmit(String forwardUrl) {
        return onSubmit(forwardUrl);
    }

    public String onSubmit(String forwardUrl) {
        return forwardUrl;
    }

    public boolean _onFormSubmitted(HttpServletRequest req, HttpServletResponse resp, HashMap<String, String[]> hmNameValue) {

        String s = req.getParameter("oacommand");
        if (s == null && hmNameValue != null) {
            String[] ss = hmNameValue.get("oacommand");
            if (ss != null && ss.length > 0) {
                s = ss[0];
            }
        }
        final boolean bWasSubmitted = (id != null && id.equals(s));

        String value = null;
        if (hmNameValue != null) {
            for (Map.Entry<String, String[]> ex : hmNameValue.entrySet()) {
                name = ex.getKey();
                if (!name.toUpperCase().startsWith(id.toUpperCase())) {
                    continue;
                }

                String[] values = ex.getValue();
                if (values != null && values.length > 0) {
                    value = values[0];
                }
            }
        }

        setValue(value);
        return bWasSubmitted;
    }

    
    
    
    
    
    public String getValidationRules() {
        return null;
    }
    
    
    public String getValidationMessages() {
        return null;
    }
    
    
    public String getTableEditorHtml() {
        return null;
    }

    
    
    
    
    
    
    
    
    public String[] getRequiredJsNames() {
        ArrayList<String> al = new ArrayList<>();

        al.add(OAJspDelegate.JS_jquery);
        al.add(OAJspDelegate.JS_jquery_ui);
        al.add(OAJspDelegate.JS_bootstrap);
        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }

    public String[] getRequiredCssNames() {
        ArrayList<String> al = new ArrayList<>();
        al.add(OAJspDelegate.CSS_jquery_ui);
        al.add(OAJspDelegate.CSS_bootstrap);

        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }

    public boolean isChanged() {
        return false;
    }

    public void beforeSubmit(final OAFormSubmitEvent formSubmitEvent) {
        
    }

    public void onSubmit1(final OAFormSubmitEvent formSubmitEvent) {
        
    }

    public void onSubmit2(final OAFormSubmitEvent formSubmitEvent) {
        
    }
    
    public void afterSubmit(final OAFormSubmitEvent formSubmitEvent) {
        
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
    

}
