package com.viaoa.web.html.bootstrap;

import com.viaoa.util.OADate;
import com.viaoa.util.OAStr;

/**
 * Create a bootstrap date text component.
 */
public class BsDate extends BsDateTime {

    public BsDate(String id) {
        super(id);
        this.typeDateTime = Type.Date;
    }

    public OADate getDateValue() {
        String val = getValue();
        if (OAStr.isEmpty(val)) return null;
        return new OADate(val, OADate.JsonFormat);
    }
}
