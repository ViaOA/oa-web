package com.viaoa.web.html.bootstrap;

import java.util.Set;
import java.util.TimeZone;

import com.viaoa.util.OADate;
import com.viaoa.util.OADateTime;
import com.viaoa.util.OAStr;
import com.viaoa.util.OAString;
import com.viaoa.util.OATime;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputText;
import com.viaoa.web.server.OASession;
import com.viaoa.web.util.OAJspUtil;

/*

<label id="lbl" style="position: relative">Test BsDateTimeText <input id="txtdt" type="Text"></label>

*/


/**
 * Create a bootstrap datetime text component.
 * <p>
 * Note:<br>
 * need to make sure that there is a parent element that has:  style="position: relative" 
 * 
 */
public class BsDateTime extends InputText {
    
    protected static enum Type {
        DateTime,
        Date,
        Time
    }
    protected Type typeDateTime;
    protected String format;

    public BsDateTime(String id) {
        super(id);
        this.typeDateTime = Type.DateTime;
    }


    public String getFormat() {
        return format;
    }

    public void setFormat(String fmt) {
        this.format = fmt;
    }

    

    @Override
    protected String getInitializeScript() {
        final StringBuilder sb = new StringBuilder();

        String fmt = getFormat();
        if (OAStr.isEmpty(fmt)) {
            if (typeDateTime == Type.Date) fmt = OADate.getGlobalOutputFormat();
            else if (typeDateTime == Type.Time) fmt = OATime.getGlobalOutputFormat();
            else fmt = OADateTime.getGlobalOutputFormat();
        }

        // Jquery date/time formats
        // see: http://api.jqueryui.com/datepicker/#utility-formatDate
        // http://docs.jquery.com/UI/Datepicker/formatDate
        // http://trentrichardson.com/examples/timepicker/
        // https://github.com/trentrichardson/jQuery-Timepicker-Addon

        String dfmtJquery = null;
        String tfmtJquery = null;

        int pos = fmt.indexOf('M');
        if (pos < 0) {
            pos = fmt.indexOf('y');
        }

        if (pos >= 0) {
            pos = fmt.indexOf('H');
            if (pos < 0) {
                pos = fmt.indexOf('h');
            }
            if (pos >= 0) {
                dfmtJquery = fmt.substring(0, pos).trim();
            }
            else {
                dfmtJquery = fmt;
            }
            if (dfmtJquery.indexOf("MMM") >= 0) {
                dfmtJquery = OAString.convert(dfmtJquery, "MMMM", "MM");
                dfmtJquery = OAString.convert(dfmtJquery, "MMM", "M");
            }
            else {
                dfmtJquery = OAString.convert(dfmtJquery, "M", "m");
            }
            dfmtJquery = OAString.convert(dfmtJquery, "yy", "y");
            dfmtJquery = OAString.convert(dfmtJquery, "E", "D");
        }

        pos = fmt.indexOf('H');
        if (pos < 0) {
            pos = fmt.indexOf('h');
        }
        if (pos >= 0) {
            tfmtJquery = fmt.substring(pos).trim();
            tfmtJquery = OAString.convert(tfmtJquery, "aa", "TT");
            tfmtJquery = OAString.convert(tfmtJquery, "a", "TT");
        }

        if (typeDateTime != Type.DateTime) {
            if (typeDateTime == Type.Date) tfmtJquery = null;
            else dfmtJquery = null;
        }

        // Bootstrap date/time formats
        // see: http://momentjs.com/docs/#/displaying/format/
        String dfmtBS = null;
        String tfmtBS = null;

        pos = fmt.indexOf('M');
        if (pos < 0) {
            pos = fmt.indexOf('y');
        }

        if (pos >= 0) {
            pos = fmt.indexOf('H');
            if (pos < 0) {
                pos = fmt.indexOf('h');
            }
            if (pos >= 0) {
                dfmtBS = fmt.substring(0, pos).trim();
            }
            else {
                dfmtBS = fmt;
            }
            dfmtBS = OAString.convert(dfmtBS, "y", "Y");
            dfmtBS = OAString.convert(dfmtBS, "d", "D");
            dfmtBS = OAString.convert(dfmtBS, "E", "d"); // day of week
        }

        pos = fmt.indexOf('H');
        if (pos < 0) {
            pos = fmt.indexOf('h');
        }
        if (pos >= 0) {
            tfmtBS = fmt.substring(pos).trim();
        }

        if (typeDateTime != Type.DateTime) {
            if (typeDateTime == Type.Date) tfmtBS = null;
            else dfmtBS = null;
        }

        if (OAStr.isNotEmpty(dfmtJquery) && OAStr.isNotEmpty(tfmtJquery)) {
            // supports jquery.datetimepicker and bootstrap datetimepicker (customized version: had to change name to bsdatetimepicker)
            // see:  https://eonasdan.github.io/bootstrap-datetimepicker/
            sb.append("if ($().bsdatetimepicker) {\n");
            sb.append("  $('#" + getId() + "').bsdatetimepicker({");
            sb.append("format: '" + OAJspUtil.createJsString((dfmtBS + " " + tfmtBS), '\'') + "'");

            sb.append(", sideBySide: true, showTodayButton: true, showClear: true, showClose: true});\n");
            sb.append("}\n");
        }
        else if (OAStr.isNotEmpty(dfmtJquery)) {
            sb.append("if ($().bsdatetimepicker) {\n");
            sb.append("$('#" + getId() + "').bsdatetimepicker({");
            sb.append("format: '" + OAJspUtil.createJsString(dfmtBS, '\'') + "'");
            sb.append(", showTodayButton: true, showClear: true, showClose: true});\n");
            sb.append("}\n");
        }
        else if (OAStr.isNotEmpty(tfmtJquery)) {
            sb.append("if ($().bsdatetimepicker) {\n");
            sb.append("  $('#" + getId() + "').bsdatetimepicker({ ");
            sb.append("format: '" + OAJspUtil.createJsString(tfmtBS, '\'') + "'");
            sb.append(", showClear: true, showClose: true});\n");
            sb.append("}\n");
        }

        return sb.toString();
    }

