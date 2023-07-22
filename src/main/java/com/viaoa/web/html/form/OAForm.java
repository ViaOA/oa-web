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
package com.viaoa.web.html.form;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.process.OAProcess;
import com.viaoa.util.OAArray;
import com.viaoa.util.OAConv;
import com.viaoa.util.OAStr;
import com.viaoa.util.OAString;
import com.viaoa.web.html.HtmlElement;
import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.OAHtmlComponent.InputType;
import com.viaoa.web.html.oa.OAInputText;
import com.viaoa.web.server.OAApplication;
import com.viaoa.web.server.OABase;
import com.viaoa.web.server.OASession;

/*
<script>
$(document).ready(function() {
    $('#logo-slider').hi5slider({
        speed : 1750,
        pause : 3500,
        transition : 'slide'
    });
});
</script>

// ID used for messages
    oaFormMessage
    oaFormErrorMessage
    oaFormHiddenMessage

// javascript methods available
    oaShowMessage(msg)


    divs will be created with these IDs
      '#oaformDialog'

// hidden form inputs
    oaform = formId
    oacommand = command that is submitting form
    oachanged = the id of the last changed component

*/


/**
 * Controls an html form and it's components
 * Form submission
 * support for multipart
 * messages, errorMessage, hiddenMessages
 * default forwardUrl
 * send script to page (addScript)
 * manage ajax or regular submit
 * Multiple stage handling for submits, with event.
 *
 * @author vvia
 */
public class OAForm extends OABase implements Serializable {
    private static Logger LOG = Logger.getLogger(OAForm.class.getName());
    
    private static final long serialVersionUID = 1L;

    protected final List<OAHtmlComponent> alOAHtmlComponent = new ArrayList();
    protected final List<OAHtmlComponent> alAddOAHtmlComponent = new ArrayList();
    
    // map OAHtmlComponent to HtmlElement
    protected final Map<OAHtmlComponent, HtmlElement> hsComponment = new HashMap<>();
    
    protected OASession session;
    protected String id;
    protected String url;
    protected String title;

    protected String forwardUrl;
    
    private int jsLibrary; 

    /** add script to be returned to browser on initialize. */
    public String jsAddScript;
    /** add script to be returned to browser, only once on initialize (then cleared) */
    public String jsAddScriptOnce;

    private final ArrayList<FormProcess> alProcess = new ArrayList<>(3);
    private volatile boolean bFormProcessClosed;

    protected final List<String> alRequiredCssName = new ArrayList<>();
    protected final List<String> alRequiredJsName = new ArrayList<>();

    // where to redirect after displaying any error messages
    protected String urlRedirect;

    protected final List<String> alCss = new ArrayList();
    protected final List<String> alJs = new ArrayList();
    
    private boolean bMessagesShowing;
    private boolean bHadProcess;
    
    protected boolean bLastDebug;
    
    //qqqqqq temp
    public boolean addValidation=false;
    
    public OAForm() {
    }
    public OAForm(String id) {
        setId(id);
    }
    public OAForm(String id, String url) {
        setId(id);
        setUrl(url);
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String s) {
        this.title = s;
    }
    
    /**
     * set the preferred js library to use. 
     * @param type see {@link OAApplication#JSLibrary_JQueryUI} {@link OAApplication#JSLibrary_Bootstrap}
     */
    public void setDefaultJsLibrary(int type) {
        this.jsLibrary = type;
    }
    public int getDefaultJsLibrary() {
        if (this.jsLibrary != 0 || getSession() == null) return this.jsLibrary;
        return getSession().getDefaultJsLibrary();
    }
    
    public enum Type {
        None,
        Bootstrap
    }
    protected Type type = Type.None;

    public void setType(Type t) {
        this.type = t;
    }
    public Type getType() {
        return type;
    }


