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
import com.viaoa.util.OAString;

/* HTML

<input id="ptxtPassword" type="password">

*/


/**
 * Component for managing html password textfield.
 * @author vvia
 *
 */
public class OAPassword extends OATextField {
    private static final long serialVersionUID = 1L;

    public OAPassword(String id, Hub hub, String propertyPath) {
        super(id, hub, propertyPath, 0, 0);
//qqqqqqqqq passwords are not encrypted yet        
//        setConversion('P');
    }
    public OAPassword(Hub hub, String propertyPath) {
        super(hub, propertyPath, 0, 0);
//qqqqqqqqq passwords are not encrypted yet        
//        setConversion('P');
    }
    public OAPassword(String id, Hub hub, String propertyPath, int width, int maxWidth) {
        super(id, hub, propertyPath, width, maxWidth);
//qqqqqqqqq passwords are not encrypted yet        
//        setConversion('P');
    }
    public OAPassword(Hub hub, String propertyPath, int width, int maxWidth) {
        super(hub, propertyPath, width, maxWidth);
//qqqqqqqqq passwords are not encrypted yet        
//        setConversion('P');
    }

    public OAPassword(Hub hub, String propertyPath, int maxWidth) {
        super(hub, propertyPath, maxWidth);
//qqqqqqqqq passwords are not encrypted yet        
//        setConversion('P');
    }

    public OAPassword(String id) {
        super(id);
    }
    public OAPassword() {
    }

    @Override
    public String getTableEditorHtml() {
        // let cell take up all space
        width = 0;  // so that the "size" attribute wont be set        
        String s = "<input id='"+id+"' type='password' style='top:4px; left:2px; width:90%; position:absolute;'>";
        return s;
    }
}
