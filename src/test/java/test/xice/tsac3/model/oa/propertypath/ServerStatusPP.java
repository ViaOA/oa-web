// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class ServerStatusPP {
    private static GSMRClientPPx gsmrClients;
    private static ServerPPx servers;
     

    public static GSMRClientPPx gsmrClients() {
        if (gsmrClients == null) gsmrClients = new GSMRClientPPx(ServerStatus.P_GSMRClients);
        return gsmrClients;
    }

    public static ServerPPx servers() {
        if (servers == null) servers = new ServerPPx(ServerStatus.P_Servers);
        return servers;
    }

    public static String id() {
        String s = ServerStatus.P_Id;
        return s;
    }

    public static String created() {
        String s = ServerStatus.P_Created;
        return s;
    }

    public static String name() {
        String s = ServerStatus.P_Name;
        return s;
    }

    public static String type() {
        String s = ServerStatus.P_Type;
        return s;
    }

    public static String color() {
        String s = ServerStatus.P_Color;
        return s;
    }
}
 
