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
public class OAHtmlElement<F extends OAObject> extends HtmlElement implements OATableColumnInterface {
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

    @Override
    public String getValueAsString(Hub hubFrom, Object obj) {
        if (obj instanceof OAObject) {
            boolean b = ((OAObject)obj).isVisible(getPropertyName());
            if (!b) return "";
        }
        if (controlUI == null) return null;
        return controlUI.getValueAsString(obj, null, 200);
    }
    
}
