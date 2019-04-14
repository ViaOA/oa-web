// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class MerchantCategoryPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private MerchantCategoryPPx merchantCategories;
    private MerchantPPx merchants;
    private MerchantCategoryPPx parentMerchantCategory;
     
    public MerchantCategoryPPx(String name) {
        this(null, name);
    }

    public MerchantCategoryPPx(PPxInterface parent, String name) {
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

    public MerchantCategoryPPx merchantCategories() {
        if (merchantCategories == null) merchantCategories = new MerchantCategoryPPx(this, MerchantCategory.P_MerchantCategories);
        return merchantCategories;
    }

    public MerchantPPx merchants() {
        if (merchants == null) merchants = new MerchantPPx(this, MerchantCategory.P_Merchants);
        return merchants;
    }

    public MerchantCategoryPPx parentMerchantCategory() {
        if (parentMerchantCategory == null) parentMerchantCategory = new MerchantCategoryPPx(this, MerchantCategory.P_ParentMerchantCategory);
        return parentMerchantCategory;
    }

    public String id() {
        return pp + "." + MerchantCategory.P_Id;
    }

    public String name() {
        return pp + "." + MerchantCategory.P_Name;
    }

    public String seq() {
        return pp + "." + MerchantCategory.P_Seq;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
