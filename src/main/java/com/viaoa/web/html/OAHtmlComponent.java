/* Copyright 1999 Vince Via vvia@viaoa.com Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed
 * to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License. */
package com.viaoa.web.html;

import java.util.*;

import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.util.OAJspUtil;
import com.viaoa.object.OAObject;
import com.viaoa.template.OATemplate;
import com.viaoa.util.*;


// https://html.spec.whatwg.org/#toc-editing    

/**
 * The main "super" class for working with any HTML element, including form and input components.
 * <p>
 * The HTML element design does not map cleanly to OO classes using inheritance.<br>
 * OA uses a composite design, where this class includes all of the functionality needed by all components,
 * and then each component (java class) can "have a" OAComponent to do the work for it's subset of methods.
 * 
 * @author vvia
 */
public class OAHtmlComponent {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected OAForm form;
    
    protected String name;
    private boolean bDebug;

    // true if the page needs reloaded (to call oaForm.getScript)
    private boolean bNeedsReloaded; 
    
    // the attribute "value" is used differently, based on the element type.
    public static enum ValueAttributeType {
        Text(EventType.OnBlur),   // value of input/text
        Checkbox(EventType.OnChange),  // value sent on submit
        Radio(EventType.OnChange),  // value sent on submit
        Button(EventType.OnClick),  // button text
        File(EventType.OnBlur),   // value of file selected
        Select(EventType.OnChange); // selected option value 
        
        private EventType eventType;
        
        ValueAttributeType(EventType et) {
            this.eventType = et;
        }
        
        public EventType getDefaultEventType() {
            return eventType;
        }
    }
    protected ValueAttributeType valueAttributeType;
    
    public ValueAttributeType getValueAttributeType() {
        if (valueAttributeType == null) {
            InputType it = getInputType();
            if (it == null) return null;
            this.valueAttributeType = it.getValueAttributeType();
        }
        return this.valueAttributeType;
    }
    public void setValueAttributeType(ValueAttributeType vat) {
        if (this.valueAttributeType != vat) setNeedsReloaded(true);
        this.valueAttributeType = vat;
    }
    
    public InputType getInputType() {
        String s = getType();
        if (OAStr.isEmpty(s)) return null;
        for (InputType it : InputType.values()) {
            if (OAStr.isEqualIgnoreCase(it.getDisplay(), s)) {
                return it;
            }
        }
        return null;
    }
    
    // Types used for an Input Element
    public static enum InputType {
        Button(ValueAttributeType.Button),
        CheckBox(ValueAttributeType.Checkbox),
        Color(ValueAttributeType.Text),
        Date(ValueAttributeType.Text),
        DateTimeLocal("datetime-local", ValueAttributeType.Text),
        Email(ValueAttributeType.Text),
        File(),
        Hidden(ValueAttributeType.Text),
        Image(),
        Month(ValueAttributeType.Text),
        Number(ValueAttributeType.Text),
        Password(ValueAttributeType.Text),
        Radio(ValueAttributeType.Radio),
        Range(ValueAttributeType.Text),
        Reset(),
        Search(ValueAttributeType.Text),
        Submit(),
        Tel(ValueAttributeType.Text),
        Text(ValueAttributeType.Text),
        Time(ValueAttributeType.Text),
        Url(ValueAttributeType.Text),
        Week(ValueAttributeType.Text);
        
        private String display;
        private ValueAttributeType valueAttributeType;
        
        InputType() {
            display = name().toLowerCase();
        }

        InputType(ValueAttributeType vat) {
            display = name().toLowerCase();
            this.valueAttributeType = vat;
        }
        public ValueAttributeType getValueAttributeType() {
            return valueAttributeType;
            
        }
        InputType(String name, ValueAttributeType vat) {
            this.display = name;
            this.valueAttributeType = vat;
        }

        InputType(String name) {
            this.display = name;
        }
        
        public String getDisplay() {
            if (display == null) return name();
            return display;
        }
        
    }
    private String type;  // input type attribute 

    
    protected String value;
    protected String[] values;
    
    protected String labelId;
    protected String floatLabel;
    protected String placeHolder; // works with input types: text, search, url, tel, email, and password
    protected String pattern;

    protected boolean bEnabled = true;
    protected boolean bReadOnly;
    protected boolean bHidden;   // uses HTML hidden attr, does not use space
    protected boolean bVisible=true;  // uses CSS visibility:visible|hidden, does take up space
    
    
    
    protected boolean bRequired;
    private boolean bFocus;
    protected String forwardUrl;
    protected boolean bSubmit, bAjaxSubmit;
    protected String toolTipText;
    protected String toolTipTemplate;
    
    private boolean bIsPlainText;  // true if the text is not HTML
    private String autoComplete;
    private String fileAccept;
    protected boolean bMultiple;
    
    /**
     * List of choices suggested by browser to editor component.
     */
    protected String list; // id of dataList element 
    protected List<String> dataList; 

    protected Map<String, String> hmStyle;
    private List<String> alStyleAdd;
    private List<String> alStyleRemove;

    protected Set<String> hsClass;
    protected Set<String> hsClassAdd;
    protected Set<String> hsClassRemove;

    protected String confirmMessage;
    protected String confirmMessageTemplate;
    
    protected String height; // ex:  200px,  12em
    protected String width; // ex:  200px,  12em
    protected String minHeight; // ex:  200px,  12em
    protected String minWidth; // ex:  200px,  12em
    protected String maxHeight; // ex:  200px,  12em
    protected String maxWidth; // ex:  200px,  12em
    
    // number of chars for text, password
    protected int minLength;
    protected int maxLength;
    protected int size;  // types: text, search, tel, url, email, and password ... also used for select, for number of rows 

    protected int cols;  // textarea
    protected int rows;  // textarea
    
    // input min/max values
    protected int min;
    protected int max;
    
    protected int imageHeight;
    protected int imageWidth;
    
    protected boolean checked;
    protected boolean spellCheck;
    
    private List<HtmlOption> alOptions;
    
    
    // overflow: visible|hidden|clip|scroll|auto|initial|inherit;
    // https://www.w3schools.com/cssref/pr_pos_overflow.php
    public static enum OverflowType {
        Default("visible"),
        Visible(),
        Hidden(),
        Clip(),
        Scroll(),
        Auto(),
        Initial(),
        Inherit();
        
        private String display;
        
        OverflowType() {
            display = name().toLowerCase();
        }
        
        OverflowType(String cssName) {
            this.display = cssName;
        }
        
        public String getDisplay() {
            if (display == null) return name();
            return display;
        }
    }
    
    protected String overflow;  // overflow visible, hidden, scroll, auto, etc
    protected String source; // img src
    

    // tells the browser what type of input device to display.
    public static enum InputModeType { 
        Default("text"),
        None(),
        Text(),
        Decimal(),
        Numeric(),
        Tel(),
        Search(),
        Email(),
        Url();
        
