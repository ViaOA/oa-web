// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class AddOnItemPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private AwardTypePPx awardType;
    private EmployeeAwardPPx employeeAwards;
    private ItemPPx item;
    private LocationPPx location;
    private ProgramPPx program;
     
    public AddOnItemPPx(String name) {
        this(null, name);
    }

    public AddOnItemPPx(PPxInterface parent, String name) {
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

    public AwardTypePPx awardType() {
        if (awardType == null) awardType = new AwardTypePPx(this, AddOnItem.P_AwardType);
        return awardType;
    }

    public EmployeeAwardPPx employeeAwards() {
        if (employeeAwards == null) employeeAwards = new EmployeeAwardPPx(this, AddOnItem.P_EmployeeAwards);
        return employeeAwards;
    }

    public ItemPPx item() {
        if (item == null) item = new ItemPPx(this, AddOnItem.P_Item);
        return item;
    }

    public LocationPPx location() {
        if (location == null) location = new LocationPPx(this, AddOnItem.P_Location);
        return location;
    }

    public ProgramPPx program() {
        if (program == null) program = new ProgramPPx(this, AddOnItem.P_Program);
        return program;
    }

    public String id() {
        return pp + "." + AddOnItem.P_Id;
    }

    public String created() {
        return pp + "." + AddOnItem.P_Created;
    }

    public String value() {
        return pp + "." + AddOnItem.P_Value;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 