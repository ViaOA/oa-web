// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import test.xice.tsam.model.oa.ApplicationStatus;
import test.xice.tsam.model.oa.propertypath.ApplicationPPx;

import test.xice.tsam.model.oa.*;
 
public class ApplicationStatusPP {
    private static ApplicationPPx applications;
     

    public static ApplicationPPx applications() {
        if (applications == null) applications = new ApplicationPPx(ApplicationStatus.P_Applications);
        return applications;
    }

    public static String id() {
        String s = ApplicationStatus.P_Id;
        return s;
    }

    public static String created() {
        String s = ApplicationStatus.P_Created;
        return s;
    }

    public static String name() {
        String s = ApplicationStatus.P_Name;
        return s;
    }

    public static String type() {
        String s = ApplicationStatus.P_Type;
        return s;
    }

    public static String color() {
        String s = ApplicationStatus.P_Color;
        return s;
    }
}
 
