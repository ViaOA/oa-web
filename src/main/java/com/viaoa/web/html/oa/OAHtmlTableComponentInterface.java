package com.viaoa.web.html.oa;

import com.viaoa.hub.Hub;
import com.viaoa.web.html.HtmlTD;

public interface OAHtmlTableComponentInterface {
    
//qqqqqqq remove these in web components    
    public default String getTableCellEditor(HtmlTD td, int row, boolean hasFocus) {
        return null;
    }
    public default String getTableCellRenderer(HtmlTD td, int row) {
        return null;
    }

//qqqqqqq this is what the OATable is calling
    public default String getTableCellEditor(Hub hubTable, HtmlTD td, int row, boolean hasFocus) {
        return getTableCellEditor(td, row, hasFocus);
    }

    public default String getTableCellRenderer(Hub hubTable, HtmlTD td, int row) {
        return getTableCellRenderer(td, row);
    }
    
}
