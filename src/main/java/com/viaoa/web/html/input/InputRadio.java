package com.viaoa.web.html.input;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputType;
import com.viaoa.web.html.form.OAFormSubmitEvent;

// <input type="radio" id="soup" name="meal" value="soup" checked />

// NOTE:  all grouped checkboxes should have different ID, but the same Name
public class InputRadio extends HtmlFormElement {

    /**
     * A group of radio buttons need to use the same name, but each should have it's own unique Id;
     * @param id
     * @param name used by the other group of radio buttons.
     * @param value that is submitted if this radio is selected.
     */
    public InputRadio(String id, String name, String value) {
        super(id, InputType.Radio);
    }

    public String getValue(String value) {
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


//qqqq create subclass RadioToggleButton