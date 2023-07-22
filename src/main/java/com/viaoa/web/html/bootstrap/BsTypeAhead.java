package com.viaoa.web.html.bootstrap;

import java.util.*;

import com.viaoa.object.OAObject;
import com.viaoa.object.OATypeAhead;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.input.InputText;
import com.viaoa.web.server.OASession;

/**
 * Uses OATypeAhead
 * <p>
 * Note:<br>
 * 
 * If multiple, then value will be a list of comma-separated objIds.
 * <br>
 * If not multiple, then value will be the display (not Id)
 * 
 * <p>
 * To do: this will need to update to latest twitter version when using Bootstrap 5
 *  
 * @author vince
 */
public class BsTypeAhead extends InputText {

    protected OATypeAhead typeAhead;
    protected boolean bMultiValue;
    
    public static class TypeAheadValue {
        public String id, display, dropDownDisplay;
        
        public TypeAheadValue(String id, String display, String dropDownDisplay) {
            this.id = id;
            this.display = display;
            this.dropDownDisplay = dropDownDisplay;
        }
    }
    
    public BsTypeAhead(String id) {
        super(id);
    }

    public void setTypeAhead(OATypeAhead ta) {
        this.typeAhead = ta;
    }

    public OATypeAhead getTypeAhead() {
        return typeAhead;
    }
    
    public void setMultiValue(boolean b) {
        this.bMultiValue = b;
    }

    public boolean getMultiValue() {
        return this.bMultiValue;
    }
    
    protected List<TypeAheadValue> getTypeAheadValues(final String search) {
        List<TypeAheadValue> al =  new ArrayList<>(); 
        
        OATypeAhead ta = getTypeAhead();
        if (ta == null) return al;
        
        List<OAObject> alObj = ta.search(search);
        for (OAObject obj : alObj) {
            TypeAheadValue tav = new TypeAheadValue(obj.getObjectKey().toString(), ta.getDisplayValue(obj), ta.getDropDownDisplayValue(obj));
            al.add(tav);
        }
        return al;
    }
    
