package com.viaoa.web.html.input;

import java.util.*;

import com.viaoa.object.OAObject;
import com.viaoa.util.OADate;
import com.viaoa.util.OADateTime;
import com.viaoa.util.OAStr;
import com.viaoa.util.OATime;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/*

<input id="date" type="date" name="date" value="2023-06-30">

*/

public class InputDate extends InputElement {
    public InputDate(String selector) {
        super(selector, InputType.Date);
    }

    public void setValue(OADate date) {
        if (date == null) super.setValue(null);
        else super.setValue(date.toString(OADate.JsonFormat));
    }
    
    public OADate getDateValue() {
        String val = getValue();
        if (OAStr.isEmpty(val)) return null;
        return new OADate(val, OADate.JsonFormat);
    }
    
    public void setMin(OADate date) {
        if (date == null) htmlComponent.setMin(null);
        else htmlComponent.setMin(date.toString(OADate.JsonFormat));
    }

    public void setMax(OADate date) {
        if (date == null) htmlComponent.setMax(null);
        else htmlComponent.setMax(date.toString(OADate.JsonFormat));
    }
    
    public OADate getMinDate() {
        String val = htmlComponent.getMin();
        if (val == null) return null;
        return new OADate(val, OADate.JsonFormat);
    }
    public OADate getMaxDate() {
        String val = htmlComponent.getMax();
        if (val == null) return null;
        return new OADate(val, OADate.JsonFormat);
    }

    public String getPattern() {
        return htmlComponent.getPattern();
    }

    public void setPattern(String pattern) {
        htmlComponent.setPattern(pattern);
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
    
    /**
     * The display width of the text field, number of characters wide.
     */
    public int getSize() {
        return htmlComponent.getSize();
    }

    public void setSize(int val) {
        htmlComponent.setSize(val);
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

    public String getAutoComplete() {
        return htmlComponent.getAutoComplete();
    }
    public void setAutoComplete(String val) {
        htmlComponent.setAutoComplete(val);
    }
    
    public String getFloatLabel() {
        return htmlComponent.getFloatLabel();
    }

    public void setFloatLabel(String floatLabel) {
        htmlComponent.setFloatLabel(floatLabel);
    }

    public String getPlaceHolder() {
        return htmlComponent.getPlaceHolder();
    }
    public void setPlaceHolder(String placeHolder) {
        htmlComponent.setPlaceHolder(placeHolder);
    }

    public String getMin() {
        return htmlComponent.getMin();
    }
    public void setMin(String min) {
        htmlComponent.setMin(min);
    }
    
    public String getMax() {
        return htmlComponent.getMax();
    }
    public void setMax(String max) {
        htmlComponent.setMax(max);
    }
    
    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("pattern");
        hsSupported.add("readonly");
        hsSupported.add("required");
        hsSupported.add("size");
        hsSupported.add("minlength");
        hsSupported.add("maxlength");
        hsSupported.add("autocomplete");
        hsSupported.add("floatlabel");
        hsSupported.add("placeholder");
        hsSupported.add("min");
        hsSupported.add("max");
    }

    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
}
