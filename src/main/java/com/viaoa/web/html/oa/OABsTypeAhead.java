package com.viaoa.web.html.oa;

import java.util.*;
import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIPropertyController;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.bootstrap.BsTypeAhead;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * Create a OA Bootstrap TypeAhead that could be used
 *
 * @author vince
 */
public class OABsTypeAhead extends BsTypeAhead implements OAHtmlComponentInterface {
    private final OAUIPropertyController oaUiControl;
    private final Hub hub;
    

    public OABsTypeAhead(String id, OATypeAhead typeAhead) {
        this(id, null, null, typeAhead);
    }

    public OABsTypeAhead(String id, Hub hub, OATypeAhead typeAhead) {
        this(id, hub, null, typeAhead);
    }
    
    /**
     * Create a new BS TypeAhead that can search for an object match as the user types.
     * Once searched, then:
     * <ul>
     * <li>If hub and propName are used, then set the property value of the selected object.
     * <li>If hub is used and not propName, then the hub AO will be set to the search object.
     * <li>otherwise onSelected(object) can be overwritten to handle the searched object.
     * </ul>
     * 
     * @param id html element id
     */
    public OABsTypeAhead(String id, Hub hub, String propName, OATypeAhead typeAhead) {
        super(id);
        this.hub = hub;
        if (typeAhead == null) typeAhead = OATypeAhead.createTypeAhead(new String[0]);
        setTypeAhead(typeAhead);

        if (hub == null || propName == null) oaUiControl = null;
        else {
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
    }

    /* 
     * Called when form is submitted.<br>
     * Uses OAUISelectController to handle updating selected value.
     */
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        String[] ss = formSubmitEvent.getNameValueMap().get(getId() + "_id");
        if (ss == null || ss.length != 1) return; // no change made
        String id = ss[0];
        
        OAObject obj = null;
        if (id != null && typeAhead != null) {
            obj = typeAhead.findObjectUsingId(id);            
        }
        
        onSelected(obj);
    }

    /**
     * This is called whenever a new submit is done.
     */
    protected void onSelected(OAObject obj) {
        if (getHub() != null) {
            if (oaUiControl == null) {
                getHub().setAO(obj);
            }
            else {
                oaUiControl.onSetProperty(obj);
            }
        }
    }
    
    public Hub getHub() {
        return hub;
    }

    public OAUIPropertyController getController() {
        return oaUiControl;
    }

    @Override
    protected void beforeGetScript() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();
        
        String s = null;
        if (oaUiControl != null) {
            boolean b = oaUiControl.isEnabled();
            setEnabled(bIsFormEnabled && b);
    
            b = oaUiControl.isVisible();
            setVisible(b);
            
            Object obj = oaUiControl.getValue();
            if (obj instanceof OAObject) {
                s = getTypeAhead().getDisplayValue((OAObject) obj);
            }
        }
        else if (hub != null) {
            boolean b = hub.isValid();
            setEnabled(bIsFormEnabled && b);
            
            Object obj = hub.getAO();
            if (obj instanceof OAObject) {
                s = getTypeAhead().getDisplayValue((OAObject) obj);
            }
        }
        setValue(s);
    }
}
