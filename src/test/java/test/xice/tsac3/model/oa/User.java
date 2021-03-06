// Generated by OABuilder
package test.xice.tsac3.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.xice.tsac3.model.oa.filter.*;
import test.xice.tsac3.model.oa.propertypath.*;

import com.viaoa.annotation.*;
 
@OAClass(
    shortName = "use",
    displayName = "User",
    displayProperty = "fullNameAndUserId",
    sortProperty = "lastName"
)
@OATable(
    name = "UserTable",
    indexes = {
        @OAIndex(name = "UserTableLastName", columns = {@OAIndexColumn(name = "LastName")}),
        @OAIndex(name = "UserTableCompany", columns = { @OAIndexColumn(name = "CompanyId") })
    }
)
public class User extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_UserId = "UserId";
    public static final String P_UserId = "UserId";
    public static final String PROPERTY_FirstName = "FirstName";
    public static final String P_FirstName = "FirstName";
    public static final String PROPERTY_LastName = "LastName";
    public static final String P_LastName = "LastName";
    public static final String PROPERTY_Email = "Email";
    public static final String P_Email = "Email";
    public static final String PROPERTY_Phone = "Phone";
    public static final String P_Phone = "Phone";
    public static final String PROPERTY_ScreenAccessType = "ScreenAccessType";
    public static final String P_ScreenAccessType = "ScreenAccessType";
    public static final String PROPERTY_IsGatewayUser = "IsGatewayUser";
    public static final String P_IsGatewayUser = "IsGatewayUser";
    public static final String PROPERTY_TraderMnemonic = "TraderMnemonic";
    public static final String P_TraderMnemonic = "TraderMnemonic";
    public static final String PROPERTY_EndexTradingAccount = "EndexTradingAccount";
    public static final String P_EndexTradingAccount = "EndexTradingAccount";
    public static final String PROPERTY_EndexShipperCode = "EndexShipperCode";
    public static final String P_EndexShipperCode = "EndexShipperCode";
    public static final String PROPERTY_UserFunctionType = "UserFunctionType";
    public static final String P_UserFunctionType = "UserFunctionType";
     
    public static final String PROPERTY_IsLoggedIn = "IsLoggedIn";
    public static final String P_IsLoggedIn = "IsLoggedIn";
    public static final String PROPERTY_FullName = "FullName";
    public static final String P_FullName = "FullName";
    public static final String PROPERTY_FullNameAndUserId = "FullNameAndUserId";
    public static final String P_FullNameAndUserId = "FullNameAndUserId";
    public static final String PROPERTY_TotalUsers = "TotalUsers";
    public static final String P_TotalUsers = "TotalUsers";
    public static final String PROPERTY_EnableLLADCommands = "EnableLLADCommands";
    public static final String P_EnableLLADCommands = "EnableLLADCommands";
    public static final String PROPERTY_EnableLLADCommandsAndLoggedIn = "EnableLLADCommandsAndLoggedIn";
    public static final String P_EnableLLADCommandsAndLoggedIn = "EnableLLADCommandsAndLoggedIn";
    public static final String PROPERTY_FastCompanyName = "FastCompanyName";
    public static final String P_FastCompanyName = "FastCompanyName";
     
    public static final String PROPERTY_CalcEnvironment = "CalcEnvironment";
    public static final String P_CalcEnvironment = "CalcEnvironment";
    public static final String PROPERTY_CalcEnvironment1 = "CalcEnvironment1";
    public static final String P_CalcEnvironment1 = "CalcEnvironment1";
    public static final String PROPERTY_CalcLLADServer = "CalcLLADServer";
    public static final String P_CalcLLADServer = "CalcLLADServer";
    public static final String PROPERTY_Company = "Company";
    public static final String P_Company = "Company";
    public static final String PROPERTY_LLADServers = "LLADServers";
    public static final String P_LLADServers = "LLADServers";
    public static final String PROPERTY_MarketTypes = "MarketTypes";
    public static final String P_MarketTypes = "MarketTypes";
    public static final String PROPERTY_UserLoginHistories = "UserLoginHistories";
    public static final String P_UserLoginHistories = "UserLoginHistories";
    public static final String PROPERTY_UserLogins = "UserLogins";
    public static final String P_UserLogins = "UserLogins";
     
    protected int id;
    protected String userId;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;
    protected String screenAccessType;
    protected boolean isGatewayUser;
    protected String traderMnemonic;
    protected String endexTradingAccount;
    protected String endexShipperCode;
    protected String userFunctionType;
     
    // Links to other objects.
    protected transient Company company;
    protected transient Hub<LLADServer> hubLLADServers;
    protected transient Hub<MarketType> hubMarketTypes;
    // protected transient Hub<UserLoginHistory> hubUserLoginHistories;
    protected transient Hub<UserLogin> hubUserLogins;
     
    public User() {
    }
     
    public User(int id) {
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
    @OAProperty(displayName = "User Id", maxLength = 35, displayLength = 20, columnLength = 14, isProcessed = true)
    @OAColumn(maxLength = 35)
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String newValue) {
        fireBeforePropertyChange(P_UserId, this.userId, newValue);
        String old = userId;
        this.userId = newValue;
        firePropertyChange(P_UserId, old, this.userId);
    }
    @OAProperty(displayName = "First Name", maxLength = 75, displayLength = 20, columnLength = 12, isProcessed = true)
    @OAColumn(maxLength = 75)
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String newValue) {
        fireBeforePropertyChange(P_FirstName, this.firstName, newValue);
        String old = firstName;
        this.firstName = newValue;
        firePropertyChange(P_FirstName, old, this.firstName);
    }
    @OAProperty(displayName = "Last Name", maxLength = 75, displayLength = 20, columnLength = 12, isProcessed = true)
    @OAColumn(maxLength = 75)
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String newValue) {
        fireBeforePropertyChange(P_LastName, this.lastName, newValue);
        String old = lastName;
        this.lastName = newValue;
        firePropertyChange(P_LastName, old, this.lastName);
    }
    @OAProperty(maxLength = 75, displayLength = 40, columnLength = 15, isProcessed = true, isEmail = true)
    @OAColumn(maxLength = 75)
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String newValue) {
        fireBeforePropertyChange(P_Email, this.email, newValue);
        String old = email;
        this.email = newValue;
        firePropertyChange(P_Email, old, this.email);
    }
    @OAProperty(maxLength = 25, displayLength = 25, columnLength = 14, isProcessed = true, isPhone = true)
    @OAColumn(maxLength = 25)
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String newValue) {
        fireBeforePropertyChange(P_Phone, this.phone, newValue);
        String old = phone;
        this.phone = newValue;
        firePropertyChange(P_Phone, old, this.phone);
    }
    @OAProperty(displayName = "Screen Access Type", maxLength = 25, displayLength = 15, columnLength = 8, isProcessed = true)
    @OAColumn(maxLength = 25)
    public String getScreenAccessType() {
        return screenAccessType;
    }
    
    public void setScreenAccessType(String newValue) {
        fireBeforePropertyChange(P_ScreenAccessType, this.screenAccessType, newValue);
        String old = screenAccessType;
        this.screenAccessType = newValue;
        firePropertyChange(P_ScreenAccessType, old, this.screenAccessType);
    }
    @OAProperty(displayName = "Is Gateway User", displayLength = 5, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getIsGatewayUser() {
        return isGatewayUser;
    }
    
    public void setIsGatewayUser(boolean newValue) {
        fireBeforePropertyChange(P_IsGatewayUser, this.isGatewayUser, newValue);
        boolean old = isGatewayUser;
        this.isGatewayUser = newValue;
        firePropertyChange(P_IsGatewayUser, old, this.isGatewayUser);
    }
    @OAProperty(displayName = "Trader Mnemonic", maxLength = 15, displayLength = 8, columnLength = 6, isProcessed = true)
    @OAColumn(maxLength = 15)
    public String getTraderMnemonic() {
        return traderMnemonic;
    }
    
    public void setTraderMnemonic(String newValue) {
        fireBeforePropertyChange(P_TraderMnemonic, this.traderMnemonic, newValue);
        String old = traderMnemonic;
        this.traderMnemonic = newValue;
        firePropertyChange(P_TraderMnemonic, old, this.traderMnemonic);
    }
    @OAProperty(displayName = "Endex Trading Account", maxLength = 25, displayLength = 12, columnLength = 8, isProcessed = true)
    @OAColumn(maxLength = 25)
    public String getEndexTradingAccount() {
        return endexTradingAccount;
    }
    
    public void setEndexTradingAccount(String newValue) {
        fireBeforePropertyChange(P_EndexTradingAccount, this.endexTradingAccount, newValue);
        String old = endexTradingAccount;
        this.endexTradingAccount = newValue;
        firePropertyChange(P_EndexTradingAccount, old, this.endexTradingAccount);
    }
    @OAProperty(displayName = "Endex Shipper Code", maxLength = 25, displayLength = 12, isProcessed = true)
    @OAColumn(maxLength = 25)
    public String getEndexShipperCode() {
        return endexShipperCode;
    }
    
    public void setEndexShipperCode(String newValue) {
        fireBeforePropertyChange(P_EndexShipperCode, this.endexShipperCode, newValue);
        String old = endexShipperCode;
        this.endexShipperCode = newValue;
        firePropertyChange(P_EndexShipperCode, old, this.endexShipperCode);
    }
    @OAProperty(displayName = "User Function Type", maxLength = 25, displayLength = 20, columnLength = 14, isProcessed = true)
    @OAColumn(maxLength = 25)
    public String getUserFunctionType() {
        return userFunctionType;
    }
    
    public void setUserFunctionType(String newValue) {
        fireBeforePropertyChange(P_UserFunctionType, this.userFunctionType, newValue);
        String old = userFunctionType;
        this.userFunctionType = newValue;
        firePropertyChange(P_UserFunctionType, old, this.userFunctionType);
    }
    @OACalculatedProperty(displayName = "Is Logged In", displayLength = 5, columnLength = 12, properties = {P_UserLogins})
    public boolean getIsLoggedIn() {
        boolean isLoggedIn;
        // userLogins
        Hub<UserLogin> hubUserLogins = this.getUserLogins();
        return hubUserLogins.getSize() > 0;
    }
    
     
    @OACalculatedProperty(displayName = "User Name", displayLength = 32, columnLength = 22, properties = {P_FirstName, P_LastName})
    public String getFullName() {
        String fullName = "";
        // firstName
        firstName = this.getFirstName();
        if (firstName != null) {
            if (fullName.length() > 0) fullName += " ";
            fullName += firstName;
        }
    
        // lastName
        lastName = this.getLastName();
        if (lastName != null) {
            if (fullName.length() > 0) fullName += " ";
            fullName += lastName;
        }
    
        return fullName;
    }
    
     
    @OACalculatedProperty(displayName = "User", displayLength = 40, columnLength = 33, properties = {P_FirstName, P_LastName, P_UserId})
    public String getFullNameAndUserId() {
        String fullNameAndUserId = "";
        // firstName
        firstName = this.getFirstName();
        if (firstName != null) {
            if (fullNameAndUserId.length() > 0) fullNameAndUserId += " ";
            fullNameAndUserId += firstName;
        }
    
        // lastName
        lastName = this.getLastName();
        if (lastName != null) {
            if (fullNameAndUserId.length() > 0) fullNameAndUserId += " ";
            fullNameAndUserId += lastName;
        }
    
        // userId
        userId = this.getUserId();
        if (userId != null) {
            if (fullNameAndUserId.length() > 0) fullNameAndUserId += " ";
            fullNameAndUserId += "("+userId+")";
        }
    
        return fullNameAndUserId;
    }
    
     
    @OACalculatedProperty(displayName = "Total Users", displayLength = 5)
    public static int getTotalUsers(Hub<User> hub) {
        return hub.getSize();
    }
     
    @OACalculatedProperty(displayName = "Enable LLAD Commands", description = "used to check if user has access to use LLAD commands.", displayLength = 5, columnName = "LLAD Commands")
    public boolean getEnableLLADCommands() {
        return true;
    }
     
    @OACalculatedProperty(displayName = "Enable LLADCommands And Logged In", displayLength = 29, properties = {P_UserLogins})
    public boolean getEnableLLADCommandsAndLoggedIn() {
        return true;
    }
     
    @OACalculatedProperty(displayName = "Company Name", displayLength = 40, columnLength = 20, properties = {P_Company})
    public String getFastCompanyName() {
        String fastCompanyName = null;
        // company
        Company company = this.getCompany();
        if (company != null) {
            fastCompanyName = company.getName();
        }
        if (fastCompanyName == null) fastCompanyName = "";
        return fastCompanyName;
    }
     
    @OAOne(
        displayName = "Environment", 
        isCalculated = true, 
        reverseName = Environment.P_LoginUsers, 
        allowCreateNew = false, 
        allowAddExisting = false
    )
    private Environment getCalcEnvironment() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        displayName = "Environment", 
        isCalculated = true, 
        reverseName = Environment.P_CalcUsers, 
        allowCreateNew = false, 
        allowAddExisting = false
    )
    private Environment getCalcEnvironment1() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        displayName = "Calc LLADServer", 
        isCalculated = true, 
        reverseName = LLADServer.P_CalcLoginUsers, 
        allowCreateNew = false, 
        allowAddExisting = false
    )
    private LLADServer getCalcLLADServer() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        reverseName = Company.P_Users, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"CompanyId"})
    public Company getCompany() {
        if (company == null) {
            company = (Company) getObject(P_Company);
        }
        return company;
    }
    
    public void setCompany(Company newValue) {
        fireBeforePropertyChange(P_Company, this.company, newValue);
        Company old = this.company;
        this.company = newValue;
        firePropertyChange(P_Company, old, this.company);
    }
    
    @OAMany(
        toClass = LLADServer.class, 
        reverseName = LLADServer.P_Users
    )
    @OALinkTable(name = "LLADServerUserTable", indexName = "LLADServerUser", columns = {"UserTableId"})
    public Hub<LLADServer> getLLADServers() {
        if (hubLLADServers == null) {
            hubLLADServers = (Hub<LLADServer>) getHub(P_LLADServers);
        }
        return hubLLADServers;
    }
    
    @OAMany(
        displayName = "Market Types", 
        toClass = MarketType.class, 
        reverseName = MarketType.P_Users
    )
    @OALinkTable(name = "UserTableMarketType", indexName = "MarketTypeUser", columns = {"UserTableId"})
    public Hub<MarketType> getMarketTypes() {
        if (hubMarketTypes == null) {
            hubMarketTypes = (Hub<MarketType>) getHub(P_MarketTypes);
        }
        return hubMarketTypes;
    }
    
    @OAMany(
        displayName = "User Login Histories", 
        toClass = UserLoginHistory.class, 
        cacheSize = 100, 
        reverseName = UserLoginHistory.P_User, 
        cascadeSave = true, 
        cascadeDelete = true
    )
    public Hub<UserLoginHistory> getUserLoginHistories() {
        Hub<UserLoginHistory> hubUserLoginHistories;
        {
            hubUserLoginHistories = (Hub<UserLoginHistory>) getHub(P_UserLoginHistories);
        }
        return hubUserLoginHistories;
    }
    
    @OAMany(
        displayName = "User Logins", 
        toClass = UserLogin.class, 
        reverseName = UserLogin.P_User, 
        cascadeSave = true
    )
    public Hub<UserLogin> getUserLogins() {
        if (hubUserLogins == null) {
            hubUserLogins = (Hub<UserLogin>) getHub(P_UserLogins);
        }
        return hubUserLogins;
    }
    
    // sendText - sending text message
    public void sendText(String textMsg) {
    }
     
    // forceLogout - force logout
    public void forceLogout() {
    }
     
    // resetPassword - resetting user password
    public void resetPassword(String newPassword, String reenterPassword) {
    }
     
    // requestUserLoginHistory
    public void requestUserLoginHistory() {
    }
     
    // refreshUserCache - Refresh User Cache
    public void refreshUserCache() {
    }
     
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.userId = rs.getString(2);
        this.firstName = rs.getString(3);
        this.lastName = rs.getString(4);
        this.email = rs.getString(5);
        this.phone = rs.getString(6);
        this.screenAccessType = rs.getString(7);
        this.isGatewayUser = rs.getBoolean(8);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, User.P_IsGatewayUser, true);
        }
        this.traderMnemonic = rs.getString(9);
        this.endexTradingAccount = rs.getString(10);
        this.endexShipperCode = rs.getString(11);
        this.userFunctionType = rs.getString(12);
        int companyFkey = rs.getInt(13);
        if (!rs.wasNull() && companyFkey > 0) {
            setProperty(P_Company, new OAObjectKey(companyFkey));
        }
        if (rs.getMetaData().getColumnCount() != 13) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
