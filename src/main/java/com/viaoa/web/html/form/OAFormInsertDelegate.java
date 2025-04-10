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

import java.util.HashMap;

import com.viaoa.util.OAString;

public class OAFormInsertDelegate {

    // list of known css files that oajsp components use.
    // public static final String CSS_jquery_ui = "jquery-ui";
 // public static final String CSS_jquery_ui_basetheme = "jquery-ui-basetheme";
 // public static final String CSS_jquery_ui_theme = "jquery-ui-theme";
 // public static final String CSS_jquery_timepicker = "jquery-timepicker";
 // public static final String CSS_bootstrap_treeview = "bootstrap-treeview";
    public static final String CSS_bootstrap = "bootstrap";
 // public static final String CSS_bootstrap_theme = "bootstrap-theme";
 // public static final String CSS_bootstrap_datetimepicker = "bootstrap-datetimepicker";
 // public static final String CSS_bootstrap_tagsinput = "bootstrap-tagsinput";
 // public static final String CSS_bootstrap_typeahead = "bootstrap-typeahead";
 // public static final String CSS_bootstrap_select = "bootstrap-select";
 // public static final String CSS_bootstrap_ladda = "bootstrap-ladda";
 // public static final String CSS_summernote = "summernote";
    public static final String CSS_oaweb= "oaweb";
 // public static final String CSS_croppie = "croppie";

    /**
     * name/file location for CSS resources.
     * Used by OAJSP components to map requirements
     */
    protected static final HashMap<String, String> hmCssFilePath = new HashMap<>();
    /**
     * name/file location for JS resources.
     * Used by OAJSP components to map requirements
     */
    protected static final HashMap<String, String> hmJsFilePath = new HashMap<>();
    
//qqqqqqqqqqq verify/update these qqqqqqqqqqqqqqq    
    
    static {
        // registerRequiredCssName(CSS_jquery_ui, "vendor/jquery-ui-1.12.1/jquery-ui.css");
        // registerRequiredCssName(CSS_jquery_ui_basetheme, "vendor/jquery-ui-1.12.1/jquery-ui.theme.css");
        // registerRequiredCssName(CSS_jquery_ui_theme, "vendor/jquery-ui-themes-1.12.1/themes/base/theme.css");
        // registerRequiredCssName(CSS_jquery_timepicker, "vendor/jquery-timepicker/timepicker.css");
        // registerRequiredCssName(CSS_bootstrap_treeview, "vendor/bootstrap-treeview/bootstrap-treeview.css");
        registerRequiredCssName(CSS_bootstrap, "vendor/bootstrap-5.3.3/css/bootstrap.css");
        // registerRequiredCssName(CSS_bootstrap_theme, "vendor/bootstrap-5.3.3/css/bootstrap-theme.css");
        // registerRequiredCssName(CSS_bootstrap_datetimepicker, "vendor/bootstrap-datetimepicker/bootstrap-datetimepicker.css");
        // registerRequiredCssName(CSS_bootstrap_tagsinput, "vendor/bootstrap-tagsinput/bootstrap-tagsinput.css");
        // registerRequiredCssName(CSS_bootstrap_typeahead, "vendor/bootstrap-typeahead/bootstrap-typeahead.css");
        // registerRequiredCssName(CSS_bootstrap_select, "vendor/bootstrap-select/css/bootstrap-select.css");
        // registerRequiredCssName(CSS_bootstrap_ladda, "vendor/bootstrap-spinner/ladda-themeless.css");
        // registerRequiredCssName(CSS_summernote, "vendor/summernote/summernote.css");
        
        
        registerRequiredCssName(CSS_oaweb, "vendor/viaoa/oaweb.css");
        // registerRequiredCssName(CSS_croppie, "vendor/Croppie-2.6.2/croppie.css");
    }
    

