// Generated by OABuilder
package test.xice.tsam.model.oa;
 
import java.util.logging.*;
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import test.xice.tsam.model.oa.AdminUser;
import test.xice.tsam.model.oa.MRADServerCommand;
import com.viaoa.annotation.*;
import com.viaoa.util.OADateTime;

import test.xice.tsam.model.oa.filter.*;
import test.xice.tsam.model.oa.propertypath.*;

import com.viaoa.util.OADate;

 
@OAClass(
    shortName = "au",
    displayName = "Admin User",
    isLookup = true,
    isPreSelect = true,
    displayProperty = "fullName"
)
@OATable(
)
public class AdminUser extends OAObject {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(AdminUser.class.getName());
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_LoginId = "LoginId";
    public static final String P_LoginId = "LoginId";
    public static final String PROPERTY_Password = "Password";
    public static final String P_Password = "Password";
    public static final String PROPERTY_FirstName = "FirstName";
    public static final String P_FirstName = "FirstName";
    public static final String PROPERTY_LastName = "LastName";
    public static final String P_LastName = "LastName";
    public static final String PROPERTY_Title = "Title";
    public static final String P_Title = "Title";
    public static final String PROPERTY_PrefixName = "PrefixName";
    public static final String P_PrefixName = "PrefixName";
    public static final String PROPERTY_InactiveDate = "InactiveDate";
    public static final String P_InactiveDate = "InactiveDate";
    public static final String PROPERTY_InactiveReason = "InactiveReason";
    public static final String P_InactiveReason = "InactiveReason";
    public static final String PROPERTY_LoggedIn = "LoggedIn";
    public static final String P_LoggedIn = "LoggedIn";
    public static final String PROPERTY_Admin = "Admin";
    public static final String P_Admin = "Admin";
    public static final String PROPERTY_EditProcessed = "EditProcessed";
    public static final String P_EditProcessed = "EditProcessed";
    public static final String PROPERTY_EnableLLADCommands = "EnableLLADCommands";
    public static final String P_EnableLLADCommands = "EnableLLADCommands";
    public static final String PROPERTY_EnableGSMR = "EnableGSMR";
    public static final String P_EnableGSMR = "EnableGSMR";
    public static final String PROPERTY_EnableMRAD = "EnableMRAD";
    public static final String P_EnableMRAD = "EnableMRAD";
    public static final String PROPERTY_MiscPassword = "MiscPassword";
    public static final String P_MiscPassword = "MiscPassword";
     
    public static final String PROPERTY_FullName = "FullName";
    public static final String P_FullName = "FullName";
     
    public static final String PROPERTY_MRADServerCommands = "MRADServerCommands";
    public static final String P_MRADServerCommands = "MRADServerCommands";
     
    protected int id;
    protected OADateTime created;
    protected String loginId;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String title;
    protected String prefixName;
    protected OADate inactiveDate;
    protected String inactiveReason;
    protected boolean loggedIn;
    protected boolean admin;
    protected boolean editProcessed;
    protected boolean enableLLADCommands;
    protected boolean enableGSMR;
    protected boolean enableMRAD;
    protected String miscPassword;
     
    // Links to other objects.
     
    public AdminUser() {
        if (!isLoading()) {
            setCreated(new OADateTime());
        }
    }
     
    public AdminUser(int id) {
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
    
    @OAProperty(displayName = "Login Id", maxLength = 25, isImportMatch = true, displayLength = 10)
    @OAColumn(maxLength = 25)
    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String newValue) {
        fireBeforePropertyChange(P_LoginId, this.loginId, newValue);
        String old = loginId;
        this.loginId = newValue;
        firePropertyChange(P_LoginId, old, this.loginId);
    }
    
    @OAProperty(maxLength = 75, displayLength = 10, columnLength = 8, isPassword = true)
    @OAColumn(maxLength = 75)
    public String getPassword() {
        return password;
    }
    public void setPassword(String newValue) {
        fireBeforePropertyChange(P_Password, this.password, newValue);
        String old = password;
        this.password = newValue;
        firePropertyChange(P_Password, old, this.password);
    }
    
