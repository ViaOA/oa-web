// Generated by OABuilder
package test.xice.tsac2.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac2.model.oa.*;
 
public class WarningPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public WarningPPx(String name) {
        this(null, name);
    }

    public WarningPPx(PPxInterface parent, String name) {
        String s = null;
        if (parent != null) {
            s = parent.toString();
        }
        if (s == null) s = "";
        if (name != null && name.length() > 0) {
            if (s.length() > 0 && name.charAt(0) != ':') s += ".";
            s += name;
        }
        pp = s;
    }

    public GSMRWarningPPx gsmrWarning() {
        GSMRWarningPPx ppx = new GSMRWarningPPx(this, Warning.P_GSMRWarning);
        return ppx;
    }

    public String id() {
        return pp + "." + Warning.P_Id;
    }

    public String created() {
        return pp + "." + Warning.P_Created;
    }

    public String message() {
        return pp + "." + Warning.P_Message;
    }

    public String exceptionMessage() {
        return pp + "." + Warning.P_ExceptionMessage;
    }

    public String stackTrace() {
        return pp + "." + Warning.P_StackTrace;
    }

    public String actionTaken() {
        return pp + "." + Warning.P_ActionTaken;
    }

    public String actionComment() {
        return pp + "." + Warning.P_ActionComment;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
