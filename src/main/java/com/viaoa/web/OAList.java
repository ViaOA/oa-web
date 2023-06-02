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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.hub.Hub;
import com.viaoa.object.OAObject;
import com.viaoa.template.OATemplate;
import com.viaoa.util.*;
import com.viaoa.web.swing.EmptyBorder;

/*
 * Used with an HTML <UL/OL> or <OL/OL> to replace the <LI> with hub objects.
 * 
 * @author vvia
 */
public class OAList extends OAWebComponent {
    private static final long serialVersionUID = 1L;

    protected String nullDescription = "";
    private boolean bShowNullDescriptionFirst;  // false=bottom of list

    
    protected String htmlTemplate;
    private OATemplate template;
    private Map<String, OAWebComponent> hmChildren = new HashMap<>();
    
    protected HashMap<Integer, String> hmHeading;
    
    
    public OAList(String id, Hub hub, String propertyPath) {
        this(id, hub, propertyPath, 0, 0);
    }
    public OAList(String id, Hub hub, String propertyPath, int cols, int rows) {
        super(id, hub, propertyPath);
        setColumns(cols);
        this.rows = rows;
    }
    public OAList(String id, Hub hub, String propertyPath, String width, String height) {
        super(id, hub, propertyPath);
        this.width = width;
        this.maxHeight = height;
    }

    /**
     * set the heading to use, beginning at a specific row.
     */
    public void addHeading(int row, String heading) {
        if (hmHeading == null) hmHeading = new HashMap<>();
        hmHeading.put(row, heading);
    }
    

    @Override
    public boolean isChanged() {
        return false;
    }
    
    @Override
    public boolean _beforeFormSubmitted() {
        return true;
    }

    private String submitUpdateScript;
    

    @Override
    public boolean _onFormSubmitted(HttpServletRequest req, HttpServletResponse resp, HashMap<String, String[]> hmNameValue) {
        boolean bWasSubmitted = _myOnSubmit(req, resp, hmNameValue);
        return bWasSubmitted;
    }
    
