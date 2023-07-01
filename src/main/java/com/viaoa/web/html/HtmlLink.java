package com.viaoa.web.html;

import java.util.HashSet;
import java.util.Set;

/**
 * Control and Html A element.
 * <p>
 * Uses setInnerHtml to change the text for the link.
 * 
 * @author vince
 */
public class HtmlLink extends HtmlElement {

    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        // hsSupported.add("disabled");  qqqqq not an attribute, but can be done in JS
        hsSupported.add("href");
        hsSupported.add("target");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
    
    public HtmlLink(String id) {
        super(id);
    }

    public boolean getEnabled() {
        return oaHtmlComponent.getEnabled();
    }

    public boolean isEnabled() {
        return oaHtmlComponent.getEnabled();
    }

    public String getHref() {
        return oaHtmlComponent.getHref();
    }
    public void setHref(String href) {
        oaHtmlComponent.setHref(href);
    }
    
    public String getTarget() {
        return oaHtmlComponent.getTarget();
    }
    public void setTarget(String target) {
        oaHtmlComponent.setTarget(target);
    }
}
