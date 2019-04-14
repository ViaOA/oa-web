// Generated by OABuilder
package test.hifive.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.annotation.*;
import com.viaoa.util.OADate;

import test.hifive.model.oa.filter.*;
import test.hifive.model.oa.propertypath.*;
 
@OAClass(
    shortName = "ho",
    displayName = "Hi5 Order",
    displayProperty = "created"
)
@OATable(
    indexes = {
        @OAIndex(name = "HifiveOrderEmployee", columns = { @OAIndexColumn(name = "EmployeeId") })
    }
)
public class HifiveOrder extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_ProcessDate = "ProcessDate";
    public static final String P_ProcessDate = "ProcessDate";
    public static final String PROPERTY_MediaPointsUsed = "MediaPointsUsed";
    public static final String P_MediaPointsUsed = "MediaPointsUsed";
    public static final String PROPERTY_MediaNotes = "MediaNotes";
    public static final String P_MediaNotes = "MediaNotes";
     
     
    public static final String PROPERTY_Employee = "Employee";
    public static final String P_Employee = "Employee";
    public static final String PROPERTY_HifiveOrderCards = "HifiveOrderCards";
    public static final String P_HifiveOrderCards = "HifiveOrderCards";
    public static final String PROPERTY_HifiveOrderItems = "HifiveOrderItems";
    public static final String P_HifiveOrderItems = "HifiveOrderItems";
    public static final String PROPERTY_HindaOrder = "HindaOrder";
    public static final String P_HindaOrder = "HindaOrder";
     
    protected int id;
    protected OADate created;
    protected OADate processDate;
    protected double mediaPointsUsed;
    protected String mediaNotes;
     
    // Links to other objects.
    protected transient Employee employee;
    protected transient Hub<HifiveOrderCard> hubHifiveOrderCards;
    protected transient Hub<HifiveOrderItem> hubHifiveOrderItems;
    protected transient HindaOrder hindaOrder;
     
    public HifiveOrder() {
        if (!isLoading()) {
            setCreated(new OADate());
        }
    }
     
    public HifiveOrder(int id) {
        this();
        setId(id);
    }
     
    @OAProperty(isUnique = true, displayLength = 5)
    @OAId()
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getId() {
        return id;
    }
    
    public void setId(int newValue) {
        fireBeforePropertyChange(P_Id, this.id, newValue);
        int old = id;
        this.id = newValue;
        firePropertyChange(P_Id, old, this.id);
    }
    @OAProperty(defaultValue = "new OADate()", displayLength = 8, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getCreated() {
        return created;
    }
    
    public void setCreated(OADate newValue) {
        fireBeforePropertyChange(P_Created, this.created, newValue);
        OADate old = created;
        this.created = newValue;
        firePropertyChange(P_Created, old, this.created);
    }
    @OAProperty(displayName = "Process Date", displayLength = 8, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getProcessDate() {
        return processDate;
    }
    
    public void setProcessDate(OADate newValue) {
        fireBeforePropertyChange(P_ProcessDate, this.processDate, newValue);
        OADate old = processDate;
        this.processDate = newValue;
        firePropertyChange(P_ProcessDate, old, this.processDate);
    }
    @OAProperty(displayName = "Media Points Used", decimalPlaces = 2, displayLength = 7)
    @OAColumn(sqlType = java.sql.Types.DOUBLE)
    public double getMediaPointsUsed() {
        return mediaPointsUsed;
    }
    
    public void setMediaPointsUsed(double newValue) {
        fireBeforePropertyChange(P_MediaPointsUsed, this.mediaPointsUsed, newValue);
        double old = mediaPointsUsed;
        this.mediaPointsUsed = newValue;
        firePropertyChange(P_MediaPointsUsed, old, this.mediaPointsUsed);
    }
    @OAProperty(displayName = "Media Notes", maxLength = 254, displayLength = 40)
    @OAColumn(maxLength = 254)
    public String getMediaNotes() {
        return mediaNotes;
    }
    
    public void setMediaNotes(String newValue) {
        fireBeforePropertyChange(P_MediaNotes, this.mediaNotes, newValue);
        String old = mediaNotes;
        this.mediaNotes = newValue;
        firePropertyChange(P_MediaNotes, old, this.mediaNotes);
    }
    @OAOne(
        reverseName = Employee.P_HifiveOrders, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"EmployeeId"})
    public Employee getEmployee() {
        if (employee == null) {
            employee = (Employee) getObject(P_Employee);
        }
        return employee;
    }
    
    public void setEmployee(Employee newValue) {
        fireBeforePropertyChange(P_Employee, this.employee, newValue);
        Employee old = this.employee;
        this.employee = newValue;
        firePropertyChange(P_Employee, old, this.employee);
    }
    
    @OAMany(
        displayName = "Hi5 Order Cards", 
        toClass = HifiveOrderCard.class, 
        owner = true, 
        reverseName = HifiveOrderCard.P_HifiveOrder, 
        cascadeSave = true, 
        cascadeDelete = true, 
        seqProperty = HifiveOrderCard.P_Seq, 
        sortProperty = HifiveOrderCard.P_Seq
    )
    public Hub<HifiveOrderCard> getHifiveOrderCards() {
        if (hubHifiveOrderCards == null) {
            hubHifiveOrderCards = (Hub<HifiveOrderCard>) getHub(P_HifiveOrderCards);
        }
        return hubHifiveOrderCards;
    }
    
    @OAMany(
        displayName = "Hifive Order Items", 
        toClass = HifiveOrderItem.class, 
        owner = true, 
        reverseName = HifiveOrderItem.P_HifiveOrder, 
        cascadeSave = true, 
        cascadeDelete = true, 
        mustBeEmptyForDelete = true, 
        seqProperty = HifiveOrderItem.P_Seq, 
        sortProperty = HifiveOrderItem.P_Seq
    )
    public Hub<HifiveOrderItem> getHifiveOrderItems() {
        if (hubHifiveOrderItems == null) {
            hubHifiveOrderItems = (Hub<HifiveOrderItem>) getHub(P_HifiveOrderItems);
        }
        return hubHifiveOrderItems;
    }
    
    @OAOne(
        displayName = "Hinda Order", 
        reverseName = HindaOrder.P_HifiveOrder
    )
    public HindaOrder getHindaOrder() {
        if (hindaOrder == null) {
            hindaOrder = (HindaOrder) getObject(P_HindaOrder);
        }
        return hindaOrder;
    }
    
    public void setHindaOrder(HindaOrder newValue) {
        fireBeforePropertyChange(P_HindaOrder, this.hindaOrder, newValue);
        HindaOrder old = this.hindaOrder;
        this.hindaOrder = newValue;
        firePropertyChange(P_HindaOrder, old, this.hindaOrder);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Date date;
        date = rs.getDate(2);
        if (date != null) this.created = new OADate(date);
        date = rs.getDate(3);
        if (date != null) this.processDate = new OADate(date);
        this.mediaPointsUsed = (double) rs.getDouble(4);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, HifiveOrder.P_MediaPointsUsed, true);
        }
        this.mediaNotes = rs.getString(5);
        int employeeFkey = rs.getInt(6);
        if (!rs.wasNull() && employeeFkey > 0) {
            setProperty(P_Employee, new OAObjectKey(employeeFkey));
        }
        if (rs.getMetaData().getColumnCount() != 6) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
