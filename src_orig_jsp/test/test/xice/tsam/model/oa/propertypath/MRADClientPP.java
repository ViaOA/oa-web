// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import test.xice.tsam.model.oa.MRADClient;
import test.xice.tsam.model.oa.propertypath.ApplicationPPx;
import test.xice.tsam.model.oa.propertypath.HostInfoPPx;
import test.xice.tsam.model.oa.propertypath.MRADClientCommandPPx;
import test.xice.tsam.model.oa.propertypath.MRADClientMessagePPx;
import test.xice.tsam.model.oa.propertypath.MRADServerPPx;

import test.xice.tsam.model.oa.*;
 
public class MRADClientPP {
    private static ApplicationPPx application;
    private static HostInfoPPx hostInfo;
    private static MRADClientCommandPPx lastMRADClientCommand;
    private static MRADClientMessagePPx lastMRADClientMessage;
    private static MRADClientCommandPPx mradClientCommands;
    private static MRADClientMessagePPx mradClientMessages;
    private static MRADServerPPx mradServer;
     

    public static ApplicationPPx application() {
        if (application == null) application = new ApplicationPPx(MRADClient.P_Application);
        return application;
    }

    public static HostInfoPPx hostInfo() {
        if (hostInfo == null) hostInfo = new HostInfoPPx(MRADClient.P_HostInfo);
        return hostInfo;
    }

    public static MRADClientCommandPPx lastMRADClientCommand() {
        if (lastMRADClientCommand == null) lastMRADClientCommand = new MRADClientCommandPPx(MRADClient.P_LastMRADClientCommand);
        return lastMRADClientCommand;
    }

    public static MRADClientMessagePPx lastMRADClientMessage() {
        if (lastMRADClientMessage == null) lastMRADClientMessage = new MRADClientMessagePPx(MRADClient.P_LastMRADClientMessage);
        return lastMRADClientMessage;
    }

    public static MRADClientCommandPPx mradClientCommands() {
        if (mradClientCommands == null) mradClientCommands = new MRADClientCommandPPx(MRADClient.P_MRADClientCommands);
        return mradClientCommands;
    }

    public static MRADClientMessagePPx mradClientMessages() {
        if (mradClientMessages == null) mradClientMessages = new MRADClientMessagePPx(MRADClient.P_MRADClientMessages);
        return mradClientMessages;
    }

    public static MRADServerPPx mradServer() {
        if (mradServer == null) mradServer = new MRADServerPPx(MRADClient.P_MRADServer);
        return mradServer;
    }

    public static String id() {
        String s = MRADClient.P_Id;
        return s;
    }

    public static String created() {
        String s = MRADClient.P_Created;
        return s;
    }

    public static String hostName() {
        String s = MRADClient.P_HostName;
        return s;
    }

    public static String ipAddress() {
        String s = MRADClient.P_IpAddress;
        return s;
    }

    public static String name() {
        String s = MRADClient.P_Name;
        return s;
    }

    public static String description() {
        String s = MRADClient.P_Description;
        return s;
    }

    public static String routerAbsolutePath() {
        String s = MRADClient.P_RouterAbsolutePath;
        return s;
    }

    public static String startScript() {
        String s = MRADClient.P_StartScript;
        return s;
    }

    public static String stopScript() {
        String s = MRADClient.P_StopScript;
        return s;
    }

    public static String snapshotStartScript() {
        String s = MRADClient.P_SnapshotStartScript;
        return s;
    }

    public static String directory() {
        String s = MRADClient.P_Directory;
        return s;
    }

    public static String version() {
        String s = MRADClient.P_Version;
        return s;
    }

    public static String remoteSocketAddress() {
        String s = MRADClient.P_RemoteSocketAddress;
        return s;
    }

    public static String applicationStatus() {
        String s = MRADClient.P_ApplicationStatus;
        return s;
    }

    public static String started() {
        String s = MRADClient.P_Started;
        return s;
    }

    public static String ready() {
        String s = MRADClient.P_Ready;
        return s;
    }

    public static String serverTypeId() {
        String s = MRADClient.P_ServerTypeId;
        return s;
    }

    public static String applicationTypeCode() {
        String s = MRADClient.P_ApplicationTypeCode;
        return s;
    }

    public static String dtConnected() {
        String s = MRADClient.P_DtConnected;
        return s;
    }

    public static String dtDisconnected() {
        String s = MRADClient.P_DtDisconnected;
        return s;
    }

    public static String totalMemory() {
        String s = MRADClient.P_TotalMemory;
        return s;
    }

    public static String freeMemory() {
        String s = MRADClient.P_FreeMemory;
        return s;
    }

    public static String javaVendor() {
        String s = MRADClient.P_JavaVendor;
        return s;
    }

    public static String javaVersion() {
        String s = MRADClient.P_JavaVersion;
        return s;
    }

    public static String osArch() {
        String s = MRADClient.P_OsArch;
        return s;
    }

    public static String osName() {
        String s = MRADClient.P_OsName;
        return s;
    }

    public static String osVersion() {
        String s = MRADClient.P_OsVersion;
        return s;
    }

    public static String processId() {
        String s = MRADClient.P_ProcessId;
        return s;
    }

    public static String installedVersion() {
        String s = MRADClient.P_InstalledVersion;
        return s;
    }

    public static String dtInstall() {
        String s = MRADClient.P_DtInstall;
        return s;
    }

    public static String dtLastUpdated() {
        String s = MRADClient.P_DtLastUpdated;
        return s;
    }

    public static String lastConnectionId() {
        String s = MRADClient.P_LastConnectionId;
        return s;
    }

    public static String mradClientVersion() {
        String s = MRADClient.P_MRADClientVersion;
        return s;
    }

    public static String autoComplete() {
        String s = MRADClient.P_AutoComplete;
        return s;
    }

    public static String calcVersion() {
        String s = MRADClient.P_CalcVersion;
        return s;
    }

    public static String isConnected() {
        String s = MRADClient.P_IsConnected;
        return s;
    }
}
 