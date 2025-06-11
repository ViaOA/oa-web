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
public class OAList extends OAHtmlSelect {

    public OAList(String selector, Hub hub, String propName) {
        super(selector, hub, propName, null);
    }

    public OAList(String selector, Hub hub, String propName, Hub hubSelect) {
        super(selector, hub, propName, hubSelect);
   }
}




