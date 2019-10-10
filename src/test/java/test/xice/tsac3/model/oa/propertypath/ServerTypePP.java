// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class ServerTypePP {
    private static EnvironmentServerTypePPx environmentServerTypes;
    private static ServerPPx servers;
    private static ServerTypeClientPPx serverTypeClients;
    private static ServerTypeClientPPx serverTypeClients2;
    private static ServerTypeVersionPPx serverTypeVersions;
    private static SiloServerInfoPPx siloServerInfos;
    private static SiloTypePPx siloTypes;
     

    public static EnvironmentServerTypePPx environmentServerTypes() {
        if (environmentServerTypes == null) environmentServerTypes = new EnvironmentServerTypePPx(ServerType.P_EnvironmentServerTypes);
        return environmentServerTypes;
    }

    public static ServerPPx servers() {
        if (servers == null) servers = new ServerPPx(ServerType.P_Servers);
        return servers;
    }

    public static ServerTypeClientPPx serverTypeClients() {
        if (serverTypeClients == null) serverTypeClients = new ServerTypeClientPPx(ServerType.P_ServerTypeClients);
        return serverTypeClients;
    }

    public static ServerTypeClientPPx serverTypeClients2() {
        if (serverTypeClients2 == null) serverTypeClients2 = new ServerTypeClientPPx(ServerType.P_ServerTypeClients2);
        return serverTypeClients2;
    }

    public static ServerTypeVersionPPx serverTypeVersions() {
        if (serverTypeVersions == null) serverTypeVersions = new ServerTypeVersionPPx(ServerType.P_ServerTypeVersions);
        return serverTypeVersions;
    }

    public static SiloServerInfoPPx siloServerInfos() {
        if (siloServerInfos == null) siloServerInfos = new SiloServerInfoPPx(ServerType.P_SiloServerInfos);
        return siloServerInfos;
    }

    public static SiloTypePPx siloTypes() {
        if (siloTypes == null) siloTypes = new SiloTypePPx(ServerType.P_SiloTypes);
        return siloTypes;
    }

    public static String id() {
        String s = ServerType.P_Id;
        return s;
    }

    public static String code() {
        String s = ServerType.P_Code;
        return s;
    }

    public static String name() {
        String s = ServerType.P_Name;
        return s;
    }

    public static String description() {
        String s = ServerType.P_Description;
        return s;
    }

    public static String serverTypeId() {
        String s = ServerType.P_ServerTypeId;
        return s;
    }

    public static String usesCron() {
        String s = ServerType.P_UsesCron;
        return s;
    }

    public static String usesPool() {
        String s = ServerType.P_UsesPool;
        return s;
    }

    public static String usesDns() {
        String s = ServerType.P_UsesDns;
        return s;
    }

    public static String dnsName() {
        String s = ServerType.P_DnsName;
        return s;
    }

    public static String dnsShortName() {
        String s = ServerType.P_DNSShortName;
        return s;
    }

    public static String clientPort() {
        String s = ServerType.P_ClientPort;
        return s;
    }

    public static String webPort() {
        String s = ServerType.P_WebPort;
        return s;
    }

    public static String sslPort() {
        String s = ServerType.P_SslPort;
        return s;
    }

    public static String vipClientPort() {
        String s = ServerType.P_VIPClientPort;
        return s;
    }

    public static String vipWebPort() {
        String s = ServerType.P_VIPWebPort;
        return s;
    }

    public static String vipSSLPort() {
        String s = ServerType.P_VIPSSLPort;
        return s;
    }

    public static String hasClient() {
        String s = ServerType.P_HasClient;
        return s;
    }

    public static String sudoUser() {
        String s = ServerType.P_SudoUser;
        return s;
    }

    public static String usesIDL() {
        String s = ServerType.P_UsesIDL;
        return s;
    }

    public static String packageName() {
        String s = ServerType.P_PackageName;
        return s;
    }

    public static String pomGroupId() {
        String s = ServerType.P_PomGroupId;
        return s;
    }

    public static String pomArtifactId() {
        String s = ServerType.P_PomArtifactId;
        return s;
    }

    public static String pomClientArtifactId() {
        String s = ServerType.P_PomClientArtifactId;
        return s;
    }
}
 