package com.viaoa.web.html.input;

import com.viaoa.web.html.OAHtmlComponent.InputType;

/* 
  <input id="cmd" type="submit" name="cmd" value="Submit Button">
*/

/**
  Submit button.
  <p>
  notes:<br> 
  value is also the buttons label/text<br>
  see: use the HtmlButton with type=submit to create a submit button where value is not the buttons text.<br> 
  <p>
  if clicked:
  submit will send name="cmd", value="Button text"
*/
public class InputSubmit extends InputButton {

    public InputSubmit(String selector) {
        super(selector, InputType.Submit);   
    }
}
