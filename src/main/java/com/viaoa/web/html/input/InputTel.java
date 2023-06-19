package com.viaoa.web.html.input;

import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputTel extends InputText {

    public InputTel(String id) {
        super(id, InputType.Tel);
        oaHtmlComponent.setInputMode(InputModeType.Tel);
    }

}
