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
    shortName = "stv",
    displayName = "Server Type Version",
    displayProperty = "version"
)
@OATable(
    indexes = {
        @OAIndex(name = "ServerTypeVersionIdL", columns = { @OAIndexColumn(name = "IdLId") }), 
        @OAIndex(name = "ServerTypeVersionServerType", columns = { @OAIndexColumn(name = "ServerTypeId") })
    }
)
public class ServerTypeVersion extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_Version = "Version";
    public static final String P_Version = "Version";
    public static final String PROPERTY_ClientVersion = "ClientVersion";
    public static final String P_ClientVersion = "ClientVersion";
    public static final String PROPERTY_VerifiedInRepo = "VerifiedInRepo";
    public static final String P_VerifiedInRepo = "VerifiedInRepo";
    public static final String PROPERTY_DownloadedPom = "DownloadedPom";
    public static final String P_DownloadedPom = "DownloadedPom";
    public static final String PROPERTY_DownloadedZip = "DownloadedZip";
    public static final String P_DownloadedZip = "DownloadedZip";
     
     
    public static final String PROPERTY_EnvironmentServerTypes = "EnvironmentServerTypes";
    public static final String P_EnvironmentServerTypes = "EnvironmentServerTypes";
    public static final String PROPERTY_IDL = "IDL";
    public static final String P_IDL = "IDL";
    public static final String PROPERTY_RCInstalledVersionDetails = "RCInstalledVersionDetails";
    public static final String P_RCInstalledVersionDetails = "RCInstalledVersionDetails";
    public static final String PROPERTY_ServerInstalls = "ServerInstalls";
    public static final String P_ServerInstalls = "ServerInstalls";
    public static final String PROPERTY_Servers = "Servers";
    public static final String P_Servers = "Servers";
    public static final String PROPERTY_ServerType = "ServerType";
    public static final String P_ServerType = "ServerType";
    public static final String PROPERTY_ServerTypeClientVersions = "ServerTypeClientVersions";
    public static final String P_ServerTypeClientVersions = "ServerTypeClientVersions";
    public static final String PROPERTY_ServerTypeClientVersions2 = "ServerTypeClientVersions2";
    public static final String P_ServerTypeClientVersions2 = "ServerTypeClientVersions2";
    public static final String PROPERTY_SiloServerInfos = "SiloServerInfos";
    public static final String P_SiloServerInfos = "SiloServerInfos";
     
    protected int id;
    protected OADateTime created;
    protected String version;
    protected String clientVersion;
    protected boolean verifiedInRepo;
    protected boolean downloadedPom;
    protected boolean downloadedZip;
     
    // Links to other objects.
    protected transient IDL idL;
    protected transient ServerType serverType;
    protected transient Hub<ServerTypeClientVersion> hubServerTypeClientVersions;
     
    public ServerTypeVersion() {
        if (!isLoading()) {
            setCreated(new OADateTime());
        }
    }
     
    public ServerTypeVersion(int id) {
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
    @OAProperty(maxLength = 25, displayLength = 12, columnLength = 8)
    @OAColumn(maxLength = 25)
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String newValue) {
        fireBeforePropertyChange(P_Version, this.version, newValue);
        String old = version;
        this.version = newValue;
        firePropertyChange(P_Version, old, this.version);
    }
    @OAProperty(displayName = "Client Version", maxLength = 25, displayLength = 12, columnLength = 8)
    @OAColumn(name = "Version", maxLength = 25)
    public String getClientVersion() {
        return clientVersion;
    }
    
    public void setClientVersion(String newValue) {
        fireBeforePropertyChange(P_ClientVersion, this.clientVersion, newValue);
        String old = clientVersion;
        this.clientVersion = newValue;
        firePropertyChange(P_ClientVersion, old, this.clientVersion);
    }
    @OAProperty(displayName = "Verified In Repo", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    /**
      the POM for this was found and verified
    */
    public boolean getVerifiedInRepo() {
        return verifiedInRepo;
    }
    
    public void setVerifiedInRepo(boolean newValue) {
        fireBeforePropertyChange(P_VerifiedInRepo, this.verifiedInRepo, newValue);
        boolean old = verifiedInRepo;
        this.verifiedInRepo = newValue;
        firePropertyChange(P_VerifiedInRepo, old, this.verifiedInRepo);
    }
    @OAProperty(displayName = "Downloaded Pom", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getDownloadedPom() {
        return downloadedPom;
    }
    
    public void setDownloadedPom(boolean newValue) {
        fireBeforePropertyChange(P_DownloadedPom, this.downloadedPom, newValue);
        boolean old = downloadedPom;
        this.downloadedPom = newValue;
        firePropertyChange(P_DownloadedPom, old, this.downloadedPom);
    }
    @OAProperty(displayName = "Downloaded Zip", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getDownloadedZip() {
        return downloadedZip;
    }
    
    public void setDownloadedZip(boolean newValue) {
        fireBeforePropertyChange(P_DownloadedZip, this.downloadedZip, newValue);
        boolean old = downloadedZip;
        this.downloadedZip = newValue;
        firePropertyChange(P_DownloadedZip, old, this.downloadedZip);
    }
    @OAMany(
        displayName = "Environment Server Types", 
        toClass = EnvironmentServerType.class, 
        reverseName = EnvironmentServerType.P_ServerTypeVersion, 
        createMethod = false
    )
    private Hub<EnvironmentServerType> getEnvironmentServerTypes() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        reverseName = IDL.P_ServerTypeVersions, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"IdLId"})
    public IDL getIDL() {
        if (idL == null) {
            idL = (IDL) getObject(P_IDL);
        }
        return idL;
    }
    
    public void setIDL(IDL newValue) {
        fireBeforePropertyChange(P_IDL, this.idL, newValue);
        IDL old = this.idL;
        this.idL = newValue;
        firePropertyChange(P_IDL, old, this.idL);
    }
    
    @OAMany(
        displayName = "RCInstalled Version Details", 
        toClass = RCInstalledVersionDetail.class, 
        reverseName = RCInstalledVersionDetail.P_ServerTypeVersion, 
        createMethod = false
    )
    private Hub<RCInstalledVersionDetail> getRCInstalledVersionDetails() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "Server Installs", 
        toClass = ServerInstall.class, 
        reverseName = ServerInstall.P_NewServerTypeVersion, 
        createMethod = false
    )
    private Hub<ServerInstall> getServerInstalls() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        toClass = Server.class, 
        reverseName = Server.P_ServerTypeVersion, 
        createMethod = false
    )
    private Hub<Server> getServers() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        displayName = "Server Type", 
        reverseName = ServerType.P_ServerTypeVersions, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"ServerTypeId"})
    public ServerType getServerType() {
        if (serverType == null) {
            serverType = (ServerType) getObject(P_ServerType);
        }
        return serverType;
    }
    
    public void setServerType(ServerType newValue) {
        fireBeforePropertyChange(P_ServerType, this.serverType, newValue);
        ServerType old = this.serverType;
        this.serverType = newValue;
        firePropertyChange(P_ServerType, old, this.serverType);
    }
    
    @OAMany(
        displayName = "Server Type Client Versions", 
        toClass = ServerTypeClientVersion.class, 
        owner = true, 
        reverseName = ServerTypeClientVersion.P_ServerTypeVersion, 
        cascadeSave = true, 
        cascadeDelete = true, 
        uniqueProperty = ServerTypeClientVersion.P_ServerTypeVersion
    )
    public Hub<ServerTypeClientVersion> getServerTypeClientVersions() {
        if (hubServerTypeClientVersions == null) {
            hubServerTypeClientVersions = (Hub<ServerTypeClientVersion>) getHub(P_ServerTypeClientVersions);
        }
        return hubServerTypeClientVersions;
    }
    
    @OAMany(
        displayName = "Server Type Client Versions", 
        toClass = ServerTypeClientVersion.class, 
        reverseName = ServerTypeClientVersion.P_ClientServerTypeVersion, 
        createMethod = false
    )
    private Hub<ServerTypeClientVersion> getServerTypeClientVersions2() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "Silo Server Infos", 
        toClass = SiloServerInfo.class, 
        reverseName = SiloServerInfo.P_ServerTypeVersion, 
        createMethod = false
    )
    private Hub<SiloServerInfo> getSiloServerInfos() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Timestamp timestamp;
        timestamp = rs.getTimestamp(2);
        if (timestamp != null) this.created = new OADateTime(timestamp);
        this.version = rs.getString(3);
        this.clientVersion = rs.getString(4);
        this.verifiedInRepo = rs.getBoolean(5);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, ServerTypeVersion.P_VerifiedInRepo, true);
        }
        this.downloadedPom = rs.getBoolean(6);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, ServerTypeVersion.P_DownloadedPom, true);
        }
        this.downloadedZip = rs.getBoolean(7);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, ServerTypeVersion.P_DownloadedZip, true);
        }
        int idLFkey = rs.getInt(8);
        if (!rs.wasNull() && idLFkey > 0) {
            setProperty(P_IDL, new OAObjectKey(idLFkey));
        }
        int serverTypeFkey = rs.getInt(9);
        if (!rs.wasNull() && serverTypeFkey > 0) {
            setProperty(P_ServerType, new OAObjectKey(serverTypeFkey));
        }
        if (rs.getMetaData().getColumnCount() != 9) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