    public OASession getSession() {
        return session;
    }
    public void setSession(OASession s) {
        this.session = s;;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    /** URL for this page */
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
    /** page to go to on a submit, unless overwritten by a component or JspSubmit(..) */
    public void setForwardUrl(String urlForward) {
        this.forwardUrl = urlForward;
    }
    public String getForwardUrl() {
        return forwardUrl;
    }

    /** resets the form, takes off any edits not saved */
    public void reset() {
        for (int i=0; ;i++) {
            if (i >= alOAHtmlComponent.size()) break;
            OAHtmlComponent comp = alOAHtmlComponent.get(i);
            comp.reset();
        }
    }
    
    private static class FormProcess {
        OAProcess p; 
        boolean bShowInDialog;
        
        FormProcess(OAProcess p) {
            this.p = p;
            bShowInDialog = true;
        }
    }

    public void addProcess(OAProcess p) {
        if (p == null) return;
        if (bFormProcessClosed) {
            bFormProcessClosed = false;
            addScript("$('#oaFormProcessClosed').val('');");
        }
            
        for (FormProcess fp : alProcess) {
            if (fp.p == p) return;
        }
        alProcess.add(new FormProcess(p));
    }
    public void removeProcess(OAProcess p) {
        if (p == null) return;
        for (FormProcess fp : alProcess) {
            if (fp.p == p) {
                alProcess.remove(fp);
                break;
            }
        }
    }
    public void clearProcesses() {
        alProcess.clear();
    }
    
    public OAProcess[] getProcesses() {
        FormProcess[] fps = new FormProcess[0]; 
        fps = alProcess.toArray(fps);
        
        OAProcess[] ps = new OAProcess[fps.length];
        int i = 0;
        for (FormProcess fp : fps) {
            ps[i++] = fp.p;
        }
        return ps;
    }
    
    
    /**
     * Flag to have the current processes shown/hidden to the user.
     */
    public void showProcesses(boolean b) {
        if (b) {
            if (bFormProcessClosed) {
                addScript("$('#oaFormProcessClosed').val('');");
            }
        }
        bFormProcessClosed = !b;
        for (FormProcess fp : alProcess) {
            fp.bShowInDialog = b;
        }
    }
  
    protected void p(StringBuilder sb, String line, int indent) {
        p(sb, line, indent, false);
    }
    protected void p(StringBuilder sb, String line, int indent, boolean bEmbeddedHtml) {
        p(sb, line, indent, bEmbeddedHtml, false);
    }
    
    protected void p(StringBuilder sb, String line, int indent, boolean bEmbeddedHtml, boolean bLastLineEmbeddedHtml) {
        if (sb == null) return;
        for (int i=0; i<indent; i++) {
            sb.append("  ");
        }

        if (bEmbeddedHtml) {
            sb.append('\"');
        }
        
        int i = 0;
        int x = line.length();
        for ( ; i<x && line.charAt(i) == ' ';  i++);
        if (i > 0) sb.append(line.substring(i));
        else sb.append(line);
        
        if (bEmbeddedHtml) {
            
        }
        if (bEmbeddedHtml) {
            if (bLastLineEmbeddedHtml) sb.append("\"");
            else sb.append("\" +");;
        }
        sb.append('\n');
    }
    
    
    /** javascript to include during the first initialization, (then cleared) */
    public void addScript(String js) {
        addScript(js, true);
    }
    /** add script to be returned to browser when page is initialized. */
    public void addScript(String js, boolean bOnce) {
        if (OAString.isEmpty(js)) return;

        // nees to end in ';'
        if (!js.endsWith(";")) {
            int x = js.length() - 1;
            for ( ; ; x--) {
                if (x < 0) {
                    js += ";";
                    break;
                }
                char ch = js.charAt(x);
                if (ch == ';') break;
                if (Character.isWhitespace(ch)) continue;
                js += ";";
                break;
            }
        }
        if (bOnce) {
            if (jsAddScriptOnce == null) jsAddScriptOnce = "";
            jsAddScriptOnce += js + "\n";
        }
        else {
            if (jsAddScript == null) jsAddScript = "";
            jsAddScript += js + "\n";
        }
    }


    /** finds out if any of the values have changed */
    public boolean isChanged() {
        for (int i=0; ;i++) {
            if (i >= alOAHtmlComponent.size()) break;
            OAHtmlComponent comp = alOAHtmlComponent.get(i);
            if (comp.isChanged()) return true;
        }
        return false;
    }

    /** finds out the name of components that have changed */
    public OAHtmlComponent[] getChangedComponents() {
        List<OAHtmlComponent> al = new ArrayList();

        for (int i=0; ;i++) {
            if (i >= alOAHtmlComponent.size()) break;
            OAHtmlComponent comp = alOAHtmlComponent.get(i);
            if (comp.isChanged()) al.add(comp);
        }

        OAHtmlComponent[] ss = new OAHtmlComponent[al.size()];
        al.toArray(ss);
        return ss;
    }

    
    /*
        getScript
            beforeGetScript
            beforePageLoad
            afterPageLoad
    */
    
    public String getScript() {
        beforeGetScript();
        
        beforePageLoad();

        final StringBuilder sb = new StringBuilder(2048);

        
        p(sb, "<script>", 0);
        
        getInitializeScript(sb);

        String s = getAjaxCallbackScript();
        if (OAStr.isNotEmpty(s)) {
            sb.append(s);
        }

        s = getAjaxScript(true);
        if (OAStr.isNotEmpty(s)) {
            sb.append(s);
        }
        
        // ======== end JQuery document ready 
        p(sb, "});", 0); 
        
        p(sb, "</script>", 0);
        
        afterPageLoad();

        String js = sb.toString();
        return js;
    }

    protected void beforeGetScript() {
        for (OAHtmlComponent comp : alOAHtmlComponent) {
            comp.beforeGetScript();
        }
    }
    
    
    protected void beforePageLoad() {
        for (OAHtmlComponent comp : alOAHtmlComponent) {
            comp.beforePageLoad();
        }
    }
    protected void afterPageLoad() {
        for (OAHtmlComponent comp : alOAHtmlComponent) {
            comp.afterPageLoad();
        }
    }
    
    
    // javascript code to initialize client/browser
    protected void getInitializeScript(StringBuilder sb) {
        alAddOAHtmlComponent.clear();

        // ======== outside method definitions 
        
        int indent = 0;
        p(sb, "", indent);
        p(sb, "var oaShowMessage;", indent);
        p(sb, "if ($().modal) {", indent);
        // bootstrap
        p(sb, "oaShowMessage = function(title, msg) {", ++indent);
        p(sb, "  $('#oaformDialog .modal-title').html(title);", ++indent);
        p(sb, "  $('#oaformDialog .modal-body').html(msg);", indent);
        p(sb, "  $('#oaformDialog').modal({keyboard: true});", indent);
        p(sb, "}", --indent);

        p(sb, "}", --indent);
        p(sb, "else {", indent);
        // jquery version
        p(sb, "oaShowMessage = function(title, msg) {", ++indent);
        p(sb, "    $('#oaformDialog').dialog('option', 'title', title);", ++indent);
        p(sb, "    $('#oaformDialog').html(msg);", indent);
        p(sb, "    $('#oaformDialog').dialog('open');", indent);
        p(sb, "}", --indent);
        p(sb, "}", --indent);
        
        
        // bootstrap
        p(sb, "function oaShowSnackbarMessage(msg) {", indent);
        p(sb, "    $('#oaFormSnackbarMessage').html(msg);", ++indent);
        p(sb, "    $('#oaFormSnackbarMessage').css({visibility:'visible', opacity: 0.0}).animate({opacity: 1.0},300);", indent);
        p(sb, "    setTimeout(function() {", indent++);
        p(sb, "      $('#oaFormSnackbarMessage').animate({opacity: 0.0}, 2000,", indent); 
        p(sb, "        function() {", ++indent);
        p(sb, "          $('#oaFormSnackbarMessage').css('visibility','hidden');", ++indent);
        p(sb, "        }", --indent);
        p(sb, "      );", --indent);
        p(sb, "    }, 2000);", --indent);
        p(sb, "  }", --indent);

        
        // ======== JQuery document ready 
        
        p(sb, "$(document).ready(function() {", indent);
        indent++;
        
        // form dialog
        p(sb, "if ($().modal) {", indent);
        // bootstrap version
        p(sb, "$('#"+id+"').prepend(", ++indent);
        p(sb, "<div id='oaformDialog' class='modal fade' tabindex='-1'>", indent, true);
        p(sb, "  <div class='modal-dialog'>", ++indent, true);
        p(sb, "    <div class='modal-content'>", ++indent, true);
        p(sb, "      <div class='modal-header'>", ++indent, true);
        p(sb, "        <button type='button' class='close' data-dismiss='modal'><span>&times;</span></button>", ++indent, true);
        p(sb, "        <h4 class='modal-title'>", indent, true);
        p(sb, "        </h4>", indent, true);
        p(sb, "      </div>", --indent, true);
        p(sb, "      <div class='modal-body'>", indent, true);
        p(sb, "      </div>", indent, true);
        p(sb, "      <div class='modal-footer'>", indent, true);
        p(sb, "        <button type='button' class='btn btn-primary' data-dismiss='modal'>Ok</button>", ++indent, true);
        p(sb, "      </div>", --indent, true);
        p(sb, "    </div>", --indent, true);
        p(sb, "  </div>", --indent, true);
        p(sb, "</div>", --indent, true, true);
        p(sb, ");", --indent);
        p(sb, "}", --indent);
        p(sb, "else {", indent++);
        // jquery version
        p(sb, "    $('#"+id+"').prepend(\"<div id='oaformDialog'></div>\");", indent);
        p(sb, "    $('#oaformDialog').dialog({", indent++);
        p(sb, "        autoOpen : false,", indent);
        p(sb, "        title : 'Message',", indent);
        p(sb, "        modal : true,", indent);
        p(sb, "        width : 420,", indent);
        p(sb, "        zIndex: 19999,", indent);
        p(sb, "        buttons: [", indent++);
        p(sb, "          { text: 'Ok', click: function() { $(this).dialog('close'); } }", indent);
        p(sb, "         ]", --indent);
        p(sb, "    });", --indent);
        p(sb, "}", --indent);
        
        // bootstrap progress modal
        p(sb, "$('#"+id+"').prepend(", indent++);
        p(sb, "<div id='oaFormProcess' class='modal fade' tabindex='-1'>", indent++, true);
        p(sb, "<div class='modal-dialog'>", indent++, true);
        p(sb, "  <div class='modal-content'>", indent++, true);
        p(sb, "    <div class='modal-header'>", indent++, true);
        p(sb, "      <button id='oaFormProcessClose' type='button' class='close' data-dismiss='modal'>&times;</button>", indent, true);
        p(sb, "      <h4 class='modal-title' id='myModalLabel'><span id='oaFormProcessTitle'></span></h4>", indent, true);
        p(sb, "    </div>", --indent, true);
        p(sb, "    <div class='modal-body center-block'>", indent++, true);
        p(sb, "      <div class='progress'>", indent++, true);
        p(sb, "        <div id='oaFormProcessProgress' class='progress-bar bar' role='progressbar' style='min-width: 2em; width: 5%;'>", indent++, true);
        p(sb, "", indent, true); // ex: '25%'          
        p(sb, "        </div>", --indent, true);
        p(sb, "      </div>", --indent, true);
        p(sb, "      <div>", indent, true);
        p(sb, "      <span id='oaFormProcessStep'></span> ... <span id='oaFormProcessMessage'></span>", indent, true);
        p(sb, "      </div>", indent, true);
        p(sb, "    </div>", --indent, true);
        p(sb, "    <div id='oaFormProcessFooter' class='modal-footer'>", indent, true);
        p(sb, "      <button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>", ++indent, true);
        p(sb, "    </div>", --indent, true);
        p(sb, "  </div>", --indent, true);
        p(sb, "</div>", --indent, true);
        p(sb, "</div>", --indent, true, true);
        p(sb, ");", --indent);

        // update hidden oaFormProcessClosed if progress dlg is closed
        p(sb, "$('#oaFormProcess button').click(function() { $('#oaFormProcessClosed').val('true'); } );", indent);
        
        
        getProcessingScript(sb, indent);
        
        p(sb, "$('body').append(", indent++);
        p(sb, "<div id='oaFormSnackbarMessage'></div>", indent, true, true);
        p(sb, ");", --indent);
        
        p(sb, "$('body').append(\"<div id='oaWait'><img src='image/oawait.gif'></div>\");", indent);

        p(sb, "$('#"+id+"').attr('method', 'post');", indent);
        p(sb, "$('#"+id+"').attr('action', 'oaform.jsp');", indent);
        p(sb, "$('#"+id+"').prepend(\"<input type='hidden' name='oaform' value='"+getId()+"'>\");", indent);

        p(sb, "$('#"+id+"').prepend(\"<input id='oaajaxid' type='hidden' name='oaajaxid' value='"+aiAjaxIdLastUsed.get()+"'>\");", indent);

        p(sb, "$('#"+id+"').prepend(\"<input id='oaFormProcessClosed' type='hidden' name='oaFormProcessClosed' value=''>\");", indent);
        
        // hidden command used by label,button when it is submitted
        p(sb, "$('#"+id+"').prepend(\"<input id='oacommand' type='hidden' name='oacommand' value=''>\");", indent);

        // hidden param used for a component 
        p(sb, "$('#"+id+"').prepend(\"<input id='oaparam' type='hidden' name='oaparam' value=''>\");", indent);
        
        // hidden command that can be used to know if any data on page has been changed
        p(sb, "$('#"+id+"').prepend(\"<input id='oachanged' type='hidden' name='oachanged' value=''>\");", indent);

        // hidden input for browser javascript date.timezoneOffset  (its sign [+/-] is opposite of java timezone offset)

        
        // 20170925 reworked browser time/tz
        p(sb, "(function() {", indent++);
        p(sb, "  var dtNow = new Date();", indent);
        p(sb, "  var tzOffsetJan = (new Date(dtNow.getFullYear(),0,1)).getTimezoneOffset();", indent);
        p(sb, "  var tzOffsetJul = (new Date(dtNow.getFullYear(),6,1)).getTimezoneOffset();", indent);
        p(sb, "  $('#"+id+"').prepend(\"<input id='jsDate' type='hidden' name='jsDate' value='\"+(dtNow.toString())+\"'>\");", indent);
        p(sb, "  $('#"+id+"').prepend(\"<input id='jsTzRawOffset' type='hidden' name='jsTzRawOffset' value='\"+Math.max(tzOffsetJan,tzOffsetJul)+\"'>\");", indent);
        p(sb, "  $('#"+id+"').prepend(\"<input id='jsDateSupportsDST' type='hidden' name='jsDateSupportsDST' value='\"+(tzOffsetJan != tzOffsetJul)+\"'>\");", indent);
        p(sb, "})();", --indent);

        final boolean debugNow = getCalcDebug();
        bLastDebug = debugNow;
        
        if (debugNow) {
            p(sb, "$('#"+id+"').addClass('oaDebug');", indent);
            p(sb, "$('.oaBindable').addClass('oaDebug');", indent);
        }
        else {
            /*
            p(sb, "$('#"+id+"').removeClass('oaDebug');", indent);
            p(sb, "$('.oaBindable').removeClass('oaDebug');", indent);
            */
        }

        boolean b = true;
        for (OAHtmlComponent comp : alOAHtmlComponent) {
            String s = comp.getInitializeScript();

            if (debugNow) p(sb, "$('#"+comp.getId()+"').addClass('oaDebug');", indent);
            // else p(sb, "$('#"+comp.getId()+"').removeClass('oaDebug');", indent);

            if (!OAString.isEmpty(s)) p(sb, s + "", indent);
            if (comp.getInputType() == InputType.File) { 
                if (b) {
                    p(sb, "$('#"+id+"').attr('enctype', 'multipart/form-data');", indent);
                    p(sb, "$('#"+id+"').attr('action', 'oaform.jsp?oaform="+getId()+"');", indent);
                    b = false;
                    // else:  application/x-www-form-urlencoded
                }
            }
        }
        
        
        getMessages(sb, indent);
        p(sb, "", indent);

        // add jquery.validate
        if (addValidation) {
            p(sb, "if ($.validator) {", indent);
            p(sb, "  $('#"+id+"').validate({", ++indent);
    
            p(sb, "  onsubmit: false", ++indent);
            
            b = false;
            for (int i=0; ;i++) {
                if (i >= alOAHtmlComponent.size()) break;
                OAHtmlComponent comp = alOAHtmlComponent.get(i);
                String s = comp.getVerifyScript();
    
                s = comp.getValidationRules();
                if (OAString.isNotEmpty(s)) {
                    if (!b) {
                        b = true;
                        p(sb, ", rules: {", indent);
                        sb.append(s);
                    }
                    else p(sb, ", " +s, indent);
                }
            }
            if (b) {
                p(sb, "}", --indent);
            }            
            
            b = false;
            for (int i=0; ;i++) {
                if (i >= alOAHtmlComponent.size()) break;
                OAHtmlComponent comp = alOAHtmlComponent.get(i);
                String s = comp.getVerifyScript();
    
                s = comp.getValidationMessages();
                if (OAString.isNotEmpty(s)) {
                    if (!b) {
                        b = true;
                        p(sb, ", messages: {", indent);
                        sb.append(s);
                    }
                    else p(sb, ", " +s, indent);
                }
            }
            if (b) {
                p(sb, "}", 1);
            }            

            p(sb, ",", indent);            
            p(sb, "highlight : function(element) {", indent++);
            p(sb, "  $(element).closest('.form-group').removeClass('has-success').addClass('has-error');", indent);
            p(sb, "},", --indent);
            p(sb, "success : function(element) {", indent++);
            p(sb, "  $(element).closest('.form-group').removeClass('has-error');", indent);
            p(sb, "  $(element).remove();", indent);
            p(sb, "},", --indent);
            p(sb, "errorPlacement : function(error, element) {", indent);
            p(sb, "  element.parent().append(error);", ++indent);
            p(sb, "}", --indent);
            
            p(sb, "  });", --indent);
            p(sb, "}", --indent);
        }        
        
        
        
        // add form submit, to verify components
        p(sb, "$('#"+id+"').on('submit', oaSubmit);", indent);
        p(sb, "var oaSubmitCancelled;", indent);
        p(sb, "function oaSubmit(event) {", indent);
        p(sb, "  oaSubmitCancelled = true;", ++indent);
        p(sb, "  var errors = [];", indent);
        p(sb, "  var requires = [];", indent);
        p(sb, "  var regex;", indent);
        p(sb, "  var val;", indent);

        for (int i=0; ;i++) {
            if (i >= alOAHtmlComponent.size()) break;
            OAHtmlComponent comp = alOAHtmlComponent.get(i);
            String s = comp.getVerifyScript();
            if (!OAString.isEmpty(s)) p(sb, "  " + s, indent);
        }

        // 20171104 add jquery.validate
        if (addValidation) {
            p(sb, "  if ($.validator) {", ++indent);
            p(sb, "    if(!$('#"+id+"').valid()) {", indent);
            p(sb, "      $('"+id+"').validate().focusInvalid();", ++indent);
            p(sb, "      oaShowMessage('Can not submit', 'Required fields are missing');", indent);
            p(sb, "      return false;", indent);
            p(sb, "    }", --indent);
            p(sb, "  }", --indent);
        }        
        
           
        p(sb, "  if (requires.length > 0) {", indent);
        p(sb, "    event.preventDefault();", ++indent);
        p(sb, "    var msg = '';", indent);
        p(sb, "    for (var i=0; i<requires.length; i++) {", indent);
        p(sb, "      if (i > 0) {", ++indent);
        p(sb, "        msg += ',<br>';", ++indent);
        p(sb, "      }", --indent);
        p(sb, "      msg += requires[i];", indent);
        p(sb, "    }", --indent);
        p(sb, "    oaShowMessage('Required fields are missing', msg);", indent);
        p(sb, "    return false;", indent);
        p(sb, "  }", --indent);

        p(sb, "  if (errors.length > 0) {", indent);
        p(sb, "    event.preventDefault();", ++indent);
        p(sb, "    var msg = '';", indent);
        p(sb, "    for (var i=0; i<errors.length; i++) {", indent);
        p(sb, "      if (i > 0) {", ++indent);
        p(sb, "        msg += ', ';", ++indent);
        p(sb, "        msg += '<br>';", indent);
        p(sb, "      }", --indent);
        p(sb, "      msg += errors[i];", indent);
        p(sb, "    }", --indent);
        p(sb, "    oaShowMessage('Errors on page', msg);", indent);
        p(sb, "    return false;", indent);
        p(sb, "  }", --indent);
        p(sb, "  oaSubmitCancelled = false;", indent);
        p(sb, "  return true;", indent);
        p(sb, "}", --indent); // end function oaSubmit(..)

        p(sb, "var cntAjaxSubmit = 0;", indent);
        p(sb, "function ajaxSubmit(cmdName) {", indent);
        p(sb, "  cntAjaxSubmit++;", ++indent);
        p(sb, "  var bUseAsync = (cntAjaxSubmit == 1);", indent);
        p(sb, "  if (bUseAsync && cntAjaxSubmit == 1) {", indent);
        p(sb, "    $('#oaWait').fadeIn(200, function(){ if (cntAjaxSubmit < 1) {cntAjaxSubmit=0;$('#oaWait').hide();}});", ++indent);  
        p(sb, "  }", --indent);
        p(sb, "  var f1 = function(data) {", indent);
        p(sb, "    if (--cntAjaxSubmit < 1) {", ++indent);
        p(sb, "      cntAjaxSubmit=0; ", ++indent);
        p(sb, "      $('#oaWait').hide();", indent);
        p(sb, "    }", --indent);
        p(sb, "    if (data) eval(data);", indent);
        p(sb, "  }", --indent);
        p(sb, "  var f2 = function() {", indent);
        p(sb, "    if (--cntAjaxSubmit < 1) {", ++indent);
        p(sb, "      cntAjaxSubmit=0; ", ++indent);
        p(sb, "      $('#oaWait').hide();", indent);
        p(sb, "    }", --indent);
        p(sb, "  }", --indent);

        // 20260628 support for sending enctype=multipart
        p(sb, "  var objAjax = {", indent);
        p(sb, "    method: 'POST',", ++indent);
        p(sb, "    url: 'oaajax.jsp?oaform="+id+"',", indent);
        p(sb, "    data: new FormData($('#"+id+"')[0]),", indent);
        p(sb, "    processData: false,", indent); // so that jq does not try to serialize
        p(sb, "    contentType: false,", indent); // so jq wont do it's default 
        p(sb, "    dataType: 'script',", indent);
        p(sb, "    timeout: 30000,", indent);
        p(sb, "    async: bUseAsync", indent);
        p(sb, "  };", --indent);
        
        p(sb, "  $.ajax(objAjax).done(f1).fail(f2);", indent);
        
        /*was:
        p(sb, "  var args = $('#"+id+"').serialize();", indent);
        p(sb, "  if (cmdName != undefined && cmdName) args = cmdName + '=1&' + args;", indent);
        p(sb, "  $.ajax({", indent);
        p(sb, "    type: 'POST',", ++indent);
        p(sb, "    url: 'oaajax.jsp',", indent);
        p(sb, "    data: args,", indent);
        p(sb, "    success: f1,", indent);
        p(sb, "    error: f2,", indent);
        p(sb, "    dataType: 'text',", indent);
        p(sb, "    timeout: 30000,", indent);
        p(sb, "    async: bUseAsync", indent);
        p(sb, "  });", --indent);
        */
        
        p(sb, "}", --indent);

        p(sb, "function ajaxSubmit2(cmdName) {", indent);
        p(sb, "  var args = $('#"+id+"').serialize();", ++indent);
        p(sb, "  if (cmdName != undefined && cmdName) args = cmdName + '=1&' + args;", indent);
        p(sb, "  $.ajax({", indent);
        p(sb, "    method: 'POST',", ++indent);
        p(sb, "    data: args,", indent);
        p(sb, "    url: 'oaajax.jsp',", indent);
        p(sb, "    dataType: 'script'", indent);
        p(sb, "  }).done(function(data) {if (data) eval(data);});", --indent);
        p(sb, "}", --indent);
        
        
        if (!OAString.isEmpty(jsAddScript)) {
            p(sb, "    " + jsAddScript + "", indent);
        }
        if (!OAString.isEmpty(jsAddScriptOnce)) {
            p(sb, "    " + jsAddScriptOnce + "", indent);
            jsAddScriptOnce = null;
        }


        String js = sb.toString();
        if (js.indexOf(".focus()") < 0) {
            p(sb, "    $('input:enabled:first').focus();", indent);
        }

        String s = getRedirect();
        if (OAString.isNotEmpty(s)) {
            setRedirect(null);
            p(sb, "window.location = '"+s+"';", indent);            
        }
        
        // ======== JQuery document ready 
        // p(sb, "});", --indent); // end jquery.ready ****
    }

    
    public String getAjaxScript() {
        return getAjaxScript(false);
    }

    public String getAjaxScript(final boolean bIsInitializing) {
        
        if (!bIsInitializing) beforeGetScript();        
        
        if (!getEnabled()) return "";
        StringBuilder sb = new StringBuilder(1024);

        final boolean debugNow = getCalcDebug();
        
        int indent = 0;
        if (debugNow != bLastDebug) {
            if (debugNow) {
                p(sb, "    $('#"+id+"').addClass('oaDebug');", indent);
                p(sb, "    $('.oaBindable').addClass('oaDebug');", indent);
            }
            else {
                p(sb, "    $('#"+id+"').removeClass('oaDebug');", indent);
                p(sb, "    $('.oaBindable').removeClass('oaDebug');", indent);
            }
        }

        for (int i=0; ;i++) {
            if (i >= alOAHtmlComponent.size()) break;
            OAHtmlComponent comp = alOAHtmlComponent.get(i);
            
            String s;
            if (alAddOAHtmlComponent.contains(comp)) {
                s = comp.getInitializeScript();
                if (!OAString.isEmpty(s)) p(sb, s + "", 1);
                s = comp.getVerifyScript();
                if (!OAString.isEmpty(s)) p(sb, s + "", 1);
            }
            s = comp.getAjaxScript(bIsInitializing);
            
            if (!OAString.isEmpty(s)) p(sb, s + "", 1);
            if (debugNow != bLastDebug) {
                if (debugNow) p(sb, "    $('#"+comp.getId()+"').addClass('oaDebug');", indent);
                else p(sb, "    $('#"+comp.getId()+"').removeClass('oaDebug');", indent);
            }
        }
        alAddOAHtmlComponent.clear();

        /* added to js code 
        p(sb, "$('#oacommand').val('');", indent); // set back to blank
        p(sb, "$('#oaparam').val('');", indent); // set back to blank
        */

        getProcessingScript(sb, indent);

        String s = getAjaxCallbackScript();
        if (s != null) sb.append(s);

        if (!OAString.isEmpty(jsAddScriptOnce)) {
            sb.append(jsAddScriptOnce);
            jsAddScriptOnce = null;
        }

        s = getRedirect();
        if (OAString.isNotEmpty(s)) {
            setRedirect(null);
            p(sb, "window.location = '"+s+"';", indent);            
        }

        getMessages(sb, indent);
        
        String js = sb.toString();
        if (js == null) js = "";
        
        bLastDebug = debugNow;
        return js;
    }

    /**
     * Used to show popup showing any oaprocesses that are running
     * @see #addProcess(OAProcess)
     */
    protected void getProcessingScript(final StringBuilder sb, int indent) {
        boolean bHasProcess = false;
        String title = null;
        int cnt = 0;
        int perc = 0;
        int step1 = 0;
        int step2 = 0;
        String step = "";
        boolean bBlock = false;
        // int indent = 0;

        for (FormProcess fp : alProcess) {
            OAProcess p = fp.p;
            if (!fp.bShowInDialog) continue;
            if (!p.isDone()) {
                cnt++;
                if (!bBlock && p.getBlock()) {
                    if (!p.isBlockTimedout()) {
                        if (!p.isTimedout()) {
                            bBlock = true;
                        }
                    }
                }
            }
            
            bHasProcess = true;
            String s = p.getName();
            
            if (p.isDone()) {
                if (s == null) s = "";
                s += " - Done";
            }
            if (p.getCancelled()) {
                if (s == null) s = "";
                s += " - Cancelled";
            }
            
            if (OAString.isNotEmpty(s)) {
                if (title == null) title = s;
                else title += ", "+ s;
            }
            
            step1 = p.getCurrentStep();
            step2 = p.getTotalSteps();
            
            if (p.isDone()) {
                perc = 100;
            }
            else if (step1 > step2) perc = 100;
            else if (step1 < 2) perc = 0;
            else {
                perc = (int) (((step1-1)/((double)step2) ) * 100.0);
            }
            
            String[] ss = p.getSteps();
            if (ss != null && step1 > 0 && (step1-1) < ss.length) step = ss[step1-1];
        }
        
        if (!bHasProcess) {
            if (!bHadProcess) return;
        }
        
        p(sb, "if ($().modal) {", indent++);  // qqqqqqqq removed once jquery-ui is no longer used
        if (!bHasProcess) {
            bHadProcess = false;
            p(sb, "$('#oaFormProcess').modal('hide');", indent);
        }
        else {
            bHadProcess = true;
            if (title == null) title = "";
            else title = OAString.convert(title, "'", "\\'");
            p(sb, "$('#oaFormProcessTitle').html('"+title+"');", indent);
            if (bBlock && cnt > 0) {
                p(sb, "$('#oaFormProcessClose').hide();", indent);
                p(sb, "$('#oaFormProcessFooter').hide();", indent);
            }
            else {
                p(sb, "$('#oaFormProcessClose').show();", indent);
                p(sb, "$('#oaFormProcessFooter').show();", indent);
            }
            
            p(sb, "$('#oaFormProcessProgress').css({width: '"+perc+"%'}).html('"+perc+"%');", indent);
            
             if (cnt == 0 || (step1 == 0 && step2 == 0 && OAString.isEmpty(step))) {
                p(sb, "$('#oaFormProcessStep').html('');", indent);
                p(sb, "$('#oaFormProcessMessage').html('');", indent);
            }
            else {
                p(sb, "$('#oaFormProcessStep').html('Step "+step1+" of "+step2+"');", indent);
                p(sb, "$('#oaFormProcessMessage').html('"+OAString.getNonNull(step)+"');", indent);
            }
            
            p(sb, "if ($('#oaFormProcessClosed').val() != 'true') {", indent);
            p(sb, "$('#oaFormProcess').modal('show'); }", indent);
            if (!bFormProcessClosed) requestAjaxCallback();
        }
        p(sb, "}", --indent);  // qqqqqqqq removed once jquery-ui is no longer used
    }    
    
    protected void getMessages(StringBuilder sb, int indent) {
        String[] msg1, msg2;

        msg1 = msg2 = null;
        if (session != null) {
            msg1 = session.getApplication().getMessages();
            msg2 = session.getMessages();
            session.clearMessages();
        }
        boolean b = _addMessages(sb, indent, "oaFormMessage", "Message", msg1, msg2, this.getMessages());

        clearMessages();

        msg1 = msg2 = null;
        if (session != null) {
            msg1 = session.getApplication().getErrorMessages();
            msg2 = session.getErrorMessages();
            session.clearErrorMessages();
        }
        b = b || _addMessages(sb, indent, "oaFormErrorMessage", "Error", msg1, msg2, this.getErrorMessages());
        clearErrorMessages();
        
        
        // popup
        msg1 = msg2 = null;
        if (session != null) {
            msg1 = session.getApplication().getPopupMessages();
            msg2 = session.getPopupMessages();
            session.clearPopupMessages();
        }
        b = b || _addMessages(sb, indent, null, null, msg1, msg2, this.getPopupMessages());
        clearPopupMessages();

        if (b) {  // have redirect show after message is displayed
            String s = getRedirect();
            if (OAString.isNotEmpty(s)) {
                setRedirect(null);
                s = "$('#oaformDialog').on('hidden.bs.modal', function () {"+
                    "$('#oaWait').show();" + 
                    "window.location = '"+s+"';"+            
                    "});";
                addScript(s);
            }
        }        
        
        
        msg1 = msg2 = null;
        if (session != null) {
            msg1 = session.getApplication().getHiddenMessages();
            msg2 = session.getHiddenMessages();
            session.clearHiddenMessages();
        }
        _addMessages(sb, indent, "oaFormHiddenMessage", "", msg1, msg2, this.getHiddenMessages());
        clearHiddenMessages();
        

        

        // snackbar
        msg1 = msg2 = null;
        if (session != null) {
            msg1 = session.getApplication().getSnackbarMessages();
            msg2 = session.getSnackbarMessages();
            session.clearSnackbarMessages();
        }
        _addSnackbarMessages(sb, indent, null, null, msg1, msg2, this.getSnackbarMessages());
        clearSnackbarMessages();
        
        
        // console.log
        for (String s : alConsole) {
            if (OAString.isEmpty(s)) continue;
            s = OAString.convert(s, "'", "\\'");
            addScript("console.log('"+s+"');");
        }
        alConsole.clear();
    }

    protected final transient ArrayList<String> alConsole = new ArrayList<String>(5);
    public void addConsoleMessage(String msg) {
        alConsole.add(msg);
    }
    
    
    private boolean _addMessages(StringBuilder sb, int indent, String id, String title, String[] msgs1, String[] msgs2, String[] msgs3) {
        boolean bResult = (msgs1 != null && msgs1.length > 0) || (msgs2 != null && msgs2.length > 0) || (msgs3 != null && msgs3.length > 0);
        if (title == null) title = "";
        String msg = "";
        if (msgs1 != null) {
            for (String s : msgs1) {
                if (msg.length() > 0) msg += "<br>";
                msg += s;
            }
        }
        if (msgs2 != null) {
            for (String s : msgs2) {
                if (msg.length() > 0) msg += "<br>";
                msg += s;
            }
        }
        if (msgs3 != null) {
            for (String s : msgs3) {
                if (msg.length() > 0) msg += "<br>";
                msg += s;
            }
        }

        boolean bDebugx = getDebug();
        if (bLastDebug != bDebugx) {
            if (getDebug()) p(sb, "    $('#"+id+"').addClass('oaDebug');", indent);
            else p(sb, "    $('#"+id+"').removeClass('oaDebug');", indent);
        }

        msg = OAString.convert(msg, "'", "\\'");
        if (msg.length() > 0) {
            bMessagesShowing = true;
            if (id != null) {
                p(sb, "if ($('#"+id+"').length) {", indent);
                p(sb, "  $('#"+id+"').html('"+msg+"');", ++indent);
                p(sb, "  $('#"+id+"').show();", indent);
                p(sb, "} else {", --indent);
                p(sb, "  oaShowMessage('"+title+"', '"+msg+"');", ++indent);
                p(sb, "}", --indent);
            }
            else {
                p(sb, "oaShowMessage('"+title+"', '"+msg+"');", indent);
            }
        }
        else {
            if (bMessagesShowing) {
                if (OAStr.isNotEmpty(id)) {
                    p(sb, "$('#"+id+"').hide();", indent);
                }
                bMessagesShowing = false;
            }
        }
        return bResult;
    }
    
    
    private void _addSnackbarMessages(StringBuilder sb, int indent, String id, String title, String[] msgs1, String[] msgs2, String[] msgs3) {
        if (title == null) title = "";
        String msg = "";
        if (msgs1 != null) {
            for (String s : msgs1) {
                if (msg.length() > 0) msg += "<br>";
                msg += s;
            }
        }
        if (msgs2 != null) {
            for (String s : msgs2) {
                if (msg.length() > 0) msg += "<br>";
                msg += s;
            }
        }
        if (msgs3 != null) {
            for (String s : msgs3) {
                if (msg.length() > 0) msg += "<br>";
                msg += s;
            }
        }

        msg = OAString.convert(msg, "'", "\\'");
        if (msg.length() > 0) {
            p(sb, "oaShowSnackbarMessage('"+msg+"');", indent);
        }
    }
    
    public List<OAHtmlComponent> getComponents() {
        return alOAHtmlComponent;
    }

    public OAHtmlComponent getComponent(String id) {
        if (id == null) return null;
        for (int i=0; ;i++) {
            if (i >= alOAHtmlComponent.size()) break;
            OAHtmlComponent comp = alOAHtmlComponent.get(i);
            if (id.equalsIgnoreCase(comp.getId())) {
                return comp;
            }
        }
        return null;
    }

    

    public void add(OAHtmlComponent comp) {
        if (comp == null) return;
        add(null, comp);
    }
    public void add(String id, OAHtmlComponent comp) {
        if (comp == null) return;
        if (OAString.isEmpty(id)) id = comp.getId();
        else comp.setId(id);

        if (!OAString.isEmpty(id)) {
            OAHtmlComponent compx = getComponent(id);
            
            if (compx != null) {
                if ( comp.getClass().isAssignableFrom(compx.getClass()) || compx.getClass().isAssignableFrom(comp.getClass()) ) {
                    remove(id);
                }
            }
        }
        if (!alOAHtmlComponent.contains(comp)) {
            alOAHtmlComponent.add(comp);
        }
        if (!alAddOAHtmlComponent.contains(comp)) {
            alAddOAHtmlComponent.add(comp);
        }
        comp.setForm(this);
    }

    
    public void add(HtmlElement he) {
        if (he == null) return;
        add(he.getOAHtmlComponent());
        hsComponment.put(he.getOAHtmlComponent(), he);
    }
    public void add(String id, HtmlElement he) {
        if (he == null) return;
        add(id, he.getOAHtmlComponent());
        hsComponment.put(he.getOAHtmlComponent(), he);
    }
    
    public void remove(String name) {
        OAHtmlComponent comp = getComponent(name);
        if (comp != null) {
            alOAHtmlComponent.remove(comp);
            hsComponment.remove(comp);
        }
        super.remove(name);
    }
    

    /** used to manage ajax callbacks from the browser, so that not too many will be created on the browser. */
    private final AtomicInteger aiAjaxIdLastRequest = new AtomicInteger();
    
    private final AtomicInteger aiAjaxIdLastUsed = new AtomicInteger();
    private final AtomicInteger aiAjaxIdLastReceived = new AtomicInteger();

    
    /** used to have the browser ajax callback 
     *
     * @see OAForm#getCallbackMs() to set the time in miliseconds.
     */
    public void requestAjaxCallback() {
        aiAjaxIdLastRequest.incrementAndGet();
    }    

    protected String getAjaxCallbackScript() {
        try {
            lockSubmit.writeLock().lock();
            return _getAjaxCallbackScript();
        }
        finally {
            lockSubmit.writeLock().unlock();
        }
    }
    protected String _getAjaxCallbackScript() {
        int x1 = aiAjaxIdLastUsed.get();
        int x2 = aiAjaxIdLastRequest.get();
        if (x1 == x2) return null;

        x2 = aiAjaxIdLastReceived.get();
        if (x1 > x2) return null;  // will be returning
        
        aiAjaxIdLastUsed.set(aiAjaxIdLastRequest.get());
        
        String s = "$('#oaajaxid').val('"+aiAjaxIdLastUsed.get()+"');";
        s = "window.setTimeout(function() {"+s+"ajaxSubmit2();}, "+getCallbackMs()+");";
        return s;
    }
    
    private int msForCallback = 2500;;
    /**
     * If requestAjaxCallback is true, then this is the amount of time(ms) before browser will call the server.
     * default is 2500 (ms)
     * @param ms miliseconds
     */
    public void setCallbackMs(int ms) {
        if (ms > 0) msForCallback = ms;
    }
    public int getCallbackMs() {
        return msForCallback;
    }
    
    
    private final ReentrantReadWriteLock lockSubmit = new ReentrantReadWriteLock();

    protected OAFormSubmitEvent lastFormSubmitEvent;
    public OAFormSubmitEvent getLastFormSubmitEvent() {
        return lastFormSubmitEvent;
    }
    
    /** called to process the form.
     *  See oaform.jsp
     */
    public String processSubmit(OASession session, HttpServletRequest request, HttpServletResponse response, boolean isAjax) {
        final OAFormSubmitEvent formSubmitEvent = new OAFormSubmitEvent(this, session, request, response, isAjax);

        this.lastFormSubmitEvent = formSubmitEvent;

        if (this.session == null) this.session = session;
        try {
            lockSubmit.writeLock().lock();
            _processSubmit(formSubmitEvent);
        }
        finally {
            lockSubmit.writeLock().unlock();
        }
        
        String forward = formSubmitEvent.getForwardUrl();
        if (OAString.isEmpty(forward)) forward = this.getUrl();

        return forward;
    }    
    
    protected void _processSubmit(final OAFormSubmitEvent formSubmitEvent) {
        if (this.session == null) this.session = session;

        try {
            formSubmitEvent.getRequest().setCharacterEncoding("UTF-8");
        }
        catch (Exception e) {}

        final Map<String, String[]> hmNameValue = formSubmitEvent.getNameValueMap();
        final String contentType = formSubmitEvent.getRequest().getContentType();
        if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
            try {
                processMultipart(formSubmitEvent, hmNameValue);
            }
            catch (Exception e) {
                LOG.log(Level.WARNING, "Exception while processing Multipart, oaform.Id="+getId(), e);
                this.addErrorMessage(e.toString());
            }
            
            if (formSubmitEvent.getForm().getCalcDebug()) {
                hmNameValue.forEach( (k,v) -> 
                System.out.println("  Key: " + k + ": Values[0]: " + ((v == null || v.length == 0) ? "" : v[0])));
            }
        }
        else {
            Enumeration enumx = formSubmitEvent.getRequest().getParameterNames();
            while ( enumx.hasMoreElements()) {
                String name = (String) enumx.nextElement();
                String[] values = formSubmitEvent.getRequest().getParameterValues(name);
                hmNameValue.put(name, values);
            }
        }

        if (!bFormProcessClosed) {
            String[] ss = hmNameValue.get("oaFormProcessClosed");
            if (ss != null && ss.length == 1) {
                bFormProcessClosed = OAConv.toBoolean(ss[0]);
            }
        }
        
        String[] ss = hmNameValue.get("oaajaxid");
        if (ss != null && ss.length == 1 && OAString.isInteger(ss[0])) {
            int x = OAConv.toInt(ss[0]);
            int x2 = aiAjaxIdLastReceived.get();
            if (x > x2) {
                aiAjaxIdLastReceived.set(x);
            }
        }

        // Browser info
        ss = hmNameValue.get("jsDate");
        String jsDate = null;
        if (ss != null && ss.length > 0) jsDate = ss[0];
        
        ss = hmNameValue.get("jsTzRawOffset");
        String jsTzRawOffset = null;
        if (ss != null && ss.length > 0) jsTzRawOffset = ss[0];
        else jsTzRawOffset = "";
        
        ss = hmNameValue.get("jsDateSupportsDST");
        String jsDateSupportsDST = null;
        if (ss != null && ss.length > 0) jsDateSupportsDST = ss[0];
        
        try {
            String s = formSubmitEvent.getRequest().getHeader("Accept-Language");
            session.setBrowserInfo(jsDate, OAConv.toInt(jsTzRawOffset), OAConv.toBoolean(jsDateSupportsDST), s);
        }
        catch (Exception e) {
            LOG.log(Level.WARNING, "error setting browser info, jsDate="+jsDate+", jsTzRawOffset="+jsTzRawOffset+", jsDateSupportsDST="+jsDateSupportsDST, e);
        }
        

//qqqqqqqqq have OAHtmlTable use command=table.Id, and subcommand=TD.id  qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq        
        
        final List<OAHtmlComponent> alOAHtmlComponentAll = new ArrayList();
        
        for (OAHtmlComponent comp : alOAHtmlComponent) {
            alOAHtmlComponentAll.add(comp);
            comp.geten
        }
        
        
        // set the component that submitted (if any)
        if (!formSubmitEvent.getCancel()) {
            String[] ids = hmNameValue.get("oacommand");
            String id = ids == null || ids.length == 0 ? null : ids[0];
            
            if (formSubmitEvent.getSubmitOAHtmlComponent() == null && OAStr.isNotEmpty(id)) {
                formSubmitEvent.setSubmitOAHtmlComponent(getComponent(id));
            }

            if (formSubmitEvent.getSubmitOAHtmlComponent() == null) {
                for (OAHtmlComponent comp : alOAHtmlComponent) {
                    if (comp.getInputType() == OAHtmlComponent.InputType.Submit) {
                        if (formSubmitEvent.getRequest().getParameter(comp.getCalcName()) != null) {
                            formSubmitEvent.setSubmitOAHtmlComponent(comp);
                            break;
                        }
                    }
                    
                    if (comp.getInputType() == OAHtmlComponent.InputType.Image) {
                        String val = formSubmitEvent.getRequest().getParameter(comp.getCalcName()+".x");
                        if (val != null) {
                            formSubmitEvent.setSubmitOAHtmlComponent(comp);
                            break;
                        }
                    }                
                }
            }
            
            // input type=image also includes the x & y for the mouse click. 
            if (formSubmitEvent.getSubmitOAHtmlComponent() != null) {
                if (formSubmitEvent.getSubmitOAHtmlComponent().getInputType() == OAHtmlComponent.InputType.Image) {
                    String s = formSubmitEvent.getSubmitOAHtmlComponent().getCalcName();
                    String val = formSubmitEvent.getRequest().getParameter(s+".x");
                    if (val != null) {
                        formSubmitEvent.setImageClickX(OAConv.toInt(val));
                        val = formSubmitEvent.getRequest().getParameter(s+".y");
                        formSubmitEvent.setImageClickY(OAConv.toInt(val));
                    }
                }
            }
            
        }
        
        if (!formSubmitEvent.getCancel()) {
            for (OAHtmlComponent comp : alOAHtmlComponent) {
                comp.onSubmitPrecheck(formSubmitEvent);
                if (formSubmitEvent.getCancel()) break;
            }
        }
        
        if (!formSubmitEvent.getCancel()) {
            // all are called, allows components to cancel
            for (OAHtmlComponent comp : alOAHtmlComponent) {
                if (!comp.getEnabled()) continue;
                comp.onSubmitBeforeLoadValues(formSubmitEvent);
                if (formSubmitEvent.getCancel()) break;
            }
        }
        
        if (!formSubmitEvent.getCancel()) {
            for (OAHtmlComponent comp : alOAHtmlComponent) {
                if (!comp.getEnabled()) continue;
                comp.onSubmitLoadValues(formSubmitEvent);
            }
        }

        if (!formSubmitEvent.getCancel()) {
            for (OAHtmlComponent comp : alOAHtmlComponent) {
                if (!comp.getEnabled()) continue;
                comp.onSubmitAfterLoadValues(formSubmitEvent);
                if (formSubmitEvent.getCancel()) break;
            }
        }
        
        if (!formSubmitEvent.getCancel()) {
            OAHtmlComponent he = formSubmitEvent.getSubmitOAHtmlComponent();
            if (he != null) {
                String s = he.getForwardUrl();
                if (OAStr.isNotEmpty(s)) formSubmitEvent.setForwardUrl(s);
                he.onSubmit(formSubmitEvent);
            }
        }

        if (!formSubmitEvent.getCancel()) {
            for (OAHtmlComponent comp : alOAHtmlComponent) {
                if (!comp.getEnabled()) continue;
                comp.onSubmitCompleted(formSubmitEvent);
            }
        }
    }
    
    
    /* Parse Multipart posted forms
        POST / HTTP/1.1
        Host: localhost:8000
        User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:29.0) Gecko/20100101 Firefox/29.0
        Accept: text/html,application/xhtml+xml,application/xml;q=0.9,* / *;q=0.8
        Accept-Language: en-US,en;q=0.5
        Accept-Encoding: gzip, deflate
        Cookie: __atuvc=34%7C7; permanent=0; _gitlab_session=226ad8a0be43681acf38c2fab9497240; __profilin=p%3Dt; request_method=GET
        Connection: keep-alive
        Content-Type: multipart/form-data; boundary=---------------------------9051914041544843365972754266
        Content-Length: 554
        
        -----------------------------9051914041544843365972754266
        Content-Disposition: form-data; name="text"
        
        text default
        -----------------------------9051914041544843365972754266
        Content-Disposition: form-data; name="file1"; filename="a.txt"
        Content-Type: text/plain
        
        Content of a.txt.
        
        -----------------------------9051914041544843365972754266
        Content-Disposition: form-data; name="file2"; filename="a.html"
        Content-Type: text/html
        
        <!DOCTYPE html><title>Content of a.html.</title>
        
        -----------------------------9051914041544843365972754266--       
    */
    
