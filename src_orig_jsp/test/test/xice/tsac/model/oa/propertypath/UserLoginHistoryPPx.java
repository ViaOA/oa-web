// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac.model.oa.*;
 
public class UserLoginHistoryPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public UserLoginHistoryPPx(String name) {
        this(null, name);
    }

    public UserLoginHistoryPPx(PPxInterface parent, String name) {
        String s = null;
        if (parent != null) {
            s = parent.toString();
        }
        if (s == null) s = "";
        if (name != null && name.length() > 0) {
            if (s.length() > 0 && name.charAt(0) != ':') s += ".";
            s += name;
        }
        pp = s;
    }

    public ClientAppTypePPx clientAppType() {
        ClientAppTypePPx ppx = new ClientAppTypePPx(this, UserLoginHistory.P_ClientAppType);
        return ppx;
    }

    public LLADClientPPx lladClient() {
        LLADClientPPx ppx = new LLADClientPPx(this, UserLoginHistory.P_LLADClient);
        return ppx;
    }

    public UserPPx user() {
        UserPPx ppx = new UserPPx(this, UserLoginHistory.P_User);
        return ppx;
    }

    public String id() {
        return pp + "." + UserLoginHistory.P_Id;
    }

    public String login() {
        return pp + "." + UserLoginHistory.P_Login;
    }

    public String logout() {
        return pp + "." + UserLoginHistory.P_Logout;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 