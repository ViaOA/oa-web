// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class CustomDataPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private EmployeeCustomDataPPx employeeCustomDatas;
    private ProgramPPx program;
     
    public CustomDataPPx(String name) {
        this(null, name);
    }

    public CustomDataPPx(PPxInterface parent, String name) {
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

    public EmployeeCustomDataPPx employeeCustomDatas() {
        if (employeeCustomDatas == null) employeeCustomDatas = new EmployeeCustomDataPPx(this, CustomData.P_EmployeeCustomDatas);
        return employeeCustomDatas;
    }

    public ProgramPPx program() {
        if (program == null) program = new ProgramPPx(this, CustomData.P_Program);
        return program;
    }

    public String id() {
        return pp + "." + CustomData.P_Id;
    }

    public String code() {
        return pp + "." + CustomData.P_Code;
    }

    public String name() {
        return pp + "." + CustomData.P_Name;
    }

    public String description() {
        return pp + "." + CustomData.P_Description;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
