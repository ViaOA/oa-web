// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import test.xice.tsac.model.oa.*;
 
public class RCStageDetailPP {
    private static ServerPPx origServer;
    private static PackageVersionPPx packageVersion;
    private static RCDeployDetailPPx rcDeployDetail;
    private static RCStagePPx rcStage;
    private static ServerPPx server;
     

    public static ServerPPx origServer() {
        if (origServer == null) origServer = new ServerPPx(RCStageDetail.P_OrigServer);
        return origServer;
    }

    public static PackageVersionPPx packageVersion() {
        if (packageVersion == null) packageVersion = new PackageVersionPPx(RCStageDetail.P_PackageVersion);
        return packageVersion;
    }

    public static RCDeployDetailPPx rcDeployDetail() {
        if (rcDeployDetail == null) rcDeployDetail = new RCDeployDetailPPx(RCStageDetail.P_RCDeployDetail);
        return rcDeployDetail;
    }

    public static RCStagePPx rcStage() {
        if (rcStage == null) rcStage = new RCStagePPx(RCStageDetail.P_RCStage);
        return rcStage;
    }

    public static ServerPPx server() {
        if (server == null) server = new ServerPPx(RCStageDetail.P_Server);
        return server;
    }

    public static String id() {
        String s = RCStageDetail.P_Id;
        return s;
    }

    public static String selected() {
        String s = RCStageDetail.P_Selected;
        return s;
    }

    public static String error() {
        String s = RCStageDetail.P_Error;
        return s;
    }

    public static String message() {
        String s = RCStageDetail.P_Message;
        return s;
    }

    public static String packageId() {
        String s = RCStageDetail.P_PackageId;
        return s;
    }

    public static String packageName() {
        String s = RCStageDetail.P_PackageName;
        return s;
    }

    public static String version() {
        String s = RCStageDetail.P_Version;
        return s;
    }

    public static String destHost() {
        String s = RCStageDetail.P_DestHost;
        return s;
    }

    public static String origHost() {
        String s = RCStageDetail.P_OrigHost;
        return s;
    }

    public static String totalTime() {
        String s = RCStageDetail.P_TotalTime;
        return s;
    }

    public static String invalidMessage() {
        String s = RCStageDetail.P_InvalidMessage;
        return s;
    }
}
 