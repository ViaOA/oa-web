// Generated by OABuilder
package test.xice.tsac2.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac2.model.oa.*;
 
public class ApplicationPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
     
    public ApplicationPPx(String name) {
        this(null, name);
    }

    public ApplicationPPx(PPxInterface parent, String name) {
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

    public AdminClientPPx adminClient() {
        AdminClientPPx ppx = new AdminClientPPx(this, Application.P_AdminClient);
        return ppx;
    }

    public AdminServerPPx adminServer() {
        AdminServerPPx ppx = new AdminServerPPx(this, Application.P_AdminServer);
        return ppx;
    }

    public ApplicationGroupPPx applicationGroup() {
        ApplicationGroupPPx ppx = new ApplicationGroupPPx(this, Application.P_ApplicationGroup);
        return ppx;
    }

    public ApplicationStatusPPx applicationStatus() {
        ApplicationStatusPPx ppx = new ApplicationStatusPPx(this, Application.P_ApplicationStatus);
        return ppx;
    }

    public ApplicationTypePPx applicationType() {
        ApplicationTypePPx ppx = new ApplicationTypePPx(this, Application.P_ApplicationType);
        return ppx;
    }

    public ApplicationVersionPPx applicationVersions() {
        ApplicationVersionPPx ppx = new ApplicationVersionPPx(this, Application.P_ApplicationVersions);
        return ppx;
    }

    public GSMRClientPPx gsmrClient() {
        GSMRClientPPx ppx = new GSMRClientPPx(this, Application.P_GSMRClient);
        return ppx;
    }

    public GSMRServerPPx gsmrServer() {
        GSMRServerPPx ppx = new GSMRServerPPx(this, Application.P_GSMRServer);
        return ppx;
    }

    public InstallVersionPPx installVersions() {
        InstallVersionPPx ppx = new InstallVersionPPx(this, Application.P_InstallVersions);
        return ppx;
    }

    public LLADClientPPx lladClient() {
        LLADClientPPx ppx = new LLADClientPPx(this, Application.P_LLADClient);
        return ppx;
    }

    public LLADServerPPx lladServer() {
        LLADServerPPx ppx = new LLADServerPPx(this, Application.P_LLADServer);
        return ppx;
    }

    public RCInstalledVersionDetailPPx rcInstalledVersionDetails() {
        RCInstalledVersionDetailPPx ppx = new RCInstalledVersionDetailPPx(this, Application.P_RCInstalledVersionDetails);
        return ppx;
    }

    public RCServerListDetailPPx rcServerListDetails() {
        RCServerListDetailPPx ppx = new RCServerListDetailPPx(this, Application.P_RCServerListDetails);
        return ppx;
    }

    public SchedulePPx schedules() {
        SchedulePPx ppx = new SchedulePPx(this, Application.P_Schedules);
        return ppx;
    }

    public ServerPPx server() {
        ServerPPx ppx = new ServerPPx(this, Application.P_Server);
        return ppx;
    }

    public ServerFilePPx serverFiles() {
        ServerFilePPx ppx = new ServerFilePPx(this, Application.P_ServerFiles);
        return ppx;
    }

    public String id() {
        return pp + "." + Application.P_Id;
    }

    public String instanceNumber() {
        return pp + "." + Application.P_InstanceNumber;
    }

    public String tradingSystemId() {
        return pp + "." + Application.P_TradingSystemId;
    }

    public String directory() {
        return pp + "." + Application.P_Directory;
    }

    public String startCommand() {
        return pp + "." + Application.P_StartCommand;
    }

    public String stopCommand() {
        return pp + "." + Application.P_StopCommand;
    }

    public String start() {
        return pp + ".start";
    }

    public String stop() {
        return pp + ".stop";
    }

    public String kill() {
        return pp + ".kill";
    }

    public String suspend() {
        return pp + ".suspend";
    }

    public String resume() {
        return pp + ".resume";
    }

    public String ping() {
        return pp + ".ping";
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
