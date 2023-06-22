package com.viaoa.web.html.input;

import java.util.List;

import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputText extends HtmlFormElement {
    
    public InputText(String id) {
        super(id, InputType.Text);
    }

    // used by subclasses to set the input type, ex: "tel"
    protected InputText(String id, InputType type) {
        super(id, type);
    }
    
    
    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }

    public String getPlaceHolder() {
        return oaHtmlComponent.getPlaceHolder();
    }
    public void setPlaceHolder(String placeHolder) {
        oaHtmlComponent.setPlaceHolder(placeHolder);
    }

    public String getPattern() {
        return oaHtmlComponent.getPattern();
    }

    public void setPattern(String pattern) {
        oaHtmlComponent.setPattern(pattern);
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
    

    // Id of datalist element
    public String getList() {
        return oaHtmlComponent.getList();
    }

    public void setList(String listId) {
        oaHtmlComponent.setList(listId);
    }
    
    public List<String> getDataList() {
        return oaHtmlComponent.getDataList();
    }

    public void setDataList(List<String> lst) {
        oaHtmlComponent.setDataList(lst);
    }
    

    
    /**
     * The display width of the text field, number of characters wide.
     */
    public int getSize() {
        return oaHtmlComponent.getSize();
    }

    public void setSize(int val) {
        oaHtmlComponent.setSize(val);
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

    public String getAutoComplete() {
        return oaHtmlComponent.getAutoComplete();
    }
    public void setAutoComplete(String val) {
        oaHtmlComponent.setAutoComplete(val);
    }
    
    
    

    public String getInputMode() {
        return oaHtmlComponent.getInputMode();
    }

    public void setInputMode(String mode) {
        oaHtmlComponent.setInputMode(mode);
    }

    public void setInputMode(InputModeType type) {
        oaHtmlComponent.setInputMode(type);
    }


    public boolean getSpellCheck() {
        return oaHtmlComponent.getSpellCheck();
    }
    public boolean isSpellCheck() {
        return getSpellCheck();
    }
    public void setSpellCheck(boolean b) {
        oaHtmlComponent.setSpellCheck(b);
    }
}
