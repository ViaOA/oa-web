// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsam.model.oa.MRADClientMessage;
import test.xice.tsam.model.oa.propertypath.MRADClientPPx;
import test.xice.tsam.model.oa.propertypath.PPxInterface;

import test.xice.tsam.model.oa.*;
 
public class MRADClientMessagePPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public MRADClientMessagePPx(String name) {
        this(null, name);
    }

    public MRADClientMessagePPx(PPxInterface parent, String name) {
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

    public MRADClientPPx mradClient() {
        MRADClientPPx ppx = new MRADClientPPx(this, MRADClientMessage.P_MRADClient);
        return ppx;
    }

    public MRADClientPPx mradClient2() {
        MRADClientPPx ppx = new MRADClientPPx(this, MRADClientMessage.P_MRADClient2);
        return ppx;
    }

    public String id() {
        return pp + "." + MRADClientMessage.P_Id;
    }

    public String created() {
        return pp + "." + MRADClientMessage.P_Created;
    }

    public String text() {
        return pp + "." + MRADClientMessage.P_Text;
    }

    public String type() {
        return pp + "." + MRADClientMessage.P_Type;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 