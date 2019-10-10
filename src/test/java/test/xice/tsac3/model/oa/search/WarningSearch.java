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
public class WarningSearch extends OAObject {
    private static final long serialVersionUID = 1L;



    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }

    protected String extraWhere;
    protected Object[] extraWhereParams;
    protected OAFilter<Warning> filterExtraWhere;

    public void setExtraWhere(String s, Object ... args) {
        this.extraWhere = s;
        this.extraWhereParams = args;
        if (!OAString.isEmpty(s) && getExtraWhereFilter() == null) {
            OAFilter<Warning> f = new OAQueryFilter<Warning>(Warning.class, s, args);
            setExtraWhereFilter(f);
        }
    }
    public void setExtraWhereFilter(OAFilter<Warning> filter) {
        this.filterExtraWhere = filter;
    }
    public OAFilter<Warning> getExtraWhereFilter() {
        return this.filterExtraWhere;
    }

    public OASelect<Warning> getSelect() {
        String sql = "";
        String sortOrder = "";
        Object[] args = new Object[0];

        if (!OAString.isEmpty(extraWhere)) {
            if (sql.length() > 0) sql = "(" + sql + ") AND ";
            sql += extraWhere;
            args = OAArray.add(Object.class, args, extraWhereParams);
        }

        OASelect<Warning> sel = new OASelect<Warning>(Warning.class, sql, args, sortOrder);
        sel.setDataSourceFilter(this.getDataSourceFilter());
        sel.setFilter(this.getCustomFilter());
        return sel;
    }

    private OAFilter<Warning> filterDataSourceFilter;
    public OAFilter<Warning> getDataSourceFilter() {
        if (filterDataSourceFilter != null) return filterDataSourceFilter;
        filterDataSourceFilter = new OAFilter<Warning>() {
            @Override
            public boolean isUsed(Warning warning) {
                return WarningSearch.this.isUsedForDataSourceFilter(warning);
            }
        };
        return filterDataSourceFilter;
    }
    
    private OAFilter<Warning> filterCustomFilter;
    public OAFilter<Warning> getCustomFilter() {
        if (filterCustomFilter != null) return filterCustomFilter;
        filterCustomFilter = new OAFilter<Warning>() {
            @Override
            public boolean isUsed(Warning warning) {
                boolean b = WarningSearch.this.isUsedForCustomFilter(warning);
                if (b && filterExtraWhere != null) b = filterExtraWhere.isUsed(warning);
                return b;
            }
        };
        return filterCustomFilter;
    }
    
    public boolean isUsedForDataSourceFilter(Warning warning) {
        return true;
    }
    public boolean isUsedForCustomFilter(Warning warning) {
        return true;
    }
}