/*  Copyright 1999-2015 Vince Via vvia@viaoa.com
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.viaoa.web;

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
import com.viaoa.util.OAConv;
import com.viaoa.util.OAString;

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
 * calls each component 3 times on submission: before, beforeOnSubmit, onSubmit, getReturn js script
 *
 *
 * @author vvia
 *
 */
public class OAForm extends OABase implements Serializable {
    private static Logger LOG = Logger.getLogger(OAForm.class.getName());
    
    private static final long serialVersionUID = 1L;

    protected final List<OAWebComponent> alComponent = new ArrayList<>();
    protected final List<OAWebComponent> alNewAddComponent = new ArrayList<>();

    protected OASession session;
    protected String id;
    protected String url;  // jsp name

    protected String forwardUrl;
    
    private int jsLibrary; 

    /** add script to be returned to browser on initialize. */
    public String jsAddScript;
    /** add script to be returned to browser, only once on initialize (then cleared) */
    public String jsAddScriptOnce;

    private final ArrayList<FormProcess> alProcess = new ArrayList<>(3);
    private volatile boolean bFormProcessClosed;

    protected final ArrayList<String> alRequiredCssName = new ArrayList<>();
    protected final ArrayList<String> alRequiredJsName = new ArrayList<>();

    // where to redirect after displaying any error messages
    protected String urlRedirect;
    
    private static class FormProcess {
        OAProcess p; 
        boolean bShowInDialog;
        
        FormProcess(OAProcess p) {
            this.p = p;
            bShowInDialog = true;
        }
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


    public OAForm() {
    }
    public OAForm(String id) {
        setId(id);
    }
    public OAForm(String id, String url) {
        setId(id);
        setUrl(url);
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
        for (OAWebComponent comp : alComponent) {
            comp.reset();
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

        // needs to end in ';'
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
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            if (comp.isChanged()) return true;
        }
        return false;
    }

    /** finds out the name of components that have changed */
    public OAWebComponent[] getChangedComponents() {
        ArrayList<OAWebComponent> al = new ArrayList<OAWebComponent>();

        for (int i=0; ;i++) {
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            if (comp.isChanged()) al.add(comp);
        }

        OAWebComponent[] ss = new OAWebComponent[al.size()];
        al.toArray(ss);
        return ss;
    }

    /** returns true to continue, false to not process the request */
    protected boolean beforeSubmit() {
        for (int i=0; ;i++) {
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            if (!comp._beforeFormSubmitted()) return false;
        }
        return true;
    }
    /**
     * Returns the component that initiated the submit;
     * @param req
     * @param resp
     */
    protected OAWebComponent onSubmit(HttpServletRequest req, HttpServletResponse resp, HashMap<String,String[]> hmNameValue) {
        OAWebComponent compSubmit = null;
        for (int i=0; ;i++) {
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            if (comp._onFormSubmitted(req, resp, hmNameValue)) compSubmit = comp;
        }
        return compSubmit;
    }
    protected String afterSubmit(String forwardUrl) {
        for (int i=0; ;i++) {
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            forwardUrl = comp._afterFormSubmitted(forwardUrl);
        }
        return forwardUrl;
    }

    /** called after beforeSubmit/onSubmit/afterSubmit
        This is used inside JSP to process a submit;
     */
    protected String onJspSubmit(OAWebComponent submitComponent, String forwardUrl) {
        return forwardUrl;
    }

    public String getScript() {
        beforePageLoad();
        String js = getInitScript();
        String s = getAjaxCallbackScript();
        if (s != null) js += s;
        afterPageLoad();
        
        return js;
    }