    protected boolean _myOnSubmit(HttpServletRequest req, HttpServletResponse resp, HashMap<String,String[]> hmNameValue) {
        // Enumeration enumx = req.getParameterNames();
        
        String s = req.getParameter("oacommand");
        if (s == null && hmNameValue != null) {
            String[] ss = hmNameValue.get("oacommand");
            if (ss != null && ss.length > 0) s = ss[0];
        }
        boolean bWasSubmitted = (id != null && (id.equalsIgnoreCase(s) || getId().equalsIgnoreCase(s)));
        
        
        String name = null;
        String[] values = null;

        for (Map.Entry<String, String[]> ex : hmNameValue.entrySet()) {
            name = (String) ex.getKey();
            if (!name.equalsIgnoreCase("oalist"+id)) continue;
            values = ex.getValue();
            break;
        }

        if (values == null || values.length == 0 || OAString.isEmpty(values[0])) {
            return false;        
        }

        int row = OAConv.toInt(values[0]);
        if (row == lastRow) return bWasSubmitted; // not changed
        
        
        Object obj = hub.getAt(row);
        if (hub != null) hub.setAO(obj);

        
        submitUpdateScript = "";
        submitUpdateScript += "$('#"+id+" li').removeClass('oaSelected');";

        if (hub.getPos() >= 0) {
            submitUpdateScript += "$('#oalist"+id+"').val('"+hub.getPos()+"');";
            //was: s = " li:nth-child("+(hub.getPos()+1)+")";
        }
        else {
            submitUpdateScript += "$('#oalist"+id+"').val('');";
            //was: s = " li:nth-child("+(hub.getSize()+1)+")";
        }
        // use attr selector
        s = " li[oarow=\\'"+hub.getPos()+"\\']";
        submitUpdateScript += "$('#"+id+s+"').addClass('oaSelected');";

        // see if tooltip needs to be updated
        
        
        return bWasSubmitted; // true if this caused the form submit
    }


    
    @Override
    public String getScript() {
        lastAjaxSent = null;
        submitUpdateScript = null;
        bHadToolTip = false;
        StringBuilder sb = new StringBuilder(1024);
    
        sb.append("$('form').prepend(\"<input id='oalist"+id+"' type='hidden' name='oalist"+id+"' value=''>\");\n");

        
        sb.append("function oaList"+id+"Click() {\n");
        sb.append("    var v = $(this).attr('oarow');\n");
        
        sb.append("    if (v == null) return;\n");
        sb.append("    $('#oalist"+id+"').val(v);\n");
        sb.append("    $('#oacommand').val('" + id + "');\n");
        
        
        String s = getOnLineClickJs();
        if (s != null) {
            sb.append("    "+s);
        }
        
        if (getAjaxSubmit() && OAString.isEmpty(forwardUrl)) {
            sb.append("    ajaxSubmit();\n");
        }
        else {
            sb.append("    $('form').submit();\n");
            sb.append("    $('#oacommand').val('');\n");
        }
        sb.append("}\n");
        
        s = getScript2();
        if (s != null) sb.append(s);
        
        s = getAjaxScript();
        if (s != null) sb.append(s);
        
        s = getScript3();
        if (s != null) sb.append(s);
        
        if (isRequired()) {
            sb.append("$('#" + id + "').addClass('oaRequired');\n");
            sb.append("$('#" + id + "').attr('required', true);\n");
        }
        sb.append("$('#" + id + "').blur(function() {$(this).removeClass('oaError');}); \n");

        if (getSubmit() || getAjaxSubmit()) {
            sb.append("$('#" + id + "').addClass('oaSubmit');\n");
        }
        
        
        if (OAString.isNotEmpty(maxHeight)) {
            sb.append("$('#"+id+"').css(\"max-height\", \""+maxHeight+"\");\n");
        }
        else if (rows > 0) {
            int x = (int) (rows * 1.25);
            x += 3;
            sb.append("$('#"+id+" > li').css(\"line-height\", \"1.1em\");\n");
            sb.append("$('#"+id+"').css(\"max-height\", \""+x+"em\");\n");
        }

        
        if (width != null) {
            sb.append("$('#"+id+"').css(\"width\", \""+width+"\");\n");
            sb.append("$('#"+id+"').css(\"max-width\", \""+width+"\");\n");
        }
        else if (columns > 0) {
            int x = (int) (columns * .75);
            sb.append("$('#"+id+"').css(\"width\", \""+(x+2)+"em\");\n");
            sb.append("$('#"+id+"').css(\"max-width\", \""+(x+3)+"em\");\n");
        }
        
        if (OAString.isNotEmpty(width)) {
            sb.append("$('#"+id+"').css(\"width\", \""+width+"\");\n");
            sb.append("$('#"+id+"').css(\"max-width\", \""+width+"\");\n");
        }
        else if (columns > 0) {
            int x = (int) (columns * .75);
            sb.append("$('#"+id+"').css(\"width\", \""+(x+2)+"em\");\n");
            sb.append("$('#"+id+"').css(\"max-width\", \""+(x+3)+"em\");\n");
        }

        String js = sb.toString();
        return js;
    }
    
    protected String getScript2() {
        return null;
    }
    protected String getScript3() {
        return null;
    }
    

    @Override
    public String getVerifyScript() {
        StringBuilder sb = new StringBuilder(1024);

        if (isRequired()) {
            sb.append("if ($('#oalist" + id + "').val() == '') { requires.push('" + (name != null ? name : id) + "'); $('#" + id
                    + "').addClass('oaError');}\n");
        }

        if (sb.length() == 0) return null;
        return sb.toString();
    }

    private String lastAjaxSent;
    
