package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.Hub;
import com.viaoa.web.html.*;

public class OATableComboBox extends HtmlDiv implements OATableColumnInterface {
     
	private Hub hub;
	
    public OATableComboBox(String selector, Hub hub, OATable table, String ppDisplay) {
        super(selector);
        this.hub = hub;
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
}
