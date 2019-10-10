// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import test.hifive.model.oa.*;
 
public class QuestionResultPP {
    private static AnswerResultPPx answerResults;
    private static QuestionPPx question;
    private static QuizResultPPx quizResult;
     

    public static AnswerResultPPx answerResults() {
        if (answerResults == null) answerResults = new AnswerResultPPx(QuestionResult.P_AnswerResults);
        return answerResults;
    }

    public static QuestionPPx question() {
        if (question == null) question = new QuestionPPx(QuestionResult.P_Question);
        return question;
    }

    public static QuizResultPPx quizResult() {
        if (quizResult == null) quizResult = new QuizResultPPx(QuestionResult.P_QuizResult);
        return quizResult;
    }

    public static String id() {
        String s = QuestionResult.P_Id;
        return s;
    }

    public static String questionText() {
        String s = QuestionResult.P_QuestionText;
        return s;
    }
}
 