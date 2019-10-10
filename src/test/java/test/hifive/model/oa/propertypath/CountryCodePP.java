// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import test.hifive.model.oa.*;
 
public class CountryCodePP {
    private static LocationPPx calcLocations;
    private static CurrencyTypePPx currencyType;
    private static EmployeePPx employees;
    private static LocationPPx locations;
    private static ProgramPPx programs;
     

    public static LocationPPx calcLocations() {
        if (calcLocations == null) calcLocations = new LocationPPx(CountryCode.P_CalcLocations);
        return calcLocations;
    }

    public static CurrencyTypePPx currencyType() {
        if (currencyType == null) currencyType = new CurrencyTypePPx(CountryCode.P_CurrencyType);
        return currencyType;
    }

    public static EmployeePPx employees() {
        if (employees == null) employees = new EmployeePPx(CountryCode.P_Employees);
        return employees;
    }

    public static LocationPPx locations() {
        if (locations == null) locations = new LocationPPx(CountryCode.P_Locations);
        return locations;
    }

    public static ProgramPPx programs() {
        if (programs == null) programs = new ProgramPPx(CountryCode.P_Programs);
        return programs;
    }

    public static String id() {
        String s = CountryCode.P_Id;
        return s;
    }

    public static String name() {
        String s = CountryCode.P_Name;
        return s;
    }

    public static String code() {
        String s = CountryCode.P_Code;
        return s;
    }

    public static String stateIsRequired() {
        String s = CountryCode.P_StateIsRequired;
        return s;
    }

    public static String stateName() {
        String s = CountryCode.P_StateName;
        return s;
    }

    public static String zipCodeIsRequired() {
        String s = CountryCode.P_ZipCodeIsRequired;
        return s;
    }

    public static String zipCodeName() {
        String s = CountryCode.P_ZipCodeName;
        return s;
    }

    public static String usesState() {
        String s = CountryCode.P_UsesState;
        return s;
    }

    public static String usesZipCode() {
        String s = CountryCode.P_UsesZipCode;
        return s;
    }
}
 