package com.viaoa.web.html;


public class HtmlOptionGroup extends HtmlOption {

    public HtmlOptionGroup(String value) {
        super(value);
    }
    public HtmlOptionGroup(String value, String label) {
        super(value, label);
    }

    /*qqqqqqqq remove, since selected is not an option
    public HtmlOptionGroup(String value, boolean bSelected) {
        super(value, bSelected);
    }
    
    public HtmlOptionGroup(String value, String label, boolean bSelected) {
        super(value, label, bSelected);
    }
    */
}
