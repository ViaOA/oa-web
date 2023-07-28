/* Copyright 1999 Vince Via vvia@viaoa.com Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed
 * to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License. */
package com.viaoa.web.html;

import java.io.OutputStream;
import java.util.*;

import com.viaoa.web.html.form.OAFormInsertDelegate;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.html.form.OAFormSubmitEvent;
import com.viaoa.web.server.OASession;
import com.viaoa.web.util.OAJspUtil;
import com.viaoa.object.OAObject;
import com.viaoa.util.*;


// https://html.spec.whatwg.org/#toc-editing    

//FORM: <dialog>
//SESSION: browser history    
//drag and drop    
//OA add duration    
//data-*     


/**
 * The main "super" class for controlling any HTML element.
 * <p>
 * The HTML element design does not map cleanly to OO classes using inheritance, etc.<br>
 * OA uses a composite design, where this class includes all of the functionality needed by all components,
 * and then each component (java class) can "have a" OAHtmlComponent to do the work for it's subset of methods.
 * <p>
 * This is used inside of  HtmlElement (base class for all OA web components), to handle all html element features.<br>
 * <p>
 * <em>"HtmlElement has an OAHtmlComponent"</em>, and overrides the OAHtmlComponent public methods and 
 * makes them protected in HtmlElement. Since they are protected in HtmlElement and cant be directly called,
 * just call HtmlElement.getOAHtmlComponent.<i>methodName</I>, which is a public method that HtmlElement has overwritten.
 * <p>
 * This is designed to help hide methods (by making "public" here, but "protected" in HtmlElement), 
 * so that the OA web components appear to only have it's necessary subset for the html element that it
 * represents.
 * <p>
 * Notes: if a form component is disabled, then data is not sent to server during form submit.    
 *
 * 
 * @author vvia
 */
public class OAHtmlComponent {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected OAForm form;
    
    protected String name;
    private boolean bDebug;

    // true if the page needs refreshed (to call oaForm.getScript)
    private boolean bNeedsRefreshed; 
    
    
    // see: onSubmitLoadValues  that loads submitted values.
    public static enum FormElementType {
        Text(EventType.OnBlur),   // value of input/text
        TextArea(EventType.OnBlur),   // value of input/text
        StyledTextArea(EventType.OnBlur),   // value of input/text
        Range(EventType.OnChange),  // value of input/text
        Checkbox(EventType.OnChange),  // value sent on submit
        Radio(EventType.OnChange),  // value sent on submit
        Button(EventType.OnClick),  // value is the button text
        Submit(null),
        Reset(null),
        Image(EventType.OnClick),
        File(EventType.OnBlur),   // does not use value
        Select(EventType.OnChange); // selected option value 
        
        private EventType eventType;
        
        FormElementType(EventType et) {
            this.eventType = et;
        }
        
        public EventType getDefaultEventType() {
            return eventType;
        }
        
        public boolean getUsesValue() {
            boolean b = (this != File);
            return b;
        }
        public boolean getUsesChecked() {
            boolean b = (this == Radio || this == Checkbox); 
            return b;
        }
        
    }
    protected FormElementType formElementType;
    
    public FormElementType getFormElementType() {
        if (formElementType == null) {
            InputType it = getInputType();
            if (it == null) return null;
            this.formElementType = it.getFormElementType();
        }
        return this.formElementType;
    }
    public void setFormElementType(FormElementType fet) {
        if (this.formElementType != fet) setNeedsRefreshed(true);
        this.formElementType = fet;
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
        Button(FormElementType.Button),
        CheckBox(FormElementType.Checkbox),
        Color(FormElementType.Text),
        Date(FormElementType.Text),
        DateTimeLocal("datetime-local", FormElementType.Text),
        Email(FormElementType.Text),
        File(FormElementType.File),
        Hidden(FormElementType.Text),
        Image(FormElementType.Image),
        Month(FormElementType.Text),
        Number(FormElementType.Text),
        Password(FormElementType.Text),
        Radio(FormElementType.Radio),
        Range(FormElementType.Range),
        Reset(FormElementType.Reset),
        Search(FormElementType.Text),
        Submit(FormElementType.Submit),
        Tel(FormElementType.Text),
        Text(FormElementType.Text),
        Time(FormElementType.Text),
        Url(FormElementType.Text),
        Week(FormElementType.Text);
        
        private String display;
        private FormElementType formElementType;
        
        InputType() {
            display = name().toLowerCase();
        }

        InputType(FormElementType fet) {
            display = name().toLowerCase();
            this.formElementType = fet;
        }
        public FormElementType getFormElementType() {
            return formElementType;
            
        }
        InputType(String name, FormElementType fet) {
            this.display = name;
            this.formElementType = fet;
        }

        InputType(String name) {
            this.display = name;
        }
        
        public boolean getSubmitsByDefault() {
            return this == Submit || this == Image;
        }
        
        public String getDisplay() {
            if (display == null) return name();
            return display;
        }
        
    }
    private String type;  // input type attribute, see InputType 

    protected String value;  
    protected String[] values; 
    
