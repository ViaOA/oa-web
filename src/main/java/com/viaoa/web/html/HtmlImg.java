package com.viaoa.web.html;

public class HtmlImg extends HtmlFormElement {

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

    public void setWidth(int val) {
        oaHtmlComponent.setImageWidth(val);
    }

}
