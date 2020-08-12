// Generated by OABuilder
package test.xice.tsam.model.oa.search;

import java.util.logging.*;

import test.xice.tsam.model.oa.SiloType;
import test.xice.tsam.model.oa.search.SiloTypeSearch;
import com.viaoa.annotation.*;
import com.viaoa.datasource.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.filter.OAQueryFilter;

import test.xice.tsam.model.oa.*;
import test.xice.tsam.model.oa.propertypath.*;

@OAClass(useDataSource=false, localOnly=true)
public class SiloTypeSearch extends OAObject {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(SiloTypeSearch.class.getName());
    public static final String P_Name = "Name";
    public static final String P_Type = "Type";

    protected String name;
    protected int type;

    public String getName() {
        return name;
    }
    public void setName(String newValue) {
        fireBeforePropertyChange(P_Name, this.name, newValue);
        String old = name;
        this.name = newValue;
        firePropertyChange(P_Name, old, this.name);
    }
    
      

    public int getType() {
        return type;
    }
    public void setType(int newValue) {
        fireBeforePropertyChange(P_Type, this.type, newValue);
        int old = type;
        this.type = newValue;
        firePropertyChange(P_Type, old, this.type);
    }
    
      

    public void reset() {
        setName(null);
        setType(0);
        setNull(P_Type);
    }

    public boolean isDataEntered() {
        if (getName() != null) return true;
        if (!isNull(P_Type)) return true;
        return false;
    }

    protected String extraWhere;
    protected Object[] extraWhereParams;
    protected OAFilter<SiloType> filterExtraWhere;

    public void setExtraWhere(String s, Object ... args) {
        this.extraWhere = s;
        this.extraWhereParams = args;
        if (!OAString.isEmpty(s) && getExtraWhereFilter() == null) {
            OAFilter<SiloType> f = new OAQueryFilter<SiloType>(SiloType.class, s, args);
            setExtraWhereFilter(f);
        }
    }
    public void setExtraWhereFilter(OAFilter<SiloType> filter) {
        this.filterExtraWhere = filter;
    }
    public OAFilter<SiloType> getExtraWhereFilter() {
        return this.filterExtraWhere;
    }

    public OASelect<SiloType> getSelect() {
        String sql = "";
        String sortOrder = "";
        Object[] args = new Object[0];
        if (!OAString.isEmpty(this.name)) {
            if (sql.length() > 0) sql += " AND ";
            String value = name.replace("*", "%");
            if (!value.endsWith("%")) value += "%";
            if (value.indexOf("%") >= 0) {
                value = value.toUpperCase();
                sql += SiloType.P_Name + " LIKE ?";
            }
            else {
                sql += SiloType.P_Name + " = ?";
            }
            args = OAArray.add(Object.class, args, value);
        }
        if (!isNull(P_Type)) {
            if (sql.length() > 0) sql += " AND ";
            sql += SiloType.P_Type + " = ?";
            args = OAArray.add(Object.class, args, this.type);
        }

        if (!OAString.isEmpty(extraWhere)) {
            if (sql.length() > 0) sql = "(" + sql + ") AND ";
            sql += extraWhere;
            args = OAArray.add(Object.class, args, extraWhereParams);
        }

        OASelect<SiloType> sel = new OASelect<SiloType>(SiloType.class, sql, args, sortOrder);
        sel.setDataSourceFilter(this.getDataSourceFilter());
        sel.setFilter(this.getCustomFilter());
        return sel;
    }

    private OAFilter<SiloType> filterDataSourceFilter;
    public OAFilter<SiloType> getDataSourceFilter() {
        if (filterDataSourceFilter != null) return filterDataSourceFilter;
        filterDataSourceFilter = new OAFilter<SiloType>() {
            @Override
            public boolean isUsed(SiloType siloType) {
                return SiloTypeSearch.this.isUsedForDataSourceFilter(siloType);
            }
        };
        return filterDataSourceFilter;
    }
    
    private OAFilter<SiloType> filterCustomFilter;
    public OAFilter<SiloType> getCustomFilter() {
        if (filterCustomFilter != null) return filterCustomFilter;
        filterCustomFilter = new OAFilter<SiloType>() {
            @Override
            public boolean isUsed(SiloType siloType) {
                boolean b = SiloTypeSearch.this.isUsedForCustomFilter(siloType);
                if (b && filterExtraWhere != null) b = filterExtraWhere.isUsed(siloType);
                return b;
            }
        };
        return filterCustomFilter;
    }
    
    public boolean isUsedForDataSourceFilter(SiloType siloType) {
        if (name != null) {
            String s = getName();
            if (s != null && s.indexOf('*') < 0 && s.indexOf('%') < 0) s += '*';
            if (!OACompare.isLike(siloType.getName(), s)) return false;
        }
        if (!isNull(P_Type)) {
            if (!OACompare.isEqual(siloType.getType(), type)) return false;
        }
        return true;
    }
    public boolean isUsedForCustomFilter(SiloType siloType) {
        return true;
    }
}