    protected String labelId;  // Id of label element
    protected String floatLabel;  // display text above input control, once data has been entered.  Works with placeholder.
    protected String placeHolder; // works with input types: text, search, url, tel, email, and password
    protected String pattern;  // uses Regex to verify input.
    protected String title;  // used as a tooltip

    protected boolean bEnabled = true;
    protected boolean bReadOnly;
    protected boolean bHidden;   // uses HTML hidden attr, does not use space ... same CSS display: none ... does not take up space 
    protected boolean bVisible=true;  // uses CSS visibility:visible|hidden, ... does take up space
    
    protected boolean bRequired;
    private boolean bFocus;
    protected String forwardUrl;
    protected boolean bSubmit, bAjaxSubmit;
    protected String toolTipText;
    
    private boolean bIsPlainText;  // true if the text is not HTML
    private String autoComplete; // To disable, set to "off"

    protected boolean bMultiple;  // select, email, file
    private String accept;  // file
    private String capture;  // file
    protected int maxFileSize; // file
    protected boolean bFileUploaded;  // for files
    
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
    
    protected String height; // ex:  200px,  12em
    protected String width; // ex:  200px,  12em
    protected String minHeight; // ex:  200px,  12em
    protected String minWidth; // ex:  200px,  12em
    protected String maxHeight; // ex:  200px,  12em
    protected String maxWidth; // ex:  200px,  12em
    
    // number of chars for text, password
    protected int minLength;
    protected int maxLength;

    // types: text, search, tel, url, email, and password ... also used for select, for number of rows
    protected int size;


    protected int cols;  // textarea
    protected int rows;  // textarea
    
    // input min/max values
    protected String min;
    protected String max;
    
    protected int imageHeight;
    protected int imageWidth;
    
    protected boolean checked;
    protected boolean spellCheck;
    
    private List<HtmlOption> alOptions;  // list of options for a select
    
    protected char chAccessKey;  // hot-key
    protected int tabIndex = -2;  // valid values are >= -1
    protected boolean bUseTabIndex;  // flag to know if tabIndex is used. Auto set to true if tabIndex is set.

    protected String href;
    protected String target;
    
    // overflow: visible|hidden|clip|scroll|auto|initial|inherit;
    // https://www.w3schools.com/cssref/pr_pos_overflow.php
    public static enum OverflowType {
        Default("visible"),
        Visible(), // (default) shows all, and will go outside of boundaries
        Hidden(),  // cuts off, can have program scrolling
        Clip(),   // cuts off, no program scrolling
        Scroll(),  // Hort & Vert scrollbars
        Auto(),   // only scrollbars that are needed
        Initial(),
        Inherit();
        
        private String displayValue;  // value
        
        OverflowType() {
            displayValue = name().toLowerCase();
        }
        
        OverflowType(String val) {
            this.displayValue = val;
        }
        
