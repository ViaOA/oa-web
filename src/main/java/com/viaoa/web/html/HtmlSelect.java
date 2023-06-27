package com.viaoa.web.html;

import java.util.*;

import com.viaoa.web.html.OAHtmlComponent.ComponentType;

public class HtmlSelect extends HtmlFormElement {

//qqqqqqqqqqqqqqqqqqq ALSO create an HtmlButton qqqqqqqqqqqq with submit/reset attributes ?? qqqqqqq    
    
//qqqqqqqqqqqqqq Values stores all of the selected items qqqqqqqqqqqqqqqqqqqqqqqqqqq    
    
//qqqqqq does not support readonly
    // qqqq use disabled
    
    public HtmlSelect(String id) {
        super(id, null, ComponentType.Select);
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

 