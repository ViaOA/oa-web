package test.vetjobs.oa;
 
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.annotation.*;
 
 
@OAClass(
    shortName = "fol",
    displayName = "Folder",
    isLookup = true,
    isPreSelect = true
)
@OATable(
)
public class Folder extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String P_Id = "Id";
    public static final String P_Name = "Name";
    public static final String P_Description = "Description";
    public static final String P_Hide = "Hide";
     
     
    public static final String P_Jobs = "Jobs";
    public static final String P_BatchRows = "BatchRows";
     
    protected int id;
    protected String name;
    protected String description;
    protected boolean hide;
     
    // Links to other objects.
     
     
    public Folder() {
    }
     
    public Folder(int id) {
        this();
        setId(id);
    }
    @OAProperty(displayLength = 5)
    @OAId()
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getId() {
        return id;
    }
    
    public void setId(int newValue) {
        int old = id;
        this.id = newValue;
        firePropertyChange(P_Id, old, this.id);
    }
    
     
    @OAProperty(maxLength = 75, displayLength = 4)
    @OAColumn(maxLength = 75)
    public String getName() {
        return name;
    }
    
    public void setName(String newValue) {
        String old = name;
        this.name = newValue;
        firePropertyChange(P_Name, old, this.name);
    }
    
     
    @OAProperty(maxLength = 120, displayLength = 11)
    @OAColumn(maxLength = 120)
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String newValue) {
        String old = description;
        this.description = newValue;
        firePropertyChange(P_Description, old, this.description);
    }
    
     
    @OAProperty(displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.BOOLEAN)
    public boolean getHide() {
        return hide;
    }
    
    public void setHide(boolean newValue) {
        boolean old = hide;
        this.hide = newValue;
        firePropertyChange(P_Hide, old, this.hide);
    }
    
     
    @OAMany(toClass = Job.class, reverseName = Job.P_Folder, createMethod = false)
    private Hub<Job> getJobs() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
     
    @OAMany(displayName = "Batch Rows", toClass = BatchRow.class, reverseName = BatchRow.P_Folder, createMethod = false)
    private Hub<BatchRow> getBatchRows() {
        // oamodel has createMethod set to false, this method exists only for annotations.
        return null;
    }
    
     
}
 
