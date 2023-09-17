package com.viaoa.web.html.bootstrap;

import com.viaoa.util.OADate;

/**
 * Create a bootstrap date text component.
 */
public class BsDate extends BsDateTime {

    public BsDate(String id) {
        super(id);
        this.typeDateTime = Type.Date;
    }

    public void setValue(OADate date) {
        if (date == null) super.setValue(null);
        else super.setValue(date.toString(OADate.JsonFormat));
    }
    
}
