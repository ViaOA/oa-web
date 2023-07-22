package com.viaoa.web.html.jquery;

import java.util.Set;

import com.viaoa.util.OAStr;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.input.InputText;

public class JqMaskedInput extends InputText {

    // see jquery maskedinput js lib
    public final static String MaskInput_USPhoneNumber = "(999) 999-9999";
    public final static String MaskInput_DateMMDDYYYY = "99/99/9999";
    public final static String MaskInput_DateMMDDYY = "99/99/99";
    public final static String MaskInput_TimeHMS = "99:99:99";
    public final static String MaskInput_TimeHM = "99:99";
    public final static String MaskInput_Integer = "9?999999";
    public final static String MaskInput_Decimal = "9?dddddd";
    public final static String MaskInput_SingleDigit = "9";
    public final static String MaskInput_DoubleDigit = "99";
    
    protected String inputMask;

    public JqMaskedInput(String id) {
        super(id);
    }

    @Override
    protected String getInitializeScript() {
        if (OAStr.isEmpty(inputMask)) return null;

        final StringBuilder sb = new StringBuilder();

        if (inputMask.indexOf('d') >= 0) {
            sb.append("$.mask.definitions['d'] = '[0-9.]';\n");
        }
        sb.append("$('#" + getId() + "').mask('" + inputMask + "');\n");

        return sb.toString();
    }

    /**
     * ex: '(999) 999-9999' last char optional: ("(99) 999-99-9?9") see:
     * http://digitalbush.com/projects/masked-input-plugin/
     *
     * @return
     */
    public String getInputMask() {
        return inputMask;
    }

    public void setInputMask(String inputMask) {
        if (OAStr.isNotEqual(this.inputMask, inputMask)) {
            htmlComponent.setNeedsRefreshed(true);
        }
        this.inputMask = inputMask;
    }

    @Override
    public void getRequiredJsNames(Set<String> hsJsName) {
        hsJsName.add(OAFormInsertDelegate.JS_jquery_maskedinput);
    }
    
}
