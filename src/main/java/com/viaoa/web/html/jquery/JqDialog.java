package com.viaoa.web.html.jquery;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Set;

import com.viaoa.util.OAString;
import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * Turn an html element into a JQ Dialog.
 * 
 * @author vvia
 */
public class JqDialog extends HtmlElement {
    protected boolean bModal;
    protected Dimension dim, dimMin, dimMax;
    protected String title;
    protected String closeButtonText = "Close";
    private String submitButtonText;
    
    // list of button names. If selected, then the name/text will be set when onSubmit is called.
    private ArrayList<String> alButtons = new ArrayList<String>();  
    
    public JqDialog(String id) {
        super(id);
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
        
        
        sb.append("$('#"+getId()+"').dialog({autoOpen: false, modal: "+bModal);
        sb.append(", closeOnEscape: true, resizable: true");

        if (dim != null) {
            sb.append(", width: "+dim.width);
            sb.append(", height: "+dim.height);
        }
        if (dimMin != null) {
            sb.append(", minWidth: "+dimMin.width);
            sb.append(", minHeight: "+dimMin.height);
        }
        if (dimMax != null) {
            sb.append(", maxWidth: "+dimMax.width);
            sb.append(", maxHeight: "+dimMax.height);
        }

        String s = getCloseButtonText();
        if (!OAString.isEmpty(s)) {
            sb.append(", closeText: '"+s+"'");
        }

        
        if (alButtons.size() > 0) {
            sb.append(", buttons: [");
            int i=0;
            for (String text : alButtons) {
                if (i++ > 0) sb.append(", ");
                sb.append("{text: '"+text+"', click: function() { $(this).dialog('close'); ");
                
                if (getAjaxSubmit()) {
                    sb.append("$('#oacommand').val('"+getId()+"_"+text+"');ajaxSubmit();");
                }
                else if (getSubmit()) {
                    sb.append("$('#oacommand').val('"+getId()+"_"+text+"'); $('form').submit(); $('#oacommand').val('');");
                }
                sb.append("}}");
            }
            sb.append("]");
        }
        
        // end of constructor
        sb.append("});\n");

        
        String js = sb.toString();
        return js;
    }

    private String lastAjaxSent;
    
    @Override
    protected String getAjaxScript(final boolean bIsInitializing) {
        StringBuilder sb = new StringBuilder(256);
        
        if (getVisible()) {
            sb.append("$('#"+getId()+"').dialog('open');\n");
            lastAjaxSent = null;
        }
        else sb.append("$('#"+getId()+"').dialog('close');\n");

        String s = getTitle();
        if (s == null) s = "";
        sb.append("$('#"+getId()+"').dialog('option', 'title', '"+s+"');\n");

        String js = sb.toString();
        if (lastAjaxSent != null && lastAjaxSent.equals(js)) js = null;
        else lastAjaxSent = js;
        
        return js;
    }

    
    public void getRequiredJsNames(Set<String> hsJsName) {
        hsJsName.add(OAFormInsertDelegate.JS_jquery);
        hsJsName.add(OAFormInsertDelegate.JS_jquery_ui);

/*qqqqqqqqqq        
        if (OAString.isNotEmpty(getToolTip())) {
            hsJsName.add(OAJspDelegate.JS_bootstrap);
        }
        if (getForm() == null || getForm().getDefaultJsLibrary() == OAApplication.JSLibrary_JQueryUI) {
            hsJsName.add(OAJspDelegate.JS_jquery_ui);
        }
        else {
            hsJsName.add(OAJspDelegate.JS_bootstrap);
        }
*/        
    }

    @Override
    public void getRequiredCssNames(Set<String> hsCssName) {
        hsCssName.add(OAFormInsertDelegate.CSS_jquery_ui);
        
/*qqqqqqqq        
        if (OAString.isNotEmpty(getToolTip())) {
            hsCssName.add(OAJspDelegate.CSS_bootstrap);
        }
        if (getForm() == null || getForm().getDefaultJsLibrary() == OAApplication.JSLibrary_JQueryUI) {
            hsCssName.add(OAJspDelegate.CSS_jquery_ui);
        }
        else {
            hsCssName.add(OAJspDelegate.CSS_bootstrap);
        }
*/        
    }

}
