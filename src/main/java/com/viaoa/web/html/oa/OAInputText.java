package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.uicontroller.*;
import com.viaoa.util.*;
import com.viaoa.web.html.*;
import com.viaoa.web.html.input.InputText;

/**
 * Binds InputText to an Hub + propertyName
 */
public class OAInputText extends InputText implements OAEditorInterface, OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIController controlUI;

    // extra properties
    private int maxSize;
    private boolean bMaxSizeChanged;
    
    
    public OAInputText(String elementIdentifier, Hub hub, String propName) {
        super(elementIdentifier);
        
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
    
    @Override
    public String getValueAsString(Hub hubFrom, Object obj) {
        // qqqqqqqqqqqqqqqqqqqqqqqqqqqqq
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
    
    
    
/*qqqqq    
 
    private static class LastRefresh {
        OAObject objUsed;
        String value;
    }
    private final LastRefresh lastRefresh = new LastRefresh();


    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        if (getHub() == null || getPropertyName() == null) {
            return;
        }
        if (lastRefresh.objUsed == null) return;
        
        // make sure that it did not change
        Object objPrev = oaUiPropertyControl.getValue(lastRefresh.objUsed);
        if (!OACompare.isEqual(objPrev, lastRefresh.value)) {
            formSubmitEvent.addSyncError("OAInputText Id="+getId());
            return;
        }
        
        final String val = getValue();
        if (OAString.isNotEqual(lastRefresh.value, val)) {
            oaUiPropertyControl.onSetProperty(lastRefresh.objUsed, val);
            lastRefresh.value = val;
        }
    }
*/    
    
    /*
    @Override
    public void beforeGetJavaScriptForClient() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();

        lastRefresh.objUsed = (OAObject) oaUiPropertyControl.getHub().getAO(); 
        lastRefresh.value = oaUiPropertyControl.getValueAsString(lastRefresh.objUsed);
        
        boolean b = oaUiPropertyControl.isEnabled(lastRefresh.objUsed);
        setEnabled(bIsFormEnabled && b);

        b = oaUiPropertyControl.isVisible(lastRefresh.objUsed);
        setVisible(b);
        
        b = oaUiPropertyControl.isRequired();
        setRequired(b);
        
        if (getConversion() == 0) {
            OAObjectInfo oi = getHub().getOAObjectInfo();
            OAPropertyInfo pi = oi.getPropertyInfo(getPropertyName());
            if (pi != null) {
                if (pi.isUpper()) oaUiPropertyControl.setConversion('U');
                else if (pi.isLower()) oaUiPropertyControl.setConversion('L');
            }
        }
        
        setValue(lastRefresh.value);
        
        OAObjectInfo oi = getHub().getOAObjectInfo();
        OAPropertyInfo pi = oi.getPropertyInfo(getPropertyName());
        
        if (pi != null) {
            if (getMaxLength() == 0 && pi.getMaxLength() > 0) setMaxLength(pi.getMaxLength());
            if (getMinLength() == 0) setMinLength(pi.getMinLength());
            
            if (getSize() < 1) {
                setSize(pi.getDisplayLength());
            }
        }
    }

    @Override
    public String getTableCellRenderer(Hub hubTable, HtmlTD td, int row) {
        OAObject obj;
        if (hubTable != null && hubTable != getHub()) {
            obj = (OAObject) hubTable.getAt(row);
            if (obj != null) {
                String pp = OAObjectReflectDelegate.getPropertyPathBetweenHubs(hubTable, getHub());
                obj = (OAObject) obj.getProperty(pp);
            }
        }
        else {
            obj = (OAObject) getHub().get(row);
        }

        String s;
        if (obj == null) s = "";
        else {
            boolean b = obj.isVisible(getPropertyName());
            if (!b) s = "";
            else {
                s = obj.getPropertyAsString(getPropertyName(), getFormat());
                if (s == null) s = "";
                else td.addClass("oaNoTextOverflow");
            }
        }
        return s;
    }
    
     
    @Override
    public String getTableCellEditor(Hub hubTable, HtmlTD td, int row, boolean bHasFocus) {
        OAObject obj;
        if (hubTable != null && hubTable != getHub()) {
            obj = (OAObject) hubTable.getAt(row);
            if (obj != null) {
                String pp = OAObjectReflectDelegate.getPropertyPathBetweenHubs(hubTable, getHub());
                obj = (OAObject) obj.getProperty(pp);
            }
        }
        else {
            obj = (OAObject) getHub().get(row);
        }
        
        String s = "<input type='text' id='"+getId()+"'";
        s += " class='oaFitColumnSize'";  //qqqqqqqqqqqq convert to kabob name
        if (obj == null) s += " style='visibility: hidden;'"; 
        s += ">";
        // note: other settings will be added by InputText
        return s;
    }
*/

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
        
        //qqqqqqqq was: String s = getOAHtmlComponent().getJavaScriptForClient(hsVars, bHasChanges);
        js = OAStr.concat(s, js, "\n");
        
        return js;
    }
    
    @Override
    protected void onClientChangeEvent(String newValue) {
        super.onClientChangeEvent(newValue);
        controlUI.setValue(getValue());
    }
    
}
