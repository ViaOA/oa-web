package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUISelectController;
import com.viaoa.util.OAConverter;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlOption;
import com.viaoa.web.html.HtmlSelect;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

// qqqqqqqqqqq need to add support for recursiveLinks

/**
 * HtmlSelect to work with OAModel.
 * <p>
 * Notes:<br>
 * setDisplay rows to change to/from dropdown (=1) or scrollinglist (> 1)<br>
 * Set ajaxSubmit=true if other components need to change when and item is selected.<br>
 * 
 * @author vince
 */
public class OAHtmlSelect<F extends OAObject> extends HtmlSelect implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUISelectController oaUiControl;
    private String propName;
    private String format;
    private String nullDescription = "";

    /*
     * Hubs can change (link, master/detail) between the time a page is sent,
     * and when it is submitted back.<br>
     * This class is used to capture the current hub/object/selected info
     * when a page is sent. 
     */
    private static class LastRefresh {
        Hub hubUsed;
        OAObject objSelected;

        // if hub is linked, then this is the current object that it's linked to and changing.
        OAObject objLinkedTo;  
    }
    
    private final LastRefresh lastRefresh = new LastRefresh();

    public OAHtmlSelect(String id, Hub<F> hub, String propName) {
        super(id);
        this.propName = propName;

        // used to interact between component with hub.
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

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    
    /* Called when form is submitted.<br> 
     * Uses OAUISelectController to handle updating selected value. 
     */
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        final String[] values = getValues();
        if (values == null || values.length == 0) return;
        
        // verify that hubs "have not moved", when using detailHub, linkHub
        if (getHub().getRealHub() != lastRefresh.hubUsed) {
            if (lastRefresh.objLinkedTo == null) return; // it was not linked, so do change AO
        }
        
        if ("oanull".equals(values[0])) {
            // change AO
            // if hub is linked, then it will update the linkedTo object
            oaUiControl.onAOChange(lastRefresh.objLinkedTo, lastRefresh.objSelected, null);
        }
        else {
            if (lastRefresh.objSelected == null || (lastRefresh.objSelected.getGuid() != OAConverter.toInt(values[0]))) {
                // if a different option was selected, find the object using the value (guid) and set the AO in hub,
                //     ... if there's a linkToObject, then it will be updated during the hub.setAO
                final int guidSelected = OAConverter.toInt(values[0]);
                boolean bFound = false;
                // look for object in hubUsed
                for (F obj : (Hub<F>) lastRefresh.hubUsed) {
                    if (obj == lastRefresh.objSelected) continue;
                    if (obj.getGuid() == guidSelected) {
                        oaUiControl.onAOChange(lastRefresh.objLinkedTo, lastRefresh.objSelected, obj);
                        bFound = true;
                        break;
                    }
                }
                if (!bFound) {
                    // (currency issue) object can have been removed from hubUsed, get from cache.
                    F obj = OAObjectCacheDelegate.getUsingGuid(getHub().getObjectClass(), guidSelected);
                    if (obj != null) {
                        oaUiControl.onAOChange(lastRefresh.objLinkedTo, lastRefresh.objSelected, obj);
                    }
                }
            }
        }
    }

    public Hub<F> getHub() {
        return oaUiControl.getHub();
    }

    public OAUISelectController getController() {
        return oaUiControl;
    }

    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();

        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible();
        setVisible(b);
        setMultiple(false);

        clearOptions();

        lastRefresh.hubUsed = getHub().getRealHub();
        lastRefresh.objSelected = getHub().getAO();
        
        Hub h = getHub().getLinkHub(true);
        if (h != null) {
            lastRefresh.objLinkedTo = (OAObject) h.getAO();
        }
        else lastRefresh.objLinkedTo = null;

        for (HtmlOption ho : getHtmlOptions(lastRefresh)) {
            add(ho);
        }
    }

    protected List<HtmlOption> getHtmlOptions(final LastRefresh lastRefresh) {
        final List<HtmlOption> alOption = new ArrayList<>();
        
        for (Object obj : lastRefresh.hubUsed) {
            HtmlOption ho;
            if (obj instanceof OAObject) {
                String label = ((OAObject) obj).getPropertyAsString(propName, format);
                if (OAStr.isEmpty(label)) label = " "; // otherwise it will default to displaying value.
                ho = new HtmlOption("" + ((OAObject) obj).getGuid(), label, (lastRefresh.objSelected == obj));
            }
            else {
                ho = new HtmlOption(obj.toString(), obj.toString(), (lastRefresh.objSelected == obj));
            }
            alOption.add(ho);
        }
        String s = getNullDescription();
        if (s != null) {
            if (s.length() == 0) s = " ";
            HtmlOption ho = new HtmlOption("oanull", s, lastRefresh.objSelected == null);
            alOption.add(ho);
        }
        return alOption;
    }

    public String getNullDescription() {
        return nullDescription;
    }

    public void setNullDescription(String s) {
        this.nullDescription = s;
    }

    @Override
    public String getTableCellRenderer(HtmlTD td, int row) {
        if (row < 0) return "";
        if (oaUiControl.getLinkToHub() == null) return "";
        OAObject objLinkTo = (OAObject) oaUiControl.getLinkToHub().get(row);
        if (objLinkTo == null) return "";

        String s;
        if (!oaUiControl.isVisible(objLinkTo)) s = "";
        else {
            Object objx = objLinkTo.getProperty(oaUiControl.getLinkPropertyName());
            if (!(objx instanceof OAObject)) return "";
            s = ((OAObject) objx).getPropertyAsString(propName, format);
            if (s == null) s = "";
            td.addClass("oaNoTextOverflow");
        }
        return s;
    }

    @Override
    public String getTableCellEditor(HtmlTD td, int row, boolean bHasFocus) {
        boolean bVisible = true;
        if (oaUiControl.getLinkToHub() == null) bVisible = false;
        else {
            OAObject objLinkTo = (OAObject) oaUiControl.getLinkToHub().get(row);
            if (objLinkTo == null) {
                bVisible = false;
            }
            else bVisible = oaUiControl.isVisible(objLinkTo);
        }
        setVisible(bVisible);

        String s = "<select id='" + getId() + "' name='" + getId() + "'";
        s += " class='oaFitColumnSize'";
        if (row < 0 || getHub().get(row) == null) s += " style='visibility: hidden;'";
        s += ">";
        // note: options will be added oahtmlcomponent
        s += "</select>";
        return s;
    }
}
