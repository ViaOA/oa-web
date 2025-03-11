package com.viaoa.web.html.input;

import java.util.*;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/*
 <input type="radio" id="radBeef" name="meal" value="beef" />
 <input type="radio" id="radChicken" name="meal" value="chicken" checked />
 <input type="radio" id="radTofu" name="meal" value="tofu" />

 NOTE:  all grouped checkboxes should have different Id, but the same Name with different value.
*/ 
 
public class InputRadio extends InputElement {
    private InputRadioGroup inputRadioGroup;
    
    /**
     * A group of radio buttons need to use the same name, but each should have it's own unique Id;
     * @param name used by the other group of radio buttons.
     * @param value that is submitted if this radio is selected.
     */
    public InputRadio(String selector) {
        super(selector, InputType.Radio);
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
        hsSupported.add("checked");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

    public InputRadioGroup getInputRadioGroup() {
        return inputRadioGroup;
    }
    public void setInputRadioGroup(InputRadioGroup irg) {
        this.inputRadioGroup = irg;
        irg.add(this);
    }
    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (OAStr.isNotEqual(type, Event_Change)) return;
        onClientChangeEvent();
    }
    
    protected void onClientChangeEvent() {
        this.setChecked(true);
        this.getOAHtmlComponent().setCheckedChanged(false);
        
        if (inputRadioGroup != null) {
            for (InputRadio ir : inputRadioGroup.getInputRadios()) {
                if (ir != this) {
                    ir.setChecked(false);
                    ir.getOAHtmlComponent().setCheckedChanged(false);
                }
            }
            inputRadioGroup.setCheckedInputRadio(this);
        }
    }
    
}