        private String display;
        
        InputModeType() {
            display = name().toLowerCase();
        }
        
        InputModeType(String cssName) {
            this.display = cssName;
        }
        
        public String getDisplay() {
            if (display == null) return name();
            return display;
        }
    }
    protected String inputMode;  
    
    
    // https://www.w3schools.com/tags/ref_eventattributes.asp
    public static enum EventType {
        OnBlur(),
        OnClick(),
        OnDoubleClick(),
        OnChange(),
        OnContextMenu(),
        OnFocus(),
        OnInput(),
        OnSearch();
        
        private String display;
        
        EventType() {
            display = name().toLowerCase();
        }
        
        EventType(String name) {
            this.display = name;
        }
        
        public String getDisplay() {
            if (display == null) return name();
            return display;
        }
    }
    protected String eventName;
    
    
    // https://www.w3schools.com/cssref/pr_class_cursor.php
    public static enum CursorType {
        Default(),
        Pointer(),
        Crosshair(),
        Progress(),
        Help(),
        Url(),
        Wait(),
        None(),
        NotAllowed("not-allowed");
        
        protected String display;
        
        CursorType() {
            display = name().toLowerCase();
        }
        
        CursorType(String cssName) {
            this.display = cssName;
        }
        
        public String getDisplay() {
            if (display == null) return name();
            return display;
        }
    }
    protected String cursor;
    protected String alt; // for image
    protected int step;
    protected String innerHtml;
    
// autofocus
// disabled
    
    public static enum WrapType {
        Default("soft"),
        Soft(),
        Hard(),
        Off();
        
        protected String display;
        
        WrapType() {
            display = name().toLowerCase();
        }
        
        WrapType(String name) {
            this.display = name;
        }
        
        public String getDisplay() {
            if (display == null) return name();
            return display;
        }
    }
    protected String wrap;
    

// text has "placeholder"
    
    
// if disabled, then data is not sent to server during submit qqqqqqqqq    
    
// put in OAComponent:   protected OATemplate templateToolTip, templateConfirmMessage;
    
// regex match
    
//qqqqqqqq add html autocomplete    

    // multiple  ... needs names/values HM  datalist qqqqqqqqqqqqqqqqqqqqqqq    
    
    
// buttons are not submitable
    
// checkboxes do not submit if checkedness is false

// input type=image button  will send name.x & name.y
    
// select uses option<>

// checkbox and radio will use value. If value=null and it's selected, then value will be sent as "on"    
  
// accesskey="A"    

// FORM: <dialog>
// SESSION: history    
    
// drag and drop    
    
// OA add duration    
    
// ATTR: spellcheck, tabindex, 
// events:  onX
// data-*     
    
    
/*    
    
<input type="text" list="function-types">
<datalist id="function-types">
  <option value="function">function</option>
  <option value="async function">async function</option>
  <option value="function*">generator function</option>
  <option value="=>">arrow function</option>
  <option value="async =>">async arrow function</option>
  <option value="async function*">async generator function</option>
</datalist>
  
*/    
    
