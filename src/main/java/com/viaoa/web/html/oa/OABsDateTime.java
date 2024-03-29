package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.*;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.bootstrap.BsDateTime;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;


/**
 * Binds Bootstrap DateTime to a Hub + propertyName
 *
 */
public class OABsDateTime extends BsDateTime implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIPropertyController oaUiControl;
    
    private static class LastRefresh {
        OAObject objUsed;
        OADateTime value;
    }
    private final LastRefresh lastRefresh = new LastRefresh();


    public OABsDateTime(String id, Hub hub, String propName) {
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
        if (lastRefresh.objUsed == null) return;

        Object objPrev = oaUiControl.getValue(lastRefresh.objUsed);
        if (!OACompare.isEqual(objPrev, lastRefresh.value)) {
            formSubmitEvent.addSyncError("OABsDateTime Id="+getId());
            return;
        }
        
        final OADateTime dt = getDateTimeValue();
        if (OACompare.isNotEqual(lastRefresh.value, dt)) {
            oaUiControl.onSetProperty(lastRefresh.objUsed, dt);
            lastRefresh.value = dt;
        }
    }
    
    
    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();

        lastRefresh.objUsed = (OAObject) oaUiControl.getHub().getAO(); 
        lastRefresh.value = (OADateTime) oaUiControl.getValue(lastRefresh.objUsed);
        
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
