package com.viaoa.web.html.input;

import java.util.*;

import com.viaoa.hub.Hub;
import com.viaoa.web.html.OAHtmlComponent.*;
import com.viaoa.web.html.oa.OATableColumnInterface;

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

    public String getColor() {
        return getValue();
    }
    public void setColor(String color) {
        setValue(color);
    }
    
    
    public String getPattern() {
        return htmlComponent.getPattern();
    }

    public void setPattern(String pattern) {
        htmlComponent.setPattern(pattern);
    }
    
    public boolean getReadOnly() {
        return htmlComponent.getReadOnly();
    }

    public boolean isReadOnly() {
        return htmlComponent.getReadOnly();
    }
    public void setReadOnly(boolean b) {
        htmlComponent.setReadOnly(b);
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
    
    public String getInputMode() {
        return htmlComponent.getInputMode();
    }

    public void setInputMode(String mode) {
        htmlComponent.setInputMode(mode);
    }

    public void setInputMode(InputModeType type) {
        htmlComponent.setInputMode(type);
    }
    
    public String getPlaceHolder() {
        return htmlComponent.getPlaceHolder();
    }
    public void setPlaceHolder(String placeHolder) {
        htmlComponent.setPlaceHolder(placeHolder);
    }

    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("pattern");
        hsSupported.add("readonly");
        hsSupported.add("required");
        hsSupported.add("list");
        hsSupported.add("datalist");
        hsSupported.add("floatlabel");
        hsSupported.add("inputmode");
        hsSupported.add("placeholder");
    }

    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

}
