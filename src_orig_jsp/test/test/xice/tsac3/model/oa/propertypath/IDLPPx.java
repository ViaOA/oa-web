// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac3.model.oa.*;
 
public class IDLPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private EnvironmentPPx environments;
    private ServerTypeVersionPPx serverTypeVersions;
     
    public IDLPPx(String name) {
        this(null, name);
    }

    public IDLPPx(PPxInterface parent, String name) {
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

    public EnvironmentPPx environments() {
        if (environments == null) environments = new EnvironmentPPx(this, IDL.P_Environments);
        return environments;
    }

    public ServerTypeVersionPPx serverTypeVersions() {
        if (serverTypeVersions == null) serverTypeVersions = new ServerTypeVersionPPx(this, IDL.P_ServerTypeVersions);
        return serverTypeVersions;
    }

    public String id() {
        return pp + "." + IDL.P_Id;
    }

    public String created() {
        return pp + "." + IDL.P_Created;
    }

    public String version() {
        return pp + "." + IDL.P_Version;
    }

    public String releaseDate() {
        return pp + "." + IDL.P_ReleaseDate;
    }

    public String seq() {
        return pp + "." + IDL.P_Seq;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 