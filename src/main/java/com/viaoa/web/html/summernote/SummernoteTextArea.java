package com.viaoa.web.html.summernote;

import java.util.Set;

import com.viaoa.object.OAObject;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlTextArea;
import com.viaoa.web.html.OAHtmlComponent.FormElementType;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.util.OAJspUtil;
import com.viaoa.web.util.OAWebUtil;

/*

textarea {
  resize: none;
}

*/


/**
 * Create a Summernote styled editor.
 * <p>
 * Note:<br>
 * 
 * If there are style issues, than make sure that the html parent element styles are not being inherited. 
 * 
 */
public class SummernoteTextArea extends HtmlTextArea {

    public SummernoteTextArea(String id) {
        super(id, FormElementType.StyledTextArea);
    }

    @Override
    protected String getInitializeScript() {
        final StringBuilder sb = new StringBuilder();

        /*
        if (width > 0) sb.append("$('#"+id+"').css('width', '"+width+"em');\n");
        if (getRows() > 0) sb.append("$('#"+id+"').css('height', '"+getRows()+"em');\n");
        */
        
        sb.append("$('#"+getId()+"').summernote({\n");
        
        if (getRows() > 0) sb.append("height: "+(getRows()*12)+",\n");
        // sb.append("minHeight: null,\n");
        //sb.append("maxHeight: null,\n");
        if (getCols() > 0) sb.append("width: "+(getCols()*10)+",\n");
        
        sb.append("spellCheck: true,");
        
        String s = getPlaceHolder();
        
        if (OAStr.isNotEmpty(s)) {
            s = OAJspUtil.createJsString(s, '\'');
            sb.append("placeholder: '" + s +"',");
        }
        sb.append("tabsize: 2,");
        
        sb.append("toolbar: [\n");
        sb.append("   // [groupName, [list of button]]\n");
        sb.append("   ['style', ['bold', 'italic', 'underline', 'clear']],\n");
        sb.append("   ['font', ['strikethrough', 'superscript', 'subscript']],\n");
        sb.append("   ['fontsize', ['fontsize']],\n");
        sb.append("   ['color', ['color']],\n");
        sb.append("   ['para', ['ul', 'ol', 'paragraph']],\n");
        sb.append("   ['insert', ['table', 'hr']],\n");
        sb.append("   ['view', ['fullscreen', 'codeview', 'help']],\n");
        sb.append("   ['height', ['height']]\n");
        sb.append(" ]\n");
        sb.append("});\n");
        
        sb.append("$('#"+getId()+"').on('summernote.blur', function() {\n");
        sb.append("  var txt = $(this).summernote('code');\n");        
        sb.append("  $(this).val(txt);\n");        
        sb.append("});\n");

        return sb.toString();
    }

    private boolean bWasEnabled;
    private boolean bWasSpellCheck;

    @Override
    protected String getAjaxScript(boolean bIsInitializing) {
        final StringBuilder sb = new StringBuilder();
     
        if (!getEnabled() || getReadOnly()) {
            if (bIsInitializing || bWasEnabled) sb.append("$('#"+getId()+"').summernote('disable');\n");
            bWasEnabled = false;
        }
        else {
            if (!bWasEnabled) sb.append("$('#"+getId()+"').summernote('enable');\n");
            bWasEnabled = true;
        }

        if (!getSpellCheck()) {
            if (bIsInitializing || bWasSpellCheck) sb.append("$('#"+getId()+"').summernote({spellCheck: false});\n");
            bWasSpellCheck = false;
        }
        else {
            if (!bWasSpellCheck) sb.append("$('#"+getId()+"').summernote({spellCheck: true});\n");
            bWasSpellCheck = true;
        }

//qqqqqqqqqqqqqqq dont change if it's the same as submitted        
        
        if (!getOAHtmlComponent().getValueChangedByAjax()) {
            String s = getValue();
            if (s == null) s = "";
            s = OAJspUtil.createJsString(s, '\'');
            
            // this is the same as OAHtmlComponent
            sb.append("$('#" + getId() + "').val('"+s+"');\n");
            sb.append("$('#" + getId() + "')[0].oaPrevValue = $('#" + getId() + "').val();\n");
            
            // extra step needed for custom styled text component
            sb.append("$('#"+getId()+"').summernote('code', $('#" + getId() + "').val());\n");
        }


        
        
        return sb.toString();
    }
    
    @Override
    public void getRequiredCssNames(Set<String> hsCssName) {
        hsCssName.add(OAFormInsertDelegate.CSS_bootstrap);
        hsCssName.add(OAFormInsertDelegate.CSS_summernote);
    }

    @Override
    public void getRequiredJsNames(Set<String> hsJsName) {
        hsJsName.add(OAFormInsertDelegate.JS_jquery);
        hsJsName.add(OAFormInsertDelegate.JS_jquery_ui);
        hsJsName.add(OAFormInsertDelegate.JS_bootstrap);
        hsJsName.add(OAFormInsertDelegate.JS_summernote);
    }
}
