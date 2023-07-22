package com.viaoa.web.html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.viaoa.util.OAStr;
import com.viaoa.web.util.OAWebUtil;

/**
 * Control and Html TR element.
 * <p>
 * @author vince
 */
public class HtmlTR extends HtmlElement {
    private final List<HtmlTD> alTd = new ArrayList<>();

    
    public HtmlTR() {
        this(null);
    }
    public HtmlTR(String id) {
        super(id);
    }

    public void addTableData(HtmlTD td) {
        this.alTd.add(td);
    }
    
    public List<HtmlTD> getTableDatas() {
        return alTd;
    }
    
    
    protected String createHtml() {
        final StringBuilder sb = new StringBuilder();
        
        sb.append("<tr");
        
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
        
        sb.append(">");
        
        
        for (HtmlTD td : getTableDatas()) {
            sb.append(td.createHtml());
        }
        
        
        
        
        sb.append("</tr>");
        return sb.toString();
    }
    
    
    
    
    public boolean getEnabled() {
        return htmlComponent.getEnabled();
    }

    public boolean isEnabled() {
        return htmlComponent.getEnabled();
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
