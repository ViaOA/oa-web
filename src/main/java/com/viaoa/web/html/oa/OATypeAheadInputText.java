package com.viaoa.web.html.oa;


import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.*;
import com.viaoa.util.*;
import com.viaoa.web.html.*;
import com.viaoa.web.html.input.InputText;

/**
 *  InputText that is used for a type ahead search.
 *  
 *  @see OATypeAhead to config with hub and properties to use.
 */
public class OATypeAheadInputText extends InputText implements OAHtmlComponentInterface, OAHtmlTableComponentInterface {
    private final OAUITypeAheadController controlUI;

    // extra properties
    private int maxSize;
    private boolean bMaxSizeChanged;
    
    
    public OATypeAheadInputText(String elementIdentifier, OATypeAhead ta) {
        super(elementIdentifier);
        
        controlUI = new OAUITypeAheadController(ta) {
            @Override
            public void updateComponent(Object object) {
                String s = this.getValueAsString(object);
                OATypeAheadInputText.this.setValue(s);
                OATypeAheadInputText.this.setEnabled(this.isEnabled());
                OATypeAheadInputText.this.setVisible(this.isVisible());
            }
            
            @Override
            public void updateLabel(Object object) {
                OAHtmlComponent lbl = getOAHtmlComponent().getLabelComponent();
                if (lbl == null) return;
                lbl.setVisible(isVisible());

                boolean b = this.isEnabled();
                if (!b && getHub().getActiveObject() != null) b = true;
                lbl.setEnabled(b);
            }
        };
    }

    @Override
    public void close() {
        super.close();
        if (controlUI != null) controlUI.close();
    }
    
    
    public Hub getHub() {
        return controlUI.getHub();
    }
    public String getPropertyName() {
        return controlUI.getEndPropertyName();
    }
    public String getFormat() {
        return controlUI.getFormat();
    }
    public void setFormat(String format) {
        controlUI.setFormat(format);
    }
    
    public void setConversion(char conv) {
        controlUI.setConversion(conv);
    }
    public char getConversion() {
        return controlUI.getConversion();
    }

    /**
     * Allow size to grow to fit text, from original (attribute) size to this.maxSize.
     */
    public void setMaxSize(int x) {
        this.bMaxSizeChanged |= this.bMaxSizeChanged || (x != this.maxSize);
        this.maxSize = x;
    }
    public int getMaxSize() {
        return maxSize;
    }
    
    
    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
        boolean b = getOAHtmlComponent().getValueChanged();
        String js = null;
        if (bMaxSizeChanged) {
            bMaxSizeChanged = false;
            js = OAStr.concat(js, "comp.setMaxSize("+getMaxSize()+");", "\n");
        }
        else if (b) { // need to resize
            js = OAStr.concat(js, "comp.adjustSize();", "\n");
        }


        if (OAStr.isNotEmpty(jsonSearch)) {
            hsVars.add("objs");
            
            js = OAStr.concat(js, "objs = JSON.parse(`"+jsonSearch+"`);", "\n");
            js = OAStr.concat(js, "comp.update(objs);", "\n");
            
            jsonSearch = null;
        }
        
        bHasChanges |= OAStr.isNotEmpty(js);
        
        String s = getOAHtmlComponent().getJavaScriptForClient(hsVars, bHasChanges);
        js = OAStr.concat(s, js, "\n");
        
        return js;
    }

    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (OAStr.isEqual(type, Event_Search)) {
            String search = map.get("search");
            onClientSearchEvent(search);
        }
        else if (OAStr.isEqual(type, Event_Select)) {
            String objId = map.get("objId");
            Object obj = controlUI.findObjectUsingId(objId);
            onClientSelectEvent(obj);
        }
    }
    
    private String jsonSearch;
    protected void onClientSearchEvent(String search) {
        jsonSearch = controlUI.getJson(search);
    }
    protected void onClientSelectEvent(Object obj) {
        controlUI.getTypeAhead().getHub().setActiveObject(obj);
    }
    
}
