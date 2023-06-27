package com.viaoa.web.html;

import static com.viaoa.web.html.OAHtmlComponent.CursorType;
import static com.viaoa.web.html.OAHtmlComponent.EventType;
import static com.viaoa.web.html.OAHtmlComponent.OverflowType;

import java.io.OutputStream;
import java.util.List;

import com.viaoa.object.OAObject;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;

/**
 * Base class for all HTML Elements.
 * 
 */
public class HtmlElement {
    protected final OAHtmlComponent oaHtmlComponent;

    public HtmlElement(String id) {
        oaHtmlComponent = new OAHtmlComponent(id) {
            @Override
            public OutputStream onSubmitGetFileOutputStream(OAFormSubmitEvent formSubmitEvent, String fname, long contentLength) {
                OutputStream os = HtmlElement.this.onSubmitGetFileOutputStream(fname, contentLength);
                return os;
            }
            
            @Override
            public void onSubmitPrecheck(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitPrecheck(formSubmitEvent);
                if (formSubmitEvent.getSubmitOAHtmlComponent() == HtmlElement.this.oaHtmlComponent) {
                    formSubmitEvent.setSubmitHtmlElement(HtmlElement.this);;
                }
            }

            @Override
            public void onSubmitBeforeLoadValues(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitBeforeLoadValues(formSubmitEvent);
                HtmlElement.this.onSubmitBeforeLoadValues(formSubmitEvent);
            }
            
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitLoadValues(formSubmitEvent);
                HtmlElement.this.onSubmitLoadValues(formSubmitEvent);
            }
            
            @Override
            public void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitAfterLoadValues(formSubmitEvent);
                HtmlElement.this.onSubmitAfterLoadValues(formSubmitEvent);
            }
            
            
            @Override
            public void onSubmit(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmit(formSubmitEvent);
                HtmlElement.this.onSubmit(formSubmitEvent);
            }
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                super.onSubmitCompleted(formSubmitEvent);
                HtmlElement.this.onSubmitCompleted(formSubmitEvent);
            }
            
            @Override
            public void beforePageLoad() {
                super.beforePageLoad();
                HtmlElement.this.beforePageLoad();
            }
            
            @Override
            public void afterPageLoad() {
                super.afterPageLoad();
                HtmlElement.this.afterPageLoad();
            }
        };
        
        int xx = 4; //qqqqqqq
        xx++;
    }

    /**
     * Internal component used bo control any/all HTML elements, attributes, etc.
     */
    public OAHtmlComponent getOAHtmlComponent() {
        return oaHtmlComponent;
    }
    
    public OAForm getForm() {
        return oaHtmlComponent.getForm();
    }

    public void setForm(OAForm form) {
        oaHtmlComponent.setForm(form);
    }
    
    public String getId() {
        return oaHtmlComponent.getId();
    }

    public boolean getHidden() {
        return oaHtmlComponent.getHidden();
    }
    public boolean isHidden() {
        return oaHtmlComponent.getHidden();
    }
    public void setHidden(boolean b) {
        oaHtmlComponent.setHidden(b);
    }

    public boolean getVisible() {
        return oaHtmlComponent.getVisible();
    }
    public boolean isVisible() {
        return oaHtmlComponent.getVisible();
    }
    public void setVisible(boolean b) {
        oaHtmlComponent.setVisible(b);
    }

    public String getForwardUrl() {
        return oaHtmlComponent.getForwardUrl();
    }

    public void setForwardUrl(String forwardUrl) {
        oaHtmlComponent.setForwardUrl(forwardUrl);
    }

    public boolean getSubmit() {
        return oaHtmlComponent.getSubmit();
    }

    public void setSubmit(boolean b) {
        oaHtmlComponent.setSubmit(b);
    }

    public boolean getAjaxSubmit() {
        return oaHtmlComponent.getAjaxSubmit();
    }

    public void setAjaxSubmit(boolean b) {
        oaHtmlComponent.setAjaxSubmit(b);
    }

    public String getToolTip() {
        return oaHtmlComponent.getToolTipText();
    }

    public void setToolTip(String tooltip) {
        oaHtmlComponent.setToolTipText(tooltip);
    }

    public String getToolTipText() {
        return oaHtmlComponent.getToolTipText();
    }

    public void setToolTipText(String tooltip) {
        oaHtmlComponent.setToolTipText(tooltip);
    }

    public String getToolTipTemplate() {
        return oaHtmlComponent.getToolTipTemplate();
    }

    public void setToolTipTemplate(String toolTipTemplate) {
        oaHtmlComponent.setToolTipTemplate(toolTipTemplate);
    }

    public String getCalcToolTip() {
        return oaHtmlComponent.getCalcToolTip();
    }

    public boolean getPlainText() {
        return oaHtmlComponent.getPlainText();
    }

    public boolean isPlainText() {
        return oaHtmlComponent.getPlainText();
    }

    public void setPlainText(boolean b) {
        oaHtmlComponent.setPlainText(b);
    }

    public String getStyle(String name) {
        return oaHtmlComponent.getStyle(name);
    }

    public List<String> getStyles() {
        return oaHtmlComponent.getStyles();
    }
    
    public void addStyle(String name, String value) {
        oaHtmlComponent.addStyle(name, value);
    }

    public void removeStyle(String name) {
        oaHtmlComponent.removeStyle(name);
    }

    public List<String> getClasses() {
        return oaHtmlComponent.getClasses();
    }
    public void addClass(String name) {
        oaHtmlComponent.addClass(name);
    }

    public void removeClass(String name) {
        oaHtmlComponent.removeClass(name);
    }

    public String getConfirmMessage() {
        return oaHtmlComponent.getConfirmMessage();
    }

    public void setConfirmMessage(String msg) {
        oaHtmlComponent.setConfirmMessage(msg);
    }

    public String getConfirmMessageTemplate() {
        return oaHtmlComponent.getConfirmMessageTemplate();
    }

    public void setConfirmMessageTemplate(String msg) {
        oaHtmlComponent.setConfirmMessageTemplate(msg);
    }
    
    public String getCalcConfirmMessage() {
        return oaHtmlComponent.getCalcConfirmMessage();
    }
    
    public String getHeight() {
        return oaHtmlComponent.getHeight();
    }

    public void setHeight(String val) {
        oaHtmlComponent.setHeight(val);
    }

    public String getWidth() {
        return oaHtmlComponent.getWidth();
    }

    public void setWidth(String val) {
        oaHtmlComponent.setWidth(val);
    }
    
    public String getMinHeight() {
        return oaHtmlComponent.getMinHeight();
    }

    public void setMinHeight(String val) {
        oaHtmlComponent.setMinHeight(val);
    }

    public String getMinWidth() {
        return oaHtmlComponent.getMinWidth();
    }

    public void setMinWidth(String val) {
        oaHtmlComponent.setMinWidth(val);
    }

    public String getMaxHeight() {
        return oaHtmlComponent.getMaxHeight();
    }

    public void setMaxHeight(String val) {
        oaHtmlComponent.setMaxHeight(val);
    }

    public String getMaxWidth() {
        return oaHtmlComponent.getMaxWidth();
    }

    public void setMaxWidth(String val) {
        oaHtmlComponent.setMaxWidth(val);
    }

    public String getOverflow() {
        return oaHtmlComponent.getOverflow();
    }

    public void setOverflow(String overflow) {
        oaHtmlComponent.setOverflow(overflow);
    }

    public void setOverflow(OverflowType overflowType) {
        oaHtmlComponent.setOverflow(overflowType);
    }

    public String getEventName() {
        return oaHtmlComponent.getEventName();
    }

    public void setEventName(String name) {
        oaHtmlComponent.setEventName(name);
    }

    public void setEventType(EventType eventType) {
        oaHtmlComponent.setEventType(eventType);
    }

    public String getCursor() {
        return oaHtmlComponent.getCursor();
    }

    public void setCursor(String cursorName) {
        oaHtmlComponent.setCursor(cursorName);
    }

    public void setCursor(CursorType cursorType) {
        setCursor(cursorType == null ? CursorType.Default.getDisplay() : cursorType.getDisplay());
    }

    public String getInnerHtml() {
        return oaHtmlComponent.getInnerHtml();
    }

    public void setInnerHtml(String html) {
        oaHtmlComponent.setInnerHtml(html);
    }
    
    
    public String getScript() {
        return oaHtmlComponent.getScript();
    }
    public String getVerifyScript() {
        return oaHtmlComponent.getVerifyScript();
    }

    public String getAjaxScript() {
        return oaHtmlComponent.getAjaxScript();
    }
    public String getAjaxScript(final boolean bIsInitializing) {
        return oaHtmlComponent.getAjaxScript(bIsInitializing);
    }


    public boolean isChanged() {
        return oaHtmlComponent.isChanged();
    }

    public String getRenderHtml(OAObject obj) {
        return oaHtmlComponent.getEditorHtml(obj);
    }

    public boolean getNeedsReloaded() {
        return oaHtmlComponent.getNeedsReloaded();
    }

    // called by OAForm
    public void beforePageLoad() {
    }
    
    // called by OAForm
    public void afterPageLoad() {
    }
    
    // chance to cancel event
    public void onSubmitBeforeLoadValues(OAFormSubmitEvent formSubmitEvent) {
    }
    public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
    }
    public void onSubmitAfterLoadValues(OAFormSubmitEvent formSubmitEvent) {
    }
    
    /**
     * Only called for the component that submitted, event must not be cancelled.
     */
    public void onSubmit(OAFormSubmitEvent formSubmitEvent) {
    }
    
    public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
    }

    
    public OutputStream onSubmitGetFileOutputStream(String fname, long contentLength) {
        return null;
    }
}
