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
package com.viaoa.web.ui.control;

import javax.swing.JComponent;

import com.viaoa.hub.Hub;
import com.viaoa.hub.HubChangeListener;

/**
 * factory methods for OAJfcController.
 * 
 * @author vvia
 */
public class OAWebUIControllerFactory {

	public static OAJfcController create(Hub hub, JComponent comp, HubChangeListener.Type type) {
		OAJfcController jc = new OAJfcController(hub, null, null, comp, type, false, true);
		return jc;
	}

	public static OAJfcController createHubValid(Hub hub, JComponent comp) {
		OAJfcController jc = new OAJfcController(hub, null, null, comp, HubChangeListener.Type.HubValid, false, true);
		return jc;
	}

	public static OAJfcController createHubValid(Hub hub, String prop, JComponent comp) {
		OAJfcController jc = new OAJfcController(hub, null, prop, comp, HubChangeListener.Type.HubValid, false, true);
		return jc;
	}

	public static OAJfcController createAoNotNull(Hub hub, JComponent comp) {
		OAJfcController jc = new OAJfcController(hub, null, null, comp, HubChangeListener.Type.AoNotNull, false, true);
		return jc;
	}

	public static OAJfcController createAoNotNull(Hub hub, String prop, JComponent comp) {
		OAJfcController jc = new OAJfcController(hub, null, prop, comp, HubChangeListener.Type.AoNotNull, false, true);
		return jc;
	}

	public static OAJfcController createHubNotEmpty(Hub hub, JComponent comp) {
		OAJfcController jc = new OAJfcController(hub, null, null, comp, HubChangeListener.Type.HubNotEmpty, false, true);
		return jc;
	}

	// dont include extended checks

	public static OAJfcController createOnlyHubNotEmpty(Hub hub, JComponent comp) {
		OAJfcController jc = new OAJfcController(hub, null, null, comp, HubChangeListener.Type.HubNotEmpty, false, false);
		return jc;
	}

	public static OAJfcController createOnlyAoNotNull(Hub hub, JComponent comp) {
		OAJfcController jc = new OAJfcController(hub, null, null, comp, HubChangeListener.Type.AoNotNull, false, false);
		return jc;
	}

	public static OAJfcController createOnlyHubValid(Hub hub, JComponent comp) {
		OAJfcController jc = new OAJfcController(hub, null, null, comp, HubChangeListener.Type.HubValid, false, false);
		return jc;
	}

	public static OAJfcController createOnlyAoNotNew(Hub hub, JComponent comp) {
		OAJfcController jc = new OAJfcController(hub, null, null, comp, HubChangeListener.Type.AoNotNew, false, false);
		return jc;
	}

	public static OAJfcController createOnlyAoNew(Hub hub, JComponent comp) {
		OAJfcController jc = new OAJfcController(hub, null, null, comp, HubChangeListener.Type.AoNew, false, false);
		return jc;
	}

}
