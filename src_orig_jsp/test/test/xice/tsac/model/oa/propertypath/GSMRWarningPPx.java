// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac.model.oa.*;
 
public class GSMRWarningPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public GSMRWarningPPx(String name) {
        this(null, name);
    }

    public GSMRWarningPPx(PPxInterface parent, String name) {
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

    public GCIConnectionPPx gciConnection() {
        GCIConnectionPPx ppx = new GCIConnectionPPx(this, GSMRWarning.P_GCIConnection);
        return ppx;
    }

    public GSMRServerPPx gsmrServer() {
        GSMRServerPPx ppx = new GSMRServerPPx(this, GSMRWarning.P_GSMRServer);
        return ppx;
    }

    public GSRequestPPx gsRequest() {
        GSRequestPPx ppx = new GSRequestPPx(this, GSMRWarning.P_GSRequest);
        return ppx;
    }

    public WarningPPx warning() {
        WarningPPx ppx = new WarningPPx(this, GSMRWarning.P_Warning);
        return ppx;
    }

    public String id() {
        return pp + "." + GSMRWarning.P_Id;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 