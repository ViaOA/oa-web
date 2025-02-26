package com.viaoa.web.html;

import java.util.HashSet;
import java.util.Set;

import com.viaoa.web.html.OAHtmlComponent.FormElementType;

/*


textarea {
  resize: none; / * dont allow user to resize it * /
}

*/

public class HtmlTextArea extends HtmlFormElement {
    
    public HtmlTextArea(String selector) {
        super(selector, FormElementType.TextArea);
    }
    
    protected HtmlTextArea(final String selector, final FormElementType formElementType) {
        super(selector, formElementType);
    }
    
    
    public String getValue() {
        return htmlComponent.getValue();
    }
    public void setValue(String value) {
        htmlComponent.setValue(value);
    }

    public boolean getSpellCheck() {
        return htmlComponent.getSpellCheck();
    }
    public boolean isSpellCheck() {
        return htmlComponent.getSpellCheck();
    }
    public void setSpellCheck(boolean b) {
        htmlComponent.setSpellCheck(b);
    }
    
    public int getCols() {
        return htmlComponent.getCols();
    }

    public void setCols(int val) {
        htmlComponent.setCols(val);
    }
    
    public int getRows() {
        return htmlComponent.getRows();
    }

    public void setRows(int val) {
        htmlComponent.setRows(val);
    }

    public String getAutoComplete() {
        return htmlComponent.getAutoComplete();
    }
    public void setAutoComplete(String val) {
        htmlComponent.setAutoComplete(val);
    }

    public String getPlaceHolder() {
        return htmlComponent.getPlaceHolder();
    }
    public void setPlaceHolder(String placeHolder) {
        htmlComponent.setPlaceHolder(placeHolder);
    }
    
    public boolean getReadOnly() {
        return htmlComponent.getReadOnly();
    }

    public boolean isReadOnly() {
        return htmlComponent.getReadOnly();
    }
    public void setReadOnly(boolean b) {
        htmlComponent.setReadOnly(b);
    }
    
    public boolean getRequired() {
        return htmlComponent.getRequired();
    }

    public boolean isRequired() {
        return htmlComponent.getRequired();
    }

    public void setRequired(boolean req) {
        htmlComponent.setRequired(req);
    }

    public int getMinLength() {
        return htmlComponent.getMinLength();
    }

    public void setMinLength(int val) {
        htmlComponent.setMinLength(val);
    }

    public int getMaxLength() {
        return htmlComponent.getMaxLength();
    }

    public void setMaxLength(int val) {
        htmlComponent.setMaxLength(val);
    }

    public String getWrap() {
        return htmlComponent.getWrap();
    }
    public void setWrap(String wrap) {
        htmlComponent.setWrap(wrap);
    }
    public void setWrap(OAHtmlComponent.WrapType wrap) {
        htmlComponent.setWrap(wrap);
    }

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

}
