package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.OACompare;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.jquery.JqAutoNumeric;
import com.viaoa.web.html.jquery.JqMaskedInput;

/**
 * Binds JqAutoNumeric to an Hub + propertyName
 *
 */
public class OAJqAutoNumeric extends JqAutoNumeric implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIPropertyController oaUiControl;
    private static class LastRefresh {
        OAObject objUsed;
        Object value;
    }
    private final LastRefresh lastRefresh = new LastRefresh();
    
    public OAJqAutoNumeric(String id, Hub hub, String propName) {
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

        Object objPrev = oaUiControl.getValue(lastRefresh.objUsed);
        if (!OACompare.isEqual(objPrev, lastRefresh.value)) {
            formSubmitEvent.addSyncError("OAJqAutoNumeric Id="+getId());
            return;
        }
        
        final String val = getValue();
        oaUiControl.onSetProperty(lastRefresh.objUsed, val);
        lastRefresh.value = val;
    }
    
    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        lastRefresh.objUsed = (OAObject) oaUiControl.getHub().getAO(); 
        lastRefresh.value = oaUiControl.getValue(lastRefresh.objUsed);
        
        boolean b = oaUiControl.isEnabled(lastRefresh.objUsed);
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible(lastRefresh.objUsed);
        setVisible(b);
        
        b = oaUiControl.isRequired();
        setRequired(b);
        
        String val = oaUiControl.getValueAsString(lastRefresh.objUsed);
        setValue(val);
                
        OAObjectInfo oi = getHub().getOAObjectInfo();
        OAPropertyInfo pi = oi.getPropertyInfo(getPropertyName());
        if (pi != null) {
            if (getMaxLength() == 0 && pi.getMaxLength() > 0) setMaxLength(pi.getMaxLength());
            if (getMinLength() == 0) setMinLength(pi.getMinLength());
            
            if (getSize() < 1) {
                setSize(pi.getDisplayLength());
            }
            
            /*qqqqq  Min & Max values ??
            if (pi.getmaxgetMax() == 0 && pi.getMaxLength() > 0) setMaxLength(pi.getMaxLength());
            if (getMin() == 0) setMinLength(pi.getMinLength());
            */
        }
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
        String s = "<input type='text' id='"+getId()+"'";
        s += " class='oaFitColumnSize'";
        if (obj == null) s += " style='visibility: hidden;'"; 
        s += ">";
        // note: other settings will be added InputText
        return s;
    }
}
