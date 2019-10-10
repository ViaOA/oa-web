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
    shortName = "io",
    displayName = "Inspire Order",
    displayProperty = "created"
)
@OATable(
    indexes = {
        @OAIndex(name = "InspireOrderEmployee", columns = { @OAIndexColumn(name = "EmployeeId") })
    }
)
public class InspireOrder extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_BillDate = "BillDate";
    public static final String P_BillDate = "BillDate";
    public static final String PROPERTY_PaidDate = "PaidDate";
    public static final String P_PaidDate = "PaidDate";
    public static final String PROPERTY_CompletedDate = "CompletedDate";
    public static final String P_CompletedDate = "CompletedDate";
    public static final String PROPERTY_CashSelectedDate = "CashSelectedDate";
    public static final String P_CashSelectedDate = "CashSelectedDate";
    public static final String PROPERTY_CashAmount = "CashAmount";
    public static final String P_CashAmount = "CashAmount";
    public static final String PROPERTY_CashPointsUsed = "CashPointsUsed";
    public static final String P_CashPointsUsed = "CashPointsUsed";
    public static final String PROPERTY_CashSentDate = "CashSentDate";
    public static final String P_CashSentDate = "CashSentDate";
    public static final String PROPERTY_InternationalVisaSelectedDate = "InternationalVisaSelectedDate";
    public static final String P_InternationalVisaSelectedDate = "InternationalVisaSelectedDate";
    public static final String PROPERTY_InternationalVisaAmount = "InternationalVisaAmount";
    public static final String P_InternationalVisaAmount = "InternationalVisaAmount";
    public static final String PROPERTY_InternationalVisaPointsUsed = "InternationalVisaPointsUsed";
    public static final String P_InternationalVisaPointsUsed = "InternationalVisaPointsUsed";
    public static final String PROPERTY_InternationalVisaSentDate = "InternationalVisaSentDate";
    public static final String P_InternationalVisaSentDate = "InternationalVisaSentDate";
    public static final String PROPERTY_PointsOrdered = "PointsOrdered";
    public static final String P_PointsOrdered = "PointsOrdered";
    public static final String PROPERTY_CashInvoiceNumber = "CashInvoiceNumber";
    public static final String P_CashInvoiceNumber = "CashInvoiceNumber";
    public static final String PROPERTY_CashInvoiceDate = "CashInvoiceDate";
    public static final String P_CashInvoiceDate = "CashInvoiceDate";
    public static final String PROPERTY_InternationalVisaInvoiceNumber = "InternationalVisaInvoiceNumber";
    public static final String P_InternationalVisaInvoiceNumber = "InternationalVisaInvoiceNumber";
    public static final String PROPERTY_InternationalVisaInvoiceDate = "InternationalVisaInvoiceDate";
    public static final String P_InternationalVisaInvoiceDate = "InternationalVisaInvoiceDate";
    public static final String PROPERTY_InternationalVisaVendorInvoiced = "InternationalVisaVendorInvoiced";
    public static final String P_InternationalVisaVendorInvoiced = "InternationalVisaVendorInvoiced";
     
    public static final String PROPERTY_PointsUsed = "PointsUsed";
    public static final String P_PointsUsed = "PointsUsed";
    public static final String PROPERTY_NumberOfCartItems = "NumberOfCartItems";
    public static final String P_NumberOfCartItems = "NumberOfCartItems";
     
    public static final String PROPERTY_AwardCardOrders = "AwardCardOrders";
    public static final String P_AwardCardOrders = "AwardCardOrders";
    public static final String PROPERTY_Emails = "Emails";
    public static final String P_Emails = "Emails";
    public static final String PROPERTY_Employee = "Employee";
    public static final String P_Employee = "Employee";
    public static final String PROPERTY_InspireOrderCharities = "InspireOrderCharities";
    public static final String P_InspireOrderCharities = "InspireOrderCharities";
    public static final String PROPERTY_InspireOrderItems = "InspireOrderItems";
    public static final String P_InspireOrderItems = "InspireOrderItems";
    public static final String PROPERTY_PointsRecord = "PointsRecord";
    public static final String P_PointsRecord = "PointsRecord";
    public static final String PROPERTY_ShipTo = "ShipTo";
    public static final String P_ShipTo = "ShipTo";
     
    protected int id;
    protected OADate created;
    protected OADate billDate;
    protected OADate paidDate;
    protected OADate completedDate;
    protected OADate cashSelectedDate;
    protected double cashAmount;
    protected double cashPointsUsed;
    protected OADate cashSentDate;
    protected OADate internationalVisaSelectedDate;
    protected double internationalVisaAmount;
    protected double internationalVisaPointsUsed;
    protected OADate internationalVisaSentDate;
    protected int pointsOrdered;
    protected String cashInvoiceNumber;
    protected OADate cashInvoiceDate;
    protected String internationalVisaInvoiceNumber;
    protected OADate internationalVisaInvoiceDate;
    protected boolean internationalVisaVendorInvoiced;
     
    // Links to other objects.
    protected transient Hub<AwardCardOrder> hubAwardCardOrders;
    protected transient Hub<Email> hubEmails;
    protected transient Employee employee;
    protected transient Hub<InspireOrderCharity> hubInspireOrderCharities;
    protected transient Hub<InspireOrderItem> hubInspireOrderItems;
    protected transient ShipTo shipTo;
     
    public InspireOrder() {
        if (!isLoading()) {
            setCreated(new OADate());
            setPointsOrdered(0);
            setShipTo(new ShipTo());
        }
    }
     
    public InspireOrder(int id) {
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
    @OAProperty(displayName = "Bill Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getBillDate() {
        return billDate;
    }
    
    public void setBillDate(OADate newValue) {
        fireBeforePropertyChange(P_BillDate, this.billDate, newValue);
        OADate old = billDate;
        this.billDate = newValue;
        firePropertyChange(P_BillDate, old, this.billDate);
    }
    @OAProperty(displayName = "Paid Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getPaidDate() {
        return paidDate;
    }
    
    public void setPaidDate(OADate newValue) {
        fireBeforePropertyChange(P_PaidDate, this.paidDate, newValue);
        OADate old = paidDate;
        this.paidDate = newValue;
        firePropertyChange(P_PaidDate, old, this.paidDate);
    }
    @OAProperty(displayName = "Completed Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getCompletedDate() {
        return completedDate;
    }
    
    public void setCompletedDate(OADate newValue) {
        fireBeforePropertyChange(P_CompletedDate, this.completedDate, newValue);
        OADate old = completedDate;
        this.completedDate = newValue;
        firePropertyChange(P_CompletedDate, old, this.completedDate);
    }
    @OAProperty(displayName = "Cash Selected Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getCashSelectedDate() {
        return cashSelectedDate;
    }
    
    public void setCashSelectedDate(OADate newValue) {
        fireBeforePropertyChange(P_CashSelectedDate, this.cashSelectedDate, newValue);
        OADate old = cashSelectedDate;
        this.cashSelectedDate = newValue;
        firePropertyChange(P_CashSelectedDate, old, this.cashSelectedDate);
    }
    @OAProperty(displayName = "Cash Amount", decimalPlaces = 2, isCurrency = true, displayLength = 7, hasCustomCode = true)
    @OAColumn(sqlType = java.sql.Types.DOUBLE)
    public double getCashAmount() {
        return cashAmount;
    }
    public void setCashAmount(double newValue) {
        double old = cashAmount;
        fireBeforePropertyChange(PROPERTY_CashAmount, old, newValue);
        this.cashAmount = newValue;
        firePropertyChange(PROPERTY_CashAmount, old, this.cashAmount);
        // custom
        if (isLoading()) return;
        if (!isServer()) return;
        
        Employee emp = getEmployee();
        if (emp != null) {
            double x = 0;//EmployeeDelegate.getPointValueForCash(emp, this.cashAmount);
            setCashPointsUsed(x);
        }
    }
    @OAProperty(displayName = "Cash Points Used", decimalPlaces = 2, displayLength = 7, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.DOUBLE)
    public double getCashPointsUsed() {
        return cashPointsUsed;
    }
    protected void setCashPointsUsed(double newValue) {
        double old = cashPointsUsed;
        fireBeforePropertyChange(PROPERTY_CashPointsUsed, old, newValue);
        this.cashPointsUsed = newValue;
        firePropertyChange(PROPERTY_CashPointsUsed, old, this.cashPointsUsed);
    }
    @OAProperty(displayName = "Cash Sent Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getCashSentDate() {
        return cashSentDate;
    }
    
    public void setCashSentDate(OADate newValue) {
        fireBeforePropertyChange(P_CashSentDate, this.cashSentDate, newValue);
        OADate old = cashSentDate;
        this.cashSentDate = newValue;
        firePropertyChange(P_CashSentDate, old, this.cashSentDate);
    }
    @OAProperty(displayName = "International Visa Selected Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getInternationalVisaSelectedDate() {
        return internationalVisaSelectedDate;
    }
    
    public void setInternationalVisaSelectedDate(OADate newValue) {
        fireBeforePropertyChange(P_InternationalVisaSelectedDate, this.internationalVisaSelectedDate, newValue);
        OADate old = internationalVisaSelectedDate;
        this.internationalVisaSelectedDate = newValue;
        firePropertyChange(P_InternationalVisaSelectedDate, old, this.internationalVisaSelectedDate);
    }
    @OAProperty(displayName = "International Visa Amount", decimalPlaces = 2, isCurrency = true, displayLength = 7, hasCustomCode = true)
    @OAColumn(sqlType = java.sql.Types.DOUBLE)
    public double getInternationalVisaAmount() {
        return internationalVisaAmount;
    }
    public void setInternationalVisaAmount(double newValue) {
        double old = internationalVisaAmount;
        fireBeforePropertyChange(PROPERTY_InternationalVisaAmount, old, newValue);
        this.internationalVisaAmount = newValue;
        firePropertyChange(PROPERTY_InternationalVisaAmount, old, this.internationalVisaAmount);
    
        // custom
        if (isLoading()) return;
        if (!isServer()) return;
        
        Employee emp = getEmployee();
        double x = 0;//EmployeeDelegate.getPointValueForInternationalVisa(emp, this.internationalVisaAmount);
        setInternationalVisaPointsUsed(x);
    }
    @OAProperty(displayName = "International Visa Points Used", decimalPlaces = 2, displayLength = 7, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.DOUBLE)
    public double getInternationalVisaPointsUsed() {
        return internationalVisaPointsUsed;
    }
    public void setInternationalVisaPointsUsed(double newValue) {
        double old = internationalVisaPointsUsed;
        fireBeforePropertyChange(PROPERTY_InternationalVisaPointsUsed, old, newValue);
        this.internationalVisaPointsUsed = newValue;
        firePropertyChange(PROPERTY_InternationalVisaPointsUsed, old, this.internationalVisaPointsUsed);
    }
    @OAProperty(displayName = "International Visa Sent Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getInternationalVisaSentDate() {
        return internationalVisaSentDate;
    }
    
    public void setInternationalVisaSentDate(OADate newValue) {
        fireBeforePropertyChange(P_InternationalVisaSentDate, this.internationalVisaSentDate, newValue);
        OADate old = internationalVisaSentDate;
        this.internationalVisaSentDate = newValue;
        firePropertyChange(P_InternationalVisaSentDate, old, this.internationalVisaSentDate);
    }
    @OAProperty(displayName = "Points Ordered", description = "How many points were ordered", defaultValue = "0", displayLength = 5, inputMask = "#,##0", outputFormat = "#,##0")
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    /**
      How many points were ordered
    */
    public int getPointsOrdered() {
        return pointsOrdered;
    }
    
    public void setPointsOrdered(int newValue) {
        fireBeforePropertyChange(P_PointsOrdered, this.pointsOrdered, newValue);
        int old = pointsOrdered;
        this.pointsOrdered = newValue;
        firePropertyChange(P_PointsOrdered, old, this.pointsOrdered);
    }
    @OAProperty(displayName = "Invoice #", maxLength = 5, displayLength = 5)
    @OAColumn(maxLength = 5)
    public String getCashInvoiceNumber() {
        return cashInvoiceNumber;
    }
    
    public void setCashInvoiceNumber(String newValue) {
        fireBeforePropertyChange(P_CashInvoiceNumber, this.cashInvoiceNumber, newValue);
        String old = cashInvoiceNumber;
        this.cashInvoiceNumber = newValue;
        firePropertyChange(P_CashInvoiceNumber, old, this.cashInvoiceNumber);
    }
    @OAProperty(displayName = "Cash Invoice Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getCashInvoiceDate() {
        return cashInvoiceDate;
    }
    
    public void setCashInvoiceDate(OADate newValue) {
        fireBeforePropertyChange(P_CashInvoiceDate, this.cashInvoiceDate, newValue);
        OADate old = cashInvoiceDate;
        this.cashInvoiceDate = newValue;
        firePropertyChange(P_CashInvoiceDate, old, this.cashInvoiceDate);
    }
    @OAProperty(displayName = "Invoice #", maxLength = 5, displayLength = 5)
    @OAColumn(maxLength = 5)
    public String getInternationalVisaInvoiceNumber() {
        return internationalVisaInvoiceNumber;
    }
    
    public void setInternationalVisaInvoiceNumber(String newValue) {
        fireBeforePropertyChange(P_InternationalVisaInvoiceNumber, this.internationalVisaInvoiceNumber, newValue);
        String old = internationalVisaInvoiceNumber;
        this.internationalVisaInvoiceNumber = newValue;
        firePropertyChange(P_InternationalVisaInvoiceNumber, old, this.internationalVisaInvoiceNumber);
    }
    @OAProperty(displayName = "International Visa Invoice Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getInternationalVisaInvoiceDate() {
        return internationalVisaInvoiceDate;
    }
    
    public void setInternationalVisaInvoiceDate(OADate newValue) {
        fireBeforePropertyChange(P_InternationalVisaInvoiceDate, this.internationalVisaInvoiceDate, newValue);
        OADate old = internationalVisaInvoiceDate;
        this.internationalVisaInvoiceDate = newValue;
        firePropertyChange(P_InternationalVisaInvoiceDate, old, this.internationalVisaInvoiceDate);
    }
    @OAProperty(displayName = "International Visa Vendor Invoiced", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getInternationalVisaVendorInvoiced() {
        return internationalVisaVendorInvoiced;
    }
    
    public void setInternationalVisaVendorInvoiced(boolean newValue) {
        fireBeforePropertyChange(P_InternationalVisaVendorInvoiced, this.internationalVisaVendorInvoiced, newValue);
        boolean old = internationalVisaVendorInvoiced;
        this.internationalVisaVendorInvoiced = newValue;
        firePropertyChange(P_InternationalVisaVendorInvoiced, old, this.internationalVisaVendorInvoiced);
    }
    @OACalculatedProperty(displayName = "Points Used", decimalPlaces = 2, displayLength = 7, properties = {P_CashAmount, P_InternationalVisaAmount, P_Employee+"."+Employee.P_Program+"."+Program.P_CharityUpcharge, P_Employee+"."+Employee.P_Program+"."+Program.P_CardUpcharge, P_Employee+"."+Employee.P_Program+"."+Program.P_ItemUpcharge, P_Employee+"."+Employee.P_Program+"."+Program.P_PointValue, P_Employee+"."+Employee.P_Program+"."+Program.P_CashUpcharge, P_InspireOrderCharities+"."+InspireOrderCharity.P_Value})
    public double getPointsUsed() {
        double tot = 0.0;
        tot += getCashPointsUsed();
        tot += getInternationalVisaPointsUsed();
    
        for (InspireOrderCharity inspireOrderCharity : getInspireOrderCharities()) {
            tot += inspireOrderCharity.getPointsUsed();
        }
        
        for (InspireOrderItem inspireOrderItem : getInspireOrderItems()) {
            tot += inspireOrderItem.getPointsUsed();
        }
        
        for (AwardCardOrder aco : getAwardCardOrders()) {
            tot += aco.getPointsUsed();
        }
        return tot;
    }    
     
    @OACalculatedProperty(displayName = "Number Of Cart Items", displayLength = 5)
    public int getNumberOfCartItems() {
        int cnt = 0;
        cnt += getInspireOrderItems().getSize();
        cnt += getAwardCardOrders().getSize();
        cnt += getInspireOrderCharities().getSize();
    
        if (getInternationalVisaAmount() > 0.0) ++cnt;
        if (getCashAmount() > 0.0) ++cnt;
    
        return cnt;
    }
     
    @OAMany(
        displayName = "Award Card Orders", 
        toClass = AwardCardOrder.class, 
        owner = true, 
        reverseName = AwardCardOrder.P_InspireOrder, 
        cascadeSave = true, 
        cascadeDelete = true, 
        mustBeEmptyForDelete = true
    )
    public Hub<AwardCardOrder> getAwardCardOrders() {
        if (hubAwardCardOrders == null) {
            hubAwardCardOrders = (Hub<AwardCardOrder>) getHub(P_AwardCardOrders);
        }
        return hubAwardCardOrders;
    }
    
    @OAMany(
        toClass = Email.class, 
        reverseName = Email.P_InspireOrder
    )
    @OALinkTable(name = "InspireOrderEmail", indexName = "EmailInspireOrder", columns = {"InspireOrderId"})
    public Hub<Email> getEmails() {
        if (hubEmails == null) {
            hubEmails = (Hub<Email>) getHub(P_Emails);
        }
        return hubEmails;
    }
    
    @OAOne(
        reverseName = Employee.P_InspireOrders, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"EmployeeId"})
    public Employee getEmployee() {
        if (employee == null) {
            employee = (Employee) getObject(PROPERTY_Employee);
        }
        return employee;
    }
    public void setEmployee(Employee newValue) {
        setEmployee(newValue, true);
    }
    public void setEmployeeTemp(Employee newValue) {
        setEmployee(newValue, false);
    }
    public void setEmployee(Employee newValue, boolean bFirePropChange) {
        Employee old = this.employee;
        if (bFirePropChange) fireBeforePropertyChange(PROPERTY_Employee, old, newValue);
        this.employee = newValue;
        if (bFirePropChange) firePropertyChange(PROPERTY_Employee, old, this.employee);
        else OAObjectPropertyDelegate.setProperty(this, PROPERTY_Employee, this.employee);
    
        // custom code
        if (this.employee == null) return;
        if (old == this.employee) return;
        if (!isServer()) return;
        if (isLoading()) return;
    
        // this will reestablish the pointsUsed
        double pointsUsed = 0;//EmployeeDelegate.getPointValueForCash(this.employee, getCashAmount());
        setCashPointsUsed(pointsUsed);
        
        pointsUsed = 0;//EmployeeDelegate.getPointValueForInternationalVisa(this.employee, getInternationalVisaAmount());
        setInternationalVisaPointsUsed(pointsUsed);
        
        for (AwardCardOrder aco : getAwardCardOrders()) {
            pointsUsed = 0;//EmployeeDelegate.getPointValueForCard(this.employee, aco.getValue());
            aco.setPointsUsed(pointsUsed);
        }
        for (InspireOrderCharity ioc : getInspireOrderCharities()) {
            pointsUsed = 0;//EmployeeDelegate.getPointValueForCharity(this.employee, ioc.getValue());
            ioc.setPointsUsed(pointsUsed);
        }
        for (InspireOrderItem oi : getInspireOrderItems()) {
            pointsUsed = 0;//EmployeeDelegate.getPointValueForItem(this.employee, oi.getProduct().getItem());
            oi.setPointsUsed(pointsUsed);
        }
    }
    @OAMany(
        displayName = "Inspire Order Charities", 
        toClass = InspireOrderCharity.class, 
        owner = true, 
        reverseName = InspireOrderCharity.P_InspireOrder, 
        cascadeSave = true, 
        cascadeDelete = true, 
        mustBeEmptyForDelete = true
    )
    public Hub<InspireOrderCharity> getInspireOrderCharities() {
        if (hubInspireOrderCharities == null) {
            hubInspireOrderCharities = (Hub<InspireOrderCharity>) getHub(P_InspireOrderCharities);
        }
        return hubInspireOrderCharities;
    }
    
    @OAMany(
        displayName = "Inspire Order Items", 
        toClass = InspireOrderItem.class, 
        owner = true, 
        reverseName = InspireOrderItem.P_InspireOrder, 
        cascadeSave = true, 
        cascadeDelete = true, 
        mustBeEmptyForDelete = true, 
        seqProperty = InspireOrderItem.P_Seq, 
        sortProperty = InspireOrderItem.P_Seq
    )
    public Hub<InspireOrderItem> getInspireOrderItems() {
        if (hubInspireOrderItems == null) {
            hubInspireOrderItems = (Hub<InspireOrderItem>) getHub(P_InspireOrderItems);
        }
        return hubInspireOrderItems;
    }
    
    @OAOne(
        displayName = "Points Record", 
        reverseName = PointsRecord.P_InspireOrder, 
        allowCreateNew = false, 
        allowAddExisting = false
    )
    private PointsRecord getPointsRecord() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        displayName = "Ship To", 
        reverseName = ShipTo.P_InspireOrder, 
        autoCreateNew = true, 
        allowAddExisting = false
    )
    @OAFkey(columns = {"ShipToId"})
    public ShipTo getShipTo() {
        if (shipTo == null) {
            shipTo = (ShipTo) getObject(P_ShipTo);
        }
        return shipTo;
    }
    
    public void setShipTo(ShipTo newValue) {
        fireBeforePropertyChange(P_ShipTo, this.shipTo, newValue);
        ShipTo old = this.shipTo;
        this.shipTo = newValue;
        firePropertyChange(P_ShipTo, old, this.shipTo);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Date date;
        date = rs.getDate(2);
        if (date != null) this.created = new OADate(date);
        date = rs.getDate(3);
        if (date != null) this.billDate = new OADate(date);
        date = rs.getDate(4);
        if (date != null) this.paidDate = new OADate(date);
        date = rs.getDate(5);
        if (date != null) this.completedDate = new OADate(date);
        date = rs.getDate(6);
        if (date != null) this.cashSelectedDate = new OADate(date);
        this.cashAmount = (double) rs.getDouble(7);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, InspireOrder.P_CashAmount, true);
        }
        this.cashPointsUsed = (double) rs.getDouble(8);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, InspireOrder.P_CashPointsUsed, true);
        }
        date = rs.getDate(9);
        if (date != null) this.cashSentDate = new OADate(date);
        date = rs.getDate(10);
        if (date != null) this.internationalVisaSelectedDate = new OADate(date);
        this.internationalVisaAmount = (double) rs.getDouble(11);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, InspireOrder.P_InternationalVisaAmount, true);
        }
        this.internationalVisaPointsUsed = (double) rs.getDouble(12);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, InspireOrder.P_InternationalVisaPointsUsed, true);
        }
        date = rs.getDate(13);
        if (date != null) this.internationalVisaSentDate = new OADate(date);
        this.pointsOrdered = (int) rs.getInt(14);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, InspireOrder.P_PointsOrdered, true);
        }
        this.cashInvoiceNumber = rs.getString(15);
        date = rs.getDate(16);
        if (date != null) this.cashInvoiceDate = new OADate(date);
        this.internationalVisaInvoiceNumber = rs.getString(17);
        date = rs.getDate(18);
        if (date != null) this.internationalVisaInvoiceDate = new OADate(date);
        this.internationalVisaVendorInvoiced = rs.getBoolean(19);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, InspireOrder.P_InternationalVisaVendorInvoiced, true);
        }
        int employeeFkey = rs.getInt(20);
        if (!rs.wasNull() && employeeFkey > 0) {
            setProperty(P_Employee, new OAObjectKey(employeeFkey));
        }
        int shipToFkey = rs.getInt(21);
        if (!rs.wasNull() && shipToFkey > 0) {
            setProperty(P_ShipTo, new OAObjectKey(shipToFkey));
        }
        if (rs.getMetaData().getColumnCount() != 21) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 