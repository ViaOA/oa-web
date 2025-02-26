package com.viaoa.web.html.input;

import java.util.HashSet;
import java.util.Set;

import com.viaoa.web.html.OAHtmlComponent.InputType;
import com.viaoa.web.html.form.OAFormSubmitEvent;

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
public class InputSubmit extends InputElement {

    public InputSubmit(String selector) {
        super(selector, InputType.Submit);   
    }
    
    protected InputSubmit(String id, InputType type) {
        super(id, type);
    }
    
    public String getButtonText() {
        return htmlComponent.getValue();
    }
    public void setButtonText(String value) {
        htmlComponent.setValue(value);
    }
    
    // the button text and value that is submitted if clicked.
    public String getValue() {
        return htmlComponent.getValue();
    }
    public void setValue(String value) {
        htmlComponent.setValue(value);
    }
    
/*qqqq    
    @Override
    protected void onSubmit(OAFormSubmitEvent formSubmitEvent) {
        super.onSubmit(formSubmitEvent);
    }
*/    
    
    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("value");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
}
