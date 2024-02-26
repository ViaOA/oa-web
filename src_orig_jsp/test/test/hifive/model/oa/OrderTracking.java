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
    shortName = "ot",
    displayName = "Order Tracking",
    displayProperty = "created"
)
@OATable(
)
public class OrderTracking extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_Description = "Description";
    public static final String P_Description = "Description";
    public static final String PROPERTY_SentEmailDate = "SentEmailDate";
    public static final String P_SentEmailDate = "SentEmailDate";
    public static final String PROPERTY_ReceivedDate = "ReceivedDate";
    public static final String P_ReceivedDate = "ReceivedDate";
    public static final String PROPERTY_CloseDate = "CloseDate";
    public static final String P_CloseDate = "CloseDate";
    public static final String PROPERTY_Notes = "Notes";
    public static final String P_Notes = "Notes";
    public static final String PROPERTY_BillingDate = "BillingDate";
    public static final String P_BillingDate = "BillingDate";
    public static final String PROPERTY_BillAmount = "BillAmount";
    public static final String P_BillAmount = "BillAmount";
    public static final String PROPERTY_InvoiceDate = "InvoiceDate";
    public static final String P_InvoiceDate = "InvoiceDate";
    public static final String PROPERTY_InvoiceNumber = "InvoiceNumber";
    public static final String P_InvoiceNumber = "InvoiceNumber";
    public static final String PROPERTY_PaidDate = "PaidDate";
    public static final String P_PaidDate = "PaidDate";
     
     
    public static final String PROPERTY_OrderItemTrackings = "OrderItemTrackings";
    public static final String P_OrderItemTrackings = "OrderItemTrackings";
    public static final String PROPERTY_OrderTrackingStatuses = "OrderTrackingStatuses";
    public static final String P_OrderTrackingStatuses = "OrderTrackingStatuses";
     
    protected int id;
    protected OADate created;
    protected String description;
    protected OADate sentEmailDate;
    protected OADate receivedDate;
    protected OADate closeDate;
    protected String notes;
    protected OADate billingDate;
    protected double billAmount;
    protected OADate invoiceDate;
    protected String invoiceNumber;
    protected OADate paidDate;
     
    // Links to other objects.
    protected transient Hub<OrderItemTracking> hubOrderItemTrackings;
    protected transient Hub<OrderTrackingStatus> hubOrderTrackingStatuses;
     
    public OrderTracking() {
        if (!isLoading()) {
            setCreated(new OADate());
        }
    }
     
    public OrderTracking(int id) {
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
    @OAProperty(maxLength = 254, displayLength = 25, columnLength = 16)
    @OAColumn(maxLength = 254)
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String newValue) {
        fireBeforePropertyChange(P_Description, this.description, newValue);
        String old = description;
        this.description = newValue;
        firePropertyChange(P_Description, old, this.description);
    }
    @OAProperty(displayName = "Sent Email Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getSentEmailDate() {
        return sentEmailDate;
    }
    
    public void setSentEmailDate(OADate newValue) {
        fireBeforePropertyChange(P_SentEmailDate, this.sentEmailDate, newValue);
        OADate old = sentEmailDate;
        this.sentEmailDate = newValue;
        firePropertyChange(P_SentEmailDate, old, this.sentEmailDate);
    }
    @OAProperty(displayName = "Received Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getReceivedDate() {
        return receivedDate;
    }
    
    public void setReceivedDate(OADate newValue) {
        fireBeforePropertyChange(P_ReceivedDate, this.receivedDate, newValue);
        OADate old = receivedDate;
        this.receivedDate = newValue;
        firePropertyChange(P_ReceivedDate, old, this.receivedDate);
    }
    @OAProperty(displayName = "Close Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getCloseDate() {
        return closeDate;
    }
    
    public void setCloseDate(OADate newValue) {
        fireBeforePropertyChange(P_CloseDate, this.closeDate, newValue);
        OADate old = closeDate;
        this.closeDate = newValue;
        firePropertyChange(P_CloseDate, old, this.closeDate);
    }
    @OAProperty(maxLength = 5, displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.CLOB)
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String newValue) {
        fireBeforePropertyChange(P_Notes, this.notes, newValue);
        String old = notes;
        this.notes = newValue;
        firePropertyChange(P_Notes, old, this.notes);
    }
    @OAProperty(displayName = "Billing Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getBillingDate() {
        return billingDate;
    }
    
    public void setBillingDate(OADate newValue) {
        fireBeforePropertyChange(P_BillingDate, this.billingDate, newValue);
        OADate old = billingDate;
        this.billingDate = newValue;
        firePropertyChange(P_BillingDate, old, this.billingDate);
    }
    @OAProperty(displayName = "Bill Amount", decimalPlaces = 2, isCurrency = true, displayLength = 7)
    @OAColumn(sqlType = java.sql.Types.DOUBLE)
    public double getBillAmount() {
        return billAmount;
    }
    
    public void setBillAmount(double newValue) {
        fireBeforePropertyChange(P_BillAmount, this.billAmount, newValue);
        double old = billAmount;
        this.billAmount = newValue;
        firePropertyChange(P_BillAmount, old, this.billAmount);
    }
    @OAProperty(displayName = "Invoice Date", displayLength = 8)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getInvoiceDate() {
        return invoiceDate;
    }
    
    public void setInvoiceDate(OADate newValue) {
        fireBeforePropertyChange(P_InvoiceDate, this.invoiceDate, newValue);
        OADate old = invoiceDate;
        this.invoiceDate = newValue;
        firePropertyChange(P_InvoiceDate, old, this.invoiceDate);
    }
    @OAProperty(displayName = "Invoice Number", maxLength = 20, displayLength = 20, columnLength = 7)
    @OAColumn(maxLength = 20)
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    
    public void setInvoiceNumber(String newValue) {
        fireBeforePropertyChange(P_InvoiceNumber, this.invoiceNumber, newValue);
        String old = invoiceNumber;
        this.invoiceNumber = newValue;
        firePropertyChange(P_InvoiceNumber, old, this.invoiceNumber);
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
    @OAMany(
        displayName = "Order Item Trackings", 
        toClass = OrderItemTracking.class, 
        owner = true, 
        reverseName = OrderItemTracking.P_OrderTracking, 
        cascadeSave = true, 
        cascadeDelete = true, 
        mustBeEmptyForDelete = true
    )
    public Hub<OrderItemTracking> getOrderItemTrackings() {
        if (hubOrderItemTrackings == null) {
            hubOrderItemTrackings = (Hub<OrderItemTracking>) getHub(P_OrderItemTrackings);
        }
        return hubOrderItemTrackings;
    }
    
    @OAMany(
        displayName = "Order Tracking Statuses", 
        toClass = OrderTrackingStatus.class, 
        owner = true, 
        reverseName = OrderTrackingStatus.P_OrderTracking, 
        cascadeSave = true, 
        cascadeDelete = true, 
        mustBeEmptyForDelete = true
    )
    public Hub<OrderTrackingStatus> getOrderTrackingStatuses() {
        if (hubOrderTrackingStatuses == null) {
            hubOrderTrackingStatuses = (Hub<OrderTrackingStatus>) getHub(P_OrderTrackingStatuses);
        }
        return hubOrderTrackingStatuses;
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Date date;
        date = rs.getDate(2);
        if (date != null) this.created = new OADate(date);
        this.description = rs.getString(3);
        date = rs.getDate(4);
        if (date != null) this.sentEmailDate = new OADate(date);
        date = rs.getDate(5);
        if (date != null) this.receivedDate = new OADate(date);
        date = rs.getDate(6);
        if (date != null) this.closeDate = new OADate(date);
        this.notes = rs.getString(7);
        date = rs.getDate(8);
        if (date != null) this.billingDate = new OADate(date);
        this.billAmount = (double) rs.getDouble(9);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, OrderTracking.P_BillAmount, true);
        }
        date = rs.getDate(10);
        if (date != null) this.invoiceDate = new OADate(date);
        this.invoiceNumber = rs.getString(11);
        date = rs.getDate(12);
        if (date != null) this.paidDate = new OADate(date);
        if (rs.getMetaData().getColumnCount() != 12) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 