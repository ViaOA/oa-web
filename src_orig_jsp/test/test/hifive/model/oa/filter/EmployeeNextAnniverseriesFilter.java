// Generated by OABuilder
package test.hifive.model.oa.filter;

import com.viaoa.annotation.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.hifive.model.oa.*;

import java.util.*;

@OAClass(useDataSource=false, localOnly=true)
@OAClassFilter(name = "NextAnniverseries", displayName = "Next Anniverseries", hasInputParams = false)
public class EmployeeNextAnniverseriesFilter extends OAObject implements CustomHubFilter<Employee> {
    private static final long serialVersionUID = 1L;


    public static final String PPCode = ":NextAnniverseries()";
    private Hub<Employee> hubMaster;
    private Hub<Employee> hub;
    private HubFilter<Employee> filter;
    private boolean bAllHubs;

    public EmployeeNextAnniverseriesFilter(Hub<Employee> hub) {
        this(true, null, hub);
    }
    public EmployeeNextAnniverseriesFilter(Hub<Employee> hubMaster, Hub<Employee> hub) {
        this(false, hubMaster, hub);
    }
    public EmployeeNextAnniverseriesFilter(boolean bAllHubs, Hub<Employee> hubMaster, Hub<Employee> hubFiltered) {
        this.hubMaster = hubMaster;
        this.hub = hubFiltered;
        if (hubMaster == null) this.hubMaster = new Hub<Employee>(Employee.class);
        this.bAllHubs = bAllHubs;
        getHubFilter(); // create filter
    }


    public void reset() {
    }

    public boolean isDataEntered() {
        return false;
    }
    public void refresh() {
        if (filter != null) getHubFilter().refresh();
    }

    @Override
    public HubFilter<Employee> getHubFilter() {
        if (filter == null) {
            filter = createHubFilter(hubMaster, hub, bAllHubs);
        }
        return filter;
    }
    protected HubFilter<Employee> createHubFilter(final Hub<Employee> hubMaster, Hub<Employee> hub, boolean bAllHubs) {
        HubFilter<Employee> filter = new HubFilter<Employee>(hubMaster, hub) {
            @Override
            public boolean isUsed(Employee employee) {
                return EmployeeNextAnniverseriesFilter.this.isUsed(employee);
            }
        };
        filter.addDependentProperty(Employee.P_HireDate);
        filter.addDependentProperty(OAString.cpp(Employee.P_Location, Location.P_Program, Program.P_BirthdayDisplayDays));
 
        if (!bAllHubs) return filter;
        // need to listen to all Employee
        OAObjectCacheHubAdder hubCacheAdder = new OAObjectCacheHubAdder(hubMaster);
        return filter;
    }

    public boolean isUsed(Employee employee) {
        OADate hireDate = employee.getHireDate();
        if (hireDate == null) return false;
    
        int amt = 0;
        Program program = employee.getProgram();
        if (program != null) {
            amt = program.getAnniversaryDisplayDays();
        }
        if (amt < 1) amt = 30;
        
        OADate d = employee.getNextAnniversaryDate();
        if (d == null) return false;
        OADate today = new OADate();
        return (today.before(d) && d.before(today.addDays(amt)));
    }
}