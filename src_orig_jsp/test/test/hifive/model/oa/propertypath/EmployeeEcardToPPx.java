// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class EmployeeEcardToPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private EmailPPx email;
    private EmployeeEcardPPx employeeEcard;
    private EmployeePPx toEmployee;
     
    public EmployeeEcardToPPx(String name) {
        this(null, name);
    }

    public EmployeeEcardToPPx(PPxInterface parent, String name) {
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

    public EmailPPx email() {
        if (email == null) email = new EmailPPx(this, EmployeeEcardTo.P_Email);
        return email;
    }

    public EmployeeEcardPPx employeeEcard() {
        if (employeeEcard == null) employeeEcard = new EmployeeEcardPPx(this, EmployeeEcardTo.P_EmployeeEcard);
        return employeeEcard;
    }

    public EmployeePPx toEmployee() {
        if (toEmployee == null) toEmployee = new EmployeePPx(this, EmployeeEcardTo.P_ToEmployee);
        return toEmployee;
    }

    public String id() {
        return pp + "." + EmployeeEcardTo.P_Id;
    }

    public String emailAddress() {
        return pp + "." + EmployeeEcardTo.P_EmailAddress;
    }

    public String name() {
        return pp + "." + EmployeeEcardTo.P_Name;
    }

    public String includeManager() {
        return pp + "." + EmployeeEcardTo.P_IncludeManager;
    }

    public String pdfBytes() {
        return pp + "." + EmployeeEcardTo.P_PdfBytes;
    }

    public String viewPdf() {
        return pp + ".viewPdf";
    }

    @Override
    public String toString() {
        return pp;
    }
}
 