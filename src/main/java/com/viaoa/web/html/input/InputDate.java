package com.viaoa.web.html.input;

import com.viaoa.web.html.OAHtmlComponent.InputType;

/*

<input id="date" type="date" name="date" value="2023-06-30">

*/

public class InputDate extends InputRange {

    public InputDate(String id) {
        super(id, InputType.Date);
    }

}
