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
    shortName = "av",
    displayName = "Application Version",
    displayProperty = "packageVersion"
)
@OATable(
    indexes = {
        @OAIndex(name = "ApplicationVersionApplication", columns = { @OAIndexColumn(name = "ApplicationId") })
    }
)
public class ApplicationVersion extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
     
     
    public static final String PROPERTY_Application = "Application";
    public static final String P_Application = "Application";
    public static final String PROPERTY_PackageType = "PackageType";
    public static final String P_PackageType = "PackageType";
    public static final String PROPERTY_PackageVersion = "PackageVersion";
    public static final String P_PackageVersion = "PackageVersion";
     
    protected int id;
     
    // Links to other objects.
    protected transient Application application;
    protected transient PackageType packageType;
    protected transient PackageVersion packageVersion;
     
    public ApplicationVersion() {
    }
     
    public ApplicationVersion(int id) {
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
        reverseName = Application.P_ApplicationVersions, 
        required = true, 
        allowCreateNew = false
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
        displayName = "Package Type", 
        reverseName = PackageType.P_ApplicationVersions, 
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
    
    @OAOne(
        displayName = "Package Version", 
        reverseName = PackageVersion.P_ApplicationVersions
    )
    @OAFkey(columns = {"PackageVersionId"})
    public PackageVersion getPackageVersion() {
        if (packageVersion == null) {
            packageVersion = (PackageVersion) getObject(P_PackageVersion);
        }
        return packageVersion;
    }
    
    public void setPackageVersion(PackageVersion newValue) {
        fireBeforePropertyChange(P_PackageVersion, this.packageVersion, newValue);
        PackageVersion old = this.packageVersion;
        this.packageVersion = newValue;
        firePropertyChange(P_PackageVersion, old, this.packageVersion);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        int applicationFkey = rs.getInt(2);
        if (!rs.wasNull() && applicationFkey > 0) {
            setProperty(P_Application, new OAObjectKey(applicationFkey));
        }
        int packageTypeFkey = rs.getInt(3);
        if (!rs.wasNull() && packageTypeFkey > 0) {
            setProperty(P_PackageType, new OAObjectKey(packageTypeFkey));
        }
        int packageVersionFkey = rs.getInt(4);
        if (!rs.wasNull() && packageVersionFkey > 0) {
            setProperty(P_PackageVersion, new OAObjectKey(packageVersionFkey));
        }
        if (rs.getMetaData().getColumnCount() != 4) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
