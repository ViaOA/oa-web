package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.uicontroller.OAUISelectController;
import com.viaoa.util.*;
import com.viaoa.web.html.*;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * HtmlSelect to work with OAModel.
 * <p>
 * 
 * Notes:<br>
 * setDisplay rows to change to/from dropdown (=1) or scrollinglist (> 1)<br>
 * 
 * @author vince
 */
public class OAComboBox extends OAHtmlSelect {

    public OAComboBox(String selector, Hub hub, String propName) {
        super(selector, hub, propName, null);
    }

    public OAComboBox(String selector, Hub hub, String propName, Hub hubSelect) {
        super(selector, hub, propName, hubSelect);
    }

    public OAComboBox(String selector, Hub hub, String propName, int cols) {
        super(selector, hub, propName, null);
        setMaxWidth(cols + "ch");
    }

    public OAComboBox(String selector, Hub hub, String propName, Hub hubSelect, int cols) {
        super(selector, hub, propName, hubSelect);
        setMaxWidth(cols + "ch");
    }

}