    protected void processMultipart(OAFormSubmitEvent formSubmitEvent, Map<String, String[]> hmNameValue) throws Exception {
        final ServletRequest request = formSubmitEvent.getRequest();
        final String contentType = request.getContentType();
        final String boundary = "--" + contentType.substring(contentType.indexOf("boundary=")+9);
        final BufferedInputStream bisx = new BufferedInputStream(request.getInputStream());
        final BufferedReader inputReader = new BufferedReader(new InputStreamReader(bisx));

        boolean bHasMore = readNextMultiPartValue(inputReader, null, boundary);
        
        for (int i=0; bHasMore ;i++) {
            String line = inputReader.readLine();
            String s = "Content-Disposition: form-data; name=";
            int pos = line.indexOf(s);

            String name = OAStr.field(line, ";", 2);
            name = OAStr.field(name, "=", 2);
            name = name.replace('"', ' ');
            name = name.trim();
            
            final OAHtmlComponent comp = getComponent(name);
            final boolean bIsFile = (comp != null) && (comp.getInputType() == InputType.File);
            
            String value = null;
            OutputStream os;
            
            if (bIsFile) {
                String fn = OAStr.field(line, ";", 3);
                fn = OAStr.field(fn, "=", 2);
                fn = fn.replace('"', ' ');
                fn = fn.trim();
                value = fn;

                line = inputReader.readLine();
                String fileContentType = OAStr.field(line, "Content-Type:", 2);
                if (fileContentType == null) fileContentType = "";
                else fileContentType = fileContentType.trim();
                
                os = comp.onSubmitGetFileOutputStream(formSubmitEvent, fn, fileContentType);
                if (formSubmitEvent.getCancel()) break;
            }
            else {
                line = inputReader.readLine();  // empty line
                os = new ByteArrayOutputStream();
            }

            BufferedOutputStream bos = os == null ? null : new BufferedOutputStream(os); 
            bHasMore = readNextMultiPartValue(inputReader, bos, "\r\n"+boundary);
            if (bos != null) {
                bos.flush();
                bos.close();
            }

            if (!bIsFile) {
                value = os.toString();
            }
            
            String[] ss = hmNameValue.get(name);
            if (ss == null) ss = new String[] {value};
            else ss = OAArray.add(ss, value);
            hmNameValue.put(name, ss);
        }
    }


