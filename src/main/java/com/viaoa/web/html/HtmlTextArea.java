package com.viaoa.web.html;

import java.util.HashSet;
import java.util.Set;

import com.viaoa.web.html.OAHtmlComponent.FormElementType;
import com.viaoa.web.html.input.InputElement;

/*


textarea {
  resize: none;
}

*/

public class HtmlTextArea extends HtmlFormElement {
    
    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        hsSupported.add("value");
        hsSupported.add("spellcheck");
        hsSupported.add("cols");
        hsSupported.add("rows");
        hsSupported.add("autocomplete");
        hsSupported.add("placeholder");
        hsSupported.add("readonly");
        hsSupported.add("required");
        hsSupported.add("minlength");
        hsSupported.add("maxlength");
        hsSupported.add("wrap");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
    
    
    
    public HtmlTextArea(String id) {
        super(id, FormElementType.Text);
    }
    
    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }

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

    public String getWrap() {
        return oaHtmlComponent.getWrap();
    }
    public void setWrap(String wrap) {
        oaHtmlComponent.setWrap(wrap);
    }
}
