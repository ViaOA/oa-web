// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac3.model.oa.*;
 
public class WarningPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private GSMRWarningPPx gsmrWarning;
    private ServerPPx server;
     
    public WarningPPx(String name) {
        this(null, name);
    }

    public WarningPPx(PPxInterface parent, String name) {
        String s = null;
        if (parent != null) {
            s = parent.toString();
        }
        if (s == null) s = "";
        if (name != null) {
            if (s.length() > 0) s += ".";
            s += name;
        }
        pp = s;
    }

    public GSMRWarningPPx gsmrWarning() {
        if (gsmrWarning == null) gsmrWarning = new GSMRWarningPPx(this, Warning.P_GSMRWarning);
        return gsmrWarning;
    }

    public ServerPPx server() {
        if (server == null) server = new ServerPPx(this, Warning.P_Server);
        return server;
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
 