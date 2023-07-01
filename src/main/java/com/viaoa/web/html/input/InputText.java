package com.viaoa.web.html.input;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.viaoa.jsp.OAJspUtil;
import com.viaoa.util.OAStr;
import com.viaoa.util.OAString;
import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputText extends InputElement {
    
    public InputText(String id) {
        super(id, InputType.Text);
    }

    // used by subclasses to set the input type, ex: "tel"
    protected InputText(String id, InputType type) {
        super(id, type);
    }
    
    
    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }

    public String getPlaceHolder() {
        return oaHtmlComponent.getPlaceHolder();
    }
    public void setPlaceHolder(String placeHolder) {
        oaHtmlComponent.setPlaceHolder(placeHolder);
    }

    public String getPattern() {
        return oaHtmlComponent.getPattern();
    }

    public void setPattern(String pattern) {
        oaHtmlComponent.setPattern(pattern);
    }
    
    public boolean getReadOnly() {
        return oaHtmlComponent.getReadOnly();
    }

    public boolean isReadOnly() {
        return oaHtmlComponent.getReadOnly();
    }
    public void setReadOnly(boolean b) {
        oaHtmlComponent.setReadOnly(b);
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
    

    // Id of datalist element
    public String getList() {
        return oaHtmlComponent.getList();
    }

    public void setList(String listId) {
        oaHtmlComponent.setList(listId);
    }
    
    public List<String> getDataList() {
        return oaHtmlComponent.getDataList();
    }

    public void setDataList(List<String> lst) {
        oaHtmlComponent.setDataList(lst);
    }
    

    
    /**
     * The display width of the text field, number of characters wide.
     */
    public int getSize() {
        return oaHtmlComponent.getSize();
    }

    public void setSize(int val) {
        oaHtmlComponent.setSize(val);
    }
    
    public int getMinLength() {
        return oaHtmlComponent.getMinLength();
    }

    public void setMinLength(int val) {
        oaHtmlComponent.setMinLength(val);
    }

    public int getMaxLength() {
        return oaHtmlComponent.getMaxLength();
    }

    public void setMaxLength(int val) {
        oaHtmlComponent.setMaxLength(val);
    }

    public String getAutoComplete() {
        return oaHtmlComponent.getAutoComplete();
    }
    public void setAutoComplete(String val) {
        oaHtmlComponent.setAutoComplete(val);
    }
    
    public String getInputMode() {
        return oaHtmlComponent.getInputMode();
    }

    public void setInputMode(String mode) {
        oaHtmlComponent.setInputMode(mode);
    }

    public void setInputMode(InputModeType type) {
        oaHtmlComponent.setInputMode(type);
    }


    public boolean getSpellCheck() {
        return oaHtmlComponent.getSpellCheck();
    }
    public boolean isSpellCheck() {
        return getSpellCheck();
    }
    public void setSpellCheck(boolean b) {
        oaHtmlComponent.setSpellCheck(b);
    }
    
    public String getFloatLabel() {
        return oaHtmlComponent.getFloatLabel();
    }

    public void setFloatLabel(String floatLabel) {
        oaHtmlComponent.setFloatLabel(floatLabel);
    }

    public String getCalcDisplayName() {
        String s = getName();
        if (OAString.isEmpty(s)) {
            s = getPlaceHolder();
            if (OAString.isEmpty(s)) {
                s = getId();
            }
        }
        return s;
    }
    
    
    @Override
    protected String getVerifyScript(final String js) {
        StringBuilder sb = null;
        if (isRequired()) {
            if (sb == null) {
                sb = new StringBuilder();
                if (OAStr.isNotEmpty(js)) sb.append(js);
            }

            String s = "Field " + getCalcDisplayName() + " is required";
            sb.append("if ($('#" + getId() + "').val() == '') {");
            sb.append("   requires.push('" + s + "');");
            sb.append("   $('#" + getId() + "').addClass('oaError');");
            sb.append("}\n");
        }

        int max = getMaxLength();
        if (max > 0) {
            if (sb == null) {
                sb = new StringBuilder();
                if (OAStr.isNotEmpty(js)) sb.append(js);
            }
            sb.append("if ($('#" + getId() + "').val().length > " + max + ") {");
            sb.append("    errors.push('Field "+getCalcDisplayName()+" can not be longer than " + max + " characters');");
            sb.append("    $('#" + getId() + "').addClass('oaError');");
            sb.append("}\n");
        }

        int min = getMinLength();
        if (min > 0) {
            if (sb == null) {
                sb = new StringBuilder();
                if (OAStr.isNotEmpty(js)) sb.append(js);
            }
            sb.append("if ($('#" + getId() + "').val().length < " + min + ") {");
            sb.append("    errors.push('Field "+getCalcDisplayName()+" must be at least " + min + " characters');");
            sb.append("    $('#" + getId() + "').addClass('oaError');");
            sb.append("}\n");
        }
        

        if (OAStr.isNotEmpty(getPattern())) {
            if (sb == null) {
                sb = new StringBuilder();
                if (OAStr.isNotEmpty(js)) sb.append(js);
            }

            sb.append("regex = new RegExp(/" + OAJspUtil.createEmbeddedJsString(getPattern(),'/') + "/);");
            sb.append("val = $('#" + getId() + "').val();");
            sb.append("if (!val.match(regex)) {");
            sb.append("    errors.push('Field "+ getCalcDisplayName() + " must match regex pattern "+ OAJspUtil.createEmbeddedJsString(getPattern(), '\'') +"');");
            sb.append("    $('#" + getId() + "').addClass('oaError');");
            sb.append("}\n");
        }
        
        if (sb == null) return js;
        return sb.toString();
    }


    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("value");
        hsSupported.add("placeholder");
        hsSupported.add("pattern");
        hsSupported.add("readonly");
        hsSupported.add("required");
        hsSupported.add("list");
        hsSupported.add("datalist");
        hsSupported.add("size");
        hsSupported.add("minlength");
        hsSupported.add("maxlength");
        hsSupported.add("autocomplete");
        hsSupported.add("inputmode");
        hsSupported.add("spellcheck");
        hsSupported.add("floatlabel");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
}
