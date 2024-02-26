// Generated by OABuilder
package test.xice.tsac2.model.oa.propertypath;
 
import test.xice.tsac2.model.oa.*;
 
public class PackageVersionPP {
    private static ApplicationVersionPPx applicationVersions;
    private static IDLPPx idL;
    private static InstallVersionPPx installVersions;
    private static PackageTypePPx packageType;
    private static RCInstalledVersionDetailPPx rcInstalledVersionDetails;
    private static RCRepoVersionDetailPPx rcRepoVersionDetails;
    private static SiloConfigVersioinPPx siloConfigVersioins;
     

    public static ApplicationVersionPPx applicationVersions() {
        if (applicationVersions == null) applicationVersions = new ApplicationVersionPPx(PackageVersion.P_ApplicationVersions);
        return applicationVersions;
    }

    public static IDLPPx idL() {
        if (idL == null) idL = new IDLPPx(PackageVersion.P_IDL);
        return idL;
    }

    public static InstallVersionPPx installVersions() {
        if (installVersions == null) installVersions = new InstallVersionPPx(PackageVersion.P_InstallVersions);
        return installVersions;
    }

    public static PackageTypePPx packageType() {
        if (packageType == null) packageType = new PackageTypePPx(PackageVersion.P_PackageType);
        return packageType;
    }

    public static RCInstalledVersionDetailPPx rcInstalledVersionDetails() {
        if (rcInstalledVersionDetails == null) rcInstalledVersionDetails = new RCInstalledVersionDetailPPx(PackageVersion.P_RCInstalledVersionDetails);
        return rcInstalledVersionDetails;
    }

    public static RCRepoVersionDetailPPx rcRepoVersionDetails() {
        if (rcRepoVersionDetails == null) rcRepoVersionDetails = new RCRepoVersionDetailPPx(PackageVersion.P_RCRepoVersionDetails);
        return rcRepoVersionDetails;
    }

    public static SiloConfigVersioinPPx siloConfigVersioins() {
        if (siloConfigVersioins == null) siloConfigVersioins = new SiloConfigVersioinPPx(PackageVersion.P_SiloConfigVersioins);
        return siloConfigVersioins;
    }

    public static String id() {
        String s = PackageVersion.P_Id;
        return s;
    }

    public static String created() {
        String s = PackageVersion.P_Created;
        return s;
    }

    public static String version() {
        String s = PackageVersion.P_Version;
        return s;
    }

    public static String buildDate() {
        String s = PackageVersion.P_BuildDate;
        return s;
    }
}
 