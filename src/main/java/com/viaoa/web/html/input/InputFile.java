package com.viaoa.web.html.input;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import com.viaoa.util.OAStr;
import com.viaoa.util.OAString;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/*    
  
<input type="file" name="file" id="file" accept="image/*" multiple />
<input type="file" name="image" accept="image/*" capture="user">
 
*/    


/**
 * Component used to upload file(s).
 * <p> 
 * Notes: 
 * An Ajax submit will also send file(s), and will clear the file(s) from the page.
 * <br>
 * Important: implement onSubmitGetFileOutputStream(fname, length) to be able to receive the file/data.
 * 
 */
public class InputFile extends InputElement {

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

    public String getCapture() {
        return oaHtmlComponent.getCapture();
    }
    public void setCapture(String val) {
        oaHtmlComponent.setCapture(val);
    }
    
    public boolean getMultiple() {
        return oaHtmlComponent.getMultiple();
    }
    public void setMultiple(boolean b) {
        oaHtmlComponent.setMultiple(b);
    }

    public int getMaxFileSize() {
        return oaHtmlComponent.getMaxFileSize();
    }
    public void setMaxFileSize(int val) {
        oaHtmlComponent.setMaxFileSize(val);
    }

    public boolean getRequired() {
        return oaHtmlComponent.getRequired();
    }

    public boolean isRequired() {
        return oaHtmlComponent.getRequired();
    }

    public void setRequired(boolean req) {
        oaHtmlComponent.setRequired(req);
    }
    
    
    @Override
    public OutputStream onSubmitGetFileOutputStream(String fname, long contentLength) {
        return super.onSubmitGetFileOutputStream(fname, contentLength);
    }

    
    @Override
    protected String getVerifyScript(final String js) {
        StringBuilder sb = null;
        
        // add js code to check for max size
        if (getMaxFileSize() > 0) {
            if (sb == null) {
                sb = new StringBuilder();
                if (OAStr.isNotEmpty(js)) sb.append(js);
            }
            
            sb.append("val = $('#"+getId()+"')[0];\n");
            sb.append("if (val.files) {\n");
            sb.append("    for (let i=0; i < val.files.length; i++) {\n");
            sb.append("        if (val.files[i].size > "+getMaxFileSize()+") {\n");
            sb.append("            errors.push('File size over limit of "+getMaxFileSize()+", for file ' + val.files[i].name);\n");
            sb.append("        }\n");
            sb.append("    }\n");
            sb.append("}\n");

  
            /*was
            sb.append("if ($('#"+getId()+"')[0].files && $('#"+getId()+"')[0].files.length > 0) {\n");
            sb.append("    if ($('#"+getId()+"')[0].files[0].size > "+getMaxFileSize()+") {\n");
            sb.append("        errors.push('File size over limit of "+getMaxFileSize()+"');\n");
            sb.append("    }\n");
            sb.append("}\n");
             */            
        }

        if (isRequired()) {
            if (sb == null) {
                sb = new StringBuilder();
                if (OAStr.isNotEmpty(js)) sb.append(js);
            }
            
            String s = getTitle();
            if (OAString.isEmpty(s)) {
                s = getName();
                if (OAString.isEmpty(s)) {
                    s = getId();
                    if (OAString.isEmpty(s)) s = getId();
                    if (OAString.isEmpty(s)) {
                        s = "";
                    }
                }
            }
            s = "File " + s + " is required";
            
            sb.append("if ($('#"+getId()+"')[0].files === undefined || $('#"+getId()+"')[0].files.length == 0) {");
            sb.append("  requires.push('" + s + "');");
            sb.append("  $('#" + getId() + "').addClass('oaError');");
            sb.append("}\n");
            
            
        }

        if (sb == null) return js;
        return sb.toString();
    }


    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("value");
        hsSupported.add("accept");
        hsSupported.add("capture");
        hsSupported.add("multiple");
        hsSupported.add("maxfilesize");
        hsSupported.add("required");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }

}
