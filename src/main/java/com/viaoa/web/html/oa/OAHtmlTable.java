package com.viaoa.web.html.oa;

import java.io.OutputStream;
import java.util.*;

import com.viaoa.hub.Hub;
import com.viaoa.uicontroller.OAUIBaseController;
import com.viaoa.uicontroller.OAUISelectController;
import com.viaoa.util.OAConv;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlCol;
import com.viaoa.web.html.HtmlColGroup;
import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.HtmlTH;
import com.viaoa.web.html.HtmlTR;
import com.viaoa.web.html.HtmlTable;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.EventType;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.server.OASession;

public class OAHtmlTable extends HtmlTable implements OAHtmlComponentInterface {

    private final OAUIBaseController oaUiControl;
    private final List<Column> alColumn = new ArrayList<>();
    private Hub hub;
    
    public OAHtmlTable(String id, Hub hub) {
        super(id);
        this.hub = hub;
        
        oaUiControl = new OAUISelectController(hub);
        setAjaxSubmit(true);
    }
    
    private static class Column {
        HtmlCol htmlCol;
        HtmlTH htmlTh;
        OAHtmlTableComponentInterface comp;

        public Column(HtmlCol htmlCol, HtmlTH htmlTh, OAHtmlTableComponentInterface comp) {
            this.htmlCol = htmlCol;
            this.htmlTh = htmlTh;
            this.comp = comp;
        }
    }
    
    
    public void addColumn(String title, OAHtmlTableComponentInterface comp) {
    
    //qqqqqqqqqqqqqqqqqqqqqqqqqqqqqq needs to add to list of 
    qqqqqqqqqqqqqqqqqqqqqqq
    if (comp instanceof HtmlElement) add((HtmlElement) comp);
    
        HtmlCol htmlCol = new HtmlCol();
        HtmlTH htmlTh = new HtmlTH();
        htmlTh.setInnerHtml(title == null ? "" : title);
        addColumn(htmlCol, htmlTh, comp);
    }
    
    
    /**
     * 
     * @param htmlCol used to add (limited) styling to the column.
     * @param htmlTh defines the column heading.
     * @param comp
     */
    public void addColumn(HtmlCol htmlCol, HtmlTH htmlTh, OAHtmlTableComponentInterface comp) {
        this.alColumn.add(new Column(htmlCol, htmlTh, comp));

        HtmlColGroup cg = getColGroup(); 
        if (cg == null) {
            cg = new HtmlColGroup("");
            setColGroup(cg);
        }
        cg.addCol(htmlCol);
        
        List<HtmlTR> al = getTHeadRows();
        HtmlTR tr;
        if (al.size() == 0) {
            tr = new HtmlTR("");
            addTHeadRow(tr);
        }
        else tr = al.get(0); 
        tr.addTableData(htmlTh);
    }

    public void addCounterColumn(String title) {
        HtmlCol htmlCol = new HtmlCol();
        HtmlTH htmlTh = new HtmlTH();
        htmlTh.setInnerHtml(title == null ? "" : title);
        addCounterColumn(htmlCol, htmlTh);
    }
    
    public void addCounterColumn(HtmlCol htmlCol, HtmlTH htmlTh) {
        OAHtmlTableComponentInterface comp = new OAHtmlTableComponentInterface() {
            @Override
            public String getTableCellRenderer(int row) {
                String s = OAConv.toString(row+1, "#,###");
                // String s = OAString.format(""+(row+1), "R,");
                return s;
            }
            @Override
            public String getTableCellEditor(int row) {
                return getTableCellRenderer(row);
            }
        };
        addColumn(htmlCol, htmlTh, comp);
    }

    public boolean getEnabled() {
        return htmlComponent.getEnabled();
    }

    public boolean isEnabled() {
        return htmlComponent.getEnabled();
    }

    public void setEnabled(boolean b) {
        htmlComponent.setEnabled(b);
    }
    
