package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.web.html.*;



/*qqqqqqqqq qqqqqqqqqqq

OAAccordion.js  ... to have it use JS only (not BS), 
    remove the html data-bs-*
    and add the flex-shrink: 0 (below) in the JS code, and remove from CSS (some browsers dont support not() css selector)

*/


/**
 * Bootstrap Accordion that uses data-bs-* to prewire JS 
 * See also OAAccordionBar. 
 */
public class OAAccordionPanel extends HtmlDiv {

    // private final List<OAAccordionBar> alHtmlElement = new ArrayList();
    // private OAAccordionBar barActive;
    
    
    public OAAccordionPanel(String selector) {
        super(selector);
    }
    
    public void addBar(OAAccordionBar bar) {
        if (bar == null) return;
        /*
        if (!this.alHtmlElement.contains(bar)) this.alHtmlElement.add(bar);
        bar.setVisible(bar == getActive());
        if (bar == getActive()) bar.addClass("show"); // BS
        else bar.removeClass("show"); // BS
        */
    }
    
    /*
    public List<OAAccordionBar> getAccordionBars() {
        return alHtmlElement;
    }
    
    public void setActive(OAAccordionBar he) {
        if (this.barActive != null) {
            this.barActive.setVisible(false);
            this.barActive.removeClass("show"); // BS
        }
        this.barActive = he;
        if (this.barActive != null) {
            this.barActive.setVisible(true);
            this.barActive.addClass("show"); // BS
        }
    }
    public OAAccordionBar getActive() {
        return this.barActive;
    }
    */
  
    
    
}
