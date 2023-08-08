package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.OACompare;
import com.viaoa.util.OAString;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.HtmlTextArea;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * Binds HtmlTextArea to an Hub + propertyName
 */
public class OAHtmlTextArea extends HtmlTextArea implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIPropertyController oaUiControl;

    private static class LastRefresh {
        OAObject objUsed;
        String value;
    }
    private final LastRefresh lastRefresh = new LastRefresh();

    public OAHtmlTextArea(String id, Hub hub, String propName) {
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

    public OAUIPropertyController getController() {
        return oaUiControl;
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

    public void setConversion(char conv) {
        oaUiControl.setConversion(conv);
    }
    public char getConversion() {
        return oaUiControl.getConversion();
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
            formSubmitEvent.addSyncError("OAHtmlTextArea Id="+getId());
            return;
        }
        
        final String val = getValue();
        if (OAString.isNotEqual(lastRefresh.value, val)) {
            oaUiControl.onSetProperty(lastRefresh.objUsed, val);
            lastRefresh.value = val;
        }
    }
    
    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();

        lastRefresh.objUsed = (OAObject) oaUiControl.getHub().getAO(); 
        lastRefresh.value = oaUiControl.getValueAsString(lastRefresh.objUsed);
        
        boolean b = oaUiControl.isEnabled(lastRefresh.objUsed);
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible(lastRefresh.objUsed);
        setVisible(b);
        
        if (getConversion() == 0) {
            OAObjectInfo oi = getHub().getOAObjectInfo();
            OAPropertyInfo pi = oi.getPropertyInfo(getPropertyName());
            if (pi != null) {
                if (pi.isUpper()) oaUiControl.setConversion('U');
                else if (pi.isLower()) oaUiControl.setConversion('L');
            }
        }
        
        setValue(lastRefresh.value);
        
        OAObjectInfo oi = getHub().getOAObjectInfo();
        OAPropertyInfo pi = oi.getPropertyInfo(getPropertyName());
        
        if (pi != null) {
            if (getMaxLength() == 0 && pi.getMaxLength() > 0) setMaxLength(pi.getMaxLength());
            if (getMinLength() == 0) setMinLength(pi.getMinLength());
        }
        b |= oaUiControl.isRequired();
        setRequired(b);
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
        String s = "<textarea id='"+getId()+"'";
        s += " class='oaFitColumnSize'";
        if (row < 0 || getHub().get(row) == null) s += " style='visibility: hidden;'"; 
        s += "'></textarea>";
        // note: other settings will be added oahtmlcomponent
        return s;
    }

}
