// Generated by OABuilder
package test.xice.tsac.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.xice.tsac.delegate.ModelDelegate;
import test.xice.tsac.delegate.oa.*;
import test.xice.tsac.model.oa.filter.*;
import test.xice.tsac.model.oa.propertypath.*;

import com.viaoa.annotation.*;
 
@OAClass(
    shortName = "mrads",
    displayName = "MRAD Server",
    displayProperty = "application.server"
)
@OATable(
    indexes = {
        @OAIndex(name = "MRADServerEnvironment", columns = { @OAIndexColumn(name = "EnvironmentId") })
    }
)
public class MRADServer extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
     
     
    public static final String PROPERTY_Application = "Application";
    public static final String P_Application = "Application";
    public static final String PROPERTY_Environment = "Environment";
    public static final String P_Environment = "Environment";
    public static final String PROPERTY_MRADClients = "MRADClients";
    public static final String P_MRADClients = "MRADClients";
    public static final String PROPERTY_MRADServerCommands = "MRADServerCommands";
    public static final String P_MRADServerCommands = "MRADServerCommands";
    public static final String PROPERTY_RemoteClient = "RemoteClient";
    public static final String P_RemoteClient = "RemoteClient";
     
    protected int id;
     
    // Links to other objects.
    protected transient Application application;
    protected transient Environment environment;
    protected transient Hub<MRADClient> hubMRADClients;
    protected transient Hub<MRADServerCommand> hubMRADServerCommands;
    protected transient RemoteClient remoteClient;
     
    public MRADServer() {
    }
     
    public MRADServer(int id) {
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
        reverseName = Application.P_MRADServer
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
        reverseName = Environment.P_MRADServer, 
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
    
    @OAMany(
        displayName = "MRAD Clients", 
        toClass = MRADClient.class, 
        owner = true, 
        reverseName = MRADClient.P_MRADServer, 
        cascadeSave = true, 
        cascadeDelete = true, 
        matchHub = (MRADServer.P_Environment+"."+Environment.P_Silos+"."+Silo.P_Servers+"."+Server.P_Applications), 
        matchProperty = MRADClient.P_Application
    )
    public Hub<MRADClient> getMRADClients() {
        if (hubMRADClients == null) {
            hubMRADClients = (Hub<MRADClient>) getHub(P_MRADClients);
        }
        return hubMRADClients;
    }
    
    @OAMany(
        displayName = "MRAD Server Commands", 
        toClass = MRADServerCommand.class, 
        owner = true, 
        reverseName = MRADServerCommand.P_MRADServer, 
        cascadeSave = true, 
        cascadeDelete = true
    )
    public Hub<MRADServerCommand> getMRADServerCommands() {
        if (hubMRADServerCommands == null) {
            hubMRADServerCommands = (Hub<MRADServerCommand>) getHub(P_MRADServerCommands);
        }
        return hubMRADServerCommands;
    }
    
    @OAOne(
        displayName = "Remote Client", 
        isCalculated = true, 
        reverseName = RemoteClient.P_MRADServers, 
        allowCreateNew = false
    )
    public RemoteClient getRemoteClient() {
        if (remoteClient == null) {
            remoteClient = (RemoteClient) getObject(P_RemoteClient);
            if (remoteClient == null) {
                remoteClient = RemoteClientDelegate.getRemoteClient(RemoteClient.TYPE_MRAD);
            }
        }
        return remoteClient;
    }
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        int applicationFkey = rs.getInt(2);
        if (!rs.wasNull() && applicationFkey > 0) {
            setProperty(P_Application, new OAObjectKey(applicationFkey));
        }
        int environmentFkey = rs.getInt(3);
        if (!rs.wasNull() && environmentFkey > 0) {
            setProperty(P_Environment, new OAObjectKey(environmentFkey));
        }
        if (rs.getMetaData().getColumnCount() != 3) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 