package com.viaoa.web.html;

import com.viaoa.object.OAObject;
import com.viaoa.util.OAString;
import com.viaoa.web.html.OAHtmlComponent.FormElementType;

import java.util.HashSet;
import java.util.Set;

/*

https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input

*/


/**
 * Support for HTML elements that work with Forms.
 * Input Elements, Select, and TextArea.
 */
public class HtmlFormElement extends HtmlElement {

    public HtmlFormElement(final String id, final FormElementType formElementType) {
        super(id);
        oaHtmlComponent.setFormElementType(formElementType);
    }
    
    public String getName() {
        return oaHtmlComponent.getName();
    }

    public void setName(String name) {
        oaHtmlComponent.setName(name);
    }
    
    public String getLabelId() {
        return oaHtmlComponent.getLabelId();
    }

    public void setLabelId(String id) {
        oaHtmlComponent.setLabelId(id);
    }


    public boolean getEnabled() {
        return oaHtmlComponent.getEnabled();
    }

    public boolean isEnabled() {
        return oaHtmlComponent.getEnabled();
    }

    public void setEnabled(boolean b) {
        oaHtmlComponent.setEnabled(b);
    }

    public void setFocus(boolean b) {
        oaHtmlComponent.setFocus(b);
    }

    public void setFocus() {
        oaHtmlComponent.setFocus();
    }
    
    public void reset() {
        oaHtmlComponent.reset();
    }

    public String getEditorHtml(OAObject obj) {
        return oaHtmlComponent.getEditorHtml(obj);
    }
    
    public String getTableEditorHtml() {
        return oaHtmlComponent.getTableEditorHtml();
    }

    public String getCalcDisplayName() {
        String s = getName();
        if (OAString.isEmpty(s)) {
            s = getId();
        }
        return s;
    }
    
    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        hsSupported.add("name");
        hsSupported.add("labelid");
        hsSupported.add("enabled");
        hsSupported.add("focus");
    }
    
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
}