    @Override
    protected void onSubmitBeforeLoadValues(OAFormSubmitEvent formSubmitEvent) {
        String[] ids = formSubmitEvent.getNameValueMap().get("oacommand");
        if (ids != null && ids.length == 1) {
            String id = ids[0];
            if (id.startsWith(getId()+"_")) {
                formSubmitEvent.setSubmitOAHtmlComponent(getOAHtmlComponent());
                formSubmitEvent.setSubmitHtmlElement(this);
                
                int dcnt = OAStr.dcount(id, "_");
                int row = OAConv.toInt(OAStr.field(id, "_", dcnt-1));
                int col = OAConv.toInt(OAStr.field(id, "_", dcnt));
            }
        }        
    }

    @Override
    protected void onSubmit(OAFormSubmitEvent formSubmitEvent) {
        if (formSubmitEvent.getSubmitHtmlElement() != this) return;
            
        String[] ids = formSubmitEvent.getNameValueMap().get("oacommand");
        if (ids != null && ids.length == 1) {
            String id = ids[0];
            if (id.startsWith(getId()+"_")) {
                int dcnt = OAStr.dcount(id, "_");
                int row = OAConv.toInt(OAStr.field(id, "_", dcnt-1));
                int col = OAConv.toInt(OAStr.field(id, "_", dcnt));
  
                hub.setPos(row);
            }
        }        
    }
    
    @Override
    protected String getInitializeScript() {
        addClass("oatable");
        
        StringBuilder sb = new StringBuilder();
        String js = super.getInitializeScript();
        if (js != null) sb.append(js);
        
        if (getAjaxSubmit()) {
            
            // $("table").on("click", "tr td", function() {

            sb.append("$('#" + getId() + "').on('click', 'tr td', function() {\n");
            
            //was:  sb.append("$('#" + getId() + " TR TD').click(function() {\n");
            sb.append("  $('#oacommand').val($(this).attr('id'));\n");
            sb.append("  ajaxSubmit();\n");
            sb.append("  $('#oacommand').val('');\n");
            sb.append("  $('#oaparam').val('');\n");
            sb.append("  return false;\n");
            sb.append("});\n");
        }
        
        return sb.toString();
    }
    
    
    @Override
    protected void beforeGetScript() {
        //rebuild the body rows
        getTBodyRows().clear();
        
        setVisible(oaUiControl.isVisible());
        setEnabled(oaUiControl.isEnabled());
        
        int row = 0;
        for (Object obj : hub) {
            final int r = row++;
            HtmlTR tr = new HtmlTR();
            //was: HtmlTR tr = new HtmlTR(getId()+"_row_"+r);
            addTBodyRow(tr);

            if (r == hub.getPos()) {
                tr.addClass("oatableSelected");
            }
            
            int col = 0;
            for (Column column : alColumn) {
                final int c = col++;
                
                HtmlTD td = new HtmlTD(getId() + "_" + r + "_" + c);
                String s;
                if (r == hub.getPos()) {
                    s = column.comp.getTableCellEditor(r);
                }
                else s = column.comp.getTableCellRenderer(r);
                if (s == null) s = "";
                
                td.setInnerHtml(s);
                tr.addTableData(td);
            }
        }
        
        super.beforeGetScript();
    }

    
    
    @Override
    protected String getAjaxScript(boolean bIsInitializing) {
        StringBuilder sb = new StringBuilder();
        String js = super.getAjaxScript(bIsInitializing);
        if (js != null) sb.append(js);

        sb.append("$('table#"+ getId() +" tbody tr:even').each(function(i) { if (!$(this).hasClass('oatableDisable') && !$(this).hasClass('oatableSelected')) $(this).addClass('oatableEven');});");
        sb.append("$('table#"+ getId() +" tbody tr:odd').each(function(i) { if (!$(this).hasClass('oatableDisable') && !$(this).hasClass('oatableSelected')) $(this).addClass('oatableOdd');});");
        
        return sb.toString();
    }
    
    
    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        hsSupported.add("enabled");
    }

    @Override    
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

    
}
