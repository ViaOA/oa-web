package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUISelectController;
import com.viaoa.util.OACompare;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.bootstrap.BsTypeAhead;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * Uses Bootstrap TypeAhead, OATypeAhead set Hub.AO
 * <p>
 * TypeAhead<F,T> is used to set the TypeAhead.hub<T>.AO 
 * <p>
 * a Input.text:TypeAhead<F,T> is used to set Hub.AO 
 * 
 * Notes:<br>
 * @author vince
 */
public class OABsTypeAheadSelect extends BsTypeAhead implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUISelectController oaUiControl;

    private static class LastRefresh {
        Hub hubUsed;
        OAObject hubUsedAO;

        // if hubUsed is linked 
        OAObject linkToObject;
        OAObject linkToObjectLinkValue;
    }
    private final LastRefresh lastRefresh = new LastRefresh();
    
    public OABsTypeAheadSelect(String id, OATypeAhead typeAhead) {
        super(id);

        if (typeAhead == null) throw new IllegalArgumentException("typeAhead cant be null");
        if (typeAhead.getHub() == null) throw new IllegalArgumentException("typeAhead hub cant be null");
        setTypeAhead(typeAhead);
        
        oaUiControl = new OAUISelectController(typeAhead.getHub()) {
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

    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        setMultiValue(false);
        super.onSubmitAfterLoadValues(formSubmitEvent);  

        // make sure that it has not changed since it was sent to page
        if (lastRefresh.hubUsed != getHub().getRealHub()) {
            formSubmitEvent.addSyncError("OABsTypeAheadSelect hubUsed changed");
            return;
        }
        
        if (lastRefresh.hubUsedAO != getHub().getAO()) {
            formSubmitEvent.addSyncError("OABsTypeAheadSelect Id="+getId());
            return;
        }

        if (lastRefresh.linkToObject != null) {
            Object objx = (OAObject) lastRefresh.linkToObject.getProperty(oaUiControl.getLinkPropertyName());
            if (objx != lastRefresh.linkToObjectLinkValue) {
                formSubmitEvent.addSyncError("OABsTypeAheadSelect Id="+getId());
                return;
            }
        }
        
        final String id = getValue(); 
        
        OAObject obj;
        if (OAStr.isEmpty(id)) {
            obj = null;
        }
        else {
            obj = typeAhead.findObjectUsingId(id);
            if (obj == null) {
                formSubmitEvent.addSyncError("OABsTypeAheadSelect could not find Id="+id);
                return;
            }
        }
        oaUiControl.onAOChange(lastRefresh.linkToObject, lastRefresh.hubUsedAO, obj);
    }

    public OAUISelectController getController() {
        return oaUiControl;
    }

    public Hub getHub() {
        return oaUiControl.getHub();
    }

    
    
    @Override
    protected void beforeGetScript() {
        setMultiValue(false);
        
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        lastRefresh.hubUsed = getHub().getRealHub();
        lastRefresh.hubUsedAO = (OAObject) getHub().getAO();
        
        Hub hubx = oaUiControl.getLinkToHub();
        lastRefresh.linkToObject = hubx == null ? null : (OAObject) hubx.getAO();
        lastRefresh.linkToObjectLinkValue = lastRefresh.linkToObject == null ? null : ((OAObject) lastRefresh.linkToObject.getProperty(oaUiControl.getLinkPropertyName()));

        boolean b = oaUiControl.isEnabled(lastRefresh.hubUsedAO);
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible(lastRefresh.hubUsedAO);
        setVisible(b);

        String val = getTypeAhead().getDisplayValue(lastRefresh.hubUsedAO);

        // set text.value
        setValue(val);
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
        String displayValue;

        if (obj == null) displayValue = "";
        else {
            displayValue = getTypeAhead().getDisplayValue(obj);
            if (!oaUiControl.isVisible(obj)) displayValue = "";
            else {
                displayValue = getTypeAhead().getDisplayValue(obj);
                td.addClass("oaNoTextOverflow");
            }
        }
        return displayValue;
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
        String s = "<input type='text' id='" + getId() + "'";
        s += " class='oaFitColumnSize'";
        if (obj == null) s += " style='visibility: hidden;'";
        s += ">";
        // note: other settings will be updated by oahtmlcomponent
        return s;
    }
}
