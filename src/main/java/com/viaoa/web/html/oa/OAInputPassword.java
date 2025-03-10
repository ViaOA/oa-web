package com.viaoa.web.html.oa;

import java.util.Set;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.*;
import com.viaoa.util.OACompare;
import com.viaoa.util.OAStr;
import com.viaoa.util.OAString;
import com.viaoa.web.html.*;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputPassword;

/**
 * Binds Input Password to an Hub + propertyName
 *
 */
public class OAInputPassword extends InputPassword implements OATableColumnInterface {
    private final OAUIController controlUI;

    // extra properties
    private int maxSize;
    private boolean bMaxSizeChanged;
    
    public OAInputPassword(String elementIdentifier, Hub hub, String propName) {
        super(elementIdentifier);
        controlUI = new OAUIController(hub, propName) {
            @Override
            public void updateComponent(Object object) {
                String s = this.getValueAsString(object);
                OAInputPassword.this.setValue(""); // never send PW
                OAInputPassword.this.setEnabled(this.isEnabled());
                OAInputPassword.this.setVisible(this.isVisible());
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
        if (obj instanceof OAObject) {
            boolean b = ((OAObject)obj).isVisible(getPropertyName());
            if (!b) return "";
        }
        String val = controlUI.getMaskPasswordValue();
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

    /**
     * Allow size to grow to fit text, from original (attribute) size to this.maxSize.
     */
    public void setMaxSize(int x) {
        this.bMaxSizeChanged |= this.bMaxSizeChanged || (x != this.maxSize);
        this.maxSize = x;
    }
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        boolean b = getOAHtmlComponent().getValueChanged();
        String js = null;
        if (bMaxSizeChanged) {
            bMaxSizeChanged = false;
            js = OAStr.concat(js, "comp.setMaxSize("+getMaxSize()+");", "\n");
        }
        else if (b) { // need to resize
            js = OAStr.concat(js, "comp.adjustSize();", "\n");
        }

        bHasChanges |= OAStr.isNotEmpty(js);
        
        String s = super.getJavaScriptForClient(hsVars, bHasChanges);
        
        js = OAStr.concat(s, js, "\n");
        
        return js;
    }
    
    @Override
    protected void onClientChangeEvent(String newValue) {
        super.onClientChangeEvent(newValue);
        controlUI.setValueDirectly(getHub().getAO(), getValue());
    }

}
