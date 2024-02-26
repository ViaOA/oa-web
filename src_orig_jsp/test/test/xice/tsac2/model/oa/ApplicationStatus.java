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

import java.awt.Color;
import com.viaoa.util.OAConverter;
 
@OAClass(
    shortName = "as",
    displayName = "Application Status",
    isLookup = true,
    isPreSelect = true,
    displayProperty = "name",
    sortProperty = "type"
)
@OATable(
)
public class ApplicationStatus extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_Name = "Name";
    public static final String P_Name = "Name";
    public static final String PROPERTY_Type = "Type";
    public static final String P_Type = "Type";
    public static final String PROPERTY_TypeAsString = "TypeAsString";
    public static final String P_TypeAsString = "TypeAsString";
    public static final String PROPERTY_Color = "Color";
    public static final String P_Color = "Color";
     
     
    public static final String PROPERTY_Applications = "Applications";
    public static final String P_Applications = "Applications";
     
    protected int id;
    protected OADateTime created;
    protected String name;
    protected int type;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_NOTCONNECTED = 1;
    public static final int TYPE_REQUESTCONNECT = 2;
    public static final int TYPE_CONNECTING = 3;
    public static final int TYPE_CONNECTED = 4;
    public static final int TYPE_CONNECTIONFAILED = 5;
    public static final int TYPE_DISCONNECTED = 6;
    public static final int TYPE_RUNNING = 7;
    public static final int TYPE_REQUESTSTOP = 8;
    public static final int TYPE_REQUESTPAUSE = 9;
    public static final int TYPE_PAUSED = 10;
    public static final int TYPE_REQUESTRESUME = 11;
    public static final Hub<String> hubType;
    static {
        hubType = new Hub<String>(String.class);
        hubType.addElement("Unknown");
        hubType.addElement("Not connected");
        hubType.addElement("Request connect");
        hubType.addElement("Connecting");
        hubType.addElement("Connected");
        hubType.addElement("Connection failed");
        hubType.addElement("Disconnected");
        hubType.addElement("Running");
        hubType.addElement("Request stop");
        hubType.addElement("Request pause");
        hubType.addElement("Paused");
        hubType.addElement("Request resume");
    }
    protected Color color;
     
    // Links to other objects.
     
    public ApplicationStatus() {
        if (!isLoading()) {
            setCreated(new OADateTime());
        }
    }
     
    public ApplicationStatus(int id) {
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
    @OAProperty(maxLength = 35, displayLength = 15, columnLength = 12)
    @OAColumn(maxLength = 35)
    public String getName() {
        return name;
    }
    
    public void setName(String newValue) {
        fireBeforePropertyChange(P_Name, this.name, newValue);
        String old = name;
        this.name = newValue;
        firePropertyChange(P_Name, old, this.name);
    }
    @OAProperty(displayLength = 20, isProcessed = true, isNameValue = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getType() {
        return type;
    }
    
    public void setType(int newValue) {
        fireBeforePropertyChange(P_Type, this.type, newValue);
        int old = type;
        this.type = newValue;
        firePropertyChange(P_Type, old, this.type);
    }
    public String getTypeAsString() {
        if (isNull(P_Type)) return "";
        String s = hubType.getAt(getType());
        if (s == null) s = "";
        return s;
    }
    @OAProperty(displayLength = 14, isProcessed = true)
    @OAColumn(maxLength = 16)
    public Color getColor() {
        if (color == null) {
            switch (getType()) {
            case ApplicationStatus.TYPE_CONNECTING: color = Color.orange; break;
            case ApplicationStatus.TYPE_CONNECTED: color = Color.yellow; break;
            case ApplicationStatus.TYPE_RUNNING: 
                color = Color.green; break;
    
            case ApplicationStatus.TYPE_REQUESTCONNECT: 
            case ApplicationStatus.TYPE_REQUESTPAUSE: 
            case ApplicationStatus.TYPE_REQUESTRESUME: 
            case ApplicationStatus.TYPE_REQUESTSTOP: 
                color = Color.cyan; break;
            
            
            default: color = Color.red; break;
            }
            setColor(color);
        }
        return color;
    }
    
    public void setColor(Color newValue) {
        fireBeforePropertyChange(PROPERTY_Color, this.color, newValue);
        Color old = color;
        this.color = newValue;
        firePropertyChange(PROPERTY_Color, old, this.color);
    }
    @OAMany(
        toClass = Application.class, 
        reverseName = Application.P_ApplicationStatus, 
        createMethod = false
    )
    private Hub<Application> getApplications() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Timestamp timestamp;
        timestamp = rs.getTimestamp(2);
        if (timestamp != null) this.created = new OADateTime(timestamp);
        this.name = rs.getString(3);
        this.type = (int) rs.getInt(4);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, ApplicationStatus.P_Type, true);
        }
        this.color = (Color) OAConverter.convert(Color.class, rs.getString(5));
        if (rs.getMetaData().getColumnCount() != 5) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 