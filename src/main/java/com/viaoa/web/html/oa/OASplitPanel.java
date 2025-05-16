package com.viaoa.web.html.oa;

import java.util.Set;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlDiv;

/**
 * Create a split panel from an existing panel.
 */
public class OASplitPanel extends HtmlDiv {
    private boolean bInit; 
    private boolean bUsedLazyLoad;
    
    public OASplitPanel(String selector) {
        super(selector);
    }
    
    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        bHasChanges |= !bInit;
        
        String js = super.getJavaScriptForClient(hsVars, bHasChanges);

        bUsedLazyLoad |= getLazyLoad();
        
        // make sure the template is loaded
        if (!bInit && !getLazyLoad()) {
            js = OAStr.concat(js, "comp.initialize("+(!bUsedLazyLoad)+");", "\n");
            bInit = true;
        }
        return js;
    }
}
