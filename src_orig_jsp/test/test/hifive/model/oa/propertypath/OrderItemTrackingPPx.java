// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class OrderItemTrackingPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private OrderTrackingPPx orderTracking;
     
    public OrderItemTrackingPPx(String name) {
        this(null, name);
    }

    public OrderItemTrackingPPx(PPxInterface parent, String name) {
        String s = null;
        if (parent != null) {
            s = parent.toString();
        }
        if (s == null) s = "";
        if (name != null) {
            if (s.length() > 0) s += ".";
            s += name;
        }
        pp = s;
    }

    public OrderTrackingPPx orderTracking() {
        if (orderTracking == null) orderTracking = new OrderTrackingPPx(this, OrderItemTracking.P_OrderTracking);
        return orderTracking;
    }

    public String id() {
        return pp + "." + OrderItemTracking.P_Id;
    }

    public String sentCarrier() {
        return pp + "." + OrderItemTracking.P_SentCarrier;
    }

    public String productionDate() {
        return pp + "." + OrderItemTracking.P_ProductionDate;
    }

    public String sentDate() {
        return pp + "." + OrderItemTracking.P_SentDate;
    }

    public String expectedDeliveryDate() {
        return pp + "." + OrderItemTracking.P_ExpectedDeliveryDate;
    }

    public String confirmEmailDate() {
        return pp + "." + OrderItemTracking.P_ConfirmEmailDate;
    }

    public String carrierTracking() {
        return pp + "." + OrderItemTracking.P_CarrierTracking;
    }

    public String cancelDate() {
        return pp + "." + OrderItemTracking.P_CancelDate;
    }

    public String replaceDate() {
        return pp + "." + OrderItemTracking.P_ReplaceDate;
    }

    public String freight() {
        return pp + "." + OrderItemTracking.P_Freight;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 