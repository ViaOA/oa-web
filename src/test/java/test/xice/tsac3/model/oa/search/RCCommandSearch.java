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
public class RCCommandSearch extends OAObject {
    private static final long serialVersionUID = 1L;



    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }

    protected String extraWhere;
    protected Object[] extraWhereParams;
    protected OAFilter<RCCommand> filterExtraWhere;

    public void setExtraWhere(String s, Object ... args) {
        this.extraWhere = s;
        this.extraWhereParams = args;
        if (!OAString.isEmpty(s) && getExtraWhereFilter() == null) {
            OAFilter<RCCommand> f = new OAQueryFilter<RCCommand>(RCCommand.class, s, args);
            setExtraWhereFilter(f);
        }
    }
    public void setExtraWhereFilter(OAFilter<RCCommand> filter) {
        this.filterExtraWhere = filter;
    }
    public OAFilter<RCCommand> getExtraWhereFilter() {
        return this.filterExtraWhere;
    }

    public OASelect<RCCommand> getSelect() {
        String sql = "";
        String sortOrder = "";
        Object[] args = new Object[0];

        if (!OAString.isEmpty(extraWhere)) {
            if (sql.length() > 0) sql = "(" + sql + ") AND ";
            sql += extraWhere;
            args = OAArray.add(Object.class, args, extraWhereParams);
        }

        OASelect<RCCommand> sel = new OASelect<RCCommand>(RCCommand.class, sql, args, sortOrder);
        sel.setDataSourceFilter(this.getDataSourceFilter());
        sel.setFilter(this.getCustomFilter());
        return sel;
    }

    private OAFilter<RCCommand> filterDataSourceFilter;
    public OAFilter<RCCommand> getDataSourceFilter() {
        if (filterDataSourceFilter != null) return filterDataSourceFilter;
        filterDataSourceFilter = new OAFilter<RCCommand>() {
            @Override
            public boolean isUsed(RCCommand rcCommand) {
                return RCCommandSearch.this.isUsedForDataSourceFilter(rcCommand);
            }
        };
        return filterDataSourceFilter;
    }
    
    private OAFilter<RCCommand> filterCustomFilter;
    public OAFilter<RCCommand> getCustomFilter() {
        if (filterCustomFilter != null) return filterCustomFilter;
        filterCustomFilter = new OAFilter<RCCommand>() {
            @Override
            public boolean isUsed(RCCommand rcCommand) {
                boolean b = RCCommandSearch.this.isUsedForCustomFilter(rcCommand);
                if (b && filterExtraWhere != null) b = filterExtraWhere.isUsed(rcCommand);
                return b;
            }
        };
        return filterCustomFilter;
    }
    
    public boolean isUsedForDataSourceFilter(RCCommand rcCommand) {
        return true;
    }
    public boolean isUsedForCustomFilter(RCCommand rcCommand) {
        return true;
    }
}
