package com.viaoa.web.html.bootstrap;

import com.viaoa.util.OADate;
import com.viaoa.util.OAStr;
import com.viaoa.util.OATime;

/**
 * Create a bootstrap date text component.
 */
public class BsTime extends BsDateTime {

    public BsTime(String selector) {
        super(selector);
        this.typeDateTime = Type.Time;
    }
    
    public OATime getTimeValue() {
        String val = getValue();
        if (OAStr.isEmpty(val)) return null;
        return new OATime(val, OATime.JsonFormat);
    }

}
