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
    shortName = "stcv",
    displayName = "Server Type Client Version"
)
@OATable(
    indexes = {
        @OAIndex(name = "ServerTypeClientVersionServerTypeVersion", columns = { @OAIndexColumn(name = "ServerTypeVersionId") })
    }
)
public class ServerTypeClientVersion extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
     
     
    public static final String PROPERTY_ClientServerTypeVersion = "ClientServerTypeVersion";
    public static final String P_ClientServerTypeVersion = "ClientServerTypeVersion";
    public static final String PROPERTY_ServerTypeVersion = "ServerTypeVersion";
    public static final String P_ServerTypeVersion = "ServerTypeVersion";
     
    protected int id;
     
    // Links to other objects.
    protected transient ServerTypeVersion clientServerTypeVersion;
    protected transient ServerTypeVersion serverTypeVersion;
     
    public ServerTypeClientVersion() {
    }
     
    public ServerTypeClientVersion(int id) {
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
    @OAOne(
        displayName = "Server Type Version", 
        reverseName = ServerTypeVersion.P_ServerTypeClientVersions2, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"ClientServerTypeVersionId"})
    public ServerTypeVersion getClientServerTypeVersion() {
        if (clientServerTypeVersion == null) {
            clientServerTypeVersion = (ServerTypeVersion) getObject(P_ClientServerTypeVersion);
        }
        return clientServerTypeVersion;
    }
    
    public void setClientServerTypeVersion(ServerTypeVersion newValue) {
        fireBeforePropertyChange(P_ClientServerTypeVersion, this.clientServerTypeVersion, newValue);
        ServerTypeVersion old = this.clientServerTypeVersion;
        this.clientServerTypeVersion = newValue;
        firePropertyChange(P_ClientServerTypeVersion, old, this.clientServerTypeVersion);
    }
    
    @OAOne(
        displayName = "Server Type Version", 
        reverseName = ServerTypeVersion.P_ServerTypeClientVersions, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"ServerTypeVersionId"})
    public ServerTypeVersion getServerTypeVersion() {
        if (serverTypeVersion == null) {
            serverTypeVersion = (ServerTypeVersion) getObject(P_ServerTypeVersion);
        }
        return serverTypeVersion;
    }
    
    public void setServerTypeVersion(ServerTypeVersion newValue) {
        fireBeforePropertyChange(P_ServerTypeVersion, this.serverTypeVersion, newValue);
        ServerTypeVersion old = this.serverTypeVersion;
        this.serverTypeVersion = newValue;
        firePropertyChange(P_ServerTypeVersion, old, this.serverTypeVersion);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        int clientServerTypeVersionFkey = rs.getInt(2);
        if (!rs.wasNull() && clientServerTypeVersionFkey > 0) {
            setProperty(P_ClientServerTypeVersion, new OAObjectKey(clientServerTypeVersionFkey));
        }
        int serverTypeVersionFkey = rs.getInt(3);
        if (!rs.wasNull() && serverTypeVersionFkey > 0) {
            setProperty(P_ServerTypeVersion, new OAObjectKey(serverTypeVersionFkey));
        }
        if (rs.getMetaData().getColumnCount() != 3) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 