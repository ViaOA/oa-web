// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class RCCommandPP {
    private static RCExecutePPx rcExecutes;
     

    public static RCExecutePPx rcExecutes() {
        if (rcExecutes == null) rcExecutes = new RCExecutePPx(RCCommand.P_RCExecutes);
        return rcExecutes;
    }

    public static String id() {
        String s = RCCommand.P_Id;
        return s;
    }

    public static String description() {
        String s = RCCommand.P_Description;
        return s;
    }

    public static String commandLine() {
        String s = RCCommand.P_CommandLine;
        return s;
    }

    public static String type() {
        String s = RCCommand.P_Type;
        return s;
    }
}
 