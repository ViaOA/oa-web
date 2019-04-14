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
    shortName = "li",
    displayName = "Login Image",
    displayProperty = "location"
)
@OATable(
    indexes = {
        @OAIndex(name = "LoginImageLoginImageSet", columns = { @OAIndexColumn(name = "LoginImageSetId") })
    }
)
public class LoginImage extends OAObject {
    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_Id = "Id";
    public static final String P_Id = "Id";
    public static final String PROPERTY_Location = "Location";
    public static final String P_Location = "Location";
    public static final String PROPERTY_LocationAsString = "LocationAsString";
    public static final String P_LocationAsString = "LocationAsString";
    public static final String PROPERTY_XPosition = "XPosition";
    public static final String P_XPosition = "XPosition";
    public static final String PROPERTY_YPosition = "YPosition";
    public static final String P_YPosition = "YPosition";
     
     
    public static final String PROPERTY_ImageStore = "ImageStore";
    public static final String P_ImageStore = "ImageStore";
    public static final String PROPERTY_LoginImageSet = "LoginImageSet";
    public static final String P_LoginImageSet = "LoginImageSet";
     
    protected int id;
    protected int location;
    public static final int LOCATION_topRight = 0;
    public static final int LOCATION_topLeft = 1;
    public static final int LOCATION_bottomRight = 2;
    public static final int LOCATION_bottomLeft = 3;
    public static final int LOCATION_center = 4;
    public static final Hub<String> hubLocation;
    static {
        hubLocation = new Hub<String>(String.class);
        hubLocation.addElement("Top Right");
        hubLocation.addElement("Top Left");
        hubLocation.addElement("Bottom Right");
        hubLocation.addElement("Bottom Left");
        hubLocation.addElement("Center");
    }
    protected int xPosition;
    protected int yPosition;
     
    // Links to other objects.
    protected transient ImageStore imageStore;
    protected transient LoginImageSet loginImageSet;
     
    public LoginImage() {
    }
     
    public LoginImage(int id) {
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
    @OAProperty(displayLength = 5, isNameValue = true)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getLocation() {
        return location;
    }
    
    public void setLocation(int newValue) {
        fireBeforePropertyChange(P_Location, this.location, newValue);
        int old = location;
        this.location = newValue;
        firePropertyChange(P_Location, old, this.location);
    }
    public String getLocationAsString() {
        if (isNull(P_Location)) return "";
        String s = hubLocation.getAt(getLocation());
        if (s == null) s = "";
        return s;
    }
    @OAProperty(displayName = "X Position", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getXPosition() {
        return xPosition;
    }
    
    public void setXPosition(int newValue) {
        fireBeforePropertyChange(P_XPosition, this.xPosition, newValue);
        int old = xPosition;
        this.xPosition = newValue;
        firePropertyChange(P_XPosition, old, this.xPosition);
    }
    @OAProperty(displayName = "Y Position", displayLength = 5)
    @OAColumn(sqlType = java.sql.Types.INTEGER)
    public int getYPosition() {
        return yPosition;
    }
    
    public void setYPosition(int newValue) {
        fireBeforePropertyChange(P_YPosition, this.yPosition, newValue);
        int old = yPosition;
        this.yPosition = newValue;
        firePropertyChange(P_YPosition, old, this.yPosition);
    }
    @OAOne(
        displayName = "Image Store", 
        owner = true, 
        reverseName = ImageStore.P_LoginImage, 
        cascadeSave = true, 
        cascadeDelete = true, 
        allowAddExisting = false
    )
    @OAFkey(columns = {"ImageStoreId"})
    public ImageStore getImageStore() {
        if (imageStore == null) {
            imageStore = (ImageStore) getObject(P_ImageStore);
        }
        return imageStore;
    }
    
    public void setImageStore(ImageStore newValue) {
        fireBeforePropertyChange(P_ImageStore, this.imageStore, newValue);
        ImageStore old = this.imageStore;
        this.imageStore = newValue;
        firePropertyChange(P_ImageStore, old, this.imageStore);
    }
    
    @OAOne(
        displayName = "Login Image Set", 
        reverseName = LoginImageSet.P_LoginImages, 
        required = true, 
        allowCreateNew = false
    )
    @OAFkey(columns = {"LoginImageSetId"})
    public LoginImageSet getLoginImageSet() {
        if (loginImageSet == null) {
            loginImageSet = (LoginImageSet) getObject(P_LoginImageSet);
        }
        return loginImageSet;
    }
    
    public void setLoginImageSet(LoginImageSet newValue) {
        fireBeforePropertyChange(P_LoginImageSet, this.loginImageSet, newValue);
        LoginImageSet old = this.loginImageSet;
        this.loginImageSet = newValue;
        firePropertyChange(P_LoginImageSet, old, this.loginImageSet);
    }
    
    public void load(ResultSet rs, int id) throws SQLException {
        this.id = id;
        this.location = (int) rs.getInt(2);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, LoginImage.P_Location, true);
        }
        this.xPosition = (int) rs.getInt(3);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, LoginImage.P_XPosition, true);
        }
        this.yPosition = (int) rs.getInt(4);
        if (rs.wasNull()) {
            OAObjectInfoDelegate.setPrimitiveNull(this, LoginImage.P_YPosition, true);
        }
        int imageStoreFkey = rs.getInt(5);
        if (!rs.wasNull() && imageStoreFkey > 0) {
            setProperty(P_ImageStore, new OAObjectKey(imageStoreFkey));
        }
        int loginImageSetFkey = rs.getInt(6);
        if (!rs.wasNull() && loginImageSetFkey > 0) {
            setProperty(P_LoginImageSet, new OAObjectKey(loginImageSetFkey));
        }
        if (rs.getMetaData().getColumnCount() != 6) {
            throw new SQLException("invalid number of columns for load method");
        }

        changedFlag = false;
        newFlag = false;
    }
}
 
