// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import test.xice.tsam.model.oa.PackageVersion;
import test.xice.tsam.model.oa.propertypath.ApplicationVersionPPx;
import test.xice.tsam.model.oa.propertypath.IDLPPx;
import test.xice.tsam.model.oa.propertypath.PackageTypePPx;
import test.xice.tsam.model.oa.propertypath.SiloConfigVersioinPPx;

import test.xice.tsam.model.oa.*;
 
public class PackageVersionPP {
    private static ApplicationVersionPPx currentApplicationVersions;
    private static IDLPPx idL;
    private static ApplicationVersionPPx nepApplicationVersions;
    private static PackageTypePPx packageType;
    private static SiloConfigVersioinPPx siloConfigVersioins;
     

    public static ApplicationVersionPPx currentApplicationVersions() {
        if (currentApplicationVersions == null) currentApplicationVersions = new ApplicationVersionPPx(PackageVersion.P_CurrentApplicationVersions);
        return currentApplicationVersions;
    }

    public static IDLPPx idL() {
        if (idL == null) idL = new IDLPPx(PackageVersion.P_IDL);
        return idL;
    }

    public static ApplicationVersionPPx nepApplicationVersions() {
        if (nepApplicationVersions == null) nepApplicationVersions = new ApplicationVersionPPx(PackageVersion.P_NepApplicationVersions);
        return nepApplicationVersions;
    }

    public static PackageTypePPx packageType() {
        if (packageType == null) packageType = new PackageTypePPx(PackageVersion.P_PackageType);
        return packageType;
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

    public static String fileSize() {
        String s = PackageVersion.P_FileSize;
        return s;
    }

    public static String fileName() {
        String s = PackageVersion.P_FileName;
        return s;
    }
}
 