/* Copyright 1999-2015 Vince Via vvia@viaoa.com Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed
 * to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License. */
package com.viaoa.web;

import java.awt.Color;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.hub.Hub;
import com.viaoa.object.*;
import com.viaoa.template.OATemplate;
import com.viaoa.util.*;

/**
 * Used to control any html element: hide or show html text: min/max line width, max rows to display
 * click event: ajax or form submit forward URL: to act as a link helper methods to set attributes
 * 
 * see #setConvertTextToHtml(boolean) set to false if text is already in html
 * 
 * @author vvia
 */
public class OAHtmlElement extends OAWebComponent {

    protected String html;
    private boolean bIsPlainText; // true if the text is not HTML

    public OAHtmlElement(String id) {
        super(id);
    }

    public OAHtmlElement(String id, Hub hub) {
        super(id, hub);
    }

    public OAHtmlElement(String id, Hub hub, String propertyPath) {
        super(id, hub, propertyPath);
    }

    @Override
    public boolean _onFormSubmitted(HttpServletRequest req, HttpServletResponse resp, HashMap<String, String[]> hmNameValue) {
        String s = req.getParameter("oacommand");
        if (s == null && hmNameValue != null) {
            String[] ss = hmNameValue.get("oacommand");
            if (ss != null && ss.length > 0) s = ss[0];
        }
        boolean bWasSubmitted = (getId() != null && getId().equals(s));
        return bWasSubmitted; // true if this caused the form submit
    }

    public void setPlainText(boolean b) {
        bIsPlainText = b;
    }

    public boolean isPlainText() {
        return bIsPlainText;
    }

    @Override
    public String getAjaxScript() {
        if (OAString.isEmpty(getId())) return null;
        StringBuilder sb = new StringBuilder(1024);

        String js = super.getAjaxScript();
        String html = getHtml();
        if (html == null) return js;

        if (OAString.isNotEmpty(js)) sb.append(js);

        if (isPlainText()) { // some html properties dont have < or > in them
            if (maxRows == 1) {
                if (lineWidth > 0) html = OAString.lineBreak(html, lineWidth, " ", 1);
            }
            else {
                if (maxRows > 1) {
                    html = OAString.lineBreak(html, lineWidth, "\n", maxRows);
                }
            }
        }
        html = OAWebUtil.createJsString(html, '\"');
        sb.append("$('#" + getId() + "').html(\"" + html + "\");\n");
        return sb.toString();
    }


    public void setHtml(String html) {
        this.html = html;
    }

    protected String getHtml() {
        if (hub == null || getPropertyPath() == null) return this.html;

        Object obj = hub.getAO();
        if (obj != null) {
            if (!(obj instanceof OAObject)) return OAConv.toString(obj, getFormat());
        }
        String value = getHtml((OAObject) obj);

        return value;
    }

    public String getHtml(OAObject obj) {
        String value = null;

        if (obj != null) {
            value = obj.getPropertyAsString(propertyPath, getFormat());
        }
        if (value == null) value = "";

        int addSp = (minLineWidth <= 0) ? 0 : (minLineWidth - value.length());
        for (int i = 0; i < addSp; i++)
            value += " "; //? might need to use &nbsp;

        if (!isPlainText()) value = OAWebUtil.convertToHtml(value);

        return value;
    }

    @Override
    public String getEditorHtml(OAObject obj) {
        return getHtml(obj);
    }

    @Override
    public String getRenderHtml(OAObject obj) {
        return getHtml(obj);
    }

}
