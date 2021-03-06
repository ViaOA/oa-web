// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac.model.oa.*;
 
public class IDLPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public IDLPPx(String name) {
        this(null, name);
    }

    public IDLPPx(PPxInterface parent, String name) {
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

    public EnvironmentPPx environments() {
        EnvironmentPPx ppx = new EnvironmentPPx(this, IDL.P_Environments);
        return ppx;
    }

    public PackageVersionPPx packageVersions() {
        PackageVersionPPx ppx = new PackageVersionPPx(this, IDL.P_PackageVersions);
        return ppx;
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
 
