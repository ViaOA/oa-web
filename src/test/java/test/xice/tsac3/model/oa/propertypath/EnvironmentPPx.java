// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac3.model.oa.*;
 
public class EnvironmentPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private ServerInstallPPx activeServerInstalls;
    private UserPPx calcUsers;
    private CompanyPPx companies;
    private EnvironmentServerTypePPx environmentServerTypes;
    private EnvironmentTypePPx environmentType;
    private IDLPPx idL;
    private UserPPx loginUsers;
    private MarketTypePPx marketTypes;
    private RCInstalledVersionPPx rcInstalledVersions;
    private SiloPPx silos;
    private SitePPx site;
     
    public EnvironmentPPx(String name) {
        this(null, name);
    }

    public EnvironmentPPx(PPxInterface parent, String name) {
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

    public ServerInstallPPx activeServerInstalls() {
        if (activeServerInstalls == null) activeServerInstalls = new ServerInstallPPx(this, Environment.P_ActiveServerInstalls);
        return activeServerInstalls;
    }

    public UserPPx calcUsers() {
        if (calcUsers == null) calcUsers = new UserPPx(this, Environment.P_CalcUsers);
        return calcUsers;
    }

    public CompanyPPx companies() {
        if (companies == null) companies = new CompanyPPx(this, Environment.P_Companies);
        return companies;
    }

    public EnvironmentServerTypePPx environmentServerTypes() {
        if (environmentServerTypes == null) environmentServerTypes = new EnvironmentServerTypePPx(this, Environment.P_EnvironmentServerTypes);
        return environmentServerTypes;
    }

    public EnvironmentTypePPx environmentType() {
        if (environmentType == null) environmentType = new EnvironmentTypePPx(this, Environment.P_EnvironmentType);
        return environmentType;
    }

    public IDLPPx idL() {
        if (idL == null) idL = new IDLPPx(this, Environment.P_IDL);
        return idL;
    }

    public UserPPx loginUsers() {
        if (loginUsers == null) loginUsers = new UserPPx(this, Environment.P_LoginUsers);
        return loginUsers;
    }

    public MarketTypePPx marketTypes() {
        if (marketTypes == null) marketTypes = new MarketTypePPx(this, Environment.P_MarketTypes);
        return marketTypes;
    }

    public RCInstalledVersionPPx rcInstalledVersions() {
        if (rcInstalledVersions == null) rcInstalledVersions = new RCInstalledVersionPPx(this, Environment.P_RCInstalledVersions);
        return rcInstalledVersions;
    }

    public SiloPPx silos() {
        if (silos == null) silos = new SiloPPx(this, Environment.P_Silos);
        return silos;
    }

    public SitePPx site() {
        if (site == null) site = new SitePPx(this, Environment.P_Site);
        return site;
    }

    public String id() {
        return pp + "." + Environment.P_Id;
    }

    public String name() {
        return pp + "." + Environment.P_Name;
    }

    public String statsLastUpdated() {
        return pp + "." + Environment.P_StatsLastUpdated;
    }

    public String status() {
        return pp + "." + Environment.P_Status;
    }

    public String usesDNS() {
        return pp + "." + Environment.P_UsesDNS;
    }

    public String usesFirewall() {
        return pp + "." + Environment.P_UsesFirewall;
    }

    public String usesVip() {
        return pp + "." + Environment.P_UsesVip;
    }

    public String abbrevName() {
        return pp + "." + Environment.P_AbbrevName;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
