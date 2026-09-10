package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.lang.OAStr;
import com.viaoa.object.*;
import com.viaoa.ui.controller.OAUICommandController;
import com.viaoa.ui.controller.OAUICommandController.Command;
import com.viaoa.ui.controller.OAUIMethodController;
import com.viaoa.web.html.HtmlButton;
import com.viaoa.web.html.OAHtmlComponent;
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
    private OAUIMethodController oaMethodControl;

    private static class LastRefresh {
        Hub hubUsed;
        OAObject hubUsedAO;
    }
    private final LastRefresh lastRefresh = new LastRefresh();

    public OAHtmlButton(String selector, Hub hub, OAUICommandController.Command command) {
        this(selector, hub, Type.Button, command);
    }

    public OAHtmlButton(String selector, Hub hub, String methodName) {
        this(selector, hub, Type.Button, Command.OtherUsesAO);
        setMethodName(methodName);
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
            	try {
            		return _performCommand(hub, obj);
            	}
            	catch (Exception e) {
            		onError("Command Exception", e.getMessage());
            	}
            	return false;
            }
            protected boolean _performCommand(Hub hub, OAObject obj) {
            	if (oaMethodControl != null) {
            		oaMethodControl.setTitle(OAHtmlButton.this.getTitle());
            		oaMethodControl.setCompletedMessage(OAHtmlButton.this.getCompletedMessage());
            		return onCallMethod();
            	}
            	else if (command == Command.OtherUsesAO 
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
            protected boolean onConfirm(String confirmMessage, String title) {
            	return OAHtmlButton.this.onConfirm(confirmMessage, title);
            }
            
            @Override
            protected void onCompleted(String completedMessage, String title) {
            	OAHtmlButton.this.onCompleted(completedMessage, title);	
            }

            @Override
            protected void onError(String errorMessage, String detailMessage) {
            	OAHtmlButton.this.onError(errorMessage, detailMessage);	
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
                obj = (OAObject) oaUiControl.getOA().internal().objects().reflect().createNewObject(getHub().getObjectClass());
                break;
            case AddManual:
                obj = (OAObject) oaUiControl.getOA().internal().objects().reflect().createNewObject(getHub().getObjectClass());
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
    
    
    
    private String jsAddMsg;
    
	/**
	 * Method in object to execute on active object in hub.
	 */
	public void setMethodName(String methodName) {
		if (OAStr.isEmpty(methodName)) oaMethodControl = null;
		else oaMethodControl = new OAUIMethodController(getHub(), methodName) {
			@Override
			protected void onError(String errorMessage, String detailMessage) {
				OAHtmlButton.this.onError(errorMessage, detailMessage);
			}
			@Override
			protected void onCompleted(String completedMessage, String title) {
				OAHtmlButton.this.onCompleted(completedMessage, title);
			}
			@Override
			protected boolean onConfirm(String confirmMessage, String title) {
				return OAHtmlButton.this.onConfirm(confirmMessage, title);
			}
		};
	}
	

	protected void onError(String errorMessage, String detailMessage) {
//qqqqqqqqqqqqqq				
getOAHtmlComponent().addMessage("Error", detailMessage, OAHtmlComponent.MessageType.Error, OAHtmlComponent.MessageTarget.Notify);				
		String s = "ERROR: " + errorMessage + " - " + detailMessage;
		s = OAStr.escapeJs(s, '\'');
		jsAddMsg = OAStr.append(jsAddMsg, "console.log('" + s + "');", "\n");
	}

	protected void onCompleted(String completedMessage, String title) {
		
getOAHtmlComponent().addNotifyMessage(title, completedMessage);				
		
		String s = "COMPLETED: " + title + " - " + completedMessage;
		s = OAStr.escapeJs(s, '\'');
		jsAddMsg = OAStr.append(jsAddMsg, "console.log('" + s + "');", "\n");
	}

	protected boolean onConfirm(String confirmMessage, String title) {
//qqqqqqqqqqqqqqqqqqq todo:				
		String s = "CONFIRM: " + title + " - " + confirmMessage;
		s = OAStr.escapeJs(s, '\'');
		jsAddMsg = OAStr.append(jsAddMsg, "console.log('" + s + "');", "\n");
		return true;
	}
	

	/**
	 * Method in object to execute on active object in hub.
	 */
	public String getMethodName() {
		if (oaMethodControl == null) return null;
		return oaMethodControl.getMethodName();
	}

	public boolean onCallMethod() {
		if (oaMethodControl == null) return false;
		return oaMethodControl.onCallMethod();
	}
	
	public void setCompletedMessage(String msg) {
		oaUiControl.setCompletedMessage(msg);
	}
	public String getCompletedMessage() {
		return oaUiControl.getCompletedMessage();
	}
    
    public void setConfirmMessage(String msg) {
    	oaUiControl.setConfirmMessage(msg);
    }
    public String getConfirmMessage() {
    	return oaUiControl.getConfirmMessage();
    }

    private boolean bWasClicked;
    
	@Override
	protected void onClickEvent() {
		bWasClicked = true;
		oaUiControl.onCommand();
	}
	
    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        boolean b = oaUiControl.isEnabled();
        if (oaMethodControl != null) b &= oaMethodControl.isEnabled();
        setEnabled(b);

        String jsAdd2 = null;
        if (bWasClicked) {
        	bWasClicked = false;
        	if (b) jsAdd2 = "ele.disabled = false;\n";
        }
        
        
        b = oaUiControl.isVisible();
        if (oaMethodControl != null) b &= oaMethodControl.isVisible();
        setVisible(b);
        
        String js = super.getJavaScriptForClient(hsVars, bHasChanges || (jsAdd2 != null || jsAddMsg != null));
        
        if (jsAddMsg != null) js += jsAddMsg + "\n";
        jsAddMsg = null;
        if (jsAdd2 != null) js += jsAdd2 + "\n";
        
        return js;
    }

    
}

