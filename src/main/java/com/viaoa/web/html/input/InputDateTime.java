package com.viaoa.web.html.input;

import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputDateTime extends InputRange {

    public InputDateTime(String id) {
        super(id, InputType.DateTimeLocal);
    }

}
