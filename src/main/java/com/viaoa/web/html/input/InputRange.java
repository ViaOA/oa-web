package com.viaoa.web.html.input;

import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputRange extends HtmlFormElement {

    public InputRange(String id) {
        super(id, InputType.Range);
    }
    public InputRange(String id, InputType type) {
        super(id, type);
    }

    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }

    public int getMin() {
        return oaHtmlComponent.getMin();
    }

    public void setMin(int val) {
        oaHtmlComponent.setMin(val);
    }
    
    public int getMax() {
        return oaHtmlComponent.getMax();
    }

    public void setMax(int val) {
        oaHtmlComponent.setMax(val);
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

    
    public int getStep() {
        return oaHtmlComponent.getStep();
    }

    public void setStep(int val) {
        oaHtmlComponent.setStep(val);
    }
    

}