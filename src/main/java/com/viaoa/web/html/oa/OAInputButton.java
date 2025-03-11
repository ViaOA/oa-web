package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUICommandController;
import com.viaoa.uicontroller.OAUICommandController.Command;
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
 * use Value to set the button's text.
 *  
 * @see OAHtmlButton
 * @author vince
 */
public class OAInputButton extends InputButton implements OATableColumnInterface {
    private final OAUICommandController controlUI;

    public OAInputButton(String selector, Hub hub, OAUICommandController.Command command) {
        super(selector);
        controlUI = new OAUICommandController(hub, command) {
            @Override
            protected Object getManualObject() {
                return OAInputButton.this.getManualObject();
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
                    return OAInputButton.this.performCommand(obj);
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

    /**
     * Override to get manual object, for commands NewManual, AddManual, ManualChangeAO
     */
    protected Object getManualObject() {
        OAObject obj = null; 
        switch (controlUI.getCommand()) { 
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
        return controlUI.getHub();
    }
    
    public OAUICommandController getController() {
        return controlUI;
    }

    @Override
    public String getValueAsString(Hub hubFrom, Object obj) {
        return htmlComponent.getValue();
    }
    
}
