// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class PointsRecordPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private EmailPPx email;
    private InspireOrderPPx inspireOrder;
    private PointsCoreValuePPx pointsCoreValue;
    private PointsRequestPPx pointsRequest;
    private EmployeePPx pointsToEmployee;
    private ProgramPPx pointsToProgram;
     
    public PointsRecordPPx(String name) {
        this(null, name);
    }

    public PointsRecordPPx(PPxInterface parent, String name) {
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

    public EmailPPx email() {
        if (email == null) email = new EmailPPx(this, PointsRecord.P_Email);
        return email;
    }

    public InspireOrderPPx inspireOrder() {
        if (inspireOrder == null) inspireOrder = new InspireOrderPPx(this, PointsRecord.P_InspireOrder);
        return inspireOrder;
    }

    public PointsCoreValuePPx pointsCoreValue() {
        if (pointsCoreValue == null) pointsCoreValue = new PointsCoreValuePPx(this, PointsRecord.P_PointsCoreValue);
        return pointsCoreValue;
    }

    public PointsRequestPPx pointsRequest() {
        if (pointsRequest == null) pointsRequest = new PointsRequestPPx(this, PointsRecord.P_PointsRequest);
        return pointsRequest;
    }

    public EmployeePPx pointsToEmployee() {
        if (pointsToEmployee == null) pointsToEmployee = new EmployeePPx(this, PointsRecord.P_PointsToEmployee);
        return pointsToEmployee;
    }

    public ProgramPPx pointsToProgram() {
        if (pointsToProgram == null) pointsToProgram = new ProgramPPx(this, PointsRecord.P_PointsToProgram);
        return pointsToProgram;
    }

    public String id() {
        return pp + "." + PointsRecord.P_Id;
    }

    public String created() {
        return pp + "." + PointsRecord.P_Created;
    }

    public String points() {
        return pp + "." + PointsRecord.P_Points;
    }

    public String toDiscretionary() {
        return pp + "." + PointsRecord.P_ToDiscretionary;
    }

    public String reason() {
        return pp + "." + PointsRecord.P_Reason;
    }

    public String comment() {
        return pp + "." + PointsRecord.P_Comment;
    }

    public String event() {
        return pp + "." + PointsRecord.P_Event;
    }

    public String custom1() {
        return pp + "." + PointsRecord.P_Custom1;
    }

    public String custom2() {
        return pp + "." + PointsRecord.P_Custom2;
    }

    public String custom3() {
        return pp + "." + PointsRecord.P_Custom3;
    }

    public String engaged() {
        return pp + "." + PointsRecord.P_Engaged;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 