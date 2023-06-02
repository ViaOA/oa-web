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
package com.viaoa.web.ui;

import com.viaoa.hub.Hub;


/* HTML
<button id="blTest">ButtonList Test here</button>
 */


/* *
 * Adds a popup list to a button, and sets the button text to the selected value.
 * 
 * Example:
 * <button id="blTest">ButtonList Test here</button>
 * @author vvia
 */
public class OAButtonList extends OAPopupList {

    public OAButtonList(String id, Hub hub, String propertyPath) {
        super(id, hub, propertyPath, true);
    }
    public OAButtonList(String id, Hub hub, String propertyPath, int cols, int rows) {
        super(id, hub, propertyPath, true, cols, rows);
    }
    public OAButtonList(String id, Hub hub, String propertyPath, String width, String height) {
        super(id, hub, propertyPath, true, width, height);
    }
}