    /**
     * Called by OAForm.processGetJson to send back Json object for typeAhead lookups.
     * <p>
     * See: oagetjson.jsp
     */
    @Override
    protected String onGetJson(OASession session) {
        String search = session.getRequest().getParameter("term");
        
        List<TypeAheadValue> al = getTypeAheadValues(search);
        
        String json = "";
        for (TypeAheadValue tav : al) {
            if (json.length() > 0) json += ", ";
            json += "{\"id\":\"" + tav.id + "\",\"display\":\"" + tav.display + "\",\"dropdowndisplay\":\"" + tav.dropDownDisplay + "\"}";
        }

        return "[" + json + "]";
    }
    
    
    @Override
    protected String getInitializeScript() {
        final StringBuilder sb = new StringBuilder();

        if (!getMultiValue()) {
            
            // hidden for storing Id 
            sb.append("$('#"+getForm().getId()+"').prepend(\"<input id='"+getId()+"_id' type='hidden' name='"+getId()+"_id' value=''>\");\n");
            
            sb.append("var " + getId() + "Bloodhound = new Bloodhound({\n");
            sb.append("  datumTokenizer : Bloodhound.tokenizers.obj.whitespace('display'),\n");
            sb.append("  queryTokenizer : Bloodhound.tokenizers.whitespace,\n");
    
            sb.append("  remote : {\n");
            sb.append("    url : 'oagetjson.jsp?oaform=" + getForm().getId() + "&id=" + getId() + "&term=%QUERY',\n");
            sb.append("    wildcard: '%QUERY',\n");
            sb.append("    cache: false\n"); // 20171030
            sb.append("  }\n");
    
            sb.append("});\n");
            sb.append("" + getId() + "Bloodhound.initialize();\n");
    
            int minLen = getTypeAhead().getMinimumInputLength();
            if (minLen < 0) {
                minLen = 3;
            }
    
            sb.append("$('#" + getId() + "').typeahead({\n");
            sb.append("    hint: " + (getTypeAhead().getShowHint() ? "true" : "false") + ",\n");
            sb.append("    highlight: true,\n");
            sb.append("    minLength: " + minLen + "\n");
            sb.append("}, {\n");
            sb.append("    source: " + getId() + "Bloodhound,\n");
            sb.append("    name: '" + getId() + "Popup',\n");
            sb.append("    limit: 400,\n"); // see: https://github.com/twitter/typeahead.js/issues/1232
            sb.append("    display: 'display',\n");
            
            /*
            //qqqqqq not in current release qqqqqq     
            highlighter: function (item) {
                var regex = new RegExp( '(' + this.query + ')', 'gi' );
                return item.replace( regex, "&lt;strong&gt;$1&lt;/strong&gt;" );
            },    
            */
            
            sb.append("updater: function(datum) {\n");
            // sb.append("    $('hiddenInputElement').val(datum.id);\n");
            sb.append("    return datum;\n");
            sb.append("}, \n");
            
            sb.append("identify: function(datum){\n");
            sb.append("    return datum.id;\n");    
            //was: sb.append("    return { id: datum.id, value: data.name};\n");    
            sb.append("},\n");
            
            sb.append("    templates: {\n");
            sb.append("      suggestion: function(data) {return '<p>'+data.dropdowndisplay+'</p>';}\n");
            sb.append("    }\n");
    
//qqqqqqqqqqqq NEED to set this _id property on ajax script            
            
            sb.append("  }).on('typeahead:select', function (obj, datum) {\n"); // https://github.com/twitter/typeahead.js/blob/master/doc/jquery_typeahead.md#custom-events
            sb.append("    $('#" + getId() + "_id').val(datum.id);\n");
            if (getAjaxSubmit()) {
                sb.append("    $('#oacommand').val('" + getId() + "');\n");
                sb.append("    ajaxSubmit();\n");
            }
            sb.append("  });\n");
        
        } else {        
            // select multiple from ta list and store displayed value in one property
            sb.append("var " + getId() + "Bloodhound = new Bloodhound({\n");
            sb.append("  datumTokenizer : Bloodhound.tokenizers.obj.whitespace('display'),\n");
            sb.append("  queryTokenizer : Bloodhound.tokenizers.whitespace,\n");
    
            sb.append("  remote : {\n");
            sb.append("    url : 'oagetjson.jsp?oaform=" + getForm().getId() + "&id=" + getId() + "&term=%QUERY',\n");
            sb.append("    wildcard: '%QUERY'\n");
            sb.append("  }\n");
    
            sb.append("});\n");
            sb.append("" + getId() + "Bloodhound.initialize();\n");
    
            int minLen = getTypeAhead().getMinimumInputLength();
            if (minLen < 0) {
                minLen = 3;
            }
    
            sb.append("$('#" + getId() + "').tagsinput({\n");
            // 20170913
            sb.append("  itemValue: 'id',\n"); // <-- store id value
            //was: sb.append("  itemValue: 'display',\n");  // <-- store display value
            sb.append("  itemText: 'display',\n");
            sb.append("  freeInput: false,\n");
            sb.append("  typeaheadjs: [\n");
            sb.append("    {\n");
            sb.append("      minLength: " + minLen + ",\n");
            sb.append("      hint: " + (getTypeAhead().getShowHint() ? "true" : "false") + ",\n");
            sb.append("      highlight: true\n");
            sb.append("    },\n");
            sb.append("    {\n");
            sb.append("      name: '" + getId() + "Popup',\n");
            sb.append("      limit: 400,\n"); // see: https://github.com/twitter/typeahead.js/issues/1232
            sb.append("      display: 'display',\n");
            sb.append("      templates: {\n");
            sb.append("        suggestion: function(data) {return '<p>'+data.dropdowndisplay+'</p>';}\n");
            sb.append("      },\n");
            sb.append("      source: " + getId() + "Bloodhound.ttAdapter()\n");
            sb.append("    }\n");
            sb.append("  ]\n");
            sb.append("});\n");
        } 
        return sb.toString(); 
    }
    
    
    @Override
    public void getRequiredCssNames(Set<String> hsCssName) {
        hsCssName.add(OAFormInsertDelegate.CSS_bootstrap);
        hsCssName.add(OAFormInsertDelegate.CSS_bootstrap_typeahead);

        if (getMultiValue()) {
            hsCssName.add(OAFormInsertDelegate.CSS_bootstrap_tagsinput);
        }
    }
    
    
    @Override
    public void getRequiredJsNames(Set<String> hsJsName) {
        hsJsName.add(OAFormInsertDelegate.JS_bootstrap);
        hsJsName.add(OAFormInsertDelegate.JS_bootstrap_typeahead);

        if (getMultiValue()) {
            hsJsName.add(OAFormInsertDelegate.JS_bootstrap_tagsinput);
        }        
    }
}
