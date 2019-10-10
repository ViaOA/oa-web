// Generated by OABuilder
package test.xice.tsac2.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.xice.tsac2.model.oa.filter.*;
import test.xice.tsac2.model.oa.propertypath.*;

import com.viaoa.annotation.*;
 
@OAClass(
    shortName = "as",
    displayName = "Admin Server",
    displayProperty = "application.server"
)
@OATable(
    indexes = {
        @OAIndex(name = "AdminServerSilo", columns = { @OAIndexColumn(name = "SiloId") })
    }
)
public class AdminServer extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
     
     
    public static final String PROPERTY_AdminClients = "AdminClients";
    public static final String P_AdminClients = "AdminClients";
    public static final String PROPERTY_Application = "Application";
    public static final String P_Application = "Application";
    public static final String PROPERTY_RemoteClient = "RemoteClient";
    public static final String P_RemoteClient = "RemoteClient";
    public static final String PROPERTY_Silo = "Silo";
    public static final String P_Silo = "Silo";
     
    protected int id;
     
    // Links to other objects.
    protected transient Hub<AdminClient> hubAdminClients;
    protected transient Application application;
    protected transient RemoteClient remoteClient;
    protected transient Silo silo;
     
    public AdminServer() {
    }
     
    public AdminServer(int id) {
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
    @OAMany(
        displayName = "Admin Clients", 
        toClass = AdminClient.class, 
        owner = true, 
        reverseName = AdminClient.P_AdminServer, 
        cascadeSave = true, 
        cascadeDelete = true
    )
    public Hub<AdminClient> getAdminClients() {
        if (hubAdminClients == null) {
            hubAdminClients = (Hub<AdminClient>) getHub(P_AdminClients);
        }
        return hubAdminClients;
    }
    
    @OAOne(
        reverseName = Application.P_AdminServer
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
    
    @OAOne(
        displayName = "Remote Client", 
        isCalculated = true, 
        reverseName = RemoteClient.P_AdminServers, 
        allowCreateNew = false
    )
    public RemoteClient getRemoteClient() {
        if (remoteClient == null) {
            remoteClient = (RemoteClient) getObject(P_RemoteClient);
            if (remoteClient == null) {
                //remoteClient = RemoteClientDelegate.getRemoteClient(RemoteClient.TYPE_AS);
            }
        }
        return remoteClient;
    }
    @OAOne(
        reverseName = Silo.P_AdminServers, 
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
        int applicationFkey = rs.getInt(2);
        if (!rs.wasNull() && applicationFkey > 0) {
            setProperty(P_Application, new OAObjectKey(applicationFkey));
        }
        int siloFkey = rs.getInt(3);
        if (!rs.wasNull() && siloFkey > 0) {
            setProperty(P_Silo, new OAObjectKey(siloFkey));
        }
        if (rs.getMetaData().getColumnCount() != 3) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 