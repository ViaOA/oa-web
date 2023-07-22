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
        return htmlComponent.getValue();
    }
    public void setValue(String value) {
        htmlComponent.setValue(value);
    }

    public String getPlaceHolder() {
        return htmlComponent.getPlaceHolder();
    }
    public void setPlaceHolder(String placeHolder) {
        htmlComponent.setPlaceHolder(placeHolder);
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
    
    
    public String getMin() {
        return htmlComponent.getMin();
    }

    public void setMin(String val) {
        htmlComponent.setMin(val);
    }
    
    public String getMax() {
        return htmlComponent.getMax();
    }

    public void setMax(String val) {
        htmlComponent.setMax(val);
    }
    

    public String getStep() {
        return htmlComponent.getStep();
    }

    public void setStep(String val) {
        htmlComponent.setStep(val);
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
