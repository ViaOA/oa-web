package com.viaoa.web.html.input;
import java.util.*;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.OAHtmlComponent.InputType;
import com.viaoa.web.html.form.OAFormSubmitEvent;


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

    
    public InputButton(String selector) {
        super(selector, InputType.Button);
    }

    public InputButton(String selector, InputType type) {
        super(selector, type);
    }
    
    public String getButtonText() {
        return htmlComponent.getValue();
    }
    public void setButtonText(String value) {
        htmlComponent.setValue(value);
    }
    

    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("value");
        hsSupported.add("buttontext");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (OAStr.isNotEqual(type, Event_Click)) return;
        onClientClickEvent();
    }
    
    protected void onClientClickEvent() {
    }

}
