// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class EmployeeAwardCharityPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private CharityPPx charity;
    private EmployeeAwardPPx employeeAward;
     
    public EmployeeAwardCharityPPx(String name) {
        this(null, name);
    }

    public EmployeeAwardCharityPPx(PPxInterface parent, String name) {
        String s = null;
        if (parent != null) {
            s = parent.toString();
        }
        if (s == null) s = "";
        if (name != null) {
            if (s.length() > 0) s += ".";
            s += name;
        }
        pp = s;
    }

    public CharityPPx charity() {
        if (charity == null) charity = new CharityPPx(this, EmployeeAwardCharity.P_Charity);
        return charity;
    }

    public EmployeeAwardPPx employeeAward() {
        if (employeeAward == null) employeeAward = new EmployeeAwardPPx(this, EmployeeAwardCharity.P_EmployeeAward);
        return employeeAward;
    }

    public String id() {
        return pp + "." + EmployeeAwardCharity.P_Id;
    }

    public String created() {
        return pp + "." + EmployeeAwardCharity.P_Created;
    }

    public String value() {
        return pp + "." + EmployeeAwardCharity.P_Value;
    }

    public String sentDate() {
        return pp + "." + EmployeeAwardCharity.P_SentDate;
    }

    public String invoiceNumber() {
        return pp + "." + EmployeeAwardCharity.P_InvoiceNumber;
    }

    public String invoiceDate() {
        return pp + "." + EmployeeAwardCharity.P_InvoiceDate;
    }

    public String vendorInvoiced() {
        return pp + "." + EmployeeAwardCharity.P_VendorInvoiced;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
