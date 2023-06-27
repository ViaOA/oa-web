package com.viaoa.web.html.input;

import java.io.*;

import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/**
 * Component used to upload file(s).
 * 
 */
public class InputFile extends HtmlFormElement {

/*    
Attributes: 
accept, capture,  
  
<input type="file" name="file" id="file" accept="image/*" multiple />
<input type="file" accept="image/*;capture=camera" />
<input type="file" accept="video/*;capture=camcorder" />
<input type="file" accept="audio/*;capture=microphone" />    
 
*/    
    public InputFile(String id) {
        super(id, InputType.File);
    }

    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }

    public String[] getValues() {
        return oaHtmlComponent.getValues();
    }
    public void setValues(String[] values) {
        oaHtmlComponent.setValues(values);
    }


    public String getAccept() {
        return oaHtmlComponent.getAccept();
    }
    public void setAccept(String val) {
        oaHtmlComponent.setAccept(val);
    }

    public boolean getMultiple() {
        return oaHtmlComponent.getMultiple();
    }
    public void setMultiple(boolean b) {
        oaHtmlComponent.setMultiple(b);
    }

    @Override
    public OutputStream onSubmitGetFileOutputStream(String fname, long contentLength) {
        return super.onSubmitGetFileOutputStream(fname, contentLength);
    }
}
