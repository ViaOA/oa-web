package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUIMultiSelectController;
import com.viaoa.uicontroller.OAUISelectController;
import com.viaoa.util.OAConverter;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlOption;
import com.viaoa.web.html.HtmlSelect;
import com.viaoa.web.html.HtmlTD;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;


//  need to add support for recursiveLinks

/**
 * HtmlSelect to work with OAModel and allows for selecting multiple options.
 * <br>
 * Works with master detail and matching property from select hub.
 * <p>
 * Notes:<br>
 * 
 * @author vince
 */
public class OAHtmlMultiSelect<F extends OAObject> extends HtmlSelect implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUIMultiSelectController oaUiControl;
    private String propName;
    private String format;
    private final Hub<F> hubSelect;

    private static class LastRefresh {
        Hub hubUsed; 
        Hub hubSelectUsed;
        final List<String> alSelectOptions = new ArrayList<>();
    }

    private final LastRefresh lastRefresh = new LastRefresh();
    
    /**
     * 
     * @param id html element Id
     * @param hub  master Hub with list of all objects to choose from.
     * @param hubSelect  hub of selected items.
     * @param propName display property
     */
    public OAHtmlMultiSelect(String id, Hub<F> hub, Hub<F> hubSelect, String propName) {
        super(id);
        this.hubSelect = hubSelect;
        this.propName = propName;
    
        // used to interact between component with hub.
        oaUiControl = new OAUIMultiSelectController(hub) {
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

    /* Called when form is submitted.<br> Uses OAUISelectController to handle updating selected  value. 
     */
    @Override
    protected void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
        final String[] values = getValues();
        if (values == null || values.length == 0) {
            if (lastRefresh.alSelectOptions.size() == 0) return;
        }
        
        // make sure that hubSelect still matches guids that were sent.
        if (lastRefresh.hubSelectUsed.size() != lastRefresh.alSelectOptions.size()) {
            formSubmitEvent.addSyncError("OAHtmlMultiSelect select list changed");
            return;
        }
        
        boolean bMatch = true;
        for (String guid : lastRefresh.alSelectOptions) {
            final int guidSelected = OAConverter.toInt(guid);
            boolean bFound = false;
            for (F obj : (Hub<F>) lastRefresh.hubSelectUsed) {
                if (obj.getGuid() == guidSelected) {
                    bFound = true;
                    break;
                }
            }
            if (!bFound) {
                bMatch = false;
                break;
            }
        }
        if (!bMatch) {
            formSubmitEvent.addSyncError("OAHtmlMultiSelect select list changed");
            return;  
        }
        
        List<F> alAdd = new ArrayList<>();
        if (values != null) {
            for (String guid : values) {
                final int guidSelected = OAConverter.toInt(guid);
                boolean bFound = false;
                for (F obj : (Hub<F>) lastRefresh.hubSelectUsed) {
                    if (obj.getGuid() == guidSelected) {
                        alAdd.add(obj);
                        bFound = true;
                        break;
                    }
                }
                if (!bFound) {
                    for (F obj : (Hub<F>) lastRefresh.hubUsed) {
                        if (obj.getGuid() == guidSelected) {
                            alAdd.add(obj);
                            bFound = true;
                            break;
                        }
                    }
                    if (!bFound) {
                        F obj = OAObjectCacheDelegate.getUsingGuid(getHub().getObjectClass(), guidSelected);
                        if (obj != null) {
                            bFound = true;
                            alAdd.add(obj);
                        }
                    }
                }
            }
        }
                
        final List<F> alRemove = new ArrayList<>();
        for (F obj : (Hub<F>) lastRefresh.hubSelectUsed) {
            if (!alAdd.contains(obj)) alRemove.add(obj);
        }
        for (F obj : alAdd) {
            if (!lastRefresh.hubSelectUsed.contains(obj)) lastRefresh.hubSelectUsed.add(obj);
        }
        for (Object obj : alRemove) {
            lastRefresh.hubSelectUsed.remove(obj);
        }
    }

    public Hub<F> getHub() {
        return oaUiControl.getHub();
    }

    public Hub<F> getSelectHub() {
        return hubSelect;
    }

    public OAUIMultiSelectController getController() {
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
        
        lastRefresh.hubUsed = getHub().getRealHub();
        lastRefresh.hubSelectUsed = hubSelect.getRealHub();
        lastRefresh.alSelectOptions.clear();
       
        for (HtmlOption ho : _getOptions(lastRefresh)) {
            add(ho);
            if (ho.getSelected()) lastRefresh.alSelectOptions.add(ho.getValue());
        }
    }

    
    protected List<HtmlOption> _getOptions(final LastRefresh lastRefresh) {
        final List<HtmlOption> alOption = new ArrayList<>();

        setMultiple(true);
        if (getDisplayRows() < 2) setDisplayRows(3);
        
        setEnabled(oaUiControl.isEnabled());
        setVisible(oaUiControl.isVisible());
        
        for (Object obj : lastRefresh.hubUsed) {
            HtmlOption ho;
            if (obj instanceof OAObject) {
                String label = ((OAObject) obj).getPropertyAsString(propName, format);
                if (OAStr.isEmpty(label)) label = " "; // otherwise it will default to displaying value.
                ho = new HtmlOption("" + ((OAObject) obj).getGuid(), label, getSelectHub().contains(obj));
            }
            else {
                ho = new HtmlOption(obj.toString(), obj.toString(), getSelectHub().contains(obj));
            }
            alOption.add(ho);
        }

        // make sure that all of the hubSelect objects are in options
        for (Object obj : lastRefresh.hubSelectUsed) {
            if (lastRefresh.hubUsed.contains(obj)) continue;
            HtmlOption ho;
            if (obj instanceof OAObject) {
                String label = ((OAObject) obj).getPropertyAsString(propName, format);
                if (OAStr.isEmpty(label)) label = " "; // otherwise it will default to displaying value.
                ho = new HtmlOption("" + ((OAObject) obj).getGuid(), label, getSelectHub().contains(obj));
            }
            else {
                ho = new HtmlOption(obj.toString(), obj.toString(), getSelectHub().contains(obj));
            }
            alOption.add(ho);
        }
        return alOption;
    }

    @Override
    public String getTableCellRenderer(HtmlTD td, int row) {
        if (row < 0) return "";
        
        String list = null;
        for (F obj : hubSelect) {
            String s = ((OAObject) obj).getPropertyAsString(propName, format);
            if (list == null) list = "";
            else list += ", ";
            list += s;
        }
        
        if (!oaUiControl.isVisible()) list = "";
        else td.addClass("oaNoTextOverflow");
        
        return list;
    }

    @Override
    public String getTableCellEditor(HtmlTD td, int row, boolean bHasFocus) {
        
        //qqqqqqqq USE: taginput with autocomplete and list of selected items         
        
        return "use taginput";
    }

}
