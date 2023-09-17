package com.viaoa.web.html;

import java.util.HashSet;
import java.util.Set;

/**
 * Html A element.
 * <p>
 * Uses setInnerHtml to change the text for the link.
 * 
 * @author vince
 */
public class HtmlLink extends HtmlElement {

    public HtmlLink(String id) {
        super(id);
    }

    public boolean getEnabled() {
        return htmlComponent.getEnabled();
    }

    public boolean isEnabled() {
        return htmlComponent.getEnabled();
    }

    public String getHref() {
        return htmlComponent.getHref();
    }
    public void setHref(String href) {
        htmlComponent.setHref(href);
    }
    
    public String getTarget() {
        return htmlComponent.getTarget();
    }
    public void setTarget(String target) {
        htmlComponent.setTarget(target);
    }


    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        hsSupported.add("enabled");
        hsSupported.add("href");
        hsSupported.add("target");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
    
}
