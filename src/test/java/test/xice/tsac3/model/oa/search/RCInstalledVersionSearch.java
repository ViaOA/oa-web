// Generated by OABuilder
package test.xice.tsac3.model.oa.search;

import com.viaoa.annotation.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.util.filter.OAQueryFilter;

import test.xice.tsac3.model.oa.*;

import com.viaoa.ds.*;

@OAClass(useDataSource=false, localOnly=true)
public class RCInstalledVersionSearch extends OAObject {
    private static final long serialVersionUID = 1L;



    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }

    protected String extraWhere;
    protected Object[] extraWhereParams;
    protected OAFilter<RCInstalledVersion> filterExtraWhere;

    public void setExtraWhere(String s, Object ... args) {
        this.extraWhere = s;
        this.extraWhereParams = args;
        if (!OAString.isEmpty(s) && getExtraWhereFilter() == null) {
            OAFilter<RCInstalledVersion> f = new OAQueryFilter<RCInstalledVersion>(RCInstalledVersion.class, s, args);
            setExtraWhereFilter(f);
        }
    }
    public void setExtraWhereFilter(OAFilter<RCInstalledVersion> filter) {
        this.filterExtraWhere = filter;
    }
    public OAFilter<RCInstalledVersion> getExtraWhereFilter() {
        return this.filterExtraWhere;
    }

    public OASelect<RCInstalledVersion> getSelect() {
        String sql = "";
        String sortOrder = "";
        Object[] args = new Object[0];

        if (!OAString.isEmpty(extraWhere)) {
            if (sql.length() > 0) sql = "(" + sql + ") AND ";
            sql += extraWhere;
            args = OAArray.add(Object.class, args, extraWhereParams);
        }

        OASelect<RCInstalledVersion> sel = new OASelect<RCInstalledVersion>(RCInstalledVersion.class, sql, args, sortOrder);
        sel.setDataSourceFilter(this.getDataSourceFilter());
        sel.setFilter(this.getCustomFilter());
        return sel;
    }

    private OAFilter<RCInstalledVersion> filterDataSourceFilter;
    public OAFilter<RCInstalledVersion> getDataSourceFilter() {
        if (filterDataSourceFilter != null) return filterDataSourceFilter;
        filterDataSourceFilter = new OAFilter<RCInstalledVersion>() {
            @Override
            public boolean isUsed(RCInstalledVersion rcInstalledVersion) {
                return RCInstalledVersionSearch.this.isUsedForDataSourceFilter(rcInstalledVersion);
            }
        };
        return filterDataSourceFilter;
    }
    
    private OAFilter<RCInstalledVersion> filterCustomFilter;
    public OAFilter<RCInstalledVersion> getCustomFilter() {
        if (filterCustomFilter != null) return filterCustomFilter;
        filterCustomFilter = new OAFilter<RCInstalledVersion>() {
            @Override
            public boolean isUsed(RCInstalledVersion rcInstalledVersion) {
                boolean b = RCInstalledVersionSearch.this.isUsedForCustomFilter(rcInstalledVersion);
                if (b && filterExtraWhere != null) b = filterExtraWhere.isUsed(rcInstalledVersion);
                return b;
            }
        };
        return filterCustomFilter;
    }
    
    public boolean isUsedForDataSourceFilter(RCInstalledVersion rcInstalledVersion) {
        return true;
    }
    public boolean isUsedForCustomFilter(RCInstalledVersion rcInstalledVersion) {
        return true;
    }
}