// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class ServerInstallPP {
    private static EnvironmentPPx calcEnvironment;
    private static ServerTypeVersionPPx newServerTypeVersion;
    private static ServerPPx server;
     

    public static EnvironmentPPx calcEnvironment() {
        if (calcEnvironment == null) calcEnvironment = new EnvironmentPPx(ServerInstall.P_CalcEnvironment);
        return calcEnvironment;
    }

    public static ServerTypeVersionPPx newServerTypeVersion() {
        if (newServerTypeVersion == null) newServerTypeVersion = new ServerTypeVersionPPx(ServerInstall.P_NewServerTypeVersion);
        return newServerTypeVersion;
    }

    public static ServerPPx server() {
        if (server == null) server = new ServerPPx(ServerInstall.P_Server);
        return server;
    }

    public static String id() {
        String s = ServerInstall.P_Id;
        return s;
    }

    public static String created() {
        String s = ServerInstall.P_Created;
        return s;
    }

    public static String downloadedZip() {
        String s = ServerInstall.P_DownloadedZip;
        return s;
    }

    public static String propagated() {
        String s = ServerInstall.P_Propagated;
        return s;
    }

    public static String installed() {
        String s = ServerInstall.P_Installed;
        return s;
    }

    public static String cancelled() {
        String s = ServerInstall.P_Cancelled;
        return s;
    }

    public static String errored() {
        String s = ServerInstall.P_Errored;
        return s;
    }

    public static String errorMessage() {
        String s = ServerInstall.P_ErrorMessage;
        return s;
    }

    public static String completed() {
        String s = ServerInstall.P_Completed;
        return s;
    }
}
 
