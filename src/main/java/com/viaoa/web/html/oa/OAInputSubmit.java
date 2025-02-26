package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUICommandController;
import com.viaoa.uicontroller.OAUICommandController.Command;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputButton;
import com.viaoa.web.html.input.InputSubmit;

/**
 * InputSubmit that works with OAModel. 
 * <p>
 * Like all of the OAWeb Components, this uses an OAHtmlComponent to interact with html page, and
 * an OAUICommandController to interact with Model OAObject & Hub.
 * <p>
 * Similar to OAHtmlButton
 * <p>
 * Overwrite method performCommand for manual/custom commands.<br>
 * Overwrite method getManualObject if command requires one for 
 *  
 * @author vince
 */
public class OAInputSubmit extends InputSubmit implements OAHtmlComponentInterface {
    private final OAUICommandController oaUiControl;

    private static class LastRefresh {
        Hub hubUsed;
        OAObject hubUsedAO;
    }
    private final LastRefresh lastRefresh = new LastRefresh();
    
    public OAInputSubmit(String selector, Hub hub, OAUICommandController.Command command) {
        super(selector);
        oaUiControl = new OAUICommandController(hub, command) {
            @Override
            protected Object getManualObject() {
                return OAInputSubmit.this.getManualObject();
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
                    return OAInputSubmit.this.performCommand(obj);
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

/*qqqqqq    
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        if (oaUiControl.getCommand().getChangesAO()) {
            Hub h = getHub();
            if (h != null) {
                if (lastRefresh.hubUsed != h.getRealHub() || lastRefresh.hubUsedAO != h.getAO()) {
                    formSubmitEvent.addSyncError("OAInputButton Id="+getId());
                    return;
                }
            }
        }
    }
*/
    
    /**
     * Called by OAForm whenever this button causes the submit (/ajaxSubmit).
     * <br>
     * By default, this will call OAUICommandController.onCommand, and should be overwritten if using any of the "manual" commands.
     */
/*qqqqqqq    
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
}
