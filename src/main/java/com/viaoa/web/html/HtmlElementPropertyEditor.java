package com.viaoa.web.html;

import java.util.*;

import com.viaoa.util.OAConv;
import com.viaoa.util.OAStr;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.html.input.*;

/**
 * OAWeb components to control htmlElementPropertyEditor.jsp
 * @author vince
 *
 */
public class HtmlElementPropertyEditor {
    private final OAForm form;
    private final HtmlElement htmlComp;
    private final OAHtmlComponent oahtmlComp;
    
    public HtmlElementPropertyEditor(final OAForm form, final HtmlElement he) {
        this.form = form;
        this.htmlComp = he;
        this.oahtmlComp = he.getOAHtmlComponent();
        setup();
    }

        
    /* list of components:
        txtId
        txtValue
        txtLabelId
        txtFloatLabel
        txtPlaceHolder
        txtTitle
        txtToolTipText
        txtPattern
        txtAutoComplete
        chkFormDebug
        chkRequired
        chkEnabled
        chkReadOnly
        chkVisible
        chkHidden
        chkAjaxSubmit
        chkSubmit
        chkSpellCheck
        txtHeight
        txtWidth
        txtMinHeight
        txtMinWidth
        txtMaxHeight
        txtMaxWidth
        txtSize
        txtMinLength
        txtMaxLength
        txtDataList
        txtMin
        txtMax
        txtStep
        chkMultiple
        txtAccept
        txtCapture
        txtMaxFileSize
        txtSrc
        txtCols
        txtRows
        txtAccessKey
        txtTabIndex
        txtHref
        txtTarget
    */
    
