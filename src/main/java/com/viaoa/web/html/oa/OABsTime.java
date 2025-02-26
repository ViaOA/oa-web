package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.*;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.bootstrap.BsTime;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;


/**
 * Binds Bootstrap Date to a Hub + propertyName
 *
 */
public class OABsTime extends BsTime implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIPropertyController oaUiControl;

    private static class LastRefresh {
        OAObject objUsed;
        OATime value;
    }
    private final LastRefresh lastRefresh = new LastRefresh();
    
    public OABsTime(String selector, Hub hub, String propName) {
        super(selector);
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
        oaUiControl.setFormat(OATime.JsonFormat);
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
        
        if (lastRefresh.objUsed == null) return;

        Object objPrev = oaUiControl.getValue(lastRefresh.objUsed);
        if (!OACompare.isEqual(objPrev, lastRefresh.value)) {
            formSubmitEvent.addSyncError("OABsTime Id="+getId());
            return;
        }
        
        final OATime time = getTimeValue();
        if (OACompare.isNotEqual(lastRefresh.value, time)) {
            oaUiControl.onSetProperty(lastRefresh.objUsed, time);
            lastRefresh.value = time;
        }
    }
    
    @Override
    public void beforeGetJavaScriptForClient() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        lastRefresh.objUsed = (OAObject) oaUiControl.getHub().getAO(); 
        lastRefresh.value = (OATime) oaUiControl.getValue(lastRefresh.objUsed);
        
        boolean b = bIsFormEnabled && oaUiControl.isEnabled(lastRefresh.objUsed);
        setEnabled(b);

        b = oaUiControl.isVisible(lastRefresh.objUsed);
        setVisible(b);
        
        b = oaUiControl.isRequired();
        setRequired(b);
        
        String val = oaUiControl.getValueAsString(lastRefresh.objUsed);
        setValue(val);
    }

    @Override
    public String getTableCellRenderer(Hub hubTable, HtmlTD td, int row) {
        OAObject obj;
        if (hubTable != null && hubTable != getHub()) {
            obj = (OAObject) hubTable.getAt(row);
            if (obj != null) {
                String pp = OAObjectReflectDelegate.getPropertyPathBetweenHubs(hubTable, getHub());
                obj = (OAObject) obj.getProperty(pp);
            }
        }
        else {
            obj = (OAObject) getHub().get(row);
        }

        String s;
        if (obj == null) s = "";
        else {
            boolean b = obj.isVisible(getPropertyName());
            if (!b) s = "";
            else {
                s = obj.getPropertyAsString(getPropertyName(), getFormat());
                td.addClass("oaNoTextOverflow");
            }
        }
        return s;
    }
    @Override
    public String getTableCellEditor(Hub hubTable, HtmlTD td, int row, boolean bHasFocus) {
        OAObject obj;
        if (hubTable != null && hubTable != getHub()) {
            obj = (OAObject) hubTable.getAt(row);
            if (obj != null) {
                String pp = OAObjectReflectDelegate.getPropertyPathBetweenHubs(hubTable, getHub());
                obj = (OAObject) obj.getProperty(pp);
            }
        }
        else {
            obj = (OAObject) getHub().get(row);
        }
        String s = "<input id='"+getId()+"'";
        s += " class='oaFitColumnSize'";
        if (obj == null) s += " style='visibility: hidden;'"; 
        else td.addStyle("position", "relative");
        s += ">";
        // note: other settings will be added oahtmlcomponent
        return s;
    }

}
