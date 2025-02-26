package com.viaoa.web.html.input;

import com.viaoa.util.OADate;
import com.viaoa.util.OADateTime;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/* Notes:

<input id="dt" type="datetime-local" name="dt" value="2023-06-29T07:45">

step="900" min="2023-01-01T00:00" max="2023-12-31T23:59"

*/

public class InputDateTime extends InputRange {

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
        if (dt == null) super.setMin(null);
        else super.setMin(dt.toString(OADateTime.JsonFormat));
    }

    public void setMax(OADateTime dt) {
        if (dt == null) super.setMax(null);
        else super.setMax(dt.toString(OADateTime.JsonFormat));
    }

    public OADateTime getMinDateTime() {
        String val = getMin();
        if (val == null) return null;
        return new OADateTime(val, OADateTime.JsonFormat);
    }
    public OADateTime getMaxDateTime() {
        String val = getMax();
        if (val == null) return null;
        return new OADateTime(val, OADateTime.JsonFormat);
    }

}