    // internal flags
    protected String lastAjaxSent;
    private boolean bToolTipChanged;
    private boolean bFloatLabelChanged;
    private boolean bPlaceHolderChanged;
    private boolean bPatternChanged;
    private boolean bRequiredChanged;
    private boolean bEnabledChanged;
    private boolean bReadOnlyChanged;
    private boolean bHiddenChanged;
    private boolean bVisibleChanged;
    private boolean bValueChangedByAjax;
    private boolean bDataListChanged;
    private boolean bCursorChanged;
    private boolean bAutoComponentChanged; // qqqqqqqqq todo: needs to add JS code & tested
    private boolean bFileAcceptChanged; // qqqqqqqqq todo: needs to add JS code & tested
    private boolean bLengthsChanged;
    private boolean bAltChanged;
    private boolean bStepChanged;
    private boolean bMinChanged;
    private boolean bMaxChanged;
    private boolean bImageChanged;
    private boolean bCheckedChanged;
    private boolean bListChanged;
    private boolean bSpellCheckChanged;
    private boolean bWrapChanged;
    private boolean bInnerHtmlChanged;
    private boolean bOptionsChanged;
    
    
    public OAHtmlComponent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        if (OAStr.isNotEqual(this.id, id)) setNeedsReloaded(true);
        this.id = id;
    }

    
    public OAForm getForm() {
        return this.form;
    }
    public void setForm(OAForm form) {
        if (this.form != form) setNeedsReloaded(true);
        this.form = form;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (OAStr.isNotEqual(this.name, name)) setNeedsReloaded(true);
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        if (OAStr.isNotEqual(this.type, type)) setNeedsReloaded(true);
        this.type = type;
    }
    public void setType(InputType type) {
        setType(type == null ? InputType.Text.getDisplay() : type.getDisplay());
    }
    
    
    public String getLabelId() {
        return labelId;
    }
    public void setLabelId(String id) {
        if (OAStr.isNotEqual(this.labelId, id)) setNeedsReloaded(true);
        this.labelId = id;
    }
    
    public String getFloatLabel() {
        return this.floatLabel;
    }
    public void setFloatLabel(String floatLabel) {
        this.bFloatLabelChanged |= OACompare.isNotEqual(this.floatLabel, floatLabel);
        if (bFloatLabelChanged && OAStr.isNotEmpty(this.floatLabel)) setNeedsReloaded(true);
        this.floatLabel = floatLabel;
    }

    public String getPlaceHolder() {
        return this.placeHolder;
    }
    public void setPlaceHolder(String placeHolder) {
        this.bPlaceHolderChanged |= OACompare.isNotEqual(this.placeHolder, placeHolder);
        this.placeHolder = placeHolder;
    }
    
    public String getPattern() {
        return this.pattern;
    }
    public void setPattern(String pattern) {
        this.bPatternChanged |= OACompare.isNotEqual(this.pattern, pattern);
        this.pattern = pattern;
    }
    
    public boolean getEnabled() {
        return bEnabled;
    }
    public boolean isEnabled() {
        return bEnabled;
    }
    public void setEnabled(boolean b) {
        bEnabledChanged |= (b != this.bEnabled);
        this.bEnabled = b;
    }

    public boolean getReadOnly() {
        return bReadOnly;
    }
    public boolean isReadOnly() {
        return bReadOnly;
    }
    public void setReadOnly(boolean b) {
        bReadOnlyChanged |= (b != this.bReadOnly);
        this.bReadOnly = b;
    }

    

    /**
     * HTML attr.  If true, then space is NOT used on the page. same as CSS display:none
     * @return
     */
    public boolean getHidden() {
        return bHidden;
    }
    public boolean isHidden() {
        return bHidden;
    }
    public void setHidden(boolean b) {
        bHiddenChanged |= (b != this.bHidden);
        this.bHidden = b;
    }

    /**
     *  Space is reserved on the page. Uses CSS visibility:visible|hidden
     */
    public boolean getVisible() {
        return bVisible;
    }
    public boolean isVisible() {
        return bVisible;
    }
    public void setVisible(boolean b) {
        bVisibleChanged |= (b != this.bVisible);
        this.bVisible= b;
    }
    
    
    public boolean getRequired() {
        return bRequired;
    }
    public boolean isRequired() {
        return bRequired;
    }
    public void setRequired(boolean required) {
        bRequiredChanged |= this.bRequired != required;
        this.bRequired = required;
    }

    public void setFocus(boolean b) {
        this.bFocus = b;
    }
    public void setFocus() {
        this.bFocus = true;
    }
    
    
    /**
     * The value attribute is used differently for different input types:
     *    For "button", "reset", and "submit" - it defines the text on the button
     *    For "text", "password", and "hidden" - it defines the initial (default) value of the input field
     *    For "checkbox", "radio", "image" - it defines the value associated with the input (this is also the value that is sent on submit)
     * Also, for "textarea"
     */
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        if (OAStr.isEqual(this.value, value)) return;
        this.value = value;
        this.bValueChangedByAjax = false;
    }

    public String[] getValues() {
        return this.values;
    }
    public void setValues(String[] values) {
        if (this.values == values) return;
        if (OACompare.isEqual(this.values, values)) return;
        /*was
        if (this.values != null && values != null) {
            int x = this.values.length;
            if (x == values.length) {
                boolean b = true;
                for (int i=0; i<x; i++) {
                    if (OAStr.isNotEqual(this.values[i], values[i])) {
                        b = false;
                        break;
                    }
                }
                if (b) return; // same
            }
        }
        */
        this.values = values;
        this.bValueChangedByAjax = false;
    }
    
    
    public String getForwardUrl() {
        return this.forwardUrl;
    }
    public void setForwardUrl(String forwardUrl) {
        if (OAStr.isNotEqual(this.forwardUrl, forwardUrl)) setNeedsReloaded(true);
        this.forwardUrl = forwardUrl;
    }

    public boolean getSubmit() {
        return bSubmit;
    }
    public void setSubmit(boolean b) {
        if (this.bSubmit != b) setNeedsReloaded(true);
        bSubmit = b;
    }
    
    public boolean getAjaxSubmit() {
        return bAjaxSubmit;
    }
    public void setAjaxSubmit(boolean b) {
        if (this.bAjaxSubmit != b) setNeedsReloaded(true);
        bAjaxSubmit = b;
    }

    public String getToolTip() {
        return this.toolTipText;
    }
    public void setToolTip(String toolTip) {
        this.bToolTipChanged |= OACompare.isNotEqual(this.toolTipText, toolTip);
        this.toolTipText = toolTip;
    }

    public String getToolTipText() {
        return this.toolTipText;
    }
    public void setToolTipText(String toolTip) {
        setToolTip(toolTip);
    }

    public String getToolTipTemplate() {
        return this.toolTipTemplate;
    }
    public void setToolTipTemplate(String toolTipTemplate) {
        this.toolTipTemplate = toolTipTemplate;
    }
    
    public String getCalcToolTip() {
        return getToolTipText();
    }
    
    public void reset() {
        value = null;
        lastAjaxSent = null;
    }
    
    public boolean isChanged() {
        return false;
    }

    // allows components to do prechecks
    public void onSubmitPrecheck(final OAFormSubmitEvent formSubmitEvent) {
    }

    // allows components to cancel
    public void onSubmitBeforeLoadValues(final OAFormSubmitEvent formSubmitEvent) {
    }

    // allows components to cancel
    public void onSubmitAfterLoadValues(final OAFormSubmitEvent formSubmitEvent) {
    }
    
    public String getCalcName() {
        String s = getName();
        if (OAString.isEmpty(s)) s = getId();
        return s;
    }
    
    // all are called, only run if not cancelled and not the submit command
    public void onSubmitLoadValues(final OAFormSubmitEvent formSubmitEvent) {
        if (formSubmitEvent.getCancel()) return;
        if (!getEnabled()) return; // disabled components do not get submitted.  Note: readony do
        
        String s = getCalcName();
        
        if (OAStr.isEmpty(s)) return;
        
        String[] ss = formSubmitEvent.getNameValueMap().get(s);
        ValueAttributeType vat = getValueAttributeType();
        if (vat != null) {
            switch (vat) {
            case Text:
                setValue(ss == null ? null : ss.length==0 ? null : ss[0]);
                break;
            case Checkbox:
                boolean b = (ss != null && ss.length > 0);
                setChecked(b);
                break;
            case Radio:
                b = (ss != null && ss.length > 0);
                if (b) {
                    b = OAStr.isEqual(getValue(), ss[0], true);
                }
                setChecked(b);
                break;
            case Button:
                break;
            case Select:
                setValues(ss);
                List<HtmlOption> al = getOptions();
                if (al != null && ss != null) {
                    for (HtmlOption ho : al) {
                        if (ho instanceof HtmlOptionGroup) {
                            continue;
                        }
                        boolean bx = false;
                        for (String sx : ss) {
                            if (OAStr.isEqual(ho.getValue(), sx)) {
                                bx = true;
                                break;
                            }
                        }
                        bOptionsChanged |= (ho.getSelected() != bx);
                        ho.setSelected(bx);
                    }
                }                        
            }
        }
        this.bValueChangedByAjax = formSubmitEvent.getAjax();
    }

    // only called on the component that submitted, if event was not previously cancelled.
    public void onSubmit(final OAFormSubmitEvent formSubmitEvent) {
    }
    
    // any cleanup 
    public void onSubmitCompleted(final OAFormSubmitEvent formSubmitEvent) {
        if (formSubmitEvent.getCancel()) return;

    }

    
    /**
     * Called by containers to get html to edit.
     */
    public String getEditorHtml(OAObject obj) {
        return null;
    }
    
    /**
     * Called by containers to get html to render a view only version.
     * This is used to display the non-activeObjects, instead of using the actual editor component.
     */
    public String getRenderHtml(OAObject obj) {
        return null;
    }

    public String getTableEditorHtml() {
        return null;
    }

    // id of <datalist>
    public String getList() {
        return list;
    }
    public void setList(String listId) {
        bListChanged |= OAStr.isNotEqual(this.list, listId);
        this.list = listId;
    }
    
    public List<String> getDataList() {
        return this.dataList;
    }
    public void setDataList(List<String> lst) {
        this.dataList = lst;
        bDataListChanged = true;
    }

    
    
    public String getStyle(String name) {
        if (OAStr.isEmpty(name)) return null;
        if (hmStyle == null) return  null;
        String s = hmStyle.get(name);
        return s;
    }
    public List<String> getStyles() {
        List<String> al = new ArrayList();
        if (hmStyle != null) {
            for (Map.Entry<String, String> ex : hmStyle.entrySet()) {
                String sx = ex.getKey();
                al.add(sx);
            }
        }
        return al;
    }

    public void addStyle(final String name, final String value) {
        if (OAStr.isEmpty(name)) return;
        if (OAStr.isEmpty(value)) {
            removeStyle(name);
            return;
        }
        
        if (hmStyle == null) hmStyle = new HashMap<String, String>();
        else {
            String s = hmStyle.get(name);
            if (s != null && s.equals(value)) return;
        }
        hmStyle.put(name, value);
        if (alStyleAdd == null) alStyleAdd = new ArrayList();
        alStyleAdd.add(name);
        if (alStyleRemove != null) alStyleRemove.remove(name);
    }
    
    public void removeStyle(final String name) {
        if (OAStr.isEmpty(name)) return;
        if (hmStyle == null) return;
        if (hmStyle.get(name) == null) return;

        hmStyle.remove(name);
        if (alStyleRemove == null) alStyleRemove = new ArrayList();
        alStyleRemove.add(name);
        if (alStyleAdd != null) alStyleAdd.remove(name);
    }
    
    protected String getStyleJs(final boolean bIsInitializing) {
        ArrayList<String> al = new ArrayList<String>();

        if (bIsInitializing) {
            if (hmStyle != null) {
                for (Map.Entry<String, String> ex : hmStyle.entrySet()) {
                    String sx = ex.getKey();
                    String v = ex.getValue();
                    al.add("'"+sx + "':'" + v + "'");
                }
            }
        }
        else {
            if (alStyleAdd != null) {
                for (String name : alStyleAdd) {
                    String val = hmStyle.get(name);
                    al.add("'"+name + "':'" + val + "'");
                }
            }
        
            if (alStyleRemove != null) {
                for (String name : alStyleRemove) {
                    al.add("'"+name + "':'initial'");
                    //was: al.add("'"+name + "':'inherit'");
                }
            }        
        }
        if (alStyleAdd != null) alStyleAdd.clear();
        if (alStyleRemove != null) alStyleRemove.clear();
        
        String css = null;
        for (String s : al) {
            if (css == null) css = "{";
            else css += ",";
            css += s;
        }
        if (css != null) css += "}";
        return css;
    }

    public String getHeight() {
        return this.height;
    }
    
    public void setHeight(String val) {
        this.height = val;
        addStyle("height", val);
    }
    public String getWidth() {
        return this.width;
    }
    public void setWidth(String val) {
        addStyle("width", val);
        this.width = val;
    }
    
    public String getMinHeight() {
        return this.minHeight;
    }
    public void setMinHeight(String val) {
        addStyle("min-height", val);
        this.minHeight = val;
    }
    public String getMinWidth() {
        return this.minWidth;
    }
    public void setMinWidth(String val) {
        addStyle("min-width", val);
        this.minWidth = val;
    }
    
    public String getMaxHeight() {
        return this.maxHeight;
    }
    public void setMaxHeight(String val) {
        addStyle("max-height", val);
        this.maxHeight = val;
    }
    public String getMaxWidth() {
        return this.maxWidth;
    }
    public void setMaxWidth(String val) {
        addStyle("max-width", val);
        this.maxWidth = val;
    }

    public int getMinLength() {
        return this.minLength;
    }
    public void setMinLength(int val) {
        bLengthsChanged |= (this.minLength != val);
        this.minLength = val;
    }
    public int getMaxLength() {
        return this.maxLength;
    }
    public void setMaxLength(int val) {
        bLengthsChanged |= (this.maxLength != val);
        this.maxLength = val;
    }
    public int getSize() {
        return this.size;
    }
    public void setSize(int val) {
        bLengthsChanged |= (this.size != val);
        this.size = val;
    }
    
    public int getCols() {
        return this.cols;
    }
    public void setCols(int val) {
        bLengthsChanged |= (this.cols != val);
        this.cols = val;
    }

    public int getRows() {
        return this.rows;
    }
    public void setRows(int val) {
        bLengthsChanged |= (this.rows != val);
        this.rows = val;
    }
    
    public int getMin() {
        return this.min;
    }
    public void setMin(int val) {
        bMinChanged |= this.min != val;
        this.min = val;
    }

    public int getMax() {
        return this.max;
    }
    public void setMax(int val) {
        bMaxChanged |= this.max != val;
        this.max = val;
    }
    
    public int getImageHeight() {
        return this.imageHeight;
    }
    public void setImageHeight(int val) {
        bImageChanged |= this.imageHeight != val;
        this.imageHeight = val;
    }
    public int getImageWidth() {
        return this.imageWidth;
    }
    public void setImageWidth(int val) {
        bImageChanged |= this.imageWidth != val;
        this.imageWidth = val;
    }

    
    
    public String getOverflow() {
        return overflow;
    }
    public void setOverflow(String overflow) {
        this.overflow = overflow;
        
        addStyle("overflow", overflow);
        if (OAStr.isEmpty(overflow)) {
            removeStyle("text-overflow");
        }
        else if (overflow.equalsIgnoreCase("hidden")) {
            addStyle("text-overflow", "ellipsis");
        }
    }
    public void setOverflow(OverflowType overflowType) {
        setOverflow(overflowType == null ? OverflowType.Default.getDisplay() : overflowType.getDisplay());
    }
    
    
    public String getSource() {
        return this.source;
    }
    public void setSource(String val) {
        bImageChanged |= OACompare.isNotEqual(this.source, val);
        this.source = val;
    }
    public String getSrc() {
        return this.source;
    }
    public void setSrc(String val) {
        setSource(val);
    }
    
    
    
    public void addClass(final String name) {
        if (OAStr.isEmpty(name)) return;
        if (hsClass == null) hsClass = new HashSet<>();
        else if (hsClass.contains(name)) return;
        hsClass.add(name);
        
        if (hsClassAdd == null) hsClassAdd = new HashSet<>();
        hsClass.add(name);
        if (hsClassRemove != null) hsClassRemove.remove(name); 
    }
    public void removeClass(final String name) {
        if (OAStr.isEmpty(name)) return;
        if (hsClass == null) return;
        hsClass.remove(name);
        
        if (hsClassAdd != null) {
            if (hsClassAdd.remove(name)) return;
        }
        if (hsClassRemove == null) hsClassRemove = new HashSet<>();
        hsClassRemove.add(name);
        if (hsClassAdd != null) hsClassAdd.remove(name); 
    }

    public List<String> getClasses() {
        List<String> al = new ArrayList();
        if (hsClass != null) {
            for (String s : hsClass) {
                al.add(s);
            }
        }
        return al;
    }

    
    protected String getClassJs(final boolean bIsInitializing) {
        String s = null;
        Iterator itx;
        
        if (bIsInitializing) {
            if (hsClass != null) {
                itx = hsClass.iterator();
                for ( ; itx.hasNext() ;  ) {
                    String sx = (String) itx.next();
                    if (s == null) s = "";
                    s += "$('#"+id+"').addClass('"+sx+"');";
                }
            }
        }
        else {
            if (hsClassAdd != null) {
                itx = hsClassAdd.iterator();
                for ( ; itx.hasNext() ;  ) {
                    String sx = (String) itx.next();
                    if (s == null) s = "";
                    s += "$('#"+id+"').addClass('"+sx+"');";
                }
            }
            
            if (hsClassRemove != null) {
                itx = hsClassRemove.iterator();
                for ( ; itx.hasNext() ;  ) {
                    String sx = (String) itx.next();
                    if (s == null) s = "";
                    s += "$('#"+id+"').removeClass('"+sx+"');";
                }
            }     
        }
        if (hsClassAdd != null) hsClassAdd.clear();
        if (hsClassRemove != null) hsClassRemove.clear();
        return s;
    }

    public String getInputMode() {
        return inputMode;
    }
    public void setInputMode(String mode) {
        if (OAStr.isNotEqual(this.inputMode, mode)) setNeedsReloaded(true);
        this.inputMode = mode;
    }
    public void setInputMode(InputModeType type) {
        setInputMode(type == null ? InputModeType.Default.getDisplay() : type.getDisplay());
    }
    
    /**
     * Message for user to verify before submitting form.
     * Requires either submit or ajaxSubmit to be true.
     */
    public String getConfirmMessage() {
        return this.confirmMessage;
    }
    public void setConfirmMessage(String msg) {
        if (OAStr.isNotEqual(this.confirmMessage, msg)) setNeedsReloaded(true);
        confirmMessage = msg;
    }
    public String getConfirmMessageTemplate() {
        return this.confirmMessageTemplate;
    }
    public void setConfirmMessageTemplate(String msg) {
        if (OAStr.isNotEqual(this.confirmMessageTemplate, msg)) setNeedsReloaded(true);
        this.confirmMessageTemplate = msg;
    }
    public String getCalcConfirmMessage() {
        return getToolTipText();
    }

    public String getCursor() {
        return cursor;
    }
    public void setCursor(String cursorName) {
        bCursorChanged |= OACompare.isNotEqual(this.cursor, cursorName);
        this.cursor = cursorName;
    }
    public void setCursor(CursorType cursorType) {
        setCursor(cursorType == null ? CursorType.Default.getDisplay() : cursorType.getDisplay());
    }

    public String getCalcEventName() {
        String name = getEventName();
        if (OAStr.isNotEmpty(name)) return name;
        
        ValueAttributeType vat = getValueAttributeType();
        if (vat == null) {
            InputType it = getInputType();
            if (it != null) vat = it.getValueAttributeType();
        }
        if (vat != null) {
            EventType et = vat.getDefaultEventType();
            if (et != null) name = et.getDisplay();
        }
        if (OAStr.isEmpty(name)) name = EventType.OnClick.getDisplay();
        return name;
    }
    
    /** 
     * The name the event that this component responds to.
     */
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String name) {
        if (OAStr.isNotEqual(this.eventName, name)) setNeedsReloaded(true);
        this.eventName = name;
    }
    public void setEventType(EventType eventType) {
        setEventName(eventType == null ? null : eventType.getDisplay());
    }

    public boolean getPlainText() {
        return bIsPlainText;
    }
    public boolean isPlainText() {
        return bIsPlainText;
    }
    public void setPlainText(boolean b) {
        if (this.bIsPlainText != b) setNeedsReloaded(true);
        bIsPlainText = b;
    }

