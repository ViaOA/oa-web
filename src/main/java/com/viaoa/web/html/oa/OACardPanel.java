package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.web.html.*;

/**
 * This uses BS classes "collapse" and "show",
 * and assumes initial html is using them.
 * 
 */
public class OACardPanel extends OAPanel {
     
    private HtmlElement heActive;
    
    public OACardPanel(String elementIdentifier) {
        super(elementIdentifier);
    }
    
    public void setActive(HtmlElement he) {
        if (he == this.heActive) return;
        
        if (this.heActive != null) {
            // this.heActive.setVisible(false);
            this.heActive.removeClass("show"); // BS
        }
        else {
            if (getChildren() != null) {
                for (HtmlElement hex : getChildren()) {
                    // hex.setVisible(false);
                    if (hex != he) hex.removeClass("show"); // BS
                }
            }
        }
        
        this.heActive = he;
        if (this.heActive != null) {
            // this.heActive.setVisible(true);
            this.heActive.addClass("show"); // BS
        }
    }
    
    @Override
    protected void onLazyLoadEvent() {
        super.onLazyLoadEvent();
        HtmlElement he = heActive;
        if (he != null) {
            heActive = null;
            setActive(he);
        }
    }
    
    public HtmlElement getActive() {
        return this.heActive;
    }
    
}
