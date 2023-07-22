package com.viaoa.web.html;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.viaoa.web.html.OAHtmlComponent.FormElementType;

public class HtmlSelect extends HtmlFormElement {
    // Note: Values[] has all of the selected items qqqqqqqqqqqqqqqqqqqqqqqqqqq    
    // Note: html select element does not support readonly, use disabled
    
    public HtmlSelect(String id) {
        super(id, FormElementType.Select);
    }
    
    /**
     * Size attribute is the rows to display. 
     * Use 1 for a combo/dropdown
     */
    public int getSize() {
        return htmlComponent.getSize();
    }

    public void setSize(int val) {
        htmlComponent.setSize(val);
    }

    /**
     * Html "rows" attribute for the number of rows displayed. 
     */
    public int getDisplayRows() {
        return htmlComponent.getSize();
    }

    public void setDisplayRows(int rowsToDisplay) {
        htmlComponent.setSize(rowsToDisplay);
    }
    
 
    public boolean getMultiple() {
        return htmlComponent.getMultiple();
    }
    public void setMultiple(boolean b) {
        htmlComponent.setMultiple(b);
    }

    public void add(HtmlOption option) {
        if (option != null) htmlComponent.add(option);
    }
    public void clearOptions() {
        htmlComponent.clearOptions();
    }
    public List<HtmlOption> getOptions() {
        return htmlComponent.getOptions();
    }
    
    public String getValue() {
        return htmlComponent.getValue();
    }
    public void setValue(String value) {
        htmlComponent.setValue(value);
    }

    public String[] getValues() {
        return htmlComponent.getValues();
    }
    public void setValues(String[] values) {
        htmlComponent.setValues(values);
    }

    public boolean getRequired() {
        return htmlComponent.getRequired();
    }

    public boolean isRequired() {
        return htmlComponent.getRequired();
    }

    public void setRequired(boolean req) {
        htmlComponent.setRequired(req);
    }

    
    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("size");
        hsSupported.add("multiple");
        hsSupported.add("required");
        hsSupported.add("value");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
}

 