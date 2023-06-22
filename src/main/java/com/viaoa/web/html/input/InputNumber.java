package com.viaoa.web.html.input;

import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputNumber extends InputRange {

    public InputNumber(String id) {
        super(id, InputType.Number);
        oaHtmlComponent.setInputMode(InputModeType.Numeric);
    }

    /**
     * The display width of the text field, number of characters wide.
     */
    public int getSize() {
        return oaHtmlComponent.getSize();
    }

    public void setSize(int val) {
        oaHtmlComponent.setSize(val);
    }
    
    public int getMinLength() {
        return oaHtmlComponent.getMinLength();
    }

    public void setMinLength(int val) {
        oaHtmlComponent.setMinLength(val);
    }

    public int getMaxLength() {
        return oaHtmlComponent.getMaxLength();
    }

    public void setMaxLength(int val) {
        oaHtmlComponent.setMaxLength(val);
    }
    
}
