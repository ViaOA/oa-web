// Generated by OABuilder
package test.hifive.model.oa.filter;

import com.viaoa.annotation.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.hifive.model.oa.*;
import test.hifive.model.oa.propertypath.*;

import java.util.*;

@OAClass(useDataSource=false, localOnly=true)
@OAClassFilter(name = "Recent", displayName = "Recent Recipient", hasInputParams = false)
public class InspireRecipientRecentFilter extends OAObject implements CustomHubFilter<InspireRecipient> {
    private static final long serialVersionUID = 1L;

    public static final String PPCode = ":Recent()";
    private Hub<InspireRecipient> hubMaster;
    private Hub<InspireRecipient> hub;
    private HubFilter<InspireRecipient> hubFilter;
    private OAObjectCacheFilter<InspireRecipient> cacheFilter;
    private boolean bUseObjectCache;

    public InspireRecipientRecentFilter(Hub<InspireRecipient> hub) {
        this(null, hub, true);
    }
    public InspireRecipientRecentFilter(Hub<InspireRecipient> hubMaster, Hub<InspireRecipient> hub) {
        this(hubMaster, hub, false);
    }
    public InspireRecipientRecentFilter(Hub<InspireRecipient> hubMaster, Hub<InspireRecipient> hubFiltered, boolean bUseObjectCache) {
        this.hubMaster = hubMaster;
        this.hub = hubFiltered;
        this.bUseObjectCache = bUseObjectCache;
        if (hubMaster != null) getHubFilter();
        if (bUseObjectCache) getObjectCacheFilter();
    }


    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }
    public void refresh() {
        if (hubFilter != null) getHubFilter().refresh();
        if (cacheFilter != null) getObjectCacheFilter().refresh();
    }

    @Override
    public HubFilter<InspireRecipient> getHubFilter() {
        if (hubFilter != null) return hubFilter;
        if (hubMaster == null) return null;
        hubFilter = new HubFilter<InspireRecipient>(hubMaster, hub) {
            @Override
            public boolean isUsed(InspireRecipient inspireRecipient) {
                return InspireRecipientRecentFilter.this.isUsed(inspireRecipient);
            }
        };
        hubFilter.addDependentProperty(InspireRecipientPP.completedDate());
        return hubFilter;
    }

    public OAObjectCacheFilter<InspireRecipient> getObjectCacheFilter() {
        if (cacheFilter != null) return cacheFilter;
        if (!bUseObjectCache) return null;
        cacheFilter = new OAObjectCacheFilter<InspireRecipient>(hubMaster) {
            @Override
            public boolean isUsed(InspireRecipient inspireRecipient) {
                return InspireRecipientRecentFilter.this.isUsed(inspireRecipient);
            }
        };
        cacheFilter.addDependentProperty(InspireRecipientPP.completedDate());
        return cacheFilter;
    }

    public boolean isUsed(InspireRecipient inspireRecipient) {
        if (inspireRecipient == null) return false;
        OADate completedDate = inspireRecipient.getCompletedDate();
        if (completedDate == null) return false;
        if (inspireRecipient.getApprovalStatus() != InspireApproval.STATUS_Approved) return false;
        
        OADate today = new OADate();
        OADate d = (OADate) completedDate.addDays(30);
        if (today.before(d)) return false; // wait 30 days from completed date
        
        // show up to 60 days from completed date
        d = (OADate) (completedDate).addDays(60);
        return today.before(d);
    }
    
}
