package com.viaoa.web.html.input;

import java.util.*;

public class InputRadioGroup {

    private String name;
    private final List<InputRadio> alInputRadio = new ArrayList();
    private InputRadio radChecked;
    
    public InputRadioGroup(String name) {
        this.name = name;
    }
    
    public void add(InputRadio rad) {
        if (rad == null) return;
        if (!this.alInputRadio.contains(rad)) this.alInputRadio.add(rad);
        if (rad.getInputRadioGroup() != this) rad.setInputRadioGroup(this); 
    }
    
    public List<InputRadio> getInputRadios() {
        return alInputRadio;
    }
    
    public String getName() {
        return name;
    }
    
    public void setCheckedInputRadio(InputRadio ir) {
        if (this.radChecked != null) this.radChecked.setChecked(false);
        this.radChecked = ir;
        if (this.radChecked != null) this.radChecked.setChecked(true);
    }
    public InputRadio getCheckedInputRadio() {
        return this.radChecked;
    }
    
}
