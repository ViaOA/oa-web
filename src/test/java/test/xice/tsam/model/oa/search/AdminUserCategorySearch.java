// Generated by OABuilder
package test.xice.tsam.model.oa.search;

import java.util.logging.*;

import test.xice.tsam.model.oa.AdminUserCategory;
import test.xice.tsam.model.oa.search.AdminUserCategorySearch;
import com.viaoa.annotation.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.ds.*;
import com.viaoa.util.filter.OAQueryFilter;

import test.xice.tsam.model.oa.*;
import test.xice.tsam.model.oa.propertypath.*;

@OAClass(useDataSource=false, localOnly=true)
public class AdminUserCategorySearch extends OAObject {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(AdminUserCategorySearch.class.getName());


    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }

    protected String extraWhere;
    protected Object[] extraWhereParams;
    protected OAFilter<AdminUserCategory> filterExtraWhere;

    public void setExtraWhere(String s, Object ... args) {
        this.extraWhere = s;
        this.extraWhereParams = args;
        if (!OAString.isEmpty(s) && getExtraWhereFilter() == null) {
            OAFilter<AdminUserCategory> f = new OAQueryFilter<AdminUserCategory>(AdminUserCategory.class, s, args);
            setExtraWhereFilter(f);
        }
    }
    public void setExtraWhereFilter(OAFilter<AdminUserCategory> filter) {
        this.filterExtraWhere = filter;
    }
    public OAFilter<AdminUserCategory> getExtraWhereFilter() {
        return this.filterExtraWhere;
    }

    public OASelect<AdminUserCategory> getSelect() {
        String sql = "";
        String sortOrder = "";
        Object[] args = new Object[0];

        if (!OAString.isEmpty(extraWhere)) {
            if (sql.length() > 0) sql = "(" + sql + ") AND ";
            sql += extraWhere;
            args = OAArray.add(Object.class, args, extraWhereParams);
        }

        OASelect<AdminUserCategory> sel = new OASelect<AdminUserCategory>(AdminUserCategory.class, sql, args, sortOrder);
        sel.setDataSourceFilter(this.getDataSourceFilter());
        sel.setFilter(this.getCustomFilter());
        return sel;
    }

    private OAFilter<AdminUserCategory> filterDataSourceFilter;
    public OAFilter<AdminUserCategory> getDataSourceFilter() {
        if (filterDataSourceFilter != null) return filterDataSourceFilter;
        filterDataSourceFilter = new OAFilter<AdminUserCategory>() {
            @Override
            public boolean isUsed(AdminUserCategory adminUserCategory) {
                return AdminUserCategorySearch.this.isUsedForDataSourceFilter(adminUserCategory);
            }
        };
        return filterDataSourceFilter;
    }
    
    private OAFilter<AdminUserCategory> filterCustomFilter;
    public OAFilter<AdminUserCategory> getCustomFilter() {
        if (filterCustomFilter != null) return filterCustomFilter;
        filterCustomFilter = new OAFilter<AdminUserCategory>() {
            @Override
            public boolean isUsed(AdminUserCategory adminUserCategory) {
                boolean b = AdminUserCategorySearch.this.isUsedForCustomFilter(adminUserCategory);
                if (b && filterExtraWhere != null) b = filterExtraWhere.isUsed(adminUserCategory);
                return b;
            }
        };
        return filterCustomFilter;
    }
    
    public boolean isUsedForDataSourceFilter(AdminUserCategory adminUserCategory) {
        return true;
    }
    public boolean isUsedForCustomFilter(AdminUserCategory adminUserCategory) {
        return true;
    }
}
