package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.OACompare;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.jquery.JqAutoNumeric;
import com.viaoa.web.html.jquery.JqMaskedInput;

/**
 * Binds JqAutoNumeric to an Hub + propertyName
 *
 */
public class OAJqAutoNumeric extends JqAutoNumeric implements OAHtmlComponentInterface {
    private final OAUIPropertyController oaUiControl;
    //qqqqq 0: verify class        
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
        
        
        //qqqqq 2: compare that it was not changed by another        
        if (lastRefresh.objUsed == null) return;

        // make sure that it did not change
        Object objPrev = oaUiControl.getValue(lastRefresh.objUsed);
        if (!OACompare.isEqual(objPrev, lastRefresh.value)) {
            //qqqqqqqqqqqqqqqqq sync error
        }
        
        final String val = getValue();
        oaUiControl.onSetProperty(lastRefresh.objUsed, val);
        lastRefresh.value = val;
    }
    
    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        //qqqqq 1: populate lastRefresh        
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
            
            /*qqqqqqqqqq  Min & Max values ??
            if (pi.getmaxgetMax() == 0 && pi.getMaxLength() > 0) setMaxLength(pi.getMaxLength());
            if (getMin() == 0) setMinLength(pi.getMinLength());
            */
            
        }
    }
}
