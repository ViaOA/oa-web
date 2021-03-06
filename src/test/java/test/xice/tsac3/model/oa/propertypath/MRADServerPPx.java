// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac3.model.oa.*;
 
public class MRADServerPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private MRADClientPPx mradClients;
    private ServerPPx server;
    private SiloPPx silo;
     
    public MRADServerPPx(String name) {
        this(null, name);
    }

    public MRADServerPPx(PPxInterface parent, String name) {
        String s = null;
        if (parent != null) {
            s = parent.toString();
        }
        if (s == null) s = "";
        if (name != null) {
            if (s.length() > 0) s += ".";
            s += name;
        }
        pp = s;
    }

    public MRADClientPPx mradClients() {
        if (mradClients == null) mradClients = new MRADClientPPx(this, MRADServer.P_MRADClients);
        return mradClients;
    }

    public ServerPPx server() {
        if (server == null) server = new ServerPPx(this, MRADServer.P_Server);
        return server;
    }

    public SiloPPx silo() {
        if (silo == null) silo = new SiloPPx(this, MRADServer.P_Silo);
        return silo;
    }

    public String id() {
        return pp + "." + MRADServer.P_Id;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
