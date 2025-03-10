package com.viaoa.web.html.input;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent.InputModeType;

import static com.viaoa.web.html.OAHtmlComponent.InputType;

import java.util.*;

/*

all <input> attributes, by type:
    https://developer.mozilla.org/en-US/docs/Web/API/HTMLInputElement

*/


/**
 * Base support for HTML Input Elements.
 */
public abstract class InputElement extends HtmlFormElement {

    public InputElement(String selector, InputType type) {
        super(selector, type == null ? null : type.getFormElementType());
        htmlComponent.setType(type);
    }
    
    public String getType() {
        return htmlComponent.getType();
    }

    public String getValue() {
        return htmlComponent.getValue();
    }
    public void setValue(String value) {
        htmlComponent.setValue(value);
    }

    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("type");
        hsSupported.add("value");
    }
    
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

    public String getVerifyScript() {
        //qqqqq not yet implemented
        return null;
    }
    public String getVerifyMessage() {
        return null;
    }
    
    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (OAStr.isNotEqual(type, Event_Change)) return;
        
        onClientChangeEvent(map.get("newValue"));
    }
    
    protected void onClientChangeEvent(String newValue) {
        getOAHtmlComponent().setValue(newValue);
        getOAHtmlComponent().setValueChanged(false);
    }
    
    
}