    /**
     * Writes all data to bos, up to the boundary, and then skips to the end of the boundry,
     * and reads 2 chars to determine if there are addition parts.
     * 
     * @return true if there are more parts read, false if this was the last part. 
     */
    protected boolean readNextMultiPartValue(final BufferedReader br, final BufferedOutputStream bos, final String boundary) throws IOException {
        int boundaryLen = boundary.length();

        for (;;) {
            int c = br.read();
            if (c < 0) {
                break; // should not happen
            }

            if (c == boundary.charAt(0)) {
                final int hold = c;
                br.mark(boundaryLen+1);
                int j=1;
                for ( ; j<boundaryLen; j++) {
                    c = br.read();
                    if (c != boundary.charAt(j)) break;
                }
                if (j == boundaryLen) break;
                br.reset();
                c = hold;
            }
            if (bos != null) bos.write(c);
        }

        // ends in CR+LF if there is more data
        // ends in "--" if there is not more data after this
        int c = br.read();
        c = br.read();
        
        if (c == '-') return false;
        return true;
    }

    /**
     * This is used to for a http (or ajax) request go directly to a component for processing. 
     */
    public String processGetJson(OASession session) throws Exception {
        if (this.session == null) this.session = session;

        String id = session.getRequest().getParameter("id");
        OAHtmlComponent comp = getComponent(id);
        String json = comp.onGetJson(session);
        if (json == null) json = "";
        return json;
    }
    
