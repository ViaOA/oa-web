// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import test.xice.tsam.model.oa.AdminUser;
import test.xice.tsam.model.oa.propertypath.MRADServerCommandPPx;

import test.xice.tsam.model.oa.*;
 
public class AdminUserPP {
    private static MRADServerCommandPPx mradServerCommands;
     

    public static MRADServerCommandPPx mradServerCommands() {
        if (mradServerCommands == null) mradServerCommands = new MRADServerCommandPPx(AdminUser.P_MRADServerCommands);
        return mradServerCommands;
    }

    public static String id() {
        String s = AdminUser.P_Id;
        return s;
    }

    public static String created() {
        String s = AdminUser.P_Created;
        return s;
    }

    public static String loginId() {
        String s = AdminUser.P_LoginId;
        return s;
    }

    public static String password() {
        String s = AdminUser.P_Password;
        return s;
    }

    public static String firstName() {
        String s = AdminUser.P_FirstName;
        return s;
    }

    public static String lastName() {
        String s = AdminUser.P_LastName;
        return s;
    }

    public static String title() {
        String s = AdminUser.P_Title;
        return s;
    }

    public static String prefixName() {
        String s = AdminUser.P_PrefixName;
        return s;
    }

    public static String inactiveDate() {
        String s = AdminUser.P_InactiveDate;
        return s;
    }

    public static String inactiveReason() {
        String s = AdminUser.P_InactiveReason;
        return s;
    }

    public static String loggedIn() {
        String s = AdminUser.P_LoggedIn;
        return s;
    }

    public static String admin() {
        String s = AdminUser.P_Admin;
        return s;
    }

    public static String editProcessed() {
        String s = AdminUser.P_EditProcessed;
        return s;
    }

    public static String enableLLADCommands() {
        String s = AdminUser.P_EnableLLADCommands;
        return s;
    }

    public static String enableGSMR() {
        String s = AdminUser.P_EnableGSMR;
        return s;
    }

    public static String enableMRAD() {
        String s = AdminUser.P_EnableMRAD;
        return s;
    }

    public static String miscPassword() {
        String s = AdminUser.P_MiscPassword;
        return s;
    }

    public static String fullName() {
        String s = AdminUser.P_FullName;
        return s;
    }
}
 
