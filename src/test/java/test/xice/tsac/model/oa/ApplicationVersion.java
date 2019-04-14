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
    shortName = "av",
    displayName = "Application Version",
    displayProperty = "currentPackageVersion"
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
    public static final String PROPERTY_CurrentVersion = "CurrentVersion";
    public static final String P_CurrentVersion = "CurrentVersion";
     
     
    public static final String PROPERTY_Application = "Application";
    public static final String P_Application = "Application";
    public static final String PROPERTY_CurrentPackageVersion = "CurrentPackageVersion";
    public static final String P_CurrentPackageVersion = "CurrentPackageVersion";
    public static final String PROPERTY_NewPackageVersion = "NewPackageVersion";
    public static final String P_NewPackageVersion = "NewPackageVersion";
    public static final String PROPERTY_PackageType = "PackageType";
    public static final String P_PackageType = "PackageType";
    public static final String PROPERTY_RCDeployDetails = "RCDeployDetails";
    public static final String P_RCDeployDetails = "RCDeployDetails";
     
    protected int id;
    protected String currentVersion;
     
    // Links to other objects.
    protected transient Application application;
    protected transient PackageVersion currentPackageVersion;
    protected transient PackageVersion newPackageVersion;
    protected transient PackageType packageType;
     
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
    @OAProperty(displayName = "Current Version", maxLength = 55, displayLength = 15, columnLength = 12)
    @OAColumn(maxLength = 55)
    public String getCurrentVersion() {
        return currentVersion;
    }
    
    public void setCurrentVersion(String newValue) {
        fireBeforePropertyChange(P_CurrentVersion, this.currentVersion, newValue);
        String old = currentVersion;
        this.currentVersion = newValue;
        firePropertyChange(P_CurrentVersion, old, this.currentVersion);
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
        displayName = "Current Package Version", 
        reverseName = PackageVersion.P_CurrentApplicationVersions
    )
    @OAFkey(columns = {"CurrentPackageVersionId"})
    public PackageVersion getCurrentPackageVersion() {
        if (currentPackageVersion == null) {
            currentPackageVersion = (PackageVersion) getObject(P_CurrentPackageVersion);
        }
        return currentPackageVersion;
    }
    
    public void setCurrentPackageVersion(PackageVersion newValue) {
        fireBeforePropertyChange(P_CurrentPackageVersion, this.currentPackageVersion, newValue);
        PackageVersion old = this.currentPackageVersion;
        this.currentPackageVersion = newValue;
        firePropertyChange(P_CurrentPackageVersion, old, this.currentPackageVersion);
    }
    
    @OAOne(
        displayName = "New Package Version", 
        reverseName = PackageVersion.P_NepApplicationVersions
    )
    @OAFkey(columns = {"NewPackageVersionId"})
    public PackageVersion getNewPackageVersion() {
        if (newPackageVersion == null) {
            newPackageVersion = (PackageVersion) getObject(P_NewPackageVersion);
        }
        return newPackageVersion;
    }
    
    public void setNewPackageVersion(PackageVersion newValue) {
        fireBeforePropertyChange(P_NewPackageVersion, this.newPackageVersion, newValue);
        PackageVersion old = this.newPackageVersion;
        this.newPackageVersion = newValue;
        firePropertyChange(P_NewPackageVersion, old, this.newPackageVersion);
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
    
    @OAMany(
        displayName = "RC Deploy Details", 
        toClass = RCDeployDetail.class, 
        reverseName = RCDeployDetail.P_ApplicationVersion, 
        createMethod = false
    )
    private Hub<RCDeployDetail> getRCDeployDetails() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.currentVersion = rs.getString(2);
        int applicationFkey = rs.getInt(3);
        if (!rs.wasNull() && applicationFkey > 0) {
            setProperty(P_Application, new OAObjectKey(applicationFkey));
        }
        int currentPackageVersionFkey = rs.getInt(4);
        if (!rs.wasNull() && currentPackageVersionFkey > 0) {
            setProperty(P_CurrentPackageVersion, new OAObjectKey(currentPackageVersionFkey));
        }
        int newPackageVersionFkey = rs.getInt(5);
        if (!rs.wasNull() && newPackageVersionFkey > 0) {
            setProperty(P_NewPackageVersion, new OAObjectKey(newPackageVersionFkey));
        }
        int packageTypeFkey = rs.getInt(6);
        if (!rs.wasNull() && packageTypeFkey > 0) {
            setProperty(P_PackageType, new OAObjectKey(packageTypeFkey));
        }
        if (rs.getMetaData().getColumnCount() != 6) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
