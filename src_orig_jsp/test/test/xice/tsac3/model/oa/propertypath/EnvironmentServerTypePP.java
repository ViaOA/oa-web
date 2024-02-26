// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import test.xice.tsac3.model.oa.*;
 
public class EnvironmentServerTypePP {
    private static EnvironmentPPx environment;
    private static ServerTypePPx serverType;
    private static ServerTypeVersionPPx serverTypeVersion;
     

    public static EnvironmentPPx environment() {
        if (environment == null) environment = new EnvironmentPPx(EnvironmentServerType.P_Environment);
        return environment;
    }

    public static ServerTypePPx serverType() {
        if (serverType == null) serverType = new ServerTypePPx(EnvironmentServerType.P_ServerType);
        return serverType;
    }

    public static ServerTypeVersionPPx serverTypeVersion() {
        if (serverTypeVersion == null) serverTypeVersion = new ServerTypeVersionPPx(EnvironmentServerType.P_ServerTypeVersion);
        return serverTypeVersion;
    }

    public static String id() {
        String s = EnvironmentServerType.P_Id;
        return s;
    }
}
 