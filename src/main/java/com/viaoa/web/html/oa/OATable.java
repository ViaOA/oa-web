package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.uicontroller.*;
import com.viaoa.util.*;
import com.viaoa.web.html.*;
import com.viaoa.web.html.input.InputCheckBox;

/*
tabindex=0 for both table and it's wrapper

dont allow hub.row to be changed if table is not enabled

*/

/**
 * Creates a table listing. Columns are added by adding HtmlElements.
 * Supports keyboard navigation and editors.
 * <p>
 * Notes:<br>
 * see styles in oa-web.css <br>
 * adds style "table-layout: fixed"<br>
 * Can be wrapped in a div that scroll css, and has sticky column header.<br>
 */
public class OATable extends HtmlElement {

    private final Hub hub;
    private final Hub hubSelect;

    private OAUITableController controlUITable;
    
    private final List<OATableColumn> alTableColumn = new ArrayList();
    private MySelectCheckBox chkSelect;
    
    private final List<Change> alChange = new ArrayList();
    
    private final boolean bAddCounterColumn;
    private boolean bIsInitialized;
//qqqqqq    private volatile boolean bIgnoreChangeAO;
    
    
    public OATable(String selector, Hub hub) {
        this(selector, hub, null, true);
    }
    public OATable(String selector, Hub hub, boolean bAddCounterColumn) {
        this(selector, hub, null, bAddCounterColumn);
    }
    public OATable(String elementIdentifier, Hub hub, Hub hubSelect) {
        this(elementIdentifier, hub, hubSelect, true);
    }

    
    public OATable(String elementIdentifier, Hub hub, Hub hubSelect, boolean bAddCounterColumn) {
        super(elementIdentifier);
        this.hub = hub;
        this.hubSelect = hubSelect;
        this.bAddCounterColumn = bAddCounterColumn;
        
        if (bAddCounterColumn) {
            OAHtmlElement he = new OAHtmlElement("spanCount", hub, "") {
                @Override
                public String getValueAsString(Hub hubFrom, Object obj) {
                    int pos = hubFrom.indexOf(obj);
                    return ""+(pos+1);
                }
            };
            he.setFormat("#,###");
            OATableColumn tc = new OATableColumn(he, "#", 5, "");
            addColumn("spanCount", tc);
        }

        if (hubSelect != null) {
            chkSelect = new MySelectCheckBox(hub, hubSelect);
            OATableColumn tc = new OATableColumn(chkSelect, "Select", 6, "");
            addColumn("chkSelect", tc);
        }
        
    }

    
    private static class MySelectCheckBox extends InputCheckBox implements OATableColumnInterface {
        private final Hub hub;
        private final Hub hubSelect;
        public MySelectCheckBox(Hub hub, Hub hubSelect) {
            super("chkSelect");
            this.hub = hub;
            this.hubSelect = hubSelect;
        }

        @Override
        public String getValueAsString(Hub hubFrom, Object obj) {
            if (hubSelect.contains(obj)) return "true";
            return "";
        }
        
        @Override
        public void setChecked(boolean b) {
            super.setChecked(b);
            Object obj = hub.getAO();
            if (obj != null) {
                if (b) hubSelect.add(obj);
                else hubSelect.remove(obj);
            }
        }
    }
    
    
    
    @Override
    public void close() {
        super.close();
        if (controlUITable != null) controlUITable.close();
    }

    
    public Hub getHub() {
        return hub;
    }
    public OAUITableController getController() {
        return controlUITable;
    }

    private int lastMultiSelectValue = -1;
    protected void updateMultiSelectCheckBox() {
        if (hubSelect == null) return;
        int x = hubSelect.size();
        int x2 = hub.size();

        int val = 0;
        if (x > 0) {
            if (x == x2) val = 2;
            else val = 1;    
        }
        
        if (lastMultiSelectValue == val) return;
        lastMultiSelectValue = val;
        
        for (Change c : alChange) {
            if (c.ct == ChangeType.updateHeaderSelectCheckBox) c.bCancel = true;
        }
        String js = "comp.updateHeaderSelectCheckBox("+val+");\n";
        Change c = new Change(ChangeType.updateHeaderSelectCheckBox, js);
        alChange.add(c);
    }

    

