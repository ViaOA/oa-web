package com.viaoa.web.html;

import java.util.HashSet;
import java.util.Set;

/**
 * Control and Html TH element.
 * <p>
 * @author vince
 */
public class HtmlTH extends HtmlTD {

    public HtmlTH() {
        this(null);
    }
    public HtmlTH(String selector) {
        super(selector, "th");
    }

}
