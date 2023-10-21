package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.OACompare;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputRadio;

/**
 * Binds Input Radio to an Hub + propertyName
 *
 */
public class OAInputRadio extends InputRadio implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIPropertyController oaUiControl;
    private Object selectValue;
    
    private static class LastRefresh {
        OAObject objUsed;
        Object value;
    }
    private final LastRefresh lastRefresh = new LastRefresh();
    
    
    public OAInputRadio(String id, String name, String value, Hub hub, String propName, Object selectValue) {
        super(id, name, value);
        this.selectValue = selectValue;
        
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
        
        // make sure that it did not change
        Object objPrev = oaUiControl.getValue(lastRefresh.objUsed);
        if (!OACompare.isEqual(objPrev, lastRefresh.value)) {
            formSubmitEvent.addSyncError("OAInputRadio Id="+getId());
            return;
        }

        boolean b = isChecked();
        if (b) oaUiControl.onSetProperty(lastRefresh.objUsed, selectValue);
    }
    
    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        lastRefresh.objUsed = (OAObject) oaUiControl.getHub().getAO(); 
        lastRefresh.value = oaUiControl.getValue(lastRefresh.objUsed);
        
        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible();
        setVisible(b);

        b = (lastRefresh.objUsed == null) ? false : OACompare.isEqual(selectValue, lastRefresh.value);
        setChecked(b);
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
                if (s == null) s = "";
                else td.addClass("oaNoTextOverflow");
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
        String s = "<input type='radio' id='"+getId()+"'";
        s += " class='oaFitColumnSize'";
        if (obj == null) s += " style='visibility: hidden;'"; 
        s += ">";
        return s;
    }

    
}
