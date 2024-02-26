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
    shortName = "pt",
    displayName = "Package Type",
    isLookup = true,
    isPreSelect = true,
    displayProperty = "code"
)
@OATable(
)
public class PackageType extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Code = "Code";
    public static final String P_Code = "Code";
    public static final String PROPERTY_PackageName = "PackageName";
    public static final String P_PackageName = "PackageName";
    public static final String PROPERTY_PomGroupId = "PomGroupId";
    public static final String P_PomGroupId = "PomGroupId";
    public static final String PROPERTY_PomArtifactId = "PomArtifactId";
    public static final String P_PomArtifactId = "PomArtifactId";
    public static final String PROPERTY_RepoDirectory = "RepoDirectory";
    public static final String P_RepoDirectory = "RepoDirectory";
     
     
    public static final String PROPERTY_ApplicationTypes = "ApplicationTypes";
    public static final String P_ApplicationTypes = "ApplicationTypes";
    public static final String PROPERTY_ApplicationVersions = "ApplicationVersions";
    public static final String P_ApplicationVersions = "ApplicationVersions";
    public static final String PROPERTY_InstallVersions = "InstallVersions";
    public static final String P_InstallVersions = "InstallVersions";
    public static final String PROPERTY_PackageVersions = "PackageVersions";
    public static final String P_PackageVersions = "PackageVersions";
    public static final String PROPERTY_RCPackageListDetails = "RCPackageListDetails";
    public static final String P_RCPackageListDetails = "RCPackageListDetails";
    public static final String PROPERTY_RCRepoVersionDetails = "RCRepoVersionDetails";
    public static final String P_RCRepoVersionDetails = "RCRepoVersionDetails";
    public static final String PROPERTY_SiloConfigVersioins = "SiloConfigVersioins";
    public static final String P_SiloConfigVersioins = "SiloConfigVersioins";
     
    protected int id;
    protected String code;
    protected String packageName;
    protected String pomGroupId;
    protected String pomArtifactId;
    protected String repoDirectory;
     
    // Links to other objects.
    protected transient Hub<ApplicationType> hubApplicationTypes;
    protected transient Hub<PackageVersion> hubPackageVersions;
     
    public PackageType() {
    }
     
    public PackageType(int id) {
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
    @OAProperty(maxLength = 14, isUnique = true, displayLength = 8)
    @OAColumn(maxLength = 14)
    public String getCode() {
        return code;
    }
    
    public void setCode(String newValue) {
        fireBeforePropertyChange(P_Code, this.code, newValue);
        String old = code;
        this.code = newValue;
        firePropertyChange(P_Code, old, this.code);
    }
    @OAProperty(displayName = "Package Name", description = "solaris package name, ex: ICEgsmr", maxLength = 55, isUnique = true, displayLength = 40, columnLength = 12, isProcessed = true)
    @OAColumn(maxLength = 55)
    /**
      solaris package name, ex: ICEgsmr
    */
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String newValue) {
        fireBeforePropertyChange(P_PackageName, this.packageName, newValue);
        String old = packageName;
        this.packageName = newValue;
        firePropertyChange(P_PackageName, old, this.packageName);
    }
    @OAProperty(displayName = "Pom Group Id", maxLength = 55, displayLength = 40, columnLength = 12)
    @OAColumn(maxLength = 55)
    public String getPomGroupId() {
        return pomGroupId;
    }
    
    public void setPomGroupId(String newValue) {
        fireBeforePropertyChange(P_PomGroupId, this.pomGroupId, newValue);
        String old = pomGroupId;
        this.pomGroupId = newValue;
        firePropertyChange(P_PomGroupId, old, this.pomGroupId);
    }
    @OAProperty(displayName = "Pom Artifact Id", maxLength = 25, displayLength = 14, columnLength = 8)
    @OAColumn(maxLength = 25)
    public String getPomArtifactId() {
        return pomArtifactId;
    }
    
    public void setPomArtifactId(String newValue) {
        fireBeforePropertyChange(P_PomArtifactId, this.pomArtifactId, newValue);
        String old = pomArtifactId;
        this.pomArtifactId = newValue;
        firePropertyChange(P_PomArtifactId, old, this.pomArtifactId);
    }
    @OAProperty(displayName = "Repo Directory", maxLength = 75, displayLength = 22, columnLength = 18)
    @OAColumn(maxLength = 75)
    public String getRepoDirectory() {
        return repoDirectory;
    }
    
    public void setRepoDirectory(String newValue) {
        fireBeforePropertyChange(P_RepoDirectory, this.repoDirectory, newValue);
        String old = repoDirectory;
        this.repoDirectory = newValue;
        firePropertyChange(P_RepoDirectory, old, this.repoDirectory);
    }
    @OAMany(
        displayName = "Application Types", 
        toClass = ApplicationType.class, 
        reverseName = ApplicationType.P_PackageTypes
    )
    @OALinkTable(name = "ApplicationTypePackageType", indexName = "ApplicationTypePackageType", columns = {"PackageTypeId"})
    public Hub<ApplicationType> getApplicationTypes() {
        if (hubApplicationTypes == null) {
            hubApplicationTypes = (Hub<ApplicationType>) getHub(P_ApplicationTypes);
        }
        return hubApplicationTypes;
    }
    
    @OAMany(
        displayName = "Application Versions", 
        toClass = ApplicationVersion.class, 
        reverseName = ApplicationVersion.P_PackageType, 
        createMethod = false
    )
    private Hub<ApplicationVersion> getApplicationVersions() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "Install Versions", 
        toClass = InstallVersion.class, 
        reverseName = InstallVersion.P_PackageType, 
        createMethod = false
    )
    private Hub<InstallVersion> getInstallVersions() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "Package Versions", 
        toClass = PackageVersion.class, 
        owner = true, 
        reverseName = PackageVersion.P_PackageType, 
        cascadeSave = true, 
        cascadeDelete = true
    )
    public Hub<PackageVersion> getPackageVersions() {
        if (hubPackageVersions == null) {
            hubPackageVersions = (Hub<PackageVersion>) getHub(P_PackageVersions);
        }
        return hubPackageVersions;
    }
    
    @OAMany(
        displayName = "RCPackage List Details", 
        toClass = RCPackageListDetail.class, 
        reverseName = RCPackageListDetail.P_PackageType, 
        createMethod = false
    )
    private Hub<RCPackageListDetail> getRCPackageListDetails() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "RCRepo Version Details", 
        toClass = RCRepoVersionDetail.class, 
        reverseName = RCRepoVersionDetail.P_PackageType, 
        createMethod = false
    )
    private Hub<RCRepoVersionDetail> getRCRepoVersionDetails() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        displayName = "Silo Config Versioins", 
        toClass = SiloConfigVersioin.class, 
        reverseName = SiloConfigVersioin.P_PackageType, 
        createMethod = false
    )
    private Hub<SiloConfigVersioin> getSiloConfigVersioins() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.code = rs.getString(2);
        this.packageName = rs.getString(3);
        this.pomGroupId = rs.getString(4);
        this.pomArtifactId = rs.getString(5);
        this.repoDirectory = rs.getString(6);
        if (rs.getMetaData().getColumnCount() != 6) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 