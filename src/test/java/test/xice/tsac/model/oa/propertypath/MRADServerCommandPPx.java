// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac.model.oa.*;
 
public class MRADServerCommandPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public MRADServerCommandPPx(String name) {
        this(null, name);
    }

    public MRADServerCommandPPx(PPxInterface parent, String name) {
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

    public AdminUserPPx adminUser() {
        AdminUserPPx ppx = new AdminUserPPx(this, MRADServerCommand.P_AdminUser);
        return ppx;
    }

    public MRADClientCommandPPx mradClientCommands() {
        MRADClientCommandPPx ppx = new MRADClientCommandPPx(this, MRADServerCommand.P_MRADClientCommands);
        return ppx;
    }

    public MRADServerPPx mradServer() {
        MRADServerPPx ppx = new MRADServerPPx(this, MRADServerCommand.P_MRADServer);
        return ppx;
    }

    public SSHExecutePPx sshExecute() {
        SSHExecutePPx ppx = new SSHExecutePPx(this, MRADServerCommand.P_SSHExecute);
        return ppx;
    }

    public String id() {
        return pp + "." + MRADServerCommand.P_Id;
    }

    public String created() {
        return pp + "." + MRADServerCommand.P_Created;
    }

    public String type() {
        return pp + "." + MRADServerCommand.P_Type;
    }

    public String started() {
        return pp + "." + MRADServerCommand.P_Started;
    }

    public String error() {
        return pp + "." + MRADServerCommand.P_Error;
    }

    public String console() {
        return pp + "." + MRADServerCommand.P_Console;
    }

    public String runOnMRADServer() {
        return pp + ".runOnMRADServer";
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
