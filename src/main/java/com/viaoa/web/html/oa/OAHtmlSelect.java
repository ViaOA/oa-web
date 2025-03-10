package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUISelectController;
import com.viaoa.util.*;
import com.viaoa.web.html.*;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * HtmlSelect to work with OAModel.
 * <p>
 * 
 * Notes:<br>
 * setDisplay rows to change to/from dropdown (=1) or scrollinglist (> 1)<br>
 * 
 * @author vince
 */
public class OAHtmlSelect extends HtmlSelect implements OATableColumnInterface {
    private final OAUISelectController controlUISelect;
    private final String propName;
    private String format;
    private String nullDescription = ""; 
    private String jsClient, jsSelected;
    private volatile boolean bIgnoreSelected;

    public OAHtmlSelect(String selector, Hub hub, String propName) {
        this(selector, hub, propName, null);
    }

    public OAHtmlSelect(String selector, Hub hub, String propName, Hub hubSelect) {
        super(selector);
        this.propName = propName;

        // used to interact between component with hub.
        controlUISelect = new OAUISelectController(hub, propName, hubSelect, false) {
            @Override
            public void updateComponent(Object object) {
                OAHtmlSelect.this.updateComponent(object);
            }

            @Override
            public void updateLabel(Object object) {
                OAHtmlSelect.this.updateLabel(object);
            }
            @Override
            public void add(Object obj) {
                String s = (obj == null) ? getNullDescription() : getValueAsString(obj);
                HtmlOption ho = new HtmlOption("id"+getOptions().size(), s);
                getOptions().add(ho);
                jsClient = OAStr.concat(jsClient, "comp.add('"+s+"', '"+ho.getValue()+"');", "\n");
            }
            @Override
            public void insert(Object obj, int pos) {
                if (OAStr.isNotEmpty(getNullDescription())) pos++;
                String s = getValueAsString(obj);
                HtmlOption ho = new HtmlOption("id"+getOptions().size(), s);
                getOptions().add(pos, ho);
                jsClient = OAStr.concat(jsClient, "comp.insert('"+s+"', '"+ho.getValue()+"', "+pos+");", "\n");
                addSelected();
            }
            @Override
            public void remove(int pos) {
                if (OAStr.isNotEmpty(getNullDescription())) pos++;
                getOptions().remove(pos);
                jsClient = OAStr.concat(jsClient, "comp.remove("+pos+");", "\n");
                addSelected();
            }
            
            void addSelected() {
                int[] poss = new int[] {};
                if (hubSelect != null) {
                    for (Object obj : hubSelect) {
                        int p = hub.getPos(obj);
                        poss = OAArray.add(poss,  p);
                    }
                }
                else {
                    int pos = hub.getPos();
                    if (pos >= 0) poss = new int[pos];
                }
                this.setSelected(poss);
            }
            
            @Override
            public void clear() {
                getOptions().clear();
                jsClient = OAStr.concat(jsClient, "comp.clear();", "\n");
                addSelected();
            }
            @Override
            public void newList() {
                getOptions().clear();
                jsClient = OAStr.concat(jsClient, "comp.clear();", "\n");
                if (OAStr.isNotEmpty(nullDescription)) {
                    this.add(null);
                }
                for (Object obj : getHub()) {
                    this.add(obj);
                }
                addSelected();
            }
            @Override
            public void changed(Object object) {
                int pos = getHub().getPos(object);
                if (OAStr.isNotEmpty(getNullDescription())) pos++;
                this.remove(pos);
                this.insert(object, pos);
            }
            @Override
            public void setSelected(int[] poss) {
                jsSelected = null;
                String js = "";
                int i = 0;
                for (HtmlOption ho : getOptions()) {
                    boolean b = OAArray.contains(poss, i);
                    if (b && !bIgnoreSelected) {
                        int x = OAStr.isNotEmpty(getNullDescription()) ? i + 1 : i;
                        js = OAStr.concat(js, ""+x, ",");
                    }
                    ho.setSelected(b);
                    i++;
                }
                if (!bIgnoreSelected) {
                    if (bIgnoreSelected && OAStr.isEmpty(js) && OAStr.isNotEmpty(getNullDescription())) js = "0";
                    jsSelected = "comp.setSelected(["+js+"]);";
                }
            }
        };
        if (hubSelect != null) setMultiple(true);
        controlUISelect.reset();
        controlUISelect.newList();
    }

    @Override
    public void close() {
        super.close();
        if (controlUISelect != null) controlUISelect.close();
    }
    
    
    public String getPropertyName() {
        return this.propName;
    }
    
