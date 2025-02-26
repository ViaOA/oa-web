package com.viaoa.web.html.bootstrap;

import java.util.Set;

import com.viaoa.util.OAString;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.input.InputText;
import com.viaoa.web.util.OAJspUtil;


/**
 * Create a comma separate list of values.
 * @author vince
 */
public class BsTagsInput extends InputText {

    public BsTagsInput(String selector) {
        super(selector);
    }

    
    @Override
    protected String getInitializeScript() {
        final StringBuilder sb = new StringBuilder();
        
        // https://bootstrap-tagsinput.github.io/bootstrap-tagsinput/examples/
        
        sb.append("$('#" + getId() + "').tagsinput({allowDuplicates: false, freeInput: true });\n");
        return sb.toString(); 
    }
    
    @Override
    protected String getAjaxScript(boolean bIsInitializing) {
        final StringBuilder sb = new StringBuilder();
        
        sb.append("$('#" + getId() + "').tagsinput('removeAll');\n");
        for (int i = 1;; i++) {
            String sx = OAString.field(getValue(), ",", i);
            
            if (sx == null) {
                break;
            }
            sx = sx.trim();
            if (sx.length() == 0) {
                continue;
            }
            sx = OAJspUtil.createJsString(sx, '\'');
            sb.append("$('#" + getId() + "').tagsinput('add', '" + sx + "');\n");
        }

        return sb.toString(); 
    }
    
    @Override
    public void getRequiredCssNames(Set<String> hsCssName) {
      //qqqqq hsCssName.add(OAFormInsertDelegate.CSS_bootstrap_tagsinput);
    }
    
    @Override
    public void getRequiredJsNames(Set<String> hsJsName) {
        hsJsName.add(OAFormInsertDelegate.JS_bootstrap);
      //qqqqq hsJsName.add(OAFormInsertDelegate.JS_bootstrap_tagsinput);
    }
}
