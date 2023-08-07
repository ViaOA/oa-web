package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.*;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputDate;

/**
 * Binds InputDate to a Hub + propertyName
 *
 */
public class OAInputDate extends InputDate implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIPropertyController oaUiControl;
    
  //qqqqq 0: verify class        
    private static class LastRefresh {
        OAObject objUsed;
        OADate value;
    }
    private final LastRefresh lastRefresh = new LastRefresh();

    public OAInputDate(String id, Hub hub, String propName) {
        super(id);
        oaUiControl = new OAUIPropertyController(hub, propName) {
            @Override
            protected void onCompleted(String completedMessage, String title) {
                OAForm form = getForm();
                if (form != null) {
                    form.addMessage(completedMessage);
                    form.addConsoleMessage(title + " - " + completedMessage);
                }
            }
            @Override
            protected void onError(String errorMessage, String detailMessage) {
                OAForm form = getForm();
                if (form != null) {
                    form.addError(errorMessage);
                    form.addConsoleMessage(errorMessage + " - " + detailMessage);
                }
            }
        };
    }

    
    public Hub getHub() {
        return oaUiControl.getHub();
    }
    public String getPropertyName() {
        return oaUiControl.getPropertyName();
    }
    public String getFormat() {
        return oaUiControl.getFormat();
    }
    public void setFormat(String format) {
        oaUiControl.setFormat(format);
    }

    
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        if (getHub() == null || getPropertyName() == null) {
            return;
        }
        
      //qqqqq 2: compare that it was not changed by another        
        if (lastRefresh.objUsed == null) return;
        
        // make sure that it did not change
        Object objPrev = oaUiControl.getValue(lastRefresh.objUsed);
        if (!OACompare.isEqual(objPrev, lastRefresh.value)) {
            //qqqqqqqqqqqqqqqqq add sync error msg
        }
        
        final OADate date = getDateValue();
        if (OACompare.isNotEqual(lastRefresh.value, date)) {
            oaUiControl.onSetProperty(lastRefresh.objUsed, date);
            lastRefresh.value = date;
        }
    }
    
    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
  //qqqqq 1: populate lastRefresh        
        lastRefresh.objUsed = (OAObject) oaUiControl.getHub().getAO(); 
        lastRefresh.value = (OADate) oaUiControl.getValue(lastRefresh.objUsed);
        
        
        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible();
        setVisible(b);
        
        b = oaUiControl.isRequired();
        setRequired(b);
        
        setValue(lastRefresh.value);
    }

        
    @Override
    public String getTableCellRenderer(HtmlTD td, int row) {
        OAObject obj = (OAObject) getHub().get(row);

        String s;
        if (obj == null) s = "";
        else {
            boolean b = obj.isVisible(getPropertyName());
            if (!b) s = "";
            else {
                s = obj.getPropertyAsString(getPropertyName(), getFormat());
                if (s == null) s = "";
                td.addClass("oaNoTextOverflow");
            }
        }
        return s;
    }
    @Override
    public String getTableCellEditor(HtmlTD td, int row, boolean bHasFocus) {
        String s = "<input id='"+getId()+"'";
        s += " class='oaFitColumnSize'";
        if (row < 0 || getHub().get(row) == null) s += " style='visibility: hidden;'"; 
        s += ">";
        // note: other settings will be added oahtmlcomponent
        return s;
    }
}
