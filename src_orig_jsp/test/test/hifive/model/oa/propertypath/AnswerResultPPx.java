// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class AnswerResultPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private AnswerPPx currentAnswer;
    private QuestionResultPPx questionResult;
     
    public AnswerResultPPx(String name) {
        this(null, name);
    }

    public AnswerResultPPx(PPxInterface parent, String name) {
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

    public AnswerPPx currentAnswer() {
        if (currentAnswer == null) currentAnswer = new AnswerPPx(this, AnswerResult.P_CurrentAnswer);
        return currentAnswer;
    }

    public QuestionResultPPx questionResult() {
        if (questionResult == null) questionResult = new QuestionResultPPx(this, AnswerResult.P_QuestionResult);
        return questionResult;
    }

    public String id() {
        return pp + "." + AnswerResult.P_Id;
    }

    public String answerText() {
        return pp + "." + AnswerResult.P_AnswerText;
    }

    public String answerValue() {
        return pp + "." + AnswerResult.P_AnswerValue;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 