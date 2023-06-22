package com.viaoa.web.html;

import com.viaoa.object.OAObject;
import com.viaoa.web.html.OAHtmlComponent.ValueAttributeType;

import static com.viaoa.web.html.OAHtmlComponent.InputModeType;
import static com.viaoa.web.html.OAHtmlComponent.InputType;

import java.util.List;

/*

https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#popovertarget

all <input> attributes, by type:
    https://developer.mozilla.org/en-US/docs/Web/API/HTMLInputElement

*/

// see list of attributes per element type
// https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input

/**
 * Support for HTML Input Elements.
 */
public class HtmlFormElement extends HtmlElement {

    public HtmlFormElement(String id) {
        this(id, null, null);
    }
    public HtmlFormElement(String id, InputType type) {
        this(id, type, null);
    }
    
    public HtmlFormElement(String id, InputType type, ValueAttributeType vat) {
        super(id);
        if (type != null) oaHtmlComponent.setType(type);
        else if (vat != null) {
            oaHtmlComponent.setValueAttributeType(vat);
        }
    }

    public String getName() {
        return oaHtmlComponent.getName();
    }

    public void setName(String name) {
        oaHtmlComponent.setName(name);
    }
    
    public String getType() {
        return oaHtmlComponent.getType();
    }

    /* put in constructor
    public void setType(String type) {
        oaComponent.setType(type);
    }
    public void setType(InputType type) {
        oaComponent.setType(type);
    }
    */

    public String getLabelId() {
        return oaHtmlComponent.getLabelId();
    }

    public void setLabelId(String id) {
        oaHtmlComponent.setLabelId(id);
    }

    public String getFloatLabel() {
        return oaHtmlComponent.getFloatLabel();
    }

    public void setFloatLabel(String floatLabel) {
        oaHtmlComponent.setFloatLabel(floatLabel);
    }

    public boolean getEnabled() {
        return oaHtmlComponent.getEnabled();
    }

    public boolean isEnabled() {
        return oaHtmlComponent.getEnabled();
    }

    public void setEnabled(boolean b) {
        oaHtmlComponent.setEnabled(b);
    }

    public void setFocus(boolean b) {
        oaHtmlComponent.setFocus(b);
    }

    public void setFocus() {
        oaHtmlComponent.setFocus();
    }
    
    public void reset() {
        oaHtmlComponent.reset();
    }

    public String getEditorHtml(OAObject obj) {
        return oaHtmlComponent.getEditorHtml(obj);
    }
    
    public String getTableEditorHtml() {
        return oaHtmlComponent.getTableEditorHtml();
    }
    
    public boolean getDebug() {
        return oaHtmlComponent.getDebug();
    }
    public void setDebug(boolean b) {
        oaHtmlComponent.setDebug(b);
    }
}

/* Notes: 

https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#popovertarget

Attributes: 

accept   file
alt      image
autocomplete  all except checkbox, radio, and buttons
capture  file
checked   checkbox, radio
disabled   all
height   img
list   all except hidden, password, checkbox, radio, and buttons ... Value of the id attribute of the <datalist>  
min, max   date, month, week, time, datetime-local, number, range
minlength, maxlength   text, search, url, tel, email, password
multiple   email, file
name   all
pattern   text, search, url, tel, email, password
placeholder   text, search, url, tel, email, password, number
readonly   all except hidden, range, color, checkbox, radio, and buttons
required   all except hidden, range, color, and buttons

size   **text**, search, url, tel, email, password   - display size/length of text shown
src  image
step   date, month, week, time, datetime-local, number, range   
value   all except image
width  image

all - value, name, 
file - accept, capture, multiple, readonly

image - alt, height, width, src, !value

text=search,url,tel,email (+multiple), 

text+ - minlength, maxlength, pattern, placeholder, size, readonly, autocomplete
checkbox, radio - checked, !readonly
email - autocomplete,

date - min, max, step
month - min, max, step
week - min, max, step
time - min, max, step
datetime-local - min, max, step 
number - min, max, placeholder, step
range - min, max, !readonly, !required, step

color - !readonly, !required
submit -
button - !readonly, !required

all  disable

 */
    
    
// https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#popovertarget
//         qqqqqqqqq get Attributes for various input types    
    
// qqqqqqqq have all of the others based on this
    
//  type=email multiple
// type=search, phone, 
    
// pattern - regex,  patternMismatch 
// <input name="username" type="text" value="Sasha" pattern="\w{3,16}" required>    
//  https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/RegExp    
// aria-label="3-digit prefix"
// pattern="[0-9]{4}"
//  title="4 to 8 lowercase letters"
// autocomplete
       // https://developer.mozilla.org/en-US/docs/Web/HTML/Attributes/autocomplete
    
/* file   
    <input type="file"
            name="picture"
            accept="image/*"
            capture="user" multiple>
*/
// time, date, datetime-local, range, week, color, number, tel,     