    protected void beforePageLoad() {
    }
    protected void afterPageLoad() {
    }
    
    
    // javascript code to initialize client/browser
    protected String getInitScript() {
        getSession().put("oaformLast", this);  // used by oadebug.jsp, oaenable.jsp to know the last page that was viewed

        alNewAddComponent.clear();
        
        if (!getEnabled()) return "";
        final StringBuilder sb = new StringBuilder(2048);

        int indent = 0;
        p(sb, "", indent);
        p(sb, "<script>", indent++);
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

        if (getDebug()) {
            p(sb, "$('#"+id+"').addClass('oaDebug');", indent);
            p(sb, "$('.oaBindable').addClass('oaDebug');", indent);
        }
        else {
            p(sb, "$('#"+id+"').removeClass('oaDebug');", indent);
            p(sb, "$('.oaBindable').removeClass('oaDebug');", indent);
        }

        for (int i=0; ;i++) {
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            String s = comp.getScript();

            if (getDebug()) p(sb, "$('#"+comp.getId()+"').addClass('oaDebug');", indent);
            else p(sb, "$('#"+comp.getId()+"').removeClass('oaDebug');", indent);

            if (!OAString.isEmpty(s)) p(sb, s + "", indent);
            if (comp instanceof OAJspMultipartInterface) {
                p(sb, "$('#"+id+"').attr('enctype', 'multipart/form-data');", indent);
                p(sb, "$('#"+id+"').attr('action', 'oaform.jsp?oaform="+getId()+"');", indent);
            }
        }
        getMessages(sb, indent);
        p(sb, "", indent);

        // add jquery.validate
        if (addValidation) {
            p(sb, "if ($.validator) {", indent);
            p(sb, "  $('#"+id+"').validate({", ++indent);
    
            p(sb, "  onsubmit: false", ++indent);
            
            boolean b = false;
            for (int i=0; ;i++) {
                if (i >= alComponent.size()) break;
                OAWebComponent comp = alComponent.get(i);
                String s = comp.getScript();
    
                // todo:  add other components 
                if (comp instanceof OATextField) {
                    s = ((OATextField) comp).getValidationRules();
                    if (OAString.isNotEmpty(s)) {
                        if (!b) {
                            b = true;
                            p(sb, ", rules: {", indent);
                            sb.append(s);
                        }
                        else p(sb, ", " +s, indent);
                    }
                }
            }
            if (b) {
                p(sb, "}", --indent);
            }            
            
            b = false;
            for (int i=0; ;i++) {
                if (i >= alComponent.size()) break;
                OAWebComponent comp = alComponent.get(i);
                String s = comp.getScript();
    
                // todo:  add other components qqqqqqqqqqq 
                if (comp instanceof OATextField) {
                    s = ((OATextField) comp).getValidationMessages();
                    if (OAString.isNotEmpty(s)) {
                        if (!b) {
                            b = true;
                            p(sb, ", messages: {", indent);
                            sb.append(s);
                        }
                        else p(sb, ", " +s, indent);
                    }
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
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
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
        p(sb, "        if (i % 3 == 0) msg += '<br>';", indent);
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
        p(sb, "}", --indent);

        p(sb, "function ajaxSubmit2(cmdName) {", indent);
        p(sb, "  var args = $('#"+id+"').serialize();", ++indent);
        p(sb, "  if (cmdName != undefined && cmdName) args = cmdName + '=1&' + args;", indent);
        p(sb, "  $.ajax({", indent);
        p(sb, "    type: 'POST',", ++indent);
        p(sb, "    data: args,", indent);
        p(sb, "    url: 'oaajax.jsp',", indent);
        p(sb, "    success: function(data) {if (data) eval(data);},", indent);
        p(sb, "    dataType: 'text'", indent);
        p(sb, "  });", --indent);
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
        
        p(sb, "});", --indent); // end jquery.ready ****


        p(sb, "</script>", --indent);
        js = sb.toString();

        return js;
    }

    /**
     * Use to get any changes since the last time getScript was called.
     */
    public String getUpdateScript() {
        String s = getAjaxScript();
        return s;
    }    
    
    private boolean bLastDebug;
    /**
     * same as calling {@link #getUpdateScript()}
     */
    public String getAjaxScript() {
        if (!getEnabled()) return "";
        StringBuilder sb = new StringBuilder(1024);

        int indent = 0;
        boolean bDebugx = getDebug();
        if (bLastDebug != bDebugx) {
            if (bDebugx) {
                p(sb, "    $('#"+id+"').addClass('oaDebug');", indent);
                p(sb, "    $('.oaBindable').addClass('oaDebug');", indent);
            }
            else {
                p(sb, "    $('#"+id+"').removeClass('oaDebug');", indent);
                p(sb, "    $('.oaBindable').removeClass('oaDebug');", indent);
            }
        }

        for (int i=0; ;i++) {
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            
            String s;
            if (alNewAddComponent.contains(comp)) {
                s = comp.getScript();
            }
            else {
                s = comp.getAjaxScript();
            }
            
            if (!OAString.isEmpty(s)) p(sb, s + "", 1);
            if (bLastDebug != bDebugx) {
                if (bDebugx) p(sb, "    $('#"+comp.getId()+"').addClass('oaDebug');", indent);
                else p(sb, "    $('#"+comp.getId()+"').removeClass('oaDebug');", indent);
            }
        }
        alNewAddComponent.clear();

        
        p(sb, "$('#oacommand').val('');", indent); // set back to blank
        p(sb, "$('#oaparam').val('');", indent); // set back to blank

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
        
        bLastDebug = bDebugx;
        return js;
    }

    /**
     * Used to show popup showing any oaprocesses that are running
     * @see #addProcess(OAProcess)
     */
    protected void getProcessingScript(final StringBuilder sb, int indent) {
        // final StringBuilder sb = new StringBuilder(1024);

        boolean b = false;
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
            
            b = true;
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
                perc = (int) ( ( (step1-1)/((double)step2) ) * 100.0);
            }
            
            String[] ss = p.getSteps();
            if (ss != null && step1 > 0 && (step1-1) < ss.length) step = ss[step1-1];
        }
        
        p(sb, "if ($().modal) {", indent++);  // qqqqqqqq removed once jquery-ui is no longer used
        if (!b) {
            p(sb, "$('#oaFormProcess').modal('hide');", indent);
        }
        else {
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

    protected transient ArrayList<String> alConsole = new ArrayList<String>(5);
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
            if (id != null) {
                p(sb, "$('#"+id+"').hide();", indent);
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
    
    public List<OAWebComponent> getComponents() {
        return alComponent;
    }

    public OAWebComponent getComponent(String id) {
        return getComponent(id, null);
    }
    public OAWebComponent getComponent(String id, Class c) {
        if (id == null) return null;
        for (int i=0; ;i++) {
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            if (id.equalsIgnoreCase(comp.getId())) {
                if (c == null || c.isAssignableFrom(comp.getClass())) {
                    return comp;
                }
            }
        }
        return null;
    }
    public OAWebComponent[] getComponents(String id) {
        if (id == null) return null;
        ArrayList<OAWebComponent> al = new ArrayList<>();
        for (int i=0; ;i++) {
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            if (id.equalsIgnoreCase(comp.getId())) {
                al.add(comp);
            }
        }
        OAWebComponent[] jcs = al.toArray(new OAWebComponent[0]);
        return jcs;
    }

    public void remove(String name) {
        OAWebComponent comp = getComponent(name);
        if (comp != null) alComponent.remove(comp);
        super.remove(name);
    }
    public void add(OAWebComponent comp) {
        if (comp == null) return;
        String id = comp.getId();

        if (!OAString.isEmpty(id)) {
            OAWebComponent compx = getComponent(id);
            
            if (compx != null) {
                if ( comp.getClass().isAssignableFrom(compx.getClass()) || compx.getClass().isAssignableFrom(comp.getClass()) ) {
                    remove(id);
                }
            }
        }
        if (!alComponent.contains(comp)) {
            alComponent.add(comp);
        }
        if (!alNewAddComponent.contains(comp)) {
            alNewAddComponent.add(comp);
        }
        comp.setForm(this);
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
    
    /** called to process the form.
     *  See oaform.jsp
     */
    public String processSubmit(OASession session, HttpServletRequest request, HttpServletResponse response) {
        if (this.session == null) this.session = session;
        try {
            lockSubmit.writeLock().lock();
            return _processSubmit(session, request, response);
        }
        finally {
            lockSubmit.writeLock().unlock();
        }
    }    
    protected String _processSubmit(OASession session, HttpServletRequest request, HttpServletResponse response) {
        if (this.session == null) this.session = session;

        try {
            // Thread.sleep(350); //qqqqq test delay            
            request.setCharacterEncoding("UTF-8");
        }
        catch (Exception e) {}


        HashMap<String, String[]> hmNameValue = new HashMap<String, String[]>();

        
        String contentType = request.getContentType();
        if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
            try {
                processMultipart(request, hmNameValue);
            }
            catch (Exception e){
                this.addErrorMessage(e.toString());
            }
        }
        else {
            Enumeration enumx = request.getParameterNames();
            while ( enumx.hasMoreElements()) {
                String name = (String) enumx.nextElement();
                String[] values = request.getParameterValues(name);
                hmNameValue.put(name,values);
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
            String s = request.getHeader("Accept-Language");
            session.setBrowserInfo(jsDate, OAConv.toInt(jsTzRawOffset), OAConv.toBoolean(jsDateSupportsDST), s);
        }
        catch (Exception e) {
            LOG.log(Level.WARNING, "error setting browser info, jsDate="+jsDate+", jsTzRawOffset="+jsTzRawOffset+", jsDateSupportsDST="+jsDateSupportsDST, e);
        }
        
        boolean bShouldProcess = beforeSubmit();

        String forward = null;

        if (bShouldProcess) {
            forward = getForwardUrl();
            if (OAString.isEmpty(forward)) forward = this.getUrl();
            
            OAWebComponent compSubmit = onSubmit(request, response, hmNameValue);
            
            forward = onSubmit(compSubmit, forward);
            forward = afterSubmit(forward);
            forward = onJspSubmit(compSubmit, forward);
        }
        if (OAString.isEmpty(forward)) {
            forward = this.getUrl();
        }
        return forward;
    }

    protected String onSubmit(OAWebComponent compSubmit, String forward) {
        if (compSubmit != null) {
            String s = compSubmit.getForwardUrl();
            if (s != null) forward = s;
            compSubmit._beforeOnSubmit();
            forward = compSubmit._onSubmit(forward);
        }
        return forward;
        
    }
    
    public String processForward(OASession session, HttpServletRequest request, HttpServletResponse response) {
        HashMap<String,String[]> hmNameValue = new HashMap<String, String[]>();
        Enumeration enumx = request.getParameterNames();
        while ( enumx.hasMoreElements()) {
            String name = (String) enumx.nextElement();
            String[] values = request.getParameterValues(name);
            hmNameValue.put(name,values);
        }
        return processForward(session, request, response, null);
    }

    
    /**
     * Called by oaforward.jsp to be able to have a link call submit method without doing a form submit.
     */
    public String processForward(OASession session, HttpServletRequest request, HttpServletResponse response, HashMap<String,String[]> hmNameValue) {
        if (this.session == null) this.session = session;
        try {
            request.setCharacterEncoding("UTF-8");
        }
        catch (Exception e) {}

        String id = request.getParameter("oacommand");
        OAWebComponent comp = getComponent(id);
        if (comp == null) return getUrl();

        String forward = comp.getForwardUrl();

        comp._onFormSubmitted(request, response, hmNameValue);
        comp._beforeOnSubmit();
        forward = comp._onSubmit(forward);
    
        if (OAString.isEmpty(forward)) {
            forward = this.getUrl();
        }
        
        return forward;
    }


    // Parse Multipart posted forms ============================================================
    protected void processMultipart(ServletRequest request, HashMap<String, String[]> hmNameValue) throws Exception {
        int len = request.getContentLength();
        if (len <= 1) return;
        String contentType = request.getContentType();
        String sep = "--" + contentType.substring(contentType.indexOf("boundary=")+9);
        sep += "\r\n";

        BufferedInputStream bis = new BufferedInputStream(request.getInputStream());

        for (int i=0;;i++) {
            String s = getNextMultipart(bis, null, sep);
            if (s == null) break;

            /*
                Content-Disposition: form-data; name="txtCreate"\r\n10/21/2008\r\n
                Content-Disposition: form-data; name="fiFile"; filename=""
                 ; filename=""
            */
            String[] nameValue = processMultipart(s);
            /*
                [0]=txtCreate [1]=10/21/2008
                 [0]=fiFile [1]=; filename="budget.txt"
            */

            if (nameValue == null) continue;
            String name = nameValue[0];
            String[] values = (String[]) hmNameValue.get(name);
            if (values == null) hmNameValue.put(name, new String[] { nameValue[1] });
            else {
                String[] newValues = new String[values.length+1];
                System.arraycopy(values,0,newValues,0,values.length);
                newValues[values.length] = nameValue[1];
                hmNameValue.put(name,newValues);
            }

            // see if this was an OAFileInput component
            OAWebComponent comp = getComponent(name);
            if (comp == null) continue;
            if (!(comp instanceof OAJspMultipartInterface)) continue;

            if (nameValue.length < 2) continue;
            String fname = nameValue[1];
            int x = fname.indexOf('\"');
            if (x >= 0) fname = fname.substring(x+1);
            fname = com.viaoa.util.OAString.convert(fname, "\"", null);
            if (OAString.isEmpty(fname)) continue;

            OutputStream os = ((OAJspMultipartInterface)comp).getOutputStream(len, fname);
            if (os == null) {
                os = new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        // no op
                    }
                };
            }
            BufferedOutputStream bos = new BufferedOutputStream(os);
            getNextMultipart(bis, bos, "\r\n"+sep);  // this will write to bos
            bos.flush();
            bos.close();
        }
        bis.close();
    }

    protected String[] processMultipart(String line) {
        String s = "Content-Disposition: form-data; name=";
        int pos = line.indexOf(s);

        // Content-Disposition: form-data; name="txtText"[13][10][13][10]test[13][10]

        if (pos < 0) return null;

        line = line.substring(pos + s.length());
        // "txtText"[13][10][13][10]test[13][10]

        pos = line.indexOf('\r');
        if (pos < 0) {
            pos = line.indexOf('\n');
            if (pos < 0) {
                pos = line.indexOf("; ");
                if (pos < 0) return null;
            }
        }

        String name = line.substring(0,pos);
        // "txtText"

        name = name.replace('"',' ');
        name = name.trim();  // txtText


        String value = line.substring(pos);
        // [13][10][13][10]test[13][10]


        // skip 2 CRLF
        for (int j=0;j < 4 && value.length() > 0;) {
            char c = value.charAt(0);
            if (c == '\n' || c == '\r') {
                value = value.substring(1);
                j++;
            }
            else break;
        }
        // test[13][10]


        pos = value.indexOf('\r');
        if (pos >= 0) value = value.substring(0,pos);
        // test

        return new String[] { name, value };
    }


    /* returns all data up to sep and "eats" the sep */
    protected String getNextMultipart(BufferedInputStream bis, BufferedOutputStream bos, String sep) throws IOException {
        if (sep == null) return null;
        StringBuffer sb = new StringBuffer(1024);
        int c=0;
        boolean eof = false;

        String sep2 = null;
        if (bos == null) sep2 = "\r\nContent-Type:";  // this marks the beginning of a file

        int sepLen = sep.length();
        int sep2Len = (sep2!=null)?sep2.length():0;

        for (;;) {
            c = bis.read();
            if (c < 0) {
                eof = true;
                break;
            }

            if (sep2 != null && c == sep2.charAt(0)) {
                int hold = c;
                bis.mark(sep2Len+1);
                int j=1;
                for (;j<sep2Len ; j++) {
                    c = bis.read();
                    if (c != sep2.charAt(j)) break;
                }
                if (j == sep2Len) {
                    // goto end of 2nd LF
                    for (j=0; j<2;) {
                        c = bis.read();
                        if (c == '\n') j++;
                    }
                    break;
                }
                bis.reset();
                c = hold;
            }

            if (c == sep.charAt(0)) {
                int hold = c;
                bis.mark(sepLen+1);
                int j=1;
                for ( ; j<sepLen; j++) {
                    c = bis.read();
                    if (c != sep.charAt(j)) break;
                }
                if (j == sepLen) break;
                bis.reset();
                c = hold;
            }

            if (bos != null) bos.write(c);
            else sb.append((char)c);
        }
        if (eof && sb.length() == 0) return null;
        return new String(sb);
    }



    public OALabel getLabel(String id) {
        OAWebComponent comp = getComponent(id, OALabel.class);
        if (comp instanceof OALabel) return (OALabel) comp;
        return null;
    }
    public OATextField getTextField(String id) {
        OAWebComponent comp = getComponent(id, OATextField.class);
        if (comp instanceof OATextField) return (OATextField) comp;
        return null;
    }
    public OAPassword getPassword(String id) {
        OAWebComponent comp = getComponent(id, OAPassword.class);
        if (comp instanceof OAPassword) return (OAPassword) comp;
        return null;
    }
    public OAButton getButton(String id) {
        OAWebComponent comp = getComponent(id, OAButton.class);
        if (comp instanceof OAButton) return (OAButton) comp;
        return null;
    }
    public OAFileInput getFileInput(String id) {
        OAWebComponent comp = getComponent(id, OAButton.class);
        if (comp instanceof OAFileInput) return (OAFileInput) comp;
        return null;
    }
    
    public OAButtonList getButtonList(String id) {
        OAWebComponent comp = getComponent(id, OAButtonList.class);
        if (comp instanceof OAButtonList) return (OAButtonList) comp;
        return null;
    }
    public OAHtmlElement getHtmlElement(String id) {
        OAWebComponent comp = getComponent(id, OAHtmlElement.class);
        if (comp instanceof OAHtmlElement) return (OAHtmlElement) comp;
        return null;
    }
    public OATextArea getTextArea(String id) {
        OAWebComponent comp = getComponent(id, OATextArea.class);
        if (comp instanceof OATextArea) return (OATextArea) comp;
        return null;
    }
    public OACombo getCombo(String id) {
        OAWebComponent comp = getComponent(id, OACombo.class);
        if (comp instanceof OACombo) return (OACombo) comp;
        return null;
    }
    public OATable getTable(String id) {
        OAWebComponent comp = getComponent(id, OATable.class);
        if (comp instanceof OATable) return (OATable) comp;
        return null;
    }
    public OALink getLink(String id) {
        OAWebComponent comp = getComponent(id, OALink.class);
        if (comp instanceof OALink) return (OALink) comp;
        return null;
    }
    public OACheckBox getCheckBox(String id) {
        OAWebComponent comp = getComponent(id, OACheckBox.class);
        if (comp instanceof OACheckBox) return (OACheckBox) comp;
        return null;
    }
    public OAGrid getGrid(String id) {
        OAWebComponent comp = getComponent(id, OAGrid.class);
        if (comp instanceof OAGrid) return (OAGrid) comp;
        return null;
    }
    public OAHtmlSelect getSelect(String id) {
        OAWebComponent comp = getComponent(id, OAHtmlSelect.class);
        if (comp instanceof OAHtmlSelect) return (OAHtmlSelect) comp;
        return null;
    }
    public OAImage getImage(String id) {
        OAWebComponent comp = getComponent(id, OAImage.class);
        if (comp instanceof OAImage) return (OAImage) comp;
        return null;
    }
    public OARadio getRadio(String id) {
        OAWebComponent comp = getComponent(id, OARadio.class);
        if (comp instanceof OARadio) return (OARadio) comp;
        return null;
    }
    public OAServletImage getServletImage(String id) {
        OAWebComponent comp = getComponent(id, OAServletImage.class);
        if (comp instanceof OAServletImage) return (OAServletImage) comp;
        return null;
    }
    public OAList getList(String id) {
        OAWebComponent comp = getComponent(id, OAList.class);
        if (comp instanceof OAList) return (OAList) comp;
        return null;
    }
    public OADialog getDialog(String id) {
        OAWebComponent comp = getComponent(id, OADialog.class);
        if (comp instanceof OADialog) return (OADialog) comp;
        return null;
    }
    public OAPopup getPopup(String id) {
        OAWebComponent comp = getComponent(id, OAPopup.class);
        if (comp instanceof OAPopup) return (OAPopup) comp;
        return null;
    }
    public OAPopupList getPopupList(String id) {
        OAWebComponent comp = getComponent(id, OAPopupList.class);
        if (comp instanceof OAPopupList) return (OAPopupList) comp;
        return null;
    }

    
    protected final ArrayList<String> alCss = new ArrayList<>();
    /**
     * filepath to include on CSS files on the page
     * @param filePath full path of page (relative to webcontent)
     */
    public void addCss(String filePath) {
        if (filePath == null) return;
        if (!alCss.contains(filePath)) alCss.add(filePath);
        
    }

    protected final ArrayList<String> alJs = new ArrayList<>();
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
     * @see OAJspDelegate#registerRequiredCssName(String, String) 
     */
    public void addRequiredCssName(String name) {
        if (name == null) return;
        name = name.toUpperCase();
        if (!alRequiredCssName.contains(name)) alRequiredCssName.add(name);
        
    }
    /**
     * add names of JS to include on page
     * @param name of js to include,  
     * @see OAJspDelegate#registerRequiredJsName(String, String) 
     */
    public void addRequiredJsName(String name) {
        if (name == null) return;
        name = name.toUpperCase();
        if (!alRequiredJsName.contains(name)) alRequiredJsName.add(name);
    }
    
    
    public String getCssInsert() {
        ArrayList<String> alName = new ArrayList<>();
        
        for (int i=0; ;i++) {
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            String[] ss = comp.getRequiredCssNames();
            if (ss == null) continue;

            for (String s : ss) {
                if (!alName.contains(s.toUpperCase())) alName.add(s.toUpperCase());
            }
        }
        
        // include oajsp.css after components
        if (!alName.contains(OAJspDelegate.CSS_oajsp.toUpperCase())) alName.add(OAJspDelegate.CSS_oajsp.toUpperCase());

        for (String s : alRequiredCssName) {
            if (!alName.contains(s.toUpperCase())) alName.add(s.toUpperCase());
        }
        
        ArrayList<String> alFilePath = new ArrayList<>();

        for (String s : alName) {
            s = OAJspDelegate.getCssFilePath(s);
            if (s != null && !alFilePath.contains(s)) alFilePath.add(s);
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
        ArrayList<String> alName = new ArrayList<>();
        
        if (!alName.contains(OAJspDelegate.JS_jquery)) alName.add(OAJspDelegate.JS_jquery);
        if (!alName.contains(OAJspDelegate.JS_jquery_ui)) alName.add(OAJspDelegate.JS_jquery_ui);
        
        if (addValidation) {
            if (!alName.contains(OAJspDelegate.JS_jquery_validation)) alName.add(OAJspDelegate.JS_jquery_validation);
        }
        
        for (String s : alRequiredJsName) {
            s = s.toUpperCase();
            if (!alName.contains(s)) alName.add(s);
        }
        
        for (int i=0; ;i++) {
            if (i >= alComponent.size()) break;
            OAWebComponent comp = alComponent.get(i);
            String[] ss = comp.getRequiredJsNames();
            if (ss == null) continue;

            for (String s : ss) {
                s = s.toUpperCase();
                if (!alName.contains(s)) alName.add(s);
            }
        }        
        
        ArrayList<String> alFilePath = new ArrayList<>();

        for (String s : alName) {
            s = OAJspDelegate.getJsFilePath(s);
            if (s != null && !alFilePath.contains(s)) alFilePath.add(s);
        }
        
        for (String s : alJs) {
            if (s != null && !alFilePath.contains(s)) alFilePath.add(s);
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
    

    //qqqqqq 20171105 temp
    public boolean addValidation=false;
}