    @OAProperty(displayName = "First Name", maxLength = 25, displayLength = 12, columnLength = 10)
    @OAColumn(maxLength = 25)
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String newValue) {
        fireBeforePropertyChange(P_FirstName, this.firstName, newValue);
        String old = firstName;
        this.firstName = newValue;
        firePropertyChange(P_FirstName, old, this.firstName);
    }
    
    @OAProperty(displayName = "Last Name", maxLength = 35, displayLength = 12, columnLength = 10)
    @OAColumn(maxLength = 35)
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String newValue) {
        fireBeforePropertyChange(P_LastName, this.lastName, newValue);
        String old = lastName;
        this.lastName = newValue;
        firePropertyChange(P_LastName, old, this.lastName);
    }
    
    @OAProperty(maxLength = 35, displayLength = 8, columnLength = 7)
    @OAColumn(maxLength = 35)
    public String getTitle() {
        return title;
    }
    public void setTitle(String newValue) {
        fireBeforePropertyChange(P_Title, this.title, newValue);
        String old = title;
        this.title = newValue;
        firePropertyChange(P_Title, old, this.title);
    }
    
    @OAProperty(displayName = "Prefix Name", maxLength = 20, displayLength = 4, columnLength = 3)
    @OAColumn(maxLength = 20)
    public String getPrefixName() {
        return prefixName;
    }
    public void setPrefixName(String newValue) {
        fireBeforePropertyChange(P_PrefixName, this.prefixName, newValue);
        String old = prefixName;
        this.prefixName = newValue;
        firePropertyChange(P_PrefixName, old, this.prefixName);
    }
    
    @OAProperty(displayName = "Inactive Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getInactiveDate() {
        return inactiveDate;
    }
    public void setInactiveDate(OADate newValue) {
        fireBeforePropertyChange(P_InactiveDate, this.inactiveDate, newValue);
        OADate old = inactiveDate;
        this.inactiveDate = newValue;
        firePropertyChange(P_InactiveDate, old, this.inactiveDate);
    }
    
    @OAProperty(displayName = "Inactive Reason", maxLength = 125, displayLength = 18, columnLength = 12)
    @OAColumn(maxLength = 125)
    public String getInactiveReason() {
        return inactiveReason;
    }
    public void setInactiveReason(String newValue) {
        fireBeforePropertyChange(P_InactiveReason, this.inactiveReason, newValue);
        String old = inactiveReason;
        this.inactiveReason = newValue;
        firePropertyChange(P_InactiveReason, old, this.inactiveReason);
    }
    
    @OAProperty(displayName = "Logged In", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getLoggedIn() {
        return loggedIn;
    }
    public void setLoggedIn(boolean newValue) {
        fireBeforePropertyChange(P_LoggedIn, this.loggedIn, newValue);
        boolean old = loggedIn;
        this.loggedIn = newValue;
        firePropertyChange(P_LoggedIn, old, this.loggedIn);
    }
    
    @OAProperty(displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getAdmin() {
        return admin;
    }
    public void setAdmin(boolean newValue) {
        fireBeforePropertyChange(P_Admin, this.admin, newValue);
        boolean old = admin;
        this.admin = newValue;
        firePropertyChange(P_Admin, old, this.admin);
    }
    
    @OAProperty(displayName = "Edit Processed", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getEditProcessed() {
        return editProcessed;
    }
    public void setEditProcessed(boolean newValue) {
        fireBeforePropertyChange(P_EditProcessed, this.editProcessed, newValue);
        boolean old = editProcessed;
        this.editProcessed = newValue;
        firePropertyChange(P_EditProcessed, old, this.editProcessed);
    }
    
    @OAProperty(displayName = "LLAD Commands", description = "this will give the user permissions to use Login/Logout commands for Users.", displayLength = 5)
    @OAColumn(name = "CanUseLLADCommands", sqlType = java.sql.Types.BOOLEAN)
    /**
      this will give the user permissions to use Login/Logout commands for Users.
    */
    public boolean getEnableLLADCommands() {
        return enableLLADCommands;
    }
    public void setEnableLLADCommands(boolean newValue) {
        fireBeforePropertyChange(P_EnableLLADCommands, this.enableLLADCommands, newValue);
        boolean old = enableLLADCommands;
        this.enableLLADCommands = newValue;
        firePropertyChange(P_EnableLLADCommands, old, this.enableLLADCommands);
    }
    
    @OAProperty(displayName = "Enable GSMR", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getEnableGSMR() {
        return enableGSMR;
    }
    public void setEnableGSMR(boolean newValue) {
        fireBeforePropertyChange(P_EnableGSMR, this.enableGSMR, newValue);
        boolean old = enableGSMR;
        this.enableGSMR = newValue;
        firePropertyChange(P_EnableGSMR, old, this.enableGSMR);
    }
    
    @OAProperty(displayName = "Enable MRAD", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getEnableMRAD() {
        return enableMRAD;
    }
    public void setEnableMRAD(boolean newValue) {
        fireBeforePropertyChange(P_EnableMRAD, this.enableMRAD, newValue);
        boolean old = enableMRAD;
        this.enableMRAD = newValue;
        firePropertyChange(P_EnableMRAD, old, this.enableMRAD);
    }
    
    @OAProperty(displayName = "TE/CE Password", maxLength = 75, displayLength = 10, columnLength = 8, isPassword = true)
    @OAColumn(maxLength = 75)
    public String getMiscPassword() {
        return miscPassword;
    }
    public void setMiscPassword(String newValue) {
        fireBeforePropertyChange(P_MiscPassword, this.miscPassword, newValue);
        String old = miscPassword;
        this.miscPassword = newValue;
        firePropertyChange(P_MiscPassword, old, this.miscPassword);
    }
    
    @OACalculatedProperty(displayName = "Full Name", displayLength = 14, columnLength = 13, properties = {P_FirstName, P_LastName, P_Title, P_PrefixName})
    public String getFullName() {
        String fn = "";
        if (prefixName != null)  fn = prefixName;
        if (!OAString.isEmpty(firstName)) {
            if (fn.length() > 0) fn += " ";
            fn += firstName;
        }
        if (!OAString.isEmpty(lastName)) {
            if (fn.length() > 0) fn += " ";
            fn += lastName;
        }
        
        return fn;
    }
     
    @OAMany(
        displayName = "MRAD Server Commands", 
        toClass = MRADServerCommand.class, 
        reverseName = MRADServerCommand.P_AdminUser, 
        createMethod = false
    )
    private Hub<MRADServerCommand> getMRADServerCommands() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Timestamp timestamp;
        timestamp = rs.getTimestamp(2);
        if (timestamp != null) this.created = new OADateTime(timestamp);
        this.loginId = rs.getString(3);
        this.password = rs.getString(4);
        this.firstName = rs.getString(5);
        this.lastName = rs.getString(6);
        this.title = rs.getString(7);
        this.prefixName = rs.getString(8);
        java.sql.Date date;
        date = rs.getDate(9);
        if (date != null) this.inactiveDate = new OADate(date);
        this.inactiveReason = rs.getString(10);
        this.loggedIn = rs.getBoolean(11);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, AdminUser.P_LoggedIn, true);
        }
        this.admin = rs.getBoolean(12);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, AdminUser.P_Admin, true);
        }
        this.editProcessed = rs.getBoolean(13);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, AdminUser.P_EditProcessed, true);
        }
        this.enableLLADCommands = rs.getBoolean(14);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, AdminUser.P_EnableLLADCommands, true);
        }
        this.enableGSMR = rs.getBoolean(15);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, AdminUser.P_EnableGSMR, true);
        }
        this.enableMRAD = rs.getBoolean(16);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, AdminUser.P_EnableMRAD, true);
        }
        this.miscPassword = rs.getString(17);
        if (rs.getMetaData().getColumnCount() != 17) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 