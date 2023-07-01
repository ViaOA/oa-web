package com.viaoa.web.html.input;
import java.util.*;
import com.viaoa.web.html.OAHtmlComponent.InputType;


/*

<input id="cmdAjaxSubmit" type=button value="ajaxsubmit"> 

InputButton comp = new InputButton("cmd");
comp.setValue("Button Here!");
form.add(comp);

<label><input id="cmd" type="button" value="Wrong text"></label>

*/

/**
 * Input element with type = button.
 * Note: does not submit the form.
 * 
 * See also: Button element  
 *
 */
public class InputButton extends InputElement {

    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("value");
        hsSupported.add("buttontext");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
    
    
    public InputButton(String id) {
        super(id, InputType.Button);
    }

    public String getButtonText() {
        return oaHtmlComponent.getValue();
    }
    public void setButtonText(String value) {
        oaHtmlComponent.setValue(value);
    }
    
    
    // the button text and value that is submitted if clicked.
    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }
    
/*    

If the image button is used to submit the form, this control doesn't submit its value — 
    instead, the X and Y coordinates of the click on the image are submitted 


<input type="image" alt="Click me!" src="my-img.png" width="80" height="30" />

  acts as a submit button

*/    
    /*    
    
    <input type="submit" value="Submit this form" />
    <input type="reset" value="Reset this form" />
    <input type="button" value="Do Nothing without JavaScript" />

    <button type="submit">Submit this form</button>
    <button type="reset">Reset this form</button>
    <button type="button">Do Nothing without JavaScript</button>    
        
    */      
}
