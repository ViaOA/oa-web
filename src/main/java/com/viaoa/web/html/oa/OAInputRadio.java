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
public class OAInputRadio extends InputRadio implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIController controlUI;
    private Object selectValue;
    
    
    public OAInputRadio(String selector, Hub hub, String propName, Object selectValue) {
        super(selector);
        this.selectValue = selectValue;
        
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
            formSubmitEvent.addSyncError("OAInputRadio Id="+getId());
            return;
        }

        boolean b = isChecked();
        if (b) controlUI.onSetProperty(lastRefresh.objUsed, selectValue);
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

        b = (lastRefresh.objUsed == null) ? false : OACompare.isEqual(selectValue, lastRefresh.value);
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
        String s = "<input type='radio' id='"+getId()+"'";
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
    protected void onClientCheckedEvent() {
        super.onClientCheckedEvent();
        this.controlUI.setValue(selectValue); // set property
    }
    
}