    public String getFormat() {
        return this.format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    
    public Hub getHub() {
        return controlUISelect.getHub();
    }
    public OAUISelectController getController() {
        return controlUISelect;
    }

    public String getNullDescription() {
        return nullDescription;
    }

    public void setNullDescription(String s) {
        this.nullDescription = s;
    }
    
    
    /*qqqqq
    @Override
    public void beforeGetJavaScriptForClient() {
        OAForm form = getOAHtmlComponent().getForm();
        final boolean bIsFormEnabled = form == null || form.getEnabled();

        boolean b = oaUiControl.isEnabled();
        setEnabled(bIsFormEnabled && b);

        b = oaUiControl.isVisible();
        setVisible(b);
        setMultiple(false);

        clearOptions();

        lastRefresh.hubUsed = getHub().getRealHub();
        lastRefresh.objSelected = getHub().getAO();
        
        Hub h = getHub().getLinkHub(true);
        if (h != null) {
            lastRefresh.objLinkedTo = (OAObject) h.getAO();
        }
        else lastRefresh.objLinkedTo = null;

        for (HtmlOption ho : getHtmlOptions(lastRefresh)) {
            add(ho);
        }
    }

    protected List<HtmlOption> getHtmlOptions(final LastRefresh lastRefresh) {
        final List<HtmlOption> alOption = new ArrayList<>();
        
        for (Object obj : lastRefresh.hubUsed) {
            HtmlOption ho;
            if (obj instanceof OAObject) {
                String label = ((OAObject) obj).getPropertyAsString(propName, format);
                if (OAStr.isEmpty(label)) label = " "; // otherwise it will default to displaying value.
                ho = new HtmlOption("" + ((OAObject) obj).getGuid(), label, (lastRefresh.objSelected == obj));
            }
            else {
                ho = new HtmlOption(obj.toString(), obj.toString(), (lastRefresh.objSelected == obj));
            }
            alOption.add(ho);
        }
        String s = getNullDescription();
        if (s != null) {
            if (s.length() == 0) s = " ";
            HtmlOption ho = new HtmlOption("oanull", s, lastRefresh.objSelected == null);
            alOption.add(ho);
        }
        return alOption;
    }


    @Override
    public String getTableCellRenderer(HtmlTD td, int row) {
        if (row < 0) return "";
        
        if (oaUiControl.getLinkToHub() == null) return "";
        OAObject objLinkTo = (OAObject) oaUiControl.getLinkToHub().get(row);
        if (objLinkTo == null) return "";

        String s;
        if (!oaUiControl.isVisible(objLinkTo)) s = "";
        else {
            Object objx = objLinkTo.getProperty(oaUiControl.getLinkPropertyName());
            if (!(objx instanceof OAObject)) return "";
            s = ((OAObject) objx).getPropertyAsString(propName, format);
            if (s == null) s = "";
            td.addClass("oaNoTextOverflow");
        }
        return s;
    }

    @Override
    public String getTableCellEditor(HtmlTD td, int row, boolean bHasFocus) {
        boolean bVisible = true;
        if (oaUiControl.getLinkToHub() == null) bVisible = false;
        else {
            OAObject objLinkTo = (OAObject) oaUiControl.getLinkToHub().get(row);
            if (objLinkTo == null) {
                bVisible = false;
            }
            else bVisible = oaUiControl.isVisible(objLinkTo);
        }
        setVisible(bVisible);

        String s = "<select id='" + getId() + "' name='" + getId() + "'";
        s += " class='oaFitColumnSize'";
        
        Hub h = getHub().getLinkHub(true);
        
        
        if (row < 0 || (h != null && h.get(row) == null)) {
            s += " style='visibility: hidden;'";
        }
        s += ">";
        // note: options will be added oahtmlcomponent
        s += "</select>";
        return s;
    }
    */








    /** 
     * Called when a change is necessary for UI component. 
     * */
    public void updateComponent(Object object) {
        setVisible(getController().isVisible());
        setEnabled(getController().isEnabled());
    }
    

    public void updateLabel(Object object) {
        OAHtmlComponent lbl = getOAHtmlComponent().getLabelComponent();
        if (lbl == null) return;
        lbl.setVisible(getController().isVisible());
        lbl.setEnabled(getController().isEnabled());
    }
    

    
    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        String js = jsClient;
        jsClient = null;

        if (!getOAHtmlComponent().isInitialized()) js = null;
        else {
            if (OAStr.isNotEmpty(jsSelected)) js = OAStr.concat(js, jsSelected, "\n");
        }
        jsSelected = null;

        bHasChanges |= OAStr.isNotEmpty(js);
        String js1 = super.getJavaScriptForClient(hsVars, bHasChanges);
        
        js = OAStr.concat(js1, js, "\n");
        return js;
    }
    
    protected void onClientChangeEvent(int[] selectIndexes) {
        
        // qqqqq verify that it's allowed to be changed
        super.onClientChangeEvent(selectIndexes);  // updates htmlOptions[]
        
        Hub hub = getHub();
        Hub hubSelected = getController().getSelectHub();
        bIgnoreSelected = true;
        if (hubSelected != null) {
            for (int pos : selectIndexes) {
                Object obj = hub.getAt(pos);
                hubSelected.add(obj);
            }
            for (Object obj : hubSelected) {
                int pos = hub.getPos(obj);
                if (!OAArray.contains(selectIndexes, pos)) {
                    hubSelected.remove(obj);
                }
            }
        }
        else {
            int pos = selectIndexes.length - 1;
            if (pos >= 0) pos = selectIndexes[pos];
            if (OAStr.isNotEmpty(getNullDescription())) pos--;
            getHub().setPos(pos);
        }
        bIgnoreSelected = false;
    }

    @Override
    public String getValueAsString(Hub hubFrom, Object obj) {
        if (obj instanceof OAObject) {
            boolean b = ((OAObject)obj).isVisible(getPropertyName());
            if (!b) return "";
        }
        String s = controlUISelect.getValueAsString(hubFrom, obj);
        return s;
    }

}




