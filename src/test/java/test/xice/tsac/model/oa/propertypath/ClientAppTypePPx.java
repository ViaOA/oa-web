// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac.model.oa.*;
 
public class ClientAppTypePPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public ClientAppTypePPx(String name) {
        this(null, name);
    }

    public ClientAppTypePPx(PPxInterface parent, String name) {
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

    public UserLoginHistoryPPx userLoginHistories() {
        UserLoginHistoryPPx ppx = new UserLoginHistoryPPx(this, ClientAppType.P_UserLoginHistories);
        return ppx;
    }

    public UserLoginPPx userLogins() {
        UserLoginPPx ppx = new UserLoginPPx(this, ClientAppType.P_UserLogins);
        return ppx;
    }

    public String id() {
        return pp + "." + ClientAppType.P_Id;
    }

    public String name() {
        return pp + "." + ClientAppType.P_Name;
    }

    public String seq() {
        return pp + "." + ClientAppType.P_Seq;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 