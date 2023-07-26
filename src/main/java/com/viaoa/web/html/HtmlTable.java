package com.viaoa.web.html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.viaoa.web.util.OAWebUtil;

/*      
<table>
    <caption>Superheros and sidekicks</caption>
    <colgroup>
        <col>
        <col width=120 span="2">
        <col span="2">
    </colgroup>
    
    <thead>
    </thead>
    
    <tr>
        <td> </td>
        <th scope="col">Batman</th>
        <th scope="col">Robin</th>
        <th scope="col">The Flash</th>
        <th scope="col">Kid Flash</th>
    </tr>
    <tr>
        <th scope="row">Skill</th>
        <td>Smarts</td>
        <td>Dex, acrobat</td>
        <td>Super speed</td>
        <td>Super speed</td>
    </tr>
    
    <tfoot>
    </tfoot>
</table>              
*/    


/**
 * Html Table element.
 * <p>
 */
public class HtmlTable extends HtmlElement {
    private HtmlColGroup colGroup;

    private final List<HtmlTR> alTHeadRow = new ArrayList<>();
    private final List<HtmlTR> alTFootRow = new ArrayList<>();
    private final List<HtmlTR> alTBodyRow = new ArrayList<>();
    private String prevAjaxScript;

    public HtmlTable(String id) {
        super(id);
    }

    public boolean getEnabled() {
        return htmlComponent.getEnabled();
    }

    public boolean isEnabled() {
        return htmlComponent.getEnabled();
    }

    
    public HtmlColGroup getColGroup() {
        return colGroup;
    }
    public void setColGroup(HtmlColGroup cg) {
        this.colGroup = cg;
    }
    
    
    public void addTHeadRow(HtmlTR tr) {
        this.alTHeadRow.add(tr);
    }
    public List<HtmlTR> getTHeadRows() {
        return alTHeadRow;
    }
    
    public void addTFootRow(HtmlTR tr) {
        this.alTFootRow.add(tr);
    }
    public List<HtmlTR> getTFootRows() {
        return alTFootRow;
    }
    
    public void addTBodyRow(HtmlTR tr) {
        this.alTBodyRow.add(tr);
    }
    public List<HtmlTR> getTBodyRows() {
        return alTBodyRow;
    }

    
    
    @Override
    protected String getAjaxScript(boolean bIsInitializing) {
    
        StringBuilder sb = new StringBuilder();

        HtmlColGroup colGroup = getColGroup();
        if (colGroup != null) {
            sb.append(colGroup.createHtml());
        }
        
        if (getTHeadRows().size() > 0) {
            sb.append("<thead>");
            for (HtmlTR tr : getTHeadRows()) {
                sb.append(tr.createHtml());
            }
            sb.append("</thead>");
        }

        if (getTFootRows().size() > 0) {
            sb.append("<tfoot>");
            for (HtmlTR tr : getTFootRows()) {
                sb.append(tr.createHtml());
            }
            sb.append("</tfoot>");
        }
        
        if (getTBodyRows().size() > 0) {
            sb.append("<tbody>\n");
            for (HtmlTR tr : getTBodyRows()) {
                sb.append(tr.createHtml() + "\n");
            }
            sb.append("</tbody>\n");
        }
        String html = sb.toString();

        if (!bIsInitializing && html.equals(prevAjaxScript)) return null;
        prevAjaxScript = html;

        html = OAWebUtil.createEmbeddedHtmlString(html, '\"');
        
        sb = new StringBuilder();
        sb.append("$('#"+getId()+"').html(`");
        sb.append(html);
        sb.append("`);\n");
        
        String js = sb.toString();
        
        return js;
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
