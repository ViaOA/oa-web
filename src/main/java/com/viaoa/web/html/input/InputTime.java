package com.viaoa.web.html.input;

import com.viaoa.util.OADate;
import com.viaoa.util.OADateTime;
import com.viaoa.util.OAStr;
import com.viaoa.util.OATime;
import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputType;


// <input id="txtTime" type="time" name="txtTime">
//  07:09 PM   value = 17:09


/**
 * 
 * 
 *
 */
public class InputTime extends InputRange {

    public InputTime(String selector) {
        super(selector, InputType.Time);
    }
    
    public void setValue(OATime time) {
        if (time == null) super.setValue(null);
        else super.setValue(time.toString(OATime.JsonFormat));
    }

    public OATime getTimeValue() {
        String val = getValue();
        if (OAStr.isEmpty(val)) return null;
        return new OATime(val, OATime.JsonFormat);
    }


    public void setMin(OATime time) {
        if (time == null) super.setMin(null);
        else super.setMin(time.toString(OATime.JsonFormat));
    }

    public void setMax(OATime time) {
        if (time == null) super.setMax(null);
        else super.setMax(time.toString(OATime.JsonFormat));
    }


    public OATime getMinTime() {
        String val = getMin();
        if (val == null) return null;
        return new OATime(val, OATime.JsonFormat);
    }
    public OATime getMaxTime() {
        String val = getMax();
        if (val == null) return null;
        return new OATime(val, OATime.JsonFormat);
    }

}
