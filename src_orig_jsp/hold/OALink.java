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
import com.viaoa.util.*;

/**
 * Component for managing html links.
 * @author vvia
 *
 */
public class OALink extends OAHtmlElement {
    private static final long serialVersionUID = 1L;

    
    public OALink(String id) {
        super(id);
    }
    public OALink(String id, String forwardUrl) {
        super(id);
        setForwardUrl(forwardUrl);
    }
    public OALink(String id, Hub hub) {
        super(id, hub);
    }

    public OALink(String id, Hub hub, String forwardUrl) {
        super(id, hub);
        setForwardUrl(forwardUrl);
    }

    @Override
    public String getScript() {
        lastAjaxSent = null;

        StringBuilder sb = new StringBuilder(1024);
        
        String furl = getForwardUrl();
        if (!OAString.isEmpty(furl)) {
            sb.append("$('#"+id+"').attr('href', '"+furl+"');\n");
        }
        
        String confirm = getConfirmMessage();
        if (OAString.isNotEmpty(confirm)) {
            confirm = OAJspUtil.createJsString(confirm, '\"');
            confirm = "if (!window.confirm(\""+confirm+"\")) return false;";
        }
        else confirm = "";
        
        if (bSubmit || bAjaxSubmit) {
            if (bAjaxSubmit) {
                sb.append("$('#"+id+"').click(function() {"+confirm+"$('#oacommand').val('"+id+"');ajaxSubmit();return false;});\n");
            }
            else {
                sb.append("$('#"+id+"').click(function() {"+confirm+"$('#oacommand').val('"+id+"'); $('form').submit(); $('#oacommand').val(''); return false;});\n");
            }
            sb.append("$('#"+id+"').addClass('oaSubmit');\n");
        }
        
        String s = getAjaxScript();
        if (s != null) sb.append(s);
        String js = sb.toString();
        
        return js;
    }

    @Override
    public String getRenderHtml(OAObject obj) {
        String txt = getRenderText(obj);
        if (txt == null) txt = "";

        String classz = getRenderClass(obj);
        if (OAString.isEmpty(classz)) classz = "";
        else classz = " class='"+classz+"' ";
        
        String style = getRenderStyle(obj);
        if (OAString.isEmpty(style)) style = "";
        else style = " style='"+style+"' ";
        
        String s = "<a href='#' "+classz+style+getRenderOnClick(obj)+">"+txt+"</a>";
        return s;
    }
    
    
    public String getRenderText(OAObject obj) {
        String s = "Text";
        return s;
    }
    public String getRenderStyle(OAObject obj) {
        return "";
    }
    public String getRenderClass(OAObject obj) {
        return "";
    }
    public String getRenderOnClick(OAObject obj) {
        String js = "";
        String s = getProcessedConfirmMessage(obj);
        if (OAString.isNotEmpty(s)) {
            s = OAJspUtil.createEmbeddedJsString(s, '\"');
            js += "if (!window.confirm(\""+s+"\")) return false;";  
        }
        js += "$(\"#oacommand\").val(\""+id+"\");"; 
        
        js = "onClick='" + js + "'";
        
        return js;
    }
    @Override
    public String getEditorHtml(OAObject obj) {
        return getRenderHtml(obj);
    }

    
    
    
    
}
