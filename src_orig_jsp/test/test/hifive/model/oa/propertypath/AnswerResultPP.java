// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import test.hifive.model.oa.*;
 
public class AnswerResultPP {
    private static AnswerPPx currentAnswer;
    private static QuestionResultPPx questionResult;
     

    public static AnswerPPx currentAnswer() {
        if (currentAnswer == null) currentAnswer = new AnswerPPx(AnswerResult.P_CurrentAnswer);
        return currentAnswer;
    }

    public static QuestionResultPPx questionResult() {
        if (questionResult == null) questionResult = new QuestionResultPPx(AnswerResult.P_QuestionResult);
        return questionResult;
    }

    public static String id() {
        String s = AnswerResult.P_Id;
        return s;
    }

    public static String answerText() {
        String s = AnswerResult.P_AnswerText;
        return s;
    }

    public static String answerValue() {
        String s = AnswerResult.P_AnswerValue;
        return s;
    }
}
 