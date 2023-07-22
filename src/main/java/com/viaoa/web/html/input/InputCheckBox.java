package com.viaoa.web.html.input;

import java.util.*;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/*
 <input type="checkbox" id="chk1" name="chk1" value="first" checked />
 <input type="checkbox" id="chk2" name="chk2" value="second" />
 
 note:
 only checkboxes with checked are submitted.
 
 */

/**
 * Input element with type checkbox.
 * <p>
 * Notes: this will only submit the value if it is checked.<br>
 * Multiple checkboxes can use the same name.  
 * 
 * @author vince
 */
public class InputCheckBox extends InputElement {

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
        return htmlComponent.getValue();
    }
    public void setValue(String value) {
        htmlComponent.setValue(value);
    }

    public boolean getChecked() {
        return htmlComponent.getChecked();
    }
    public boolean isChecked() {
        return getChecked();
    }
    public void setChecked(boolean b) {
        htmlComponent.setChecked(b);
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
