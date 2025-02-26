package com.viaoa.web.util;

import java.awt.Window;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.hub.Hub;
import com.viaoa.object.OAObject;
import com.viaoa.template.OATemplate;
import com.viaoa.util.OAStr;
import com.viaoa.util.OAString;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.server.OASession;


//qqqqqqqqqqqqqqq a lot of this has been replaced by OAStr methods

/**
 * Utility used to convert dynamic data for internal text, so that it is html and javascript safe.
 * 
 * Data that is sent as code to the browser needs to be converted, so that the code that is created for
 * it does not break, either within the javascript or html.
 * 
 * The outer js code will wrap the text in single or double quotes, and can then have html inside of it.
 * The html can also have js code in it, so there is various encoding/escaping that needs to be done to
 * have well formed js and html that will not break the back from processing it.
 * 
 * 
 * @author vvia
 */
public class OAWebUtil {

    
    
    
    
    
    public static void debug(ServletContext application, OASession oasession, HttpServletRequest request, HttpServletResponse response) {
        System.out.print("realPath=" + application.getRealPath("test"));
        System.out.print(", servletPath=" + request.getServletPath());
        System.out.print(", pathInfo=" + request.getPathInfo());
        System.out.println(", pathTranslated=" + request.getPathTranslated());
        System.out.print("requestURI=" + request.getRequestURI());
        System.out.print(", serverName=" + request.getServerName());
        System.out.println(" serverPort=" + request.getServerPort());
        Enumeration enumx = request.getParameterNames();
        while (enumx.hasMoreElements()) {
            String name = (String) enumx.nextElement();
            String[] values = request.getParameterValues(name);
            if (values.length == 0) System.out.println("param: name=" + name + "  values=null");
            else if (values.length == 1) System.out.println("param: name=" + name + "  value=" + values[0]);
            else {
                for (int i = 0; i < values.length; i++) {
                    System.out.println("param: name=" + name + "  value[" + i + "]=" + values[i]);
                }
            }
        }
    }

    /**
     * Helper method use to make sure that text is correctly escaped to be inside of javascript code.
     * This will call createJsString(text, '"', false, false)
     */
    /* public static String createText(final String text) { String s = createJsString(text, '"', false,
     * true); return s; } public static String createText(final String text, char quoteChar) { String s
     * = createJsString(text, quoteChar, false, true); return s; }
     * 
     * 
     * public static String createHtml(final String text) { String s = createJsString(text, '"', false,
     * true); return s; } public static String createHtml(final String text, char quoteChar) { String s
     * = createJsString(text, quoteChar, false, true); return s; } */

    /* This is only needed when putting html code/data inside of something that is quoted.
     * 
     * where data = "John's" and would need to be convertedto "John&#39;s" example: <input type='text'
     * value='[data]'>
     * 
     * @return */
    //qqqqqqqq moved to OAString    
    public static String createEmbeddedHtmlString(final String text, final char htmlQuoteChar) {
        String s;
        if (htmlQuoteChar == '\'') s = OAString.convert(text, "\'", "&#39;"); // &apos;  not yet supported
        else if (htmlQuoteChar == '\"') s = OAString.convert(text, "\"", "&#34;"); // &quot;
        else s = text;
        return s;
    }

