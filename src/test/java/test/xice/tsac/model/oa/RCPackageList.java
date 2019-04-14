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
import test.xice.tsac.delegate.oa.RCPackageListDelegate;
import test.xice.tsac.delegate.oa.RemoteClientDelegate;
import test.xice.tsac.model.oa.filter.*;
import test.xice.tsac.model.oa.propertypath.*;
 
@OAClass(
    shortName = "rcpl",
    displayName = "RCPackage List",
    displayProperty = "created"
)
@OATable(
    indexes = {
        @OAIndex(name = "RCPackageListEnvironment", columns = { @OAIndexColumn(name = "EnvironmentId") })
    }
)
public class RCPackageList extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
     
     
    public static final String PROPERTY_Environment = "Environment";
    public static final String P_Environment = "Environment";
    public static final String PROPERTY_RCExecute = "RCExecute";
    public static final String P_RCExecute = "RCExecute";
    public static final String PROPERTY_RCPackageListDetails = "RCPackageListDetails";
    public static final String P_RCPackageListDetails = "RCPackageListDetails";
     
    protected int id;
    protected OADateTime created;
     
    // Links to other objects.
    protected transient Environment environment;
    protected transient RCExecute rcExecute;
    protected transient Hub<RCPackageListDetail> hubRCPackageListDetails;
     
    public RCPackageList() {
        if (!isLoading()) {
            setCreated(new OADateTime());
        }
    }
     
    public RCPackageList(int id) {
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
    @OAOne(
        reverseName = Environment.P_RCPackageLists, 
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
        reverseName = RCExecute.P_RCPackageLists
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
        displayName = "RCPackage List Details", 
        toClass = RCPackageListDetail.class, 
        owner = true, 
        reverseName = RCPackageListDetail.P_RCPackageList, 
        cascadeSave = true, 
        cascadeDelete = true
    )
    public Hub<RCPackageListDetail> getRCPackageListDetails() {
        if (hubRCPackageListDetails == null) {
            hubRCPackageListDetails = (Hub<RCPackageListDetail>) getHub(P_RCPackageListDetails);
        }
        return hubRCPackageListDetails;
    }
    
    // run - getting RC service listing
    public void run() throws Exception {
        RCPackageListDelegate.run(this);
    }
     
    // process - Processing RC service listing
    public void process() throws Exception {
        RCPackageListDelegate.process(this);
    }
     
    // load - Load into database
    public void load() throws Exception {
        RCPackageListDelegate.load(this);
    }
     
    // selectAll
    public void selectAll() {
        for (RCPackageListDetail detail : getRCPackageListDetails()) {
            detail.setSelected(true);
        }
    }
     
    // deselectAll
    public void deselectAll() {
        for (RCPackageListDetail detail : getRCPackageListDetails()) {
            detail.setSelected(false);
        }
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
 
