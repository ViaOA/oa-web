package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.OAObject;
import com.viaoa.uicontroller.*;
import com.viaoa.web.html.*;
import com.viaoa.web.html.input.InputDate;

/**
 * Binds InputDate to a Hub + propertyName
 */
public class OAInputDate extends InputDate implements OATableColumnInterface {
    private final OAUIController controlUI;

    public OAInputDate(String elementIdentifier, Hub hub, String propName) {
        super(elementIdentifier);
        
        controlUI = new OAUIController(hub, propName) {
            @Override
            public void updateComponent(Object object) {
                String s = this.getValueAsString(object);
                OAInputDate.this.setValue(s);
                OAInputDate.this.setEnabled(this.isEnabled());
                OAInputDate.this.setVisible(this.isVisible());
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
        controlUI.setFormat("yyyy-MM-dd");
    }
    
    public OAUIController getController() {
        return controlUI;
    }
    
    @Override
    public String getValueAsString(Hub hubFrom, Object obj) {
        if (obj instanceof OAObject) {
            boolean b = ((OAObject)obj).isVisible(getPropertyName());
            if (!b) return "";
        }
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
