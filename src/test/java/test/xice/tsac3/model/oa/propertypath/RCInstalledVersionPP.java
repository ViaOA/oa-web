// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class RCInstalledVersionPP {
    private static EnvironmentPPx environment;
    private static RCExecutePPx rcExecute;
    private static RCInstalledVersionDetailPPx rcInstalledVersionDetails;
     

    public static EnvironmentPPx environment() {
        if (environment == null) environment = new EnvironmentPPx(RCInstalledVersion.P_Environment);
        return environment;
    }

    public static RCExecutePPx rcExecute() {
        if (rcExecute == null) rcExecute = new RCExecutePPx(RCInstalledVersion.P_RCExecute);
        return rcExecute;
    }

    public static RCInstalledVersionDetailPPx rcInstalledVersionDetails() {
        if (rcInstalledVersionDetails == null) rcInstalledVersionDetails = new RCInstalledVersionDetailPPx(RCInstalledVersion.P_RCInstalledVersionDetails);
        return rcInstalledVersionDetails;
    }

    public static String id() {
        String s = RCInstalledVersion.P_Id;
        return s;
    }

    public static String created() {
        String s = RCInstalledVersion.P_Created;
        return s;
    }

    public static String rcAvailable() {
        String s = RCInstalledVersion.P_RCAvailable;
        return s;
    }

    public static String canRun() {
        String s = RCInstalledVersion.P_CanRun;
        return s;
    }

    public static String canProcess() {
        String s = RCInstalledVersion.P_CanProcess;
        return s;
    }

    public static String canLoad() {
        String s = RCInstalledVersion.P_CanLoad;
        return s;
    }

    public static String run() {
        String s = "run";
        return s;
    }

    public static String process() {
        String s = "process";
        return s;
    }

    public static String load() {
        String s = "load";
        return s;
    }
}
 
