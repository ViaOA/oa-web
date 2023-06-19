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
    
}
