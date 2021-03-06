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
    shortName = "sil",
    displayName = "Silo",
    displayProperty = "siloType",
    rootTreePropertyPaths = {
        "[Site]."+Site.P_Environments+"."+Environment.P_Silos
    }
)
@OATable(
    indexes = {
        @OAIndex(name = "SiloEnvironment", columns = { @OAIndexColumn(name = "EnvironmentId") })
    }
)
public class Silo extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_NetworkMask = "NetworkMask";
    public static final String P_NetworkMask = "NetworkMask";
     
     
    public static final String PROPERTY_ApplicationGroups = "ApplicationGroups";
    public static final String P_ApplicationGroups = "ApplicationGroups";
    public static final String PROPERTY_Environment = "Environment";
    public static final String P_Environment = "Environment";
    public static final String PROPERTY_GSMRServers = "GSMRServers";
    public static final String P_GSMRServers = "GSMRServers";
    public static final String PROPERTY_LLADServers = "LLADServers";
    public static final String P_LLADServers = "LLADServers";
    public static final String PROPERTY_Servers = "Servers";
    public static final String P_Servers = "Servers";
    public static final String PROPERTY_SiloConfigs = "SiloConfigs";
    public static final String P_SiloConfigs = "SiloConfigs";
    public static final String PROPERTY_SiloType = "SiloType";
    public static final String P_SiloType = "SiloType";
     
    protected int id;
    protected String networkMask;
     
    // Links to other objects.
    protected transient Hub<ApplicationGroup> hubApplicationGroups;
    protected transient Environment environment;
    protected transient Hub<GSMRServer> hubGSMRServers;
    protected transient Hub<LLADServer> hubLLADServers;
    // protected transient Hub<Server> hubServers;
    protected transient Hub<SiloConfig> hubSiloConfigs;
    protected transient SiloType siloType;
     
    public Silo() {
    }
     
    public Silo(int id) {
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
    @OAProperty(displayName = "Network Mask", maxLength = 25, displayLength = 16, columnLength = 15)
    @OAColumn(maxLength = 25)
    public String getNetworkMask() {
        return networkMask;
    }
    
    public void setNetworkMask(String newValue) {
        fireBeforePropertyChange(P_NetworkMask, this.networkMask, newValue);
        String old = networkMask;
        this.networkMask = newValue;
        firePropertyChange(P_NetworkMask, old, this.networkMask);
    }
    @OAMany(
        displayName = "Application Groups", 
        toClass = ApplicationGroup.class, 
        owner = true, 
        reverseName = ApplicationGroup.P_Silo, 
        cascadeSave = true, 
        cascadeDelete = true, 
        seqProperty = ApplicationGroup.P_Seq, 
        sortProperty = ApplicationGroup.P_Seq
    )
    public Hub<ApplicationGroup> getApplicationGroups() {
        if (hubApplicationGroups == null) {
            hubApplicationGroups = (Hub<ApplicationGroup>) getHub(P_ApplicationGroups);
        }
        return hubApplicationGroups;
    }
    
    @OAOne(
        reverseName = Environment.P_Silos, 
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
        toClass = GSMRServer.class, 
        owner = true, 
        reverseName = GSMRServer.P_Silo, 
        cascadeSave = true, 
        cascadeDelete = true, 
        uniqueProperty = GSMRServer.P_Application
    )
    public Hub<GSMRServer> getGSMRServers() {
        if (hubGSMRServers == null) {
            hubGSMRServers = (Hub<GSMRServer>) getHub(P_GSMRServers);
        }
        return hubGSMRServers;
    }
    
    @OAMany(
        toClass = LLADServer.class, 
        owner = true, 
        reverseName = LLADServer.P_Silo, 
        cascadeSave = true, 
        cascadeDelete = true
    )
    public Hub<LLADServer> getLLADServers() {
        if (hubLLADServers == null) {
            hubLLADServers = (Hub<LLADServer>) getHub(P_LLADServers);
        }
        return hubLLADServers;
    }
    
    @OAMany(
        toClass = Server.class, 
        owner = true, 
        cacheSize = 25, 
        reverseName = Server.P_Silo, 
        cascadeSave = true, 
        cascadeDelete = true
    )
    public Hub<Server> getServers() {
        Hub<Server> hubServers;
        {
            hubServers = (Hub<Server>) getHub(P_Servers);
        }
        return hubServers;
    }
    
    @OAMany(
        displayName = "Silo Configs", 
        toClass = SiloConfig.class, 
        reverseName = SiloConfig.P_Silo, 
        matchHub = (Silo.P_SiloType+"."+SiloType.P_ApplicationTypes), 
        matchProperty = SiloConfig.P_ApplicationType
    )
    public Hub<SiloConfig> getSiloConfigs() {
        if (hubSiloConfigs == null) {
            hubSiloConfigs = (Hub<SiloConfig>) getHub(P_SiloConfigs);
        }
        return hubSiloConfigs;
    }
    
    @OAOne(
        displayName = "Silo Type", 
        reverseName = SiloType.P_Silos, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"SiloTypeId"})
    public SiloType getSiloType() {
        if (siloType == null) {
            siloType = (SiloType) getObject(P_SiloType);
        }
        return siloType;
    }
    
    public void setSiloType(SiloType newValue) {
        fireBeforePropertyChange(P_SiloType, this.siloType, newValue);
        SiloType old = this.siloType;
        this.siloType = newValue;
        firePropertyChange(P_SiloType, old, this.siloType);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.networkMask = rs.getString(2);
        int environmentFkey = rs.getInt(3);
        if (!rs.wasNull() && environmentFkey > 0) {
            setProperty(P_Environment, new OAObjectKey(environmentFkey));
        }
        int siloTypeFkey = rs.getInt(4);
        if (!rs.wasNull() && siloTypeFkey > 0) {
            setProperty(P_SiloType, new OAObjectKey(siloTypeFkey));
        }
        if (rs.getMetaData().getColumnCount() != 4) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
