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
        return htmlComponent.getValue();
    }
    public void setValue(String value) {
        htmlComponent.setValue(value);
    }

    public String[] getValues() {
        return htmlComponent.getValues();
    }
    public void setValues(String[] values) {
        htmlComponent.setValues(values);
    }


    public String getAccept() {
        return htmlComponent.getAccept();
    }
    public void setAccept(String val) {
        htmlComponent.setAccept(val);
    }

    public String getCapture() {
        return htmlComponent.getCapture();
    }
    public void setCapture(String val) {
        htmlComponent.setCapture(val);
    }
    
    public boolean getMultiple() {
        return htmlComponent.getMultiple();
    }
    public void setMultiple(boolean b) {
        htmlComponent.setMultiple(b);
    }

    public int getMaxFileSize() {
        return htmlComponent.getMaxFileSize();
    }
    public void setMaxFileSize(int val) {
        htmlComponent.setMaxFileSize(val);
    }

    public boolean getRequired() {
        return htmlComponent.getRequired();
    }

    public boolean isRequired() {
        return htmlComponent.getRequired();
    }

    public void setRequired(boolean req) {
        htmlComponent.setRequired(req);
    }
    
    
    @Override
    public OutputStream onSubmitGetFileOutputStream(String fileName, String contentType) {
        return super.onSubmitGetFileOutputStream(fileName, contentType);
    }

    
    @Override
    protected String getVerifyScript() {
        StringBuilder sb = null;
        
        // add js code to check for max size
        if (getMaxFileSize() > 0) {
            if (sb == null) {
                sb = new StringBuilder();
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

        if (sb == null) return null;
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
