package com.viaoa.web.html.input;

import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/*

<input id="txtTel" type="tel" name="txtTel" inputmode="tel">

*/

public class InputTel extends InputText {

    public InputTel(String selector) {
        super(selector, InputType.Tel);
        htmlComponent.setInputMode(InputModeType.Tel);
    }

}