//qqqqqqqqqqqqqq add js for this    
    public String getAutoComplete() {
        return this.autoComplete;
    }
    public void setAutoComplete(String val) {
        bAutoComponentChanged |= OACompare.isNotEqual(this.autoComplete, val);
        this.autoComplete = val;
    }


    public String getFileAccept() {
        return this.fileAccept;
    }
    public void setFileAccept(String val) {
        bFileAcceptChanged |= OACompare.isNotEqual(this.fileAccept, val);
        this.fileAccept = val;
    }

    public boolean getMultiple() {
        return this.bMultiple;
    }
    public void setMultiple(boolean b) {
        if (this.bMultiple != b) setNeedsReloaded(true);
        this.bMultiple = b;
    }
    
    /**
     * Called by OAForm.
     */
    public void beforePageLoad() {
    }
    /**
     * Called by OAForm.
     */
    public void afterPageLoad() {
    }
    
    public String getScript() {
        String s = _getScript();
        setNeedsReloaded(false);
        return s;
    }

    protected String _getScript() {
        StringBuilder sb = new StringBuilder(1024);

        String s = getCalcName();
        sb.append("$('#" + id + "').attr('name', '"+s+"');\n");

        s = getType();
        if (OAStr.isNotEmpty(s)) sb.append("$('#" + id + "').attr('type', '"+s+"');\n");
        
        sb.append("$('#" + id + "').blur(function() {$(this).removeClass('oaError');}); \n");

        if (getSubmit() || getAjaxSubmit()) {
            sb.append("$('#" + id + "').addClass('oaSubmit');\n");
        }

        if (getMultiple()) sb.append("$('#" + id + "').attr('multiple', true);\n");

        String confirm = getConfirmMessage();
        if (OAString.isNotEmpty(confirm)) {
            confirm = OAJspUtil.createJsString(confirm, '\"');
            confirm = "if (!window.confirm(\""+confirm+"\")) return false;";
        }
        else confirm = "";

        getDataListJs(sb);
        
        s = getVerifyScript();
        if (OAString.isNotEmpty(s)) {
            sb.append("\n");
            sb.append(s);
        }

        String eventName = getCalcEventName();
        if (OAStr.isNotEmpty(eventName)) {
            if (eventName.startsWith("on")) eventName = eventName.substring(2);
        
            if (getSubmit()) {
                sb.append("$('#" + id + "')."+ eventName + "(function() {\n");
                
//qQQQQQQQQQQQQQQ ONLY IF TEXT Value is used                
//qqqqqqqqq new ... add to ajaxsubmit qqqqqqqqq
                if (getValueAttributeType() == ValueAttributeType.Text) {
                    sb.append("if (typeof this.oaPrevValue === \"undefined\") {\n");
                    sb.append("    if (this.value === this.getAttribute(\"value\")) return false;\n");
                    sb.append("}\n");
                    sb.append("else if (this.value === this.oaPrevValue) return false;\n");
                    sb.append("this.oaPrevValue = this.value;\n");
                }                
                
                if (OAStr.isNotEmpty(confirm)) {
                    sb.append("  "+confirm);
                }
                sb.append("  $('#oacommand').val('" + id + "');\n");
                sb.append("  $('#"+getForm().getId()+"').submit();\n");
                sb.append("  $('#oacommand').val('');\n");
                sb.append("  $('#oaparam').val('');\n");
                sb.append("  return false;\n");
                sb.append("});\n");
            }
            else if (getAjaxSubmit()) {
                sb.append("$('#" + id + "')."+ eventName+"(function() {\n");

//qQQQQQQQQQQQQQQ ONLY IF TEXT Value is used                
//qqqqqqqqq new ... add to submit qqqqqqqqq                
                if (getValueAttributeType() == ValueAttributeType.Text) {
                    sb.append("if (typeof this.oaPrevValue === \"undefined\") {\n");
                    sb.append("    if (this.value === this.getAttribute(\"value\")) return false;\n");
                    sb.append("}\n");
                    sb.append("else if (this.value === this.oaPrevValue) return false;\n");
                    sb.append("this.oaPrevValue = this.value;\n");
                }
                
                if (OAStr.isNotEmpty(confirm)) {
                    sb.append("  "+confirm);
                }
                sb.append("  $('#oacommand').val('" + id + "');\n");
                sb.append("  ajaxSubmit();\n");
                sb.append("  $('#oacommand').val('');\n");
                sb.append("  $('#oaparam').val('');\n");
                sb.append("  return false;\n");
                sb.append("});\n");
            }
            
            if (OAStr.isNotEmpty(getForwardUrl())) {
                sb.append("$('#"+id+"')."+eventName+"(function() {"+confirm+"window.location = 'oaforward.jsp?oaform="+getForm().getId()+"&oacommand="+id+"';return false;});\n");
            }
        }
        
        sb.append(getAjaxScript(true));

        s = getInputMode();
        if (OAStr.isNotEmpty(s)) {
            sb.append("$('#" + id + "').attr('inputmode', '"+s+"');\n");
        }
        
        
        String js = sb.toString();
        return js;
    }

    public String getVerifyScript() {
        return null;
    }

    public String getAjaxScript() {
        return getAjaxScript(false);
    }

    
    
    public String getAjaxScript(final boolean bIsInitializing) {
        StringBuilder sb = new StringBuilder(1024);

        String s = getValue();
        if (s == null) s = "";
        
        
        ValueAttributeType vat = getValueAttributeType();        
        
        if (vat != null && !this.bValueChangedByAjax) {
            sb.append("$('#" + id + "').val('"+s+"');\n");

//qqqqqqqqqqqqqqqq            
            if (!bIsInitializing && vat == ValueAttributeType.Text) {
                
                sb.append("$('#" + id + "').oaPrevValue = $('#" + id + "').val(); //qqqqqqqqqqqqqqq\n");
            }
        }
        
        if (bIsInitializing || ((hsClassAdd != null && hsClassAdd.size() > 0) || (hsClassRemove != null && hsClassRemove.size() > 0))) {
            s = getClassJs(bIsInitializing);
            if (OAString.isNotEmpty(s)) sb.append(s);
        }
        
        s = getInnerHtml();
        if (bInnerHtmlChanged || (bIsInitializing && OAStr.isNotEmpty(s))) {
            bInnerHtmlChanged = false;
            s = OAJspUtil.createEmbeddedHtmlString(s, '\'');
            sb.append("$('#"+id+"').html('"+s+"');\n");
        }

        if (bCursorChanged || (bIsInitializing && OAStr.isNotEmpty(getCursor()))) {
            bCursorChanged = false;
            s = getCursor();
            if (OAStr.isEmpty(s)) s = CursorType.Default.getDisplay();
            sb.append("$('#"+id+"').css('cursor', '"+s+"');\n");
        }
        
        if (bIsInitializing || (alStyleAdd != null && alStyleAdd.size() > 0) || (alStyleRemove != null && alStyleRemove.size() > 0)) {
            s = getStyleJs(bIsInitializing);
            if (OAStr.isNotEmpty(s)) {
                sb.append("$('#"+id+"').css("+s+");\n");
            }
        }
        
        if (bWrapChanged || (bIsInitializing && OAStr.isNotEmpty(getWrap()))) {
            bWrapChanged = false;
            s = getWrap();
            if (OAStr.isNotEmpty(s)) {
                sb.append("$('#"+id+"').attr('wrap', '"+s+"');\n");
            }
            else {
                sb.append("$('#"+id+"').removeAttr('wrap');\n");
            }
        }
        
        
        
        
        if (bListChanged || (bIsInitializing && OAStr.isNotEmpty(getList()))) {
            bListChanged = false;
            if (OAStr.isNotEmpty(getList())) {
                sb.append("$('#" + id + "').attr('list', '"+getList()+"');\n");
            }
            else {
                sb.append("$('#" + id + "').removeAttr('list');\n");
            }
        }

        if (bLengthsChanged || (bIsInitializing && (getSize() > 0 || getMinLength() > 0 || getMaxLength() > 0))) {
            bLengthsChanged = false;
            if (getSize() > 0) {
                sb.append("$('#" + id + "').attr('size', "+getSize()+");\n");
            }
            if (getMinLength() > 0) {
                sb.append("$('#" + id + "').attr('minlength', "+getMinLength()+");\n");
            }
            if (getMaxLength() > 0) {
                sb.append("$('#" + id + "').attr('maxlength', "+getMaxLength()+");\n");
            }
            
            if (getCols() > 0) {
                sb.append("$('#" + id + "').attr('cols', "+getCols()+");\n");
            }
            if (getRows() > 0) {
                sb.append("$('#" + id + "').attr('rows', "+getRows()+");\n");
            }
        }
             
        
        if (bPlaceHolderChanged || (bIsInitializing && OAStr.isNotEmpty(getPlaceHolder()))) {
            bPlaceHolderChanged = false;
            sb.append("$('#" + id + "').attr('placeholder', '"+getPlaceHolder()+"');\n");
        }
        
        if (bFloatLabelChanged || (bIsInitializing && OAStr.isNotEmpty(getFloatLabel()))) {
            bFloatLabelChanged = false;
            getFloatLabelJs(sb);
        }
        
        if (bPatternChanged || (bIsInitializing && OAStr.isNotEmpty(getPattern()))) {
            if (OAString.isEmpty(getPattern())) {
                sb.append("$('#" + id + "').removeAttr('pattern');\n");
            }
            else {
                //qqqqqqqqqqqq todo: pattern needs to have correct '\' chars excaped
//qqqqqqqqqq create method getRegex as JS in OAJspUtil                
                sb.append("$('#" + id + "').attr('pattern', '"+getPattern()+"');\n");
            }
        }
            
        if (bImageChanged || (bIsInitializing && (OAStr.isNotEmpty(getSource()) || getImageWidth() > 0 || getImageHeight() > 0))) {
            bImageChanged = false;
            sb.append("$('#" + id + "').attr('src', '"+getSource()+"');\n");
            if (getImageWidth() > 0) sb.append("$('#" + id + "').attr('width', "+getImageWidth()+");\n");
            if (getImageHeight() > 0) sb.append("$('#" + id + "').attr('width', "+getImageHeight()+");\n");
        }
            
        
        
        // tooltip
        if (bToolTipChanged || (bIsInitializing && OAStr.isNotEmpty(getToolTipText()))) {
            bToolTipChanged = false;
            String prefix = null;
            String tt = getCalcToolTip();
            if (OAString.isNotEmpty(tt)) {
                if (bIsInitializing) {
                    sb.append("$('#" + id + "').tooltip();\n");
                }
                tt = OAJspUtil.createJsString(tt, '\'');
    
                sb.append("$('#" + id + "').data('bs.tooltip').options.title = '" + tt + "';\n");
                sb.append("$('#" + id + "').data('bs.tooltip').options.placement = 'top';\n");
            }
            else {
                sb.append("$('#" + id + "').tooltip('destroy');\n");
            }
        }

        if (bFocus) {
            bFocus = false;
            sb.append("$('#" + id + "').focus();\n");
        }

        if (bEnabledChanged || (bIsInitializing && !getEnabled())) {
            bEnabledChanged = false;
            s = getEnabledScript();
            if (OAString.isNotEmpty(s)) sb.append(s);
        }

        if (bReadOnlyChanged || (bIsInitializing && isReadOnly())) {
            bReadOnlyChanged = false;
            if (isReadOnly()) {
                sb.append("$('#" + id + "').prop('readOnly', true);\n");
                sb.append("$('#" + id + "').attr('readonly', 'readonly');\n");
            }
            else {
                sb.append("$('#" + id + "').prop('readOnly', false);\n");
                sb.append("$('#" + id + "').removeAttr('readonly');\n");
            }
        }
        
        // HTML hidden attribute does not take up space
        if (bHiddenChanged || (bIsInitializing && getHidden())) {
            bHiddenChanged = false;
            if (isHidden()) {
                sb.append("$('#" + id + "').prop('hidden', true);\n");
                sb.append("$('#" + id + "').attr('hidden', 'hidden');\n");
            }
            else {
                sb.append("$('#" + id + "').prop('hidden', false);\n");
                sb.append("$('#" + id + "').removeAttr('hidden');\n");
            }
            
            if (OAStr.isNotEmpty(getLabelId())) {
                if (isHidden()) {
                    sb.append("$('#" + getLabelId() + "').prop('hidden', true);\n");
                    sb.append("$('#" + getLabelId() + "').attr('hidden', 'hidden');\n");
                }
                else {
                    sb.append("$('#" + getLabelId() + "').prop('hidden', false);\n");
                    sb.append("$('#" + getLabelId() + "').removeAttr('hidden');\n");
                }
            }
        }
        
        if (bVisibleChanged || (bIsInitializing && !getVisible())) {
            bVisibleChanged = false;
            if (isVisible()) {
                sb.append("$('#"+id+"').css('visibility', 'visible');\n");
            }
            else {
                sb.append("$('#"+id+"').css('visibility', 'hidden');\n");
            }
            
            if (OAStr.isNotEmpty(getLabelId())) {
                if (isVisible()) {
                     sb.append("$('#"+getLabelId()+"').css('visibility', 'visible');\n");
                }
                else {
                    sb.append("$('#"+getLabelId()+"').css('visibility', 'hidden');\n");
                }
            }
        }
        
        
        
        if (bRequiredChanged || (bIsInitializing && getRequired())) {
            bRequiredChanged = false;
            if (isRequired()) {
                sb.append("$('#" + id + "').addClass('oaRequired');\n");
                sb.append("$('#" + id + "').prop('required', true);\n");
                sb.append("$('#" + id + "').attr('required', 'required');\n");
            }
            else {
                sb.append("$('#" + id + "').removeClass('oaRequired');\n");
                sb.append("$('#" + id + "').prop('required', false);\n");
                sb.append("$('#" + id + "').removeAttr('required');\n");
            }
        }

        if (bAltChanged || (bIsInitializing && OAStr.isNotEmpty(getAlt()))) {
            bAltChanged = false;
            if (OAStr.isNotEmpty(getAlt())) {
                sb.append("$('#" + id + "').attr('alt', '"+getAlt()+"');\n");
            }
            else {
                sb.append("$('#" + id + "').removeAttr('alt');\n");
            }
        }

        if (bStepChanged || (bIsInitializing && getStep() != 0)) {
            bStepChanged = false;
            if (getStep() != 0) {
                sb.append("$('#" + id + "').attr('step', "+getStep()+");\n");
            }
            else {
                sb.append("$('#" + id + "').removeAttr('step');\n");
            }
        }
        
        if (bCheckedChanged || (bIsInitializing && isChecked())) {
            bCheckedChanged = false;
            if (!bValueChangedByAjax) { 
                if (isChecked()) {
                    sb.append("$('#" + id + "').prop('checked', true);\n");
                    sb.append("$('#" + id + "').attr('checked', 'checked');\n");
                }
                else {
                    sb.append("$('#" + id + "').prop('checked', false);\n");
                    sb.append("$('#" + id + "').removeAttr('checked');\n");
                }
            }
        }
        
        if (bSpellCheckChanged || (bIsInitializing && isSpellCheck())) {
            bSpellCheckChanged = false;
            if (isChecked()) {
                sb.append("$('#" + id + "').prop('spellchecked', true);\n");
                sb.append("$('#" + id + "').attr('spellchecked', true);\n");
            }
            else {
                sb.append("$('#" + id + "').prop('spellcheck', false);\n");
                sb.append("$('#" + id + "').removeAttr('spellcheck');\n");
            }
        }
        
        if (bIsInitializing || bDataListChanged) {
            bDataListChanged = false;
            List<String> dataList = getDataList();
            if (dataList != null) {
                sb.append("$('#" + id + "DataList').remove();\n");
                sb.append("$('#" + id + "').attr('list', '"+id+"DataList');\n");
        
                String list = "<datalist id=\""+getId()+"DataList\">";
                for (String option : dataList) {
                    list += "<option value=\""+option+"\">";
                }
                list += "</datalist>";
                sb.append("$('#" + id + "').append('" + list + "');\n");
            }
        }

        
        if (bIsInitializing || bOptionsChanged) {
            bOptionsChanged = false;
            List<HtmlOption> al = getOptions();
            if (al != null) {
                String list = "";
                boolean bInOptGroup = false;
                for (HtmlOption ho : al) {
                    
                    s = ho.getLabel();
                    if (OAStr.isEmpty(s)) s = ho.getValue();
                    
                    if (ho instanceof HtmlOptionGroup) {
                        if (bInOptGroup) {
                            list += "</optgroup>";
                        }
                        bInOptGroup = true;
                        list += "<optgroup label=\""+s+"\"";
                        if (!ho.getEnabled()) list += " disabled";
                        list += ">";
                    }
                    else {
                        list += "<option value=\""+ho.getValue()+"\"";
                        if (!ho.getEnabled()) list += " disabled";
                        if (ho.getSelected()) list += " selected";
                        list += ">"+s;
                    }

                    if (ho instanceof HtmlOptionGroup) {
                    }
                    else {
                        list += "</option>";
                    }
                }
                if (bInOptGroup) {
                    list += "</optgroup>";
                }
                sb.append("$('#" + id + "').html('" + OAJspUtil.createJsString(list, '\'') + "');\n");
            }
        }
        
        
        String js = sb.toString();
        if (!bIsInitializing && lastAjaxSent != null && lastAjaxSent.equals(js)) {
            js = null;
        }
        else {
            lastAjaxSent = js;
        }
        
        this.bValueChangedByAjax = false;
        return js;
    }

    protected void getDataListJs(StringBuilder sb) {
        List<String> dataList = getDataList();
        if (dataList == null || dataList.size() == 0) return;
        if (OAStr.isEmpty(getList())) return;

        sb.append("$('#" + getList() + ").remove()");;
        
        sb.append("$('#" + id + ").append(\"");
        sb.append("<datalist id='"+getList()+"'>");
        for (String s : dataList) {
            sb.append("<option value='"+ OAJspUtil.createJsString(s, '\"') +"'>");
        }
        sb.append("</datalist>\");\n");
    }

    public String getInnerHtml() {
        return innerHtml;
    }
    public void setInnerHtml(String html) {
        bInnerHtmlChanged |= OAStr.isNotEqual(this.innerHtml, html);
        this.innerHtml = html;
    }
    
    
    protected void getFloatLabelJs(StringBuilder sb) {
        sb.append("$('#" + id + "').addClass('oaFloatLabel');\n");

        if (OAString.isEmpty(getFloatLabel())) {
            sb.append("$('#" + id + "').after('<span class=\\'active\\'></span>');\n");
        }
        else {
            sb.append("$('#" + id + "').after('<span></span>');\n");
            sb.append("$('#" + id + "').on('propertychange change keyup paste input', ");
            sb.append("function() {\n");
            sb.append("  if ($('#" + id + "').val().length > 0) $('#" + id + " + span').addClass('active');\n");
            sb.append("  else $('#" + id + " + span').removeClass('active');\n");
            sb.append("});\n");
            sb.append("if ($('#" + id + "').val().length > 0) $('#" + id + " + span').addClass(\'active\');\n");
        }
        sb.append("$('#" + id + " + span').html(\"" + OAJspUtil.createJsString(getFloatLabel(), '\"') + "\");\n");
    }

    protected String getEnabledScript() {
        StringBuilder sb = new StringBuilder(64);
        final String lblId = getLabelId();
        if (getEnabled()) {
            if (OAString.isNotEmpty(lblId)) {
                sb.append("$('#" + lblId + "').removeAttr('disabled');\n");
                sb.append("$('#" + lblId + "').prop('disabled', false);\n");
            }
            sb.append("$('#" + id + "').removeAttr('disabled');\n");
            sb.append("$('#" + id + "').prop('disabled', false);\n");
        }
        else {
            if (OAString.isNotEmpty(lblId)) {
                sb.append("$('#" + lblId + "').attr('disabled', 'disabled');\n");
                sb.append("$('#" + lblId + "').prop('disabled', true);\n");
            }
            sb.append("$('#" + id + "').attr('disabled', 'disabled');\n");
            sb.append("$('#" + id + "').prop('disabled', true);\n");
        }
        return sb.toString();
    }

