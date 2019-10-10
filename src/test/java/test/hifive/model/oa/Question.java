// Generated by OABuilder
package test.hifive.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;

import test.hifive.model.oa.filter.*;
import test.hifive.model.oa.propertypath.*;

import com.viaoa.annotation.*;
 
@OAClass(
    shortName = "que",
    displayName = "Question",
    displayProperty = "questionText",
    sortProperty = "quiz"
)
@OATable(
    indexes = {
        @OAIndex(name = "QuestionQuiz", columns = { @OAIndexColumn(name = "QuizId") })
    }
)
public class Question extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_QuestionText = "QuestionText";
    public static final String P_QuestionText = "QuestionText";
     
     
    public static final String PROPERTY_Answers = "Answers";
    public static final String P_Answers = "Answers";
    public static final String PROPERTY_QuestionResults = "QuestionResults";
    public static final String P_QuestionResults = "QuestionResults";
    public static final String PROPERTY_Quiz = "Quiz";
    public static final String P_Quiz = "Quiz";
     
    protected int id;
    protected String questionText;
     
    // Links to other objects.
    protected transient Hub<Answer> hubAnswers;
    protected transient Hub<QuestionResult> hubQuestionResults;
    protected transient Quiz quiz;
     
    public Question() {
    }
     
    public Question(int id) {
        this();
        setId(id);
    }
     
    @OAProperty(isUnique = true, displayLength = 5)
    @OAId()
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getId() {
        return id;
    }
    
    public void setId(int newValue) {
        fireBeforePropertyChange(P_Id, this.id, newValue);
        int old = id;
        this.id = newValue;
        firePropertyChange(P_Id, old, this.id);
    }
    @OAProperty(displayName = "Question Text", maxLength = 200, displayLength = 40)
    @OAColumn(sqlType = java.sql.Types.CLOB)
    public String getQuestionText() {
        return questionText;
    }
    
    public void setQuestionText(String newValue) {
        fireBeforePropertyChange(P_QuestionText, this.questionText, newValue);
        String old = questionText;
        this.questionText = newValue;
        firePropertyChange(P_QuestionText, old, this.questionText);
    }
    @OAMany(
        toClass = Answer.class, 
        owner = true, 
        reverseName = Answer.P_Question
    )
    public Hub<Answer> getAnswers() {
        if (hubAnswers == null) {
            hubAnswers = (Hub<Answer>) getHub(P_Answers);
        }
        return hubAnswers;
    }
    
    @OAMany(
        displayName = "Question Results", 
        toClass = QuestionResult.class, 
        reverseName = QuestionResult.P_Question
    )
    public Hub<QuestionResult> getQuestionResults() {
        if (hubQuestionResults == null) {
            hubQuestionResults = (Hub<QuestionResult>) getHub(P_QuestionResults);
        }
        return hubQuestionResults;
    }
    
    @OAOne(
        reverseName = Quiz.P_Questions
    )
    @OAFkey(columns = {"QuizId"})
    public Quiz getQuiz() {
        if (quiz == null) {
            quiz = (Quiz) getObject(P_Quiz);
        }
        return quiz;
    }
    
    public void setQuiz(Quiz newValue) {
        fireBeforePropertyChange(P_Quiz, this.quiz, newValue);
        Quiz old = this.quiz;
        this.quiz = newValue;
        firePropertyChange(P_Quiz, old, this.quiz);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.questionText = rs.getString(2);
        int quizFkey = rs.getInt(3);
        if (!rs.wasNull() && quizFkey > 0) {
            setProperty(P_Quiz, new OAObjectKey(quizFkey));
        }
        if (rs.getMetaData().getColumnCount() != 3) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 