package com.viaoa.web.html;

import java.util.HashSet;
import java.util.Set;

import com.viaoa.web.html.OAHtmlComponent.FormElementType;
import com.viaoa.web.html.input.InputElement;

/*
Button that is similar to InputButton.
<button class="favorite styled" type="button">Add to favorites</button>

*/
public class HtmlButton extends HtmlFormElement {
    public static enum Type {
        Button,
        Submit,
        Reset;
    }
    
    public HtmlButton(String id, Type type) {
        super(id, (type == null || type == Type.Button) ? FormElementType.Button : (type == Type.Submit ? FormElementType.Submit : FormElementType.Reset));
        OAHtmlComponent.InputType it;
        if (type == null) it = OAHtmlComponent.InputType.Button;
        else if (type == Type.Button) it = OAHtmlComponent.InputType.Button;
        else if (type == Type.Submit) it = OAHtmlComponent.InputType.Submit;
        else it = OAHtmlComponent.InputType.Reset;
        htmlComponent.setType(it);
    }

    public String getButtonText() {
        return htmlComponent.getInnerHtml();
    }
    public void setButtonText(String value) {
        htmlComponent.setInnerHtml(value);
    }
    
    public String getType() {
        return htmlComponent.getType();
    }
    
    
    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        hsSupported.add("type");
        hsSupported.add("value");
        hsSupported.add("buttontext");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
    
    
}
