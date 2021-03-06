// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class EnvironmentPP {
    private static ServerInstallPPx activeServerInstalls;
    private static UserPPx calcUsers;
    private static CompanyPPx companies;
    private static EnvironmentServerTypePPx environmentServerTypes;
    private static EnvironmentTypePPx environmentType;
    private static IDLPPx idL;
    private static UserPPx loginUsers;
    private static MarketTypePPx marketTypes;
    private static RCInstalledVersionPPx rcInstalledVersions;
    private static SiloPPx silos;
    private static SitePPx site;
     

    public static ServerInstallPPx activeServerInstalls() {
        if (activeServerInstalls == null) activeServerInstalls = new ServerInstallPPx(Environment.P_ActiveServerInstalls);
        return activeServerInstalls;
    }

    public static UserPPx calcUsers() {
        if (calcUsers == null) calcUsers = new UserPPx(Environment.P_CalcUsers);
        return calcUsers;
    }

    public static CompanyPPx companies() {
        if (companies == null) companies = new CompanyPPx(Environment.P_Companies);
        return companies;
    }

    public static EnvironmentServerTypePPx environmentServerTypes() {
        if (environmentServerTypes == null) environmentServerTypes = new EnvironmentServerTypePPx(Environment.P_EnvironmentServerTypes);
        return environmentServerTypes;
    }

    public static EnvironmentTypePPx environmentType() {
        if (environmentType == null) environmentType = new EnvironmentTypePPx(Environment.P_EnvironmentType);
        return environmentType;
    }

    public static IDLPPx idL() {
        if (idL == null) idL = new IDLPPx(Environment.P_IDL);
        return idL;
    }

    public static UserPPx loginUsers() {
        if (loginUsers == null) loginUsers = new UserPPx(Environment.P_LoginUsers);
        return loginUsers;
    }

    public static MarketTypePPx marketTypes() {
        if (marketTypes == null) marketTypes = new MarketTypePPx(Environment.P_MarketTypes);
        return marketTypes;
    }

    public static RCInstalledVersionPPx rcInstalledVersions() {
        if (rcInstalledVersions == null) rcInstalledVersions = new RCInstalledVersionPPx(Environment.P_RCInstalledVersions);
        return rcInstalledVersions;
    }

    public static SiloPPx silos() {
        if (silos == null) silos = new SiloPPx(Environment.P_Silos);
        return silos;
    }

    public static SitePPx site() {
        if (site == null) site = new SitePPx(Environment.P_Site);
        return site;
    }

    public static String id() {
        String s = Environment.P_Id;
        return s;
    }

    public static String name() {
        String s = Environment.P_Name;
        return s;
    }

    public static String statsLastUpdated() {
        String s = Environment.P_StatsLastUpdated;
        return s;
    }

    public static String status() {
        String s = Environment.P_Status;
        return s;
    }

    public static String usesDNS() {
        String s = Environment.P_UsesDNS;
        return s;
    }

    public static String usesFirewall() {
        String s = Environment.P_UsesFirewall;
        return s;
    }

    public static String usesVip() {
        String s = Environment.P_UsesVip;
        return s;
    }

    public static String abbrevName() {
        String s = Environment.P_AbbrevName;
        return s;
    }
}
 
