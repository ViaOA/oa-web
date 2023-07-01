package com.viaoa.web.html.input;

import java.util.HashSet;
import java.util.Set;

import com.viaoa.web.html.OAHtmlComponent.InputType;

/*
 <input type="radio" id="radBeef" name="meal" value="beef" />
 <input type="radio" id="radChicken" name="meal" value="chicken" checked />
 <input type="radio" id="radTofu" name="meal" value="tofu" />

 NOTE:  all grouped checkboxes should have different Id, but the same Name with different value.
*/ 
 
public class InputRadio extends InputElement {

    /**
     * A group of radio buttons need to use the same name, but each should have it's own unique Id;
     * @param name used by the other group of radio buttons.
     * @param value that is submitted if this radio is selected.
     */
    public InputRadio(String id, String name, String value) {
        super(id, InputType.Radio);
        setName(name);
        setValue(value);
    }

    public InputRadio(String id, String name) {
        this(id, name, id);
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


    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("value");
        hsSupported.add("checked");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

}


