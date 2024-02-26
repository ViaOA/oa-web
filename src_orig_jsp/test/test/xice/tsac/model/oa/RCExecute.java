// Generated by OABuilder
package test.xice.tsac.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.annotation.*;
import com.viaoa.util.OADateTime;

import test.xice.tsac.delegate.RemoteDelegate;
import test.xice.tsac.delegate.ServerModelDelegate;
import test.xice.tsac.delegate.oa.RCExecuteDelegate;
import test.xice.tsac.delegate.oa.RemoteClientDelegate;
import test.xice.tsac.model.oa.filter.*;
import test.xice.tsac.model.oa.propertypath.*;
 
@OAClass(
    shortName = "rce",
    displayName = "RCExecute",
    displayProperty = "rcCommand"
)
@OATable(
)
public class RCExecute extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_Started = "Started";
    public static final String P_Started = "Started";
    public static final String PROPERTY_Completed = "Completed";
    public static final String P_Completed = "Completed";
    public static final String PROPERTY_CommandLine = "CommandLine";
    public static final String P_CommandLine = "CommandLine";
    public static final String PROPERTY_ConfigFileName = "ConfigFileName";
    public static final String P_ConfigFileName = "ConfigFileName";
    public static final String PROPERTY_Input = "Input";
    public static final String P_Input = "Input";
    public static final String PROPERTY_Output = "Output";
    public static final String P_Output = "Output";
    public static final String PROPERTY_Error = "Error";
    public static final String P_Error = "Error";
    public static final String PROPERTY_Console = "Console";
    public static final String P_Console = "Console";
    public static final String PROPERTY_Processed = "Processed";
    public static final String P_Processed = "Processed";
    public static final String PROPERTY_ProcessingOutput = "ProcessingOutput";
    public static final String P_ProcessingOutput = "ProcessingOutput";
    public static final String PROPERTY_Loaded = "Loaded";
    public static final String P_Loaded = "Loaded";
     
    public static final String PROPERTY_CanRun = "CanRun";
    public static final String P_CanRun = "CanRun";
     
    public static final String PROPERTY_RCCommand = "RCCommand";
    public static final String P_RCCommand = "RCCommand";
    public static final String PROPERTY_RCDownloads = "RCDownloads";
    public static final String P_RCDownloads = "RCDownloads";
    public static final String PROPERTY_RCInstalledVersions = "RCInstalledVersions";
    public static final String P_RCInstalledVersions = "RCInstalledVersions";
    public static final String PROPERTY_RCInstalls = "RCInstalls";
    public static final String P_RCInstalls = "RCInstalls";
    public static final String PROPERTY_RCPackageLists = "RCPackageLists";
    public static final String P_RCPackageLists = "RCPackageLists";
    public static final String PROPERTY_RCRepoVersions = "RCRepoVersions";
    public static final String P_RCRepoVersions = "RCRepoVersions";
    public static final String PROPERTY_RCServerLists = "RCServerLists";
    public static final String P_RCServerLists = "RCServerLists";
    public static final String PROPERTY_RCServiceLists = "RCServiceLists";
    public static final String P_RCServiceLists = "RCServiceLists";
    public static final String PROPERTY_RCStages = "RCStages";
    public static final String P_RCStages = "RCStages";
    public static final String PROPERTY_RCStarts = "RCStarts";
    public static final String P_RCStarts = "RCStarts";
    public static final String PROPERTY_RCStops = "RCStops";
    public static final String P_RCStops = "RCStops";
    public static final String PROPERTY_RCVerifies = "RCVerifies";
    public static final String P_RCVerifies = "RCVerifies";
    public static final String PROPERTY_RemoteClient = "RemoteClient";
    public static final String P_RemoteClient = "RemoteClient";
     
    protected int id;
    protected OADateTime created;
    protected OADateTime started;
    protected OADateTime completed;
    protected String commandLine;
    protected String configFileName;
    protected String input;
    protected String output;
    protected String error;
    protected String console;
    protected OADateTime processed;
    protected String processingOutput;
    protected OADateTime loaded;
     
    // Links to other objects.
    protected transient RCCommand rcCommand;
    protected transient RemoteClient remoteClient;
     
    public RCExecute() {
        if (!isLoading()) {
            setCreated(new OADateTime());
        }
    }
     
    public RCExecute(int id) {
        this();
        setId(id);
    }
     
    @OAProperty(isUnique = true, displayLength = 5)
    @OAId()
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getId() {
        return id;
    }
    
    public void setId(int newValue) {
        fireBeforePropertyChange(P_Id, this.id, newValue);
        int old = id;
        this.id = newValue;
        firePropertyChange(P_Id, old, this.id);
    }
    @OAProperty(defaultValue = "new OADateTime()", displayLength = 15)
    @OAColumn(sqlType = java.sql.Types.TIMESTAMP)
    public OADateTime getCreated() {
        return created;
    }
    
    public void setCreated(OADateTime newValue) {
        fireBeforePropertyChange(P_Created, this.created, newValue);
        OADateTime old = created;
        this.created = newValue;
        firePropertyChange(P_Created, old, this.created);
    }
    @OAProperty(displayLength = 15, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.TIMESTAMP)
    public OADateTime getStarted() {
        return started;
    }
    
    public void setStarted(OADateTime newValue) {
        fireBeforePropertyChange(P_Started, this.started, newValue);
        OADateTime old = started;
        this.started = newValue;
        firePropertyChange(P_Started, old, this.started);
    }
    @OAProperty(displayLength = 15, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.TIMESTAMP)
    public OADateTime getCompleted() {
        return completed;
    }
    
    public void setCompleted(OADateTime newValue) {
        fireBeforePropertyChange(P_Completed, this.completed, newValue);
        OADateTime old = completed;
        this.completed = newValue;
        firePropertyChange(P_Completed, old, this.completed);
    }
    @OAProperty(displayName = "Command Line", maxLength = 500, displayLength = 40, columnLength = 24)
    @OAColumn(maxLength = 500)
    public String getCommandLine() {
        return commandLine;
    }
    
    public void setCommandLine(String newValue) {
        fireBeforePropertyChange(P_CommandLine, this.commandLine, newValue);
        String old = commandLine;
        this.commandLine = newValue;
        firePropertyChange(P_CommandLine, old, this.commandLine);
    }
    @OAProperty(displayName = "Config File Name", maxLength = 125, displayLength = 40, columnLength = 18)
    @OAColumn(maxLength = 125)
    public String getConfigFileName() {
        return configFileName;
    }
    
    public void setConfigFileName(String newValue) {
        fireBeforePropertyChange(P_ConfigFileName, this.configFileName, newValue);
        String old = configFileName;
        this.configFileName = newValue;
        firePropertyChange(P_ConfigFileName, old, this.configFileName);
    }
    @OAProperty(maxLength = 6, displayLength = 6)
    @OAColumn(sqlType = java.sql.Types.CLOB)
    public String getInput() {
        return input;
    }
    
    public void setInput(String newValue) {
        fireBeforePropertyChange(P_Input, this.input, newValue);
        String old = input;
        this.input = newValue;
        firePropertyChange(P_Input, old, this.input);
    }
    @OAProperty(maxLength = 6, displayLength = 6)
    @OAColumn(sqlType = java.sql.Types.CLOB)
    public String getOutput() {
        return output;
    }
    
    public void setOutput(String newValue) {
        fireBeforePropertyChange(P_Output, this.output, newValue);
        String old = output;
        this.output = newValue;
        firePropertyChange(P_Output, old, this.output);
    }
    @OAProperty(maxLength = 150, displayLength = 40, columnLength = 18, isProcessed = true)
    @OAColumn(maxLength = 150)
    public String getError() {
        return error;
    }
    
    public void setError(String newValue) {
        fireBeforePropertyChange(P_Error, this.error, newValue);
        String old = error;
        this.error = newValue;
        firePropertyChange(P_Error, old, this.error);
    }
    @OAProperty(maxLength = 800, displayLength = 40)
    public String getConsole() {
        return console;
    }
    
    public void setConsole(String newValue) {
        fireBeforePropertyChange(P_Console, this.console, newValue);
        String old = console;
        this.console = newValue;
        firePropertyChange(P_Console, old, this.console);
    }
    @OAProperty(displayLength = 15, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.TIMESTAMP)
    public OADateTime getProcessed() {
        return processed;
    }
    
    public void setProcessed(OADateTime newValue) {
        fireBeforePropertyChange(P_Processed, this.processed, newValue);
        OADateTime old = processed;
        this.processed = newValue;
        firePropertyChange(P_Processed, old, this.processed);
    }
    @OAProperty(displayName = "Processing Output", maxLength = 16, displayLength = 16, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.CLOB)
    public String getProcessingOutput() {
        return processingOutput;
    }
    
    public void setProcessingOutput(String newValue) {
        fireBeforePropertyChange(P_ProcessingOutput, this.processingOutput, newValue);
        String old = processingOutput;
        this.processingOutput = newValue;
        firePropertyChange(P_ProcessingOutput, old, this.processingOutput);
    }
    @OAProperty(displayLength = 15)
    @OAColumn(sqlType = java.sql.Types.TIMESTAMP)
    public OADateTime getLoaded() {
        return loaded;
    }
    
    public void setLoaded(OADateTime newValue) {
        fireBeforePropertyChange(P_Loaded, this.loaded, newValue);
        OADateTime old = loaded;
        this.loaded = newValue;
        firePropertyChange(P_Loaded, old, this.loaded);
    }
    @OACalculatedProperty(displayName = "Can Run", displayLength = 5, properties = {P_Completed, P_RemoteClient+"."+RemoteClient.P_Status})
    public boolean getCanRun() {
         if (this.getCompleted() != null) return false;
    
         RemoteClient rc = getRemoteClient();
         if (rc == null) return false;
         if (rc.getStatus() != RemoteClient.STATUS_Connected) return false;
    
        return true;
    }
    
     
    @OAOne(
        reverseName = RCCommand.P_RCExecutes, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"RcCommandId"})
    public RCCommand getRCCommand() {
        if (rcCommand == null) {
            rcCommand = (RCCommand) getObject(P_RCCommand);
        }
        return rcCommand;
    }
    
    public void setRCCommand(RCCommand newValue) {
        fireBeforePropertyChange(P_RCCommand, this.rcCommand, newValue);
        RCCommand old = this.rcCommand;
        this.rcCommand = newValue;
        firePropertyChange(P_RCCommand, old, this.rcCommand);
    }
    
    @OAMany(
        toClass = RCDownload.class, 
        reverseName = RCDownload.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCDownload> getRCDownloads() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RCInstalled Versions", 
        toClass = RCInstalledVersion.class, 
        reverseName = RCInstalledVersion.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCInstalledVersion> getRCInstalledVersions() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RC Installs", 
        toClass = RCInstall.class, 
        reverseName = RCInstall.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCInstall> getRCInstalls() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RCPackage Lists", 
        toClass = RCPackageList.class, 
        reverseName = RCPackageList.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCPackageList> getRCPackageLists() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RCRepo Versions", 
        toClass = RCRepoVersion.class, 
        reverseName = RCRepoVersion.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCRepoVersion> getRCRepoVersions() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RCServer Lists", 
        toClass = RCServerList.class, 
        reverseName = RCServerList.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCServerList> getRCServerLists() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RCService Lists", 
        toClass = RCServiceList.class, 
        reverseName = RCServiceList.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCServiceList> getRCServiceLists() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RC Stages", 
        toClass = RCStage.class, 
        reverseName = RCStage.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCStage> getRCStages() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RC Starts", 
        toClass = RCStart.class, 
        reverseName = RCStart.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCStart> getRCStarts() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RC Stops", 
        toClass = RCStop.class, 
        reverseName = RCStop.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCStop> getRCStops() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RC Verifies", 
        toClass = RCVerify.class, 
        reverseName = RCVerify.P_RCExecute, 
        createMethod = false
    )
    private Hub<RCVerify> getRCVerifies() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        displayName = "Remote Client", 
        isCalculated = true, 
        reverseName = RemoteClient.P_RCExecutes, 
        allowCreateNew = false
    )
    public RemoteClient getRemoteClient() {
        if (remoteClient == null) {
            remoteClient = (RemoteClient) getObject(P_RemoteClient);
            if (remoteClient == null && isServer()) {
                remoteClient = RemoteClientDelegate.getRemoteClient(RemoteClient.TYPE_RC);
            }
        }
        return remoteClient;
    }
    // run - Run command
    public void run() {
        RCCommand cmd = getRCCommand();
        if (cmd == null) throw new RuntimeException("must have Command selected");
        try {
            setError(null);
            RCExecuteDelegate.runOnRCInstance(this);
        }
        catch (Exception e) {
            setError("exception while invoking command, exception="+e.getMessage());
            throw new RuntimeException("exception while invoking command", e);
        }
    }
     
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Timestamp timestamp;
        timestamp = rs.getTimestamp(2);
        if (timestamp != null) this.created = new OADateTime(timestamp);
        timestamp = rs.getTimestamp(3);
        if (timestamp != null) this.started = new OADateTime(timestamp);
        timestamp = rs.getTimestamp(4);
        if (timestamp != null) this.completed = new OADateTime(timestamp);
        this.commandLine = rs.getString(5);
        this.configFileName = rs.getString(6);
        this.input = rs.getString(7);
        this.output = rs.getString(8);
        this.error = rs.getString(9);
        timestamp = rs.getTimestamp(10);
        if (timestamp != null) this.processed = new OADateTime(timestamp);
        this.processingOutput = rs.getString(11);
        timestamp = rs.getTimestamp(12);
        if (timestamp != null) this.loaded = new OADateTime(timestamp);
        int rcCommandFkey = rs.getInt(13);
        if (!rs.wasNull() && rcCommandFkey > 0) {
            setProperty(P_RCCommand, new OAObjectKey(rcCommandFkey));
        }
        if (rs.getMetaData().getColumnCount() != 13) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 