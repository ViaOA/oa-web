package com.viaoa.web.html.input;

import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputType;
import com.viaoa.web.html.form.OAFormSubmitEvent;

// note: if clicked, then OAFormSubmitEvent.submitOAComponent will be set to this

public class InputSubmit extends HtmlFormElement {

    
    public InputSubmit(String id) {
        super(id, InputType.Submit);   
    }
    
    // the button text and value that is submitted if clicked.
    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }
    
    
//qqqqqqqqqqqq value is the button's Text/label ....... see OAHtmlFormSubmitEvent     

/*    
    @Override
    public void onSubmitRunCommand(OAHtmlFormSubmitEvent formSubmitEvent) {
        // TODO Auto-generated method stub
        super.onSubmitRunCommand(formSubmitEvent);
    }
*/    
}