    public OADateTime getDateTimeValue() {
        String val = getValue();
        if (OAStr.isEmpty(val)) return null;
        return new OADateTime(val, OADateTime.JsonFormat);
    }
    public OADate getDateValue() {
        String val = getValue();
        if (OAStr.isEmpty(val)) return null;
        return new OADate(val, OADate.JsonFormat);
    }
    public OATime getTimeValue() {
        String val = getValue();
        if (OAStr.isEmpty(val)) return null;
        return new OATime(val, OATime.JsonFormat);
    }
    
    
//qqqqqqqq might want to do this during the method onSubmitLoadValues    
    
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {

        String value = getValue();
        if (OAStr.isEmpty(value)) return;

        
        String fmt = getFormat();
        boolean b = true;
        
        if (typeDateTime == Type.DateTime || typeDateTime == Type.Time) {
        if (OAStr.isNotEmpty(value)) {
            if (!OAString.isEmpty(fmt)) {
                String s = fmt.toUpperCase();
                if (s.indexOf("X") >= 0 || s.indexOf("Z") >= 0) { // includes timezone in value
                    b = false;
                }
            }
        }
        }
        if (!b) return;
    
        OAForm f = getOAHtmlComponent().getForm();
        if (f == null) return;
            
        OASession sess = f.getSession();
        if (sess == null) return;

        
        if (typeDateTime == Type.DateTime) {
            OADateTime dt = OADateTime.valueOf(value, fmt);
            TimeZone tz = sess.getBrowserTimeZone();
            if (tz != null) {
                dt.setTimeZone(tz);
            }
            OADateTime d2 = new OADateTime(dt.getTime()); // use this computer's timezone
            setValue(d2.toString(fmt));
        } else if (typeDateTime == Type.Time) {
            OATime dt = (OATime) OATime.valueOf(value, fmt);
            TimeZone tz = sess.getBrowserTimeZone();
            if (tz != null) {
                dt.setTimeZone(tz);
            }
            OATime d2 = new OATime(dt.getTime()); // use this computer/server timezone
            setValue(d2.toString(fmt));
        }
    }
    
    @Override
    public void getRequiredCssNames(Set<String> hsCssName) {
        hsCssName.add(OAFormInsertDelegate.CSS_bootstrap);
        hsCssName.add(OAFormInsertDelegate.CSS_bootstrap_datetimepicker);
    }

    @Override
    public void getRequiredJsNames(Set<String> hsJsName) {
        hsJsName.add(OAFormInsertDelegate.JS_jquery);
        hsJsName.add(OAFormInsertDelegate.JS_jquery_ui);
        hsJsName.add(OAFormInsertDelegate.JS_bootstrap);
        hsJsName.add(OAFormInsertDelegate.JS_moment);
        hsJsName.add(OAFormInsertDelegate.JS_bootstrap_datetimepicker);
    }
}
