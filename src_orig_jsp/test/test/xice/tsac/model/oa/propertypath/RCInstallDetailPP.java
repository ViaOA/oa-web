// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import test.xice.tsac.model.oa.*;
 
public class RCInstallDetailPP {
    private static PackageVersionPPx afterPackageVersion;
    private static PackageVersionPPx beforePackageVersion;
    private static RCDeployDetailPPx rcDeployDetail;
    private static RCInstallPPx rcInstall;
    private static ServerPPx server;
     

    public static PackageVersionPPx afterPackageVersion() {
        if (afterPackageVersion == null) afterPackageVersion = new PackageVersionPPx(RCInstallDetail.P_AfterPackageVersion);
        return afterPackageVersion;
    }

    public static PackageVersionPPx beforePackageVersion() {
        if (beforePackageVersion == null) beforePackageVersion = new PackageVersionPPx(RCInstallDetail.P_BeforePackageVersion);
        return beforePackageVersion;
    }

    public static RCDeployDetailPPx rcDeployDetail() {
        if (rcDeployDetail == null) rcDeployDetail = new RCDeployDetailPPx(RCInstallDetail.P_RCDeployDetail);
        return rcDeployDetail;
    }

    public static RCInstallPPx rcInstall() {
        if (rcInstall == null) rcInstall = new RCInstallPPx(RCInstallDetail.P_RCInstall);
        return rcInstall;
    }

    public static ServerPPx server() {
        if (server == null) server = new ServerPPx(RCInstallDetail.P_Server);
        return server;
    }

    public static String id() {
        String s = RCInstallDetail.P_Id;
        return s;
    }

    public static String selected() {
        String s = RCInstallDetail.P_Selected;
        return s;
    }

    public static String error() {
        String s = RCInstallDetail.P_Error;
        return s;
    }

    public static String message() {
        String s = RCInstallDetail.P_Message;
        return s;
    }

    public static String packageId() {
        String s = RCInstallDetail.P_PackageId;
        return s;
    }

    public static String packageName() {
        String s = RCInstallDetail.P_PackageName;
        return s;
    }

    public static String beforeVersion() {
        String s = RCInstallDetail.P_BeforeVersion;
        return s;
    }

    public static String afterVersion() {
        String s = RCInstallDetail.P_AfterVersion;
        return s;
    }

    public static String destHost() {
        String s = RCInstallDetail.P_DestHost;
        return s;
    }

    public static String totalTime() {
        String s = RCInstallDetail.P_TotalTime;
        return s;
    }

    public static String invalidMessage() {
        String s = RCInstallDetail.P_InvalidMessage;
        return s;
    }

    public static String loaded() {
        String s = RCInstallDetail.P_Loaded;
        return s;
    }
}
 