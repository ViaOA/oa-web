// Generated by OABuilder
package test.xice.tsam.model.oa;
 
import java.util.logging.*;
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import test.xice.tsam.model.oa.ApplicationVersion;
import test.xice.tsam.model.oa.IDL;
import test.xice.tsam.model.oa.PackageType;
import test.xice.tsam.model.oa.PackageVersion;
import test.xice.tsam.model.oa.SiloConfigVersioin;
import com.viaoa.annotation.*;
import com.viaoa.util.OADateTime;

import test.xice.tsam.delegate.oa.*;
import test.xice.tsam.model.oa.filter.*;
import test.xice.tsam.model.oa.propertypath.*;

import com.viaoa.util.OADate;
 
@OAClass(
    shortName = "pv",
    displayName = "Package Version",
    displayProperty = "version"
)
@OATable(
    indexes = {
        @OAIndex(name = "PackageVersionPackageType", columns = { @OAIndexColumn(name = "PackageTypeId") })
    }
)
public class PackageVersion extends OAObject {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(PackageVersion.class.getName());
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_Version = "Version";
    public static final String P_Version = "Version";
    public static final String PROPERTY_BuildDate = "BuildDate";
    public static final String P_BuildDate = "BuildDate";
    public static final String PROPERTY_FileSize = "FileSize";
    public static final String P_FileSize = "FileSize";
    public static final String PROPERTY_FileName = "FileName";
    public static final String P_FileName = "FileName";
     
     
    public static final String PROPERTY_CurrentApplicationVersions = "CurrentApplicationVersions";
    public static final String P_CurrentApplicationVersions = "CurrentApplicationVersions";
    public static final String PROPERTY_IDL = "IDL";
    public static final String P_IDL = "IDL";
    public static final String PROPERTY_NepApplicationVersions = "NepApplicationVersions";
    public static final String P_NepApplicationVersions = "NepApplicationVersions";
    public static final String PROPERTY_PackageType = "PackageType";
    public static final String P_PackageType = "PackageType";
    public static final String PROPERTY_SiloConfigVersioins = "SiloConfigVersioins";
    public static final String P_SiloConfigVersioins = "SiloConfigVersioins";
     
    protected int id;
    protected OADateTime created;
    protected String version;
    protected OADate buildDate;
    protected int fileSize;
    protected String fileName;
     
    // Links to other objects.
    protected transient IDL idL;
    protected transient PackageType packageType;
     
    public PackageVersion() {
        if (!isLoading()) {
            setCreated(new OADateTime());
        }
    }
     
    public PackageVersion(int id) {
        this();
        setId(id);
    }
     
    @OAProperty(isUnique = true, displayLength = 3)
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
    
    @OAProperty(defaultValue = "new OADateTime()", displayLength = 12, isProcessed = true)
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
    
    @OAProperty(maxLength = 25, displayLength = 8, columnLength = 6)
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
    
    @OAProperty(displayName = "Build Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getBuildDate() {
        return buildDate;
    }
    public void setBuildDate(OADate newValue) {
        fireBeforePropertyChange(P_BuildDate, this.buildDate, newValue);
        OADate old = buildDate;
        this.buildDate = newValue;
        firePropertyChange(P_BuildDate, old, this.buildDate);
    }
    
    @OAProperty(displayName = "File Size", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getFileSize() {
        return fileSize;
    }
    public void setFileSize(int newValue) {
        fireBeforePropertyChange(P_FileSize, this.fileSize, newValue);
        int old = fileSize;
        this.fileSize = newValue;
        firePropertyChange(P_FileSize, old, this.fileSize);
    }
    
    @OAProperty(displayName = "File Name", maxLength = 55, displayLength = 14, columnLength = 10)
    @OAColumn(maxLength = 55)
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String newValue) {
        fireBeforePropertyChange(P_FileName, this.fileName, newValue);
        String old = fileName;
        this.fileName = newValue;
        firePropertyChange(P_FileName, old, this.fileName);
    }
    
    @OAMany(
        displayName = "Current Application Versions", 
        toClass = ApplicationVersion.class, 
        reverseName = ApplicationVersion.P_CurrentPackageVersion, 
        createMethod = false
    )
    private Hub<ApplicationVersion> getCurrentApplicationVersions() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        reverseName = IDL.P_PackageVersions, 
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
        displayName = "Nep Application Versions", 
        toClass = ApplicationVersion.class, 
        reverseName = ApplicationVersion.P_NewPackageVersion, 
        createMethod = false
    )
    private Hub<ApplicationVersion> getNepApplicationVersions() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        displayName = "Package Type", 
        reverseName = PackageType.P_PackageVersions, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"PackageTypeId"})
    public PackageType getPackageType() {
        if (packageType == null) {
            packageType = (PackageType) getObject(P_PackageType);
        }
        return packageType;
    }
    
    public void setPackageType(PackageType newValue) {
        fireBeforePropertyChange(P_PackageType, this.packageType, newValue);
        PackageType old = this.packageType;
        this.packageType = newValue;
        firePropertyChange(P_PackageType, old, this.packageType);
    }
    
    @OAMany(
        displayName = "Silo Config Versioins", 
        toClass = SiloConfigVersioin.class, 
        reverseName = SiloConfigVersioin.P_PackageVersion, 
        createMethod = false
    )
    private Hub<SiloConfigVersioin> getSiloConfigVersioins() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Timestamp timestamp;
        timestamp = rs.getTimestamp(2);
        if (timestamp != null) this.created = new OADateTime(timestamp);
        this.version = rs.getString(3);
        java.sql.Date date;
        date = rs.getDate(4);
        if (date != null) this.buildDate = new OADate(date);
        this.fileSize = (int) rs.getInt(5);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, PackageVersion.P_FileSize, true);
        }
        this.fileName = rs.getString(6);
        int idLFkey = rs.getInt(7);
        if (!rs.wasNull() && idLFkey > 0) {
            setProperty(P_IDL, new OAObjectKey(idLFkey));
        }
        int packageTypeFkey = rs.getInt(8);
        if (!rs.wasNull() && packageTypeFkey > 0) {
            setProperty(P_PackageType, new OAObjectKey(packageTypeFkey));
        }
        if (rs.getMetaData().getColumnCount() != 8) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
