// Generated by OABuilder
package test.xice.tsac.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.xice.tsac.model.oa.filter.*;
import test.xice.tsac.model.oa.propertypath.*;

import com.viaoa.annotation.*;
 
@OAClass(
    shortName = "rcsd",
    displayName = "RC Stage Detail",
    displayProperty = "packageName"
)
@OATable(
    indexes = {
        @OAIndex(name = "RCStageDetailRcStage", columns = { @OAIndexColumn(name = "RcStageId") })
    }
)
public class RCStageDetail extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Selected = "Selected";
    public static final String P_Selected = "Selected";
    public static final String PROPERTY_Error = "Error";
    public static final String P_Error = "Error";
    public static final String PROPERTY_Message = "Message";
    public static final String P_Message = "Message";
    public static final String PROPERTY_PackageId = "PackageId";
    public static final String P_PackageId = "PackageId";
    public static final String PROPERTY_PackageName = "PackageName";
    public static final String P_PackageName = "PackageName";
    public static final String PROPERTY_Version = "Version";
    public static final String P_Version = "Version";
    public static final String PROPERTY_DestHost = "DestHost";
    public static final String P_DestHost = "DestHost";
    public static final String PROPERTY_OrigHost = "OrigHost";
    public static final String P_OrigHost = "OrigHost";
    public static final String PROPERTY_TotalTime = "TotalTime";
    public static final String P_TotalTime = "TotalTime";
    public static final String PROPERTY_InvalidMessage = "InvalidMessage";
    public static final String P_InvalidMessage = "InvalidMessage";
     
     
    public static final String PROPERTY_OrigServer = "OrigServer";
    public static final String P_OrigServer = "OrigServer";
    public static final String PROPERTY_PackageVersion = "PackageVersion";
    public static final String P_PackageVersion = "PackageVersion";
    public static final String PROPERTY_RCDeployDetail = "RCDeployDetail";
    public static final String P_RCDeployDetail = "RCDeployDetail";
    public static final String PROPERTY_RCStage = "RCStage";
    public static final String P_RCStage = "RCStage";
    public static final String PROPERTY_Server = "Server";
    public static final String P_Server = "Server";
     
    protected int id;
    protected boolean selected;
    protected String error;
    protected String message;
    protected String packageId;
    protected String packageName;
    protected String version;
    protected String destHost;
    protected String origHost;
    protected int totalTime;
    protected String invalidMessage;
     
    // Links to other objects.
    protected transient Server origServer;
    protected transient PackageVersion packageVersion;
    protected transient RCDeployDetail rcDeployDetail;
    protected transient RCStage rcStage;
    protected transient Server server;
     
    public RCStageDetail() {
    }
     
    public RCStageDetail(int id) {
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
    @OAProperty(displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getSelected() {
        return selected;
    }
    
    public void setSelected(boolean newValue) {
        fireBeforePropertyChange(P_Selected, this.selected, newValue);
        boolean old = selected;
        this.selected = newValue;
        firePropertyChange(P_Selected, old, this.selected);
    }
    @OAProperty(maxLength = 128, displayLength = 40, columnLength = 18)
    @OAColumn(maxLength = 128)
    public String getError() {
        return error;
    }
    
    public void setError(String newValue) {
        fireBeforePropertyChange(P_Error, this.error, newValue);
        String old = error;
        this.error = newValue;
        firePropertyChange(P_Error, old, this.error);
    }
    @OAProperty(maxLength = 128, displayLength = 40, columnLength = 18)
    @OAColumn(name = "Error", maxLength = 128)
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String newValue) {
        fireBeforePropertyChange(P_Message, this.message, newValue);
        String old = message;
        this.message = newValue;
        firePropertyChange(P_Message, old, this.message);
    }
    @OAProperty(displayName = "Package Id", maxLength = 55, displayLength = 20, columnLength = 11)
    @OAColumn(maxLength = 55)
    public String getPackageId() {
        return packageId;
    }
    
    public void setPackageId(String newValue) {
        fireBeforePropertyChange(P_PackageId, this.packageId, newValue);
        String old = packageId;
        this.packageId = newValue;
        firePropertyChange(P_PackageId, old, this.packageId);
    }
    @OAProperty(displayName = "Package Name", maxLength = 55, displayLength = 20, columnLength = 11)
    @OAColumn(maxLength = 55)
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String newValue) {
        fireBeforePropertyChange(P_PackageName, this.packageName, newValue);
        String old = packageName;
        this.packageName = newValue;
        firePropertyChange(P_PackageName, old, this.packageName);
    }
    @OAProperty(maxLength = 20, displayLength = 15, columnLength = 8)
    @OAColumn(maxLength = 20)
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String newValue) {
        fireBeforePropertyChange(P_Version, this.version, newValue);
        String old = version;
        this.version = newValue;
        firePropertyChange(P_Version, old, this.version);
    }
    @OAProperty(displayName = "Dest Host", maxLength = 35, displayLength = 20, columnLength = 14)
    @OAColumn(maxLength = 35)
    public String getDestHost() {
        return destHost;
    }
    
    public void setDestHost(String newValue) {
        fireBeforePropertyChange(P_DestHost, this.destHost, newValue);
        String old = destHost;
        this.destHost = newValue;
        firePropertyChange(P_DestHost, old, this.destHost);
    }
    @OAProperty(displayName = "Orig Host", maxLength = 35, displayLength = 20, columnLength = 14)
    @OAColumn(maxLength = 35)
    public String getOrigHost() {
        return origHost;
    }
    
    public void setOrigHost(String newValue) {
        fireBeforePropertyChange(P_OrigHost, this.origHost, newValue);
        String old = origHost;
        this.origHost = newValue;
        firePropertyChange(P_OrigHost, old, this.origHost);
    }
    @OAProperty(displayName = "Total Time", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getTotalTime() {
        return totalTime;
    }
    
    public void setTotalTime(int newValue) {
        fireBeforePropertyChange(P_TotalTime, this.totalTime, newValue);
        int old = totalTime;
        this.totalTime = newValue;
        firePropertyChange(P_TotalTime, old, this.totalTime);
    }
    @OAProperty(displayName = "Invalid Message", maxLength = 120, displayLength = 40, isProcessed = true)
    @OAColumn(maxLength = 120)
    public String getInvalidMessage() {
        return invalidMessage;
    }
    
    public void setInvalidMessage(String newValue) {
        fireBeforePropertyChange(P_InvalidMessage, this.invalidMessage, newValue);
        String old = invalidMessage;
        this.invalidMessage = newValue;
        firePropertyChange(P_InvalidMessage, old, this.invalidMessage);
    }
    @OAOne(
        displayName = "Orig Server", 
        reverseName = Server.P_RCOrigStageDetails
    )
    @OAFkey(columns = {"OrigServerId"})
    public Server getOrigServer() {
        if (origServer == null) {
            origServer = (Server) getObject(P_OrigServer);
        }
        return origServer;
    }
    
    public void setOrigServer(Server newValue) {
        fireBeforePropertyChange(P_OrigServer, this.origServer, newValue);
        Server old = this.origServer;
        this.origServer = newValue;
        firePropertyChange(P_OrigServer, old, this.origServer);
    }
    
    @OAOne(
        displayName = "Package Version", 
        reverseName = PackageVersion.P_RCStageDetails
    )
    @OAFkey(columns = {"PackageVersionId"})
    public PackageVersion getPackageVersion() {
        if (packageVersion == null) {
            packageVersion = (PackageVersion) getObject(P_PackageVersion);
        }
        return packageVersion;
    }
    
    public void setPackageVersion(PackageVersion newValue) {
        fireBeforePropertyChange(P_PackageVersion, this.packageVersion, newValue);
        PackageVersion old = this.packageVersion;
        this.packageVersion = newValue;
        firePropertyChange(P_PackageVersion, old, this.packageVersion);
    }
    
    @OAOne(
        displayName = "RC Deploy Detail", 
        reverseName = RCDeployDetail.P_RCStageDetail
    )
    public RCDeployDetail getRCDeployDetail() {
        if (rcDeployDetail == null) {
            rcDeployDetail = (RCDeployDetail) getObject(P_RCDeployDetail);
        }
        return rcDeployDetail;
    }
    
    public void setRCDeployDetail(RCDeployDetail newValue) {
        fireBeforePropertyChange(P_RCDeployDetail, this.rcDeployDetail, newValue);
        RCDeployDetail old = this.rcDeployDetail;
        this.rcDeployDetail = newValue;
        firePropertyChange(P_RCDeployDetail, old, this.rcDeployDetail);
    }
    
    @OAOne(
        displayName = "RC Stage", 
        reverseName = RCStage.P_RCStageDetails, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"RcStageId"})
    public RCStage getRCStage() {
        if (rcStage == null) {
            rcStage = (RCStage) getObject(P_RCStage);
        }
        return rcStage;
    }
    
    public void setRCStage(RCStage newValue) {
        fireBeforePropertyChange(P_RCStage, this.rcStage, newValue);
        RCStage old = this.rcStage;
        this.rcStage = newValue;
        firePropertyChange(P_RCStage, old, this.rcStage);
    }
    
    @OAOne(
        reverseName = Server.P_RCStageDetails
    )
    @OAFkey(columns = {"ServerId"})
    public Server getServer() {
        if (server == null) {
            server = (Server) getObject(P_Server);
        }
        return server;
    }
    
    public void setServer(Server newValue) {
        fireBeforePropertyChange(P_Server, this.server, newValue);
        Server old = this.server;
        this.server = newValue;
        firePropertyChange(P_Server, old, this.server);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.selected = rs.getBoolean(2);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, RCStageDetail.P_Selected, true);
        }
        this.error = rs.getString(3);
        this.message = rs.getString(4);
        this.packageId = rs.getString(5);
        this.packageName = rs.getString(6);
        this.version = rs.getString(7);
        this.destHost = rs.getString(8);
        this.origHost = rs.getString(9);
        this.totalTime = (int) rs.getInt(10);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, RCStageDetail.P_TotalTime, true);
        }
        this.invalidMessage = rs.getString(11);
        int origServerFkey = rs.getInt(12);
        if (!rs.wasNull() && origServerFkey > 0) {
            setProperty(P_OrigServer, new OAObjectKey(origServerFkey));
        }
        int packageVersionFkey = rs.getInt(13);
        if (!rs.wasNull() && packageVersionFkey > 0) {
            setProperty(P_PackageVersion, new OAObjectKey(packageVersionFkey));
        }
        int rcStageFkey = rs.getInt(14);
        if (!rs.wasNull() && rcStageFkey > 0) {
            setProperty(P_RCStage, new OAObjectKey(rcStageFkey));
        }
        int serverFkey = rs.getInt(15);
        if (!rs.wasNull() && serverFkey > 0) {
            setProperty(P_Server, new OAObjectKey(serverFkey));
        }
        if (rs.getMetaData().getColumnCount() != 15) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 