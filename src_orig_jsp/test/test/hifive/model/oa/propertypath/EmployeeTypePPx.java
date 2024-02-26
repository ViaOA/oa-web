// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class EmployeeTypePPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private EmployeePPx employees;
     
    public EmployeeTypePPx(String name) {
        this(null, name);
    }

    public EmployeeTypePPx(PPxInterface parent, String name) {
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

    public EmployeePPx employees() {
        if (employees == null) employees = new EmployeePPx(this, EmployeeType.P_Employees);
        return employees;
    }

    public String id() {
        return pp + "." + EmployeeType.P_Id;
    }

    public String name() {
        return pp + "." + EmployeeType.P_Name;
    }

    public String type() {
        return pp + "." + EmployeeType.P_Type;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 