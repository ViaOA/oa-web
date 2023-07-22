package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputText;

/**
 * Binds InputText to an Hub + propertyName
 *
 */
public class OAInputText extends InputText implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {

    private final OAUIPropertyController oaUiControl;
    
    public OAInputText(String id, Hub hub, String propName) {
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
        OAObject obj = (OAObject) getHub().getAO();
        if (obj == null) {
            return;
        }

        final String val = getValue();
        oaUiControl.onSetProperty(val);
    }
    
    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible();
        setVisible(b);
        
        b = oaUiControl.isRequired();
        setRequired(b);
        
        if (getConversion() == 0) {
            OAObjectInfo oi = getHub().getOAObjectInfo();
            OAPropertyInfo pi = oi.getPropertyInfo(getPropertyName());
            if (pi.isUpper()) oaUiControl.setConversion('U');
            else if (pi.isLower()) oaUiControl.setConversion('L');
        }
        
        
        String val = oaUiControl.getValueAsString();
        setValue(val);
        
        OAObjectInfo oi = getHub().getOAObjectInfo();
        OAPropertyInfo pi = oi.getPropertyInfo(getPropertyName());
        if (pi != null) {
            if (getMaxLength() == 0 && pi.getMaxLength() > 0) setMaxLength(pi.getMaxLength());
            if (getMinLength() == 0) setMinLength(pi.getMinLength());
            
            if (getSize() < 1) {
                setSize(pi.getDisplayLength());
            }
            setRequired(pi.getRequired());
        }
    }

    @Override
    public String getTableCellRenderer(int row) {
        OAObject obj = (OAObject) getHub().get(row);

        String s;
        if (obj == null) s = "";
        else {
            boolean b = obj.isVisible(getPropertyName());
            if (!b) s = "";
            else s = obj.getPropertyAsString(getPropertyName(), getFormat());
        }
        return s;
    }
    @Override
    public String getTableCellEditor(int row) {
        String s = "<input type='text' id='"+getId()+"'>";
        return getTableCellRenderer(row);
    }
}
