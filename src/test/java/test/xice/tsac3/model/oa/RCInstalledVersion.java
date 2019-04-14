// Generated by OABuilder
package test.xice.tsac3.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.annotation.*;
import com.viaoa.util.OADateTime;

import test.xice.tsac3.model.oa.filter.*;
import test.xice.tsac3.model.oa.propertypath.*;
 
@OAClass(
    shortName = "rciv",
    displayName = "RCInstalled Version",
    displayProperty = "created"
)
@OATable(
    indexes = {
        @OAIndex(name = "RCInstalledVersionEnvironment", columns = { @OAIndexColumn(name = "EnvironmentId") })
    }
)
public class RCInstalledVersion extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
     
    public static final String PROPERTY_RCAvailable = "RCAvailable";
    public static final String P_RCAvailable = "RCAvailable";
    public static final String PROPERTY_CanRun = "CanRun";
    public static final String P_CanRun = "CanRun";
    public static final String PROPERTY_CanProcess = "CanProcess";
    public static final String P_CanProcess = "CanProcess";
    public static final String PROPERTY_CanLoad = "CanLoad";
    public static final String P_CanLoad = "CanLoad";
     
    public static final String PROPERTY_Environment = "Environment";
    public static final String P_Environment = "Environment";
    public static final String PROPERTY_RCExecute = "RCExecute";
    public static final String P_RCExecute = "RCExecute";
    public static final String PROPERTY_RCInstalledVersionDetails = "RCInstalledVersionDetails";
    public static final String P_RCInstalledVersionDetails = "RCInstalledVersionDetails";
     
    protected int id;
    protected OADateTime created;
     
    // Links to other objects.
    protected transient Environment environment;
    protected transient RCExecute rcExecute;
    protected transient Hub<RCInstalledVersionDetail> hubRCInstalledVersionDetails;
     
    public RCInstalledVersion() {
        if (!isLoading()) {
            setCreated(new OADateTime());
        }
    }
     
    public RCInstalledVersion(int id) {
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
    @OAProperty(defaultValue = "new OADateTime()", displayLength = 15, isProcessed = true)
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
    @OACalculatedProperty(displayLength = 5)
    public boolean getRCAvailable() {
        return true;
    }
     
    @OACalculatedProperty(displayName = "Can Run", displayLength = 5, properties = {P_RCExecute+"."+RCExecute.P_Started})
    public boolean getCanRun() {
        RCExecute ex = getRCExecute();
        if (ex != null && ex.getStarted() != null) return false;
        if (!getRCAvailable()) return false;
        return true;
    }
     
    @OACalculatedProperty(displayName = "Can Process", displayLength = 5, properties = {P_RCExecute+"."+RCExecute.P_Completed, P_RCExecute+"."+RCExecute.P_Loaded})
    public boolean getCanProcess() {
        RCExecute ex = getRCExecute();
        if (ex == null || ex.getCompleted() == null) return false;
        if (ex.getLoaded() != null) return false;
        return true;
    }
     
    @OACalculatedProperty(displayName = "Can Load", displayLength = 5, properties = {P_RCExecute+"."+RCExecute.P_Processed, P_RCExecute+"."+RCExecute.P_Loaded})
    public boolean getCanLoad() {
        RCExecute ex = getRCExecute();
        if (ex == null || ex.getCompleted() == null) return false;
        if (ex.getProcessed() == null) return false;
        if (ex.getLoaded() != null) return false;
        return true;
    }
     
    @OAOne(
        reverseName = Environment.P_RCInstalledVersions, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"EnvironmentId"})
    public Environment getEnvironment() {
        if (environment == null) {
            environment = (Environment) getObject(P_Environment);
        }
        return environment;
    }
    
    public void setEnvironment(Environment newValue) {
        fireBeforePropertyChange(P_Environment, this.environment, newValue);
        Environment old = this.environment;
        this.environment = newValue;
        firePropertyChange(P_Environment, old, this.environment);
    }
    
    @OAOne(
        reverseName = RCExecute.P_RCInstalledVersions
    )
    @OAFkey(columns = {"RcExecuteId"})
    public RCExecute getRCExecute() {
        if (rcExecute == null) {
            rcExecute = (RCExecute) getObject(P_RCExecute);
        }
        return rcExecute;
    }
    
    public void setRCExecute(RCExecute newValue) {
        fireBeforePropertyChange(P_RCExecute, this.rcExecute, newValue);
        RCExecute old = this.rcExecute;
        this.rcExecute = newValue;
        firePropertyChange(P_RCExecute, old, this.rcExecute);
    }
    
    @OAMany(
        displayName = "RCInstalled Version Details", 
        toClass = RCInstalledVersionDetail.class, 
        owner = true, 
        reverseName = RCInstalledVersionDetail.P_RCInstalledVersion, 
        cascadeSave = true, 
        cascadeDelete = true
    )
    public Hub<RCInstalledVersionDetail> getRCInstalledVersionDetails() {
        if (hubRCInstalledVersionDetails == null) {
            hubRCInstalledVersionDetails = (Hub<RCInstalledVersionDetail>) getHub(P_RCInstalledVersionDetails);
        }
        return hubRCInstalledVersionDetails;
    }
    
    // run - running RC to get Installed Versions
    public void run() throws Exception {
    }
     
    // process - Processing RC server installed versions
    public void process() throws Exception {
    }
     
    // load - Load into database
    public void load() throws Exception {
    }
     
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Timestamp timestamp;
        timestamp = rs.getTimestamp(2);
        if (timestamp != null) this.created = new OADateTime(timestamp);
        int environmentFkey = rs.getInt(3);
        if (!rs.wasNull() && environmentFkey > 0) {
            setProperty(P_Environment, new OAObjectKey(environmentFkey));
        }
        int rcExecuteFkey = rs.getInt(4);
        if (!rs.wasNull() && rcExecuteFkey > 0) {
            setProperty(P_RCExecute, new OAObjectKey(rcExecuteFkey));
        }
        if (rs.getMetaData().getColumnCount() != 4) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
