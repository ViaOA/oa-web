package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.*;
import com.viaoa.util.OACompare;
import com.viaoa.util.OAString;
import com.viaoa.web.html.*;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * Binds HtmlTextArea to an Hub + propertyName
 */
public class OAHtmlTextArea extends HtmlTextArea implements OAEditorInterface, OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIController controlUI;

    
    public OAHtmlTextArea(String elementIdentifier, Hub hub, String propName) {
        super(elementIdentifier);
        
        controlUI = new OAUIController(hub, propName) {
            @Override
            public void updateComponent(Object object) {
                String s = this.getValueAsString(object);
                OAHtmlTextArea.this.setValue(s);
                OAHtmlTextArea.this.setEnabled(this.isEnabled());
                OAHtmlTextArea.this.setVisible(this.isVisible());
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

    public void setConversion(char conv) {
        controlUI.setConversion(conv);
    }
    public char getConversion() {
        return controlUI.getConversion();
    }
    
    @Override
    protected void onClientChangeEvent(String newValue) {
        super.onClientChangeEvent(newValue);
        controlUI.setValue(getValue());
    }

}
