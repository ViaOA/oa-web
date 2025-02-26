package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.util.*;
import com.viaoa.web.html.HtmlElement;

/* html

qqqqqqqqqqqqqqqqqqqqq

*/

/**
 * 
 */
public class OATabPanel extends OAPanel {

    private int activeTab = 0;
    private int lastActiveTab = 0;
    private int lastTabCount = 0;
    
    public OATabPanel(String elementIdentifier) {
        super(elementIdentifier);
    }
    
    
    public void setActiveTab(String name) {
        int pos = 0;
        if (name != null) {
            for (HtmlElement he : getChildren()) {
                if (name.equalsIgnoreCase(he.getHtmlSelector())) break;
            }
        }        
        setActiveTab(pos);
    }
    
    public int getActiveTab() {
        return this.activeTab;
    }
    public void setActiveTab(int pos) {
        this.activeTab = pos;
    }
    
    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        String js = null;
        if (lastTabCount != getChildren().size()) {
            for (int i=lastTabCount; i<getChildren().size(); i++) {
                js = OAStr.concat(js, "comp.addTab('"+getChildren().get(i).getDataOAName()+"');", "\n");
            }
            lastTabCount = getChildren().size();
        }
        
        if (this.activeTab != lastActiveTab) {
            js = OAStr.concat(js, "comp.setActiveTab("+activeTab+", false);", "\n");
            lastActiveTab = this.activeTab;
        }
        
        bHasChanges |= OAStr.isNotEmpty(js);
        
        String s = super.getJavaScriptForClient(hsVars, bHasChanges);
        js = OAStr.concat(s, js, "\n");
        
        return js;
    }
    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (OAStr.isNotEqual(type, Event_Change)) return;
        onChangeTabEvent(OAConv.toInt(map.get("newValue")));
    }
    
    protected void onChangeTabEvent(int pos) {
        this.activeTab = pos;
    }    
}
