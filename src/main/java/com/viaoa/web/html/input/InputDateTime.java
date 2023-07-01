package com.viaoa.web.html.input;

import com.viaoa.web.html.OAHtmlComponent.InputType;

/* Notes:

<input id="dt" type="datetime-local" name="dt" value="2023-06-29T07:45">

step="900" min="2023-01-01T00:00" max="2023-12-31T23:59"

*/

public class InputDateTime extends InputRange {

    public InputDateTime(String id) {
        super(id, InputType.DateTimeLocal);
    }

}
