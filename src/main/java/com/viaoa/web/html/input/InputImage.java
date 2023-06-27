package com.viaoa.web.html.input;

import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/**
 * Form submit button that uses an image.
 *
 */
public class InputImage extends HtmlFormElement {

    // Note: it submits value.x, value.y  if it's clicked 
    // OAForm will set the OAFormSubmitEvent.imageClickX/Y 
    
    
    public InputImage(String id) {
        super(id, InputType.Image);
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
