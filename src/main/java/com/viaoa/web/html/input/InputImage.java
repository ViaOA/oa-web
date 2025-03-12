package com.viaoa.web.html.input;

import java.util.*;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/*

<input type="image" src="submit-button.png" alt="Submit">

<input id="img" type="image" name="img" value="img" src="image/icon.gif">

*/

/**
 * Form submit button that uses an image.
 * <p>
 * Note: automatically submits on click.
 * sends name.x & name.y name values, and stores in the OAFormSubmitEvent, and can be accessed 
 * using the onSubmit* methods.
 */
public class InputImage extends InputElement {
    // Note: it submits value.x, value.y  if it's clicked 
    // OAForm will set the OAFormSubmitEvent.imageClickX/Y 
    
    public InputImage(String selector) {
        super(selector, InputType.Image);
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

    public void setWidth(int val) {
        htmlComponent.setImageWidth(val);
    }

    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("src");
        hsSupported.add("alt");
        hsSupported.add("imageheigth");
        hsSupported.add("imagewidth");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (OAStr.isNotEqual(type, Event_Click)) return;
        onClientClickEvent();
    }
    
    protected void onClientClickEvent() {
    }

}
