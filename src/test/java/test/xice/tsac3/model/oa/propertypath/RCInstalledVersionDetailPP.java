// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class RCInstalledVersionDetailPP {
    private static RCInstalledVersionPPx rcInstalledVersion;
    private static ServerPPx server;
    private static ServerTypeVersionPPx serverTypeVersion;
     

    public static RCInstalledVersionPPx rcInstalledVersion() {
        if (rcInstalledVersion == null) rcInstalledVersion = new RCInstalledVersionPPx(RCInstalledVersionDetail.P_RCInstalledVersion);
        return rcInstalledVersion;
    }

    public static ServerPPx server() {
        if (server == null) server = new ServerPPx(RCInstalledVersionDetail.P_Server);
        return server;
    }

    public static ServerTypeVersionPPx serverTypeVersion() {
        if (serverTypeVersion == null) serverTypeVersion = new ServerTypeVersionPPx(RCInstalledVersionDetail.P_ServerTypeVersion);
        return serverTypeVersion;
    }

    public static String id() {
        String s = RCInstalledVersionDetail.P_Id;
        return s;
    }

    public static String selected() {
        String s = RCInstalledVersionDetail.P_Selected;
        return s;
    }

    public static String hostName() {
        String s = RCInstalledVersionDetail.P_HostName;
        return s;
    }

    public static String packageName() {
        String s = RCInstalledVersionDetail.P_PackageName;
        return s;
    }

    public static String installedDate() {
        String s = RCInstalledVersionDetail.P_InstalledDate;
        return s;
    }

    public static String version() {
        String s = RCInstalledVersionDetail.P_Version;
        return s;
    }

    public static String error() {
        String s = RCInstalledVersionDetail.P_Error;
        return s;
    }

    public static String invalidMessage() {
        String s = RCInstalledVersionDetail.P_InvalidMessage;
        return s;
    }

    public static String loaded() {
        String s = RCInstalledVersionDetail.P_Loaded;
        return s;
    }
}
 
