// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import test.xice.tsam.model.oa.ApplicationGroup;
import test.xice.tsam.model.oa.propertypath.ApplicationPPx;
import test.xice.tsam.model.oa.propertypath.ApplicationTypePPx;
import test.xice.tsam.model.oa.propertypath.SiloPPx;

import test.xice.tsam.model.oa.*;
 
public class ApplicationGroupPP {
    private static ApplicationPPx excludeApplications;
    private static ApplicationTypePPx excludeApplicationTypes;
    private static ApplicationPPx includeApplications;
    private static ApplicationTypePPx includeApplicationTypes;
    private static ApplicationPPx selectedApplications;
    private static SiloPPx silo;
     

    public static ApplicationPPx excludeApplications() {
        if (excludeApplications == null) excludeApplications = new ApplicationPPx(ApplicationGroup.P_ExcludeApplications);
        return excludeApplications;
    }

    public static ApplicationTypePPx excludeApplicationTypes() {
        if (excludeApplicationTypes == null) excludeApplicationTypes = new ApplicationTypePPx(ApplicationGroup.P_ExcludeApplicationTypes);
        return excludeApplicationTypes;
    }

    public static ApplicationPPx includeApplications() {
        if (includeApplications == null) includeApplications = new ApplicationPPx(ApplicationGroup.P_IncludeApplications);
        return includeApplications;
    }

    public static ApplicationTypePPx includeApplicationTypes() {
        if (includeApplicationTypes == null) includeApplicationTypes = new ApplicationTypePPx(ApplicationGroup.P_IncludeApplicationTypes);
        return includeApplicationTypes;
    }

    public static ApplicationPPx selectedApplications() {
        if (selectedApplications == null) selectedApplications = new ApplicationPPx(ApplicationGroup.P_SelectedApplications);
        return selectedApplications;
    }

    public static SiloPPx silo() {
        if (silo == null) silo = new SiloPPx(ApplicationGroup.P_Silo);
        return silo;
    }

    public static String id() {
        String s = ApplicationGroup.P_Id;
        return s;
    }

    public static String code() {
        String s = ApplicationGroup.P_Code;
        return s;
    }

    public static String name() {
        String s = ApplicationGroup.P_Name;
        return s;
    }

    public static String seq() {
        String s = ApplicationGroup.P_Seq;
        return s;
    }
}
 
