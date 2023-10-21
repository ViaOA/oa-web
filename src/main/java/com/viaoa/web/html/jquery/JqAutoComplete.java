package com.viaoa.web.html.jquery;

import java.util.List;
import java.util.Set;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.input.InputText;
import com.viaoa.web.server.OASession;
import com.viaoa.web.util.OAJspUtil;

/**
 * Uses JQuery autocomplete.
 * <p>
 * Note: does not support multivalues.
 *  
 * @author vince
 */
public abstract class JqAutoComplete extends InputText {

    public JqAutoComplete(String id) {
        super(id);
    }
    
    @Override
    protected String getInitializeScript() {
        final StringBuilder sb = new StringBuilder();

        // support for jqueryui autocomplete
        sb.append("var cache" + getId() + " = {}, lastXhr" + getId() + ";\n");
        sb.append("$( '#" + getId() + "' ).autocomplete({\n");
        sb.append("minLength: 3,\n");
        sb.append("source: function( request, response ) {\n");
        sb.append("    var term = request.term;\n");
        sb.append("    if ( term in cache" + getId() + " ) {\n");
        sb.append("        response( cache" + getId() + "[ term ] );\n");
        sb.append("        return;\n");
        sb.append("    }\n");
        sb.append("    \n");
        sb.append("    lastXhr" + getId() + " = $.getJSON( 'oagetjson.jsp?oaform=" + getForm().getId() + "&id=" + getId()
                + "', request, function( data, status, xhr ) {\n");
        sb.append("        cache" + getId() + "[ term ] = data;\n");
        sb.append("        if ( xhr === lastXhr" + getId() + " ) {\n");
        sb.append("            response( data );\n");
        sb.append("        }\n");
        sb.append("    });\n");
        sb.append("}\n");

        if (getAjaxSubmit()) {
            sb.append(",\n");
            sb.append("select: function( event, ui ) {\n");
            sb.append("    $('#" + getId() + "').val(ui.item.value);\n");
            sb.append("    $('#oacommand').val('" + getId() + "');\n");
            sb.append("    ajaxSubmit();\n");
            sb.append("}\n");
        }
        sb.append("});\n");
        
        return sb.toString(); 
    }
    
    
//qqqqqqqqqqq this can use OATypeAhead ??  qqqqqqqqqqqqqqqqq    
    
    
    
    /**
     * Called by browser. Uses oagetjson.jsp
     *
     * @param value from user text input 
     * @return list of values to send back to browser.
     */
    public abstract List<String> getAutoCompleteText(String value);

    /**
     * Called by OAForm.processClientRequest to send back Json object for AutoComplete lookups.
     * <p>
     * See: oaclientrequest.jsp
     */
    @Override
    protected String onGetJson(OASession session) {
        String value = session.getRequest().getParameter("term");
        List<String> al = getAutoCompleteText(value);
        
        String values = null;
        if (al != null) {
            for (String s : al) {
                if (values == null) {
                    values = "[";
                }
                else values += ", ";

                s = OAJspUtil.createJsString(s, '\"');
                values += "\"" + s + "\"";
            }
            values += "]";
        }
        if (values == null) values = "[]";
        return values;
    }
    
    @Override
    public void getRequiredCssNames(Set<String> hsCssName) {
    }
    
    
    @Override
    public void getRequiredJsNames(Set<String> hsJsName) {
        hsJsName.add(OAFormInsertDelegate.JS_bootstrap);
    }
}
