package com.viaoa.web.html;

public class HtmlOption {

//qqqqqqqqqqq use OAComponent
// https://developer.mozilla.org/en-US/docs/Web/HTML/Element/option
// optgroup    

    protected boolean bEnabled; // attribute, uses  'disabled'
    protected boolean bSelected; // attribute
    protected String value;  // attribute sent to server on submit
    protected String label;  // display

    
    public HtmlOption(String value) {
        this.value = value;
    }
    public HtmlOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public HtmlOption(String value, boolean bSelected) {
        this.value = value;
        this.bSelected = bSelected;
    }
    
    public HtmlOption(String value, String label, boolean bSelected) {
        this.value = value;
        this.label = label;
        this.bSelected = bSelected;
    }
    
    public boolean getEnabled() {
        return bEnabled;
    }
    public boolean isEnabled() {
        return bEnabled;
    }
    public void setEnabled(boolean b) {
        this.bEnabled = b;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    

    public boolean getSelected() {
        return bSelected;
    }
    public boolean isSelected() {
        return bSelected;
    }
    public void setSelected(boolean b) {
        this.bSelected = b;
    }
    
    

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    
    
}
