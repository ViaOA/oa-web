package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.*;
import com.viaoa.web.html.input.InputRadio;

public class OAAccordionBar extends HtmlElement {
    
    public OAAccordionBar(String selector) {
        super(selector);
    }
    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (OAStr.isEqual(type, Event_Show)) onShowEvent();
        else if (OAStr.isEqual(type, Event_Hide)) onHideEvent();
    }

    protected void onShowEvent() {
        
    }
    protected void onHideEvent() {
        
    }
    
    
}