/*qqqqqqq    
    protected String getVisibleScript() {
        StringBuilder sb = new StringBuilder(64);
        final String lblId = getLabelId();
        if (getVisible()) {
            if (OAString.isNotEmpty(lblId)) {
                sb.append("$('#" + lblId + "').show();\n");
            }
            else {
                // qqqqqqqq sb.append("$('#" + lblId + "').getLabel().show();\n"); //qqqqqq need to see if getLable!=null
                // qqqqqq also put in getEnabledScript
            }
            sb.append("$('#" + id + "').show();\n");
        }
        else {
            if (OAString.isNotEmpty(lblId)) {
                sb.append("$('#" + lblId + "').hide();\n");
            }
            sb.append("$('#" + id + "').hide();\n");
        }
        return sb.toString();
    }
*/
    
    public String getValidationRules() {
        return null;
    }
    
    public String getValidationMessages() {
        return null;
    }
    
    public String[] getRequiredJsNames() {
        ArrayList<String> al = new ArrayList<>();

        al.add(OAFormInsertDelegate.JS_jquery);
        al.add(OAFormInsertDelegate.JS_jquery_ui);
        al.add(OAFormInsertDelegate.JS_bootstrap);
        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }

    public String[] getRequiredCssNames() {
        ArrayList<String> al = new ArrayList<>();
        al.add(OAFormInsertDelegate.CSS_jquery_ui);
        al.add(OAFormInsertDelegate.CSS_bootstrap);

        String[] ss = new String[al.size()];
        return al.toArray(ss);
    }

    public String getAlt() {
        return this.alt;
    }
    public void setAlt(String alt) {
        this.bAltChanged |= OACompare.isNotEqual(this.alt, alt);
        this.alt = alt;
    }
    
    public int getStep() {
        return this.step;
    }
    public void setStep(int step) {
        this.bStepChanged |= this.step != step;
        this.step = step;
    }
    
    public boolean getChecked() {
        return this.checked;
    }
    public boolean isChecked() {
        return getChecked();
    }
    public void setChecked(boolean chk) {
        this.bCheckedChanged |= this.checked != chk;
        this.checked = chk;
        this.bValueChangedByAjax = false;
    }

  //qqqqqqqqqqqqqqqq add common Styles ... Fonts, borders, colors, etc 
    
    // Text values in input elements (not password) Text in <textarea> elements.
    public boolean getSpellCheck() {
        return this.spellCheck;
    }
    public boolean isSpellCheck() {
        return getSpellCheck();
    }
    public void setSpellCheck(boolean b) {
        this.bSpellCheckChanged |= this.spellCheck != b;
        this.spellCheck = b;
    }
    
    public List<HtmlOption> getOptions() {
        return alOptions;
    }
    public void add(HtmlOption option) {
        if (alOptions == null) alOptions = new ArrayList();
        getOptions().add(option);
    }

    public String getWrap() {
        return wrap;
    }
    public void setWrap(String wrap) {
        bWrapChanged |= OACompare.isNotEqual(this.wrap, wrap);
        this.wrap = wrap;
    }
    public void setWrap(WrapType wrapType) {
        setWrap(wrapType == null ? WrapType.Default.getDisplay() : wrapType.getDisplay());
    }
    
    public boolean getNeedsReloaded() {
        return bNeedsReloaded;
    }

    protected void setNeedsReloaded(boolean b) {
        this.bNeedsReloaded = b;
    }
    
    public boolean getDebug() {
        return this.bDebug;
    }
    public void setDebug(boolean b) {
        this.bDebug = b;
    }
    
}