    /**
     * Called by oaforward.jsp to be able to have a component submit method called,
     * without doing a form submit.
     */
    public String processForward(OASession session, HttpServletRequest request, HttpServletResponse response) {
        if (this.session == null) this.session = session;

        final OAFormSubmitEvent formSubmitEvent = new OAFormSubmitEvent(this, session, request, response, false);
        this.lastFormSubmitEvent = formSubmitEvent;

        final Map<String, String[]> hmNameValue = formSubmitEvent.getNameValueMap();
        final String contentType = formSubmitEvent.getRequest().getContentType();
        
        Enumeration enumx = request.getParameterNames();
        while ( enumx.hasMoreElements()) {
            String name = (String) enumx.nextElement();
            String[] values = request.getParameterValues(name);
            hmNameValue.put(name,values);
        }

        _processForward(formSubmitEvent);
        
        String forward = formSubmitEvent.getForwardUrl();
        if (OAString.isEmpty(forward)) forward = this.getUrl();
        
        return forward;
    }
    
    protected void _processForward(final OAFormSubmitEvent formSubmitEvent) {
        try {
            formSubmitEvent.getRequest().setCharacterEncoding("UTF-8");
        }
        catch (Exception e) {}

        String id = formSubmitEvent.getRequest().getParameter("oacommand");
        final OAHtmlComponent comp = getComponent(id);
        if (comp == null) return;

        formSubmitEvent.setSubmitOAHtmlComponent(comp);
        formSubmitEvent.setForwardUrl(comp.getForwardUrl());

        comp.onSubmitPrecheck(formSubmitEvent);
        comp.onSubmitBeforeLoadValues(formSubmitEvent);
        comp.onSubmitLoadValues(formSubmitEvent);
        comp.onSubmit(formSubmitEvent);
        comp.onSubmitCompleted(formSubmitEvent);
    }

