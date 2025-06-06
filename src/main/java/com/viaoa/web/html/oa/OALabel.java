package com.viaoa.web.html.oa;

import com.viaoa.hub.Hub;

public class OALabel extends OAHtmlElement {

    public OALabel(String selector, Hub hub, String propName) {
        super(selector, hub, propName);
    }

    public OALabel(String selector, Hub hub, String propName, int w) {
        super(selector, hub, propName);
        if (w > 0) setWidth(w + "ch");
    }
}
