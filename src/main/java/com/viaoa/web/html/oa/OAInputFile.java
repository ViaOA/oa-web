package com.viaoa.web.html.oa;

import com.viaoa.hub.*;
import com.viaoa.web.html.input.InputText;

/**
 */
public class OAInputFile extends InputText implements OATableColumnInterface {

	private Hub hub;
    public OAInputFile(String elementIdentifier, Hub hub, String propName, int size) {
        super(elementIdentifier);
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
