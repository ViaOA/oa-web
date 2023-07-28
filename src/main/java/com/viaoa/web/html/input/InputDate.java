package com.viaoa.web.html.input;

import com.viaoa.object.OAObject;
import com.viaoa.util.OADate;
import com.viaoa.util.OADateTime;
import com.viaoa.util.OAStr;
import com.viaoa.util.OATime;
import com.viaoa.web.html.OAHtmlComponent.InputType;
import com.viaoa.web.html.oa.OAHtmlComponentInterface;
import com.viaoa.web.html.oa.OAHtmlTableComponentInterface;

/*

<input id="date" type="date" name="date" value="2023-06-30">

*/

public class InputDate extends InputRange {

    public InputDate(String id) {
        super(id, InputType.Date);
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
        if (date == null) super.setMin(null);
        else super.setMin(date.toString(OADate.JsonFormat));
    }

    public void setMax(OADate date) {
        if (date == null) super.setMax(null);
        else super.setMax(date.toString(OADate.JsonFormat));
    }
    
    public OADate getMinDate() {
        String val = getMin();
        if (val == null) return null;
        return new OADate(val, OADate.JsonFormat);
    }
    public OADate getMaxDate() {
        String val = getMax();
        if (val == null) return null;
        return new OADate(val, OADate.JsonFormat);
    }

}
