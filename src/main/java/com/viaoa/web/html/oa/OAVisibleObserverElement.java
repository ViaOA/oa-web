package com.viaoa.web.html.oa;

import java.util.Map;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.HtmlElement;

/**
 * Creates a js visible listener on the client.
 * Optionally loads an html template.
 * Sends event to server, with will then create the components.
 */
public class OAVisibleObserverElement extends HtmlElement {

    private String urlTemplate;
    private String componentName;
    private boolean bInit;
    
    public OAVisibleObserverElement(String selector) {
        super(selector);
    }    
    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (OAStr.isNotEqual(type, Event_Visible)) return;
        onVisibleEvent();
    }

    protected void onVisibleEvent() {
        // Note: this works with HtmlElment lazyLoad
    }
}
