package com.viaoa.web.html.input;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputType;
import com.viaoa.web.html.form.OAFormSubmitEvent;

// <input type="checkbox" id="chk1" name="chk" value="yes" checked />
// <input type="checkbox" id="chk2" name="chk" value="no" />

/**
 * Input element with type checkbox.
 * <p>
 * Notes: this will only submit the value if it is checked.<br>
 * Multiple checkboxes can use the same name.  
 * 
 * @author vince
 */
public class InputCheckBox extends HtmlFormElement {

    
    /**
     * A group of radio buttons need to use the same name, but each should have it's own unique Id;
     * @param id
     * @param name used by the other group of radio buttons.
     * @param value that is submitted if this radio is selected.
     */
    public InputCheckBox(String id, String name, String value) {
        super(id, InputType.CheckBox);
        setValue(value);
    }
    public InputCheckBox(String id, String value) {
        this(id, id, value);
    }
    public InputCheckBox(String id) {
        this(id, id, id);
    }
    
    
    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }

    public boolean getChecked() {
        return oaHtmlComponent.getChecked();
    }
    public boolean isChecked() {
        return getChecked();
    }
    public void setChecked(boolean b) {
        oaHtmlComponent.setChecked(b);
    }
    
}
//qqqq create subclass CheckboxToggleButton
//qqqq  create subclass that uses on/off values
//qqqqq create subclass that allows multiple
