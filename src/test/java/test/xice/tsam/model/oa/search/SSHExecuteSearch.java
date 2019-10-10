// Generated by OABuilder
package test.xice.tsam.model.oa.search;

import java.util.logging.*;

import test.xice.tsam.model.oa.SSHExecute;
import test.xice.tsam.model.oa.search.SSHExecuteSearch;
import com.viaoa.annotation.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.ds.*;
import com.viaoa.util.filter.OAQueryFilter;

import test.xice.tsam.model.oa.*;
import test.xice.tsam.model.oa.propertypath.*;


@OAClass(useDataSource=false, localOnly=true)
public class SSHExecuteSearch extends OAObject {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(SSHExecuteSearch.class.getName());


    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }

    protected String extraWhere;
    protected Object[] extraWhereParams;
    protected OAFilter<SSHExecute> filterExtraWhere;

    public void setExtraWhere(String s, Object ... args) {
        this.extraWhere = s;
        this.extraWhereParams = args;
        if (!OAString.isEmpty(s) && getExtraWhereFilter() == null) {
            OAFilter<SSHExecute> f = new OAQueryFilter<SSHExecute>(SSHExecute.class, s, args);
            setExtraWhereFilter(f);
        }
    }
    public void setExtraWhereFilter(OAFilter<SSHExecute> filter) {
        this.filterExtraWhere = filter;
    }
    public OAFilter<SSHExecute> getExtraWhereFilter() {
        return this.filterExtraWhere;
    }

    public OASelect<SSHExecute> getSelect() {
        String sql = "";
        String sortOrder = "";
        Object[] args = new Object[0];

        if (!OAString.isEmpty(extraWhere)) {
            if (sql.length() > 0) sql = "(" + sql + ") AND ";
            sql += extraWhere;
            args = OAArray.add(Object.class, args, extraWhereParams);
        }

        OASelect<SSHExecute> sel = new OASelect<SSHExecute>(SSHExecute.class, sql, args, sortOrder);
        sel.setDataSourceFilter(this.getDataSourceFilter());
        sel.setFilter(this.getCustomFilter());
        return sel;
    }

    private OAFilter<SSHExecute> filterDataSourceFilter;
    public OAFilter<SSHExecute> getDataSourceFilter() {
        if (filterDataSourceFilter != null) return filterDataSourceFilter;
        filterDataSourceFilter = new OAFilter<SSHExecute>() {
            @Override
            public boolean isUsed(SSHExecute sshExecute) {
                return SSHExecuteSearch.this.isUsedForDataSourceFilter(sshExecute);
            }
        };
        return filterDataSourceFilter;
    }
    
    private OAFilter<SSHExecute> filterCustomFilter;
    public OAFilter<SSHExecute> getCustomFilter() {
        if (filterCustomFilter != null) return filterCustomFilter;
        filterCustomFilter = new OAFilter<SSHExecute>() {
            @Override
            public boolean isUsed(SSHExecute sshExecute) {
                boolean b = SSHExecuteSearch.this.isUsedForCustomFilter(sshExecute);
                if (b && filterExtraWhere != null) b = filterExtraWhere.isUsed(sshExecute);
                return b;
            }
        };
        return filterCustomFilter;
    }
    
    public boolean isUsedForDataSourceFilter(SSHExecute sshExecute) {
        return true;
    }
    public boolean isUsedForCustomFilter(SSHExecute sshExecute) {
        return true;
    }
}