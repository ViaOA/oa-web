// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class LocationEmailTypePPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private EmailPPx emails;
    private EmailTypePPx emailType;
    private LocationPPx location;
     
    public LocationEmailTypePPx(String name) {
        this(null, name);
    }

    public LocationEmailTypePPx(PPxInterface parent, String name) {
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

    public EmailPPx emails() {
        if (emails == null) emails = new EmailPPx(this, LocationEmailType.P_Emails);
        return emails;
    }

    public EmailTypePPx emailType() {
        if (emailType == null) emailType = new EmailTypePPx(this, LocationEmailType.P_EmailType);
        return emailType;
    }

    public LocationPPx location() {
        if (location == null) location = new LocationPPx(this, LocationEmailType.P_Location);
        return location;
    }

    public String id() {
        return pp + "." + LocationEmailType.P_Id;
    }

    public String created() {
        return pp + "." + LocationEmailType.P_Created;
    }

    public String allowAutomaticSend() {
        return pp + "." + LocationEmailType.P_AllowAutomaticSend;
    }

    public String subject() {
        return pp + "." + LocationEmailType.P_Subject;
    }

    public String text() {
        return pp + "." + LocationEmailType.P_Text;
    }

    public String textFieldClass() {
        return pp + ".textFieldClass";
    }

    @Override
    public String toString() {
        return pp;
    }
}
 