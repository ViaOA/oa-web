// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import test.hifive.model.oa.*;
 
public class InspireApprovalPP {
    private static EmailPPx email;
    private static EmployeePPx employee;
    private static InspireAwardLevelPPx inspireAwardLevel;
    private static InspireRecipientPPx inspireRecipient;
    private static EmailPPx reminderEmails;
     

    public static EmailPPx email() {
        if (email == null) email = new EmailPPx(InspireApproval.P_Email);
        return email;
    }

    public static EmployeePPx employee() {
        if (employee == null) employee = new EmployeePPx(InspireApproval.P_Employee);
        return employee;
    }

    public static InspireAwardLevelPPx inspireAwardLevel() {
        if (inspireAwardLevel == null) inspireAwardLevel = new InspireAwardLevelPPx(InspireApproval.P_InspireAwardLevel);
        return inspireAwardLevel;
    }

    public static InspireRecipientPPx inspireRecipient() {
        if (inspireRecipient == null) inspireRecipient = new InspireRecipientPPx(InspireApproval.P_InspireRecipient);
        return inspireRecipient;
    }

    public static EmailPPx reminderEmails() {
        if (reminderEmails == null) reminderEmails = new EmailPPx(InspireApproval.P_ReminderEmails);
        return reminderEmails;
    }

    public static String id() {
        String s = InspireApproval.P_Id;
        return s;
    }

    public static String created() {
        String s = InspireApproval.P_Created;
        return s;
    }

    public static String status() {
        String s = InspireApproval.P_Status;
        return s;
    }

    public static String statusDate() {
        String s = InspireApproval.P_StatusDate;
        return s;
    }

    public static String comments() {
        String s = InspireApproval.P_Comments;
        return s;
    }

    public static String updateEmail() {
        String s = "updateEmail";
        return s;
    }
}
 
