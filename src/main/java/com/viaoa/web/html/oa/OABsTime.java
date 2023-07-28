package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.*;
import com.viaoa.web.html.bootstrap.BsTime;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;


/**
 * Binds Bootstrap Date to a Hub + propertyName
 *
 */
public class OABsTime extends BsTime implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {

    private final OAUIPropertyController oaUiControl;

    public OABsTime(String id, Hub hub, String propName) {
        super(id);
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
        oaUiControl.setFormat(OATime.JsonFormat);
    }

    public Hub getHub() {
        return oaUiControl.getHub();
    }
    public String getPropertyName() {
        return oaUiControl.getPropertyName();
    }
    public String getFormat() {
        return oaUiControl.getFormat();
    }
    public void setFormat(String format) {
        oaUiControl.setFormat(format);
    }

    
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        if (getHub() == null || getPropertyName() == null) {
            return;
        }
        OAObject obj = (OAObject) getHub().getAO();
        if (obj == null) {
            return;
        }

        final OADateTime dt = getDateTimeValue();
        oaUiControl.onSetProperty(dt);
    }
    
    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible();
        setVisible(b);
        
        b = oaUiControl.isRequired();
        setRequired(b);
        
        String val = oaUiControl.getValueAsString();
        setValue(val);
    }

    @Override
    public String getTableCellRenderer(int row) {
        OAObject obj = (OAObject) getHub().get(row);

        String s;
        if (obj == null) s = "";
        else {
            boolean b = obj.isVisible(getPropertyName());
            if (!b) s = "";
            else {
                s = obj.getPropertyAsString(getPropertyName(), getFormat());
                td.addClass("oaNoTextOverflow");
            }
        }
        return s;
    }
    @Override
    public String getTableCellEditor(int row, boolean bHasFocus) {
        String s = "<input id='"+getId()+"'";
        s += " style='width: 100%; height: 100%; border: none; box-sizing: border-box; padding: 2px; color: black;";
        if (row < 0 || getHub().get(row) == null) s += " visibility: hidden;"; 
        
        s += "'>";
        // note: other settings will be added oahtmlcomponent
        return s;
    }

}
