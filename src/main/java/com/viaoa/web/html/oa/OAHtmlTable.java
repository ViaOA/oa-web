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
import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.HtmlTH;
import com.viaoa.web.html.HtmlTR;
import com.viaoa.web.html.HtmlTable;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.EventType;
import com.viaoa.web.html.form.OAForm;
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
        getOAHtmlComponent().addClass("oatable");
    }
    
    public static class Column {
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
        if (comp instanceof HtmlElement) {
            add((HtmlElement) comp);
        }
    
        HtmlCol htmlCol = new HtmlCol();
        HtmlTH htmlTh = new HtmlTH();
        htmlTh.setInnerHtml(title == null ? "" : title);
        addColumn(htmlCol, htmlTh, comp);
    }
    
    public Column getColumn(int pos) {
        if (pos < 0 || pos >= this.alColumn.size()) return null;
        return this.alColumn.get(pos);
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
        htmlTh.addClass("oatableColumnCount");
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
            public String getTableCellEditor(int row, boolean bHasFocus) {
                //String s = "<input type=text value='"+(row+1)+"' onkeypress='(event) {event.preventDefault(); return false;}'";
                // if (bHasFocus) s += " autofocus";
                //s += ">";
                String s = getTableCellRenderer(row);
                return s;
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
    
    private int submitRow=-1, submitCol=-1;
    private String submitKeys;
    
    @Override
    protected void onSubmitBeforeLoadValues(OAFormSubmitEvent formSubmitEvent) {
        submitCol = submitRow = -1;
        String[] ids = formSubmitEvent.getNameValueMap().get("oacommand");
        if (ids != null && ids.length == 1) {
            String id = ids[0];
            if (id.startsWith(getId()+"_")) {
                formSubmitEvent.setSubmitOAHtmlComponent(getOAHtmlComponent());
                formSubmitEvent.setSubmitHtmlElement(this);
                
                int dcnt = OAStr.dcount(id, "_");
                submitRow = OAConv.toInt(OAStr.field(id, "_", dcnt-1));
                submitCol = OAConv.toInt(OAStr.field(id, "_", dcnt));
            }
        }
        
        submitKeys = null;
        String[] params = formSubmitEvent.getNameValueMap().get("oaparam");
        if (params != null && params.length == 1) {
            submitKeys = params[0];
        }
    }

    @Override
    protected void onSubmit(OAFormSubmitEvent formSubmitEvent) {
        if (formSubmitEvent.getSubmitHtmlElement() != this) return;
            
        if (submitRow >=0 && submitRow >= 0 && OAStr.isNotEmpty(submitKeys)) {
            if (submitKeys.indexOf(OAForm.Key_UP) >= 0) submitRow--;
            else if (submitKeys.indexOf(OAForm.Key_DOWN) >= 0) submitRow++;
            else if (submitKeys.indexOf(OAForm.Key_LEFT) >= 0) submitCol--;
            else if (submitKeys.indexOf(OAForm.Key_RIGHT) >= 0) submitCol++;
        }
        hub.setPos(submitRow);
    }
    
    
    
    
    @Override
    protected String getInitializeScript() {
        addClass("oatable");
        
        StringBuilder sb = new StringBuilder();
        String js = super.getInitializeScript();
        if (js != null) sb.append(js);
        
        if (getAjaxSubmit()) {
            sb.append("$('#"+getId()+"').on('focus', 'tbody tr td', function(event) {\n");
            sb.append("  if ($(this).parent().hasClass('oatableSelected')) return true;\n");
            sb.append("  $('#oacommand').val($(this).attr('id'));\n");
            sb.append("  ajaxSubmit();\n");
            sb.append("  return true;\n");
            sb.append("});\n");
        }
            
        // add nav keys event
        sb.append("$('#" + getId() + "').on('keydown', 'tr td', function(event) {\n");

        sb.append("  var ss = $(this).attr('id').split('_');\n");
        sb.append("  if (ss == undefined) return true;\n");
        sb.append("  var row = ss[ss.length -2];\n");
        sb.append("  var col = ss[ss.length -1];\n");
        sb.append("  const totalRows = event.delegateTarget.tBodies[0].rows.length;\n");
        sb.append("  const totalCols = event.delegateTarget.tBodies[0].rows[0].cells.length;\n");
        
        sb.append("  var keys = '';\n");
        sb.append("  if (event.shiftKey) keys += '[SHIFT]';\n");
        sb.append("  if (event.ctrlKey) keys += '[CTRL]';\n");
        sb.append("  if (event.altKey) keys += '[ALT]';\n");
        sb.append("  if (keys.length > 0) {\n");
        sb.append("    keys = 'KEYS='+keys;\n");
        sb.append("  }\n");
        sb.append("  var bEnd = true;\n");
        sb.append("  var bBeg = true;\n");
        sb.append("  if (event.target.tagName === 'INPUT' && event.target.type === 'text') {\n");
        sb.append("    var p1 = event.target.selectionStart;\n");
        sb.append("    var p2 = event.target.selectionEnd;\n");
        sb.append("    if (p2 < p1) {\n");
        sb.append("      var p3 = p1;\n");
        sb.append("      p1 = p2;\n");
        sb.append("      p2 = p3;\n");
        sb.append("    }\n");
        sb.append("    bBeg = p1 == 0 && p2 == 0;\n");
        sb.append("    bEnd = p2 == event.target.value.length && p1 == p2;\n");
        sb.append("  }\n");

        sb.append("  switch (event.which) {\n");
        sb.append("  case 38: keys += '[UP]';\n");
        sb.append("    if (row == 0) return false;\n");
        sb.append("    row--;\n");
        sb.append("    break\n");
        sb.append("  case 33: keys += '[PGUP]';\n");
        sb.append("    if (row == 0) return false;\n");
        sb.append("    row=0;\n");
        sb.append("    break;\n");
        sb.append("  case 40: keys += '[DOWN]';\n");
        sb.append("    if (row+1 == totalRows) return false;\n");
        sb.append("    row++;\n");
        sb.append("    break;\n");
        sb.append("  case 34: keys += '[PGDN]';\n");
        sb.append("    if (row+1 == totalRows) return false;\n");
        sb.append("    row = totalRows-1;\n");
        sb.append("    break;\n");
        sb.append("  case 39: keys += '[RIGHT]';\n");
        sb.append("    if (col+1 == totalCols) return true;\n");
        sb.append("    if (!bEnd) return true;\n");
        sb.append("    col++;\n");
        sb.append("    break;\n");
        sb.append("  case 35: keys += '[END]';\n");
        sb.append("    if (col+1 == totalCols) return true;\n");
        sb.append("    if (!bEnd) return true;\n");
        sb.append("    col = totalCols-1;\n");
        sb.append("    break;\n");
        sb.append("  case 36: keys += '[HOME]';\n");
        sb.append("    if (col == 0) return false;\n");
        sb.append("    if (!bBeg) return true;\n");
        sb.append("    col = 0;\n");
        sb.append("    break;\n");
        sb.append("  case 37: keys += '[LEFT]';\n");
        sb.append("    if (col == 0) return false;\n");
        sb.append("    if (!bBeg) return true;\n");
        sb.append("    col--;\n");
        sb.append("    break;\n");
        sb.append("  default:\n");
        sb.append("    return true;\n");
        sb.append("  }  \n");

        /*
        sb.append("  case 27: keys += '[ESC]';break;\n");
        sb.append("  case 8: keys += '[BACK]';break;\n");
        sb.append("  case 45: keys += '[INS]';break;\n");
        sb.append("  case 46: keys += '[DEL]';break;\n");
        
        sb.append("  default: keys += String.fromCharCode(event.which);break;\n");
        */

        sb.append("  $('#"+getId()+"_'+row+'_'+col).focus();\n");
        sb.append("  return false;\n");
        sb.append("});\n");

        
        
        
        
        //qqqqqqqqqqqqqqqq OLD
        if (false && getAjaxSubmit()) { 
            // mouse click event
            sb.append("$('#" + getId() + "').on('click', 'tr td', function(event) {\n");
            
            // check if TR is the selected row  
            sb.append("  if ($(this).parent().hasClass('oatableSelected')) {"); 
            sb.append("      if (!event.shiftKey && !event.ctrlKey) return false;");  
            sb.append("  }");  

            sb.append("  $('#oacommand').val($(this).attr('id'));\n");
            
            sb.append("  var keys = 'KEYS=';\n"); 
            sb.append("  if (event.shiftKey) keys += '[SHIFT]';\n"); 
            sb.append("  if (event.ctrlKey) keys += '[CTRL]';\n"); 
            sb.append("  if (event.ctrlKey) keys += '[ALT]';\n"); 
            sb.append("  $('#oaparam').val(keys);\n");
            
            sb.append("  ajaxSubmit();\n");
            sb.append("  $('#oacommand').val('');\n");
            sb.append("  $('#oaparam').val('');\n");
            sb.append("  return false;\n");
            sb.append("});\n");
            
            // add nav keys event
            sb.append("$('#" + getId() + "').on('keydown', 'tr td', function(event) {\n");

            sb.append("  var ss = $(this).attr('id').split('_');\n");
            sb.append("  if (ss == undefined) return true;\n");
            sb.append("  var row = ss[ss.length -2];\n");
            sb.append("  var col = ss[ss.length -1];\n");
            
            
            sb.append("  if (!$(this).parent().hasClass('oatableSelected')) return false;\n"); 
            sb.append("  var keys = 'KEYS=';\n"); 
            sb.append("  if (event.shiftKey) keys += '"+OAForm.Key_Shift+"';\n"); 
            sb.append("  if (event.ctrlKey) keys += '"+OAForm.Key_CTRL+"';\n");
            sb.append("  if (event.altKey) keys += '"+OAForm.Key_ALT+"';\n");

            sb.append("  var bEnd = true;\n");
            sb.append("  var bBeg = true;\n");
            sb.append("  if (event.target.tagName === 'INPUT' && event.target.type === 'text') {;\n");
            sb.append("    bBeg = (event.target.selectionEnd == 0 || event.target.selectionStart == 0);\n");
            sb.append("    bEnd = (event.target.selectionEnd >= event.target.value.length || event.target.selectionStart >= event.target.value.length);\n");
            sb.append("  }\n");
            
            sb.append("  switch (event.which) {\n");
            sb.append("  case 38: keys += '[UP]';\n");
            sb.append("    if (row == 0) return false;\n");
            sb.append("    break\n");
            sb.append("  case 33: keys += '[PGUP]';\n");
            sb.append("    if (row == 0) return false;\n");
            sb.append("    break;\n");
            sb.append("  case 40: keys += '[DOWN]';\n");
            sb.append("    if (row+1 == "+hub.getSize()+") return false;\n");
            sb.append("    break;\n");
            sb.append("  case 34: keys += '[PGDN]';\n");
            sb.append("    if (row+1 == "+hub.getSize()+") return false;\n");
            sb.append("    break;\n");
            sb.append("  case 39: keys += '[RIGHT]';\n");
            sb.append("    if (col == "+alColumn.size()+") return true;\n");
            sb.append("    if (!bEnd) return true;\n");
            sb.append("    break;\n");
            sb.append("  case 35: keys += '[END]';\n");
            sb.append("    if (col == "+alColumn.size()+") return true;\n");
            sb.append("    if (!bEnd) return true;\n");
            sb.append("    break;\n");
            sb.append("  case 36: keys += '[HOME]';\n");
            sb.append("    if (col == 0) return false;\n");
            sb.append("    if (!bBeg) return true;\n");
            sb.append("    break;\n");
            sb.append("  case 37: keys += '[LEFT]';\n");
            sb.append("    if (col == 0) return false;\n");
            sb.append("    if (!bBeg) return true;\n");
            sb.append("    break;\n");

            sb.append("  default:\n");
            sb.append("    if (!event.target.hasAttribute('id')) return false;\n");
            sb.append("    return true;\n");
            sb.append("  }  \n");

            /*
            sb.append("  case 27: keys += '[ESC]';break;\n");
            sb.append("  case 8: keys += '[BACK]';break;\n");
            sb.append("  case 45: keys += '[INS]';break;\n");
            sb.append("  case 46: keys += '[DEL]';break;\n");
            
            sb.append("  default: keys += String.fromCharCode(event.which);break;\n");
            */

            sb.append("  $('#oaparam').val(keys);\n");
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
            HtmlTR tr = new HtmlTR(getId()+"_"+r);
            addTBodyRow(tr);

            if (r == hub.getPos()) {
                tr.addClass("oatableSelected");
            }
            
            int col = 0;
            for (Column column : alColumn) {
                final int c = col++;
                
                HtmlTD td = new HtmlTD(getId() + "_" + r + "_" + c);
                td.setTabIndex(0);
                String s;
                
//qqqqqqqqqqqqqqqqqqqqqqqqqqq
/*               
 
dont use height qqqqqqq

 td {border: 1px solid black;height: 50px;padding: 5px;position: relative;}

    input[type="text"] {
      width: 100%; height: 100%;border: none;box-sizing: border-box;padding: 5px;position: absolute;top: 0;left: 0;
    }                
                
*/     
                
/*                
//qqqqqqq ctrl+mouseclick needs to unset selected line AO
  
table commands
keys:  up, down, left, right, pgUp, pgDown, home, end

shift+control
mouse click                
                
*/                
                
                if (r == hub.getPos()) {
                    if (column.comp instanceof HtmlElement) {
                        ((HtmlElement) column.comp).getOAHtmlComponent().setNeedsRefreshed(true); // since it will be removed, and then re-added to the page/dom.
                    }
                    s = column.comp.getTableCellEditor(r, (r == submitRow && c == submitCol));
                    if (r == submitRow && c == submitCol && (column.comp instanceof HtmlFormElement)) {
                        ((HtmlFormElement) column.comp).setFocus();
                    }
                }
                else s = column.comp.getTableCellRenderer(r);
                if (s == null) s = "";
                
                td.setInnerHtml(s);
                tr.addTableData(td);
            }
        }
        
        
//qqqqqqqqq if click on AO row, then dont call ajax        
// qqqqqqqq if there is no AO row, then need to do what?? qqqqqqqqqqqqqqqqqqqqqq
// qqqq selecting row needs to set focus on cell clicked and select all text        
        
        
        if (hub.getPos() < 0) {
            
            HtmlTR tr = new HtmlTR();
            // tr.setVisible(false);
            tr.setHidden(true);
            addTBodyRow(tr);
            
            for (Column column : alColumn) {
                HtmlTD td = new HtmlTD();
                String s = column.comp.getTableCellEditor(-1, false);
                td.setInnerHtml(s);
                tr.addTableData(td);
                if (column.comp instanceof HtmlElement) {
                    ((HtmlElement) column.comp).getOAHtmlComponent().setNeedsRefreshed(true); // since it will be removed, and then readded to the page/dom.
                }
            }            
        }

        super.beforeGetScript();
    }

    
    
    @Override
    protected String getAjaxScript(boolean bIsInitializing) {
        StringBuilder sb = new StringBuilder();
        String js = super.getAjaxScript(bIsInitializing);
        if (js != null) sb.append(js);

        //qqqqqqqqqqqqqqqqqqqqqq update rows only ..... dont redo full table ??
//        $("#table tbody tr:eq(0) td:eq(0)").html("TEXTXXX");        
        
        sb.append("$('table#"+ getId() +" tbody tr:even').each(function(i) { if (!$(this).hasClass('oatableDisable') && !$(this).hasClass('oatableSelected')) $(this).addClass('oatableEven');});\n");
        sb.append("$('table#"+ getId() +" tbody tr:odd').each(function(i) { if (!$(this).hasClass('oatableDisable') && !$(this).hasClass('oatableSelected')) $(this).addClass('oatableOdd');});\n");
        
//qqqqqqqqqqqqqqqq        
        if (submitRow >= 0 && submitCol >= 0) {
            sb.append("$('#"+ getId() +"_" + submitRow + "_"+ submitCol + ").focus();\n");
        }
        submitRow = submitCol = -1;

        
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
