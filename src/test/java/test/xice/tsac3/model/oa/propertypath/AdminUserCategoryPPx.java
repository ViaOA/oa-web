// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac3.model.oa.*;
 
public class AdminUserCategoryPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private AdminUserCategoryPPx adminUserCategories;
    private AdminUserCategoryPPx parentAdminUserCategory;
     
    public AdminUserCategoryPPx(String name) {
        this(null, name);
    }

    public AdminUserCategoryPPx(PPxInterface parent, String name) {
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

    public AdminUserCategoryPPx adminUserCategories() {
        if (adminUserCategories == null) adminUserCategories = new AdminUserCategoryPPx(this, AdminUserCategory.P_AdminUserCategories);
        return adminUserCategories;
    }

    public AdminUserCategoryPPx parentAdminUserCategory() {
        if (parentAdminUserCategory == null) parentAdminUserCategory = new AdminUserCategoryPPx(this, AdminUserCategory.P_ParentAdminUserCategory);
        return parentAdminUserCategory;
    }

    public String id() {
        return pp + "." + AdminUserCategory.P_Id;
    }

    public String name() {
        return pp + "." + AdminUserCategory.P_Name;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
