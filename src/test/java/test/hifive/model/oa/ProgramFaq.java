// Generated by OABuilder
package test.hifive.model.oa;
 
import java.sql.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.annotation.*;
import com.viaoa.util.OADate;

import test.hifive.model.oa.filter.*;
import test.hifive.model.oa.propertypath.*;
 
@OAClass(
    shortName = "pf",
    displayName = "Program Faq",
    displayProperty = "created",
    rootTreePropertyPaths = {
        "[Company]."+Company.P_Programs+"."+Program.P_ProgramFaqs
    }
)
@OATable(
    indexes = {
        @OAIndex(name = "ProgramFaqLocation", columns = { @OAIndexColumn(name = "LocationId") }), 
        @OAIndex(name = "ProgramFaqProgram", columns = { @OAIndexColumn(name = "ProgramId") })
    }
)
public class ProgramFaq extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Created = "Created";
    public static final String P_Created = "Created";
    public static final String PROPERTY_Seq = "Seq";
    public static final String P_Seq = "Seq";
    public static final String PROPERTY_Question = "Question";
    public static final String P_Question = "Question";
    public static final String PROPERTY_Answer = "Answer";
    public static final String P_Answer = "Answer";
    public static final String PROPERTY_ManagerOnly = "ManagerOnly";
    public static final String P_ManagerOnly = "ManagerOnly";
     
     
    public static final String PROPERTY_Location = "Location";
    public static final String P_Location = "Location";
    public static final String PROPERTY_Program = "Program";
    public static final String P_Program = "Program";
     
    protected int id;
    protected OADate created;
    protected int seq;
    protected String question;
    protected String answer;
    protected boolean managerOnly;
     
    // Links to other objects.
    protected transient Program program;
     
    public ProgramFaq() {
        if (!isLoading()) {
            setCreated(new OADate());
        }
    }
     
    public ProgramFaq(int id) {
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
    @OAProperty(defaultValue = "new OADate()", displayLength = 8, isProcessed = true)
    @OAColumn(sqlType = java.sql.Types.DATE)
    public OADate getCreated() {
        return created;
    }
    
    public void setCreated(OADate newValue) {
        fireBeforePropertyChange(P_Created, this.created, newValue);
        OADate old = created;
        this.created = newValue;
        firePropertyChange(P_Created, old, this.created);
    }
    @OAProperty(displayLength = 5, isAutoSeq = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getSeq() {
        return seq;
    }
    
    public void setSeq(int newValue) {
        fireBeforePropertyChange(P_Seq, this.seq, newValue);
        int old = seq;
        this.seq = newValue;
        firePropertyChange(P_Seq, old, this.seq);
    }
    @OAProperty(maxLength = 150, displayLength = 40, columnLength = 20)
    @OAColumn(maxLength = 150)
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String newValue) {
        fireBeforePropertyChange(P_Question, this.question, newValue);
        String old = question;
        this.question = newValue;
        firePropertyChange(P_Question, old, this.question);
    }
    @OAProperty(displayName = "Description", maxLength = 11, displayLength = 11)
    @OAColumn(sqlType = java.sql.Types.CLOB)
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String newValue) {
        fireBeforePropertyChange(P_Answer, this.answer, newValue);
        String old = answer;
        this.answer = newValue;
        firePropertyChange(P_Answer, old, this.answer);
    }
    @OAProperty(displayName = "Manager Only", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getManagerOnly() {
        return managerOnly;
    }
    
    public void setManagerOnly(boolean newValue) {
        fireBeforePropertyChange(P_ManagerOnly, this.managerOnly, newValue);
        boolean old = managerOnly;
        this.managerOnly = newValue;
        firePropertyChange(P_ManagerOnly, old, this.managerOnly);
    }
    @OAOne(
        reverseName = Location.P_ProgramFaqs, 
        allowCreateNew = false, 
        allowAddExisting = false
    )
    @OALinkTable(name = "LocationProgramFaq", indexName = "LocationProgramFaq", columns = {"ProgramFaqId"})
    private Location getLocation() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
    @OAOne(
        reverseName = Program.P_ProgramFaqs, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"ProgramId"})
    public Program getProgram() {
        if (program == null) {
            program = (Program) getObject(P_Program);
        }
        return program;
    }
    
    public void setProgram(Program newValue) {
        fireBeforePropertyChange(P_Program, this.program, newValue);
        Program old = this.program;
        this.program = newValue;
        firePropertyChange(P_Program, old, this.program);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        java.sql.Date date;
        date = rs.getDate(2);
        if (date != null) this.created = new OADate(date);
        this.seq = (int) rs.getInt(3);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, ProgramFaq.P_Seq, true);
        }
        this.question = rs.getString(4);
        this.answer = rs.getString(5);
        this.managerOnly = rs.getBoolean(6);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, ProgramFaq.P_ManagerOnly, true);
        }
        int programFkey = rs.getInt(7);
        if (!rs.wasNull() && programFkey > 0) {
            setProperty(P_Program, new OAObjectKey(programFkey));
        }
        if (rs.getMetaData().getColumnCount() != 7) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 