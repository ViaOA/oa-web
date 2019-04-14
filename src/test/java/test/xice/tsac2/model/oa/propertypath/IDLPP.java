// Generated by OABuilder
package test.xice.tsac2.model.oa.propertypath;
 
import test.xice.tsac2.model.oa.*;
 
public class IDLPP {
    private static EnvironmentPPx environments;
    private static PackageVersionPPx packageVersions;
     

    public static EnvironmentPPx environments() {
        if (environments == null) environments = new EnvironmentPPx(IDL.P_Environments);
        return environments;
    }

    public static PackageVersionPPx packageVersions() {
        if (packageVersions == null) packageVersions = new PackageVersionPPx(IDL.P_PackageVersions);
        return packageVersions;
    }

    public static String id() {
        String s = IDL.P_Id;
        return s;
    }

    public static String created() {
        String s = IDL.P_Created;
        return s;
    }

    public static String version() {
        String s = IDL.P_Version;
        return s;
    }

    public static String releaseDate() {
        String s = IDL.P_ReleaseDate;
        return s;
    }

    public static String seq() {
        String s = IDL.P_Seq;
        return s;
    }
}
 
