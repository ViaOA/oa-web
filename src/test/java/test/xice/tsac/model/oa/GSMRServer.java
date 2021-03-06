// Generated by OABuilder
package test.xice.tsac.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.annotation.*;
import com.viaoa.util.OADateTime;

import test.xice.tsac.delegate.oa.GSMRServerDelegate;
import test.xice.tsac.model.oa.filter.*;
import test.xice.tsac.model.oa.propertypath.*;

import java.io.File;
import java.util.*;
 
@OAClass(
    shortName = "gsmrs",
    displayName = "GSMRServer",
    displayProperty = "application.server"
)
@OATable(
    indexes = {
        @OAIndex(name = "GSMRServerSilo", columns = { @OAIndexColumn(name = "SiloId") })
    }
)
public class GSMRServer extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_InstanceNumber = "InstanceNumber";
    public static final String P_InstanceNumber = "InstanceNumber";
    public static final String PROPERTY_GemstoneName = "GemstoneName";
    public static final String P_GemstoneName = "GemstoneName";
    public static final String PROPERTY_GemstoneHost = "GemstoneHost";
    public static final String P_GemstoneHost = "GemstoneHost";
    public static final String PROPERTY_GemstoneUserId = "GemstoneUserId";
    public static final String P_GemstoneUserId = "GemstoneUserId";
    public static final String PROPERTY_GemstonePassword = "GemstonePassword";
    public static final String P_GemstonePassword = "GemstonePassword";
    public static final String PROPERTY_UniqueName = "UniqueName";
    public static final String P_UniqueName = "UniqueName";
    public static final String PROPERTY_IflLogLevel = "IflLogLevel";
    public static final String P_IflLogLevel = "IflLogLevel";
    public static final String PROPERTY_TotalConnectionCount = "TotalConnectionCount";
    public static final String P_TotalConnectionCount = "TotalConnectionCount";
    public static final String PROPERTY_HeavyConnectionCount = "HeavyConnectionCount";
    public static final String P_HeavyConnectionCount = "HeavyConnectionCount";
    public static final String PROPERTY_UsedConnections = "UsedConnections";
    public static final String P_UsedConnections = "UsedConnections";
    public static final String PROPERTY_MaxUsedConnections = "MaxUsedConnections";
    public static final String P_MaxUsedConnections = "MaxUsedConnections";
    public static final String PROPERTY_AllUsedCounections = "AllUsedCounections";
    public static final String P_AllUsedCounections = "AllUsedCounections";
    public static final String PROPERTY_LogBinaryInput = "LogBinaryInput";
    public static final String P_LogBinaryInput = "LogBinaryInput";
    public static final String PROPERTY_LogBinaryOutput = "LogBinaryOutput";
    public static final String P_LogBinaryOutput = "LogBinaryOutput";
    public static final String PROPERTY_Status = "Status";
    public static final String P_Status = "Status";
    public static final String PROPERTY_LogsLoaded = "LogsLoaded";
    public static final String P_LogsLoaded = "LogsLoaded";
     
    public static final String PROPERTY_ClientCount = "ClientCount";
    public static final String P_ClientCount = "ClientCount";
     
    public static final String PROPERTY_Application = "Application";
    public static final String P_Application = "Application";
    public static final String PROPERTY_GCIConnections = "GCIConnections";
    public static final String P_GCIConnections = "GCIConnections";
    public static final String PROPERTY_GSMRClients = "GSMRClients";
    public static final String P_GSMRClients = "GSMRClients";
    public static final String PROPERTY_GSMRWarnings = "GSMRWarnings";
    public static final String P_GSMRWarnings = "GSMRWarnings";
    public static final String PROPERTY_Silo = "Silo";
    public static final String P_Silo = "Silo";
     
    protected int id;
    protected int instanceNumber;
    protected String gemstoneName;
    protected String gemstoneHost;
    protected String gemstoneUserId;
    protected String gemstonePassword;
    protected String uniqueName;
    protected int iflLogLevel;
    protected int totalConnectionCount;
    protected int heavyConnectionCount;
    protected int usedConnections;
    protected int maxUsedConnections;
    protected int allUsedCounections;
    protected boolean logBinaryInput;
    protected boolean logBinaryOutput;
    protected String status;
    protected OADateTime logsLoaded;
     
    // Links to other objects.
    protected transient Application application;
    protected transient Hub<GCIConnection> hubGCIConnections;
    protected transient Hub<GSMRClient> hubGSMRClients;
    protected transient Hub<GSMRWarning> hubGSMRWarnings;
    protected transient Silo silo;
     
    public GSMRServer() {
        if (!isLoading()) {
            setInstanceNumber(1);
        }
    }
     
    public GSMRServer(int id) {
        this();
        setId(id);
    }
     
    @OAProperty(isUnique = true, displayLength = 5, isProcessed = true)
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
    @OAProperty(displayName = "Instance Number", defaultValue = "1", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getInstanceNumber() {
        return instanceNumber;
    }
    
    public void setInstanceNumber(int newValue) {
        fireBeforePropertyChange(P_InstanceNumber, this.instanceNumber, newValue);
        int old = instanceNumber;
        this.instanceNumber = newValue;
        firePropertyChange(P_InstanceNumber, old, this.instanceNumber);
    }
    @OAProperty(displayName = "Gemstone Name", maxLength = 55, displayLength = 12, isProcessed = true)
    @OAColumn(maxLength = 55)
    public String getGemstoneName() {
        return gemstoneName;
    }
    
    public void setGemstoneName(String newValue) {
        fireBeforePropertyChange(P_GemstoneName, this.gemstoneName, newValue);
        String old = gemstoneName;
        this.gemstoneName = newValue;
        firePropertyChange(P_GemstoneName, old, this.gemstoneName);
    }
    @OAProperty(displayName = "Gemstone Host", maxLength = 55, displayLength = 14, columnLength = 12, isProcessed = true)
    @OAColumn(maxLength = 55)
    public String getGemstoneHost() {
        return gemstoneHost;
    }
    
    public void setGemstoneHost(String newValue) {
        fireBeforePropertyChange(P_GemstoneHost, this.gemstoneHost, newValue);
        String old = gemstoneHost;
        this.gemstoneHost = newValue;
        firePropertyChange(P_GemstoneHost, old, this.gemstoneHost);
    }
    @OAProperty(displayName = "Gemstone User Id", maxLength = 35, displayLength = 10, isProcessed = true)
    @OAColumn(maxLength = 35)
    public String getGemstoneUserId() {
        return gemstoneUserId;
    }
    
    public void setGemstoneUserId(String newValue) {
        fireBeforePropertyChange(PROPERTY_GemstoneUserId, this.gemstoneUserId, newValue);
        String old = gemstoneUserId;
        this.gemstoneUserId = newValue;
        firePropertyChange(PROPERTY_GemstoneUserId, old, this.gemstoneUserId);
    }
    @OAProperty(displayName = "Gemstone Password", maxLength = 35, displayLength = 10, columnLength = 8, isPassword = true, isProcessed = true)
    @OAColumn(maxLength = 35)
    public String getGemstonePassword() {
        return gemstonePassword;
    }
    
    public void setGemstonePassword(String newValue) {
        fireBeforePropertyChange(P_GemstonePassword, this.gemstonePassword, newValue);
        String old = gemstonePassword;
        this.gemstonePassword = newValue;
        firePropertyChange(P_GemstonePassword, old, this.gemstonePassword);
    }
    @OAProperty(displayName = "Unique Name", maxLength = 75, displayLength = 12, isProcessed = true)
    @OAColumn(maxLength = 75)
    public String getUniqueName() {
        return uniqueName;
    }
    
    public void setUniqueName(String newValue) {
        fireBeforePropertyChange(P_UniqueName, this.uniqueName, newValue);
        String old = uniqueName;
        this.uniqueName = newValue;
        firePropertyChange(P_UniqueName, old, this.uniqueName);
    }
    @OAProperty(displayName = "Ifl Log Level", displayLength = 3, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getIflLogLevel() {
        return iflLogLevel;
    }
    
    public void setIflLogLevel(int newValue) {
        fireBeforePropertyChange(P_IflLogLevel, this.iflLogLevel, newValue);
        int old = iflLogLevel;
        this.iflLogLevel = newValue;
        firePropertyChange(P_IflLogLevel, old, this.iflLogLevel);
    }
    @OAProperty(displayName = "Total Connection Count", displayLength = 3, columnLength = 12, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getTotalConnectionCount() {
        return totalConnectionCount;
    }
    
    public void setTotalConnectionCount(int newValue) {
        fireBeforePropertyChange(P_TotalConnectionCount, this.totalConnectionCount, newValue);
        int old = totalConnectionCount;
        this.totalConnectionCount = newValue;
        firePropertyChange(P_TotalConnectionCount, old, this.totalConnectionCount);
    }
    @OAProperty(displayName = "Heavy Connection Count", displayLength = 2, columnLength = 6, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getHeavyConnectionCount() {
        return heavyConnectionCount;
    }
    
    public void setHeavyConnectionCount(int newValue) {
        fireBeforePropertyChange(P_HeavyConnectionCount, this.heavyConnectionCount, newValue);
        int old = heavyConnectionCount;
        this.heavyConnectionCount = newValue;
        firePropertyChange(P_HeavyConnectionCount, old, this.heavyConnectionCount);
    }
    @OAProperty(displayName = "Used Connections", displayLength = 2, columnLength = 8, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getUsedConnections() {
        return usedConnections;
    }
    
    public void setUsedConnections(int newValue) {
        fireBeforePropertyChange(P_UsedConnections, this.usedConnections, newValue);
        int old = usedConnections;
        this.usedConnections = newValue;
        firePropertyChange(P_UsedConnections, old, this.usedConnections);
    }
    @OAProperty(displayName = "Max Used Connections", displayLength = 2, columnLength = 14, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getMaxUsedConnections() {
        return maxUsedConnections;
    }
    
    public void setMaxUsedConnections(int newValue) {
        fireBeforePropertyChange(P_MaxUsedConnections, this.maxUsedConnections, newValue);
        int old = maxUsedConnections;
        this.maxUsedConnections = newValue;
        firePropertyChange(P_MaxUsedConnections, old, this.maxUsedConnections);
    }
    @OAProperty(displayName = "All Used Counections", description = "the total times that all connections were in use", displayLength = 3, columnLength = 12, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    /**
      the total times that all connections were in use
    */
    public int getAllUsedCounections() {
        return allUsedCounections;
    }
    
    public void setAllUsedCounections(int newValue) {
        fireBeforePropertyChange(P_AllUsedCounections, this.allUsedCounections, newValue);
        int old = allUsedCounections;
        this.allUsedCounections = newValue;
        firePropertyChange(P_AllUsedCounections, old, this.allUsedCounections);
    }
    @OAProperty(displayName = "Log Binary Input", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getLogBinaryInput() {
        return logBinaryInput;
    }
    
    public void setLogBinaryInput(boolean newValue) {
        fireBeforePropertyChange(P_LogBinaryInput, this.logBinaryInput, newValue);
        boolean old = logBinaryInput;
        this.logBinaryInput = newValue;
        firePropertyChange(P_LogBinaryInput, old, this.logBinaryInput);
    }
    @OAProperty(displayName = "Log Binary Output", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getLogBinaryOutput() {
        return logBinaryOutput;
    }
    
    public void setLogBinaryOutput(boolean newValue) {
        fireBeforePropertyChange(P_LogBinaryOutput, this.logBinaryOutput, newValue);
        boolean old = logBinaryOutput;
        this.logBinaryOutput = newValue;
        firePropertyChange(P_LogBinaryOutput, old, this.logBinaryOutput);
    }
    @OAProperty(maxLength = 75, displayLength = 32, columnLength = 22, isProcessed = true)
    @OAColumn(maxLength = 75)
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String newValue) {
        fireBeforePropertyChange(P_Status, this.status, newValue);
        String old = status;
        this.status = newValue;
        firePropertyChange(P_Status, old, this.status);
    }
    @OAProperty(displayName = "Logs Loaded", description = "date/time when request and warning logs were loaded.", displayLength = 15, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.TIMESTAMP)
    /**
      date/time when request and warning logs were loaded.
    */
    public OADateTime getLogsLoaded() {
        return logsLoaded;
    }
    
    public void setLogsLoaded(OADateTime newValue) {
        fireBeforePropertyChange(P_LogsLoaded, this.logsLoaded, newValue);
        OADateTime old = logsLoaded;
        this.logsLoaded = newValue;
        firePropertyChange(P_LogsLoaded, old, this.logsLoaded);
    }
    @OACalculatedProperty(displayName = "Client Count", displayLength = 5, columnLength = 10, properties = {P_GSMRClients})
    public int getClientCount() {
        return this.getGSMRClients().getSize();
    }
     
    @OAOne(
        reverseName = Application.P_GSMRServer
    )
    @OAFkey(columns = {"ApplicationId"})
    public Application getApplication() {
        if (application == null) {
            application = (Application) getObject(P_Application);
        }
        return application;
    }
    
    public void setApplication(Application newValue) {
        fireBeforePropertyChange(P_Application, this.application, newValue);
        Application old = this.application;
        this.application = newValue;
        firePropertyChange(P_Application, old, this.application);
    }
    
    @OAMany(
        toClass = GCIConnection.class, 
        owner = true, 
        reverseName = GCIConnection.P_GSMRServer, 
        cascadeSave = true, 
        cascadeDelete = true
    )
    public Hub<GCIConnection> getGCIConnections() {
        if (hubGCIConnections == null) {
            hubGCIConnections = (Hub<GCIConnection>) getHub(P_GCIConnections);
        }
        return hubGCIConnections;
    }
    
    @OAMany(
        toClass = GSMRClient.class, 
        owner = true, 
        reverseName = GSMRClient.P_GSMRServer, 
        cascadeSave = true, 
        cascadeDelete = true, 
        mustBeEmptyForDelete = true
    )
    public Hub<GSMRClient> getGSMRClients() {
        if (hubGSMRClients == null) {
            hubGSMRClients = (Hub<GSMRClient>) getHub(P_GSMRClients);
        }
        return hubGSMRClients;
    }
    
    @OAMany(
        toClass = GSMRWarning.class, 
        reverseName = GSMRWarning.P_GSMRServer
    )
    public Hub<GSMRWarning> getGSMRWarnings() {
        if (hubGSMRWarnings == null) {
            hubGSMRWarnings = (Hub<GSMRWarning>) getHub(P_GSMRWarnings);
        }
        return hubGSMRWarnings;
    }
    
    @OAOne(
        reverseName = Silo.P_GSMRServers, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"SiloId"})
    public Silo getSilo() {
        if (silo == null) {
            silo = (Silo) getObject(P_Silo);
        }
        return silo;
    }
    
    public void setSilo(Silo newValue) {
        fireBeforePropertyChange(P_Silo, this.silo, newValue);
        Silo old = this.silo;
        this.silo = newValue;
        firePropertyChange(P_Silo, old, this.silo);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.instanceNumber = (int) rs.getInt(2);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRServer.P_InstanceNumber, true);
        }
        this.gemstoneName = rs.getString(3);
        this.gemstoneHost = rs.getString(4);
        this.gemstoneUserId = rs.getString(5);
        this.gemstonePassword = rs.getString(6);
        this.uniqueName = rs.getString(7);
        this.iflLogLevel = (int) rs.getInt(8);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRServer.P_IflLogLevel, true);
        }
        this.totalConnectionCount = (int) rs.getInt(9);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRServer.P_TotalConnectionCount, true);
        }
        this.heavyConnectionCount = (int) rs.getInt(10);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRServer.P_HeavyConnectionCount, true);
        }
        this.usedConnections = (int) rs.getInt(11);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRServer.P_UsedConnections, true);
        }
        this.maxUsedConnections = (int) rs.getInt(12);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRServer.P_MaxUsedConnections, true);
        }
        this.allUsedCounections = (int) rs.getInt(13);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRServer.P_AllUsedCounections, true);
        }
        this.logBinaryInput = rs.getBoolean(14);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRServer.P_LogBinaryInput, true);
        }
        this.logBinaryOutput = rs.getBoolean(15);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRServer.P_LogBinaryOutput, true);
        }
        this.status = rs.getString(16);
        java.sql.Timestamp timestamp;
        timestamp = rs.getTimestamp(17);
        if (timestamp != null) this.logsLoaded = new OADateTime(timestamp);
        int applicationFkey = rs.getInt(18);
        if (!rs.wasNull() && applicationFkey > 0) {
            setProperty(P_Application, new OAObjectKey(applicationFkey));
        }
        int siloFkey = rs.getInt(19);
        if (!rs.wasNull() && siloFkey > 0) {
            setProperty(P_Silo, new OAObjectKey(siloFkey));
        }
        if (rs.getMetaData().getColumnCount() != 19) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
