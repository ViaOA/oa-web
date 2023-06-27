package com.viaoa.web.html.input;

import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputType;


// <input id="txtTime" type="time" name="txtTime">
//  07:09 PM   value = 17:09


/**
 * 
 * 
 *
 */
public class InputTime extends InputRange {

    public InputTime(String id) {
        super(id, InputType.Time);
    }

}