    // list of known js files that oajsp components use.
    // public static final String JS_jquery = "jquery";
    // public static final String JS_jquery_ui = "jquery-ui";
    // public static final String JS_jquery_maskedinput = "jquery-maskedinput";
    // public static final String JS_jquery_timepicker = "jquery-timepicker";
    public static final String JS_bootstrap = "bootstrap";
    // public static final String JS_bootstrap_treeview = "bootstrap-treeview";
    // public static final String JS_moment = "moment";
    // public static final String JS_bootstrap_datetimepicker = "bootstrap-datetimepicker";
    // public static final String JS_bootstrap_tagsinput = "bootstrap-tagsinput";
 // public static final String JS_bootstrap_typeahead = "bootstrap-typeahead";
 // public static final String JS_bootstrap_select = "bootstrap-select";
 // public static final String JS_bootstrap_ladda = "bootstrap-ladda";
 // public static final String JS_bootstrap_spin = "bootstrap-spin";
 // public static final String JS_jquery_slimscroll = "jquery-slimscroll";
 // public static final String JS_summernote = "summernote";
 // public static final String JS_jquery_autonumeric = "jquery-autonumeric";
 // public static final String JS_jquery_validation = "jquery-validation";
 // public static final String JS_croppie = "croppie";
    
    static {
     // registerRequiredJsName(JS_jquery, "vendor/jquery-1.12.4/jquery.js");
     // registerRequiredJsName(JS_jquery_ui, "vendor/jquery-ui-1.12.1/jquery-ui.js");
     // registerRequiredJsName(JS_jquery_maskedinput, "vendor/jquery-maskedinput/maskedinput.js");
     // registerRequiredJsName(JS_jquery_timepicker, "vendor/jquery-timepicker/timepicker.js");
        registerRequiredJsName(JS_bootstrap, "vendor/bootstrap-3.4.1/js/bootstrap.js");
        
     // registerRequiredJsName(JS_bootstrap_treeview, "vendor/bootstrap-treeview/bootstrap-treeview.js");
     // registerRequiredJsName(JS_moment, "vendor/moment/moment.min.js");
     // registerRequiredJsName(JS_bootstrap_datetimepicker, "vendor/bootstrap-datetimepicker/bootstrap-datetimepicker.js");
     // registerRequiredJsName(JS_bootstrap_tagsinput, "vendor/bootstrap-tagsinput/bootstrap-tagsinput.js");
     // registerRequiredJsName(JS_bootstrap_typeahead, "vendor/bootstrap-typeahead/bootstrap-typeahead.js");
     // registerRequiredJsName(JS_bootstrap_select, "vendor/bootstrap-select/js/bootstrap-select.js");
     // registerRequiredJsName(JS_bootstrap_ladda, "vendor/bootstrap-spinner/ladda.js");
     // registerRequiredJsName(JS_bootstrap_spin, "vendor/bootstrap-spinner/spin.js");
     // registerRequiredJsName(JS_jquery_slimscroll, "vendor/jquery-slimscroll/jquery.slimscroll.js");
     // registerRequiredJsName(JS_summernote, "vendor/summernote/summernote.js");
     // registerRequiredJsName(JS_jquery_autonumeric, "vendor/jquery-autonumeric/autoNumeric.js");
     // registerRequiredJsName(JS_jquery_validation, "vendor/jquery-validation/jquery.validate.js");
     // registerRequiredJsName(JS_croppie, "vendor/Croppie-2.6.2/croppie.min.js");
    }

    
    /**
     * Add a name / filepath of a CSS.
     * @param name lookup name, not case sensitive
     * @param filePath location of file, relative to webcontent
     */
    public static void registerRequiredCssName(String name, String filePath) {
        if (OAString.isEmpty(name)) return;
        hmCssFilePath.put(name.toUpperCase(), filePath);
    }
    public static String getCssFilePath(String name) {
        if (OAString.isEmpty(name)) return null;
        return hmCssFilePath.get(name.toUpperCase());
    }
    

    /**
     * Add a name / filepath of a JS.
     * @param name lookup name, not case sensitive
     * @param filePath location of file, relative to webcontent
     */
    public static void registerRequiredJsName(String name, String filePath) {
        if (OAString.isEmpty(name)) return;
        hmJsFilePath.put(name.toUpperCase(), filePath);
    }
    public static String getJsFilePath(String name) {
        if (OAString.isEmpty(name)) return null;
        return hmJsFilePath.get(name.toUpperCase());
    }

    public static final String LOCATION_Top = "top";
    public static final String LOCATION_Right = "right";
    public static final String LOCATION_Bottom = "bottom";
    public static final String LOCATION_Left = "left";
    
    
}
