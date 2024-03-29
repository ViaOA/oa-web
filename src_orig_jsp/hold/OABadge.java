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

import com.viaoa.hub.Hub;
import com.viaoa.util.OAString;


/**
 * creates a bootstrap badge
 * @author vvia
 */
public class OABadge extends OAHtmlElement {

    public OABadge(String id, Hub hub, String propertyPath) {
        super(id, hub, propertyPath);
        addClass("badge");
    }
}
