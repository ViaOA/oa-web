package com.viaoa.web.html.oa;

import java.util.Set;

import com.viaoa.hub.Hub;
import com.viaoa.lang.OAStr;
import com.viaoa.ui.controller.OAUICommandController;
import com.viaoa.web.html.*;

public class OATableComboBox extends HtmlDiv implements OATableColumnInterface {

	private Hub hub;
	private boolean closePopupRequested;
	private int maxRowCount;

	public OATableComboBox(String selector, Hub hub, OATable table, String ppDisplay) {
		super(selector);
		this.hub = hub;

		OAHtmlButton cmd = new OAHtmlButton("cmdClear", getHub(), OAUICommandController.Command.ClearAO) {
			@Override
			protected void onClickEvent() {
				super.onClickEvent();
				OATableComboBox.this.hidePopup();
			}
		};
		this.add(cmd);

		HtmlButton cmd2 = new HtmlButton("cmdClose") {
			@Override
			protected void onClickEvent() {
				super.onClickEvent();
				OATableComboBox.this.hidePopup();
			}
		};
		this.add(cmd2);
	}

	public int getMaximumRowCount() {
		return this.maxRowCount;
	}
	public void setMaximumRowCount(int count) {
		this.maxRowCount = count;
	}
	
	public void hidePopup() {
		closePopupRequested = true;
	}

	@Override
	public String getValueAsString(Hub hubFrom, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Hub<?> getHub() {
		return hub;
	}

	@Override
	public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) {
		boolean closePopup = closePopupRequested;

		String js = super.getJavaScriptForClient(hsVars, bHasChanges || closePopup);
		if (closePopup) {
			closePopupRequested = false;
			js = OAStr.concat(js, "comp.hidePopup();", "\n");
		}
		return js;
	}

}
