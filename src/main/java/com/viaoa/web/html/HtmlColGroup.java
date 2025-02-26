package com.viaoa.web.html;

import java.util.*;

/**
 * Control and Html colgroup element.
 * <p>
 * @author vince
 */
public class HtmlColGroup extends HtmlElement {

    private final List<HtmlCol> alCol = new ArrayList<>();
    
    public HtmlColGroup() {
        this(null);
    }
    public HtmlColGroup(String selector) {
        super(selector);
    }

    public void addCol(HtmlCol col) {
        this.alCol.add(col);
    }
    
    public List<HtmlCol> getCols() {
        return alCol;
    }
    
    protected String createHtml() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<colgroup>");
        for (HtmlCol col : getCols()) {
            sb.append(col.createHtml());
        }
        sb.append("</colgroup>");
        return sb.toString();
    }
    
    

    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        // hsSupported.add("enabled"); 
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

}
