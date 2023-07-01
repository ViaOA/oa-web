package com.viaoa.web.html;

import java.util.HashSet;
import java.util.Set;

import com.viaoa.web.html.OAHtmlComponent.FormElementType;

public class HtmlSelect extends HtmlFormElement {

//qqqqqqqqqqqqqq Values[] has all of the selected items qqqqqqqqqqqqqqqqqqqqqqqqqqq    
    
//qqqqqq does not support readonly, use disabled
    
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
    
    
    public HtmlSelect(String id) {
        super(id, FormElementType.Select);
    }
    
    /**
     * Size attribute is the rows to display. 
     * Use 1 for a combo/dropdown
     */
    public int getSize() {
        return oaHtmlComponent.getSize();
    }

    public void setSize(int val) {
        oaHtmlComponent.setSize(val);
    }
    
 
    public boolean getMultiple() {
        return oaHtmlComponent.getMultiple();
    }
    public void setMultiple(boolean b) {
        oaHtmlComponent.setMultiple(b);
    }

    public void add(HtmlOption option) {
        if (option != null) oaHtmlComponent.add(option);
    }
 
    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }

    public String[] getValues() {
        return oaHtmlComponent.getValues();
    }
    public void setValues(String[] values) {
        oaHtmlComponent.setValues(values);
    }

    public boolean getRequired() {
        return oaHtmlComponent.getRequired();
    }

    public boolean isRequired() {
        return oaHtmlComponent.getRequired();
    }

    public void setRequired(boolean req) {
        oaHtmlComponent.setRequired(req);
    }

}

 