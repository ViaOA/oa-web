// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class CashstarOrderPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private AwardCardOrderPPx awardCardOrder;
     
    public CashstarOrderPPx(String name) {
        this(null, name);
    }

    public CashstarOrderPPx(PPxInterface parent, String name) {
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

    public AwardCardOrderPPx awardCardOrder() {
        if (awardCardOrder == null) awardCardOrder = new AwardCardOrderPPx(this, CashstarOrder.P_AwardCardOrder);
        return awardCardOrder;
    }

    public String id() {
        return pp + "." + CashstarOrder.P_Id;
    }

    public String orderNumber() {
        return pp + "." + CashstarOrder.P_OrderNumber;
    }

    public String cardStatus() {
        return pp + "." + CashstarOrder.P_CardStatus;
    }

    public String egcCode() {
        return pp + "." + CashstarOrder.P_EgcCode;
    }

    public String egcNumber() {
        return pp + "." + CashstarOrder.P_EgcNumber;
    }

    public String cardUrl() {
        return pp + "." + CashstarOrder.P_CardUrl;
    }

    public String balanaceLastUpdated() {
        return pp + "." + CashstarOrder.P_BalanaceLastUpdated;
    }

    public String challengeType() {
        return pp + "." + CashstarOrder.P_ChallengeType;
    }

    public String currency() {
        return pp + "." + CashstarOrder.P_Currency;
    }

    public String active() {
        return pp + "." + CashstarOrder.P_Active;
    }

    public String faceplateCode() {
        return pp + "." + CashstarOrder.P_FaceplateCode;
    }

    public String viewed() {
        return pp + "." + CashstarOrder.P_Viewed;
    }

    public String challengeDescription() {
        return pp + "." + CashstarOrder.P_ChallengeDescription;
    }

    public String challenge() {
        return pp + "." + CashstarOrder.P_Challenge;
    }

    public String firstViewed() {
        return pp + "." + CashstarOrder.P_FirstViewed;
    }

    public String auditNumber() {
        return pp + "." + CashstarOrder.P_AuditNumber;
    }

    public String transactionId() {
        return pp + "." + CashstarOrder.P_TransactionId;
    }

    public String initialBalance() {
        return pp + "." + CashstarOrder.P_InitialBalance;
    }

    public String currentBalance() {
        return pp + "." + CashstarOrder.P_CurrentBalance;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 
