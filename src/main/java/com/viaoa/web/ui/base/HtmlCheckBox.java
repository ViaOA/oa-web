/*  Copyright 1999 Vince Via vvia@viaoa.com
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.viaoa.web.ui.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.viaoa.util.OAString;

/* HTML
<input id='chk' type='checkbox' name='chk' value='' checked>
*/


//qqqqqqqqqqqqqqq Create a multiple check qqqqqqqqqqqq where there can be multiple checkboxes create, to populate a list of values

/**
 * Controls an html input type=checkbox bind to hub, property show/hide, that can be bound to property enabled, that can be bound to
 * property ajax submit on change
 *
 * @author vvia
 */
public class HtmlCheckBox extends BaseComponent {
	private static final long serialVersionUID = 1L;

	private boolean bChecked;

    public HtmlCheckBox(String id) {
        super(id);
    }
	
	public boolean isChecked() {
		return bChecked;
	}
    public boolean getChecked() {
        return bChecked;
    }

	public void setChecked(boolean b) {
		this.bChecked = b;
	}

    private String lastAjaxSent;
	
	@Override
	public String getAjaxScript() {
		final StringBuilder sb = new StringBuilder(512);

		String js = super.getAjaxScript();
        if (js != null) sb.append(js);

		if (isChecked()) {
            sb.append("$('#" + id + "').prop('checked', true);\n");
			sb.append("$('#" + id + "').attr('checked', 'checked');\n");
		} else {
            sb.append("$('#" + id + "').prop('checked', false);\n");
			sb.append("$('#" + id + "').removeAttr('checked');\n");
		}

		js = sb.toString();

		if (lastAjaxSent != null && lastAjaxSent.equals(js)) {
			js = null;
		} else {
			lastAjaxSent = js;
		}

		return js;
	}

	@Override
	public void onSubmit1(OAFormSubmitEvent formSubmitEvent) {
	    if (formSubmitEvent.getCancel()) return;
	    super.onSubmit1(formSubmitEvent);
	    
        String s = getName();
        if (OAString.isEmpty(s)) s = getId();
	    
	    String[] values = formSubmitEvent.getRequest().getParameterValues(s);
	    
	    setChecked(values != null && values.length > 0);
	}
	
	
}
