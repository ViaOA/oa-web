package com.viaoa.web.html;

import java.util.*;

import com.viaoa.util.OAConv;
import com.viaoa.web.html.OAHtmlComponent.FormElementType;
import com.viaoa.web.html.oa.OATreeNodeData;

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

    public HtmlButton(String selector) {
        this(selector, null);
    }
    
    public HtmlButton(String selector, Type type) {
        super(selector, 
            (type == null || type == Type.Button) ? FormElementType.Button : (type == Type.Submit ? FormElementType.Submit : FormElementType.Reset)
        );
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

    public String getValue() {
        return htmlComponent.getValue();
    }
    public void setValue(String value) {
        htmlComponent.setValue(value);
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
    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (Event_Click.equals(type)) {
            onClickEvent(map);
        }
    }

    protected void onClickEvent(final Map<String, String> map) {
        onClickEvent();
    }
    protected void onClickEvent() {
        
    }
    
    
    
    
    
}