  //qqqqqqqq moved to OAString    
    /* Javascript code that is inside of JS &gt; Html example:
     * $('#id').html(" ..  <button onclick='embedded'>"); */
    public static String createEmbeddedJsString(final String text, final char jsQuoteChar) {
        return _createJsString(text, jsQuoteChar, true);
    }

//qqqqqqqq moved to OAString    
    public static String createJsString(final String text, final char jsQuoteChar) {
        return _createJsString(text, jsQuoteChar, false);
    }

// qqqqqqqqqq replace using OAStr.convertToHtml    
    /* First checks to see if it might be html. If so, returns text, else converts [NL] to <br> and < >
     * to html codes. */
    public static String convertToHtml(final String text) {
        if (text == null) return "";
        final int x = text.length();
        // check to see if there is html within the text.  If so, then dont convert.
        int cnt1 = 0;
        int cnt2 = 0;
        int cnt3 = 0;
        for (int i = 0; i < x; i++) {
            char c = text.charAt(i);
            if (c == '<') cnt1++;
            else if (c == '>') cnt2++;
            else if (c == '\n') cnt3++;
        }
        if (cnt1 > 0 && cnt1 == cnt2) return text; // guessing that this is already html

        if (cnt1 == 0 && cnt2 == 0 && cnt3 == 0) return text;

        StringBuilder sb = new StringBuilder(x + (3 * (cnt1 + cnt2 + cnt3)));

        for (int i = 0; i < x; i++) {
            char ch = text.charAt(i);
            switch (ch) {
            case '\r':
                break;
            case '\n':
                sb.append("<br>");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '\'':
                sb.append("&apos;");
                break;
            case '\"':
                sb.append("&quot;");
                break;
            default:
                sb.append(ch);
            }
        }
        return sb.toString();

    }

    
//qqqqqqqqqqqqqq moved to OAString    
    /* Converts a string to be used inside of a codegen Javascript screen
     * 
     * @param text
     * 
     * @param jsQuoteChar the char used for quoting the string
     * 
     * @param bIsJsCodeEmbeddedInHtml true if this is for js string that will be embedded inside of html
     * 
     * @param bMakeHtmlSafe if text should be checked to see if < or > should be encoded.
     * 
     * @return string that can be put into a codegen js string and quoted with quoteChars */
    protected static String _createJsString(final String text, final char jsQuoteChar, final boolean bIsJsCodeEmbeddedInHtml) {
        if (text == null) return "";
        final int x = text.length();
        StringBuilder sb = null;

        for (int i = 0; i < x; i++) {
            char ch = text.charAt(i);

            if (ch == '\r' || ch == '\n' || ch == '\\' || ch == jsQuoteChar || (bIsJsCodeEmbeddedInHtml && (ch == '\'' || ch == '\"'))) {
                if (sb == null) {
                    sb = new StringBuilder(x + 4);
                    if (i > 0) sb.append(text.substring(0, i));
                }

                if (ch == '\'') {
                    if (bIsJsCodeEmbeddedInHtml) sb.append("\\x27"); // x27 = "\'"
                    //was if (bIsJsCodeEmbeddedInHtml) sb.append("\\x5Cx27");  //  x5C = "\"   x27 = "\'"
                    else sb.append("\\" + jsQuoteChar);
                }
                else if (ch == '\"') {
                    if (bIsJsCodeEmbeddedInHtml) sb.append("\\x22"); // x22 = "\""
                    //was if (bIsJsCodeEmbeddedInHtml) sb.append("\\x5Cx22");  //  x5C = "\"   x22 = "\""
                    else sb.append("\\" + jsQuoteChar);
                }
                else if (ch == '<') {
                    sb.append("&lt;");
                }
                else if (ch == '>') {
                    sb.append("&gt;");
                }
                else if (ch == '\n') {
                    if (!bIsJsCodeEmbeddedInHtml) sb.append("\\n");
                    else sb.append("\\n"); //  \n
                    //was else sb.append("\\x5Cn"); //  \n
                }
                else if (ch == '\r') {
                    // no-op
                }
                else if (ch == '\\') {
                    if (!bIsJsCodeEmbeddedInHtml) sb.append("\\\\");
                    else sb.append("\\");
                    //was: else sb.append("\\x5C\\x5C"); // x5C = "\"
                }
            }
            else {
                if (sb != null) sb.append(ch);
            }
        }

        if (sb == null) {
            return text;
        }
        return sb.toString();
    }

/*qqqqqq is this used?    
    public static Window getWindow(OAWebComponent component) {
        // TODO Auto-generated method stub
        return null;
    }
*/
    
    
//qqqqqqqqqqqqqqqqq use OAStr.escapeJSON    
    /**
     * Convert json string value to a properly escaped string.
     * <p>
     * This will escape all double chars to \"
     * <p>
     * NOTE: In JSON (JavaScript Object Notation), both keys (names) and string values are represented
     * using double quotes ("). Single quotes (') are not valid for defining keys or string values in
     * JSON. The JSON specification specifies that keys and string values must be enclosed in double
     * quotes.
     */
    public static String escapeJsonString(final String text) {
        if (text == null) return null;

        final char jsonQuoteChar = '\"';

        final int x = text.length();
        StringBuilder sb = null;

        for (int i = 0; i < x; i++) {
            
            char ch = text.charAt(i);

            boolean bNeedsBackSlash = false;
            boolean bUnicode = false;
            
            switch (ch) {
            case '"':
                bNeedsBackSlash = true;
                break;
            case '\\':
                bNeedsBackSlash = true;
                break;
            case '\b':
                bNeedsBackSlash = true;
                ch = 'b';
                break;
            case '\f':
                bNeedsBackSlash = true;
                ch = 'f';
                break;
            case '\n':
                bNeedsBackSlash = true;
                ch = 'n';
                break;
            case '\r':
                bNeedsBackSlash = true;
                ch = 'r';
                break;
            case '\t':
                bNeedsBackSlash = true;
                ch = 't';
                break;
            default:
                if (Character.isISOControl(ch)) {
                    // Any other control characters are escaped using Unicode escapes.
                    bUnicode = true;
                }
            }

            if (bNeedsBackSlash || bUnicode) {
                if (sb == null) {
                    sb = new StringBuilder(x + 4);
                    if (i > 0) sb.append(text.substring(0, i));
                }
                
                if (bUnicode) sb.append(String.format("\\u%04x", (int) ch));
                else sb.append("\\");
            }
            if (sb != null) sb.append(ch);
        }

        if (sb == null) {
            return text;
        }
        return sb.toString();
    }

    /* qqq public static String getCalcToolTipText(OAHtmlComponent comp, Hub h) { if (comp == null)
     * return null; if (h == null) { return comp.getCalcToolTipText(); } Object obj = h.getAO(); if
     * (!(obj instanceof OAObject)) { return comp.getCalcToolTipText(); }
     * 
     * String tt = comp.getToolTipTemplate(); if (OAStr.isEmpty(tt)) { return comp.getCalcToolTipText();
     * }
     * 
     * OATemplate oat = new OATemplate(tt); String ttp = oat.process((OAObject) obj);
     * 
     * return ttp; } */

}
/* if (ch == '&') { sb.append("&amp;"); } else if (ch == '"') { sb.append("&#34;"); // &quot; } else if
 * (ch == '\'') { sb.append("&#39;"); // &apos; not yet supported } */
