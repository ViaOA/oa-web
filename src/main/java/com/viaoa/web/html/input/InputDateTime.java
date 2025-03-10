package com.viaoa.web.html.input;

import java.util.*;

import com.viaoa.util.OADate;
import com.viaoa.util.OADateTime;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/* Notes:

<input id="dt" type="datetime-local" name="dt" value="2023-06-29T07:45">

step="900" min="2023-01-01T00:00" max="2023-12-31T23:59"

*/

public class InputDateTime extends InputElement {

    public InputDateTime(String selector) {
        super(selector, InputType.DateTimeLocal);
    }
    
    public void setValue(OADateTime dateTime) {
        if (dateTime == null) super.setValue(null);
        else super.setValue(dateTime.toString(OADateTime.JsonFormat));
    }

    public OADateTime getDateTimeValue() {
        String val = getValue();
        if (OAStr.isEmpty(val)) return null;
        return new OADateTime(val, OADateTime.JsonFormat);
    }
    
    public void setMin(OADateTime dt) {
        if (dt == null) htmlComponent.setMin(null);
        else htmlComponent.setMin(dt.toString(OADateTime.JsonFormat));
    }

    public void setMax(OADateTime dt) {
        if (dt == null) htmlComponent.setMax(null);
        else htmlComponent.setMax(dt.toString(OADateTime.JsonFormat));
    }

    public OADateTime getMinDateTime() {
        String val = htmlComponent.getMin();
        if (val == null) return null;
        return new OADateTime(val, OADateTime.JsonFormat);
    }
    public OADateTime getMaxDateTime() {
        String val = htmlComponent.getMax();
        if (val == null) return null;
        return new OADateTime(val, OADateTime.JsonFormat);
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
