package com.viaoa.web.html.input;

import java.util.*;

import com.viaoa.web.html.OAHtmlComponent.*;

public class InputPassword extends InputElement {
    public InputPassword(String selector) {
        super(selector, InputType.Password);
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
     * The display width of the text field, number of characters wide.
     */
    public int getSize() {
        return htmlComponent.getSize();
    }

    public void setSize(int val) {
        htmlComponent.setSize(val);
    }
    
    public int getMinLength() {
        return htmlComponent.getMinLength();
    }

    public void setMinLength(int val) {
        htmlComponent.setMinLength(val);
    }

    public int getMaxLength() {
        return htmlComponent.getMaxLength();
    }

    public void setMaxLength(int val) {
        htmlComponent.setMaxLength(val);
    }

    public String getAutoComplete() {
        return htmlComponent.getAutoComplete();
    }
    public void setAutoComplete(String val) {
        htmlComponent.setAutoComplete(val);
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
        hsSupported.add("readonly");
        hsSupported.add("required");
        hsSupported.add("size");
        hsSupported.add("minlength");
        hsSupported.add("maxlength");
        hsSupported.add("autocomplete");
        hsSupported.add("floatlabel");
        hsSupported.add("inputmode");
        hsSupported.add("placeholder");
    }

    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
}
