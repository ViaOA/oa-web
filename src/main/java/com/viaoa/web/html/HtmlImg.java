package com.viaoa.web.html;

import java.util.HashSet;
import java.util.Set;

public class HtmlImg extends HtmlElement {

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
    
    public HtmlImg(String id) {
        super(id);
    }

    public String getSource() {
        return oaHtmlComponent.getSource();
    }
    public void setSource(String src) {
        oaHtmlComponent.setSource(src);
    }

    public String getSrc() {
        return oaHtmlComponent.getSrc();
    }
    public void setSrc(String src) {
        oaHtmlComponent.setSrc(src);
    }
    
    public String getAlt() {
        return oaHtmlComponent.getAlt();
    }
    public void setAlt(String alt) {
        oaHtmlComponent.setAlt(alt);
    }
    
    public int getImageHeight() {
        return oaHtmlComponent.getImageHeight();
    }

    public void setImageHeight(int val) {
        oaHtmlComponent.setImageHeight(val);
    }

    public int getImageWidth() {
        return oaHtmlComponent.getImageWidth();
    }

    /*remove ??
    public void setWidth(int val) {
        oaHtmlComponent.setImageWidth(val);
    }
    */

}
