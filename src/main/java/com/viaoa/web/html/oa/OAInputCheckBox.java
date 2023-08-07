package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.OACompare;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputCheckBox;

//qqqqqqqq option to dynamically create check boxes for a multilist 

/**
 * Binds Input CheckBox to an Hub + propertyName
 *
 */
public class OAInputCheckBox extends InputCheckBox implements OAHtmlComponentInterface {
    private final OAUIPropertyController oaUiControl;
    private Object onValue, offValue;
    
  //qqqqq 0: verify class        
    private static class LastRefresh {
        OAObject objUsed;
        Object value;
    }
    private final LastRefresh lastRefresh = new LastRefresh();
    
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
        
      //qqqqq 2: compare that it was not changed by another        
        if (lastRefresh.objUsed == null) return;
        
        // make sure that it did not change
        Object objPrev = oaUiControl.getValue(lastRefresh.objUsed);
        if (!OACompare.isEqual(objPrev, lastRefresh.value)) {
            //qqqqqqqqqqqqqqqqq add sync error msg
        }

        boolean b = isChecked();
        oaUiControl.onSetProperty(lastRefresh.objUsed, b ? onValue : offValue);
    }
    
    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
      //qqqqq 1: populate lastRefresh        
        lastRefresh.objUsed = (OAObject) oaUiControl.getHub().getAO(); 
        lastRefresh.value = oaUiControl.getValue(lastRefresh.objUsed);
        
        
        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible();
        setVisible(b);

        b = (lastRefresh.objUsed == null) ? false : OACompare.isEqual(onValue, lastRefresh.value);
        setChecked(b);
    }
    
//qqqqqqqqqq table interface    
    
}