    protected void setup() {
        InputCheckBox chk;
        InputText txt;

        HtmlElement he = new HtmlElement("cmdReloadMessage") {
            public void beforePageLoad() {
                setVisible(false);
            }
            public void onSubmitCompleted(OAFormSubmitEvent formSubmitEvent) {
                setVisible(oahtmlComp.getNeedsReloaded());
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
                oahtmlComp.setId(getValue());
            }
        };
        txt.setHidden(!htmlComp.isSupported("id"));
        txt.setEnabled(htmlComp.isSupported("id"));
        txt.setLabelId("lblId");
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getId());
        form.add(txt);
        
        
        txt = new InputText("txtValue") {
            String hold1;
            String hold2;
            @Override
            public void onSubmitBeforeLoadValues(OAFormSubmitEvent formSubmitEvent) {
                hold1 = getValue();
                hold2 = oahtmlComp.getValue();
            }
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                boolean b = false;
                if (OAStr.isNotEqual(hold1, getValue())) {
                    if (OAStr.isEqualIgnoreCase(hold2, oahtmlComp.getValue())) {
                        b = true;
                        oahtmlComp.setValue(getValue());
                    }
                }
                if (!b) setValue(oahtmlComp.getValue());
            }
            @Override
            public void beforePageLoad() {
                setValue(oahtmlComp.getValue());
            }
        };
        txt.setLabelId("lblValue");
        txt.setHidden(!htmlComp.isSupported("value"));
        txt.setEnabled(htmlComp.isSupported("value"));
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getValue());
        form.add(txt);
        
        txt = new InputText("txtLabelId") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setLabelId(getValue());
            }
        };
        txt.setLabelId("lblLabelId");
        txt.setHidden(!htmlComp.isSupported("labelId"));
        txt.setEnabled(htmlComp.isSupported("labelId"));
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getLabelId());
        form.add(txt);

        txt = new InputText("txtFloatLabel") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setFloatLabel(getValue());
            }
        };
        txt.setLabelId("lblFloatLabel");
        txt.setHidden(!htmlComp.isSupported("floatLabel"));
        txt.setEnabled(htmlComp.isSupported("floatLabel"));
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getFloatLabel());
        form.add(txt);

        txt = new InputText("txtPlaceHolder") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setPlaceHolder(getValue());
            }
        };
        txt.setLabelId("lblPlaceHolder");
        txt.setHidden(!htmlComp.isSupported("placeHolder"));
        txt.setEnabled(htmlComp.isSupported("placeHolder"));
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getPlaceHolder());
        form.add(txt);

        txt = new InputText("txtTitle") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setTitle(getValue());
            }
        };
        txt.setLabelId("lblTitle");
        txt.setHidden(!htmlComp.isSupported("title"));
        txt.setEnabled(htmlComp.isSupported("title"));
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getTitle());
        form.add(txt);
        
        
        txt = new InputText("txtToolTipText") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setToolTipText(getValue());
            }
        };
        txt.setLabelId("lblToolTipText");
        txt.setHidden(!htmlComp.isSupported("toolTipText"));
        txt.setEnabled(htmlComp.isSupported("toolTipText"));
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getToolTipText());
        form.add(txt);

        txt = new InputText("txtPattern") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setPattern(getValue());
            }
        };
        txt.setLabelId("lblPattern");
        txt.setHidden(!htmlComp.isSupported("pattern"));
        txt.setEnabled(htmlComp.isSupported("pattern"));
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getPattern());
        form.add(txt);
        
        
        txt = new InputText("txtAutoComplete") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setAutoComplete(getValue());
            }
        };
        txt.setLabelId("lblAutoComplete");
        txt.setHidden(!htmlComp.isSupported("autocomplete"));
        txt.setEnabled(htmlComp.isSupported("autocomplete"));
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getAutoComplete());
        form.add(txt);
                
        
        
        chk = new InputCheckBox("chkFormDebug") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                form.setDebug(getChecked());
            }
        };
        chk.setLabelId("lblFormDebug");
        chk.setHidden(!htmlComp.isSupported("debug"));
        chk.setEnabled(htmlComp.isSupported("debug"));
        chk.setChecked(form.getDebug());
        chk.setAjaxSubmit(true);
        form.add(chk);

        chk = new InputCheckBox("chkRequired") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setRequired(getChecked());
            }
        };
        chk.setLabelId("lblRequired");
        chk.setHidden(!htmlComp.isSupported("required"));
        chk.setEnabled(htmlComp.isSupported("required"));
        chk.setChecked(oahtmlComp.getRequired());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        
        chk = new InputCheckBox("chkEnabled") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setEnabled(getChecked());
            }
        };
        chk.setLabelId("lblEnabled");
        chk.setHidden(!htmlComp.isSupported("enabled"));
        chk.setEnabled(htmlComp.isSupported("enabled"));
        chk.setChecked(oahtmlComp.getEnabled());
        chk.setAjaxSubmit(true);
        form.add(chk);

        chk = new InputCheckBox("chkReadOnly") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setReadOnly(getChecked());
            }
        };
        chk.setLabelId("lblReadOnly");
        chk.setHidden(!htmlComp.isSupported("readOnly"));
        chk.setEnabled(htmlComp.isSupported("readOnly"));
        chk.setChecked(oahtmlComp.getReadOnly());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkVisible") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setVisible(getChecked());
            }
        };
        chk.setLabelId("lblVisible");
        chk.setHidden(!htmlComp.isSupported("visible"));
        chk.setEnabled(htmlComp.isSupported("visible"));
        chk.setChecked(oahtmlComp.getVisible());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkHidden") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setHidden(getChecked());
            }
        };
        chk.setLabelId("lblHidden");
        chk.setHidden(!htmlComp.isSupported("hidden"));
        chk.setEnabled(htmlComp.isSupported("hidden"));
        chk.setChecked(oahtmlComp.getHidden());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkAjaxSubmit") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setAjaxSubmit(getChecked());
            }
        };
        chk.setLabelId("lblAjaxSubmit");
        chk.setHidden(!htmlComp.isSupported("ajaxSubmit"));
        chk.setEnabled(htmlComp.isSupported("ajaxSubmit"));
        chk.setChecked(oahtmlComp.getAjaxSubmit());
        chk.setAjaxSubmit(true);
        form.add(chk);

        chk = new InputCheckBox("chkSubmit") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setSubmit(getChecked());
            }
        };
        chk.setLabelId("lblSubmit");
        chk.setHidden(!htmlComp.isSupported("submit"));
        chk.setEnabled(htmlComp.isSupported("submit"));
        chk.setChecked(oahtmlComp.getSubmit());
        chk.setAjaxSubmit(true);
        form.add(chk);
        
        chk = new InputCheckBox("chkSpellCheck") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setSpellCheck(getChecked());
            }
        };
        chk.setLabelId("lblSpellCheck");
        chk.setHidden(!htmlComp.isSupported("spellCheck"));
        chk.setEnabled(htmlComp.isSupported("spellCheck"));
        chk.setChecked(oahtmlComp.getSpellCheck());
        chk.setAjaxSubmit(true);
        form.add(chk);

        
        txt = new InputText("txtHeight") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setHeight(getValue());
            }
        };
        txt.setLabelId("lblHeight");
        txt.setHidden(!htmlComp.isSupported("height"));
        txt.setEnabled(htmlComp.isSupported("height"));
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getHeight());
        form.add(txt);
        
        txt = new InputText("txtWidth") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setWidth(getValue());
            }
        };
        txt.setLabelId("lblWidth");
        txt.setHidden(!htmlComp.isSupported("width"));
        txt.setEnabled(htmlComp.isSupported("width"));
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getWidth());
        form.add(txt);

        txt = new InputText("txtMinHeight") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setMinHeight(getValue());
            }
        };
        txt.setLabelId("lblMinHeight");
        txt.setHidden(!htmlComp.isSupported("minHeight"));
        txt.setEnabled(htmlComp.isSupported("minHeight"));
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getMinHeight());
        form.add(txt);
        
        txt = new InputText("txtMinWidth") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setMinWidth(getValue());
            }
        };
        txt.setLabelId("lblMinWidth");
        txt.setHidden(!htmlComp.isSupported("minWidth"));
        txt.setEnabled(htmlComp.isSupported("minWidth"));
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getMinWidth());
        form.add(txt);
        
        txt = new InputText("txtMaxHeight") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setMaxHeight(getValue());
            }
        };
        txt.setLabelId("lblMaxHeight");
        txt.setHidden(!htmlComp.isSupported("maxHeight"));
        txt.setEnabled(htmlComp.isSupported("maxHeight"));
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getMaxHeight());
        form.add(txt);
        
        txt = new InputText("txtMaxWidth") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setMaxWidth(getValue());
            }
        };
        txt.setLabelId("lblMaxWidth");
        txt.setHidden(!htmlComp.isSupported("maxWidth"));
        txt.setEnabled(htmlComp.isSupported("maxWidth"));
        txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getMaxWidth());
        form.add(txt);
        
        // display size (in chars) of txt field
        InputNumber txtNumber = new InputNumber("txtSize") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setSize(OAConv.toInt(getValue()));
            }
        };
        txtNumber.setLabelId("lblSize");
        txtNumber.setHidden(!htmlComp.isSupported("size"));
        txtNumber.setEnabled(htmlComp.isSupported("size"));
        txtNumber.addStyle("width", "5em");
        txtNumber.setAjaxSubmit(true);
        if (oahtmlComp.getSize() > 0) txtNumber.setValue(""+oahtmlComp.getSize());
        form.add(txtNumber);

        // min input size (in chars) of txt field
        txtNumber = new InputNumber("txtMinLength") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setMinLength(OAConv.toInt(getValue()));
            }
        };
        txtNumber.setLabelId("lblMinLength");
        txtNumber.setHidden(!htmlComp.isSupported("minLength"));
        txtNumber.setEnabled(htmlComp.isSupported("minLength"));
        txtNumber.addStyle("width", "5em");
        txtNumber.setAjaxSubmit(true);
        if (oahtmlComp.getMinLength() > 0) txtNumber.setValue(""+oahtmlComp.getMinLength());
        form.add(txtNumber);
        
        // max input size (in chars) of txt field
        txtNumber = new InputNumber("txtMaxLength") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setMaxLength(OAConv.toInt(getValue()));
            }
        };
        txtNumber.setLabelId("lblMaxLength");
        txtNumber.setHidden(!htmlComp.isSupported("maxLength"));
        txtNumber.setEnabled(htmlComp.isSupported("maxLength"));
        txtNumber.addStyle("width", "5em");
        txtNumber.setAjaxSubmit(true);
        if (oahtmlComp.getMaxLength() > 0) txtNumber.setValue(""+oahtmlComp.getMaxLength());
        form.add(txtNumber);
        

        txt = new InputText("txtDataList") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                String s = getValue();
                if (OAStr.isEmpty(s)) {
                    oahtmlComp.setDataList(null);
                    return;
                }
                List<String> al = Arrays.asList(s.split(",\\s*"));
                oahtmlComp.setDataList(al);
            }
        };
        txt.setLabelId("lblDataList");
        txt.setHidden(!htmlComp.isSupported("dataList"));
        txt.setEnabled(htmlComp.isSupported("dataList"));
        txt.setSize(40);
        txt.setAjaxSubmit(true);
        form.add(txt);

        // min value allowed
        txt = new InputText("txtMin") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setMin(getValue());
            }
        };
        txt.setLabelId("lblMin");
        txt.setHidden(!htmlComp.isSupported("min"));
        txt.setEnabled(htmlComp.isSupported("min"));
        txt.setSize(12);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getMin());
        form.add(txt);
        
        // max value allowed
        txt = new InputText("txtMax") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setMax(getValue());
            }
        };
        txt.setLabelId("lblMax");
        txt.setHidden(!htmlComp.isSupported("max"));
        txt.setEnabled(htmlComp.isSupported("max"));
        txt.setSize(12);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getMax());
        form.add(txt);

        txt = new InputText("txtStep") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setStep(getValue());
            }
        };
        txt.setLabelId("lblStep");
        txt.setHidden(!htmlComp.isSupported("step"));
        txt.setEnabled(htmlComp.isSupported("step"));
        txt.setSize(12);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getStep());
        form.add(txt);
        
        
        chk = new InputCheckBox("chkMultiple") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setMultiple(getChecked());
            }
        };
        chk.setLabelId("lblMultiple");
        chk.setHidden(!htmlComp.isSupported("multiple"));
        chk.setEnabled(htmlComp.isSupported("multiple"));
        chk.setChecked(oahtmlComp.getMultiple());
        chk.setAjaxSubmit(true);
        form.add(chk);

        txt = new InputText("txtAccept") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setAccept(getValue());
            }
        };
        txt.setLabelId("lblAccept");
        txt.setHidden(!htmlComp.isSupported("accept"));
        txt.setEnabled(htmlComp.isSupported("accept"));
        txt.setSize(12);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getAccept());
        form.add(txt);

        txt = new InputText("txtCapture") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setCapture(getValue());
            }
        };
        txt.setLabelId("lblCapture");
        txt.setHidden(!htmlComp.isSupported("capture"));
        txt.setEnabled(htmlComp.isSupported("capture"));
        txt.setSize(12);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getCapture());
        txt.setToolTip("off, name, etc");
        form.add(txt);

        txtNumber = new InputNumber("txtMaxFileSize") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setMaxFileSize(OAConv.toInt(getValue()));
            }
        };
        txtNumber.setLabelId("lblMaxFileSize");
        txtNumber.setHidden(!htmlComp.isSupported("maxFileSize"));
        txtNumber.setEnabled(htmlComp.isSupported("maxFileSize"));
        txtNumber.addStyle("width", "7em");
        txtNumber.setAjaxSubmit(true);
        if (oahtmlComp.getMinLength() > 0) txtNumber.setValue(""+oahtmlComp.getMaxFileSize());
        form.add(txtNumber);
        
        
        
        txt = new InputText("txtSrc") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setSrc(getValue());
            }
        };
        txt.setLabelId("lblSrc");
        txt.setHidden(!htmlComp.isSupported("src"));
        txt.setEnabled(htmlComp.isSupported("src"));
        // txt.setSize(8);
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getSrc());
        form.add(txt);
    

        txtNumber = new InputNumber("txtCols") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setCols(OAConv.toInt(getValue()));
            }
        };
        txtNumber.setLabelId("lblCols");
        txtNumber.setHidden(!htmlComp.isSupported("cols"));
        txtNumber.setEnabled(htmlComp.isSupported("cols"));
        txtNumber.addStyle("width", "4em");
        txtNumber.setAjaxSubmit(true);
        if (oahtmlComp.getMinLength() > 0) txtNumber.setValue(""+oahtmlComp.getCols());
        form.add(txtNumber);

        txtNumber = new InputNumber("txtRows") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setRows(OAConv.toInt(getValue()));
            }
        };
        txtNumber.setLabelId("lblRows");
        txtNumber.setHidden(!htmlComp.isSupported("rows"));
        txtNumber.setEnabled(htmlComp.isSupported("rows"));
        txtNumber.addStyle("width", "4em");
        txtNumber.setAjaxSubmit(true);
        if (oahtmlComp.getMinLength() > 0) txtNumber.setValue(""+oahtmlComp.getRows());
        form.add(txtNumber);
        
        txt = new InputText("txtAccessKey") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setAccessKey(OAConv.toChar(getValue()));
            }
        };
        txt.setLabelId("lblAccessKey");
        txt.setHidden(!htmlComp.isSupported("accessKey"));
        txt.setEnabled(htmlComp.isSupported("accessKey"));
        txt.setSize(2);
        txt.setAjaxSubmit(true);
        if (oahtmlComp.getAccessKey() > 0) txt.setValue(OAConv.toString(oahtmlComp.getAccessKey()));
        form.add(txt);

        txtNumber = new InputNumber("txtTabIndex") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                String s = getValue();
                if (OAStr.isEmpty(s)) oahtmlComp.setUseTabIndex(false);
                else oahtmlComp.setTabIndex(OAConv.toInt(s));
            }
        };
        txtNumber.setLabelId("lblTabIndex");
        txtNumber.setHidden(!htmlComp.isSupported("tabIndex"));
        txtNumber.setEnabled(htmlComp.isSupported("tabIndex"));
        txtNumber.addStyle("width", "4em");
        txtNumber.setAjaxSubmit(true);
        if (oahtmlComp.getTabIndex() > 0) txtNumber.setValue(""+oahtmlComp.getTabIndex());
        form.add(txtNumber);

        txt = new InputText("txtInnerHtml") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setInnerHtml(getValue());
            }
        };
        txt.setLabelId("lblInnerHtml");
        txt.setHidden(!htmlComp.isSupported("innerHtml"));
        txt.setEnabled(htmlComp.isSupported("innerHtml"));
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getInnerHtml());
        form.add(txt);
        
        txt = new InputText("txtHref") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setHref(getValue());
            }
        };
        txt.setLabelId("lblHref");
        txt.setHidden(!htmlComp.isSupported("href"));
        txt.setEnabled(htmlComp.isSupported("href"));
        txt.setAjaxSubmit(true);
        txt.setValue(oahtmlComp.getHref());
        form.add(txt);
        
        txt = new InputText("txtTarget") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                oahtmlComp.setTarget(getValue());
            }
        };
        txt.setLabelId("lblTarget");
        txt.setHidden(!htmlComp.isSupported("target"));
        txt.setEnabled(htmlComp.isSupported("target"));
        txt.setAjaxSubmit(true);
        txt.setToolTip("_self, _blank, _parent, _top, or a named frame/window");
        txt.setValue(oahtmlComp.getTarget());
        form.add(txt);

        txt = new InputText("txtStyle") {
            @Override
            public void onSubmitLoadValues(OAFormSubmitEvent formSubmitEvent) {
                String s = getValue();
                if (OAStr.isEmpty(s)) return;
                s = OAStr.convert(s, '\'', "");
                s = OAStr.convert(s, '\"', "");
                
                String n = OAStr.field(s, ':', 1);
                n = OAStr.trim(n);
                String v = OAStr.field(s, ':', 2);
                v = OAStr.trim(v);
                oahtmlComp.addStyle(n, v);
            }
        };
        txt.setLabelId("lblStyle");
        txt.setAjaxSubmit(true);
        // txt.setValue(oahtmlComp.getTarget());
        form.add(txt);
    }

}
