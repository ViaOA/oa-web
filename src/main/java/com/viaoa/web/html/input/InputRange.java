package com.viaoa.web.html.input;

import java.util.*;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/*
<label id="lbl">Test InputRange <input id="txtRange" type="Range" name="txtRange" min=25 max=500 step=25 value="100"></label>

 */

public class InputRange extends InputElement {
    
    public InputRange(String selector) {
        super(selector, InputType.Range);
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
    
    public String getFloatLabel() {
        return htmlComponent.getFloatLabel();
    }

    public void setFloatLabel(String floatLabel) {
        htmlComponent.setFloatLabel(floatLabel);
    }
    
    public String getMin() {
        return htmlComponent.getMin();
    }
    public void setMin(String min) {
        htmlComponent.setMin(min);
    }
    
    public String getMax() {
        return htmlComponent.getMax();
    }
    public void setMax(String max) {
        htmlComponent.setMax(max);
    }
    public String getStep() {
        return htmlComponent.getStep();
    }
    public void setStep(String step) {
        htmlComponent.setStep(step);
    }
    
    
    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("size");
        hsSupported.add("minlength");
        hsSupported.add("maxlength");
        hsSupported.add("floatlabel");
        hsSupported.add("min");
        hsSupported.add("max");
        hsSupported.add("step");
    }

    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

}


