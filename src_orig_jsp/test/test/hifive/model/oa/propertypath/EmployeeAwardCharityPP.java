// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import test.hifive.model.oa.*;
 
public class EmployeeAwardCharityPP {
    private static CharityPPx charity;
    private static EmployeeAwardPPx employeeAward;
     

    public static CharityPPx charity() {
        if (charity == null) charity = new CharityPPx(EmployeeAwardCharity.P_Charity);
        return charity;
    }

    public static EmployeeAwardPPx employeeAward() {
        if (employeeAward == null) employeeAward = new EmployeeAwardPPx(EmployeeAwardCharity.P_EmployeeAward);
        return employeeAward;
    }

    public static String id() {
        String s = EmployeeAwardCharity.P_Id;
        return s;
    }

    public static String created() {
        String s = EmployeeAwardCharity.P_Created;
        return s;
    }

    public static String value() {
        String s = EmployeeAwardCharity.P_Value;
        return s;
    }

    public static String sentDate() {
        String s = EmployeeAwardCharity.P_SentDate;
        return s;
    }

    public static String invoiceNumber() {
        String s = EmployeeAwardCharity.P_InvoiceNumber;
        return s;
    }

    public static String invoiceDate() {
        String s = EmployeeAwardCharity.P_InvoiceDate;
        return s;
    }

    public static String vendorInvoiced() {
        String s = EmployeeAwardCharity.P_VendorInvoiced;
        return s;
    }
}
 