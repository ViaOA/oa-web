// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class EmployeeCustomDataPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private CustomDataPPx customData;
    private EmployeePPx employee;
     
    public EmployeeCustomDataPPx(String name) {
        this(null, name);
    }

    public EmployeeCustomDataPPx(PPxInterface parent, String name) {
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

    public CustomDataPPx customData() {
        if (customData == null) customData = new CustomDataPPx(this, EmployeeCustomData.P_CustomData);
        return customData;
    }

    public EmployeePPx employee() {
        if (employee == null) employee = new EmployeePPx(this, EmployeeCustomData.P_Employee);
        return employee;
    }

    public String id() {
        return pp + "." + EmployeeCustomData.P_Id;
    }

    public String value() {
        return pp + "." + EmployeeCustomData.P_Value;
    }

    public String code() {
        return pp + "." + EmployeeCustomData.P_Code;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 