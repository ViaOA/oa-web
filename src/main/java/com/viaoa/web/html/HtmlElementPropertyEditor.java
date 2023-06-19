package com.viaoa.web.html;

import com.viaoa.util.OAConv;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.*;

public class HtmlElementPropertyEditor {
    private final OAForm form;
    private final OAHtmlComponent oaHtmlComponent;
    
    public HtmlElementPropertyEditor(final OAForm form, final HtmlElement he) {
        this.form = form;
        this.oaHtmlComponent = he.getOAHtmlComponent();
        setup();
    }
    
    protected void setup() {
        InputCheckBox chk;
        InputText txt;

        InputButton cmdAjaxSubmit = new InputButton("cmdDoAjaxSubmit");
        cmdAjaxSubmit.setValue("ajaxSubmit");
        cmdAjaxSubmit.setAjaxSubmit(true);
        form.add(cmdAjaxSubmit);

        txt = new InputText("txtId") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setId(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getId());
        form.add(txt);
        
        txt = new InputText("txtValue") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setValue(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getValue());
        form.add(txt);
        
        txt = new InputText("txtLabelId") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setLabelId(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getLabelId());
        form.add(txt);

        txt = new InputText("txtFloatLabel") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setFloatLabel(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getFloatLabel());
        form.add(txt);

        txt = new InputText("txtPlaceHolder") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setPlaceHolder(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getPlaceHolder());
        form.add(txt);
        
        txt = new InputText("txtToolTipText") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setToolTipText(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getToolTipText());
        form.add(txt);
        
        txt = new InputText("txtAutoComplete") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setAutoComplete(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getAutoComplete());
        form.add(txt);
                
        
        
        chk = new InputCheckBox("chkFormDebug") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.form.setDebug(getChecked());
            }
        };
        chk.setChecked(form.getDebug());
        chk.setAjaxSubmit(true);
        form.add(chk);

        chk = new InputCheckBox("chkEnabled") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setEnabled(getChecked());
            }
        };
        chk.setChecked(oaHtmlComponent.getEnabled());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkVisible") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setVisible(getChecked());
            }
        };
        chk.setChecked(oaHtmlComponent.getVisible());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkHidden") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setHidden(getChecked());
            }
        };
        chk.setChecked(oaHtmlComponent.getHidden());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkAjaxSubmit") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setAjaxSubmit(getChecked());
            }
        };
        chk.setChecked(oaHtmlComponent.getAjaxSubmit());
        chk.setAjaxSubmit(true);
        form.add(chk);

        chk = new InputCheckBox("chkSubmit") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setSubmit(getChecked());
            }
        };
        chk.setChecked(oaHtmlComponent.getSubmit());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkSpellCheck") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setSpellCheck(getChecked());
            }
        };
        chk.setChecked(oaHtmlComponent.getSpellCheck());
        chk.setAjaxSubmit(true);
        form.add(chk);

        
        txt = new InputText("txtHeight") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setHeight(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getHeight());
        form.add(txt);
        
        txt = new InputText("txtWidth") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setWidth(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getWidth());
        form.add(txt);

        txt = new InputText("txtMinHeight") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setMinHeight(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getMinHeight());
        form.add(txt);
        
        txt = new InputText("txtMinWidth") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setMinHeight(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getMinWidth());
        form.add(txt);
        
        txt = new InputText("txtMaxHeight") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setMaxHeight(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getMaxHeight());
        form.add(txt);
        
        txt = new InputText("txtMaxWidth") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setMaxHeight(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oaHtmlComponent.getMaxWidth());
        form.add(txt);
        
        
        InputNumber txtNumber = new InputNumber("txtSize") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setSize(OAConv.toInt(getValue()));
            }
        };
        txt.setSize(5);
        txt.setAjaxSubmit(true);
        txt.setValue(""+oaHtmlComponent.getSize());
        form.add(txt);

        txtNumber = new InputNumber("txtMinLength") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setMinLength(OAConv.toInt(getValue()));
            }
        };
        txt.setSize(3);
        txt.setAjaxSubmit(true);
        txt.setValue(""+oaHtmlComponent.getMinLength());
        form.add(txt);
        
        txtNumber = new InputNumber("txtMaxLength") {
            @Override
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.oaHtmlComponent.setMaxLength(OAConv.toInt(getValue()));
            }
        };
        txt.setSize(5);
        txt.setAjaxSubmit(true);
        txt.setValue(""+oaHtmlComponent.getMaxLength());
        form.add(txt);
        
//qqqqqqqqq label to show eventName qqqq        
        
    }

}
