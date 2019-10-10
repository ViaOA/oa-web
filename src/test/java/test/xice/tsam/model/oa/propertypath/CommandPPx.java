// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsam.model.oa.Command;
import test.xice.tsam.model.oa.propertypath.ApplicationTypeCommandPPx;
import test.xice.tsam.model.oa.propertypath.MRADServerCommandPPx;
import test.xice.tsam.model.oa.propertypath.PPxInterface;

import test.xice.tsam.model.oa.*;
 
public class CommandPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public CommandPPx(String name) {
        this(null, name);
    }

    public CommandPPx(PPxInterface parent, String name) {
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

    public ApplicationTypeCommandPPx applicationTypeCommands() {
        ApplicationTypeCommandPPx ppx = new ApplicationTypeCommandPPx(this, Command.P_ApplicationTypeCommands);
        return ppx;
    }

    public MRADServerCommandPPx mradServerCommands() {
        MRADServerCommandPPx ppx = new MRADServerCommandPPx(this, Command.P_MRADServerCommands);
        return ppx;
    }

    public String id() {
        return pp + "." + Command.P_Id;
    }

    public String created() {
        return pp + "." + Command.P_Created;
    }

    public String seq() {
        return pp + "." + Command.P_Seq;
    }

    public String name() {
        return pp + "." + Command.P_Name;
    }

    public String description() {
        return pp + "." + Command.P_Description;
    }

    public String commandLine() {
        return pp + "." + Command.P_CommandLine;
    }

    public String type() {
        return pp + "." + Command.P_Type;
    }

    public String inAPI() {
        return pp + "." + Command.P_InAPI;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 