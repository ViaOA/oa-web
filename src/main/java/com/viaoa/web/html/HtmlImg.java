package com.viaoa.web.html;

import java.util.*;

import com.viaoa.util.OAStr;

public class HtmlImg extends HtmlElement {

    public HtmlImg(String selector) {
        super(selector);
    }

    public String getSource() {
        return htmlComponent.getSource();
    }
    public void setSource(String src) {
        htmlComponent.setSource(src);
    }

    public String getSrc() {
        return htmlComponent.getSrc();
    }
    public void setSrc(String src) {
        htmlComponent.setSrc(src);
    }
    
    public String getAlt() {
        return htmlComponent.getAlt();
    }
    public void setAlt(String alt) {
        htmlComponent.setAlt(alt);
    }
    
    public int getImageHeight() {
        return htmlComponent.getImageHeight();
    }

    public void setImageHeight(int val) {
        htmlComponent.setImageHeight(val);
    }

    public int getImageWidth() {
        return htmlComponent.getImageWidth();
    }

    /*remove ??
    public void setWidth(int val) {
        oaHtmlComponent.setImageWidth(val);
    }
    */

    private static Set<String> hsSupported = new HashSet();  // lowercase
    static {
        hsSupported.add("src");
        hsSupported.add("alt");
        hsSupported.add("imageHeight");
        hsSupported.add("imageWidth");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (OAStr.isNotEqual(type, Event_Click)) return;
        onClickEvent(map);
    }
    
    protected void onClickEvent(final Map<String, String> map) {
        
    }
    
    
}
