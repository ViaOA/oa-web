package com.viaoa.web.html.oa;

import com.viaoa.hub.Hub;

/**
 * Used by Table column components (ex: OAInputText),
 * so that table can render each table cell.
 */
public interface OATableColumnInterface {
    
    public String getValueAsString(Hub hubFrom, Object obj);
    
}
