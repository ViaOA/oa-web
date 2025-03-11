package com.viaoa.web.html.oa;

import java.util.Set;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.*;
import com.viaoa.util.*;
import com.viaoa.web.html.*;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.input.InputRadio;

/**
 * Binds Input Radio to an Hub + propertyName
 *
 */
public class OAInputRadio extends InputRadio implements OATableColumnInterface {
    private final OAUIController controlUI;
    private Object onValue;
    
    
    public OAInputRadio(String selector, Hub hub, String propName, Object selectValue) {
        super(selector);
        this.onValue = selectValue;
        
        controlUI = new OAUIController(hub, propName) {
            @Override
            public void updateComponent(Object object) {
                Object obj = this.getValue(object);
                boolean b = OACompare.isEqual(obj, selectValue);
                OAInputRadio.this.setChecked(b);
                OAInputRadio.this.setEnabled(this.isEnabled());
                OAInputRadio.this.setVisible(this.isVisible());
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
    public String getValueAsString(Hub hubFrom, Object obj) {
        if (obj instanceof OAObject) {
            boolean b = ((OAObject)obj).isVisible(getPropertyName());
            if (!b) return "";
        }
        Object val = controlUI.getValue(obj);
        
        String s;
        if (OACompare.isEqual(onValue, val)) s = "true";
        else s = "";
        return s;
    }
    
    
    @Override
    protected void onClientChangeEvent() {
        this.controlUI.setValue(onValue); // set property
    }
}
