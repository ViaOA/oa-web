// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import test.xice.tsac.model.oa.*;
 
public class ClientAppTypePP {
    private static UserLoginHistoryPPx userLoginHistories;
    private static UserLoginPPx userLogins;
     

    public static UserLoginHistoryPPx userLoginHistories() {
        if (userLoginHistories == null) userLoginHistories = new UserLoginHistoryPPx(ClientAppType.P_UserLoginHistories);
        return userLoginHistories;
    }

    public static UserLoginPPx userLogins() {
        if (userLogins == null) userLogins = new UserLoginPPx(ClientAppType.P_UserLogins);
        return userLogins;
    }

    public static String id() {
        String s = ClientAppType.P_Id;
        return s;
    }

    public static String name() {
        String s = ClientAppType.P_Name;
        return s;
    }

    public static String seq() {
        String s = ClientAppType.P_Seq;
        return s;
    }
}
 