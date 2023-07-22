package com.viaoa.web.html.input;


import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/**
 * Input type = Number.
 * 
 * Notes:<br>
 * size, minLength, maxLength are not support by this type.<br>  
 * using min, max will set the display width to match number of allowed digits.
 *
 */
public class InputNumber extends InputRange {

    public InputNumber(String id) {
        super(id, InputType.Number);
        htmlComponent.setInputMode(InputModeType.Numeric);
    }
}
