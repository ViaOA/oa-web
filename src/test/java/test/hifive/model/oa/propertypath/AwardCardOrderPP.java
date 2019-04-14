// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import test.hifive.model.oa.*;
 
public class AwardCardOrderPP {
    private static CardPPx card;
    private static CashstarOrderPPx cashstarOrder;
    private static EmployeeAwardPPx employeeAward;
    private static InspireOrderPPx inspireOrder;
    private static ValuePPx inspireValues;
     

    public static CardPPx card() {
        if (card == null) card = new CardPPx(AwardCardOrder.P_Card);
        return card;
    }

    public static CashstarOrderPPx cashstarOrder() {
        if (cashstarOrder == null) cashstarOrder = new CashstarOrderPPx(AwardCardOrder.P_CashstarOrder);
        return cashstarOrder;
    }

    public static EmployeeAwardPPx employeeAward() {
        if (employeeAward == null) employeeAward = new EmployeeAwardPPx(AwardCardOrder.P_EmployeeAward);
        return employeeAward;
    }

    public static InspireOrderPPx inspireOrder() {
        if (inspireOrder == null) inspireOrder = new InspireOrderPPx(AwardCardOrder.P_InspireOrder);
        return inspireOrder;
    }

    public static ValuePPx inspireValues() {
        if (inspireValues == null) inspireValues = new ValuePPx(AwardCardOrder.P_InspireValues);
        return inspireValues;
    }

    public static String id() {
        String s = AwardCardOrder.P_Id;
        return s;
    }

    public static String created() {
        String s = AwardCardOrder.P_Created;
        return s;
    }

    public static String value() {
        String s = AwardCardOrder.P_Value;
        return s;
    }

    public static String sentDate() {
        String s = AwardCardOrder.P_SentDate;
        return s;
    }

    public static String shippingInfo() {
        String s = AwardCardOrder.P_ShippingInfo;
        return s;
    }

    public static String cardType() {
        String s = AwardCardOrder.P_CardType;
        return s;
    }

    public static String lastStatusDate() {
        String s = AwardCardOrder.P_LastStatusDate;
        return s;
    }

    public static String lastStatus() {
        String s = AwardCardOrder.P_LastStatus;
        return s;
    }

    public static String completedDate() {
        String s = AwardCardOrder.P_CompletedDate;
        return s;
    }

    public static String pointsUsed() {
        String s = AwardCardOrder.P_PointsUsed;
        return s;
    }

    public static String invoiceNumber() {
        String s = AwardCardOrder.P_InvoiceNumber;
        return s;
    }

    public static String invoiceDate() {
        String s = AwardCardOrder.P_InvoiceDate;
        return s;
    }

    public static String vendorInvoiced() {
        String s = AwardCardOrder.P_VendorInvoiced;
        return s;
    }

    public static String inspireOrderPointsUsed() {
        String s = AwardCardOrder.P_InspireOrderPointsUsed;
        return s;
    }

    public static String inspireOrderPointsAvailable() {
        String s = AwardCardOrder.P_InspireOrderPointsAvailable;
        return s;
    }
}
 
