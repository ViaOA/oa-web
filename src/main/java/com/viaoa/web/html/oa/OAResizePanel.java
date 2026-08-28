package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.web.html.*;
import com.viaoa.web.html.input.InputRadio;

public class OAResizePanel extends HtmlDiv {

    public OAResizePanel(String selector) {
        super(selector);
    }
	
    public OAResizePanel(String selector, HtmlElement he1, HtmlElement he2, int percent) {
        super(selector);
        
        if (he1 != null) add(he1);
        if (he2 != null) add(he2);
        //qqqqqq todo:
        
    }
    
    
    
}