    @Override
    public String getAjaxScript() {

        if (submitUpdateScript != null) {
            String s = submitUpdateScript;
            submitUpdateScript = null;
            lastAjaxSent = null;
            lastRow = hub.getPos();
            return s;
        }
        StringBuilder sb = new StringBuilder(2048);
        
        
        String ppHeading = getHeadingPropertyPath();
        String lastHeading = null;

        if (getShowNullDescriptionFirst()) {
            String s = getHtml(null, -1);
            if (s != null) {
                if (s.length() == 0) s += "&nbsp;";
                sb.append("<li");
                if (hub.getAO() == null) sb.append(" class='oaSelected'");
                sb.append(" oarow='-1'>");
                sb.append(s);
                sb.append("</li>");
            }
        }
        
        for (int pos=0; ;pos++) {
            Object obj = hub.getAt(pos);
            if (obj == null) break;

            boolean b = false;
            
            String heading = null;
            if (OAString.isNotEmpty(ppHeading) && obj instanceof OAObject) {
                heading = ((OAObject) obj).getPropertyAsString(ppHeading, getFormat());
                if (OAString.isNotEmpty(heading) && !OAString.isEqual(heading, lastHeading)) {
                    sb.append("<li class='oaHeading'>"+heading+"</li>");
                    lastHeading = heading;
                }
            }
            if (hmHeading != null) {
                heading = hmHeading.get(pos);
                if (heading != null) {
                    sb.append("<li class='oaHeading'>"+heading+"</li>");
                }
            }
            
            sb.append("<li");
            if (obj == hub.getAO()) sb.append(" class='oaSelected'");
            sb.append(" oarow='"+(pos)+"'>");
            String s = getHtml(obj, pos);
            if (s != null) sb.append(s);
            sb.append("</li>");
        }
        //if (bInOptGroup) sb.append("</optgroup>");

        if (!getShowNullDescriptionFirst()) {
            String s = getHtml(null, -1);
            if (s != null) {
                if (s.length() == 0) s += "&nbsp;";
                sb.append("<li");
                if (hub.getAO() == null) sb.append(" class='oaSelected'");
                sb.append(" oarow='-1'>");
                sb.append(s);
                sb.append("</li>");
            }
        }
        
        String strListing = sb.toString();
        //strListing = Util.convert(strListing, "\\", "\\\\");
        //strListing = Util.convert(strListing, "'", "\\'");
        
        
        sb = new StringBuilder(strListing.length() + 2048);
        
        sb.append("$('#"+id+"').addClass('oaList');\n");
        if (OAString.isNotEmpty(ppHeading) || (hmHeading != null && hmHeading.size() > 0)) {
            sb.append("$('#"+id+"').addClass('oaIndent');\n");
        }

        strListing = OAWebUtil.createJsString(strListing, '\"');
        sb.append("$('#"+id+"').html(\""+strListing+"\");\n");
        
        sb.append("$('#"+id+" li').addClass('oaTextNoWrap');\n");
        
        
        if (getEnabled()) {
            sb.append("$('#"+id+" li[oarow]').click(oaList"+id+"Click);\n");
        }

        int posx = lastRow = hub.getPos();
        if (posx < 0) {
            sb.append("$('#oalist"+id+"').val('');\n"); // set back to blank
        }        
        else sb.append("    $('#oalist"+id+"').val('"+posx+"');\n");

        // tooltip for each row
        if (OAString.isNotEmpty(getToolTipHtmlTemplate())) {        
            for (int pos=0; ;pos++) {
                Object obj = hub.getAt(pos);
                if (obj == null) break;
                if (!(obj instanceof OAObject)) continue;
                
                String tt = getProcessedToolTip((OAObject) obj);
                if (OAString.isNotEmpty(tt)) {
                    tt = OAString.convertToHtml(tt);
                    sb.append("$('#"+id+" li[oarow=\\'"+pos+"\\']').tooltip();\n");
                    sb.append("$('#"+id+" li[oarow=\\'"+pos+"\\']').data('bs.tooltip').options.title = '"+tt+"';\n");
                    sb.append("$('#"+id+" li[oarow=\\'"+pos+"\\']').data('bs.tooltip').options.placement = 'right';\n");
                }
            }
        }


        // tooltip
        String prefix = null;
        String tt = getToolTip();
        if (OAString.isNotEmpty(tt)) {
            tt = OAString.convertToHtml(tt);
            if (!bHadToolTip) {
                bHadToolTip = true;
                prefix = "$('#"+id+"').tooltip();\n";
            }
            
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

    protected String getOnLineClickJs() {
        return null;
    }

    public String getNullDescription() {
        return nullDescription;
    }
    public void setNullDescription(String s) {
        nullDescription = s;
    }
    
    
    public String getHtml(Object obj, int pos) {
        if (obj == null || pos < 0) {
            return getNullDescription();
        }

        String value;
        if (obj instanceof OAObject) {
            value = ((OAObject) obj).getPropertyAsString(getPropertyPath(), getFormat());

            String temp = getTemplateHtml(obj, pos);
            if (temp != null) {
                value = temp;
            }
        }
        else value = obj.toString();
        
        return value;
    }

    
    public String[] getRequiredJsNames() {
        ArrayList<String> al = new ArrayList<>();

        al.add(OAJspDelegate.JS_jquery);
        al.add(OAJspDelegate.JS_jquery_ui);
        
        if (OAString.isNotEmpty(getToolTip())) {
            al.add(OAJspDelegate.JS_bootstrap);
        }

        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }

    @Override
    public String[] getRequiredCssNames() {
        ArrayList<String> al = new ArrayList<>();

        if (OAString.isNotEmpty(getToolTip())) {
            al.add(OAJspDelegate.CSS_bootstrap);
        }

        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }

    @Override
    public String getEditorHtml(OAObject obj) {
        return null;
    }
    @Override
    public String getRenderHtml(OAObject obj) {
        return null;
    }
    
    @Override
    public void _beforeOnSubmit() {
    }


    
    /**
     * @see #getTemplate()
     */
    public void setHtmlTemplate(String htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }
    public String getHtmlTemplate() {
        return this.htmlTemplate;
    }
   

    /**
     * The following values are set and available:
     * $OAPOS, $OACOL, $OAROW
     * @see OATemplate
     */
    public OATemplate getTemplate() {
        if (template != null) return template;
        if (OAString.isEmpty(getHtmlTemplate())) return null;
        
        template = new OATemplate() {
            @Override
            protected String getValue(OAObject obj, String propertyName, int width, String fmt, OAProperties props, boolean bUseFormat) {
                String s;
                OAWebComponent comp = hmChildren.get(propertyName);
                if (comp == null) {
                    s = super.getValue(obj, propertyName, width, fmt, props, bUseFormat);
                }
                else {
                    s = comp.getRenderHtml(obj);
                }
                s = getTemplateValue(obj, propertyName, width, fmt, props, s);
                return s;
            }
            @Override
            protected String getOutputText(String s) {
                return s;
            }
        };
        template.setTemplate(getHtmlTemplate());
        
        return template;
    }
    public void setTemplate(OATemplate temp) {
        this.template = temp;
    }
    
    /*
     * Callback from {@link #getTemplate(Object, int, int, int)}
     */
    public String getTemplateValue(OAObject obj, String propertyName, int width, String fmt, OAProperties props, String defaultValue) {
        return defaultValue;
    }
    
    /**
     * This will use the OATemplate to create the html template for a single object.  
     */
    public String getTemplateHtml(Object objx, int pos) {
        if (!(objx instanceof OAObject)) return null;
        OAObject obj = (OAObject) objx;
        
        if (getTemplate() == null) return null;
        
        template.setProperty("OAPOS", ""+pos);
        template.setProperty("OACOL", ""+(1));
        template.setProperty("OAROW", ""+(pos+1));
        
        String s = template.process(obj);
        
        return s;
    }
    
    /*
    public String getHtmlPropertyPath(Object obj, int pos, int row, int col) {
        String result = null;
        String pp = getPropertyPath();
        if (!OAString.isEmpty(pp)) {
            if (obj != null) {
                result = ((OAObject)obj).getPropertyAsString(pp);
            }
        }
        return result;
    }
    */

    public void setShowNullDescriptionFirst(boolean b) {
        this.bShowNullDescriptionFirst = b;
    }
    public boolean getShowNullDescriptionFirst() {
        return bShowNullDescriptionFirst;
    }
    public void setAllowDnD(boolean b) {
        // TODO Auto-generated method stub
        
    }
    public void setAllowRemove(boolean b) {
        // TODO Auto-generated method stub
        
    }
    public void setAllowDelete(boolean b) {
        // TODO Auto-generated method stub
        
    }
    public void setAllowInsert(boolean b) {
        // TODO Auto-generated method stub
        
    }
    public void setImageProperty(Object object) {
        // TODO Auto-generated method stub
        
    }
    public void setMaxImageHeight(int i) {
        // TODO Auto-generated method stub
        
    }
    public void setMaxImageWidth(int i) {
        // TODO Auto-generated method stub
        
    }
    public void setConfirmMessage(String string) {
        // TODO Auto-generated method stub
        
    }
    public void setIconColorProperty(Object object) {
        // TODO Auto-generated method stub
        
    }
    public void setBackgroundColorProperty(Object object) {
        // TODO Auto-generated method stub
        
    }
    public void setDoubleClickButton(Object object) {
        // TODO Auto-generated method stub
        
    }
    public void setBorder(EmptyBorder emptyBorder) {
        // TODO Auto-generated method stub
        
    }
}
