package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUISelectController;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlOption;
import com.viaoa.web.html.HtmlSelect;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * HtmlSelect to work with OAModel. Has option for creating multi select.
 * <p>
 * Notes:<br>
 * hubSelect is used for multiselect.<br>
 * setDisplay rows to change to/from dropdown (<2) or scrollinglist (> 1)
 * 
 *  
 * @author vince
 */
public class OAHtmlSelect<F extends OAObject> extends HtmlSelect implements OAHtmlComponentInterface {
    private final OAUISelectController oaUiControl;
    private String propName;
    private String format;
    private final Hub<F> hubSelect;
    private String nullDescription = "";

    public OAHtmlSelect(String id, Hub<F> hub, String propName) {
        this(id, hub, null, propName);
    }
    
    /**
     * Create select list (dropdown or list, based on displayRow value).
     * @param id html element id
     * @param hub list of all options to pick from.
     * @param hubSelect currently selected objets in hub.
     * @param propName property name to display.
     */
    public OAHtmlSelect(String id, Hub<F> hub, Hub<F> hubSelect, String propName) {
        super(id);
        this.hubSelect = hubSelect;
        this.propName = propName;
        
        // used to interact with hub.
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
        
        if (oaUiControl.getLinkToHub() != null) {
            setAjaxSubmit(true);
        }
    }

    public String getFormat() {
        return this.format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    
    /* 
     * Called when form is submitted.<br>
     * Uses OAUISelectController to handle updating selected value.
     */
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        List<F> alAdd = hubSelect == null ? null : new ArrayList<>();

        String[] ss = getValues();
        if (ss != null) {
            for (String s : ss) {
                if ("oanull".equals(s)) {
                    if (hubSelect == null) {
                        oaUiControl.onChangeAO(null);
                        return;
                    }
                    continue;
                }
                for (F obj : getHub()) {
                    if ((obj.getGuid()+"").equals(s)) {
                        if (hubSelect != null) {
                            alAdd.add(obj);
                            break;
                        }
                        else {
                            oaUiControl.onChangeAO(obj);
                            return;
                        }
                    }
                }
            }
        }
        if (alAdd != null) {
            final List<F> alRemove = new ArrayList<>();
            for (F obj : hubSelect) {
                if (!alAdd.contains(obj)) alRemove.add(obj);
            }
            for (F obj : alAdd) {
                if (!hubSelect.contains(obj)) hubSelect.add(obj);
            }
            for (Object obj : alRemove) {
                hubSelect.remove(obj);
            }
            int x = alAdd.size() - 1;
            Object obj = x < 0 ? null : alAdd.get(x);
            oaUiControl.onChangeAO(obj);
        }
        else oaUiControl.onChangeAO(null);
    }
    
    public Hub<F> getHub() {
        return oaUiControl.getHub();
    }

    public Hub<F> getSelectHub() {
        return hubSelect;
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
        
        clearOptions();
        
        if (hubSelect != null) {
            setMultiple(true);
            for (Object obj : oaUiControl.getHub()) {
                HtmlOption ho;
                if (obj instanceof OAObject) {
                    String label = ((OAObject) obj).getPropertyAsString(propName, format);
                    if (OAStr.isEmpty(label)) label = " "; // otherwise it will default to displaying value.
                    ho = new HtmlOption(""+ ((OAObject) obj).getGuid(), label, getSelectHub().contains(obj));
                }
                else {
                    ho = new HtmlOption(obj.toString(), obj.toString(), getSelectHub().contains(obj));
                }
                add(ho);
            }
        }
        else {
            for (Object obj : oaUiControl.getHub()) {
                HtmlOption ho;
                if (obj instanceof OAObject) {
                    String label = ((OAObject) obj).getPropertyAsString(propName, format);
                    if (OAStr.isEmpty(label)) label = " "; // otherwise it will default to displaying value.
                    ho = new HtmlOption(""+ ((OAObject) obj).getGuid(), label, (getHub().getAO() == obj));
                }
                else {
                    ho = new HtmlOption(obj.toString(), obj.toString(), (getHub().getAO() == obj));
                }
                add(ho);
            }
            String s = getNullDescription(); 
            if (s != null) {
                if (s.length() == 0) s = " "; 
                HtmlOption ho = new HtmlOption("oanull", s, getHub().getAO() == null && getDisplayRows() < 2);
                add(ho);
            }
        }   
    }
    
    public String getNullDescription() {
        return nullDescription;
    }
    public void setNullDescription(String s) {
        this.nullDescription = s;
    }
    
}
