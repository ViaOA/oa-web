package com.viaoa.web.html.input;

import com.viaoa.web.html.OAHtmlComponent.InputModeType;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputSearch extends InputText {
    
    public InputSearch(String id) {
        super(id, InputType.Search);
        oaHtmlComponent.setInputMode(InputModeType.Search);
    }

}
