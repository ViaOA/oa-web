package com.viaoa.web.html;

import java.util.*;

import com.viaoa.util.OAConv;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.*;

public class HtmlElementPropertyEditor {
    private final OAForm form;
    private final OAHtmlComponent htmlComp;
    
    public HtmlElementPropertyEditor(final OAForm form, final HtmlElement he) {
        this.form = form;
        this.htmlComp = he.getOAHtmlComponent();
        setup();
    }
    
    protected void setup() {
        InputCheckBox chk;
        InputText txt;

        HtmlElement he = new HtmlElement("cmdReloadMessage") {
            public void beforePageLoad() {
                setVisible(false);
            }
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                setVisible(HtmlElementPropertyEditor.this.htmlComp.getNeedsReloaded());
            }
        };
        he.addStyle("background-color", "yellow");
        he.addStyle("font-weight", "bold");
        form.add(he);

        he = new HtmlElement("cmdOAReset");
        he.setToolTip("re-create form and components");
        form.add(he);
        
        InputButton cmdAjaxSubmit = new InputButton("cmdDoAjaxSubmit");
        cmdAjaxSubmit.setValue("ajaxSubmit");
        cmdAjaxSubmit.setAjaxSubmit(true);
        form.add(cmdAjaxSubmit);

        txt = new InputText("txtId") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setId(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getId());
        form.add(txt);
        
        txt = new InputText("txtValue") {
            String hold1;
            String hold2;
            @Override
            public void onSubmitBeforeLoadValues(OAFormSubmitEvent formSubmitEvent) {
                hold1 = getValue();
                hold2 = HtmlElementPropertyEditor.this.htmlComp.getValue();
            }
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                boolean b = false;
                if (OAStr.isNotEqual(hold1, getValue())) {
                    if (OAStr.isEqualIgnoreCase(hold2, HtmlElementPropertyEditor.this.htmlComp.getValue())) {
                        b = true;
                        HtmlElementPropertyEditor.this.htmlComp.setValue(getValue());
                    }
                }
                if (!b) setValue(HtmlElementPropertyEditor.this.htmlComp.getValue());
            }
            @Override
            public void beforePageLoad() {
                setValue(HtmlElementPropertyEditor.this.htmlComp.getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getValue());
        form.add(txt);
        
        txt = new InputText("txtLabelId") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setLabelId(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getLabelId());
        form.add(txt);

        txt = new InputText("txtFloatLabel") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setFloatLabel(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getFloatLabel());
        form.add(txt);

        txt = new InputText("txtPlaceHolder") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setPlaceHolder(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getPlaceHolder());
        form.add(txt);
        
        txt = new InputText("txtToolTipText") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setToolTipText(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getToolTipText());
        form.add(txt);
        
        txt = new InputText("txtAutoComplete") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setAutoComplete(getValue());
            }
        };
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getAutoComplete());
        form.add(txt);
                
        
        
        chk = new InputCheckBox("chkFormDebug") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.form.setDebug(getChecked());
            }
        };
        chk.setChecked(form.getDebug());
        chk.setAjaxSubmit(true);
        form.add(chk);

        chk = new InputCheckBox("chkEnabled") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setEnabled(getChecked());
            }
        };
        chk.setChecked(htmlComp.getEnabled());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkVisible") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setVisible(getChecked());
            }
        };
        chk.setChecked(htmlComp.getVisible());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkHidden") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setHidden(getChecked());
            }
        };
        chk.setChecked(htmlComp.getHidden());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkAjaxSubmit") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setAjaxSubmit(getChecked());
            }
        };
        chk.setChecked(htmlComp.getAjaxSubmit());
        chk.setAjaxSubmit(true);
        form.add(chk);

        chk = new InputCheckBox("chkSubmit") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setSubmit(getChecked());
            }
        };
        chk.setChecked(htmlComp.getSubmit());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkSpellCheck") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setSpellCheck(getChecked());
            }
        };
        chk.setChecked(htmlComp.getSpellCheck());
        chk.setAjaxSubmit(true);
        form.add(chk);

        
        txt = new InputText("txtHeight") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setHeight(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getHeight());
        form.add(txt);
        
        txt = new InputText("txtWidth") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setWidth(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getWidth());
        form.add(txt);

        txt = new InputText("txtMinHeight") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setMinHeight(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getMinHeight());
        form.add(txt);
        
        txt = new InputText("txtMinWidth") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setMinWidth(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getMinWidth());
        form.add(txt);
        
        txt = new InputText("txtMaxHeight") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setMaxHeight(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getMaxHeight());
        form.add(txt);
        
        txt = new InputText("txtMaxWidth") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setMaxWidth(getValue());
            }
        };
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(htmlComp.getMaxWidth());
        form.add(txt);
        
        InputNumber txtNumber = new InputNumber("txtSize") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setSize(OAConv.toInt(getValue()));
            }
        };
        txtNumber.setSize(5);
        txtNumber.setAjaxSubmit(true);
        if (htmlComp.getSize() > 0) txtNumber.setValue(""+htmlComp.getSize());
        form.add(txtNumber);

        txtNumber = new InputNumber("txtMinLength") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setMinLength(OAConv.toInt(getValue()));
            }
        };
        txtNumber.setSize(3);
        txtNumber.setAjaxSubmit(true);
        if (htmlComp.getMinLength() > 0) txtNumber.setValue(""+htmlComp.getMinLength());
        form.add(txtNumber);
        
        txtNumber = new InputNumber("txtMaxLength") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                HtmlElementPropertyEditor.this.htmlComp.setMaxLength(OAConv.toInt(getValue()));
            }
        };
        txtNumber.setSize(5);
        txtNumber.setAjaxSubmit(true);
        if (htmlComp.getMaxLength() > 0) txtNumber.setValue(""+htmlComp.getMaxLength());
        form.add(txtNumber);
        

        txt = new InputText("txtDataList") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                String s = getValue();
                if (OAStr.isEmpty(s)) {
                    HtmlElementPropertyEditor.this.htmlComp.setDataList(null);
                    return;
                }
                List<String> al = Arrays.asList(s.split(",\\s*"));
                HtmlElementPropertyEditor.this.htmlComp.setDataList(al);
            }
        };
        txt.setSize(40);
        txt.setAjaxSubmit(true);
        form.add(txt);

//qqqqqqqqqqqqq add txtMin & txtMax qqqqqqqqqqq        
        
        
    }

}