        public String getDisplay() {
            if (displayValue == null) return name();
            return displayValue;
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
        Unknown(),
        OnBlur(),
        OnClick(true),
        OnDoubleClick(true),
        OnChange(),
        OnContextMenu(),
        OnFocus(),
        OnInput(),
        OnSearch();
        
        private String display;
        private boolean bUsesKeys;

        EventType() {
            this(false);
        }
        EventType(boolean usesKeys) {
            this.bUsesKeys = usesKeys;
            display = name().toLowerCase();
        }
        
        EventType(String name) {
            this.display = name;
        }
        
        public String getDisplay() {
            if (display == null) return name();
            return display;
        }
    
        public boolean getUsesKeys() {
            return this.bUsesKeys;
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
    protected String step;
    protected String innerHtml;
    
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

    // off, soft (default), hard.  Can also use: CSS overflow-wrap + normal, break-word and anywhere 
    protected String wrap;  
    

    // internal flags
    protected String lastAjaxSent;
    private boolean bToolTipChanged;
    private boolean bToolTipChangedInit;
    private boolean bFloatLabelChanged;
    private boolean bPlaceHolderChanged;
    private boolean bPatternChanged;
    private boolean bTitleChanged;
    private boolean bRequiredChanged;
    private boolean bEnabledChanged;
    private boolean bReadOnlyChanged;
    private boolean bHiddenChanged;
    private boolean bVisibleChanged;
    private boolean bValueChangedByAjax;
    private boolean bDataListChanged;
    private boolean bCursorChanged;
    private boolean bAutoCompleteChanged; 
    private boolean bAcceptChanged;
    private boolean bCaptureChanged;
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
    private boolean bAccessKeyChanged;
    private boolean bHrefChanged;
    private boolean bTargetChanged;
    
    
    public OAHtmlComponent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        if (OAStr.isNotEqualNullEqualsBlank(this.id, id)) setNeedsRefreshed(true);
        this.id = id;
    }
    
    public OAForm getForm() {
        return this.form;
    }
    public void setForm(OAForm form) {
        if (this.form != form) setNeedsRefreshed(true);
        this.form = form;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (OAStr.isNotEqualNullEqualsBlank(this.name, name)) setNeedsRefreshed(true);
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        if (OAStr.isNotEqualNullEqualsBlank(this.type, type)) setNeedsRefreshed(true);
        this.type = type;
    }
    public void setType(InputType type) {
        setType(type == null ? InputType.Text.getDisplay() : type.getDisplay());
    }
    
    public String getLabelId() {
        return labelId;
    }
    public void setLabelId(String id) {
        if (OAStr.isNotEqualNullEqualsBlank(this.labelId, id)) setNeedsRefreshed(true);
        this.labelId = id;
    }
    
    /**
     * Label that will appear above a text field once 
     * data has been entered.   
     * @see placeholder
     */
    public String getFloatLabel() {
        return this.floatLabel;
    }
    public void setFloatLabel(String floatLabel) {
        this.bFloatLabelChanged |= OAStr.isNotEqualNullEqualsBlank(this.floatLabel, floatLabel);
        if (bFloatLabelChanged && OAStr.isNotEmpty(this.floatLabel)) setNeedsRefreshed(true);
        this.floatLabel = floatLabel;
    }

    public String getPlaceHolder() {
        return this.placeHolder;
    }
    public void setPlaceHolder(String placeHolder) {
        this.bPlaceHolderChanged |= OAStr.isNotEqualNullEqualsBlank(this.placeHolder, placeHolder);
        this.placeHolder = placeHolder;
    }
    
    public String getPattern() {
        return this.pattern;
    }
    public void setPattern(String pattern) {
        this.bPatternChanged |= OAStr.isNotEqualNullEqualsBlank(this.pattern, pattern);
        if (bPatternChanged) setNeedsRefreshed(true);
        this.pattern = pattern;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.bTitleChanged |= OAStr.isNotEqualNullEqualsBlank(this.title, title);
        this.title = title;
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
     *  Space is reserved on the page. Uses CSS visibility: visible|hidden
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
        if (OAStr.isEqual(this.value, value, false, true)) return;
        this.value = value;
        this.bValueChangedByAjax = false;
    }
    public boolean getValueChangedByAjax() {
        return this.bValueChangedByAjax;
    }

    public String[] getValues() {
        return this.values;
    }
    public void setValues(String[] values) {
        if (this.values == values) return;
        if (OACompare.isEqual(this.values, values)) return;
        this.values = values;
        this.bValueChangedByAjax = false;
    }
    
    
    public String getForwardUrl() {
        return this.forwardUrl;
    }
    public void setForwardUrl(String forwardUrl) {
        if (OAStr.isNotEqualNullEqualsBlank(this.forwardUrl, forwardUrl)) setNeedsRefreshed(true);
        this.forwardUrl = forwardUrl;
    }

    public boolean getSubmit() {
        return bSubmit;
    }
    public void setSubmit(boolean b) {
        if (this.bSubmit != b) setNeedsRefreshed(true);
        bSubmit = b;
    }

    public boolean getAjaxSubmit() {
        return bAjaxSubmit;
    }
    public void setAjaxSubmit(boolean b) {
        if (this.bAjaxSubmit != b) setNeedsRefreshed(true);
        bAjaxSubmit = b;
    }

    /**
     * Set the bootstrap tooltip.<br>
     * <p>
     * 
     * @see #setTitle(String)
     */
    public String getToolTip() {
        return this.toolTipText;
    }
    public void setToolTip(String toolTip) {
        this.bToolTipChanged |= OAStr.isNotEqualNullEqualsBlank(this.toolTipText, toolTip);
        this.toolTipText = toolTip;
    }

    public String getToolTipText() {
        return getToolTip();
    }
    public void setToolTipText(String toolTip) {
        setToolTip(toolTip);
    }

    public void reset() {
        value = null;
        lastAjaxSent = null;
        bValueChangedByAjax = false;
    }
    
    public boolean isChanged() {
        return false;
    }

    
    /**
     * Called when parsing the request data for a multipart form that has an Input type=File.
     * @param contentLength is the total length of the submit, not just the file being uploaded. 
     * @return null to ignore.
     */
    public OutputStream onSubmitGetFileOutputStream(OAFormSubmitEvent formSubmitEvent, String fileName, String contentType) {
        bFileUploaded = true;
        return null;
    }
    
    // calls all components
    public void onSubmitPrecheck(final OAFormSubmitEvent formSubmitEvent) {
        this.bValueChangedByAjax = formSubmitEvent.getAjax();
    }

    // calls enabled components
    public void onSubmitBeforeLoadValues(final OAFormSubmitEvent formSubmitEvent) {
    }

    // calls enabled components
    public void onSubmitAfterLoadValues(final OAFormSubmitEvent formSubmitEvent) {
    }
    
    /** 
     * Called by OAForm.processComponentRequest to have a Component respond to a http or ajax request.
     */
    public String onGetJson(OASession session) {
        return "";
    }
    
    public String getCalcName() {
        String s = getName();
        if (OAString.isEmpty(s)) s = getId();
        return s;
    }
    
    // all enabled components called
    public void onSubmitLoadValues(final OAFormSubmitEvent formSubmitEvent) {
        if (formSubmitEvent.getCancel()) return;
        if (!getEnabled()) return; // disabled components do not get submitted.  Note: readony do
        
        String s = getCalcName();
        
        if (OAStr.isEmpty(s)) return;
        
        String[] ss = formSubmitEvent.getNameValueMap().get(s);
        final FormElementType formElementType = getFormElementType();
        if (formElementType != null) {
            switch (formElementType) {
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
            case Submit:
            case Reset:
                break;
            case Select:
                setValues(ss);
                if (ss != null && ss.length == 1) setValue(ss[0]);
                else setValue(null);
                
                List<HtmlOption> al = getOptions();
                if (al != null) {
                    for (HtmlOption ho : al) {
                        if (ho instanceof HtmlOptionGroup) {
                            continue;
                        }
                        boolean bx = false;
                        if (ss != null) {
                            for (String sx : ss) {
                                if (OAStr.isEqual(ho.getValue(), sx)) {
                                    bx = true;
                                    break;
                                }
                            }
                        }
                        bOptionsChanged |= (ho.getSelected() != bx);
                        ho.setSelected(bx);
                    }
                } 
            case File:
                // value not used, multipart-form processes the uploaded file(s) 
                break;
            case Text:
            case TextArea:
            case StyledTextArea:
            case Range:
            default:
                setValue(ss == null ? null : ss.length==0 ? null : ss[0]);
                break;
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

    /**
     * id for &lt;datalist&gt;
     */
    public String getList() {
        return list;
    }
    public void setList(String listId) {
        bListChanged |= OAStr.isNotEqualNullEqualsBlank(this.list, listId);
        this.list = listId;
    }

    /**
     * Used for autocomplete, to show list of available suggested values.
     */
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
        if (name.equals("width")) this.width = value;
        if (name.equals("height")) this.height = value;
        
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

    public int getRows() {
        return this.rows;
    }
    public void setRows(int val) {
        bLengthsChanged |= (this.rows != val);
        this.rows = val;
    }

    public int getCols() {
        return this.cols;
    }
    public void setCols(int val) {
        bLengthsChanged |= (this.cols != val);
        this.cols = val;
    }
    
    public String getMin() {
        return this.min;
    }
    public void setMin(String val) {
        bMinChanged |= OAStr.isNotEqualNullEqualsBlank(this.min, val);
        this.min = val;
    }

    public String getMax() {
        return this.max;
    }
    public void setMax(String val) {
        bMaxChanged |= OAStr.isNotEqualNullEqualsBlank(this.max, val);
        this.max = val;
    }

    public int getMaxFileSize() {
        return this.maxFileSize;
    }
    public void setMaxFileSize(int val) {
        bNeedsRefreshed |= val != this.maxFileSize;
        this.maxFileSize = val;
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
        bImageChanged |= OAStr.isNotEqualNullEqualsBlank(this.source, val);
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
        if (OAStr.isNotEqualNullEqualsBlank(this.inputMode, mode)) setNeedsRefreshed(true);
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
        if (OAStr.isNotEqualNullEqualsBlank(this.confirmMessage, msg)) setNeedsRefreshed(true);
        confirmMessage = msg;
    }

    public String getCursor() {
        return cursor;
    }
    public void setCursor(String cursorName) {
        bCursorChanged |= OAStr.isNotEqualNullEqualsBlank(this.cursor, cursorName);
        this.cursor = cursorName;
    }
    public void setCursor(CursorType cursorType) {
        setCursor(cursorType == null ? CursorType.Default.getDisplay() : cursorType.getDisplay());
    }

    public String getCalcEventName() {
        String name = getEventName();
        if (OAStr.isNotEmpty(name)) return name;
        
        FormElementType fet = getFormElementType();
        if (fet == null) {
            InputType it = getInputType();
            if (it != null) fet = it.getFormElementType();
        }
        if (fet != null) {
            EventType et = fet.getDefaultEventType();
            if (et != null) name = et.getDisplay();
        }
        // if (OAStr.isEmpty(name)) name = EventType.OnClick.getDisplay();
        return name;
    }
    
    /** 
     * The name the event that this component responds to.
     */
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String name) {
        if (OAStr.isNotEqualNullEqualsBlank(this.eventName, name)) setNeedsRefreshed(true);
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
        if (this.bIsPlainText != b) setNeedsRefreshed(true);
        bIsPlainText = b;
    }

    public String getAutoComplete() {
        return this.autoComplete;
    }
    /**
     * To disable, set to "off" 
     * <p>
     * on (default), off, email, name, tel, credit-card, password, etc
     * (not all work)
     * 
     */
    public void setAutoComplete(String val) {
        bAutoCompleteChanged |= OAStr.isNotEqualNullEqualsBlank(this.autoComplete, val);
        this.autoComplete = val;
    }

    /** 
     * Types of files that can be accepted for file uploading.
     * <p> 
     * examples:
     * accept="image/png" or accept=".png
     * accept="image/png, image/jpeg" or accept=".png, .jpg, .jpeg"
     * accept="image/*"
     * accept=".doc,.docx,.xml,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
     * 
     */
    public String getAccept() {
        return this.accept;
    }
    public void setAccept(String val) {
        bAcceptChanged |= OAStr.isNotEqualNullEqualsBlank(this.accept, val);
        this.accept = val;
    }

    /**
        Used with input type=file attribute access, 
        values "user", "environment"
     */
    public String getCapture() {
        return this.capture;
    }
    public void setCapture(String val) {
        bCaptureChanged |= OAStr.isNotEqualNullEqualsBlank(this.capture, val);
        this.capture = val;
    }
    
    public boolean getMultiple() {
        return this.bMultiple;
    }
    public void setMultiple(boolean b) {
        if (this.bMultiple != b) setNeedsRefreshed(true);
        this.bMultiple = b;
    }
    
    
    /**
     * Called by OAForm.
     */
    public void beforeGetScript() {
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
    
    public String getInitializeScript() {
        lastAjaxSent = null;
        bValueChangedByAjax = false;
        String s = _getInitializeScript();
        setNeedsRefreshed(false);
        return s;
    }

    protected String _getInitializeScript() {
        final StringBuilder sb = new StringBuilder(1024);

        final FormElementType formElementType = getFormElementType();        
        
        String s = getCalcName();
        sb.append("$('#" + id + "').attr('name', '"+s+"');\n");

        s = getType();
        if (OAStr.isNotEmpty(s)) sb.append("$('#" + id + "').attr('type', '"+s+"');\n");
        

        final InputType inputType = getInputType();

        if (inputType != null) {
            sb.append("$('#" + id + "').blur(function() {$(this).removeClass('oaError');}); \n");
        }
        
        
        if (inputType != null && (getSubmit() || getAjaxSubmit() || inputType.getSubmitsByDefault())) {
            sb.append("$('#" + id + "').addClass('oaSubmit');\n");
        }

        if (getMultiple()) sb.append("$('#" + id + "').attr('multiple', 'multiple');\n");
        else if (formElementType == FormElementType.File) {
            sb.append("$('#" + id + "').removeAttr('multiple');\n");
        }

        if (getUseTabIndex()) {
            sb.append("$('#" + id + "').attr('tabindex', '"+getTabIndex()+"');\n");
        }
        
        String confirm = getConfirmMessage();
        if (OAString.isNotEmpty(confirm)) {
            confirm = OAJspUtil.createJsString(confirm, '\'');
            confirm = "if (!window.confirm('"+confirm+"')) return false;";
        }
        else confirm = "";

        s = getLabelId();
        if (OAStr.isNotEmpty(s)) {
            sb.append("$('#" + s + "').attr('for', '"+id+"');\n");
            // note:  the property is: "htmlFor"
        }
        
        getDataListJs(sb);

        /*qqqqqq called by form, used in it's internal oasubmit method
        s = getVerifyScript();
        if (OAString.isNotEmpty(s)) {
            sb.append("\n");
            sb.append(s);
        }
        */

        String eventName = getCalcEventName();
        if (OAStr.isNotEmpty(eventName)) {
            if (eventName.startsWith("on")) eventName = eventName.substring(2);  // ex: onclick -> click
        
            if (getSubmit()) {
                sb.append("$('#" + id + "')."+ eventName + "(function(event) {\n");
                
                if (formElementType != null && formElementType.getUsesValue() && formElementType.getDefaultEventType() == EventType.OnBlur) {
                    sb.append("if (this.value === this.oaPrevValue) return false;\n");
                    sb.append("this.oaPrevValue = this.value;\n");
                }
                
                if (OAStr.isNotEmpty(confirm)) {
                    sb.append("  "+confirm);
                }
                sb.append("  $('#oacommand').val('" + id + "');\n");
                
                
                if (formElementType != null) {
                    EventType et = formElementType.getDefaultEventType();
                    if (et != null && et.getUsesKeys()) {
                        sb.append("  var keys = 'KEYS=';\n"); 
                        sb.append("  if (event.shiftKey) keys += '[SHIFT]';\n"); 
                        sb.append("  if (event.ctrlKey) keys += '[CTRL]';\n"); 
                        sb.append("  $('#oaparam').val(keys);\n");
                    }
                }
                
                sb.append("  $('#"+getForm().getId()+"').submit();\n");
                sb.append("  $('#oacommand').val('');\n");
                sb.append("  $('#oaparam').val('');\n");
                sb.append("  return false;\n");
                sb.append("});\n");
            }
            else if (getAjaxSubmit()) {
                sb.append("$('#" + id + "')."+ eventName+"(function(event) {\n");

                if (formElementType != null && formElementType.getUsesValue() && formElementType.getDefaultEventType() == EventType.OnBlur) {
                    sb.append("if (this.value === this.oaPrevValue) return false;\n");
                    sb.append("this.oaPrevValue = this.value;\n");
                }
                
                if (OAStr.isNotEmpty(confirm)) {
                    sb.append("  "+confirm);
                }
                sb.append("  $('#oacommand').val('" + id + "');\n");
                
                if (formElementType != null) {
                    EventType et = formElementType.getDefaultEventType();
                    if (et != null && et.getUsesKeys()) {  // ex: button or image
                        sb.append("  var keys = 'KEYS=';\n"); 
                        sb.append("  if (event.shiftKey) keys += '[SHIFT]';\n"); 
                        sb.append("  if (event.ctrlKey) keys += '[CTRL]';\n"); 
                        sb.append("  if (event.altKey) keys += '[ALT]';\n"); 
                        sb.append("  $('#oaparam').val(keys);\n");
                    }
                }
                
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

    public String getAjaxScript(final boolean bIsInitializing) {
        final StringBuilder sb = new StringBuilder(1024);

        String s = getValue();
        if (s == null) s = "";
        
        final FormElementType fet = getFormElementType();        

        if (fet != null && fet.getUsesValue()) {
            if (!this.bValueChangedByAjax) {
                if (fet != FormElementType.StyledTextArea) {
                    s = OAJspUtil.createJsString(s, '\'');
                    sb.append("$('#" + id + "').val('"+s+"');\n");
                    sb.append("$('#" + id + "')[0].oaPrevValue = $('#" + id + "').val();\n");
                    lastAjaxSent = null;
                }
            }
        }
        
        if (!bIsInitializing && bFileUploaded) {
            sb.append("$('#"+id+"')[0].value = null;\n");
            lastAjaxSent = null;
        }
        bFileUploaded = false;
        
        if (bIsInitializing || ((hsClassAdd != null && hsClassAdd.size() > 0) || (hsClassRemove != null && hsClassRemove.size() > 0))) {
            s = getClassJs(bIsInitializing);
            if (OAString.isNotEmpty(s)) sb.append(s);
        }
        
        s = getInnerHtml();
        if (bInnerHtmlChanged || (bIsInitializing && OAStr.isNotEmpty(s))) {
            bInnerHtmlChanged = false;
            s = OAJspUtil.createJsString(s, '\'');
            sb.append("$('#"+id+"').html('"+s+"');\n");
        }

        if (bAccessKeyChanged || (bIsInitializing && getAccessKey() != 0)) {
            bAccessKeyChanged = false;
            if (getAccessKey() != 0) {
                sb.append("$('#"+id+"').attr('accesskey', '"+getAccessKey()+"');\n");
            }
            else {
                sb.append("$('#"+id+"').removeAttr('accesskey');\n");
            }
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
        
        if (bAcceptChanged || (bIsInitializing && OAStr.isNotEmpty(getAccept()))) {
            bAcceptChanged = false;
            s = getAccept();
            
            if (OAStr.isNotEmpty(s)) {
                sb.append("$('#"+id+"').attr('accept', '"+s+"');\n");
            }
            else if (fet == FormElementType.File) {
                sb.append("$('#"+id+"').removeAttr('accept');\n");
            }
        }

        if (bCaptureChanged || (bIsInitializing && OAStr.isNotEmpty(getCapture()))) {
            bCaptureChanged = false;
            s = getCapture();
            
            if (OAStr.isNotEmpty(s)) {
                sb.append("$('#"+id+"').attr('capture', '"+s+"');\n");
            }
            else if (fet == FormElementType.File) {
                sb.append("$('#"+id+"').removeAttr('capture');\n");
            }
        }

        if (bAutoCompleteChanged || (bIsInitializing && OAStr.isNotEmpty(getAutoComplete()))) {
            bAutoCompleteChanged = false;
            s = getAutoComplete();
            if (OAStr.isNotEmpty(s)) {
                sb.append("$('#"+id+"').attr('autocomplete', '"+s+"');\n");
            }
            else if (fet == FormElementType.File) {
                sb.append("$('#"+id+"').removeAttr('autocomplete');\n");
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

        if (bHrefChanged || (bIsInitializing && OAStr.isNotEmpty(getHref()))) {
            bHrefChanged = false;
            sb.append("$('#" + id + "').attr('href', '"+getHref()+"');\n");
        }
        if (bTargetChanged || (bIsInitializing && OAStr.isNotEmpty(getTarget()))) {
            bHrefChanged = false;
            sb.append("$('#" + id + "').attr('target', '"+getTarget()+"');\n");
        }
        
        
        if (bMinChanged || (bIsInitializing && OAStr.isNotEmpty(getMin()))) {
            bMinChanged = false;
            if (OAStr.isEmpty(getMin())) {
                sb.append("$('#" + id + "').removeAttr('min');\n");
            }
            else sb.append("$('#" + id + "').attr('min', '"+getMin()+"');\n");
        }
        if (bMaxChanged || (bIsInitializing && OAStr.isNotEmpty(getMax()))) {
            bMaxChanged = false;
            if (OAStr.isEmpty(getMax())) {
                sb.append("$('#" + id + "').removeAttr('max');\n");
            }
            else sb.append("$('#" + id + "').attr('max', '"+getMax()+"');\n");
        }

        if (bPlaceHolderChanged || (bIsInitializing && OAStr.isNotEmpty(getPlaceHolder()))) {
            bPlaceHolderChanged = false;
            if (OAStr.isEmpty(getPlaceHolder())) { 
                sb.append("$('#" + id + "').removeAttr('placeholder');\n");
            }
            else {
                s = OAJspUtil.createJsString(getPlaceHolder(), '\'');
                sb.append("$('#" + id + "').attr('placeholder', '"+s+"');\n");
            }
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
                s = OAJspUtil.createJsString(getPattern(), '\'');
                sb.append("$('#" + id + "').attr('pattern', '"+s+"');\n");
            }
        }

        if (bTitleChanged || (bIsInitializing && OAStr.isNotEmpty(getTitle()))) {
            if (OAString.isEmpty(getTitle())) {
                sb.append("$('#" + id + "').removeAttr('title');\n");
            }
            else {
                s = OAJspUtil.createJsString(getTitle(), '\'');
                sb.append("$('#" + id + "').attr('title', '"+s+"');\n");
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
            String tt = getToolTipText();
            if (OAString.isNotEmpty(tt)) {
                if (bIsInitializing || !bToolTipChangedInit) {
                    bToolTipChangedInit = true;
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
                // sb.append("$('#" + id + "').attr('readonly', 'readonly');\n");
            }
            else {
                sb.append("$('#" + id + "').prop('readOnly', false);\n");
                // sb.append("$('#" + id + "').removeAttr('readonly');\n");
            }
        }
        
        // HTML hidden attribute does not take up space
        if (bHiddenChanged || (bIsInitializing && getHidden())) {
            bHiddenChanged = false;
            if (isHidden()) {
                sb.append("$('#" + id + "').prop('hidden', true);\n");
                // sb.append("$('#" + id + "').attr('hidden', 'hidden');\n");
            }
            else {
                sb.append("$('#" + id + "').prop('hidden', false);\n");
                // sb.append("$('#" + id + "').removeAttr('hidden');\n");
            }
            
            if (OAStr.isNotEmpty(getLabelId())) {
                if (isHidden()) {
                    sb.append("$('#" + getLabelId() + "').prop('hidden', true);\n");
                    // sb.append("$('#" + getLabelId() + "').attr('hidden', 'hidden');\n");
                }
                else {
                    sb.append("$('#" + getLabelId() + "').prop('hidden', false);\n");
                    // sb.append("$('#" + getLabelId() + "').removeAttr('hidden');\n");
                }
            }
        }
        
        // CSS visible=false does take up space
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
                // sb.append("$('#" + id + "').attr('required', 'required');\n");
            }
            else {
                sb.append("$('#" + id + "').removeClass('oaRequired');\n");
                sb.append("$('#" + id + "').prop('required', false);\n");
                // sb.append("$('#" + id + "').removeAttr('required');\n");
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

        if (bStepChanged || (bIsInitializing && OAStr.isNotEmpty(getStep()) ) ) {
            bStepChanged = false;
            if (OAStr.isNotEmpty(getStep())) {
                s = OAJspUtil.createJsString(getStep(), '\'');                
                sb.append("$('#" + id + "').attr('step', '"+s+"');\n");
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
                    // sb.append("$('#" + id + "').attr('checked', 'checked');\n");
                }
                else {
                    sb.append("$('#" + id + "').prop('checked', false);\n");
                    // sb.append("$('#" + id + "').removeAttr('checked');\n");
                }
            }
        }
        
        if (bSpellCheckChanged || (bIsInitializing && isSpellCheck())) {
            bSpellCheckChanged = false;
            if (isSpellCheck()) {
                sb.append("$('#" + id + "').prop('spellchecked', true);\n");
                // sb.append("$('#" + id + "').attr('spellchecked', true);\n");
            }
            else {
                sb.append("$('#" + id + "').prop('spellcheck', false);\n");
                // sb.append("$('#" + id + "').removeAttr('spellcheck');\n");
            }
        }
        
        if (bIsInitializing || bDataListChanged) {
            bDataListChanged = false;
            List<String> dataList = getDataList();
            if (dataList != null) {
                sb.append("$('#" + id + "DataList').remove();\n");
                sb.append("$('#" + id + "').attr('list', '"+id+"DataList');\n");
        
                String list = "<datalist id='"+getId()+"DataList'>";
                for (String option : dataList) {
                    option = OAJspUtil.createJsString(option, '\'');                
                    list += "<option value='"+option+"'>";
                }
                list += "</datalist>";
                sb.append("$('#" + id + "').append('" + list + "');\n");
            }
        }
        
        if (bIsInitializing || bOptionsChanged) {
            bOptionsChanged = false;
            List<HtmlOption> al = getOptions();
            if (al != null && al.size() > 0) {
                String list = getOptionsJs(al);
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

    public String getOptionsJs(final List<HtmlOption> al) {
        String list = "";
        boolean bInOptGroup = false;
        for (HtmlOption ho : al) {
            String s = ho.getLabel();
            if (OAStr.isEmpty(s)) s = ho.getValue();
            
            if (ho instanceof HtmlOptionGroup) {
                if (bInOptGroup) {
                    list += "</optgroup>";
                }
                bInOptGroup = true;
                list += "<optgroup label='"+s+"'";
                if (!ho.getEnabled()) list += " disabled";
                list += ">";
            }
            else {
                list += "<option value='"+ho.getValue()+"'";
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
        return list;
    }
    
    protected void getDataListJs(StringBuilder sb) {
        List<String> dataList = getDataList();
        if (dataList == null || dataList.size() == 0) return;
        if (OAStr.isEmpty(getList())) return;

        sb.append("$('#" + getList() + ").remove()");;
        
        sb.append("$('#" + id + ").append('");
        sb.append("<datalist id='"+getList()+"'>");
        for (String s : dataList) {
            sb.append("<option value='"+ OAJspUtil.createJsString(s, '\'') +"'>");
        }
        sb.append("</datalist>');\n");
    }

    public String getInnerHtml() {
        return innerHtml;
    }
    public void setInnerHtml(String html) {
        bInnerHtmlChanged |= OAStr.isNotEqualNullEqualsBlank(this.innerHtml, html);
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
        sb.append("$('#" + id + " + span').html('" + OAJspUtil.createJsString(getFloatLabel(), '\'') + "');\n");
    }

    protected String getEnabledScript() {
        StringBuilder sb = new StringBuilder(64);
        final String lblId = getLabelId();
        if (getEnabled()) {
            if (OAString.isNotEmpty(lblId)) {
                sb.append("$('#" + lblId + "').prop('disabled', false);\n");
                // sb.append("$('#" + lblId + "').removeAttr('disabled');\n");
            }
            sb.append("$('#" + id + "').prop('disabled', false);\n");
            // sb.append("$('#" + id + "').removeAttr('disabled');\n");
        }
        else {
            if (OAString.isNotEmpty(lblId)) {
                sb.append("$('#" + lblId + "').prop('disabled', true);\n");
                // sb.append("$('#" + lblId + "').attr('disabled', 'disabled');\n");
            }
            sb.append("$('#" + id + "').prop('disabled', true);\n");
            // sb.append("$('#" + id + "').attr('disabled', 'disabled');\n");
        }
        return sb.toString();
    }

    
    public String getValidationRules() {
        return null;
    }
    
    public String getValidationMessages() {
        return null;
    }
    
    public void getRequiredJsNames(final Set<String> hsJsName) {
        hsJsName.add(OAFormInsertDelegate.JS_jquery);
        hsJsName.add(OAFormInsertDelegate.JS_jquery_ui);
        hsJsName.add(OAFormInsertDelegate.JS_bootstrap);
    }

    public void getRequiredCssNames(final Set<String> hsCssName) {
        hsCssName.add(OAFormInsertDelegate.CSS_jquery_ui);
        hsCssName.add(OAFormInsertDelegate.CSS_bootstrap);
    }

    public String getAlt() {
        return this.alt;
    }
    public void setAlt(String alt) {
        this.bAltChanged |= OAStr.isNotEqualNullEqualsBlank(this.alt, alt);
        this.alt = alt;
    }
    
    public String getStep() {
        return this.step;
    }
    public void setStep(String step) {
        this.bStepChanged |= OAStr.isNotEqualNullEqualsBlank(this.step, step);
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
        if (alOptions == null) alOptions = new ArrayList();
        return alOptions;
    }
    public void add(HtmlOption option) {
        bOptionsChanged = true;
        getOptions().add(option);
    }
    public void addOption(HtmlOption option) {
        add(option);
    }
    public void clearOptions() {
        bOptionsChanged = true;
        getOptions().clear();
    }

    public String getWrap() {
        return wrap;
    }
    public void setWrap(String wrap) {
        bWrapChanged |= OAStr.isNotEqualNullEqualsBlank(this.wrap, wrap);
        this.wrap = wrap;
    }
    public void setWrap(WrapType wrapType) {
        setWrap(wrapType == null ? WrapType.Default.getDisplay() : wrapType.getDisplay());
    }
    
    public boolean getNeedsRefreshed() {
        return bNeedsRefreshed;
    }

    public void setNeedsRefreshed(boolean b) {
        this.bNeedsRefreshed = b;
    }
    
    public boolean getDebug() {
        return this.bDebug;
    }
    public void setDebug(boolean b) {
        this.bDebug = b;
    }

    public char getAccessKey() {
        return chAccessKey;
    }
    public void setAccessKey(char ch) {
        bAccessKeyChanged |= this.chAccessKey != ch; 
        this.chAccessKey = ch;
    }

    /**
     * Starts at zero. 
     * Set to -1 to not be including in tabbing.
     * Note: detfault is -2 (not used/needed)
     * @see #setUsesTabIndex(boolean) to turn on or off.
     */
    public int getTabIndex() {
        return tabIndex;
    }
    /**
     * Zero or greater to choose the order.  Using same number will group them together.
     * -1 is used to not include in tab navigation.
     * <p>   
     * Note, this will call setUsesTabIndex(true)
     */
    public void setTabIndex(int val) {
        bNeedsRefreshed |= this.tabIndex != val; 
        this.tabIndex = val;
        bUseTabIndex = true;
    }
    public boolean getUseTabIndex() {
        return bUseTabIndex;
    }
    public void setUseTabIndex(boolean b) {
        bNeedsRefreshed |= this.bUseTabIndex != b; 
        this.bUseTabIndex = b;
    }

    
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        bHrefChanged = OAStr.isNotEqualNullEqualsBlank(this.href, href);
        this.href = href;
    }

    
    /**
     * Used by html A (/link) for the target window.
     * <p>
     * Examples: _self, _blank, _parent, _top, or a named frame/window
     */
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        bTargetChanged = OAStr.isNotEqualNullEqualsBlank(this.target, target);
        this.target = target;
    }
    
    /**
     * called to know if a feature/method is supported.
     * Defaults to true, and is overwritten by HtmlElements. 
     */
    public boolean isSupported(String name) {
        return true;
    }
    
}
