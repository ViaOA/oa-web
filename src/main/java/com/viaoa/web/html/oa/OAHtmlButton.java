package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUICommandController;
import com.viaoa.uicontroller.OAUICommandController.Command;
import com.viaoa.util.OADate;
import com.viaoa.web.html.HtmlButton;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * HtmlButton to work with OAModel.
 * <p>
 * Like all of the OAWeb Components, this uses an OAHtmlComponent to interact with html page, and an
 * OAUICommandController to interact with Model OAObject & Hub.
 * <p>
 * Similar to OAInputButton
 * <p>
 * Overwrite method performCommand for manual/custom commands.<br>
 * Overwrite method getManualObject if command requires one for
 * 
 * @see OAInputButton
 * @author vince
 */
public class OAHtmlButton extends HtmlButton implements OAHtmlComponentInterface {
    private final OAUICommandController oaUiControl;

    //qqqqq 0: verify class        
    private static class LastRefresh {
        Hub hubUsed;
        OAObject hubUsedAO;
    }
    private final LastRefresh lastRefresh = new LastRefresh();

    public OAHtmlButton(String id, Hub hub, Type type, OAUICommandController.Command command) {
        super(id, type);
        oaUiControl = new OAUICommandController(hub, command) {
            @Override
            protected Object getManualObject() {
                return OAHtmlButton.this.getManualObject();
            }

            @Override
            protected boolean performCommand(OAObject obj) {
                if (command == Command.OtherUsesAO 
                        || command == Command.OtherUsesHub 
                        || command == Command.GoTo
                        || command == Command.HubSearch
                        || command == Command.Search
                        || command == Command.Select
                        || command == Command.ManualChangeAO
                        ) {
                    return OAHtmlButton.this.performCommand(obj);
                }
                else { 
                    return super.performCommand(obj);
                }
            }

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
        if (oaUiControl.getCommand().getChangesAO()) {
            Hub h = getHub();
            if (h != null) {
                if (lastRefresh.hubUsed != h.getRealHub() || lastRefresh.hubUsedAO != h.getAO()) {
                    formSubmitEvent.addSyncError("OAHtlButton sync error, hub.AO changed");
                    if (oaUiControl.getCommand().getChangesAO()) {
                        formSubmitEvent.cancel();
                    }
                }
            }
        }
    }

    /**
     * Called by OAForm whenever this button causes the submit (/ajaxSubmit). <br>
     * By default, this will call OAUICommandController.onCommand, and should be overwritten if using
     * any of the "manual" commands.
     */
    @Override
    protected void onSubmit(OAFormSubmitEvent formSubmitEvent) {
        oaUiControl.onCommand(lastRefresh.hubUsedAO);
    }

    /**
     * Override to get manual object, for commands NewManual, AddManual, ManualChangeAO
     */
    protected Object getManualObject() {
        OAObject obj = null; 
        switch (oaUiControl.getCommand()) { 
            case NewManual:
                obj = (OAObject) OAObjectReflectDelegate.createNewObject(getHub().getObjectClass());
                getHub().insert(obj, getHub().getPos());
                break;
            case AddManual:
                obj = (OAObject) OAObjectReflectDelegate.createNewObject(getHub().getObjectClass());
                getHub().add(obj);
                break;
        }
        return obj;
    }

    /**
     * Override if Command is OtherUsesAO, OtherUsesHub, ManualChangeAO, GoTo, HubSearch, Search, Select
     * 
     * @return true if command was preformed, false if it's ignored/skipped.
     */
    protected boolean performCommand(OAObject obj) {
        return true;
    }

    public Hub getHub() {
        return oaUiControl.getHub();
    }

    public OAUICommandController getController() {
        return oaUiControl;
    }

    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();

        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        Hub h = getHub();
        lastRefresh.hubUsed = h == null ? null : h.getRealHub();
        lastRefresh.hubUsedAO = h == null ? null : (OAObject) h.getAO();

        b = oaUiControl.isVisible();
        setVisible(b);
    }
}
