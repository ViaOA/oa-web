package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.OACompare;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputCheckBox;
import com.viaoa.web.html.input.InputText;
import com.viaoa.web.util.OAWebUtil;


//qqqqqqqq option to dynamically create check boxes for a multilist 

/**
 * Binds Input CheckBox to an Hub + propertyName
 *
 */
public class OAInputCheckBox extends InputCheckBox implements OAHtmlComponentInterface {

    private final OAUIPropertyController oaUiControl;
    private Object onValue, offValue;
    
    public OAInputCheckBox(String id, Hub hub, String propName) {
        this(id, hub, propName, true, false);
    }
    
    public OAInputCheckBox(String id, Hub hub, String propName, Object onValue, Object offValue) {
        super(id);
        this.onValue = onValue;
        this.offValue = offValue;
        
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
        OAObject obj = (OAObject) getHub().getAO();
        if (obj == null) {
            return;
        }

        boolean b = isChecked();
        
        oaUiControl.onSetProperty( b ? onValue : offValue);
    }
    
    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible();
        setVisible(b);

        b = (getHub().getAO() == null) ? false : OACompare.isEqual(onValue, oaUiControl.getValue());
        setChecked(b);
    }
}
