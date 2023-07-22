package com.viaoa.web.html;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.viaoa.util.OAConv;
import com.viaoa.util.OAStr;
import com.viaoa.web.util.OAWebUtil;

/**
 * Control and Html TD element.
 * <br>
 * Use setInnerHtml to set the contents.
 * <p>
 * @author vince
 */
public class HtmlTD extends HtmlElement {

    private int rowSpan, colSpan;
    private final String tagName;
    
    public HtmlTD() {
        this(null);
    }
    public HtmlTD(String id) {
        super(id);
        this.tagName = "td";
    }
    protected HtmlTD(String id, String tagName) {
        super(id);
        this.tagName = tagName;
    }

    public int getColSpan() {
        return colSpan;
    }
    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }
    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }
    
    
    public boolean getEnabled() {
        return htmlComponent.getEnabled();
    }

    public boolean isEnabled() {
        return htmlComponent.getEnabled();
    }

    protected String createHtml() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<"+tagName);

        if (OAStr.isNotEmpty(getId())) {
            sb.append(" id='"+getId()+"'");
        }
        
        List<String> al = getClasses();
        if (al != null && al.size() > 0) {
            sb.append(" class='");
            boolean b = false;
            for (String s : al) {
                if (b) sb.append(" ");
                else b = true;
                sb.append(s);
            }
            sb.append("'");
        }
        
        
        al = getStyles();
        if (al != null && al.size() > 0) {
            sb.append(" style='");
            for (String s : al) {
                sb.append(s + ": " + OAWebUtil.createJsString(getStyle(s), '\'') + ";");
            }
            sb.append("'");
        }

        int x = getRowSpan();
        if (x > 0) sb.append(" rowspan="+x);

        x = getColSpan();
        if (x > 0) sb.append(" colspan="+x);
        
        sb.append(">");
        
        sb.append(OAWebUtil.createEmbeddedHtmlString(getInnerHtml(), '\"'));
        
        sb.append("</"+tagName+">");
        return sb.toString();
    }

    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        hsSupported.add("enabled"); 
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

}
