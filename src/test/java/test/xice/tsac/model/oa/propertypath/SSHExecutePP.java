// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import test.xice.tsac.model.oa.*;
 
public class SSHExecutePP {
    private static MRADClientCommandPPx mradClientCommand;
    private static MRADServerCommandPPx mradServerCommand;
     

    public static MRADClientCommandPPx mradClientCommand() {
        if (mradClientCommand == null) mradClientCommand = new MRADClientCommandPPx(SSHExecute.P_MRADClientCommand);
        return mradClientCommand;
    }

    public static MRADServerCommandPPx mradServerCommand() {
        if (mradServerCommand == null) mradServerCommand = new MRADServerCommandPPx(SSHExecute.P_MRADServerCommand);
        return mradServerCommand;
    }

    public static String id() {
        String s = SSHExecute.P_Id;
        return s;
    }

    public static String created() {
        String s = SSHExecute.P_Created;
        return s;
    }

    public static String hostName() {
        String s = SSHExecute.P_HostName;
        return s;
    }

    public static String userId() {
        String s = SSHExecute.P_UserId;
        return s;
    }

    public static String commandLine() {
        String s = SSHExecute.P_CommandLine;
        return s;
    }

    public static String started() {
        String s = SSHExecute.P_Started;
        return s;
    }

    public static String connected() {
        String s = SSHExecute.P_Connected;
        return s;
    }

    public static String authenticated() {
        String s = SSHExecute.P_Authenticated;
        return s;
    }

    public static String completed() {
        String s = SSHExecute.P_Completed;
        return s;
    }

    public static String successful() {
        String s = SSHExecute.P_Successful;
        return s;
    }

    public static String output() {
        String s = SSHExecute.P_Output;
        return s;
    }

    public static String error() {
        String s = SSHExecute.P_Error;
        return s;
    }

    public static String console() {
        String s = SSHExecute.P_Console;
        return s;
    }
}
 