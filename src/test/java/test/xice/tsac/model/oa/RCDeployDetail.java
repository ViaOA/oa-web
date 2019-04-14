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
    shortName = "rcdd",
    displayName = "RC Deploy Detail",
    displayProperty = "applicationVersion.application"
)
@OATable(
    indexes = {
        @OAIndex(name = "RCDeployDetailRcDeploy", columns = { @OAIndexColumn(name = "RcDeployId") }), 
        @OAIndex(name = "RCDeployDetailRcDownloadDetail", columns = { @OAIndexColumn(name = "RcDownloadDetailId") }), 
        @OAIndex(name = "RCDeployDetailRcInstallDetail", columns = { @OAIndexColumn(name = "RcInstallDetailId") }), 
        @OAIndex(name = "RCDeployDetailRcStageDetail", columns = { @OAIndexColumn(name = "RcStageDetailId") }), 
        @OAIndex(name = "RCDeployDetailRcStopDetail", columns = { @OAIndexColumn(name = "RcStopDetailId") }), 
        @OAIndex(name = "RCDeployDetailRcVerifyDetail", columns = { @OAIndexColumn(name = "RcVerifyDetailId") })
    }
)
public class RCDeployDetail extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Selected = "Selected";
    public static final String P_Selected = "Selected";
     
     
    public static final String PROPERTY_ApplicationVersion = "ApplicationVersion";
    public static final String P_ApplicationVersion = "ApplicationVersion";
    public static final String PROPERTY_RCDeploy = "RCDeploy";
    public static final String P_RCDeploy = "RCDeploy";
    public static final String PROPERTY_RCDownloadDetail = "RCDownloadDetail";
    public static final String P_RCDownloadDetail = "RCDownloadDetail";
    public static final String PROPERTY_RCInstallDetail = "RCInstallDetail";
    public static final String P_RCInstallDetail = "RCInstallDetail";
    public static final String PROPERTY_RCStageDetail = "RCStageDetail";
    public static final String P_RCStageDetail = "RCStageDetail";
    public static final String PROPERTY_RCStartDetail = "RCStartDetail";
    public static final String P_RCStartDetail = "RCStartDetail";
    public static final String PROPERTY_RCStopDetail = "RCStopDetail";
    public static final String P_RCStopDetail = "RCStopDetail";
    public static final String PROPERTY_RCVerifyDetail = "RCVerifyDetail";
    public static final String P_RCVerifyDetail = "RCVerifyDetail";
     
    protected int id;
    protected boolean selected;
     
    // Links to other objects.
    protected transient ApplicationVersion applicationVersion;
    protected transient RCDeploy rcDeploy;
    protected transient RCDownloadDetail rcDownloadDetail;
    protected transient RCInstallDetail rcInstallDetail;
    protected transient RCStageDetail rcStageDetail;
    protected transient RCStartDetail rcStartDetail;
    protected transient RCStopDetail rcStopDetail;
    protected transient RCVerifyDetail rcVerifyDetail;
     
    public RCDeployDetail() {
    }
     
    public RCDeployDetail(int id) {
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
    @OAProperty(displayLength = 5, columnLength = 9)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getSelected() {
        return selected;
    }
    
    public void setSelected(boolean newValue) {
        fireBeforePropertyChange(P_Selected, this.selected, newValue);
        boolean old = selected;
        this.selected = newValue;
        firePropertyChange(P_Selected, old, this.selected);
    }
    @OAOne(
        displayName = "Application Version", 
        reverseName = ApplicationVersion.P_RCDeployDetails
    )
    @OAFkey(columns = {"ApplicationVersionId"})
    public ApplicationVersion getApplicationVersion() {
        if (applicationVersion == null) {
            applicationVersion = (ApplicationVersion) getObject(P_ApplicationVersion);
        }
        return applicationVersion;
    }
    
    public void setApplicationVersion(ApplicationVersion newValue) {
        fireBeforePropertyChange(P_ApplicationVersion, this.applicationVersion, newValue);
        ApplicationVersion old = this.applicationVersion;
        this.applicationVersion = newValue;
        firePropertyChange(P_ApplicationVersion, old, this.applicationVersion);
    }
    
    @OAOne(
        displayName = "RC Deploy", 
        reverseName = RCDeploy.P_RCDeployDetails
    )
    @OAFkey(columns = {"RcDeployId"})
    public RCDeploy getRCDeploy() {
        if (rcDeploy == null) {
            rcDeploy = (RCDeploy) getObject(P_RCDeploy);
        }
        return rcDeploy;
    }
    
    public void setRCDeploy(RCDeploy newValue) {
        fireBeforePropertyChange(P_RCDeploy, this.rcDeploy, newValue);
        RCDeploy old = this.rcDeploy;
        this.rcDeploy = newValue;
        firePropertyChange(P_RCDeploy, old, this.rcDeploy);
    }
    
    @OAOne(
        displayName = "RC Download Detail", 
        reverseName = RCDownloadDetail.P_RCDeployDetails
    )
    @OAFkey(columns = {"RcDownloadDetailId"})
    public RCDownloadDetail getRCDownloadDetail() {
        if (rcDownloadDetail == null) {
            rcDownloadDetail = (RCDownloadDetail) getObject(P_RCDownloadDetail);
        }
        return rcDownloadDetail;
    }
    
    public void setRCDownloadDetail(RCDownloadDetail newValue) {
        fireBeforePropertyChange(P_RCDownloadDetail, this.rcDownloadDetail, newValue);
        RCDownloadDetail old = this.rcDownloadDetail;
        this.rcDownloadDetail = newValue;
        firePropertyChange(P_RCDownloadDetail, old, this.rcDownloadDetail);
    }
    
    @OAOne(
        displayName = "RC Install Detail", 
        reverseName = RCInstallDetail.P_RCDeployDetail
    )
    @OAFkey(columns = {"RcInstallDetailId"})
    public RCInstallDetail getRCInstallDetail() {
        if (rcInstallDetail == null) {
            rcInstallDetail = (RCInstallDetail) getObject(P_RCInstallDetail);
        }
        return rcInstallDetail;
    }
    
    public void setRCInstallDetail(RCInstallDetail newValue) {
        fireBeforePropertyChange(P_RCInstallDetail, this.rcInstallDetail, newValue);
        RCInstallDetail old = this.rcInstallDetail;
        this.rcInstallDetail = newValue;
        firePropertyChange(P_RCInstallDetail, old, this.rcInstallDetail);
    }
    
    @OAOne(
        displayName = "RC Stage Detail", 
        reverseName = RCStageDetail.P_RCDeployDetail
    )
    @OAFkey(columns = {"RcStageDetailId"})
    public RCStageDetail getRCStageDetail() {
        if (rcStageDetail == null) {
            rcStageDetail = (RCStageDetail) getObject(P_RCStageDetail);
        }
        return rcStageDetail;
    }
    
    public void setRCStageDetail(RCStageDetail newValue) {
        fireBeforePropertyChange(P_RCStageDetail, this.rcStageDetail, newValue);
        RCStageDetail old = this.rcStageDetail;
        this.rcStageDetail = newValue;
        firePropertyChange(P_RCStageDetail, old, this.rcStageDetail);
    }
    
    @OAOne(
        displayName = "RC Start Detail", 
        reverseName = RCStartDetail.P_RCDeployDetail
    )
    public RCStartDetail getRCStartDetail() {
        if (rcStartDetail == null) {
            rcStartDetail = (RCStartDetail) getObject(P_RCStartDetail);
        }
        return rcStartDetail;
    }
    
    public void setRCStartDetail(RCStartDetail newValue) {
        fireBeforePropertyChange(P_RCStartDetail, this.rcStartDetail, newValue);
        RCStartDetail old = this.rcStartDetail;
        this.rcStartDetail = newValue;
        firePropertyChange(P_RCStartDetail, old, this.rcStartDetail);
    }
    
    @OAOne(
        displayName = "RC Stop Detail", 
        reverseName = RCStopDetail.P_RCDeployDetail
    )
    @OAFkey(columns = {"RcStopDetailId"})
    public RCStopDetail getRCStopDetail() {
        if (rcStopDetail == null) {
            rcStopDetail = (RCStopDetail) getObject(P_RCStopDetail);
        }
        return rcStopDetail;
    }
    
    public void setRCStopDetail(RCStopDetail newValue) {
        fireBeforePropertyChange(P_RCStopDetail, this.rcStopDetail, newValue);
        RCStopDetail old = this.rcStopDetail;
        this.rcStopDetail = newValue;
        firePropertyChange(P_RCStopDetail, old, this.rcStopDetail);
    }
    
    @OAOne(
        displayName = "RC Verify Detail", 
        reverseName = RCVerifyDetail.P_RCDeployDetail
    )
    @OAFkey(columns = {"RcVerifyDetailId"})
    public RCVerifyDetail getRCVerifyDetail() {
        if (rcVerifyDetail == null) {
            rcVerifyDetail = (RCVerifyDetail) getObject(P_RCVerifyDetail);
        }
        return rcVerifyDetail;
    }
    
    public void setRCVerifyDetail(RCVerifyDetail newValue) {
        fireBeforePropertyChange(P_RCVerifyDetail, this.rcVerifyDetail, newValue);
        RCVerifyDetail old = this.rcVerifyDetail;
        this.rcVerifyDetail = newValue;
        firePropertyChange(P_RCVerifyDetail, old, this.rcVerifyDetail);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.selected = rs.getBoolean(2);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, RCDeployDetail.P_Selected, true);
        }
        int applicationVersionFkey = rs.getInt(3);
        if (!rs.wasNull() && applicationVersionFkey > 0) {
            setProperty(P_ApplicationVersion, new OAObjectKey(applicationVersionFkey));
        }
        int rcDeployFkey = rs.getInt(4);
        if (!rs.wasNull() && rcDeployFkey > 0) {
            setProperty(P_RCDeploy, new OAObjectKey(rcDeployFkey));
        }
        int rcDownloadDetailFkey = rs.getInt(5);
        if (!rs.wasNull() && rcDownloadDetailFkey > 0) {
            setProperty(P_RCDownloadDetail, new OAObjectKey(rcDownloadDetailFkey));
        }
        int rcInstallDetailFkey = rs.getInt(6);
        if (!rs.wasNull() && rcInstallDetailFkey > 0) {
            setProperty(P_RCInstallDetail, new OAObjectKey(rcInstallDetailFkey));
        }
        int rcStageDetailFkey = rs.getInt(7);
        if (!rs.wasNull() && rcStageDetailFkey > 0) {
            setProperty(P_RCStageDetail, new OAObjectKey(rcStageDetailFkey));
        }
        int rcStopDetailFkey = rs.getInt(8);
        if (!rs.wasNull() && rcStopDetailFkey > 0) {
            setProperty(P_RCStopDetail, new OAObjectKey(rcStopDetailFkey));
        }
        int rcVerifyDetailFkey = rs.getInt(9);
        if (!rs.wasNull() && rcVerifyDetailFkey > 0) {
            setProperty(P_RCVerifyDetail, new OAObjectKey(rcVerifyDetailFkey));
        }
        if (rs.getMetaData().getColumnCount() != 9) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
