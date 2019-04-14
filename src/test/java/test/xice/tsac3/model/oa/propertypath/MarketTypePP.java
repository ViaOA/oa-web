// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class MarketTypePP {
    private static EnvironmentPPx environment;
    private static UserPPx users;
     

    public static EnvironmentPPx environment() {
        if (environment == null) environment = new EnvironmentPPx(MarketType.P_Environment);
        return environment;
    }

    public static UserPPx users() {
        if (users == null) users = new UserPPx(MarketType.P_Users);
        return users;
    }

    public static String id() {
        String s = MarketType.P_Id;
        return s;
    }

    public static String name() {
        String s = MarketType.P_Name;
        return s;
    }

    public static String seq() {
        String s = MarketType.P_Seq;
        return s;
    }
}
 
