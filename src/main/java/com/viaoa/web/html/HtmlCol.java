package com.viaoa.web.html;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.viaoa.util.OAConv;
import com.viaoa.util.OAStr;
import com.viaoa.web.util.OAWebUtil;

/*

limited selection of CSS properties 
    https://www.w3schools.com/html/html_table_colgroup.asp


<table>
  <colgroup>
    <col span="2" style="background-color: #D6EEEE">
    <col span="3" style="background-color: pink">
  </colgroup>
  <tr>
    <th>MON</th>
    <th>TUE</th>
    <th>WED</th>
    <th>THU</th>
...

*/

/**
 * Control and Html colgroup Col element.
 * <p>
 * This needs to have style "table-layout: fixed;" in table.
 * <p>
 * Notes:<br>
 * 
 * Styles are limited to: width, visibility, background, border
 * <br>
 * hide column using the visibility: collapse
 * 
 * 
 * 
 * 
 * @author vince
 */
public class HtmlCol extends HtmlElement {

    protected int span;
    
    
    public HtmlCol() {
        this(null);
    }
    public HtmlCol(String selector) {
        super(selector);
    }

    public int getSpan() {
        return span;
    }
    public void setSpan(int span) {
        this.span = span;
    }

    protected String createHtml() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<col");

        boolean b = false;
        List<String> al = getStyles();
        if (al != null && al.size() > 0) {
            sb.append(" style='");
            b = true;
            for (String s : al) {
                sb.append(s + ": " + OAWebUtil.createJsString(getStyle(s), '\'') + ";");
            }
        }

        if (OAStr.isNotEmpty(getWidth())) {
            if (!b) sb.append(" style='");
            sb.append("width:"+getWidth()+";");
            b = true;
            
   //qqqqqqqqqqq cut off qqqqqqqqqqqqqqqqqqqqqq         
            
        }

        
        if (!getVisible()) {
            if (!b) sb.append(" style='");
            b = true;
            sb.append("visibility:collapse;");
        }
        if (b) sb.append("'");
        
        
        int x = getSpan();
        if (x > 0) sb.append(" span="+x);
        
        sb.append(">");
        return sb.toString();
    }
    

    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        // hsSupported.add("width"); 
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

}
