// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import test.xice.tsam.model.oa.ApplicationVersion;
import test.xice.tsam.model.oa.propertypath.ApplicationPPx;
import test.xice.tsam.model.oa.propertypath.PackageTypePPx;
import test.xice.tsam.model.oa.propertypath.PackageVersionPPx;

import test.xice.tsam.model.oa.*;
 
public class ApplicationVersionPP {
    private static ApplicationPPx application;
    private static PackageVersionPPx currentPackageVersion;
    private static PackageVersionPPx newPackageVersion;
    private static PackageTypePPx packageType;
     

    public static ApplicationPPx application() {
        if (application == null) application = new ApplicationPPx(ApplicationVersion.P_Application);
        return application;
    }

    public static PackageVersionPPx currentPackageVersion() {
        if (currentPackageVersion == null) currentPackageVersion = new PackageVersionPPx(ApplicationVersion.P_CurrentPackageVersion);
        return currentPackageVersion;
    }

    public static PackageVersionPPx newPackageVersion() {
        if (newPackageVersion == null) newPackageVersion = new PackageVersionPPx(ApplicationVersion.P_NewPackageVersion);
        return newPackageVersion;
    }

    public static PackageTypePPx packageType() {
        if (packageType == null) packageType = new PackageTypePPx(ApplicationVersion.P_PackageType);
        return packageType;
    }

    public static String id() {
        String s = ApplicationVersion.P_Id;
        return s;
    }

    public static String currentVersion() {
        String s = ApplicationVersion.P_CurrentVersion;
        return s;
    }
}
 
