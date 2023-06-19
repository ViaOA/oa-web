package com.viaoa.web.html.input;

import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputUrl extends InputText {

    public InputUrl(String id) {
        super(id, InputType.Url);
        oaHtmlComponent.setInputMode(InputModeType.Url);
    }
}
