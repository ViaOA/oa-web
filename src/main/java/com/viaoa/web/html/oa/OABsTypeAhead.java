package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUISelectController;
import com.viaoa.util.*;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.bootstrap.BsTypeAhead;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;


//qqqqqqqqqqq change this to just set the OATypeAhead.hub.AO

/**
 * Binds a Bootstrap TypeAhead to a Hub.AO.linkProperty.
 * <br>
 * Uses Bootstrap TypeAhead & OATypeAhead to find an object and update Hub.AO.linkProperty
 * <p>
 * a Input.text:TypeAhead<F,T> is used to set Hub.AO.linkProperty<T> 
 * <p>
 * Notes:<br>
 *  
 * @author vince
 */
public class OABsTypeAhead extends BsTypeAhead implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUISelectController oaUiControl;

    private static class LastRefresh {
        Hub hubUsed;
        OAObject objSelected;
        // if hub is linked, then this is the current object that it's linked to and changing.
        OAObject objLinkedTo;  
    }
    private final LastRefresh lastRefresh = new LastRefresh();
    
    /**
     * Create a new OABsTypeAhead 
     * 
     * @param id html element id of input:text 
     * @param typeAhead has hub and properties to use 
     */
    public OABsTypeAhead(String id, OATypeAhead typeAhead) {
        super(id);
        
        if (typeAhead == null) throw new IllegalArgumentException("typeAhead cant be null");
        setTypeAhead(typeAhead);
        
        typeAhead.setShowHint(false);  // we want the exact match
        
        Hub hub = typeAhead.getHub();
        oaUiControl = new OAUISelectController(hub) {
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

        // verify that hubs "have not moved", when using detailHub, linkHub, etc
        if (getHub().getRealHub() != lastRefresh.hubUsed) {
            if (lastRefresh.objLinkedTo == null) return; // it was not linked, so dont change AO
        }
        
        if (lastRefresh.hubUsed != getHub().getRealHub()) {
            formSubmitEvent.addSyncError("OABsTypeAhead Id="+getId());
        }
        else {
            Hub h = getHub().getLinkHub(true);
            if (h != null) {
                if (lastRefresh.objLinkedTo != h.getAO()) {
                    formSubmitEvent.addSyncError("OABsTypeAhead Id="+getId());
                }
            }
        }
        
        final String id = getValue(); // sends Id of selected object
        
        OAObject obj;
        if (OAStr.isEmpty(id)) {
            obj = null;
            oaUiControl.onAOChange(lastRefresh.objLinkedTo, lastRefresh.objSelected, null);
        }
        else {
            obj = typeAhead.findObjectUsingId(id);
            if (obj == null) {
                formSubmitEvent.addSyncError("OABsTypeAhead could not find object for id="+id);
                return;
            }
            oaUiControl.onAOChange(lastRefresh.objLinkedTo, lastRefresh.objSelected, obj);
        }
    }

    public OAUISelectController getController() {
        return oaUiControl;
    }

    public Hub<OAObject> getHub() {
        return oaUiControl.getHub();
    }

    
    
    @Override
    protected void beforeGetScript() {
        
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible();
        setVisible(b);
        setMultiValue(false);

        lastRefresh.hubUsed = getHub().getRealHub();
        lastRefresh.objSelected = getHub().getAO();
        
        Hub<OAObject> h = getHub().getLinkHub(true);
        if (h != null) {
            lastRefresh.objLinkedTo = (OAObject) h.getAO();
        }
        else lastRefresh.objLinkedTo = null;
        
        String s = lastRefresh.objSelected == null ? "" : getTypeAhead().getDisplayValue(lastRefresh.objSelected);  
        setValue(s);
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
