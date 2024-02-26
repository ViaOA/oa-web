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
    shortName = "cc",
    displayName = "Country Code",
    isLookup = true,
    isPreSelect = true,
    displayProperty = "name",
    sortProperty = "code"
)
@OATable(
    indexes = {
        @OAIndex(name = "CountryCodeCurrencyType", columns = { @OAIndexColumn(name = "CurrencyTypeId") })
    }
)
public class CountryCode extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Name = "Name";
    public static final String P_Name = "Name";
    public static final String PROPERTY_Code = "Code";
    public static final String P_Code = "Code";
    public static final String PROPERTY_StateIsRequired = "StateIsRequired";
    public static final String P_StateIsRequired = "StateIsRequired";
    public static final String PROPERTY_StateName = "StateName";
    public static final String P_StateName = "StateName";
    public static final String PROPERTY_ZipCodeIsRequired = "ZipCodeIsRequired";
    public static final String P_ZipCodeIsRequired = "ZipCodeIsRequired";
    public static final String PROPERTY_ZipCodeName = "ZipCodeName";
    public static final String P_ZipCodeName = "ZipCodeName";
     
    public static final String PROPERTY_UsesState = "UsesState";
    public static final String P_UsesState = "UsesState";
    public static final String PROPERTY_UsesZipCode = "UsesZipCode";
    public static final String P_UsesZipCode = "UsesZipCode";
     
    public static final String PROPERTY_CalcLocations = "CalcLocations";
    public static final String P_CalcLocations = "CalcLocations";
    public static final String PROPERTY_CurrencyType = "CurrencyType";
    public static final String P_CurrencyType = "CurrencyType";
    public static final String PROPERTY_Employees = "Employees";
    public static final String P_Employees = "Employees";
    public static final String PROPERTY_Locations = "Locations";
    public static final String P_Locations = "Locations";
    public static final String PROPERTY_Programs = "Programs";
    public static final String P_Programs = "Programs";
     
    protected int id;
    protected String name;
    protected String code;
    protected boolean stateIsRequired;
    protected String stateName;
    protected boolean zipCodeIsRequired;
    protected String zipCodeName;
     
    // Links to other objects.
    protected transient CurrencyType currencyType;
     
    public CountryCode() {
    }
     
    public CountryCode(int id) {
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
    @OAProperty(maxLength = 55, displayLength = 22, columnLength = 12)
    @OAColumn(maxLength = 55)
    public String getName() {
        return name;
    }
    
    public void setName(String newValue) {
        fireBeforePropertyChange(P_Name, this.name, newValue);
        String old = name;
        this.name = newValue;
        firePropertyChange(P_Name, old, this.name);
    }
    @OAProperty(maxLength = 12, displayLength = 4, columnLength = 6)
    @OAColumn(maxLength = 12)
    public String getCode() {
        return code;
    }
    
    public void setCode(String newValue) {
        fireBeforePropertyChange(P_Code, this.code, newValue);
        String old = code;
        this.code = newValue;
        firePropertyChange(P_Code, old, this.code);
    }
    @OAProperty(displayName = "State Is Required", displayLength = 5, columnName = "State Req")
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getStateIsRequired() {
        return stateIsRequired;
    }
    
    public void setStateIsRequired(boolean newValue) {
        fireBeforePropertyChange(P_StateIsRequired, this.stateIsRequired, newValue);
        boolean old = stateIsRequired;
        this.stateIsRequired = newValue;
        firePropertyChange(P_StateIsRequired, old, this.stateIsRequired);
    }
    @OAProperty(displayName = "State Name", maxLength = 35, displayLength = 12)
    @OAColumn(maxLength = 35)
    public String getStateName() {
        return stateName;
    }
    
    public void setStateName(String newValue) {
        fireBeforePropertyChange(P_StateName, this.stateName, newValue);
        String old = stateName;
        this.stateName = newValue;
        firePropertyChange(P_StateName, old, this.stateName);
    }
    @OAProperty(displayName = "Zip Code Is Required", displayLength = 5, columnName = "Zip Req")
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getZipCodeIsRequired() {
        return zipCodeIsRequired;
    }
    
    public void setZipCodeIsRequired(boolean newValue) {
        fireBeforePropertyChange(P_ZipCodeIsRequired, this.zipCodeIsRequired, newValue);
        boolean old = zipCodeIsRequired;
        this.zipCodeIsRequired = newValue;
        firePropertyChange(P_ZipCodeIsRequired, old, this.zipCodeIsRequired);
    }
    @OAProperty(displayName = "Zip Code Name", maxLength = 35, displayLength = 12)
    @OAColumn(maxLength = 35)
    public String getZipCodeName() {
        return zipCodeName;
    }
    
    public void setZipCodeName(String newValue) {
        fireBeforePropertyChange(P_ZipCodeName, this.zipCodeName, newValue);
        String old = zipCodeName;
        this.zipCodeName = newValue;
        firePropertyChange(P_ZipCodeName, old, this.zipCodeName);
    }
    @OACalculatedProperty(displayName = "Uses State", displayLength = 5, properties = {P_StateName})
    public boolean getUsesState() {
        return !OAString.isEmpty(stateName);
    }
    
     
    @OACalculatedProperty(displayName = "Uses Zip Code", displayLength = 5, columnName = "Uses Zip", properties = {P_ZipCodeName})
    public boolean getUsesZipCode() {
        return !OAString.isEmpty(zipCodeName);
    }
    
     
    @OAMany(
        displayName = "Calc Locations", 
        toClass = Location.class, 
        recursive = false, 
        isCalculated = true, 
        reverseName = Location.P_CalcCountryCode, 
        createMethod = false
    )
    private Hub<Location> getCalcLocations() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        displayName = "Currency Type", 
        reverseName = CurrencyType.P_CountryCodes, 
        required = true
    )
    @OAFkey(columns = {"CurrencyTypeId"})
    public CurrencyType getCurrencyType() {
        if (currencyType == null) {
            currencyType = (CurrencyType) getObject(P_CurrencyType);
        }
        return currencyType;
    }
    
    public void setCurrencyType(CurrencyType newValue) {
        fireBeforePropertyChange(P_CurrencyType, this.currencyType, newValue);
        CurrencyType old = this.currencyType;
        this.currencyType = newValue;
        firePropertyChange(P_CurrencyType, old, this.currencyType);
    }
    
    @OAMany(
        toClass = Employee.class, 
        recursive = false, 
        isCalculated = true, 
        reverseName = Employee.P_CountryCode, 
        createMethod = false
    )
    private Hub<Employee> getEmployees() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        toClass = Location.class, 
        recursive = false, 
        reverseName = Location.P_CountryCode, 
        createMethod = false
    )
    private Hub<Location> getLocations() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAMany(
        toClass = Program.class, 
        reverseName = Program.P_CountryCode, 
        createMethod = false
    )
    private Hub<Program> getPrograms() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.name = rs.getString(2);
        this.code = rs.getString(3);
        this.stateIsRequired = rs.getBoolean(4);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, CountryCode.P_StateIsRequired, true);
        }
        this.stateName = rs.getString(5);
        this.zipCodeIsRequired = rs.getBoolean(6);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, CountryCode.P_ZipCodeIsRequired, true);
        }
        this.zipCodeName = rs.getString(7);
        int currencyTypeFkey = rs.getInt(8);
        if (!rs.wasNull() && currencyTypeFkey > 0) {
            setProperty(P_CurrencyType, new OAObjectKey(currencyTypeFkey));
        }
        if (rs.getMetaData().getColumnCount() != 8) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 