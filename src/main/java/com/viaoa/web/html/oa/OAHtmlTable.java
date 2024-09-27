package com.viaoa.web.html.oa;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.viaoa.hub.Hub;
import com.viaoa.object.OAObject;
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
import com.viaoa.web.html.OAHtmlComponent.OverflowType;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * Creates a table listing. Columns are added by adding HtmlElements.
 * Supports keyboard navigation and editors.
 * <p>
 * Notes:<br>
 * see styles in oaweb.css <br>
 * adds style "table-layout: fixed"<br>
 * Can be wrapped in a div that scroll css, and has sticky column header.<br>
 */
public class OAHtmlTable<T extends OAObject> extends HtmlTable implements OAHtmlComponentInterface {
    private final OAUISelectController oaUiControl;
    private final List<Column> alColumn = new ArrayList<>();

    private static class LastRefresh {
        Hub hubUsed;
        OAObject objSelected;

        // if hub is linked, then this is the current object that it's linked to and changing.
        OAObject objLinkedTo;  
    }
    private final LastRefresh lastRefresh = new LastRefresh();
    
    
    private int submitRow=-1, submitCol=-1;
    private String submitKeys;
    
    public OAHtmlTable(String id, Hub<T> hub) {
        super(id);
        
        // used to interact between component with hub.
        oaUiControl = new OAUISelectController(hub) {
            @Override
            protected void onCompleted(String completedMessage, String title) {
                OAForm form = getForm();
                if (form != null) {
                    form.addMessage(completedMessage);
                    form.addConsoleMessage(title + " - " + completedMessage);
                }
            }

            @Override
            protected void onError(String errorMessage, String detailMessage) {
                OAForm form = getForm();
                if (form != null) {
                    form.addError(errorMessage);
                    form.addConsoleMessage(errorMessage + " - " + detailMessage);
                }
            }
        };
        
        setAjaxSubmit(true);
        getOAHtmlComponent().addClass("oatable table table-bordered table-hover table-striped");
        
        // addStyle("table-layout", "fixed");
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
        addColumn(title, comp, -1);
    }
    
    /**
     * Add a new column using an HTML Element.
     * @param title heading title
     * @param comp OAWeb component 
     * @param width width of the column, using "ch" as the units.
     */
    public void addColumn(String title, OAHtmlTableComponentInterface comp, int width) {
        if (comp instanceof HtmlElement) {
            add((HtmlElement) comp);
        }
    
        HtmlCol htmlCol = new HtmlCol();
        if (width >= 0) {
            htmlCol.setWidth(width+"ch");
            // htmlCol.setOverflow(OverflowType.Hidden); // not needed, there is a CSS class to handle this
        }
        
        HtmlTH htmlTh = new HtmlTH();
        htmlTh.setInnerHtml(title == null ? "" : title);
        
        addColumn(htmlCol, htmlTh, comp);
    }
    
    public Column getColumn(int pos) {
        if (pos < 0 || pos >= this.alColumn.size()) return null;
        return this.alColumn.get(pos);
    }
    
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
        
