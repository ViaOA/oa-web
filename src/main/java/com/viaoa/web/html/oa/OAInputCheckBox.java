package com.viaoa.web.html.oa;

import java.util.Set;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.*;
import com.viaoa.util.*;
import com.viaoa.web.html.*;
import com.viaoa.web.html.input.InputCheckBox;

/**
 * Binds Input CheckBox to an Hub + propertyName
 */
public class OAInputCheckBox extends InputCheckBox implements OATableColumnInterface {
    private final OAUIController controlUI;
    private Object onValue, offValue;
    
    public OAInputCheckBox(String selector, Hub hub, String propName) {
        this(selector, hub, propName, true, false);
    }
    
    public OAInputCheckBox(String id, Hub hub, String propName, Object onValue, Object offValue) {
        super(id);
        this.onValue = onValue;
        this.offValue = offValue;
        
        
        controlUI = new OAUIController(hub, propName) {
            @Override
            public void updateComponent(Object object) {
                Object obj = this.getValue(object);
                boolean b = OACompare.isEqual(obj, onValue);
                OAInputCheckBox.this.setChecked(b);
                OAInputCheckBox.this.setEnabled(this.isEnabled());
                OAInputCheckBox.this.setVisible(this.isVisible());
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

/*qqqq    
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        if (getHub() == null || getPropertyName() == null) {
            return;
        }
        
        if (lastRefresh.objUsed == null) return;
        
        // make sure that it did not change
        Object objPrev = controlUI.getValue(lastRefresh.objUsed);
        if (!OACompare.isEqual(objPrev, lastRefresh.value)) {
            formSubmitEvent.addSyncError("OAInputCheckBox Id="+getId());
            return;
        }

        boolean b = isChecked();
        controlUI.onSetProperty(lastRefresh.objUsed, b ? onValue : offValue);
    }
*/    
/*    
    @Override
    public void beforeGetJavaScriptForClient() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        lastRefresh.objUsed = (OAObject) controlUI.getHub().getAO(); 
        lastRefresh.value = controlUI.getValue(lastRefresh.objUsed);
        
        boolean b = controlUI.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = controlUI.isVisible();
        setVisible(b);

        b = (lastRefresh.objUsed == null) ? false : OACompare.isEqual(onValue, lastRefresh.value);
        setChecked(b);
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
        String s = "<input type='checkbox' id='"+getId()+"'";
        s += " class='oaFitColumnSize'";
        if (obj == null) s += " style='visibility: hidden;'"; 
        s += ">";
        return s;
    }
*/    
    
    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        String js = super.getJavaScriptForClient(hsVars, bHasChanges);
        return js;
    }
    

    @Override
    protected void onClientChangeEvent(boolean newValue) {
        super.onClientChangeEvent(newValue);
        this.controlUI.setValue(newValue); // set property
    }
    
    @Override
    public String getValueAsString(Hub hubFrom, Object obj) {
        if (obj instanceof OAObject) {
            boolean b = ((OAObject)obj).isVisible(getPropertyName());
            if (!b) return "";
        }
        Object val = controlUI.getValue(obj);
        String s = OAStr.toString(val);
        return s;
    }
}
