package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.*;

public class OADialog extends HtmlDiv {

    private boolean bShow;
    private boolean bCalled;
    
    public OADialog(String selector) {
        super(selector);
    }

    public void show() {
        this.bShow = true;
        this.bCalled = true;
    }
    public void hide() {
        this.bShow = false;
        this.bCalled = true;
    }
    
    
    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        String js = null;
        if (bCalled) {
            if (bShow) js = "comp.show();";
            else js = "comp.hide();";
            bHasChanges = true;
            this.bCalled = false;
        }
        
        String s = super.getJavaScriptForClient(hsVars, bHasChanges);
        
        js = OAStr.concat(s, js, "\n");
        
        return js;
    }
    
}
