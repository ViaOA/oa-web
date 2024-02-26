// Generated by OABuilder
package test.xice.tsac2.model.oa.propertypath;
 
import test.xice.tsac2.model.oa.*;
 
public class AdminServerPP {
    private static AdminClientPPx adminClients;
    private static ApplicationPPx application;
    private static RemoteClientPPx remoteClient;
    private static SiloPPx silo;
     

    public static AdminClientPPx adminClients() {
        if (adminClients == null) adminClients = new AdminClientPPx(AdminServer.P_AdminClients);
        return adminClients;
    }

    public static ApplicationPPx application() {
        if (application == null) application = new ApplicationPPx(AdminServer.P_Application);
        return application;
    }

    public static RemoteClientPPx remoteClient() {
        if (remoteClient == null) remoteClient = new RemoteClientPPx(AdminServer.P_RemoteClient);
        return remoteClient;
    }

    public static SiloPPx silo() {
        if (silo == null) silo = new SiloPPx(AdminServer.P_Silo);
        return silo;
    }

    public static String id() {
        String s = AdminServer.P_Id;
        return s;
    }
}
 