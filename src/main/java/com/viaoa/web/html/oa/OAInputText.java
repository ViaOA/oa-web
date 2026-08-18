package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.lang.OAStr;
import com.viaoa.object.OAObject;
import com.viaoa.template.OATemplate;
import com.viaoa.ui.controller.OAUIController;
import com.viaoa.web.html.*;
import com.viaoa.web.html.input.InputText;

/**
 * Binds InputText to an Hub + propertyName
 */
public class OAInputText extends InputText implements OATableColumnInterface {
    private final OAUIController controlUI;

    // extra properties
    private int maxSize;
    private boolean bMaxSizeChanged;
    
    private String toolTipTemplate;
    private OATemplate toolTipOATemplate;
    
    
    public OAInputText(String elementIdentifier, Hub hub, String propName) {
        this(elementIdentifier, hub, propName, -1);
    }
    
    public OAInputText(String elementIdentifier, Hub hub, String propName, int size) {
        super(elementIdentifier);
        if (size > 0) setSize(size);
        controlUI = new OAUIController(hub, propName) {
            @Override
            public void updateComponent(Object object) {
                String s = this.getValueAsString(object);
                OAInputText.this.setValue(s);
                OAInputText.this.setEnabled(this.isEnabled());
                OAInputText.this.setVisible(this.isVisible());
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
    
    public String getToolTipTemplate() {
        return this.toolTipTemplate;
    }
    
    public void setToolTipTemplate(String template) {
        if (this.toolTipTemplate != template) toolTipOATemplate = null;
        this.toolTipTemplate = template;
    }
    public void setToolTipTextTemplate(String template) {
    	setToolTipTemplate(template);
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
    
    public void setConversion(char conv) {
        controlUI.setConversion(conv);
    }
    public char getConversion() {
        return controlUI.getConversion();
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
    public void beforeGetJavaScriptForClient() {
    	super.beforeGetJavaScriptForClient();
        if (getHub() == null || getPropertyName() == null) {
            return;
        }
        if (OAStr.isNotEmpty(getToolTipTemplate())) {
            if (toolTipOATemplate == null) {
            	toolTipOATemplate = new OATemplate(getToolTipTemplate());
            }
            OAObject obj = (OAObject) getHub().getAO();
            String val = toolTipOATemplate.process(obj);
            setToolTip(val);
        }
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
        controlUI.setValue(getValue());
    }
    
}
