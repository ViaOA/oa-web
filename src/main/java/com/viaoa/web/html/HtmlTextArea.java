package com.viaoa.web.html;

import com.viaoa.web.html.OAHtmlComponent.ComponentType;

public class HtmlTextArea extends HtmlFormElement {

    
    public HtmlTextArea(String id) {
        super(id, null, ComponentType.Text);
    }
    
    //qqqqqqqqqq ???
    // Size attribute is the rows to display
 
    
    public boolean getSpellCheck() {
        return oaHtmlComponent.getSpellCheck();
    }
    public boolean isSpellCheck() {
        return oaHtmlComponent.getSpellCheck();
    }
    public void setSpellCheck(boolean b) {
        oaHtmlComponent.setSpellCheck(b);
    }
    
    public int getCols() {
        return oaHtmlComponent.getCols();
    }

    public void setCols(int val) {
        oaHtmlComponent.setCols(val);
    }
    
    public int getRows() {
        return oaHtmlComponent.getRows();
    }

    public void setRows(int val) {
        oaHtmlComponent.setRows(val);
    }

    public String getAutoComplete() {
        return oaHtmlComponent.getAutoComplete();
    }
    public void setAutoComplete(String val) {
        oaHtmlComponent.setAutoComplete(val);
    }

    public String getPlaceHolder() {
        return oaHtmlComponent.getPlaceHolder();
    }
    public void setPlaceHolder(String placeHolder) {
        oaHtmlComponent.setPlaceHolder(placeHolder);
    }
    
    public boolean getReadOnly() {
        return oaHtmlComponent.getReadOnly();
    }

    public boolean isReadOnly() {
        return oaHtmlComponent.getReadOnly();
    }
    public void setReadOnly(boolean b) {
        oaHtmlComponent.setReadOnly(b);
    }
    
    public boolean getRequired() {
        return oaHtmlComponent.getRequired();
    }

    public boolean isRequired() {
        return oaHtmlComponent.getRequired();
    }

    public void setRequired(boolean req) {
        oaHtmlComponent.setRequired(req);
    }

    public int getMinLength() {
        return oaHtmlComponent.getMinLength();
    }

    public void setMinLength(int val) {
        oaHtmlComponent.setMinLength(val);
    }

    public int getMaxLength() {
        return oaHtmlComponent.getMaxLength();
    }

    public void setMaxLength(int val) {
        oaHtmlComponent.setMaxLength(val);
    }

    
    
    
//    , , , , and required
    // minlength, maxlength
    
// wrap = hard, soft (default), off     
    
}
