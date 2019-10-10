// Generated by OABuilder
package test.hifive.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.annotation.*;
import com.viaoa.util.OADate;

import test.hifive.model.oa.filter.*;
import test.hifive.model.oa.propertypath.*;
 
@OAClass(
    shortName = "iv",
    displayName = "Item Vendor",
    isLookup = true,
    isPreSelect = true,
    displayProperty = "name"
)
@OATable(
)
public class ItemVendor extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_Name = "Name";
    public static final String P_Name = "Name";
    public static final String PROPERTY_Notes = "Notes";
    public static final String P_Notes = "Notes";
     
     
    public static final String PROPERTY_Items = "Items";
    public static final String P_Items = "Items";
     
    protected int id;
    protected OADate created;
    protected String name;
    protected String notes;
     
    // Links to other objects.
     
    public ItemVendor() {
        if (!isLoading()) {
            setCreated(new OADate());
        }
    }
     
    public ItemVendor(int id) {
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
    @OAProperty(defaultValue = "new OADate()", displayLength = 8, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getCreated() {
        return created;
    }
    
    public void setCreated(OADate newValue) {
        fireBeforePropertyChange(P_Created, this.created, newValue);
        OADate old = created;
        this.created = newValue;
        firePropertyChange(P_Created, old, this.created);
    }
    @OAProperty(maxLength = 75, displayLength = 25)
    @OAColumn(maxLength = 75)
    public String getName() {
        return name;
    }
    
    public void setName(String newValue) {
        fireBeforePropertyChange(P_Name, this.name, newValue);
        String old = name;
        this.name = newValue;
        firePropertyChange(P_Name, old, this.name);
    }
    @OAProperty(maxLength = 5, displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.CLOB)
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String newValue) {
        fireBeforePropertyChange(P_Notes, this.notes, newValue);
        String old = notes;
        this.notes = newValue;
        firePropertyChange(P_Notes, old, this.notes);
    }
    @OAMany(
        toClass = Item.class, 
        reverseName = Item.P_ItemVendor, 
        createMethod = false
    )
    private Hub<Item> getItems() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Date date;
        date = rs.getDate(2);
        if (date != null) this.created = new OADate(date);
        this.name = rs.getString(3);
        this.notes = rs.getString(4);
        if (rs.getMetaData().getColumnCount() != 4) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 