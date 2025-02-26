package com.viaoa.web.html.input;

import com.viaoa.web.html.OAHtmlComponent.InputType;

/*

<input id="cmd" type="reset" name="cmd" value="Reset Button">



*/


/**
 * Resets the form components.
 * <p>
 * Note:<br>
 * This does not submit the form.<br>
 *
 */
public class InputReset extends InputSubmit {

    public InputReset(String selector) {
        super(selector, InputType.Reset);
    }
}
