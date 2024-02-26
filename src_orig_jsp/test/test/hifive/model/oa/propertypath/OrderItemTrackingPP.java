// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import test.hifive.model.oa.*;
 
public class OrderItemTrackingPP {
    private static OrderTrackingPPx orderTracking;
     

    public static OrderTrackingPPx orderTracking() {
        if (orderTracking == null) orderTracking = new OrderTrackingPPx(OrderItemTracking.P_OrderTracking);
        return orderTracking;
    }

    public static String id() {
        String s = OrderItemTracking.P_Id;
        return s;
    }

    public static String sentCarrier() {
        String s = OrderItemTracking.P_SentCarrier;
        return s;
    }

    public static String productionDate() {
        String s = OrderItemTracking.P_ProductionDate;
        return s;
    }

    public static String sentDate() {
        String s = OrderItemTracking.P_SentDate;
        return s;
    }

    public static String expectedDeliveryDate() {
        String s = OrderItemTracking.P_ExpectedDeliveryDate;
        return s;
    }

    public static String confirmEmailDate() {
        String s = OrderItemTracking.P_ConfirmEmailDate;
        return s;
    }

    public static String carrierTracking() {
        String s = OrderItemTracking.P_CarrierTracking;
        return s;
    }

    public static String cancelDate() {
        String s = OrderItemTracking.P_CancelDate;
        return s;
    }

    public static String replaceDate() {
        String s = OrderItemTracking.P_ReplaceDate;
        return s;
    }

    public static String freight() {
        String s = OrderItemTracking.P_Freight;
        return s;
    }
}
 