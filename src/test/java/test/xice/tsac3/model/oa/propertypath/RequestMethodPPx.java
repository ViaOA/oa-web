// Generated by OABuilder
package test.xice.tsac3.model.oa.propertypath;
 
import java.io.Serializable;

import test.xice.tsac3.model.oa.*;
 
public class RequestMethodPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private GSRequestPPx gSRequests;
     
    public RequestMethodPPx(String name) {
        this(null, name);
    }

    public RequestMethodPPx(PPxInterface parent, String name) {
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

    public GSRequestPPx gSRequests() {
        if (gSRequests == null) gSRequests = new GSRequestPPx(this, RequestMethod.P_GSRequests);
        return gSRequests;
    }

    public String id() {
        return pp + "." + RequestMethod.P_Id;
    }

    public String created() {
        return pp + "." + RequestMethod.P_Created;
    }

    public String name() {
        return pp + "." + RequestMethod.P_Name;
    }

    public String functionId() {
        return pp + "." + RequestMethod.P_FunctionId;
    }

    public String retryTimeoutSeconds() {
        return pp + "." + RequestMethod.P_RetryTimeoutSeconds;
    }

    public String retryWaitMs() {
        return pp + "." + RequestMethod.P_RetryWaitMs;
    }

    public String gemstoneTimeoutSeconds() {
        return pp + "." + RequestMethod.P_GemstoneTimeoutSeconds;
    }

    public String disabled() {
        return pp + "." + RequestMethod.P_Disabled;
    }

    public String usesHeavy() {
        return pp + "." + RequestMethod.P_UsesHeavy;
    }

    public String retryOnHeavy() {
        return pp + "." + RequestMethod.P_RetryOnHeavy;
    }

    public String avgRequestPerDay() {
        return pp + "." + RequestMethod.P_AvgRequestPerDay;
    }

    public String avgResponseSize() {
        return pp + "." + RequestMethod.P_AvgResponseSize;
    }

    public String avgResponseMs() {
        return pp + "." + RequestMethod.P_AvgResponseMs;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 