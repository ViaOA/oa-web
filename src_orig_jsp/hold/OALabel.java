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

import com.viaoa.hub.Hub;
import com.viaoa.object.OAObject;

/* HTML
ID: <span id="lblId"></span>


*/


public class OALabel extends OAHtmlElement {

    public OALabel(String id, Hub hub, String propertyPath, int width) {
        super(id, hub, propertyPath, width);
    }
    public OALabel(Hub hub, String propertyPath, int width) {
        super(hub, propertyPath, width);
    }

    public OALabel(String id, Hub hub, String propertyPath) {
        super(id, hub, propertyPath);
    }
    public OALabel(Hub hub, String propertyPath) {
        super(hub, propertyPath);
    }

    public OALabel(String id, Hub hub, String propertyPath, int width, int minWidth, int maxRows) {
        super(id, hub, propertyPath, width, minWidth, maxRows);
    }
    public OALabel(Hub hub, String propertyPath, int width, int minWidth, int maxRows) {
        super(hub, propertyPath, width, minWidth, maxRows);
    }

    public OALabel() {
        super();
    }
    
    @Override
    public void setMaxWidth(String val) {
        super.setMaxWidth(val);
        setOverflow("hidden");  // oahtmlelement will then add al.add("'text-overflow':'ellipsis'");
    }

    @Override
    public String getEditorHtml(OAObject obj) {
        return getRenderHtml(obj);  // no real editor
    }
    
}
