package com.viaoa.web.html.input;

import java.util.HashSet;
import java.util.Set;

import com.viaoa.web.html.OAHtmlComponent.InputType;

/*
<label id="lbl">Test InputRange <input id="txtRange" type="Range" name="txtRange" min=25 max=500 step=25 value="100"></label>

 */

public class InputRange extends InputElement {
    
    public InputRange(String id) {
        super(id, InputType.Range);
    }
    protected InputRange(String id, InputType type) {
        super(id, type);
    }

    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }

    public String getPlaceHolder() {
        return oaHtmlComponent.getPlaceHolder();
    }
    public void setPlaceHolder(String placeHolder) {
        oaHtmlComponent.setPlaceHolder(placeHolder);
    }
    
    public boolean getReadOnly() {
        return oaHtmlComponent.getReadOnly();
    }

    public boolean isReadOnly() {
        return oaHtmlComponent.getReadOnly();
    }
    public void setReadOnly(boolean b) {
        oaHtmlComponent.setReadOnly(b);
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
    
    
    public String getMin() {
        return oaHtmlComponent.getMin();
    }

    public void setMin(String val) {
        oaHtmlComponent.setMin(val);
    }
    
    public String getMax() {
        return oaHtmlComponent.getMax();
    }

    public void setMax(String val) {
        oaHtmlComponent.setMax(val);
    }
    

    public String getStep() {
        return oaHtmlComponent.getStep();
    }

    public void setStep(String val) {
        oaHtmlComponent.setStep(val);
    }
    
    public String getFloatLabel() {
        return oaHtmlComponent.getFloatLabel();
    }

    public void setFloatLabel(String floatLabel) {
        oaHtmlComponent.setFloatLabel(floatLabel);
    }
    
    
    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("value");
        hsSupported.add("placeholder");
        hsSupported.add("readonly");
        hsSupported.add("required");
        hsSupported.add("min");
        hsSupported.add("max");
        hsSupported.add("step");
        hsSupported.add("floatlabel");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
}
