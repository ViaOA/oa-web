// Generated by OABuilder
package test.xice.tsam.model.oa;
 
import java.util.logging.*;
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.xice.tsam.delegate.oa.*;
import test.xice.tsam.model.oa.filter.*;
import test.xice.tsam.model.oa.propertypath.*;

import test.xice.tsam.model.oa.Environment;
import test.xice.tsam.model.oa.Site;
import test.xice.tsam.model.oa.Timezone;
import com.viaoa.annotation.*;
 
@OAClass(
    shortName = "sit",
    displayName = "Site",
    isLookup = true,
    isPreSelect = true,
    displayProperty = "name"
)
@OATable(
    indexes = {
        @OAIndex(name = "SiteTimezone", columns = { @OAIndexColumn(name = "TimezoneId") })
    }
)
public class Site extends OAObject {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(Site.class.getName());
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_AbbrevName = "AbbrevName";
    public static final String P_AbbrevName = "AbbrevName";
    public static final String PROPERTY_Name = "Name";
    public static final String P_Name = "Name";
    public static final String PROPERTY_Production = "Production";
    public static final String P_Production = "Production";
     
     
    public static final String PROPERTY_Environments = "Environments";
    public static final String P_Environments = "Environments";
    public static final String PROPERTY_Timezone = "Timezone";
    public static final String P_Timezone = "Timezone";
     
    protected int id;
    protected String abbrevName;
    protected volatile String name;
    protected boolean production;

    // Links to other objects.
    protected transient Hub<Environment> hubEnvironments;
    protected transient Timezone timezone;
     
    public Site() {
    }
     
    public Site(int id) {
        this();
        setId(id);
    }
     
    @OAProperty(isUnique = true, displayLength = 3, isProcessed = true)
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
    
    @OAProperty(displayName = "Abbrev Name", maxLength = 6, displayLength = 6, columnLength = 5)
    @OAColumn(maxLength = 6)
    public String getAbbrevName() {
        return abbrevName;
    }
    public void setAbbrevName(String newValue) {
        fireBeforePropertyChange(P_AbbrevName, this.abbrevName, newValue);
        String old = abbrevName;
        this.abbrevName = newValue;
        firePropertyChange(P_AbbrevName, old, this.abbrevName);
    }
    
    @OAProperty(maxLength = 25, displayLength = 10, columnLength = 8)
    @OAColumn(maxLength = 25)
    public String getName() {
        return name;
    }
    public void setName(String newValue) {
        fireBeforePropertyChange(P_Name, this.name, newValue);
        String old = name;
        this.name = newValue;
        firePropertyChange(P_Name, old, this.name);
    }
    
    @OAProperty(displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getProduction() {
        return production;
    }
    public void setProduction(boolean newValue) {
        fireBeforePropertyChange(P_Production, this.production, newValue);
        boolean old = production;
        this.production = newValue;
        firePropertyChange(P_Production, old, this.production);
    }
    
    @OAMany(
        toClass = Environment.class, 
        owner = true, 
        reverseName = Environment.P_Site, 
        cascadeSave = true, 
        cascadeDelete = true
    )
    public Hub<Environment> getEnvironments() {
        if (hubEnvironments == null) {
            hubEnvironments = (Hub<Environment>) getHub(P_Environments);
        }
        return hubEnvironments;
    }
    
    @OAOne(
        reverseName = Timezone.P_Sites, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"TimezoneId"})
    public Timezone getTimezone() {
        if (timezone == null) {
            timezone = (Timezone) getObject(P_Timezone);
        }
        return timezone;
    }
    
    public void setTimezone(Timezone newValue) {
        fireBeforePropertyChange(P_Timezone, this.timezone, newValue);
        Timezone old = this.timezone;
        this.timezone = newValue;
        firePropertyChange(P_Timezone, old, this.timezone);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.abbrevName = rs.getString(2);
        this.name = rs.getString(3);
        this.production = rs.getBoolean(4);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, Site.P_Production, true);
        }
        int timezoneFkey = rs.getInt(5);
        if (!rs.wasNull() && timezoneFkey > 0) {
            setProperty(P_Timezone, new OAObjectKey(timezoneFkey));
        }
        if (rs.getMetaData().getColumnCount() != 5) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }


}
 