    /**
     * filepath to include on CSS files on the page
     * @param filePath full path of page (relative to webcontent)
     */
    public void addCss(String filePath) {
        if (filePath == null) return;
        if (!alCss.contains(filePath)) alCss.add(filePath);
        
    }

    /**
     * filepath to include on JS files on the page
     * @param filePath full path of page (relative to webcontent)
     */
    public void addJs(String filePath) {
        if (filePath == null) return;
        if (!alJs.contains(filePath)) alJs.add(filePath);
    }
    
    
    /**
     * add names of CSS to include on page
     * @param name of css to include,  
     * @see OAFormInsertDelegate#registerRequiredCssName(String, String) 
     */
    public void addRequiredCssName(String name) {
        if (name == null) return;
        name = name.toUpperCase();
        if (!alRequiredCssName.contains(name)) alRequiredCssName.add(name);
        
    }
    /**
     * add names of JS to include on page
     * @param name of js to include,  
     * @see OAFormInsertDelegate#registerRequiredJsName(String, String) 
     */
    public void addRequiredJsName(String name) {
        if (name == null) return;
        name = name.toUpperCase();
        if (!alRequiredJsName.contains(name)) alRequiredJsName.add(name);
    }
    
    
    public String getCssInsert() {
        final Set<String> hsCssName = new LinkedHashSet<>();

        // note: order matters
        hsCssName.add(OAFormInsertDelegate.CSS_jquery_ui);
        
        //qqqqqqqqqqq might want to add *theme css files        
        
        for (int i=0; ;i++) {
            if (i >= alOAHtmlComponent.size()) break;
            OAHtmlComponent comp = alOAHtmlComponent.get(i);
            comp.getRequiredCssNames(hsCssName);
        }
        
        // include oajsp.css after components
        hsCssName.add(OAFormInsertDelegate.CSS_oajsp);

        ArrayList<String> alFilePath = new ArrayList<>();

        for (String name : hsCssName) {
            String s = OAFormInsertDelegate.getCssFilePath(name);
            if (s == null) alFilePath.add("ERROR: CSS name="+name+" does not have a filePath mapped to it.");
            else if (!alFilePath.contains(s)) alFilePath.add(s);
        }
        
        for (String s : alCss) {
            if (s != null && !alFilePath.contains(s)) alFilePath.add(s);
        }

        StringBuilder sb = new StringBuilder(1024); 
        for (String s : alFilePath) {
            s = "<link rel=\"stylesheet\" type=\"text/css\" href=\""+s+"\">\n";
            sb.append(s);
        }
        
        return sb.toString();
    }

