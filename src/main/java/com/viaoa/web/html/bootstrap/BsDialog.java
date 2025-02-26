package com.viaoa.web.html.bootstrap;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Set;

import com.viaoa.util.OAString;
import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * Turn an html element into a BS Dialog.
 * 
 * @author vvia
 */
public class BsDialog extends HtmlElement {
    protected boolean bModal;
    protected Dimension dim, dimMin, dimMax;
    protected String title;
    protected String closeButtonText = "Close";
    private String submitButtonText;
    
    // list of button names. If selected, then the name/text will be set when onSubmit is called.
    private ArrayList<String> alButtons = new ArrayList<String>();  
    
    public BsDialog(String selector) {
        super(selector);
        setVisible(false);
    }
    public void setModal(boolean b) {
        bModal = b;
    }
    public boolean getModal() {
        return bModal;
    }

    
    @Override
    protected void onSubmitBeforeLoadValues(OAFormSubmitEvent formSubmitEvent) {
        boolean bWasSubmitted = false;
        String[] ids = formSubmitEvent.getNameValueMap().get("oacommand");
        if (ids != null && ids.length == 1) {
            String idx = ids[0];
            
            bWasSubmitted  = (idx != null && idx.startsWith(getId()+"_"));
            if (bWasSubmitted) {
                formSubmitEvent.setSubmitOAHtmlComponent(getOAHtmlComponent());
                formSubmitEvent.setSubmitHtmlElement(this);
                submitButtonText = idx.substring(getId().length()+1);
            }
        }
    }

    @Override
    protected void onSubmit(OAFormSubmitEvent formSubmitEvent) {
        if (formSubmitEvent.getSubmitHtmlElement() != this) return;
        setVisible(false);  // js code in this changed it
        getOAHtmlComponent().setVisibleChanged(false);
        onSubmit(submitButtonText);
    }

    /**
     * Override this for when button is clicked.
     */
    protected void onSubmit(String buttonName) {
    }
    
    
    public Dimension getDimension() {
        return dim;
    }
    public void setDimension(Dimension d) {
        this.dim = d;
    }
    public Dimension getMinDimension() {
        return dimMin;
    }
    public void setMinDimension(Dimension d) {
        this.dimMax = d;
    }
    public Dimension getMaxDimension() {
        return dimMax;
    }
    public void setMaxDimension(Dimension d) {
        this.dimMax = d;
    }
    
    public void setCloseButtonText(String text) {
        this.closeButtonText = text;
    }
    public String getCloseButtonText() {
        return this.closeButtonText;
    }
    
    public void addButton(String text) {
        alButtons.add(text);
    }

    @Override
    protected String getInitializeScript() {
        StringBuilder sb = new StringBuilder(2048);
        lastAjaxSent = null;
        
        
        sb.append("(function() {\n");
        sb.append("    var xx = $('#"+getId()+"').wrap(\"<div class='modal-body'></div>\").parent();\n");
        sb.append("    xx.wrap(\"<div class='modal-content'></div>\");\n");
        sb.append("    xx.before(\"<div class='modal-header'><button type='button' class='close' data-dismiss='modal'><span>&times;</span></button><h4 class='modal-title'>"+OAString.getNonNull(getTitle())+"</h4></div>\");\n");
        
        sb.append("    xx.after(\"<div class='modal-footer'>");
        
        
        for (String text : alButtons) {
            if (text == null) text = "";
            String idx = getId()+"_"+text;
            sb.append("<button id='"+idx+"' type='button' class='btn btn-default' data-dismiss='modal'>");
            sb.append(text+"</button>");
        }

        if (OAString.isNotEmpty(getCloseButtonText())) sb.append("<button type='button' class='btn btn-default' data-dismiss='modal'>"+getCloseButtonText()+"</button>");
        sb.append("</div>\");\n");
        
        sb.append("    xx = xx.parent();\n");
        sb.append("    xx = xx.wrap(\"<div class='modal-dialog'></div>\").parent();\n");
        sb.append("    xx.wrap(\"<div id='oaDialog"+getId()+"' class='modal fade' tabindex='-1'></div>\");\n");     
        sb.append(" })();\n");
        
        for (String text : alButtons) {
            if (text == null) text = "";
            String idx = getId()+"_"+text;
            
            if (getAjaxSubmit()) {
                sb.append("$('#"+idx+"').click(function() {$('#oacommand').val('"+getId()+"_"+text+"');ajaxSubmit();return false;});\n");
            }
            else if (getSubmit()) {
                sb.append("$('#"+idx+"').click(function() { $('#oacommand').val('"+getId()+"_"+text+"'); $('form').submit(); $('#oacommand').val(''); return false;});\n");
            }
        }        
        
        String js = sb.toString();
        return js;
    }

    private String lastAjaxSent;
    
    @Override
    protected String getAjaxScript(final boolean bIsInitializing) {
        StringBuilder sb = new StringBuilder(256);
        
        if (getVisible()) {
            sb.append("$('#oaDialog"+getId()+"').modal({keyboard: true});");
            lastAjaxSent = null;
        }
        else {
            sb.append("$('#oaDialog"+getId()+"').modal('hide');");
        }

        String js = sb.toString();
        if (lastAjaxSent != null && lastAjaxSent.equals(js)) js = null;
        else lastAjaxSent = js;
        
        return js;
    }
    
    public void getRequiredJsNames(Set<String> hsJsName) {
        hsJsName.add(OAFormInsertDelegate.JS_bootstrap);
        hsJsName.add(OAFormInsertDelegate.JS_bootstrap);
    }

    @Override
    public void getRequiredCssNames(Set<String> hsCssName) {
        hsCssName.add(OAFormInsertDelegate.CSS_bootstrap);
    }
}
