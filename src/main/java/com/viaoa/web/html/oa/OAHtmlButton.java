package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUICommandController;
import com.viaoa.uicontroller.OAUICommandController.Command;
import com.viaoa.util.*;
import com.viaoa.web.html.HtmlButton;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * HtmlButton to work with OAModel.
 * <p>
 * Uses OAHtmlComponent to interact with html page, and an
 * OAUICommandController to interact with Model OAObject & Hub.
 * <p>
 * Similar to OAInputButton
 * <p>
 * Overwrite method performCommand for manual/custom commands.<br>
 * Overwrite method getManualObject if command requires one
 * 
 * @see OAInputButton
 * @author vince
 */
public class OAHtmlButton extends HtmlButton {
    private final OAUICommandController oaUiControl;

    private static class LastRefresh {
        Hub hubUsed;
        OAObject hubUsedAO;
    }
    private final LastRefresh lastRefresh = new LastRefresh();

    public OAHtmlButton(String selector, Hub hub, OAUICommandController.Command command) {
        this(selector, hub, Type.Button, command);
    }
    
    
    public OAHtmlButton(String selector, Hub hub, Type type, OAUICommandController.Command command) {
        super(selector, type);
        oaUiControl = new OAUICommandController(hub, command) {
            @Override
            protected Object getManualObject() {
                return OAHtmlButton.this.getManualObject();
            }

            @Override
            protected boolean performCommand(Hub hub, OAObject obj) {
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
                    return super.performCommand(hub, obj);
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

/*qqqqq    
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        if (oaUiControl.getCommand().getChangesAO()) {
            Hub h = getHub();
            if (h != null) {
                if (lastRefresh.hubUsed != h.getRealHub() || lastRefresh.hubUsedAO != h.getAO()) {
                    formSubmitEvent.addSyncError("OAHtlButton Id="+getId());
                    return;
                }
            }
        }
    }
*/

    /**
     * Called by OAForm whenever this button causes the submit (/ajaxSubmit). <br>
     * By default, this will call OAUICommandController.onCommand, and should be overwritten if using
     * any of the "manual" commands.
     */
/*qqqqqqqq    
    @Override
    protected void onSubmit(OAFormSubmitEvent formSubmitEvent) {
        oaUiControl.onCommand();
    }
*/
    /**
     * Override to get manual object, for commands NewManual, AddManual, ManualChangeAO
     */
    protected Object getManualObject() {
        OAObject obj = null; 
        switch (oaUiControl.getCommand()) { 
            case NewManual:
                obj = (OAObject) OAObjectReflectDelegate.createNewObject(getHub().getObjectClass());
                break;
            case AddManual:
                obj = (OAObject) OAObjectReflectDelegate.createNewObject(getHub().getObjectClass());
                break;
        }
        return obj;
    }

    /**
     * Override if Command is OtherUsesAO, OtherUsesHub, ManualChangeAO, GoTo, HubSearch, Search, Select
     * 
     * @return true if command was performed, false if it's ignored/skipped.
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

/*qqqqq    
    @Override
    public void beforeGetJavaScriptForClient() {
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
*/
    
    
    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        boolean b = oaUiControl.isEnabled();
        setEnabled(b);

        b = oaUiControl.isVisible();
        setVisible(b);
        
        String js = super.getJavaScriptForClient(hsVars, bHasChanges);
        return js;
    }

    /**
     * This will call UICommandController to process the command.
     * It will call method performCommand, so overwrite it instead of this method.
     */
    @Override
    protected void onClickEvent() {
        super.onClickEvent();
        getController().onCommand();
    }
    
    
    
}