    public String getJsInsert() {
        final Set<String> hsJsName = new LinkedHashSet<>();
        
        // note: order matters
        hsJsName.add(OAFormInsertDelegate.JS_jquery); 
        hsJsName.add(OAFormInsertDelegate.JS_jquery_ui);

        
        if (addValidation) {
            hsJsName.add(OAFormInsertDelegate.JS_jquery_validation);
        }

        for (String name : alRequiredJsName) {
            hsJsName.add(name);
        }
        
        for (int i=0; ;i++) {
            if (i >= alOAHtmlComponent.size()) break;
            OAHtmlComponent comp = alOAHtmlComponent.get(i);
            comp.getRequiredJsNames(hsJsName);
        }        
       
        ArrayList<String> alFilePath = new ArrayList<>();
        for (String name : hsJsName) {
            String s = OAFormInsertDelegate.getJsFilePath(name);
            if (s == null) alFilePath.add("ERROR: JS name="+name+" does not have a filePath mapped to it."); 
            else if (!alFilePath.contains(s)) alFilePath.add(s);
        }

        StringBuilder sb = new StringBuilder(1024); 
        for (String s : alFilePath) {
            s = "<script type=\"text/javascript\" language=\"javascript\" src=\""+s+"\"></script>\n";
            sb.append(s);
        }
        
        return sb.toString();
    }
    
    public void setRedirect(String url) {
        this.urlRedirect = url;
    }
    public String getRedirect() {
        return urlRedirect;
    }
    public void setRedirectUrl(String url) {
        this.urlRedirect = url;
    }
    public String getRedirectUrl() {
        return urlRedirect;
    }
    
    public HtmlElement getHtmlElement(String id) {
        OAHtmlComponent comp = getComponent(id);
        if (comp == null) return null;
        HtmlElement he = hsComponment.get(comp);
        return he;
    }
    
    public OAInputText getOAInputText(String id) {
        HtmlElement he = getHtmlElement(id);
        if (he instanceof OAInputText) return (OAInputText) he;
        return null;
    }
    
    //qqqqqqqqq add others get* methods qqqqqqqqqqqqqqq

}
