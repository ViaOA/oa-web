package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.Hub;
import com.viaoa.web.html.*;

public class OATreeComboBox extends HtmlDiv implements OATableColumnInterface {
	private Hub hub;
	
    public OATreeComboBox(String selector, Hub hub, OATree table, String ppDisplay) {
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
