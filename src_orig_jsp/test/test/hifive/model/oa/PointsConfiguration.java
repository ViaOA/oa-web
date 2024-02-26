// Generated by OABuilder
package test.hifive.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.hifive.model.oa.filter.*;
import test.hifive.model.oa.propertypath.*;

import com.viaoa.annotation.*;
 
@OAClass(
    shortName = "pc",
    displayName = "Points Configuration",
    displayProperty = "program"
)
@OATable(
)
public class PointsConfiguration extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_NotificationToNominatorManager = "NotificationToNominatorManager";
    public static final String P_NotificationToNominatorManager = "NotificationToNominatorManager";
    public static final String PROPERTY_NotificationToRecipientManager = "NotificationToRecipientManager";
    public static final String P_NotificationToRecipientManager = "NotificationToRecipientManager";
    public static final String PROPERTY_CertificateToNominator = "CertificateToNominator";
    public static final String P_CertificateToNominator = "CertificateToNominator";
    public static final String PROPERTY_CertificateToRecipient = "CertificateToRecipient";
    public static final String P_CertificateToRecipient = "CertificateToRecipient";
    public static final String PROPERTY_CertificateToRecipientManager = "CertificateToRecipientManager";
    public static final String P_CertificateToRecipientManager = "CertificateToRecipientManager";
    public static final String PROPERTY_NominationApprovedBy = "NominationApprovedBy";
    public static final String P_NominationApprovedBy = "NominationApprovedBy";
    public static final String PROPERTY_NominationApprovedByAsString = "NominationApprovedByAsString";
    public static final String P_NominationApprovedByAsString = "NominationApprovedByAsString";
    public static final String PROPERTY_DaysToDelayPoints = "DaysToDelayPoints";
    public static final String P_DaysToDelayPoints = "DaysToDelayPoints";
     
     
    public static final String PROPERTY_Program = "Program";
    public static final String P_Program = "Program";
     
    protected int id;
    protected boolean notificationToNominatorManager;
    protected boolean notificationToRecipientManager;
    protected boolean certificateToNominator;
    protected boolean certificateToRecipient;
    protected boolean certificateToRecipientManager;
    protected int nominationApprovedBy;
    public static final int NOMINATIONAPPROVEDBY_RecipientsManager = 0;
    public static final int NOMINATIONAPPROVEDBY_NominatorsManager = 1;
    public static final Hub<String> hubNominationApprovedBy;
    static {
        hubNominationApprovedBy = new Hub<String>(String.class);
        hubNominationApprovedBy.addElement("Recipient's Manager");
        hubNominationApprovedBy.addElement("Nominator's Manager");
    }
    protected int daysToDelayPoints;
     
    // Links to other objects.
    protected transient Program program;
     
    public PointsConfiguration() {
        if (!isLoading()) {
            setNotificationToNominatorManager(false);
            setNotificationToRecipientManager(false);
            setCertificateToNominator(false);
            setCertificateToRecipient(false);
            setCertificateToRecipientManager(false);
            setNominationApprovedBy(0);
            setDaysToDelayPoints(0);
        }
    }
     
    public PointsConfiguration(int id) {
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
    @OAProperty(displayName = "Notification To Nominator Manager", description = "Does the nominator's manager get notified of creation/approval/denial for this nomination", defaultValue = "false", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    /**
      Does the nominator's manager get notified of creation/approval/denial for this nomination
    */
    public boolean getNotificationToNominatorManager() {
        return notificationToNominatorManager;
    }
    
    public void setNotificationToNominatorManager(boolean newValue) {
        fireBeforePropertyChange(P_NotificationToNominatorManager, this.notificationToNominatorManager, newValue);
        boolean old = notificationToNominatorManager;
        this.notificationToNominatorManager = newValue;
        firePropertyChange(P_NotificationToNominatorManager, old, this.notificationToNominatorManager);
    }
    @OAProperty(displayName = "Notification To Recipient Manager", description = "Does the recipient's manager get notified of creation/approval/denial for this nomination", defaultValue = "false", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    /**
      Does the recipient's manager get notified of creation/approval/denial for this nomination
    */
    public boolean getNotificationToRecipientManager() {
        return notificationToRecipientManager;
    }
    
    public void setNotificationToRecipientManager(boolean newValue) {
        fireBeforePropertyChange(P_NotificationToRecipientManager, this.notificationToRecipientManager, newValue);
        boolean old = notificationToRecipientManager;
        this.notificationToRecipientManager = newValue;
        firePropertyChange(P_NotificationToRecipientManager, old, this.notificationToRecipientManager);
    }
    @OAProperty(displayName = "Certificate To Nominator", description = "does a copy of the award certificate go to the nominator", defaultValue = "false", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    /**
      does a copy of the award certificate go to the nominator
    */
    public boolean getCertificateToNominator() {
        return certificateToNominator;
    }
    
    public void setCertificateToNominator(boolean newValue) {
        fireBeforePropertyChange(P_CertificateToNominator, this.certificateToNominator, newValue);
        boolean old = certificateToNominator;
        this.certificateToNominator = newValue;
        firePropertyChange(P_CertificateToNominator, old, this.certificateToNominator);
    }
    @OAProperty(displayName = "Certificate To Recipient", description = "does a copy of the award certificate go to the recipient", defaultValue = "false", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    /**
      does a copy of the award certificate go to the recipient
    */
    public boolean getCertificateToRecipient() {
        return certificateToRecipient;
    }
    
    public void setCertificateToRecipient(boolean newValue) {
        fireBeforePropertyChange(P_CertificateToRecipient, this.certificateToRecipient, newValue);
        boolean old = certificateToRecipient;
        this.certificateToRecipient = newValue;
        firePropertyChange(P_CertificateToRecipient, old, this.certificateToRecipient);
    }
    @OAProperty(displayName = "Certificate To Recipient Manager", description = "does a copy of the award certificate go to the recipient's manager", defaultValue = "false", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    /**
      does a copy of the award certificate go to the recipient's manager
    */
    public boolean getCertificateToRecipientManager() {
        return certificateToRecipientManager;
    }
    
    public void setCertificateToRecipientManager(boolean newValue) {
        fireBeforePropertyChange(P_CertificateToRecipientManager, this.certificateToRecipientManager, newValue);
        boolean old = certificateToRecipientManager;
        this.certificateToRecipientManager = newValue;
        firePropertyChange(P_CertificateToRecipientManager, old, this.certificateToRecipientManager);
    }
    @OAProperty(displayName = "Nomination Approved By", defaultValue = "0", displayLength = 5, isNameValue = true)
    @OAColumn(name = "NominationApprovedByRecipientManagement", sqlType = java.sql.Types.INTEGER)
    public int getNominationApprovedBy() {
        return nominationApprovedBy;
    }
    
    public void setNominationApprovedBy(int newValue) {
        fireBeforePropertyChange(P_NominationApprovedBy, this.nominationApprovedBy, newValue);
        int old = nominationApprovedBy;
        this.nominationApprovedBy = newValue;
        firePropertyChange(P_NominationApprovedBy, old, this.nominationApprovedBy);
    }
    public String getNominationApprovedByAsString() {
        if (isNull(P_NominationApprovedBy)) return "";
        String s = hubNominationApprovedBy.getAt(getNominationApprovedBy());
        if (s == null) s = "";
        return s;
    }
    @OAProperty(displayName = "Days To Delay Points", description = "the number of days to wait after a nomination is approved before distributing the points", defaultValue = "0", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    /**
      the number of days to wait after a nomination is approved before distributing the points
    */
    public int getDaysToDelayPoints() {
        return daysToDelayPoints;
    }
    
    public void setDaysToDelayPoints(int newValue) {
        fireBeforePropertyChange(P_DaysToDelayPoints, this.daysToDelayPoints, newValue);
        int old = daysToDelayPoints;
        this.daysToDelayPoints = newValue;
        firePropertyChange(P_DaysToDelayPoints, old, this.daysToDelayPoints);
    }
    @OAOne(
        reverseName = Program.P_PointsConfiguration, 
        required = true, 
        allowCreateNew = false
    )
    public Program getProgram() {
        if (program == null) {
            program = (Program) getObject(P_Program);
        }
        return program;
    }
    
    public void setProgram(Program newValue) {
        fireBeforePropertyChange(P_Program, this.program, newValue);
        Program old = this.program;
        this.program = newValue;
        firePropertyChange(P_Program, old, this.program);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.notificationToNominatorManager = rs.getBoolean(2);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, PointsConfiguration.P_NotificationToNominatorManager, true);
        }
        this.notificationToRecipientManager = rs.getBoolean(3);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, PointsConfiguration.P_NotificationToRecipientManager, true);
        }
        this.certificateToNominator = rs.getBoolean(4);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, PointsConfiguration.P_CertificateToNominator, true);
        }
        this.certificateToRecipient = rs.getBoolean(5);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, PointsConfiguration.P_CertificateToRecipient, true);
        }
        this.certificateToRecipientManager = rs.getBoolean(6);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, PointsConfiguration.P_CertificateToRecipientManager, true);
        }
        this.nominationApprovedBy = (int) rs.getInt(7);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, PointsConfiguration.P_NominationApprovedBy, true);
        }
        this.daysToDelayPoints = (int) rs.getInt(8);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, PointsConfiguration.P_DaysToDelayPoints, true);
        }
        if (rs.getMetaData().getColumnCount() != 8) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 