package com.viaoa.web.html.oa;

import java.util.ArrayList;
import java.util.List;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIMultiSelectController;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.bootstrap.BsTypeAhead;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.util.OAWebUtil;

/**
 * Binds a Bootstrap TypeAhead with multivalues to a Hub<br>
 * Uses Bootstrap TypeAhead, OATypeAhead to select objects and add/remove to Hub
 * <p>
 * A TypeAhead<F,T> multivalue=true is used to add/remove Hub<T> objects
 * <p>
 * Notes:<br>
 * @author vince
 */
public class OABsTypeAheadMultiSelect<T extends OAObject> extends BsTypeAhead implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIMultiSelectController oaUiControl;

    private static class LastRefresh {
        Hub hubUsed;
        final List<Object> alSelected = new ArrayList();
    }
    private final LastRefresh lastRefresh = new LastRefresh();

    
    public OABsTypeAheadMultiSelect(String selector, Hub<T> hub, OATypeAheadInputText<?, T> typeAhead) {
        super(selector);
        setTypeAhead(typeAhead);
        setMultiValue(true);
        
        if (typeAhead == null) throw new IllegalArgumentException("typeAhead cant be null");
        setTypeAhead(typeAhead);
        
        if (hub == null) throw new IllegalArgumentException("hub cant be null");

        if (!hub.getObjectClass().equals(typeAhead.getToClass())) {
            throw new IllegalArgumentException("hub and typeAhead.toClass must be the same");
        }

        oaUiControl = new OAUIMultiSelectController(hub) {
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
        setMultiValue(true);

        // 1: make sure that the hubUsed has same objects that were sent to browser
        boolean bMatch = true;
        for (Object obj : lastRefresh.hubUsed) {
            if (!lastRefresh.alSelected.contains(obj)) {
                bMatch = false;
                break;
            }
        }
        
        for (Object obj : lastRefresh.alSelected) {
            if (!lastRefresh.hubUsed.contains(obj)) {
                bMatch = false;
                break;
            }
        }
        if (!bMatch) {
            formSubmitEvent.addSyncError("OABsTypeAheadMultiSelect Id="+getId());
            return;
        }
        

        // 2: update adds/removes
        String[] ids = getValue().split(",");
        
        final OATypeAheadInputText ta = getTypeAhead();

        final List<T> alAdd = new ArrayList<>();
        for (String id : ids) {
            T obj = (T) ta.findObjectUsingId(id);
            if (obj != null) alAdd.add(obj);
            else {
                formSubmitEvent.addSyncError("OABsTypeAheadMultiSelect could not find object for id="+id);
                return;
            }
        }

        final List alRemove = new ArrayList();
        for (Object obj : lastRefresh.hubUsed) {
            if (!alAdd.contains(obj)) alRemove.add(obj);
        }

        for (T obj : alAdd) {
            if (!lastRefresh.hubUsed.contains(obj)) lastRefresh.hubUsed.add(obj);
        }
        for (Object obj : alRemove) {
            lastRefresh.hubUsed.remove(obj);
        }
    }

    public OAUIMultiSelectController getController() {
        return oaUiControl;
    }

    public Hub<T> getHub() {
        return oaUiControl.getHub();
    }

    @Override
    public void beforeGetJavaScriptForClient() {
        setMultiValue(true);
        
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();

        lastRefresh.hubUsed = getHub().getRealHub();

        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible();
        setVisible(b);
    }

    @Override
    protected String getAjaxScript(boolean bIsInitializing) {
        setMultiValue(true);
        final OATypeAheadInputText ta = getTypeAhead();
        final StringBuilder sb = new StringBuilder();
        sb.append("$('#" + getId() + "').tagsinput('removeAll');\n");

        lastRefresh.alSelected.clear();
        
        for (T obj : (Hub<T>) lastRefresh.hubUsed) {
            lastRefresh.alSelected.add(obj);

            TypeAheadValue tav = new TypeAheadValue(obj.getObjectKey().toString(), ta.getDisplayValue(obj), ta.getDropDownDisplayValue(obj));
            
            String json = "{'id':'" + OAWebUtil.createJsString(tav.id, '\'') + 
                    "','display':'" + OAWebUtil.createJsString(tav.display, '\'') + 
                    "','dropdowndisplay':'" + OAWebUtil.createJsString(tav.dropDownDisplay, '\'') + "'}";
            
            sb.append("$('#" + getId() + "').tagsinput('add', " + json + ");\n");
        }
        return sb.toString(); 
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
            if (!oaUiControl.isVisible()) displayValue = "";
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
        // note: other settings will be added oahtmlcomponent
        return s;
    }
}
