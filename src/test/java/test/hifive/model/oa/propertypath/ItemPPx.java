// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class ItemPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private AddOnItemPPx addOnItems;
    private AwardTypePPx availableAwardTypes;
    private AwardTypePPx excludeAwardTypes;
    private AwardTypePPx helpingHandAwardType;
    private ImageStorePPx imageStore;
    private AwardTypePPx includeAwardTypes;
    private ItemCategoryPPx itemCategories;
    private ItemTypePPx itemTypes;
    private ItemVendorPPx itemVendor;
    private ProductPPx products;
    private ItemPPx replaceItems;
    private ItemPPx replacesItems;
    private SectionPPx sections;
     
    public ItemPPx(String name) {
        this(null, name);
    }

    public ItemPPx(PPxInterface parent, String name) {
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

    public AddOnItemPPx addOnItems() {
        if (addOnItems == null) addOnItems = new AddOnItemPPx(this, Item.P_AddOnItems);
        return addOnItems;
    }

    public AwardTypePPx availableAwardTypes() {
        if (availableAwardTypes == null) availableAwardTypes = new AwardTypePPx(this, Item.P_AvailableAwardTypes);
        return availableAwardTypes;
    }

    public AwardTypePPx excludeAwardTypes() {
        if (excludeAwardTypes == null) excludeAwardTypes = new AwardTypePPx(this, Item.P_ExcludeAwardTypes);
        return excludeAwardTypes;
    }

    public AwardTypePPx helpingHandAwardType() {
        if (helpingHandAwardType == null) helpingHandAwardType = new AwardTypePPx(this, Item.P_HelpingHandAwardType);
        return helpingHandAwardType;
    }

    public ImageStorePPx imageStore() {
        if (imageStore == null) imageStore = new ImageStorePPx(this, Item.P_ImageStore);
        return imageStore;
    }

    public AwardTypePPx includeAwardTypes() {
        if (includeAwardTypes == null) includeAwardTypes = new AwardTypePPx(this, Item.P_IncludeAwardTypes);
        return includeAwardTypes;
    }

    public ItemCategoryPPx itemCategories() {
        if (itemCategories == null) itemCategories = new ItemCategoryPPx(this, Item.P_ItemCategories);
        return itemCategories;
    }

    public ItemTypePPx itemTypes() {
        if (itemTypes == null) itemTypes = new ItemTypePPx(this, Item.P_ItemTypes);
        return itemTypes;
    }

    public ItemVendorPPx itemVendor() {
        if (itemVendor == null) itemVendor = new ItemVendorPPx(this, Item.P_ItemVendor);
        return itemVendor;
    }

    public ProductPPx products() {
        if (products == null) products = new ProductPPx(this, Item.P_Products);
        return products;
    }

    public ItemPPx replaceItems() {
        if (replaceItems == null) replaceItems = new ItemPPx(this, Item.P_ReplaceItems);
        return replaceItems;
    }

    public ItemPPx replacesItems() {
        if (replacesItems == null) replacesItems = new ItemPPx(this, Item.P_ReplacesItems);
        return replacesItems;
    }

    public SectionPPx sections() {
        if (sections == null) sections = new SectionPPx(this, Item.P_Sections);
        return sections;
    }

    public String id() {
        return pp + "." + Item.P_Id;
    }

    public String created() {
        return pp + "." + Item.P_Created;
    }

    public String vendorCode() {
        return pp + "." + Item.P_VendorCode;
    }

    public String vendorCode2() {
        return pp + "." + Item.P_VendorCode2;
    }

    public String name() {
        return pp + "." + Item.P_Name;
    }

    public String briefText() {
        return pp + "." + Item.P_BriefText;
    }

    public String text() {
        return pp + "." + Item.P_Text;
    }

    public String discontinuedDate() {
        return pp + "." + Item.P_DiscontinuedDate;
    }

    public String discontinuedReason() {
        return pp + "." + Item.P_DiscontinuedReason;
    }

    public String dropShip() {
        return pp + "." + Item.P_DropShip;
    }

    public String otherInformation() {
        return pp + "." + Item.P_OtherInformation;
    }

    public String manufacturer() {
        return pp + "." + Item.P_Manufacturer;
    }

    public String model() {
        return pp + "." + Item.P_Model;
    }

    public String lastUpdate() {
        return pp + "." + Item.P_LastUpdate;
    }

    public String hifiveRating() {
        return pp + "." + Item.P_HifiveRating;
    }

    public String hifiveRatingDate() {
        return pp + "." + Item.P_HifiveRatingDate;
    }

    public String hifiveRatingNote() {
        return pp + "." + Item.P_HifiveRatingNote;
    }

    public String accountNumber() {
        return pp + "." + Item.P_AccountNumber;
    }

    public String cost() {
        return pp + "." + Item.P_Cost;
    }

    public String totalCost() {
        return pp + "." + Item.P_TotalCost;
    }

    public String handlingCost() {
        return pp + "." + Item.P_HandlingCost;
    }

    public String makeApprovedIfNull() {
        return pp + ".makeApprovedIfNull";
    }

    public String hifiveApprovedFilter() {
        return pp + ":hifiveApproved()";
    }

    public String helpingHandsFilter() {
        return pp + ":helpingHands()";
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
