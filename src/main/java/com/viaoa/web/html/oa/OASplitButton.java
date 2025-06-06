package com.viaoa.web.html.oa;

import com.viaoa.hub.Hub;
import com.viaoa.uicontroller.OAUICommandController;

public class OASplitButton extends OAHtmlButton {

    public OASplitButton(String selector, Hub hub) {
        super(selector, hub, OAUICommandController.Command.OtherUsesHub);
    }
    
    public void addButton(OAHtmlButton cmd) {
        //qqqqqqq todo
    }
    
}
