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
public class ServerTypeVersionSearch extends OAObject {
    private static final long serialVersionUID = 1L;



    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }

    protected String extraWhere;
    protected Object[] extraWhereParams;
    protected OAFilter<ServerTypeVersion> filterExtraWhere;

    public void setExtraWhere(String s, Object ... args) {
        this.extraWhere = s;
        this.extraWhereParams = args;
        if (!OAString.isEmpty(s) && getExtraWhereFilter() == null) {
            OAFilter<ServerTypeVersion> f = new OAQueryFilter<ServerTypeVersion>(ServerTypeVersion.class, s, args);
            setExtraWhereFilter(f);
        }
    }
    public void setExtraWhereFilter(OAFilter<ServerTypeVersion> filter) {
        this.filterExtraWhere = filter;
    }
    public OAFilter<ServerTypeVersion> getExtraWhereFilter() {
        return this.filterExtraWhere;
    }

    public OASelect<ServerTypeVersion> getSelect() {
        String sql = "";
        String sortOrder = "";
        Object[] args = new Object[0];

        if (!OAString.isEmpty(extraWhere)) {
            if (sql.length() > 0) sql = "(" + sql + ") AND ";
            sql += extraWhere;
            args = OAArray.add(Object.class, args, extraWhereParams);
        }

        OASelect<ServerTypeVersion> sel = new OASelect<ServerTypeVersion>(ServerTypeVersion.class, sql, args, sortOrder);
        sel.setDataSourceFilter(this.getDataSourceFilter());
        sel.setFilter(this.getCustomFilter());
        return sel;
    }

    private OAFilter<ServerTypeVersion> filterDataSourceFilter;
    public OAFilter<ServerTypeVersion> getDataSourceFilter() {
        if (filterDataSourceFilter != null) return filterDataSourceFilter;
        filterDataSourceFilter = new OAFilter<ServerTypeVersion>() {
            @Override
            public boolean isUsed(ServerTypeVersion serverTypeVersion) {
                return ServerTypeVersionSearch.this.isUsedForDataSourceFilter(serverTypeVersion);
            }
        };
        return filterDataSourceFilter;
    }
    
    private OAFilter<ServerTypeVersion> filterCustomFilter;
    public OAFilter<ServerTypeVersion> getCustomFilter() {
        if (filterCustomFilter != null) return filterCustomFilter;
        filterCustomFilter = new OAFilter<ServerTypeVersion>() {
            @Override
            public boolean isUsed(ServerTypeVersion serverTypeVersion) {
                boolean b = ServerTypeVersionSearch.this.isUsedForCustomFilter(serverTypeVersion);
                if (b && filterExtraWhere != null) b = filterExtraWhere.isUsed(serverTypeVersion);
                return b;
            }
        };
        return filterCustomFilter;
    }
    
    public boolean isUsedForDataSourceFilter(ServerTypeVersion serverTypeVersion) {
        return true;
    }
    public boolean isUsedForCustomFilter(ServerTypeVersion serverTypeVersion) {
        return true;
    }
}