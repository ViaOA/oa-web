package com.viaoa.web;

import javax.swing.Icon;

import com.viaoa.hub.Hub;
import com.viaoa.object.OAObject;
import com.viaoa.web.swing.JLabel;
import com.viaoa.web.swing.JTable;

public class OALabel extends OAWebComponent {

    public OALabel(String id, Hub hub, String propertyPath, int width) {
        super(id, hub, propertyPath, width);
    }
    public OALabel(String id, Hub hub, String propertyPath) {
        super(id, hub, propertyPath);
    }
    public OALabel(String id, Hub hub, String propertyPath, int width, int minWidth, int maxRows) {
        super(id, hub, propertyPath, width, minWidth, maxRows);
    }

    public OALabel(Hub hub, String cpp, int i) {
        // TODO Auto-generated constructor stub
    }
    @Override
    public void setMaxWidth(String val) {
        super.setMaxWidth(val);
        setOverflow("hidden");  // oahtmlelement will then add al.add("'text-overflow':'ellipsis'");
    }

    @Override
    public String getEditorHtml(OAObject obj) {
        return getRenderHtml(obj);  // no real editor
    }
    public void setIconColorProperty(String ppBackcolor) {
        // TODO Auto-generated method stub
        
    }
    public void setImageProperty(String ppIcon) {
        // TODO Auto-generated method stub
        
    }
    public void setMaxImageHeight(int i) {
        // TODO Auto-generated method stub
        
    }
    public void setMaxImageWidth(int i) {
        // TODO Auto-generated method stub
        
    }
    public void setIcon(Icon icon) {
        // TODO Auto-generated method stub
    }
    public void setText(String text) {
        // TODO Auto-generated method stub
        
    }
    public void setLabelFor(OAWebComponent component) {
        // TODO Auto-generated method stub
        
    }
    
}
