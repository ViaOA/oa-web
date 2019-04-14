// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class UserPP {
    private static EnvironmentPPx calcEnvironment;
    private static EnvironmentPPx calcEnvironment1;
    private static LLADServerPPx calcLLADServer;
    private static CompanyPPx company;
    private static LLADServerPPx lladServers;
    private static MarketTypePPx marketTypes;
    private static UserLoginHistoryPPx userLoginHistories;
    private static UserLoginPPx userLogins;
     

    public static EnvironmentPPx calcEnvironment() {
        if (calcEnvironment == null) calcEnvironment = new EnvironmentPPx(User.P_CalcEnvironment);
        return calcEnvironment;
    }

    public static EnvironmentPPx calcEnvironment1() {
        if (calcEnvironment1 == null) calcEnvironment1 = new EnvironmentPPx(User.P_CalcEnvironment1);
        return calcEnvironment1;
    }

    public static LLADServerPPx calcLLADServer() {
        if (calcLLADServer == null) calcLLADServer = new LLADServerPPx(User.P_CalcLLADServer);
        return calcLLADServer;
    }

    public static CompanyPPx company() {
        if (company == null) company = new CompanyPPx(User.P_Company);
        return company;
    }

    public static LLADServerPPx lladServers() {
        if (lladServers == null) lladServers = new LLADServerPPx(User.P_LLADServers);
        return lladServers;
    }

    public static MarketTypePPx marketTypes() {
        if (marketTypes == null) marketTypes = new MarketTypePPx(User.P_MarketTypes);
        return marketTypes;
    }

    public static UserLoginHistoryPPx userLoginHistories() {
        if (userLoginHistories == null) userLoginHistories = new UserLoginHistoryPPx(User.P_UserLoginHistories);
        return userLoginHistories;
    }

    public static UserLoginPPx userLogins() {
        if (userLogins == null) userLogins = new UserLoginPPx(User.P_UserLogins);
        return userLogins;
    }

    public static String id() {
        String s = User.P_Id;
        return s;
    }

    public static String userId() {
        String s = User.P_UserId;
        return s;
    }

    public static String firstName() {
        String s = User.P_FirstName;
        return s;
    }

    public static String lastName() {
        String s = User.P_LastName;
        return s;
    }

    public static String email() {
        String s = User.P_Email;
        return s;
    }

    public static String phone() {
        String s = User.P_Phone;
        return s;
    }

    public static String screenAccessType() {
        String s = User.P_ScreenAccessType;
        return s;
    }

    public static String isGatewayUser() {
        String s = User.P_IsGatewayUser;
        return s;
    }

    public static String traderMnemonic() {
        String s = User.P_TraderMnemonic;
        return s;
    }

    public static String endexTradingAccount() {
        String s = User.P_EndexTradingAccount;
        return s;
    }

    public static String endexShipperCode() {
        String s = User.P_EndexShipperCode;
        return s;
    }

    public static String userFunctionType() {
        String s = User.P_UserFunctionType;
        return s;
    }

    public static String isLoggedIn() {
        String s = User.P_IsLoggedIn;
        return s;
    }

    public static String fullName() {
        String s = User.P_FullName;
        return s;
    }

    public static String fullNameAndUserId() {
        String s = User.P_FullNameAndUserId;
        return s;
    }

    public static String totalUsers() {
        String s = User.P_TotalUsers;
        return s;
    }

    public static String enableLLADCommands() {
        String s = User.P_EnableLLADCommands;
        return s;
    }

    public static String enableLLADCommandsAndLoggedIn() {
        String s = User.P_EnableLLADCommandsAndLoggedIn;
        return s;
    }

    public static String fastCompanyName() {
        String s = User.P_FastCompanyName;
        return s;
    }

    public static String sendText() {
        String s = "sendText";
        return s;
    }

    public static String forceLogout() {
        String s = "forceLogout";
        return s;
    }

    public static String resetPassword() {
        String s = "resetPassword";
        return s;
    }

    public static String requestUserLoginHistory() {
        String s = "requestUserLoginHistory";
        return s;
    }

    public static String refreshUserCache() {
        String s = "refreshUserCache";
        return s;
    }
}
 
