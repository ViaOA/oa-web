package com.viaoa.web.html.input;

import java.util.*;

import com.viaoa.web.html.OAHtmlComponent.InputType;

/*

<input id="color" type="color" name="color" value="#cba9b2">

*/

public class InputColor extends InputElement {

    public InputColor(String selector) {
        super(selector, InputType.Color);
    }

    public String getValue() {
        return htmlComponent.getValue();
    }
    public void setValue(String value) {
        htmlComponent.setValue(value);
    }

    /**
     * Id of datalist element, of "#AA00BB" colors.    
     */
    public String getList() {
        return htmlComponent.getList();
    }

    public void setList(String listId) {
        htmlComponent.setList(listId);
    }
    
    public List<String> getDataList() {
        return htmlComponent.getDataList();
    }

    public void setDataList(List<String> lst) {
        htmlComponent.setDataList(lst);
    }

    public String getFloatLabel() {
        return htmlComponent.getFloatLabel();
    }

    public void setFloatLabel(String floatLabel) {
        htmlComponent.setFloatLabel(floatLabel);
    }
    

    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("value");
        hsSupported.add("list");  // id of existing element datalist  
        hsSupported.add("datalist");  // new datalist to use
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
}
