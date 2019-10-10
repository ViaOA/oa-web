// Generated by OABuilder
package test.xice.tsac2.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac2.model.oa.*;
 
public class GSMRClientPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public GSMRClientPPx(String name) {
        this(null, name);
    }

    public GSMRClientPPx(PPxInterface parent, String name) {
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

    public ApplicationPPx application() {
        ApplicationPPx ppx = new ApplicationPPx(this, GSMRClient.P_Application);
        return ppx;
    }

    public GSMRServerPPx gsmrServer() {
        GSMRServerPPx ppx = new GSMRServerPPx(this, GSMRClient.P_GSMRServer);
        return ppx;
    }

    public GSRequestPPx gSRequests() {
        GSRequestPPx ppx = new GSRequestPPx(this, GSMRClient.P_GSRequests);
        return ppx;
    }

    public String id() {
        return pp + "." + GSMRClient.P_Id;
    }

    public String connectionId() {
        return pp + "." + GSMRClient.P_ConnectionId;
    }

    public String clientType() {
        return pp + "." + GSMRClient.P_ClientType;
    }

    public String clientDescription() {
        return pp + "." + GSMRClient.P_ClientDescription;
    }

    public String totalRequests() {
        return pp + "." + GSMRClient.P_TotalRequests;
    }

    public String totalRequestTime() {
        return pp + "." + GSMRClient.P_TotalRequestTime;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 