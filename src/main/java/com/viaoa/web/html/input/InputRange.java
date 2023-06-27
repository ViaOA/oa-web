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
    

}
