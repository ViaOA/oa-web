// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import test.xice.tsac.model.oa.*;
 
public class MRADServerPP {
    private static ApplicationPPx application;
    private static EnvironmentPPx environment;
    private static MRADClientPPx mradClients;
    private static MRADServerCommandPPx mradServerCommands;
    private static RemoteClientPPx remoteClient;
     

    public static ApplicationPPx application() {
        if (application == null) application = new ApplicationPPx(MRADServer.P_Application);
        return application;
    }

    public static EnvironmentPPx environment() {
        if (environment == null) environment = new EnvironmentPPx(MRADServer.P_Environment);
        return environment;
    }

    public static MRADClientPPx mradClients() {
        if (mradClients == null) mradClients = new MRADClientPPx(MRADServer.P_MRADClients);
        return mradClients;
    }

    public static MRADServerCommandPPx mradServerCommands() {
        if (mradServerCommands == null) mradServerCommands = new MRADServerCommandPPx(MRADServer.P_MRADServerCommands);
        return mradServerCommands;
    }

    public static RemoteClientPPx remoteClient() {
        if (remoteClient == null) remoteClient = new RemoteClientPPx(MRADServer.P_RemoteClient);
        return remoteClient;
    }

    public static String id() {
        String s = MRADServer.P_Id;
        return s;
    }
}
 