// Generated by OABuilder
package test.xice.tsac2.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac2.model.oa.*;
 
public class ApplicationTypePPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public ApplicationTypePPx(String name) {
        this(null, name);
    }

    public ApplicationTypePPx(PPxInterface parent, String name) {
        String s = null;
        if (parent != null) {
            s = parent.toString();
        }
        if (s == null) s = "";
        if (name != null && name.length() > 0) {
            if (s.length() > 0 && name.charAt(0) != ':') s += ".";
            s += name;
        }
        pp = s;
    }

    public ApplicationPPx applications() {
        ApplicationPPx ppx = new ApplicationPPx(this, ApplicationType.P_Applications);
        return ppx;
    }

    public PackageTypePPx packageTypes() {
        PackageTypePPx ppx = new PackageTypePPx(this, ApplicationType.P_PackageTypes);
        return ppx;
    }

    public RCServiceListDetailPPx rcServiceListDetails() {
        RCServiceListDetailPPx ppx = new RCServiceListDetailPPx(this, ApplicationType.P_RCServiceListDetails);
        return ppx;
    }

    public SiloConfigPPx siloConfigs() {
        SiloConfigPPx ppx = new SiloConfigPPx(this, ApplicationType.P_SiloConfigs);
        return ppx;
    }

    public SiloTypePPx siloTypes() {
        SiloTypePPx ppx = new SiloTypePPx(this, ApplicationType.P_SiloTypes);
        return ppx;
    }

    public String id() {
        return pp + "." + ApplicationType.P_Id;
    }

    public String code() {
        return pp + "." + ApplicationType.P_Code;
    }

    public String name() {
        return pp + "." + ApplicationType.P_Name;
    }

    public String description() {
        return pp + "." + ApplicationType.P_Description;
    }

    public String serverTypeId() {
        return pp + "." + ApplicationType.P_ServerTypeId;
    }

    public String usesCron() {
        return pp + "." + ApplicationType.P_UsesCron;
    }

    public String usesPool() {
        return pp + "." + ApplicationType.P_UsesPool;
    }

    public String usesDns() {
        return pp + "." + ApplicationType.P_UsesDns;
    }

    public String dnsName() {
        return pp + "." + ApplicationType.P_DnsName;
    }

    public String dnsShortName() {
        return pp + "." + ApplicationType.P_DnsShortName;
    }

    public String clientPort() {
        return pp + "." + ApplicationType.P_ClientPort;
    }

    public String webPort() {
        return pp + "." + ApplicationType.P_WebPort;
    }

    public String sslPort() {
        return pp + "." + ApplicationType.P_SslPort;
    }

    public String vipClientPort() {
        return pp + "." + ApplicationType.P_VIPClientPort;
    }

    public String vipWebPort() {
        return pp + "." + ApplicationType.P_VIPWebPort;
    }

    public String vipSSLPort() {
        return pp + "." + ApplicationType.P_VIPSSLPort;
    }

    public String f5Port() {
        return pp + "." + ApplicationType.P_F5Port;
    }

    public String hasClient() {
        return pp + "." + ApplicationType.P_HasClient;
    }

    public String sudoUser() {
        return pp + "." + ApplicationType.P_SudoUser;
    }

    public String usesIDL() {
        return pp + "." + ApplicationType.P_UsesIDL;
    }

    public String directory() {
        return pp + "." + ApplicationType.P_Directory;
    }

    public String startCommand() {
        return pp + "." + ApplicationType.P_StartCommand;
    }

    public String stopCommand() {
        return pp + "." + ApplicationType.P_StopCommand;
    }

    public String createdBy() {
        return pp + "." + ApplicationType.P_CreatedBy;
    }

    public String directory_copy() {
        return pp + "." + ApplicationType.P_Directory_copy;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 