// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class SiloTypePP {
    private static ServerTypePPx serverTypes;
    private static SiloPPx silos;
     

    public static ServerTypePPx serverTypes() {
        if (serverTypes == null) serverTypes = new ServerTypePPx(SiloType.P_ServerTypes);
        return serverTypes;
    }

    public static SiloPPx silos() {
        if (silos == null) silos = new SiloPPx(SiloType.P_Silos);
        return silos;
    }

    public static String id() {
        String s = SiloType.P_Id;
        return s;
    }

    public static String name() {
        String s = SiloType.P_Name;
        return s;
    }

    public static String type() {
        String s = SiloType.P_Type;
        return s;
    }
}
 