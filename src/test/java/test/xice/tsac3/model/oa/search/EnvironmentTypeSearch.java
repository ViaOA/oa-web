// Generated by OABuilder
package test.xice.tsac3.model.oa.search;

import com.viaoa.annotation.*;
import com.viaoa.datasource.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.xice.tsac3.model.oa.*;

import com.viaoa.filter.OAQueryFilter;

@OAClass(useDataSource=false, localOnly=true)
public class EnvironmentTypeSearch extends OAObject {
    private static final long serialVersionUID = 1L;



    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }

    protected String extraWhere;
    protected Object[] extraWhereParams;
    protected OAFilter<EnvironmentType> filterExtraWhere;

    public void setExtraWhere(String s, Object ... args) {
        this.extraWhere = s;
        this.extraWhereParams = args;
        if (!OAString.isEmpty(s) && getExtraWhereFilter() == null) {
            OAFilter<EnvironmentType> f = new OAQueryFilter<EnvironmentType>(EnvironmentType.class, s, args);
            setExtraWhereFilter(f);
        }
    }
    public void setExtraWhereFilter(OAFilter<EnvironmentType> filter) {
        this.filterExtraWhere = filter;
    }
    public OAFilter<EnvironmentType> getExtraWhereFilter() {
        return this.filterExtraWhere;
    }

    public OASelect<EnvironmentType> getSelect() {
        String sql = "";
        String sortOrder = "";
        Object[] args = new Object[0];

        if (!OAString.isEmpty(extraWhere)) {
            if (sql.length() > 0) sql = "(" + sql + ") AND ";
            sql += extraWhere;
            args = OAArray.add(Object.class, args, extraWhereParams);
        }

        OASelect<EnvironmentType> sel = new OASelect<EnvironmentType>(EnvironmentType.class, sql, args, sortOrder);
        sel.setDataSourceFilter(this.getDataSourceFilter());
        sel.setFilter(this.getCustomFilter());
        return sel;
    }

    private OAFilter<EnvironmentType> filterDataSourceFilter;
    public OAFilter<EnvironmentType> getDataSourceFilter() {
        if (filterDataSourceFilter != null) return filterDataSourceFilter;
        filterDataSourceFilter = new OAFilter<EnvironmentType>() {
            @Override
            public boolean isUsed(EnvironmentType environmentType) {
                return EnvironmentTypeSearch.this.isUsedForDataSourceFilter(environmentType);
            }
        };
        return filterDataSourceFilter;
    }
    
    private OAFilter<EnvironmentType> filterCustomFilter;
    public OAFilter<EnvironmentType> getCustomFilter() {
        if (filterCustomFilter != null) return filterCustomFilter;
        filterCustomFilter = new OAFilter<EnvironmentType>() {
            @Override
            public boolean isUsed(EnvironmentType environmentType) {
                boolean b = EnvironmentTypeSearch.this.isUsedForCustomFilter(environmentType);
                if (b && filterExtraWhere != null) b = filterExtraWhere.isUsed(environmentType);
                return b;
            }
        };
        return filterCustomFilter;
    }
    
    public boolean isUsedForDataSourceFilter(EnvironmentType environmentType) {
        return true;
    }
    public boolean isUsedForCustomFilter(EnvironmentType environmentType) {
        return true;
    }
}
