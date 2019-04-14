// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import test.hifive.model.oa.*;
 
public class ProgramEmailTypePP {
    private static EmailPPx emails;
    private static EmailTypePPx emailType;
    private static ProgramPPx program;
     

    public static EmailPPx emails() {
        if (emails == null) emails = new EmailPPx(ProgramEmailType.P_Emails);
        return emails;
    }

    public static EmailTypePPx emailType() {
        if (emailType == null) emailType = new EmailTypePPx(ProgramEmailType.P_EmailType);
        return emailType;
    }

    public static ProgramPPx program() {
        if (program == null) program = new ProgramPPx(ProgramEmailType.P_Program);
        return program;
    }

    public static String id() {
        String s = ProgramEmailType.P_Id;
        return s;
    }

    public static String created() {
        String s = ProgramEmailType.P_Created;
        return s;
    }

    public static String allowAutomaticSend() {
        String s = ProgramEmailType.P_AllowAutomaticSend;
        return s;
    }

    public static String subject() {
        String s = ProgramEmailType.P_Subject;
        return s;
    }

    public static String text() {
        String s = ProgramEmailType.P_Text;
        return s;
    }

    public static String textFieldClass() {
        String s = "textFieldClass";
        return s;
    }
}
 
