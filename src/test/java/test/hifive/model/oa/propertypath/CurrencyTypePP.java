// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import test.hifive.model.oa.*;
 
public class CurrencyTypePP {
    private static CountryCodePPx countryCodes;
     

    public static CountryCodePPx countryCodes() {
        if (countryCodes == null) countryCodes = new CountryCodePPx(CurrencyType.P_CountryCodes);
        return countryCodes;
    }

    public static String id() {
        String s = CurrencyType.P_Id;
        return s;
    }

    public static String name() {
        String s = CurrencyType.P_Name;
        return s;
    }

    public static String abbreviation() {
        String s = CurrencyType.P_Abbreviation;
        return s;
    }

    public static String symbol() {
        String s = CurrencyType.P_Symbol;
        return s;
    }

    public static String exchangeRate() {
        String s = CurrencyType.P_ExchangeRate;
        return s;
    }

    public static String created() {
        String s = CurrencyType.P_Created;
        return s;
    }

    public static String convertTo() {
        String s = "convertTo";
        return s;
    }

    public static String convertFrom() {
        String s = "convertFrom";
        return s;
    }
}
 