    protected enum ChangeType {
        newColumn,
        newList,
        changeAO,
        updateRow,
        addRow,
        removeRow,
        updateHeaderSelectCheckBox
    }
    protected static class Change {
        ChangeType ct;
        String js;
        boolean bCancel;
        int row;
        public Change(ChangeType ct, String js) {
            this.ct = ct;
            this.js = js;
        }
    }

    
    protected void initialize() {
        if (bIsInitialized) return;
        bIsInitialized = true;
        
        String[] ss = new String[0];
        for (OATableColumn tc : alTableColumn) {
            ss = OAArray.add(ss, tc.propertyPath);
        }
        
        
        // used to interact between component with hub.
        controlUITable = new OAUITableController(hub, hubSelect, ss) {
            //qqqqqqqqqq
            
            @Override
            public void changed(int row) {
                for (Change c : alChange) {
                    if (c.ct == ChangeType.updateRow && c.row == row) c.bCancel = true;
                }
                
                String s = "";
                Object obj = hub.get(row);
                for (OATableColumn tc : alTableColumn) {
                    String val = tc.editor.getValueAsString(getHub(), obj);
                    if (val == null) val = "null";
                    else val = "'" + OAStr.escapeJS(val, '\'') + "'";
                    s = OAStr.append(s, val, ",");
                }
                StringBuilder sb = new StringBuilder();
                sb.append("comp.updateRow(comp.createTableRow([" + s + "]), "+row+");\n");
                
                Change c = new Change(ChangeType.updateRow, sb.toString());
                c.row = row;
                alChange.add(c);
            }
            
            @Override
            public void add(Object obj) {
                String s = "";
                for (OATableColumn tc : alTableColumn) {
                    String val = tc.editor.getValueAsString(getHub(), obj);
                    if (val == null) val = "null";
                    else val = "'" + OAStr.escapeJS(val, '\'') + "'";
                    s = OAStr.append(s, val, ",");
                }
                s = "comp.addRow(comp.createTableRow([" + s + "]));\n";
                if (hub.getPos() == hub.getSize()-1) s += String.format("comp.setActiveFromServer(%d);\n", hub.getSize()-1);
                Change c = new Change(ChangeType.addRow, s);
                alChange.add(c);
            }
            @Override
            public void insert(Object obj, int row) {
                String s = "";
                for (OATableColumn tc : alTableColumn) {
                    String val = tc.editor.getValueAsString(getHub(), obj);
                    if (val == null) val = "null";
                    else val = "'" + OAStr.escapeJS(val, '\'') + "'";
                    s = OAStr.append(s, val, ",");
                }
                s = "comp.insertRow(comp.createTableRow([" + s + "]), "+row+");\n";
                if (hub.getPos() == row) s += String.format("comp.setActiveFromServer(%d);\n", row);
                if (hub.getSize() > row) {
                    s += "comp.renumberRows(" + (row+1) + ");\n";
                }
                Change c = new Change(ChangeType.addRow, s);
                c.row = row;
                alChange.add(c);
            }
            @Override
            public void remove(int row) {
                String s = "comp.removeRow("+row+");\n";
                if (hub.getSize() > row) {
                    s += "comp.renumberRows(" + row + ");\n";
                }
                if (hub.getPos() == row) s += String.format("comp.setActiveFromServer(%d);\n", row);
                Change c = new Change(ChangeType.removeRow, s);
                
                c.row = row;
                alChange.add(c);
            }
            
            @Override
            public void newList() {
                if (!bIsInitialized) return;
                for (Change c : alChange) {
                    if (c.ct == ChangeType.newList) c.bCancel = true;
                    if (c.ct == ChangeType.updateRow) c.bCancel = true;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("comp.clearRows();\n");
                
                sb.append("comp.loadRows([");
                boolean b = false;
                for (Object obj : getHub()) {
                    if (b) sb.append(",");
                    else b = true;
                    sb.append("[");
                    String s = "";
                    for (OATableColumn tc : alTableColumn) {
                        String val = tc.editor.getValueAsString(getHub(), obj);
                        if (val == null) val = "null";
                        else val = "'" + OAStr.escapeJS(val, '\'') + "'";
                        s = OAStr.append(s, val, ",");
                    }
                    sb.append(s + "]");
                }
                sb.append("]);");
                if (hub.getPos() >= 0) sb.append(String.format("comp.setActiveFromServer(%d);\n", hub.getPos()));
                
                Change c = new Change(ChangeType.newList, sb.toString());
                alChange.add(c);
            }
            
            @Override
            public void setChangeAO(int pos) {
                if (!bIsInitialized) return;
                for (Change c : alChange) {
                    if (c.ct == ChangeType.changeAO) c.bCancel = true;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("comp.setActiveFromServer(%d);\n", pos));
                Change c = new Change(ChangeType.changeAO, sb.toString());
                alChange.add(c);
            
                if (chkSelect != null) {
                    Object obj = hub.getAO();
                    chkSelect.setChecked(obj != null && hubSelect.contains(obj));
                }
            }
            
            int[] possLast;
            @Override
            public void setMultiSelect(int[] poss) {
                if (!bIsInitialized) return;
                if (poss == null) {
                    if (possLast == null) return;
                    poss = new int[] {};
                }
                
                if (possLast != null) {
                    for (int x : possLast) {
                        if (!OAArray.contains(poss, x)) changed(x);
                    }
                }
                for (int x : poss) {
                    if (!OAArray.contains(possLast, x)) changed(x);
                }
                possLast = poss;
                
                if (chkSelect != null) {
                    Object obj = hub.getAO();
                    chkSelect.setChecked(obj != null && hubSelect.contains(obj));
                    updateMultiSelectCheckBox();
                }
            }
        };
        
        
        // if (hubSelect != null) setMultiple(true);
        controlUITable.reset();
        controlUITable.newList();
        
    }

    
    
    protected static class OATableColumn {
        OATableColumnInterface editor; 
        String columnName;
        int colCharWidth;
        String propertyPath;

        public OATableColumn(OATableColumnInterface editor, String columnName, int colCharWidth, String propertyPath) {
            this.editor = editor;
            this.columnName = columnName;
            this.colCharWidth = colCharWidth;
            this.propertyPath = propertyPath;
        }
    }

    
    public void addColumn(String oaDataName, OATableColumnInterface editor, String columnName, int colCharWidth, String propertyPath) {
        OATableColumn tc = new OATableColumn(editor, columnName, colCharWidth, propertyPath);
        addColumn(oaDataName, tc);
    }

    
    
    public void addColumn(String oaDataName, OATableColumn tc) {
        alTableColumn.add(tc);
        
        /*        
        ele = table.element.querySelector(':scope [data-oa-name="col5"]');
        tc = new OATableColumn("Department", 15, ele);
        tc.usesUpDownArrowKeys = true;
        table.addColumn(tc);
        */
        
        if (tc.editor instanceof HtmlElement) {
            this.add( (HtmlElement) tc.editor);
        }
        
        String js = String.format("ele2 = ele.querySelector(':scope [data-oa-name=\"%s\"]');\n", oaDataName);
        js += String.format("tc = new OA.OATableColumn('%s', %d, ele2);\n", tc.columnName, tc.colCharWidth);
        
        js += String.format("tc.styles='max-width:%dch';", tc.colCharWidth);
        // js += "tc.classes = \n";
        
        js += "comp.addColumn(tc);\n";
        
        Change c = new Change(ChangeType.newColumn, js);
        alChange.add(c);
        
        if (controlUITable != null) controlUITable.newList();
    }


    
    
    
    
    /** 
     * Called when a change is necessary for UI component. 
     * */
    public void updateComponent(Object object) {
        setVisible(getController().isVisible());
        //etEnabled(getController().isEnabled());
    }
    

    public void updateLabel(Object object) {
        OAHtmlComponent lbl = getOAHtmlComponent().getLabelComponent();
        if (lbl == null) return;
        lbl.setVisible(getController().isVisible());
        lbl.setEnabled(getController().isEnabled());
    }
    
    
    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        final boolean bWasInitialized = bIsInitialized;
        
        if (!bWasInitialized) {
            initialize();
        }
        
        String jsChanges = "";
        for (Change c : alChange) {
            if (c.bCancel) continue;
            jsChanges = OAStr.append(jsChanges, c.js, "\n");
            
            if (c.ct == ChangeType.newColumn) {
                hsVars.add("tc");
                hsVars.add("ele2");
                hsVars.add("comp2");
            }
        }
        alChange.clear();
        
        bHasChanges |= jsChanges.length() > 0;
        
        String js = super.getJavaScriptForClient(hsVars, bHasChanges);
        
        if (!bWasInitialized) {
            // need to get children, before the TR that has them is removed.
            hsVars.add("ele2");
            hsVars.add("comp2");
            js = OAStr.concat(js,  "ele2 = ele; comp2 = comp;", "\n");
            for (OATableColumn tc : alTableColumn) {
                if (tc.editor instanceof HtmlElement) {
                    String js2 = ((HtmlElement) tc.editor).getJavaScriptForClient(hsVars, bHasChanges);
                    js = OAStr.concat(js,  js2, "\n");
                }
            }
            js = OAStr.concat(js,  "ele = ele2; comp = comp2;", "\n");
        }        
        
        if (!bWasInitialized) {
            String js2 = "comp.initialize();";
            js = OAStr.append(js, js2, "\n");
            js += String.format("comp.setActiveFromServer(%d);\n", getHub().getPos());
        }
        
        js = OAStr.append(js, jsChanges, "\n");
        
        return js;
    }
    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        if (OAStr.isEqual(type, Event_ClickHeaderCheckBox)) {
            if (hubSelect == null) return;
            int x = hubSelect.size();
            int x2 = hub.size();
            if (x == 0) hubSelect.add(hub);
            else if (x > 0) hubSelect.clear();
            return;
        }
        
        
        if (OAStr.isNotEqual(type, Event_SetActiveRow)) return;

        String s = map.get("newValue");
        String s2 = map.get("oldValue");
        onClientSetActiveRow(OAConv.toInt(s), OAConv.toInt(s2));
    }
    
    
    protected void onClientSetActiveRow(int newRow, int oldRow) {
        Hub hub = getHub();
        if (hub.getPos() != oldRow) return;
        try {
            hub.setPos(newRow);
        }
        finally {
        }
    }

    public Hub getSelectHub() {
        return hubSelect;
    }
    
    public Hub getMasterFilterHub() {
        return null;
        //qqqqqq return hubFilterMaster;
    }

}




