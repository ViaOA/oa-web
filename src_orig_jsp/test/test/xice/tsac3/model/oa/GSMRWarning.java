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
    shortName = "gsmrw",
    displayName = "GSMRWarning"
)
@OATable(
    indexes = {
        @OAIndex(name = "GSMRWarningGciConnection", columns = { @OAIndexColumn(name = "GciConnectionId") }), 
        @OAIndex(name = "GSMRWarningGsmrServer", columns = { @OAIndexColumn(name = "GsmrServerId") }), 
        @OAIndex(name = "GSMRWarningGsRequest", columns = { @OAIndexColumn(name = "GsRequestId") })
    }
)
public class GSMRWarning extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
     
     
    public static final String PROPERTY_GCIConnection = "GCIConnection";
    public static final String P_GCIConnection = "GCIConnection";
    public static final String PROPERTY_GSMRServer = "GSMRServer";
    public static final String P_GSMRServer = "GSMRServer";
    public static final String PROPERTY_GSRequest = "GSRequest";
    public static final String P_GSRequest = "GSRequest";
    public static final String PROPERTY_Warning = "Warning";
    public static final String P_Warning = "Warning";
     
    protected int id;
     
    // Links to other objects.
    protected transient GCIConnection gciConnection;
    protected transient GSMRServer gsmrServer;
    protected transient GSRequest gsRequest;
    protected transient Warning warning;
     
    public GSMRWarning() {
    }
     
    public GSMRWarning(int id) {
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
    @OAOne(
        reverseName = GCIConnection.P_GSMRWarnings, 
        allowCreateNew = false, 
        allowAddExisting = false
    )
    @OAFkey(columns = {"GciConnectionId"})
    public GCIConnection getGCIConnection() {
        if (gciConnection == null) {
            gciConnection = (GCIConnection) getObject(P_GCIConnection);
        }
        return gciConnection;
    }
    
    public void setGCIConnection(GCIConnection newValue) {
        fireBeforePropertyChange(P_GCIConnection, this.gciConnection, newValue);
        GCIConnection old = this.gciConnection;
        this.gciConnection = newValue;
        firePropertyChange(P_GCIConnection, old, this.gciConnection);
    }
    
    @OAOne(
        reverseName = GSMRServer.P_GSMRWarnings, 
        allowCreateNew = false
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
    
    @OAOne(
        reverseName = GSRequest.P_GSMRWarning, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"GsRequestId"})
    public GSRequest getGSRequest() {
        if (gsRequest == null) {
            gsRequest = (GSRequest) getObject(P_GSRequest);
        }
        return gsRequest;
    }
    
    public void setGSRequest(GSRequest newValue) {
        fireBeforePropertyChange(P_GSRequest, this.gsRequest, newValue);
        GSRequest old = this.gsRequest;
        this.gsRequest = newValue;
        firePropertyChange(P_GSRequest, old, this.gsRequest);
    }
    
    @OAOne(
        reverseName = Warning.P_GSMRWarning, 
        required = true, 
        allowAddExisting = false
    )
    @OAFkey(columns = {"WarningId"})
    public Warning getWarning() {
        if (warning == null) {
            warning = (Warning) getObject(P_Warning);
        }
        return warning;
    }
    
    public void setWarning(Warning newValue) {
        fireBeforePropertyChange(P_Warning, this.warning, newValue);
        Warning old = this.warning;
        this.warning = newValue;
        firePropertyChange(P_Warning, old, this.warning);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        int gciConnectionFkey = rs.getInt(2);
        if (!rs.wasNull() && gciConnectionFkey > 0) {
            setProperty(P_GCIConnection, new OAObjectKey(gciConnectionFkey));
        }
        int gsmrServerFkey = rs.getInt(3);
        if (!rs.wasNull() && gsmrServerFkey > 0) {
            setProperty(P_GSMRServer, new OAObjectKey(gsmrServerFkey));
        }
        int gsRequestFkey = rs.getInt(4);
        if (!rs.wasNull() && gsRequestFkey > 0) {
            setProperty(P_GSRequest, new OAObjectKey(gsRequestFkey));
        }
        int warningFkey = rs.getInt(5);
        if (!rs.wasNull() && warningFkey > 0) {
            setProperty(P_Warning, new OAObjectKey(warningFkey));
        }
        if (rs.getMetaData().getColumnCount() != 5) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 