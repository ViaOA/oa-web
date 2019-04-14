// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import test.xice.tsam.model.oa.MRADServerCommand;
import test.xice.tsam.model.oa.propertypath.AdminUserPPx;
import test.xice.tsam.model.oa.propertypath.CommandPPx;
import test.xice.tsam.model.oa.propertypath.MRADClientCommandPPx;
import test.xice.tsam.model.oa.propertypath.MRADServerPPx;
import test.xice.tsam.model.oa.propertypath.SSHExecutePPx;

import test.xice.tsam.model.oa.*;
 
public class MRADServerCommandPP {
    private static AdminUserPPx adminUser;
    private static CommandPPx command;
    private static MRADClientCommandPPx mradClientCommands;
    private static MRADServerPPx mradServer;
    private static SSHExecutePPx sshExecutes;
     

    public static AdminUserPPx adminUser() {
        if (adminUser == null) adminUser = new AdminUserPPx(MRADServerCommand.P_AdminUser);
        return adminUser;
    }

    public static CommandPPx command() {
        if (command == null) command = new CommandPPx(MRADServerCommand.P_Command);
        return command;
    }

    public static MRADClientCommandPPx mradClientCommands() {
        if (mradClientCommands == null) mradClientCommands = new MRADClientCommandPPx(MRADServerCommand.P_MRADClientCommands);
        return mradClientCommands;
    }

    public static MRADServerPPx mradServer() {
        if (mradServer == null) mradServer = new MRADServerPPx(MRADServerCommand.P_MRADServer);
        return mradServer;
    }

    public static SSHExecutePPx sshExecutes() {
        if (sshExecutes == null) sshExecutes = new SSHExecutePPx(MRADServerCommand.P_SSHExecutes);
        return sshExecutes;
    }

    public static String id() {
        String s = MRADServerCommand.P_Id;
        return s;
    }

    public static String created() {
        String s = MRADServerCommand.P_Created;
        return s;
    }

    public static String started() {
        String s = MRADServerCommand.P_Started;
        return s;
    }

    public static String error() {
        String s = MRADServerCommand.P_Error;
        return s;
    }

    public static String console() {
        String s = MRADServerCommand.P_Console;
        return s;
    }

    public static String param() {
        String s = MRADServerCommand.P_Param;
        return s;
    }

    public static String paramInteger() {
        String s = MRADServerCommand.P_ParamInteger;
        return s;
    }

    public static String runOnServer() {
        String s = "runOnServer";
        return s;
    }
}
 
