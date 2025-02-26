package com.viaoa.web.html.jquery;

import com.viaoa.util.OAString;
import com.viaoa.web.html.HtmlElement;

/**
 * Turn an html element into a popup.
 * 
 * It can be triggered by visible or by clicking another element.
 * 
 * @author vvia
 */
public class JqPopup extends HtmlElement {
    
    protected String clickId;
    protected String top, right, bottom, left;

    private String type = "'fade'";
    
    
    public JqPopup(String selector) {
        super(selector);
    }

    public JqPopup(String selector, String clickId) {
        super(selector);
        this.clickId = clickId;
        setup();
    }

   public JqPopup(String id, String top, String right, String bottom, String left) {
        this(id, null, top, right, bottom, left);
    }
    
    public JqPopup(String id, String clickId, String top, String right, String bottom, String left) {
        super(id);
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.clickId = clickId;
        setup();
    }
    
    protected void setup() {
        addClass("oaPopup");
        addClass("oaShadow");
        setVisible(false);
    }
    
    @Override
    public boolean isChanged() {
        return false;
    }

    
    @Override
    protected String getInitializeScript() {
        StringBuilder sb = new StringBuilder(2048);
        lastAjaxSent = null;
        
        String css = "";
        String topx = top;
        String leftx = left;
        
        if (OAString.isEmpty(top) && OAString.isEmpty(bottom)) {
            // center
            sb.append("$('#"+getId()+"').css({transform: 'translate(-50%, -50%)'});"); 
            topx = "50vh";
            leftx = "50vw";
        }

        if (topx != null && OAString.isNumber(topx)) topx += "px";
        if (leftx != null && OAString.isNumber(leftx)) leftx += "px";
        if (bottom != null && OAString.isNumber(bottom)) bottom += "px";
        if (right != null && OAString.isNumber(right)) right += "px";
        
        
        if (OAString.isEmpty(topx) && OAString.isEmpty(bottom)) topx = "0";
        
        if (OAString.isNotEmpty(topx)) {
            if (topx.charAt(0) == '0') type = "'slide', {direction: 'up'}";
            css = "top:'"+topx + "', ";
        }
        else {
            if (bottom.charAt(0) == '0') type = "'slide', {direction: 'down'}";
            css = "bottom:'"+bottom + "', ";
        }

        String rightx = right;
        if (OAString.isEmpty(rightx) && OAString.isEmpty(leftx)) rightx = "0";
        
        if (OAString.isNotEmpty(right)) {
            if (right.charAt(0) == '0') type = "'slide', {direction: 'right'}";
            css += "right:'"+right+"'";
        }
        else {
            if (leftx.charAt(0) == '0') type = "'slide', {direction: 'left'}";
            css += "left:'"+leftx+"'";
        }

        sb.append("$('#"+getId()+"').css({"+css+"});\n");
        
        if (OAString.isNotEmpty(clickId)) {
            sb.append("$('#"+clickId+"').click(function() {\n");
            sb.append("  if ($('#"+getId()+"').is(':visible')) $('#"+getId()+"').hide("+type+", 325); else $('#"+getId()+"').show("+type+", 325);return false;}\n");
            sb.append(");\n");
        }
        
        String js = sb.toString();
        return js;
    }

    private String lastAjaxSent;
    
    @Override
    protected String getAjaxScript(final boolean bIsInitializing) {
        StringBuilder sb = new StringBuilder(256);
        
        if (getVisible()) {
            sb.append("if (!$('#"+getId()+"').is(':visible')) $('#"+getId()+"').show("+type+", 325);\n");
        }
        else {
            sb.append("if ($('#"+getId()+"').is(':visible')) $('#"+getId()+"').hide("+type+", 325);\n");
        }

        String js = sb.toString();
        if (lastAjaxSent != null && lastAjaxSent.equals(js)) js = null;
        else lastAjaxSent = js;
        
        return js;
    }

}
