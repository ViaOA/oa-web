// Generated by OABuilder
package test.xice.tsac3.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.xice.tsac3.model.oa.filter.*;
import test.xice.tsac3.model.oa.propertypath.*;

import com.viaoa.annotation.*;
 
@OAClass(
    shortName = "gsmrc",
    displayName = "GSMRClient",
    displayProperty = "server"
)
@OATable(
    indexes = {
        @OAIndex(name = "GSMRClientGsmrServer", columns = { @OAIndexColumn(name = "GsmrServerId") })
    }
)
public class GSMRClient extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_ConnectionId = "ConnectionId";
    public static final String P_ConnectionId = "ConnectionId";
    public static final String PROPERTY_ClientType = "ClientType";
    public static final String P_ClientType = "ClientType";
    public static final String PROPERTY_ClientDescription = "ClientDescription";
    public static final String P_ClientDescription = "ClientDescription";
    public static final String PROPERTY_TotalRequests = "TotalRequests";
    public static final String P_TotalRequests = "TotalRequests";
    public static final String PROPERTY_TotalRequestTime = "TotalRequestTime";
    public static final String P_TotalRequestTime = "TotalRequestTime";
     
     
    public static final String PROPERTY_GSMRServer = "GSMRServer";
    public static final String P_GSMRServer = "GSMRServer";
    public static final String PROPERTY_GSRequests = "GSRequests";
    public static final String P_GSRequests = "GSRequests";
    public static final String PROPERTY_Server = "Server";
    public static final String P_Server = "Server";
    public static final String PROPERTY_ServerStatus = "ServerStatus";
    public static final String P_ServerStatus = "ServerStatus";
     
    protected int id;
    protected int connectionId;
    protected String clientType;
    protected String clientDescription;
    protected int totalRequests;
    protected long totalRequestTime;
     
    // Links to other objects.
    protected transient GSMRServer gsmrServer;
    protected transient Hub<GSRequest> hubGSRequests;
    protected transient Server server;
    protected transient ServerStatus serverStatus;
     
    public GSMRClient() {
    }
     
    public GSMRClient(int id) {
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
    @OAProperty(displayName = "Connection Id", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getConnectionId() {
        return connectionId;
    }
    
    public void setConnectionId(int newValue) {
        fireBeforePropertyChange(P_ConnectionId, this.connectionId, newValue);
        int old = connectionId;
        this.connectionId = newValue;
        firePropertyChange(P_ConnectionId, old, this.connectionId);
    }
    @OAProperty(displayName = "Client Type", maxLength = 25, displayLength = 20, isProcessed = true)
    @OAColumn(maxLength = 25)
    public String getClientType() {
        return clientType;
    }
    
    public void setClientType(String newValue) {
        fireBeforePropertyChange(P_ClientType, this.clientType, newValue);
        String old = clientType;
        this.clientType = newValue;
        firePropertyChange(P_ClientType, old, this.clientType);
    }
    @OAProperty(displayName = "Client Description", maxLength = 300, displayLength = 35, columnLength = 20, isProcessed = true)
    @OAColumn(maxLength = 300)
    public String getClientDescription() {
        return clientDescription;
    }
    
    public void setClientDescription(String newValue) {
        fireBeforePropertyChange(P_ClientDescription, this.clientDescription, newValue);
        String old = clientDescription;
        this.clientDescription = newValue;
        firePropertyChange(P_ClientDescription, old, this.clientDescription);
    }
    @OAProperty(displayName = "Total Requests", displayLength = 7, columnLength = 6, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getTotalRequests() {
        return totalRequests;
    }
    
    public void setTotalRequests(int newValue) {
        fireBeforePropertyChange(P_TotalRequests, this.totalRequests, newValue);
        int old = totalRequests;
        this.totalRequests = newValue;
        firePropertyChange(P_TotalRequests, old, this.totalRequests);
    }
    @OAProperty(displayName = "Total Request Time (ms)", displayLength = 6, columnLength = 14, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public long getTotalRequestTime() {
        return totalRequestTime;
    }
    
    public void setTotalRequestTime(long newValue) {
        fireBeforePropertyChange(P_TotalRequestTime, this.totalRequestTime, newValue);
        long old = totalRequestTime;
        this.totalRequestTime = newValue;
        firePropertyChange(P_TotalRequestTime, old, this.totalRequestTime);
    }
    @OAOne(
        reverseName = GSMRServer.P_GSMRClients, 
        required = true, 
        allowCreateNew = false, 
        mustBeEmptyForDelete = true
    )
    @OAFkey(columns = {"GsmrServerId"})
    public GSMRServer getGSMRServer() {
        if (gsmrServer == null) {
            gsmrServer = (GSMRServer) getObject(P_GSMRServer);
        }
        return gsmrServer;
    }
    
    public void setGSMRServer(GSMRServer newValue) {
        fireBeforePropertyChange(P_GSMRServer, this.gsmrServer, newValue);
        GSMRServer old = this.gsmrServer;
        this.gsmrServer = newValue;
        firePropertyChange(P_GSMRServer, old, this.gsmrServer);
    }
    
    @OAMany(
        toClass = GSRequest.class, 
        owner = true, 
        reverseName = GSRequest.P_GSMRClient, 
        cascadeSave = true, 
        cascadeDelete = true, 
        mustBeEmptyForDelete = true
    )
    public Hub<GSRequest> getGSRequests() {
        if (hubGSRequests == null) {
            hubGSRequests = (Hub<GSRequest>) getHub(P_GSRequests);
        }
        return hubGSRequests;
    }
    
    @OAOne(
        reverseName = Server.P_GSMRClients
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
    
    @OAOne(
        displayName = "Server Status", 
        reverseName = ServerStatus.P_GSMRClients, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"ServerStatusId"})
    public ServerStatus getServerStatus() {
        if (serverStatus == null) {
            serverStatus = (ServerStatus) getObject(P_ServerStatus);
        }
        return serverStatus;
    }
    
    public void setServerStatus(ServerStatus newValue) {
        fireBeforePropertyChange(P_ServerStatus, this.serverStatus, newValue);
        ServerStatus old = this.serverStatus;
        this.serverStatus = newValue;
        firePropertyChange(P_ServerStatus, old, this.serverStatus);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.connectionId = (int) rs.getInt(2);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRClient.P_ConnectionId, true);
        }
        this.clientType = rs.getString(3);
        this.clientDescription = rs.getString(4);
        this.totalRequests = (int) rs.getInt(5);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRClient.P_TotalRequests, true);
        }
        this.totalRequestTime = (long) rs.getLong(6);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSMRClient.P_TotalRequestTime, true);
        }
        int gsmrServerFkey = rs.getInt(7);
        if (!rs.wasNull() && gsmrServerFkey > 0) {
            setProperty(P_GSMRServer, new OAObjectKey(gsmrServerFkey));
        }
        int serverFkey = rs.getInt(8);
        if (!rs.wasNull() && serverFkey > 0) {
            setProperty(P_Server, new OAObjectKey(serverFkey));
        }
        int serverStatusFkey = rs.getInt(9);
        if (!rs.wasNull() && serverStatusFkey > 0) {
            setProperty(P_ServerStatus, new OAObjectKey(serverStatusFkey));
        }
        if (rs.getMetaData().getColumnCount() != 9) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 