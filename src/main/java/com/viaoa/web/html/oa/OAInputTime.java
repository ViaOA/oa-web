package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.*;
import com.viaoa.util.*;
import com.viaoa.web.html.*;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputTime;

/**
 * Binds InputTime to a Hub + propertyName
 *
 */
public class OAInputTime extends InputTime implements OAEditorInterface, OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIController controlUI;

    public OAInputTime(String elementIdentifier, Hub hub, String propName) {
        super(elementIdentifier);
        
        controlUI = new OAUIController(hub, propName) {
            @Override
            public void updateComponent(Object object) {
                String s = this.getValueAsString(object);
                OAInputTime.this.setValue(s);
                OAInputTime.this.setEnabled(this.isEnabled());
                OAInputTime.this.setVisible(this.isVisible());
            }
            
            @Override
            public void updateLabel(Object object) {
                OAHtmlComponent lbl = getOAHtmlComponent().getLabelComponent();
                if (lbl == null) return;
                lbl.setVisible(isVisible());

                boolean b = this.isEnabled();
                if (!b && getHub().getActiveObject() != null) b = true;
                lbl.setEnabled(b);
            }
        };
        controlUI.setFormat("HH:mm:ss"); // or HH:mm
    }
    
    @Override
    public String getValueAsString(Hub hubFrom, Object obj) {
        String val = controlUI.getValueAsString(obj);
        return val;
    }

    @Override
    public void close() {
        super.close();
        if (controlUI != null) controlUI.close();
    }
    
    
    public Hub getHub() {
        return controlUI.getHub();
    }
    public String getPropertyName() {
        return controlUI.getEndPropertyName();
    }
    
    public String getFormat() {
        return controlUI.getFormat();
    }
    public void setFormat(String format) {
        controlUI.setFormat(format);
    }

    @Override
    protected void onClientChangeEvent(String newValue) {
        super.onClientChangeEvent(newValue);
        controlUI.setValue(getValue());
    }
}