        htmlTh.addClass("table-primary");
    }

    public void addCounterColumn() {
        addCounterColumn("#");
    }
    
    /**
     * Creates a column for row count, that uses class "oatableColumnCount".
     */
    public void addCounterColumn(String title) {
        HtmlCol htmlCol = new HtmlCol();
        htmlCol.addClass("oatableColumnCount");
        htmlCol.addStyle("width", "3ch");

        HtmlTH htmlTh = new HtmlTH();
        htmlTh.setInnerHtml(title == null ? "" : title);
        htmlTh.addClass("oatableColumnCount");

        addCounterColumn(htmlCol, htmlTh);
    }
    
    public void addCounterColumn(HtmlCol htmlCol, HtmlTH htmlTh) {
        
        OAHtmlTableComponentInterface comp = new OAHtmlTableComponentInterface() {
            @Override
            public String getTableCellRenderer(HtmlTD td, int row) {
                String s = OAConv.toString(row+1, "#,###");
                return s;
            }
            @Override
            public String getTableCellEditor(HtmlTD td, int row, boolean bHasFocus) {
                String s = getTableCellRenderer(td, row);
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
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        // verify that hubs "have not moved", when using detailHub, linkHub, etc
        if (getHub().getRealHub() != lastRefresh.hubUsed) {
            if (lastRefresh.objLinkedTo == null) return; // it was not linked, so dont change AO
        }
        
        if (lastRefresh.hubUsed != getHub().getRealHub()) {
            formSubmitEvent.addSyncError("OAHtmlTable list changed");
        }
        else {
            Hub h = getHub().getLinkHub(true);
            if (h != null) {
                if (lastRefresh.objLinkedTo != h.getAO()) {
                    formSubmitEvent.addSyncError("OAHtmlTable link to changed");
                }
            }
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
        
        Object obj = getHub().get(submitRow);
        oaUiControl.onAOChange(lastRefresh.objLinkedTo, lastRefresh.objSelected, obj);
    }
    
    
    @Override
    protected String getInitializeScript() {
        // addClass("oatable");
        
        StringBuilder sb = new StringBuilder();
        String js = super.getInitializeScript();
        if (js != null) sb.append(js);
        
        if (getAjaxSubmit()) {
            sb.append("$('#"+getId()+"').on('focus', 'tbody tr td', function(event) {\n");
            sb.append("  if ($(this).parent().hasClass('oatableSelected')) return true;\n");
            sb.append("  $('#oacommand').val($(this).attr('id'));\n");
            sb.append("  ajaxSubmit();\n");
            sb.append("  $('#oacommand').val('');\n");
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

        
        // need to send before cell editor components are initialized
        js = createTableScript();
        sb.append(js);
        
        return sb.toString();
    }
    
    
    public Hub<T> getHub() {
        return oaUiControl.getHub();
    }

    public OAUISelectController getController() {
        return oaUiControl;
    }
    
    
    @Override
    protected void beforeGetScript() {
        setVisible(oaUiControl.isVisible());
        setEnabled(oaUiControl.isEnabled());
        
        lastRefresh.hubUsed = getHub().getRealHub();
        lastRefresh.objSelected = (OAObject) getHub().getAO();
        final int pos = getHub().getPos();
        
        Hub h = getHub().getLinkHub(true);
        if (h != null) {
            lastRefresh.objLinkedTo = (OAObject) h.getAO();
        }
        else lastRefresh.objLinkedTo = null;
        
        
        //rebuild the body rows
        getTBodyRows().clear();
        
        int row = 0;
        for (Object obj : lastRefresh.hubUsed) {
            final int r = row++;
            HtmlTR tr = new HtmlTR(getId()+"_"+r);
            addTBodyRow(tr);

            if (r == pos) {
                tr.addClass("oatableSelected");
                tr.addClass("table-success");
            }
            
            int col = 0;
            for (Column column : alColumn) {
                final int c = col++;
                
                HtmlTD td = new HtmlTD(getId() + "_" + r + "_" + c);
                
                for (String s : column.htmlCol.getClasses()) td.addClass(s);

    //qqqqqqqqqqqqqqqqqqqq                
//dont use style:overflow for bsDT components qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq

//qqqqqqq this is needed by date components popup .....
                //  needs to be put on div ??
//td.addStyle("position", "relative");
        
                td.setTabIndex(0);
                String s;
                
                if (r == pos) {
                    if (column.comp instanceof HtmlElement) {
                        ((HtmlElement) column.comp).getOAHtmlComponent().setNeedsRefreshed(true); // since it will be removed, and then re-added to the page/dom.
                    }
                    s = column.comp.getTableCellEditor(getHub(), td, r, (r == submitRow && c == submitCol));
                    if (r == submitRow && c == submitCol && (column.comp instanceof HtmlFormElement)) {
                        ((HtmlFormElement) column.comp).setFocus();
                    }
                }
                else if (!column.htmlCol.getVisible() || column.htmlCol.getHidden()) s = "";
                else s = column.comp.getTableCellRenderer(getHub(), td, r);
                
                if (s == null) s = "";
                td.setInnerHtml(s);
                tr.addTableData(td);
            }
        }
        
        if (pos < 0) {
            final int r = row;
            HtmlTR tr = new HtmlTR(getId()+"_"+r);
            tr.addStyle("display", "none");
            tr.setHidden(true);
            addTBodyRow(tr);

            int col = 0;
            for (Column column : alColumn) {
                final int c = col++;
                
                HtmlTD td = new HtmlTD(getId() + "_" + r + "_" + c);

                if (column.comp instanceof HtmlElement) {
                    ((HtmlElement) column.comp).getOAHtmlComponent().setNeedsRefreshed(true); // since it will be removed, and then re-added to the page/dom.
                }
                
                String s = column.comp.getTableCellEditor(getHub(), td, r, (r == submitRow && c == submitCol));
                if (s == null) s = "";
                td.setInnerHtml(s);
                tr.addTableData(td);
            }
        }
        super.beforeGetScript();
    }

    
    
    @Override
    protected String getAjaxScript(final boolean bIsInitializing) {
        if (bIsInitializing) return null; // getInitializeScript() already calls createTableScript 
        return createTableScript();
    }
    
    @Override
    protected String createTableScript() {
        String width = getWidth();
        boolean bHadWidth = OAStr.isNotEmpty(width);
        if (!bHadWidth) {  // calculate width
            int w = 0;
            String units = null;
            boolean bBadUnits = false;

            String regex = "^(\\d+)(.*)";
            // Create a Pattern object from the regex
            Pattern pattern = Pattern.compile(regex);
            // Create a Matcher object to find the number prefix in the input string
            
            for (Column column : alColumn) {
                String s = column.htmlCol.getWidth();
                if (OAStr.isEmpty(s)) {
                    bBadUnits = true;
                    break;
                }
                Matcher matcher = pattern.matcher(s);
                
                // Check if the regex pattern matches the input string
                if (matcher.matches()) {
                    // Group 1 captures the number prefix, and Group 2 captures the remaining text
                    s = matcher.group(1);
                    w += OAConv.toInt(s);

                    String s2 = matcher.group(2);
                    if (units == null) units = s2;
                    else if (!units.equalsIgnoreCase(s2)) {
                        bBadUnits = true;
                        break;
                    }
                }
                
            }
            
            if (!bBadUnits && units != null) {
                addStyle("width", w+units);
            }
        }
        StringBuilder sb = new StringBuilder();
        
        String js = super.createTableScript();
        if (js != null) sb.append(js);

        if (submitRow >= 0 && submitCol >= 0) {
            sb.append("$('#"+ getId() +"_" + submitRow + "_"+ submitCol + "').focus();\n");
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
