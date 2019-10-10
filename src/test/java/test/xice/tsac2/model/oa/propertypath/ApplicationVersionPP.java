// Generated by OABuilder
package test.xice.tsac2.model.oa.propertypath;
 
import test.xice.tsac2.model.oa.*;
 
public class ApplicationVersionPP {
    private static ApplicationPPx application;
    private static PackageTypePPx packageType;
    private static PackageVersionPPx packageVersion;
     

    public static ApplicationPPx application() {
        if (application == null) application = new ApplicationPPx(ApplicationVersion.P_Application);
        return application;
    }

    public static PackageTypePPx packageType() {
        if (packageType == null) packageType = new PackageTypePPx(ApplicationVersion.P_PackageType);
        return packageType;
    }

    public static PackageVersionPPx packageVersion() {
        if (packageVersion == null) packageVersion = new PackageVersionPPx(ApplicationVersion.P_PackageVersion);
        return packageVersion;
    }

    public static String id() {
        String s = ApplicationVersion.P_Id;
        return s;
    }
}
 