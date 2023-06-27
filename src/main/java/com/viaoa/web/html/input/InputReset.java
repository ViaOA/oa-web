package com.viaoa.web.html.input;

import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputReset extends HtmlFormElement {

    public InputReset(String id) {
        super(id, InputType.Reset);
    }

    public String getButtonText() {
        return oaHtmlComponent.getValue();
    }
    public void setButtonText(String value) {
        oaHtmlComponent.setValue(value);
    }
    
    
    // the button text
    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }
    
}
