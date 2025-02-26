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

    public HtmlFormElement(final String selector, final FormElementType formElementType) {
        super(selector);
        htmlComponent.setFormElementType(formElementType);
    }
    
    public String getName() {
        return htmlComponent.getName();
    }

    public void setName(String name) {
        htmlComponent.setName(name);
    }
    
    public String getLabelId() {
        return htmlComponent.getLabelId();
    }

    public void setLabelId(String id) {
        htmlComponent.setLabelId(id);
    }


    public boolean getEnabled() {
        return htmlComponent.getEnabled();
    }

    public boolean isEnabled() {
        return htmlComponent.getEnabled();
    }

    public void setEnabled(boolean b) {
        htmlComponent.setEnabled(b);
    }

    public void setFocus(boolean b) {
        htmlComponent.setFocus(b);
    }

    public void setFocus() {
        htmlComponent.setFocus();
    }
    
    public void reset() {
        htmlComponent.reset();
    }

    public String getEditorHtml(OAObject obj) {
        return htmlComponent.getEditorHtml(obj);
    }
    
    public String getTableEditorHtml() {
        return htmlComponent.getTableEditorHtml();
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

