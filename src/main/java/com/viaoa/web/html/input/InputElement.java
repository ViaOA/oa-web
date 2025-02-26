package com.viaoa.web.html.input;

import com.viaoa.web.html.HtmlFormElement;
import static com.viaoa.web.html.OAHtmlComponent.InputType;

import java.util.HashSet;
import java.util.Set;

/*

all <input> attributes, by type:
    https://developer.mozilla.org/en-US/docs/Web/API/HTMLInputElement

*/


/**
 * Base support for HTML Input Elements.
 */
public class InputElement extends HtmlFormElement {

    public InputElement(String selector, InputType type) {
        super(selector, type == null ? null : type.getFormElementType());
        htmlComponent.setType(type);
    }
    
    public String getType() {
        return htmlComponent.getType();
    }

    
    
    
    
    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("type");
    }
    
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
}
