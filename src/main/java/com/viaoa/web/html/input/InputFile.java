package com.viaoa.web.html.input;

import java.io.OutputStream;

import com.viaoa.util.OACompare;
import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputType;
import com.viaoa.web.html.form.OAFormMultipartInterface;

public class InputFile extends HtmlFormElement implements OAFormMultipartInterface {

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
        return oaHtmlComponent.getFileAccept();
    }
    public void setFileAccept(String val) {
        oaHtmlComponent.setFileAccept(val);
    }

    public boolean getMultiple() {
        return oaHtmlComponent.getMultiple();
    }
    public void setMultiple(boolean b) {
        oaHtmlComponent.setMultiple(b);
    }

    @Override
    public OutputStream getOutputStream(int length, String originalFileName) {
        // TODO Auto-generated method stub qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq
        return null;
    }

    
}
