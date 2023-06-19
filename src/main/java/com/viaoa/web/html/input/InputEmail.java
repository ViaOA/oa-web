package com.viaoa.web.html.input;

import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputEmail extends InputText {

    public InputEmail(String id) {
        super(id, InputType.Email);
        oaHtmlComponent.setInputMode(InputModeType.Email);
    }

}
