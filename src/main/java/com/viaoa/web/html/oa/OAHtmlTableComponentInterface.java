package com.viaoa.web.html.oa;

import com.viaoa.web.html.HtmlTD;

public interface OAHtmlTableComponentInterface {
    public String getTableCellEditor(HtmlTD td, int row, boolean hasFocus);
    public String getTableCellRenderer(HtmlTD td, int row);
}
