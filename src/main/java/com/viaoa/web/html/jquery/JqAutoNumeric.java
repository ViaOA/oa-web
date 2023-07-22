package com.viaoa.web.html.jquery;

import java.util.Set;

import com.viaoa.util.OAConv;
import com.viaoa.util.OAStr;
import com.viaoa.util.OAString;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.input.InputText;
import com.viaoa.web.util.*;

public class JqAutoNumeric extends InputText {

    // AutoNumeric jquery plugin
    // see: http://www.decorplanit.com/plugin/
    // NOTE: use the older version that supports jquery.  The new version is huge and is not a jq plugin
    //    https://plugins.jquery.com/autoNumeric/
    private static class AutoNum {
        String min;
        String max;
        char symbol;
        boolean symbolPrefix;
        int decimalPlaces;
    }

    protected AutoNum autoNum;

    public JqAutoNumeric(String id) {
        super(id);
    }

    
    /**
     * Used to use the autoNumeric plugin
     *
     * @param min           value
     * @param max           value
     * @param symbol        example: $
     * @param symbolPrefix  true if currency symbol is used as the prefix, else it will be used as suffix
     * @param decimalPlaces number of decimals places for input and display
     */
    public void setNumeric(String min, String max, char symbol, boolean symbolPrefix, int decimalPlaces) {
        autoNum = new AutoNum();
        autoNum.min = min;
        if (max == null) {
            max = "9999999999999";
        }
        autoNum.max = max;
        autoNum.symbol = symbol;
        autoNum.symbolPrefix = symbolPrefix;
        autoNum.decimalPlaces = decimalPlaces;
        htmlComponent.setNeedsRefreshed(true);
    }

    public void setNumeric(char symbol, boolean symbolPrefix, int decimalPlaces) {
        autoNum = new AutoNum();
        autoNum.symbol = symbol;
        autoNum.symbolPrefix = symbolPrefix;
        autoNum.decimalPlaces = decimalPlaces;
        htmlComponent.setNeedsRefreshed(true);
    }
    
    

    public void setPositiveInteger() {
        this.autoNum = new AutoNum();
        setNumeric("0", null, (char) 0, true, 0);
    }
    

    @Override
    protected String getInitializeScript() {
        final StringBuilder sb = new StringBuilder();
        
        String s = "";
        if (autoNum.symbol > 0) {
            s = OAString.concat(s, "aSign: '" + autoNum.symbol + "'", ", ");
            if (!autoNum.symbolPrefix) {
                s = OAString.concat(s, "pSign: 's'", ", ");
            }
        }

        if (OAString.isNotEmpty(autoNum.min) || OAString.isNotEmpty(autoNum.max)) {
            if (OAString.isNotEmpty(autoNum.min)) {
                s = OAString.concat(s, "vMin: '" + autoNum.min + "'", ", ");
            }
            if (OAString.isNotEmpty(autoNum.max)) {
                s = OAString.concat(s, "vMax: '" + autoNum.max + "'", ", ");
            }
        }
        //if (autoNum.decimalPlaces > 0)  {
        s = OAString.concat(s, "mDec: '" + autoNum.decimalPlaces + "'", ", ");
        // s = OAString.concat(s, "aPad: 'true'", ", "); //default
        //}
        
        sb.append("$('#" + getId() + "').autoNumeric('init',{" + s + "});\n");
        return sb.toString(); 
    }
    
    @Override
    protected String getAjaxScript(boolean bIsInitializing) {
        final StringBuilder sb = new StringBuilder();

        String value = getValue();
        value = OAJspUtil.createJsString(value, '\'');

        if (autoNum.decimalPlaces > 0) {
            double d = OAConv.toDouble(value);
            value = OAConv.toString(d);
        } else {
            long x = OAConv.toLong(value);
            value = x + "";
        }

        sb.append("$('#" + getId() + "').autoNumeric('set', '" + value + "');\n");
        return sb.toString();
    }
    
    @Override
    public void getRequiredJsNames(Set<String> hsJsName) {
        hsJsName.add(OAFormInsertDelegate.JS_jquery_autonumeric);
    }
}
