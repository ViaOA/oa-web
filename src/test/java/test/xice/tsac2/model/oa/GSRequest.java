// Generated by OABuilder
package test.xice.tsac2.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.annotation.*;
import com.viaoa.util.OADateTime;

import test.xice.tsac2.model.oa.filter.*;
import test.xice.tsac2.model.oa.propertypath.*;
 
@OAClass(
    shortName = "gsr",
    displayName = "GSRequest",
    displayProperty = "requestMethod"
)
@OATable(
    indexes = {
        @OAIndex(name = "GSRequestMsgId", columns = {@OAIndexColumn(name = "MsgId")}),
        @OAIndex(name = "GSRequestGciConnection", columns = { @OAIndexColumn(name = "GciConnectionId") }), 
        @OAIndex(name = "GSRequestGSMRClient", columns = { @OAIndexColumn(name = "GSMRClientId") })
    }
)
public class GSRequest extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_Count = "Count";
    public static final String P_Count = "Count";
    public static final String PROPERTY_MsgId = "MsgId";
    public static final String P_MsgId = "MsgId";
    public static final String PROPERTY_LogMessage = "LogMessage";
    public static final String P_LogMessage = "LogMessage";
    public static final String PROPERTY_MsStart = "MsStart";
    public static final String P_MsStart = "MsStart";
    public static final String PROPERTY_NsStart = "NsStart";
    public static final String P_NsStart = "NsStart";
    public static final String PROPERTY_NsEnd = "NsEnd";
    public static final String P_NsEnd = "NsEnd";
    public static final String PROPERTY_NsGemstone = "NsGemstone";
    public static final String P_NsGemstone = "NsGemstone";
    public static final String PROPERTY_RequestSize = "RequestSize";
    public static final String P_RequestSize = "RequestSize";
    public static final String PROPERTY_UsedHeavy = "UsedHeavy";
    public static final String P_UsedHeavy = "UsedHeavy";
    public static final String PROPERTY_MethodDisabled = "MethodDisabled";
    public static final String P_MethodDisabled = "MethodDisabled";
    public static final String PROPERTY_GciAvailable = "GciAvailable";
    public static final String P_GciAvailable = "GciAvailable";
    public static final String PROPERTY_GciSuccess = "GciSuccess";
    public static final String P_GciSuccess = "GciSuccess";
    public static final String PROPERTY_ResponseSize = "ResponseSize";
    public static final String P_ResponseSize = "ResponseSize";
    public static final String PROPERTY_ResponseError = "ResponseError";
    public static final String P_ResponseError = "ResponseError";
     
    public static final String PROPERTY_StartTime = "StartTime";
    public static final String P_StartTime = "StartTime";
    public static final String PROPERTY_TotalTime = "TotalTime";
    public static final String P_TotalTime = "TotalTime";
     
    public static final String PROPERTY_GCIConnection = "GCIConnection";
    public static final String P_GCIConnection = "GCIConnection";
    public static final String PROPERTY_GSMRClient = "GSMRClient";
    public static final String P_GSMRClient = "GSMRClient";
    public static final String PROPERTY_GSMRWarning = "GSMRWarning";
    public static final String P_GSMRWarning = "GSMRWarning";
    public static final String PROPERTY_RequestMethod = "RequestMethod";
    public static final String P_RequestMethod = "RequestMethod";
     
    protected int id;
    protected OADateTime created;
    protected int count;
    protected long msgId;
    protected String logMessage;
    protected long msStart;
    protected long nsStart;
    protected long nsEnd;
    protected long nsGemstone;
    protected int requestSize;
    protected boolean usedHeavy;
    protected boolean methodDisabled;
    protected boolean gciAvailable;
    protected boolean gciSuccess;
    protected int responseSize;
    protected String responseError;
     
    // Links to other objects.
    protected transient GCIConnection gciConnection;
    protected transient GSMRClient gSMRClient;
    protected transient GSMRWarning gsmrWarning;
    protected transient RequestMethod requestMethod;
     
    public GSRequest() {
        if (!isLoading()) {
            setCreated(new OADateTime());
        }
    }
     
    public GSRequest(int id) {
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
    @OAProperty(displayLength = 8, columnLength = 5, isProcessed = true)
    @OAColumn(name = "CountValue", sqlType = java.sql.Types.INTEGER)
    public int getCount() {
        return count;
    }
    
    public void setCount(int newValue) {
        fireBeforePropertyChange(P_Count, this.count, newValue);
        int old = count;
        this.count = newValue;
        firePropertyChange(P_Count, old, this.count);
    }
    @OAProperty(displayName = "Msg Id", displayLength = 14, columnLength = 12, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public long getMsgId() {
        return msgId;
    }
    
    public void setMsgId(long newValue) {
        fireBeforePropertyChange(P_MsgId, this.msgId, newValue);
        long old = msgId;
        this.msgId = newValue;
        firePropertyChange(P_MsgId, old, this.msgId);
    }
    @OAProperty(displayName = "Log Message", maxLength = 400, displayLength = 40, columnLength = 25, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.CLOB)
    public String getLogMessage() {
        return logMessage;
    }
    
    public void setLogMessage(String newValue) {
        fireBeforePropertyChange(P_LogMessage, this.logMessage, newValue);
        String old = logMessage;
        this.logMessage = newValue;
        firePropertyChange(P_LogMessage, old, this.logMessage);
    }
    @OAProperty(displayName = "Ms Start", displayLength = 8, columnLength = 6, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public long getMsStart() {
        return msStart;
    }
    
    public void setMsStart(long newValue) {
        fireBeforePropertyChange(P_MsStart, this.msStart, newValue);
        long old = msStart;
        this.msStart = newValue;
        firePropertyChange(P_MsStart, old, this.msStart);
    }
    @OAProperty(displayName = "Ns Start", displayLength = 12, columnLength = 8, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public long getNsStart() {
        return nsStart;
    }
    
    public void setNsStart(long newValue) {
        fireBeforePropertyChange(P_NsStart, this.nsStart, newValue);
        long old = nsStart;
        this.nsStart = newValue;
        firePropertyChange(P_NsStart, old, this.nsStart);
    }
    @OAProperty(displayName = "Ns End", displayLength = 12, columnLength = 8, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public long getNsEnd() {
        return nsEnd;
    }
    
    public void setNsEnd(long newValue) {
        fireBeforePropertyChange(P_NsEnd, this.nsEnd, newValue);
        long old = nsEnd;
        this.nsEnd = newValue;
        firePropertyChange(P_NsEnd, old, this.nsEnd);
    }
    @OAProperty(displayName = "Ns Gemstone", displayLength = 8, columnLength = 6, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public long getNsGemstone() {
        return nsGemstone;
    }
    
    public void setNsGemstone(long newValue) {
        fireBeforePropertyChange(P_NsGemstone, this.nsGemstone, newValue);
        long old = nsGemstone;
        this.nsGemstone = newValue;
        firePropertyChange(P_NsGemstone, old, this.nsGemstone);
    }
    @OAProperty(displayName = "Request Size", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getRequestSize() {
        return requestSize;
    }
    
    public void setRequestSize(int newValue) {
        fireBeforePropertyChange(P_RequestSize, this.requestSize, newValue);
        int old = requestSize;
        this.requestSize = newValue;
        firePropertyChange(P_RequestSize, old, this.requestSize);
    }
    @OAProperty(displayName = "Used Heavy", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getUsedHeavy() {
        return usedHeavy;
    }
    
    public void setUsedHeavy(boolean newValue) {
        fireBeforePropertyChange(P_UsedHeavy, this.usedHeavy, newValue);
        boolean old = usedHeavy;
        this.usedHeavy = newValue;
        firePropertyChange(P_UsedHeavy, old, this.usedHeavy);
    }
    @OAProperty(displayName = "Method Disabled", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getMethodDisabled() {
        return methodDisabled;
    }
    
    public void setMethodDisabled(boolean newValue) {
        fireBeforePropertyChange(P_MethodDisabled, this.methodDisabled, newValue);
        boolean old = methodDisabled;
        this.methodDisabled = newValue;
        firePropertyChange(P_MethodDisabled, old, this.methodDisabled);
    }
    @OAProperty(displayName = "Gci Available", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getGciAvailable() {
        return gciAvailable;
    }
    
    public void setGciAvailable(boolean newValue) {
        fireBeforePropertyChange(P_GciAvailable, this.gciAvailable, newValue);
        boolean old = gciAvailable;
        this.gciAvailable = newValue;
        firePropertyChange(P_GciAvailable, old, this.gciAvailable);
    }
    @OAProperty(displayName = "Gci Success", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getGciSuccess() {
        return gciSuccess;
    }
    
    public void setGciSuccess(boolean newValue) {
        fireBeforePropertyChange(P_GciSuccess, this.gciSuccess, newValue);
        boolean old = gciSuccess;
        this.gciSuccess = newValue;
        firePropertyChange(P_GciSuccess, old, this.gciSuccess);
    }
    @OAProperty(displayName = "Response Size", displayLength = 5, columnLength = 12, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getResponseSize() {
        return responseSize;
    }
    
    public void setResponseSize(int newValue) {
        fireBeforePropertyChange(P_ResponseSize, this.responseSize, newValue);
        int old = responseSize;
        this.responseSize = newValue;
        firePropertyChange(P_ResponseSize, old, this.responseSize);
    }
    @OAProperty(displayName = "Response Error", maxLength = 13, displayLength = 30, isProcessed = true)
    @OAColumn(maxLength = 13)
    public String getResponseError() {
        return responseError;
    }
    
    public void setResponseError(String newValue) {
        fireBeforePropertyChange(P_ResponseError, this.responseError, newValue);
        String old = responseError;
        this.responseError = newValue;
        firePropertyChange(P_ResponseError, old, this.responseError);
    }
    @OACalculatedProperty(displayName = "Start Time", displayLength = 24, outputFormat = "yyyy/MM/dd HH:mm:ss.SSS", properties = {P_MsStart})
    public OADateTime getStartTime() {
        OADateTime dt = new OADateTime(this.getMsStart());
        return dt;
    }
    
     
    @OACalculatedProperty(displayName = "Total Time (ms)", description = "total time in miliseconds", displayLength = 6, columnLength = 8, properties = {P_NsStart, P_NsEnd})
    public int getTotalTime() {
        nsStart = this.getNsStart();
        nsEnd = this.getNsEnd();
        long ltot = (nsEnd - nsStart);
        int tot = (int) (ltot / Math.pow(10, 6));
    
        return tot;
    }
     
    @OAOne(
        reverseName = GCIConnection.P_GSRequests, 
        allowCreateNew = false
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
        reverseName = GSMRClient.P_GSRequests, 
        required = true, 
        allowCreateNew = false, 
        mustBeEmptyForDelete = true
    )
    @OAFkey(columns = {"GSMRClientId"})
    public GSMRClient getGSMRClient() {
        if (gSMRClient == null) {
            gSMRClient = (GSMRClient) getObject(P_GSMRClient);
        }
        return gSMRClient;
    }
    
    public void setGSMRClient(GSMRClient newValue) {
        fireBeforePropertyChange(P_GSMRClient, this.gSMRClient, newValue);
        GSMRClient old = this.gSMRClient;
        this.gSMRClient = newValue;
        firePropertyChange(P_GSMRClient, old, this.gSMRClient);
    }
    
    @OAOne(
        reverseName = GSMRWarning.P_GSRequest, 
        allowAddExisting = false
    )
    public GSMRWarning getGSMRWarning() {
        if (gsmrWarning == null) {
            gsmrWarning = (GSMRWarning) getObject(P_GSMRWarning);
        }
        return gsmrWarning;
    }
    
    public void setGSMRWarning(GSMRWarning newValue) {
        fireBeforePropertyChange(P_GSMRWarning, this.gsmrWarning, newValue);
        GSMRWarning old = this.gsmrWarning;
        this.gsmrWarning = newValue;
        firePropertyChange(P_GSMRWarning, old, this.gsmrWarning);
    }
    
    @OAOne(
        displayName = "Request Method", 
        reverseName = RequestMethod.P_GSRequests, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"RequestMethodId"})
    public RequestMethod getRequestMethod() {
        if (requestMethod == null) {
            requestMethod = (RequestMethod) getObject(P_RequestMethod);
        }
        return requestMethod;
    }
    
    public void setRequestMethod(RequestMethod newValue) {
        fireBeforePropertyChange(P_RequestMethod, this.requestMethod, newValue);
        RequestMethod old = this.requestMethod;
        this.requestMethod = newValue;
        firePropertyChange(P_RequestMethod, old, this.requestMethod);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Timestamp timestamp;
        timestamp = rs.getTimestamp(2);
        if (timestamp != null) this.created = new OADateTime(timestamp);
        this.count = (int) rs.getInt(3);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_Count, true);
        }
        this.msgId = (long) rs.getLong(4);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_MsgId, true);
        }
        this.logMessage = rs.getString(5);
        this.msStart = (long) rs.getLong(6);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_MsStart, true);
        }
        this.nsStart = (long) rs.getLong(7);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_NsStart, true);
        }
        this.nsEnd = (long) rs.getLong(8);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_NsEnd, true);
        }
        this.nsGemstone = (long) rs.getLong(9);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_NsGemstone, true);
        }
        this.requestSize = (int) rs.getInt(10);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_RequestSize, true);
        }
        this.usedHeavy = rs.getBoolean(11);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_UsedHeavy, true);
        }
        this.methodDisabled = rs.getBoolean(12);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_MethodDisabled, true);
        }
        this.gciAvailable = rs.getBoolean(13);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_GciAvailable, true);
        }
        this.gciSuccess = rs.getBoolean(14);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_GciSuccess, true);
        }
        this.responseSize = (int) rs.getInt(15);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, GSRequest.P_ResponseSize, true);
        }
        this.responseError = rs.getString(16);
        int gciConnectionFkey = rs.getInt(17);
        if (!rs.wasNull() && gciConnectionFkey > 0) {
            setProperty(P_GCIConnection, new OAObjectKey(gciConnectionFkey));
        }
        int gSMRClientFkey = rs.getInt(18);
        if (!rs.wasNull() && gSMRClientFkey > 0) {
            setProperty(P_GSMRClient, new OAObjectKey(gSMRClientFkey));
        }
        int requestMethodFkey = rs.getInt(19);
        if (!rs.wasNull() && requestMethodFkey > 0) {
            setProperty(P_RequestMethod, new OAObjectKey(requestMethodFkey));
        }
        if (rs.getMetaData().getColumnCount() != 19) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
