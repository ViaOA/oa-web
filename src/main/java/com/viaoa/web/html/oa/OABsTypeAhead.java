package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.OACompare;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.bootstrap.BsTypeAhead;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

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
    private final OAUIPropertyController oaUiControl;

    private static class LastRefresh {
        OAObject objToUpdate;
        OAObject linkValue;
    }
    private final LastRefresh lastRefresh = new LastRefresh();
    
    /**
     * Create a new OABsTypeAhead that is linked to hub.AO.linkProperty
     * 
     * @param id html element id of input:text 
     * @param hub Hub.AO that gets updated
     * @param propName  name of Hub.AO.linkProperty that is updated.
     * @param typeAhead  has [root] Hub, and propertyPath to allow setting hub.AO.linkProperty 
     */
    public OABsTypeAhead(String id, Hub hub, String propName, OATypeAhead typeAhead) {
        super(id);

        if (typeAhead == null) throw new IllegalArgumentException("typeAhead cant be null");
        setTypeAhead(typeAhead);
        
        typeAhead.setShowHint(false);  // we want the exact match
        
        if (hub == null) throw new IllegalArgumentException("hub cant be null");
        if (OAStr.isEmpty(propName)) throw new IllegalArgumentException("propName cant be empty");
        
        OALinkInfo li = hub.getOAObjectInfo().getLinkInfo(propName);
        if (li == null) throw new IllegalArgumentException("propName must be a reference property to a OneLink");
        if (!li.isOne()) throw new IllegalArgumentException("propName must be a reference property to a OneLink");
        
        if (!li.getToClass().equals(typeAhead.getToClass())) {
            throw new IllegalArgumentException("the link to propertyName=%s must match class=" + typeAhead.getToClass().getSimpleName());
        }
        
        oaUiControl = new OAUIPropertyController(hub, propName) {
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
        
        if (lastRefresh.objToUpdate == null) return;
        
        // make sure that it has not changed since it was sent to page
        if (OACompare.isNotEqual(lastRefresh.linkValue, (OAObject) oaUiControl.getValue(lastRefresh.objToUpdate))) {
            formSubmitEvent.addSyncError("OABsTypeAhead Id="+getId());
            return;
        }

        final String id = getValue(); // sends Id of selected object
        
        OAObject obj;
        if (OAStr.isEmpty(id)) {
            obj = null;
        }
        else {
            obj = typeAhead.findObjectUsingId(id);
            if (obj == null) {
                formSubmitEvent.addSyncError("OABsTypeAhead could not find object for id="+id);
                return;
            }
        }

        oaUiControl.onSetProperty(lastRefresh.objToUpdate, obj);
    }

    public OAUIPropertyController getController() {
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
        
        lastRefresh.objToUpdate = (OAObject) getHub().getAO();
        lastRefresh.linkValue = (OAObject) oaUiControl.getValue(lastRefresh.objToUpdate);

        boolean b = oaUiControl.isEnabled(lastRefresh.objToUpdate);
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible(lastRefresh.objToUpdate);
        setVisible(b);

        String s = getTypeAhead().getDisplayValue(lastRefresh.linkValue);
        setValue(s);
    }
    
    
    @Override
    public String getTableCellRenderer(HtmlTD td, int row) {
        OAObject obj = (OAObject) getHub().get(row);
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
    public String getTableCellEditor(HtmlTD td, int row, boolean bHasFocus) {
        String s = "<input type='text' id='" + getId() + "'";
        s += " class='oaFitColumnSize'";
        if (row < 0 || getHub().get(row) == null) s += " style='visibility: hidden;'";
        s += ">";
        // note: other settings will be updated by oahtmlcomponent
        return s;
    }
}
