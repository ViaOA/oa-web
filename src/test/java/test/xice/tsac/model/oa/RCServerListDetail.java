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
    shortName = "rcsld",
    displayName = "RCServer List Detail",
    displayProperty = "hostName"
)
@OATable(
    indexes = {
        @OAIndex(name = "RCServerListDetailRcServerList", columns = { @OAIndexColumn(name = "RcServerListId") })
    }
)
public class RCServerListDetail extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_HostName = "HostName";
    public static final String P_HostName = "HostName";
    public static final String PROPERTY_Packages = "Packages";
    public static final String P_Packages = "Packages";
    public static final String PROPERTY_InvalidMessage = "InvalidMessage";
    public static final String P_InvalidMessage = "InvalidMessage";
    public static final String PROPERTY_Selected = "Selected";
    public static final String P_Selected = "Selected";
    public static final String PROPERTY_Loaded = "Loaded";
    public static final String P_Loaded = "Loaded";
     
     
    public static final String PROPERTY_Applications = "Applications";
    public static final String P_Applications = "Applications";
    public static final String PROPERTY_RCServerList = "RCServerList";
    public static final String P_RCServerList = "RCServerList";
    public static final String PROPERTY_Server = "Server";
    public static final String P_Server = "Server";
     
    protected int id;
    protected String hostName;
    protected String packages;
    protected String invalidMessage;
    protected boolean selected;
    protected boolean loaded;
     
    // Links to other objects.
    protected transient Hub<Application> hubApplications;
    protected transient RCServerList rcServerList;
    protected transient Server server;
     
    public RCServerListDetail() {
    }
     
    public RCServerListDetail(int id) {
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
    @OAProperty(displayName = "Host Name", maxLength = 25, displayLength = 25, columnLength = 5)
    @OAColumn(maxLength = 25)
    public String getHostName() {
        return hostName;
    }
    
    public void setHostName(String newValue) {
        fireBeforePropertyChange(P_HostName, this.hostName, newValue);
        String old = hostName;
        this.hostName = newValue;
        firePropertyChange(P_HostName, old, this.hostName);
    }
    @OAProperty(maxLength = 128, displayLength = 24, columnLength = 10)
    @OAColumn(maxLength = 128)
    public String getPackages() {
        return packages;
    }
    
    public void setPackages(String newValue) {
        fireBeforePropertyChange(P_Packages, this.packages, newValue);
        String old = packages;
        this.packages = newValue;
        firePropertyChange(P_Packages, old, this.packages);
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
    @OAProperty(displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getLoaded() {
        return loaded;
    }
    
    public void setLoaded(boolean newValue) {
        fireBeforePropertyChange(P_Loaded, this.loaded, newValue);
        boolean old = loaded;
        this.loaded = newValue;
        firePropertyChange(P_Loaded, old, this.loaded);
    }
    @OAMany(
        toClass = Application.class, 
        reverseName = Application.P_RCServerListDetails
    )
    @OALinkTable(name = "RCServerListDetailApplication", indexName = "ApplicationRcServerListDetail", columns = {"RCServerListDetailId"})
    public Hub<Application> getApplications() {
        if (hubApplications == null) {
            hubApplications = (Hub<Application>) getHub(P_Applications);
        }
        return hubApplications;
    }
    
    @OAOne(
        displayName = "RCServer List", 
        reverseName = RCServerList.P_RCServerListDetails, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"RcServerListId"})
    public RCServerList getRCServerList() {
        if (rcServerList == null) {
            rcServerList = (RCServerList) getObject(P_RCServerList);
        }
        return rcServerList;
    }
    
    public void setRCServerList(RCServerList newValue) {
        fireBeforePropertyChange(P_RCServerList, this.rcServerList, newValue);
        RCServerList old = this.rcServerList;
        this.rcServerList = newValue;
        firePropertyChange(P_RCServerList, old, this.rcServerList);
    }
    
    @OAOne(
        reverseName = Server.P_RCServerListDetails
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
        this.hostName = rs.getString(2);
        this.packages = rs.getString(3);
        this.invalidMessage = rs.getString(4);
        this.selected = rs.getBoolean(5);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, RCServerListDetail.P_Selected, true);
        }
        this.loaded = rs.getBoolean(6);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, RCServerListDetail.P_Loaded, true);
        }
        int rcServerListFkey = rs.getInt(7);
        if (!rs.wasNull() && rcServerListFkey > 0) {
            setProperty(P_RCServerList, new OAObjectKey(rcServerListFkey));
        }
        int serverFkey = rs.getInt(8);
        if (!rs.wasNull() && serverFkey > 0) {
            setProperty(P_Server, new OAObjectKey(serverFkey));
        }
        if (rs.getMetaData().getColumnCount() != 8) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
