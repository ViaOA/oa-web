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
public class ClientAppTypeSearch extends OAObject {
    private static final long serialVersionUID = 1L;



    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }

    protected String extraWhere;
    protected Object[] extraWhereParams;
    protected OAFilter<ClientAppType> filterExtraWhere;

    public void setExtraWhere(String s, Object ... args) {
        this.extraWhere = s;
        this.extraWhereParams = args;
        if (!OAString.isEmpty(s) && getExtraWhereFilter() == null) {
            OAFilter<ClientAppType> f = new OAQueryFilter<ClientAppType>(ClientAppType.class, s, args);
            setExtraWhereFilter(f);
        }
    }
    public void setExtraWhereFilter(OAFilter<ClientAppType> filter) {
        this.filterExtraWhere = filter;
    }
    public OAFilter<ClientAppType> getExtraWhereFilter() {
        return this.filterExtraWhere;
    }

    public OASelect<ClientAppType> getSelect() {
        String sql = "";
        String sortOrder = ClientAppType.P_Seq;
        Object[] args = new Object[0];

        if (!OAString.isEmpty(extraWhere)) {
            if (sql.length() > 0) sql = "(" + sql + ") AND ";
            sql += extraWhere;
            args = OAArray.add(Object.class, args, extraWhereParams);
        }

        OASelect<ClientAppType> sel = new OASelect<ClientAppType>(ClientAppType.class, sql, args, sortOrder);
        sel.setDataSourceFilter(this.getDataSourceFilter());
        sel.setFilter(this.getCustomFilter());
        return sel;
    }

    private OAFilter<ClientAppType> filterDataSourceFilter;
    public OAFilter<ClientAppType> getDataSourceFilter() {
        if (filterDataSourceFilter != null) return filterDataSourceFilter;
        filterDataSourceFilter = new OAFilter<ClientAppType>() {
            @Override
            public boolean isUsed(ClientAppType clientAppType) {
                return ClientAppTypeSearch.this.isUsedForDataSourceFilter(clientAppType);
            }
        };
        return filterDataSourceFilter;
    }
    
    private OAFilter<ClientAppType> filterCustomFilter;
    public OAFilter<ClientAppType> getCustomFilter() {
        if (filterCustomFilter != null) return filterCustomFilter;
        filterCustomFilter = new OAFilter<ClientAppType>() {
            @Override
            public boolean isUsed(ClientAppType clientAppType) {
                boolean b = ClientAppTypeSearch.this.isUsedForCustomFilter(clientAppType);
                if (b && filterExtraWhere != null) b = filterExtraWhere.isUsed(clientAppType);
                return b;
            }
        };
        return filterCustomFilter;
    }
    
    public boolean isUsedForDataSourceFilter(ClientAppType clientAppType) {
        return true;
    }
    public boolean isUsedForCustomFilter(ClientAppType clientAppType) {
        return true;
    }
}
