// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsam.model.oa.MRADServerCommand;
import test.xice.tsam.model.oa.propertypath.AdminUserPPx;
import test.xice.tsam.model.oa.propertypath.CommandPPx;
import test.xice.tsam.model.oa.propertypath.MRADClientCommandPPx;
import test.xice.tsam.model.oa.propertypath.MRADServerPPx;
import test.xice.tsam.model.oa.propertypath.PPxInterface;
import test.xice.tsam.model.oa.propertypath.SSHExecutePPx;

import test.xice.tsam.model.oa.*;
 
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

    public CommandPPx command() {
        CommandPPx ppx = new CommandPPx(this, MRADServerCommand.P_Command);
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

    public SSHExecutePPx sshExecutes() {
        SSHExecutePPx ppx = new SSHExecutePPx(this, MRADServerCommand.P_SSHExecutes);
        return ppx;
    }

    public String id() {
        return pp + "." + MRADServerCommand.P_Id;
    }

    public String created() {
        return pp + "." + MRADServerCommand.P_Created;
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

    public String param() {
        return pp + "." + MRADServerCommand.P_Param;
    }

    public String paramInteger() {
        return pp + "." + MRADServerCommand.P_ParamInteger;
    }

    public String runOnServer() {
        return pp + ".runOnServer";
    }

    @Override
    public String toString() {
        return pp;
    }
}
 