package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.template.OATemplate;
import com.viaoa.uicontroller.OAUIController;
import com.viaoa.util.*;
import com.viaoa.web.html.*;

/**
 * Used to set the inner html for any html element. Has support for OATemplate.
 * 
 * @author vince
 */
public class OAHtmlElement<F extends OAObject> extends HtmlElement implements OAEditorInterface, OAHtmlTableComponentInterface, OAHtmlComponentInterface {
    private Hub hub;
    private String propName;
    private String format;
    private String template;
    private OATemplate oaTemplate;

    private final OAUIController controlUI;
    
    public OAHtmlElement(String selector, Hub<F> hub, String propName) {
        super(selector);
        this.hub = hub;
        this.propName = propName;

        controlUI = new OAUIController(hub, propName) {
            @Override
            public void updateComponent(Object object) {
                String s = OAHtmlElement.this.getValueAsString(hub, object);
                OAHtmlElement.this.setInnerHtml(s);
                OAHtmlElement.this.setVisible(this.isVisible());
            }
            
            @Override
            public void updateLabel(Object object) {
                OAHtmlComponent lbl = getOAHtmlComponent().getLabelComponent();
                if (lbl == null) return;
                lbl.setVisible(isVisible());

                boolean b = this.isEnabled();
                if (!b && getHub().getActiveObject() != null) b = true;
                lbl.setEnabled(b);
            }
        };
    }

    public Hub getHub() {
        return hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
    }

    public String getPropertyName() {
        return this.propName;
    }

    public void setPropertyName(String propName) {
        this.propName = propName;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String template) {
        if (this.template != template) oaTemplate = null;
        this.template = template;

    }

    @Override
    public void beforeGetJavaScriptForClient() {
        if (getHub() == null || getPropertyName() == null) {
            return;
        }

        OAObject obj = (OAObject) getHub().getAO();

        String val;
        if (OAStr.isNotEmpty(getTemplate())) {
            if (oaTemplate == null) {
                oaTemplate = new OATemplate(getTemplate());
            }
            val = oaTemplate.process(obj);
        }
        else {
            if (obj == null) {
                setInnerHtml("");
                return;
            }
            setVisible(obj.isVisible(getPropertyName()));

            val = obj.getPropertyAsString(getPropertyName(), getFormat());
        }
        setInnerHtml(val);
    }

    //qqqqqqqqqqqqqqqqqqqq Change the other OA web components to use getTableCell* method that has the table hub in it. 
    //qqqqqqqqqqqqqqqqqqqqqqqqqqqqq  this one is the working example that uses getPropertypathBetweenHubs qqqqqq

    @Override
    public String getTableCellRenderer(Hub hubTable, HtmlTD td, int row) {

        Object objx;
        if (hubTable != null && hubTable != getHub()) {
            
            objx = hubTable.getAt(row);
            if (objx instanceof OAObject) {
                String pp = OAObjectReflectDelegate.getPropertyPathBetweenHubs(hubTable, getHub());
                //qqqqqq will need support for many(/hub) as the return value ?? qqqqqq and make comma separated listing
                objx = ((OAObject)objx).getProperty(pp);
            }
        }
        else {
            objx = getHub().get(row);
        }

        
        String s;
        
        if (objx instanceof Hub) {
            Hub h = (Hub) objx;
            s = "";//qqqqqqqqqqqqqq
        }
        else if (objx instanceof OAObject) {
            OAObject obj = (OAObject) objx;
            boolean b = obj.isVisible(getPropertyName());
            if (!b) s = "";
            else {
                s = obj.getPropertyAsString(getPropertyName(), getFormat());
                td.addClass("oaNoTextOverflow");
            }
        }
        else {
            s = "";
        }
        
        return s;
    }

    @Override
    public String getTableCellEditor(Hub hubTable, HtmlTD td, int row, boolean bHasFocus) {
        String s = getTableCellRenderer(hubTable, td, row);
        return s;
    }

    
    @Override
    public String getValueAsString(Hub hubFrom, Object obj) {
        if (controlUI == null) return null;
        return controlUI.getValueAsString(obj);
    }
    
}
