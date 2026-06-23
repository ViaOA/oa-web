package com.viaoa.web.html.oa;

import java.util.List;
import java.util.Set;

import com.viaoa.lang.OAStr;
import com.viaoa.web.html.HtmlDiv;
import com.viaoa.web.html.HtmlElement;

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
        if (!getLazyLoad()) {
        	bHasChanges |= !bInit;
        }
        bUsedLazyLoad |= getLazyLoad();
        
        String js = super.getJavaScriptForClient(hsVars, bHasChanges);

        
        // make sure the template is loaded
        if (!bInit && !getLazyLoad()) {
            js = OAStr.concat(js, "comp.initialize("+(!bUsedLazyLoad)+");", "\n");
            bInit = true;
        }
        return js;
    }
}
