package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUICommandController;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.InputButton;

/**
 * InputButton that works with OAModel. 
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
public class OAInputButton extends InputButton implements OAHtmlComponentInterface {

    private final OAUICommandController oaUiControl;
    
    public OAInputButton(String id, Hub hub, OAUICommandController.Command command) {
        super(id);
        oaUiControl = new OAUICommandController(hub, command) {
            @Override
            public Object getManualObject() {
                return OAInputButton.this.getManualObject();
            }
            @Override
            protected boolean performCommand(OAObject obj) {
                if (command == Command.OtherUsesAO 
                        || command == Command.OtherUsesHub 
                        || command == Command.ManualChangeAO 
                        || command == Command.GoTo
                        || command == Command.HubSearch
                        || command == Command.Search
                        || command == Command.Select
                        ) {
                    return OAInputButton.this.performCommand(obj);
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

    /**
     * Called by OAForm whenever this button causes the submit (/ajaxSubmit).
     * <br>
     * By default, this will call OAUICommandController.onCommand, and should be overwritten if using any of the "manual" commands.
     */
    @Override
    protected void onSubmit(OAFormSubmitEvent formSubmitEvent) {
        oaUiControl.onCommand();
    }
    
    /**
     * Override to get manual object, for commands NewManual, AddManual, ManualChangeAO
     */
    protected Object getManualObject() {
        return null;
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
    protected void beforeGetScript() {
        boolean b = oaUiControl.isEnabled();
        setEnabled(b);
        
    }
}
