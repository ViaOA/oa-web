package com.viaoa.web.html.input;

import java.util.HashSet;
import java.util.Set;

import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

/*

<input id="email" type="email" name="email" inputmode="email" value="jsmith@gmail.com">

*/

public class InputEmail extends InputText {

    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("multiple");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
    
    
    public InputEmail(String id) {
        super(id, InputType.Email);
        oaHtmlComponent.setInputMode(InputModeType.Email);
    }

    public boolean getMultiple() {
        return oaHtmlComponent.getMultiple();
    }
    public void setMultiple(boolean b) {
        oaHtmlComponent.setMultiple(b);
    }
    
}
