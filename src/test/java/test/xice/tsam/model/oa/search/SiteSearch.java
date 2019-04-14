// Generated by OABuilder
package test.xice.tsam.model.oa.search;

import java.util.logging.*;

import test.xice.tsam.model.oa.Site;
import test.xice.tsam.model.oa.search.SiteSearch;
import com.viaoa.annotation.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.ds.*;
import com.viaoa.util.filter.OAQueryFilter;

import test.xice.tsam.model.oa.*;
import test.xice.tsam.model.oa.propertypath.*;

@OAClass(useDataSource=false, localOnly=true)
public class SiteSearch extends OAObject {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(SiteSearch.class.getName());


    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }

    protected String extraWhere;
    protected Object[] extraWhereParams;
    protected OAFilter<Site> filterExtraWhere;

    public void setExtraWhere(String s, Object ... args) {
        this.extraWhere = s;
        this.extraWhereParams = args;
        if (!OAString.isEmpty(s) && getExtraWhereFilter() == null) {
            OAFilter<Site> f = new OAQueryFilter<Site>(Site.class, s, args);
            setExtraWhereFilter(f);
        }
    }
    public void setExtraWhereFilter(OAFilter<Site> filter) {
        this.filterExtraWhere = filter;
    }
    public OAFilter<Site> getExtraWhereFilter() {
        return this.filterExtraWhere;
    }

    public OASelect<Site> getSelect() {
        String sql = "";
        String sortOrder = "";
        Object[] args = new Object[0];

        if (!OAString.isEmpty(extraWhere)) {
            if (sql.length() > 0) sql = "(" + sql + ") AND ";
            sql += extraWhere;
            args = OAArray.add(Object.class, args, extraWhereParams);
        }

        OASelect<Site> sel = new OASelect<Site>(Site.class, sql, args, sortOrder);
        sel.setDataSourceFilter(this.getDataSourceFilter());
        sel.setFilter(this.getCustomFilter());
        return sel;
    }

    private OAFilter<Site> filterDataSourceFilter;
    public OAFilter<Site> getDataSourceFilter() {
        if (filterDataSourceFilter != null) return filterDataSourceFilter;
        filterDataSourceFilter = new OAFilter<Site>() {
            @Override
            public boolean isUsed(Site site) {
                return SiteSearch.this.isUsedForDataSourceFilter(site);
            }
        };
        return filterDataSourceFilter;
    }
    
    private OAFilter<Site> filterCustomFilter;
    public OAFilter<Site> getCustomFilter() {
        if (filterCustomFilter != null) return filterCustomFilter;
        filterCustomFilter = new OAFilter<Site>() {
            @Override
            public boolean isUsed(Site site) {
                boolean b = SiteSearch.this.isUsedForCustomFilter(site);
                if (b && filterExtraWhere != null) b = filterExtraWhere.isUsed(site);
                return b;
            }
        };
        return filterCustomFilter;
    }
    
    public boolean isUsedForDataSourceFilter(Site site) {
        return true;
    }
    public boolean isUsedForCustomFilter(Site site) {
        return true;
    }
}
