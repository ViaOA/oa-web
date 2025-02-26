package com.viaoa.web.html.input;

import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputUrl extends InputText {

    public InputUrl(String selector) {
        this(selector, InputType.Url);
    }

    public InputUrl(String selector, InputType it) {
        super(selector, it);
        htmlComponent.setInputMode(InputModeType.Url);
    }
}
