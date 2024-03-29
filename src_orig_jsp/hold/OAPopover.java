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
package com.viaoa.web.ui.hold;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.hub.Hub;
import com.viaoa.object.OAObject;
import com.viaoa.template.OATemplate;
import com.viaoa.util.OAString;
import com.viaoa.web.ui.base.OAJspDelegate;

/**
 * Advanced tooltip that is triggered by onHover, onFocus, or onClick.
 * 
 * @author vvia
 */
public class OAPopover implements OAJspComponent, OAJspRequirementsInterface {

	private static final long serialVersionUID = 1L;

	protected Hub hub;
	protected String id;

	protected OAForm form;
	protected String lastAjaxSent;

	protected String message;
	protected OATemplate templateMessage;

	protected String title;
	protected OATemplate templateTitle;

	protected boolean bOnClick, bOnHover, bOnFocus;
	protected String location;

	public OAPopover(String id) {
		this(id, null);
	}

	public OAPopover() {
	}

	public OAPopover(String id, Hub hub) {
		this.id = id;
		this.hub = hub;
		bOnClick = true;
	}

	public OAPopover(Hub hub) {
		this.hub = hub;
		bOnClick = true;
	}

	public Hub getHub() {
		return hub;
	}

	public void setOnClick(boolean b) {
		this.bOnClick = b;
	}

	public boolean getOnClick() {
		return bOnClick;
	}

	public void setOnFocus(boolean b) {
		this.bOnFocus = b;
	}

	public boolean getOnFocus() {
		return bOnFocus;
	}

	public void setOnHover(boolean b) {
		this.bOnHover = b;
	}

	public boolean getOnHover() {
		return bOnHover;
	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void reset() {
	}

	@Override
	public void setForm(OAForm form) {
		this.form = form;
	}

	@Override
	public OAForm getForm() {
		return this.form;
	}

	@Override
	public boolean _beforeFormSubmitted() {
		return true;
	}

	@Override
	public boolean _onFormSubmitted(HttpServletRequest req, HttpServletResponse resp, HashMap<String, String[]> hmNameValue) {
		return false;
	}

	@Override
	public String _onSubmit(String forwardUrl) {
		return onSubmit(forwardUrl);
	}

	@Override
	public String onSubmit(String forwardUrl) {
		return forwardUrl;
	}

	@Override
	public String _afterFormSubmitted(String forwardUrl) {
		return afterFormSubmitted(forwardUrl);
	}

	@Override
	public String afterFormSubmitted(String forwardUrl) {
		return forwardUrl;
	}

	@Override
	public String getScript() {
		lastAjaxSent = null;

		StringBuilder sb = new StringBuilder(1024);

		//qqqqqqqqqqqqqq        
		// http://getbootstrap.com/javascript/#popovers

		String s = getAjaxScript(true);
		if (s != null) {
			sb.append(s);
		}

		String js = sb.toString();

		return js;
	}

	@Override
	public String getVerifyScript() {
		return null;
	}

	@Override
	public String getAjaxScript() {
		return getAjaxScript(false);
	}

	public String getAjaxScript(boolean bInit) {
		StringBuilder sb = new StringBuilder(1024);

		String msg = getProcessedMessage();
		if (msg == null) {
			msg = "";
		}
		msg = OAJspUtil.createJsString(msg, '\'');

		String title = getProcessedTitle();
		if (title == null) {
			title = "";
		}
		title = OAJspUtil.createJsString(title, '\'');

		String trigger = "";
		if (getOnClick()) {
			trigger = "click";
		}
		if (getOnHover()) {
			if (trigger.length() > 0) {
				trigger += " ";
			}
			trigger = "hover";
		}
		if (getOnFocus()) {
			if (trigger.length() > 0) {
				trigger += " ";
			}
			trigger = "focus";
		}

		String loc = getLocation();
		if (OAString.isEmpty(loc)) {
			loc = OAJspDelegate.LOCATION_Top;
		}

		sb.append("$('#" + id + "').data('bs.popover').options.content = '" + msg + "';\n");
		sb.append("$('#" + id + "').data('bs.popover').options.title = '" + title + "';\n");
		sb.append("$('#" + id + "').data('bs.popover').options.placement = '" + loc + "';\n");
		sb.append("$('#" + id + "').data('bs.popover').options.trigger = '" + trigger + "';\n");

		String js = sb.toString();

		if (lastAjaxSent != null && lastAjaxSent.equals(js)) {
			js = null;
		} else {
			lastAjaxSent = js;
			if (bInit) {
				// http://getbootstrap.com/javascript/#popovers
				js = "$('#" + id + "').popover({content: '" + msg + "', placement: '" + loc + "', title: '" + title + "', trigger: '"
						+ trigger + "'});\n";
			}
		}

		return js;
	}

	public void setMessage(String msg) {
		this.message = msg;
		templateMessage = null;
	}

	public String getMessage() {
		return this.message;
	}

	public String getProcessedMessage() {
		if (OAString.isEmpty(message)) {
			return message;
		}
		if (templateMessage == null) {
			templateMessage = new OATemplate();
			templateMessage.setTemplate(getMessage());
		}
		OAObject obj = null;
		if (hub != null) {
			Object objx = hub.getAO();
			if (objx instanceof OAObject) {
				obj = (OAObject) objx;
			}
		}
		String s = templateMessage.process(obj, hub, null);
		return s;
	}

	public void setTitle(String title) {
		this.title = title;
		templateTitle = null;
	}

	public String getTitle() {
		return this.title;
	}

	public String getProcessedTitle() {
		if (OAString.isEmpty(title)) {
			return title;
		}
		if (templateTitle == null) {
			templateTitle = new OATemplate();
			templateTitle.setTemplate(getTitle());
		}
		OAObject obj = null;
		if (hub != null) {
			Object objx = hub.getAO();
			if (objx instanceof OAObject) {
				obj = (OAObject) objx;
			}
		}
		String s = templateTitle.process(obj, hub, null);
		return s;
	}

	@Override
	public void setEnabled(boolean b) {
	}

	@Override
	public boolean getEnabled() {
		return true;
	}

	@Override
	public void setVisible(boolean b) {
	}

	@Override
	public boolean getVisible() {
		return true;
	}

	@Override
	public String getForwardUrl() {
		return null;
	}

	/**
	 * @see OAJspDelegate for locations.
	 */
	public void setLocation(String loc) {
		this.location = loc;
	}

	public String getLocation() {
		return this.location;
	}

	public String[] getRequiredJsNames() {
		ArrayList<String> al = new ArrayList<>();

		al.add(OAJspDelegate.JS_jquery);
		al.add(OAJspDelegate.JS_bootstrap);

		String[] ss = new String[al.size()];
		return al.toArray(ss);
	}

	@Override
	public String[] getRequiredCssNames() {
		ArrayList<String> al = new ArrayList<>();

		al.add(OAJspDelegate.CSS_bootstrap);

		String[] ss = new String[al.size()];
		return al.toArray(ss);
	}

	@Override
	public String getEditorHtml(OAObject obj) {
		return null;
	}

	@Override
	public String getRenderHtml(OAObject obj) {
		return null;
	}

	@Override
	public void _beforeOnSubmit() {
	}
}
