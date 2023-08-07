package com.viaoa.web.html.bootstrap;

import java.util.*;

import com.viaoa.jsp.OAJspUtil;
import com.viaoa.object.OAObject;
import com.viaoa.object.OATypeAhead;
import com.viaoa.util.OAStr;
import com.viaoa.util.OAString;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputText;
import com.viaoa.web.server.OASession;
import com.viaoa.web.util.OAWebUtil;
/*
 https://github.com/twitter/typeahead.js/blob/master/doc/jquery_typeahead.md   
   *NOTE: this is the original twitter version. 
      There is another "bootstrap" typeahead.
      This uses last version 0.11.1, which is old but works great.
*/

/**
 * Combines InputText, BsTypeAhead and OATypeAhead to allow users to search and select one or more values.<br>
 * <p>
 * A search reponse will be json TypeAheadValue(id,display,dropdownDisplay)<br>
 * The input:text.value will be the selected <b>id</b> value, or comma sep ids if multiValue=true
 * <p>
 * Note:<br>
 * InputText.value will be set to the json.id value(s)
 * <p>
 * For multivalue=false (select one/none), at the lower level there is an input:hidden to store the id value.<br>
 *  
 * @author vince
 */
public class BsTypeAhead extends InputText {
    protected OATypeAhead typeAhead;
    protected boolean bMultiValue;
    
    /**
     * Used as json for search results.
     */
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

    /**
     * Used to return the TypeAheadValue list that will be converted to json  for 
     * onGetJson method.  
     * @param search
     */
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
     * <br>
     * This will call getTypeAheadValues(search) to do the search and get TypeAheadValue list. 
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
  
            json += "{\"id\":\"" + OAWebUtil.escapeJsonString(tav.id) + 
                    "\",\"display\":\"" + OAWebUtil.escapeJsonString(tav.display) + 
                    "\",\"dropdowndisplay\":\"" + OAWebUtil.escapeJsonString(tav.dropDownDisplay) + "\"}";
        }

        return "[" + json + "]";
    }

    /**
     * Overwritten to set input text value to be the Id selected.
     * <p>
     * By default, the input text value will be the display value.
     */
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        if (getMultiValue()) {
            // multiple: taginputs was used and it will assign text.value as a comma sep list of json.Ids
            return;
        }
            
        String val = getValue(); // already assigned
        
        // uses input:hidden to send the json.id of the selected TypeAheadValue object.
        String id;
        String[] ss = formSubmitEvent.getNameValueMap().get(getId() + "_id");
        id = (ss != null && ss.length == 1) ? ss[0] : null;

        if (OAStr.isNotEmpty(id)) {
            // id is set by js code sent to browser, and is the new value to use
            setValue(id);
        }
        else {
            // the input:hidden name="*_id" is only assigned when a new value is assigned in the browser.
            if (OAStr.isEmpty(val)) {
                // since value was cleared, then the selected item was cleared 
                // assign as null
                setValue(null);
            }
            // else no change.
        }
    }
    
    @Override
    protected String getInitializeScript() {
        final StringBuilder sb = new StringBuilder();

        if (!getMultiValue()) {
            // input hidden for storing the value of Id, since input text value will be assigned the json.display 
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
            
            sb.append("updater: function(data) {\n");
            sb.append("    return data;\n");
            sb.append("}, \n");
            
            sb.append("identify: function(data){\n");
            sb.append("    return data.id;\n");    
            sb.append("},\n");
            
            sb.append("templates: {\n");
            sb.append("  suggestion: function(data) {\n");
            sb.append("    return '<p>'+data.dropdowndisplay+'</p>';\n");
            sb.append("  },\n");
            sb.append("  notFound: function(data) {\n");
            sb.append("    return '<p>no matches found</p>';\n");
            sb.append("  }\n");
            sb.append("}\n");

            // https://github.com/twitter/typeahead.js/blob/master/doc/jquery_typeahead.md#custom-events
   
            // when item clicked or nav key - selected
            sb.append("}).on('typeahead:select', function (obj, data) {\n"); 
            sb.append("  $('#" + getId() + "_id').val(data.id);\n");
            sb.append("  oaFocusValue = $(this).val();\n");
            if (getAjaxSubmit()) {
                sb.append("  $('#oacommand').val('" + getId() + "');\n");
                sb.append("  ajaxSubmit();\n");
            }
            
            
            // tabkey, goes to first match  - selected
            sb.append("}).on('typeahead:autocomplete', function (obj, data) {\n"); 
            sb.append("  $('#" + getId() + "_id').val(data.id);\n");
            sb.append("  oaFocusValue = $(this).val();\n");
            if (getAjaxSubmit()) {
                sb.append("  $('#oacommand').val('" + getId() + "');\n");
                sb.append("  ajaxSubmit();\n");
            }
            
            // change, if val != '', then no item selected, need to restore text val - not selected 
            sb.append("}).on('typeahead:change', function (obj, text) {\n");
            sb.append("  if ($('#"+getId()+"').val().trim() != '') {\n");
            sb.append("     $('#"+ getId() +"').val(oaFocusValue);\n");
            sb.append("  }\n");
            sb.append("});\n");
        
            sb.append("$('#"+getId()+"').on('focus', function(event) {\n");
            sb.append("  oaFocusValue = $(this).val();\n");
            sb.append("});\n");

            // blur, and text val is emptpy, set txt_id.val = '' - set selcted to null  
            sb.append("$('#"+getId()+"').on('blur', function() {\n");
            sb.append("  if ($('#"+getId()+"').val().trim() === '' && oaFocusValue != '') {\n");
            sb.append("    $('#"+getId()+"_id').val('');\n");
            sb.append("    oaFocusValue = '';\n");
            sb.append("  }\n");
            sb.append("});\n");
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
    
            // add bs tagsinput            
            sb.append("$('#" + getId() + "').tagsinput({\n");
            sb.append("  itemValue: 'id',\n"); // <-- store id value
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
    protected String getAjaxScript(boolean bIsInitializing) {
        if (!getMultiValue()) return "";
        
        // only needed for multivalue=true
        
        final StringBuilder sb = new StringBuilder();
        sb.append("$('#" + getId() + "').tagsinput('removeAll');\n");
        
        for (int i = 1;; i++) {
            String id = OAString.field(getValue(), ",", i);
            if (id == null) {
                break;
            }
            id = id.trim();
            if (id.length() == 0) {
                continue;
            }

            id = OAJspUtil.createJsString(id, '\'');
            String json = "{'id':'" + id + "','display':'" + id + "','dropdowndisplay':'" + id + "'}";
            
            sb.append("$('#" + getId() + "').tagsinput('add', " + json + ");\n");
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


    // OAHtmlCompponent methods that are used
    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        // BsTypeAhead has it's own/custom "multiValue"
        // hsSupported.add("multiple"); 
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
}
