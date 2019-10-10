// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac3.model.oa.*;
 
public class ServerTypePPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private EnvironmentServerTypePPx environmentServerTypes;
    private ServerPPx servers;
    private ServerTypeClientPPx serverTypeClients;
    private ServerTypeClientPPx serverTypeClients2;
    private ServerTypeVersionPPx serverTypeVersions;
    private SiloServerInfoPPx siloServerInfos;
    private SiloTypePPx siloTypes;
     
    public ServerTypePPx(String name) {
        this(null, name);
    }

    public ServerTypePPx(PPxInterface parent, String name) {
        String s = null;
        if (parent != null) {
            s = parent.toString();
        }
        if (s == null) s = "";
        if (name != null) {
            if (s.length() > 0) s += ".";
            s += name;
        }
        pp = s;
    }

    public EnvironmentServerTypePPx environmentServerTypes() {
        if (environmentServerTypes == null) environmentServerTypes = new EnvironmentServerTypePPx(this, ServerType.P_EnvironmentServerTypes);
        return environmentServerTypes;
    }

    public ServerPPx servers() {
        if (servers == null) servers = new ServerPPx(this, ServerType.P_Servers);
        return servers;
    }

    public ServerTypeClientPPx serverTypeClients() {
        if (serverTypeClients == null) serverTypeClients = new ServerTypeClientPPx(this, ServerType.P_ServerTypeClients);
        return serverTypeClients;
    }

    public ServerTypeClientPPx serverTypeClients2() {
        if (serverTypeClients2 == null) serverTypeClients2 = new ServerTypeClientPPx(this, ServerType.P_ServerTypeClients2);
        return serverTypeClients2;
    }

    public ServerTypeVersionPPx serverTypeVersions() {
        if (serverTypeVersions == null) serverTypeVersions = new ServerTypeVersionPPx(this, ServerType.P_ServerTypeVersions);
        return serverTypeVersions;
    }

    public SiloServerInfoPPx siloServerInfos() {
        if (siloServerInfos == null) siloServerInfos = new SiloServerInfoPPx(this, ServerType.P_SiloServerInfos);
        return siloServerInfos;
    }

    public SiloTypePPx siloTypes() {
        if (siloTypes == null) siloTypes = new SiloTypePPx(this, ServerType.P_SiloTypes);
        return siloTypes;
    }

    public String id() {
        return pp + "." + ServerType.P_Id;
    }

    public String code() {
        return pp + "." + ServerType.P_Code;
    }

    public String name() {
        return pp + "." + ServerType.P_Name;
    }

    public String description() {
        return pp + "." + ServerType.P_Description;
    }

    public String serverTypeId() {
        return pp + "." + ServerType.P_ServerTypeId;
    }

    public String usesCron() {
        return pp + "." + ServerType.P_UsesCron;
    }

    public String usesPool() {
        return pp + "." + ServerType.P_UsesPool;
    }

    public String usesDns() {
        return pp + "." + ServerType.P_UsesDns;
    }

    public String dnsName() {
        return pp + "." + ServerType.P_DnsName;
    }

    public String dnsShortName() {
        return pp + "." + ServerType.P_DNSShortName;
    }

    public String clientPort() {
        return pp + "." + ServerType.P_ClientPort;
    }

    public String webPort() {
        return pp + "." + ServerType.P_WebPort;
    }

    public String sslPort() {
        return pp + "." + ServerType.P_SslPort;
    }

    public String vipClientPort() {
        return pp + "." + ServerType.P_VIPClientPort;
    }

    public String vipWebPort() {
        return pp + "." + ServerType.P_VIPWebPort;
    }

    public String vipSSLPort() {
        return pp + "." + ServerType.P_VIPSSLPort;
    }

    public String hasClient() {
        return pp + "." + ServerType.P_HasClient;
    }

    public String sudoUser() {
        return pp + "." + ServerType.P_SudoUser;
    }

    public String usesIDL() {
        return pp + "." + ServerType.P_UsesIDL;
    }

    public String packageName() {
        return pp + "." + ServerType.P_PackageName;
    }

    public String pomGroupId() {
        return pp + "." + ServerType.P_PomGroupId;
    }

    public String pomArtifactId() {
        return pp + "." + ServerType.P_PomArtifactId;
    }

    public String pomClientArtifactId() {
        return pp + "." + ServerType.P_PomClientArtifactId;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 