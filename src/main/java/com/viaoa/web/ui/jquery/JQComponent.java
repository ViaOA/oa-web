/* Copyright 1999 Vince Via vvia@viaoa.com Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed
 * to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License. */
package com.viaoa.web.ui.jquery;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.web.ui.*;
import com.viaoa.web.ui.util.OAJspUtil;
import com.viaoa.datasource.OADataSource;
import com.viaoa.hub.Hub;
import com.viaoa.object.*;
import com.viaoa.object.OATypeAhead;
import com.viaoa.template.OATemplate;
import com.viaoa.util.*;

/* HTML
 * 
 * <input id="txtLoginId" type="text" placeholder="Login Id"> */

/**
 * Controls an html input type=text<br>
 * Binds to Hub, using property path to set display size, max input, visibility, enabled, ajax submit on
 * change, required, validation, input mask support, calendar popup datetime, date, time formats
 *
 * @author vvia
 */
public class JQComponent implements OAWebUIComponentInterface, OAWebUITableInterface, OAWebUIComponentRequirementsInterface {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected OAForm form;
    protected boolean bEnabled = true;
    protected boolean bVisible = true;
    protected boolean bAjaxSubmit, bSubmit;

    protected boolean required;
    protected String value;
    private boolean bFocus;
    protected String forwardUrl;
    private String name;

    protected String toolTip;
    protected OATemplate templateToolTip;
    private boolean bHadToolTip;
    protected String floatLabel;

    protected String labelId;

    public JQComponent(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void reset() {
        value = null;
        lastAjaxSent = null;
    }

    @Override
    public void setForm(OAForm form) {
        this.form = form;
    }

    @Override
    public OAForm getForm() {
        return this.form;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public String getForwardUrl() {
        return this.forwardUrl;
    }

    public void setAjaxSubmit(boolean b) {
        bAjaxSubmit = b;
    }

    public boolean getAjaxSubmit() {
        return bAjaxSubmit;
    }

    public void setSubmit(boolean b) {
        bSubmit = b;
    }

    public boolean getSubmit() {
        return bSubmit;
    }

    @Override
    public String getScript() {
        lastAjaxSent = null;
        bHadToolTip = false;
        bFloatLabelJsInit = false;
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

        sb.append(_getAjaxScript(true));

        String js = sb.toString();
        return js;
    }

    @Override
    public String getVerifyScript() {
        return null;
    }

    /** used when displaying error message for this textfield */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String lastAjaxSent;

    @Override
    public String getAjaxScript() {
        return _getAjaxScript(false);
    }

    protected String _getAjaxScript(final boolean bIsInitializing) {
        StringBuilder sb = new StringBuilder(1024);

        if (bIsInitializing) {
            getFloatLabelJs(sb);
        }

        // tooltip
        String prefix = null;
        String tt = getProcessedToolTip();
        if (OAString.isNotEmpty(tt)) {
            if (!bHadToolTip) {
                bHadToolTip = true;
                prefix = "$('#" + id + "').tooltip();\n";
            }
            tt = OAJspUtil.createJsString(tt, '\'');

            sb.append("$('#" + id + "').data('bs.tooltip').options.title = '" + tt + "';\n");
            sb.append("$('#" + id + "').data('bs.tooltip').options.placement = 'top';\n");
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

    public boolean isRequired() {
        return required;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void setEnabled(boolean b) {
        this.bEnabled = b;
    }

    @Override
    public boolean getEnabled() {
        return bEnabled;
    }

    @Override
    public void setVisible(boolean b) {
        this.bVisible = b;
    }

    @Override
    public boolean getVisible() {
        return bVisible;
    }

    public void setValue(String value) {
        if (value != this.value || value == null || value.length() == 0 || !value.equals(this.value)) {
            lastAjaxSent = null;
        }
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setFocus(boolean b) {
        this.bFocus = b;
    }

    public void setFocus() {
        this.bFocus = true;
    }

    @Override
    public String getTableEditorHtml() {
        return null;
    }

    @Override
    public String[] getRequiredJsNames() {
        ArrayList<String> al = new ArrayList<>();

        al.add(OAJspDelegate.JS_jquery);
        al.add(OAJspDelegate.JS_jquery_ui);
        al.add(OAJspDelegate.JS_bootstrap);
        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }

    @Override
    public String[] getRequiredCssNames() {
        ArrayList<String> al = new ArrayList<>();
        al.add(OAJspDelegate.CSS_jquery_ui);
        al.add(OAJspDelegate.CSS_bootstrap);

        String[] ss = new String[al.size()];
        return al.toArray(ss);
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
        return getToolTipText();
    }

    protected String getEnabledScript(boolean b) {
        if (b) {
            return ("$('#" + id + "').prop('disabled', false);\n");
            //was: return ("$('#" + id + "').removeAttr('disabled');\n");
        }
        return ("$('#" + id + "').prop('disabled', true);\n");
        //was: return ("$('#" + id + "').attr('disabled', 'disabled');\n");
    }

    protected String getVisibleScript(boolean b) {
        if (b) {
            return ("$('#" + id + "').show();\n");
        }
        return ("$('#" + id + "').hide();\n");
    }

    @Override
    public String getRenderHtml(OAObject obj) {
        return null;
    }

    @Override
    public String getEditorHtml(OAObject obj) {
        return null;
    }

    public void setFloatLabel(String floatLabel) {
        this.floatLabel = floatLabel;
        floatLabel = null;
    }

    public String getFloatLabel() {
        return this.floatLabel;
    }

    private boolean bFloatLabelJsInit;

    protected void getFloatLabelJs(StringBuilder sb) {
        String s = getFloatLabel();
        if (OAString.isEmpty(s)) {
            if (!bFloatLabelJsInit) {
                return;
            }
        }

        if (!bFloatLabelJsInit) {
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
            bFloatLabelJsInit = true;
        }

        s = getFloatLabel();
        sb.append("$('#" + id + " + span').html(\"" + OAJspUtil.createJsString(s, '\"') + "\");\n");
    }

    public String getValidationMessages() {
        return null;
    }

    //qqqqqqqqqqqqqqqqqqq

    @Override
    public void _beforeOnSubmit() {
    }

    @Override
    public boolean _beforeFormSubmitted() {
        return true;
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
    public String _onSubmit(String forwardUrl) {
        return onSubmit(forwardUrl);
    }

    @Override
    public String onSubmit(String forwardUrl) {
        return forwardUrl;
    }

    @Override
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

}
