// Generated by OABuilder
package test.xice.tsam.model.oa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.viaoa.annotation.OAClass;
import com.viaoa.annotation.OAColumn;
import com.viaoa.annotation.OAId;
import com.viaoa.annotation.OAMany;
import com.viaoa.annotation.OAProperty;
import com.viaoa.annotation.OATable;
import com.viaoa.hub.Hub;
import com.viaoa.object.OAObject;
import com.viaoa.object.OAObjectInfoDelegate;
import com.viaoa.util.OADate;

@OAClass(shortName = "com", displayName = "Command", isLookup = true, isPreSelect = true, displayProperty = "name")
@OATable()
public class Command extends OAObject {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = Logger.getLogger(Command.class.getName());
	public static final String PROPERTY_Id = "Id";
	public static final String P_Id = "Id";
	public static final String PROPERTY_Created = "Created";
	public static final String P_Created = "Created";
	public static final String PROPERTY_Seq = "Seq";
	public static final String P_Seq = "Seq";
	public static final String PROPERTY_Name = "Name";
	public static final String P_Name = "Name";
	public static final String PROPERTY_Description = "Description";
	public static final String P_Description = "Description";
	public static final String PROPERTY_CommandLine = "CommandLine";
	public static final String P_CommandLine = "CommandLine";
	public static final String PROPERTY_Type = "Type";
	public static final String P_Type = "Type";
	public static final String PROPERTY_TypeAsString = "TypeAsString";
	public static final String P_TypeAsString = "TypeAsString";
	public static final String PROPERTY_InAPI = "InAPI";
	public static final String P_InAPI = "InAPI";

	public static final String PROPERTY_ApplicationTypeCommands = "ApplicationTypeCommands";
	public static final String P_ApplicationTypeCommands = "ApplicationTypeCommands";
	public static final String PROPERTY_MRADServerCommands = "MRADServerCommands";
	public static final String P_MRADServerCommands = "MRADServerCommands";

	protected int id;
	protected OADate created;
	protected int seq;
	protected String name;
	protected String description;
	protected String commandLine;
	protected int type;
	public static final int TYPE_UNKNOWN = 0;
	public static final int TYPE_START = 1;
	public static final int TYPE_STARTSNAPSHOT = 2;
	public static final int TYPE_STOP = 3;
	public static final int TYPE_KILL = 4;
	public static final int TYPE_SUSPEND = 5;
	public static final int TYPE_RESUME = 6;
	public static final int TYPE_PING = 7;
	public static final int TYPE_UPDATE = 8;
	public static final int TYPE_INSTALL = 9;
	public static final int TYPE_LOGCLEANUP = 10;
	public static final int TYPE_CUSTOM = 11;
	public static final int TYPE_GETHOSTINFO = 12;
	public static final Hub<String> hubType;
	static {
		hubType = new Hub<String>(String.class);
		hubType.addElement("Unknown");
		hubType.addElement("SSH Start");
		hubType.addElement("Start Snapshot");
		hubType.addElement("Stop");
		hubType.addElement("SSH Stop");
		hubType.addElement("Suspend");
		hubType.addElement("Resume");
		hubType.addElement("Ping");
		hubType.addElement("Update");
		hubType.addElement("Install");
		hubType.addElement("Log Cleanup");
		hubType.addElement("Custom");
		hubType.addElement("Get Host Information");
	}
	protected boolean inAPI;

	// Links to other objects.

	public Command() {
		if (!isLoading()) {
			setCreated(new OADate());
		}
	}

	public Command(int id) {
		this();
		setId(id);
	}

	@OAProperty(isUnique = true, displayLength = 4, columnLength = 3)
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

	@OAProperty(maxLength = 35, importMatch = true, displayLength = 10)
	@OAColumn(maxLength = 35)
	public String getName() {
		return name;
	}

	public void setName(String newValue) {
		fireBeforePropertyChange(P_Name, this.name, newValue);
		String old = name;
		this.name = newValue;
		firePropertyChange(P_Name, old, this.name);
	}

	@OAProperty(maxLength = 135, displayLength = 20, columnLength = 18)
	@OAColumn(maxLength = 135)
	public String getDescription() {
		return description;
	}

	public void setDescription(String newValue) {
		fireBeforePropertyChange(P_Description, this.description, newValue);
		String old = description;
		this.description = newValue;
		firePropertyChange(P_Description, old, this.description);
	}

	@OAProperty(displayName = "Command Line", maxLength = 400, displayLength = 20, columnLength = 15)
	@OAColumn(maxLength = 400)
	public String getCommandLine() {
		return commandLine;
	}

	public void setCommandLine(String newValue) {
		fireBeforePropertyChange(P_CommandLine, this.commandLine, newValue);
		String old = commandLine;
		this.commandLine = newValue;
		firePropertyChange(P_CommandLine, old, this.commandLine);
	}

	@OAProperty(importMatch = true, displayLength = 5, isNameValue = true)
	@OAColumn(sqlType = java.sql.Types.INTEGER)
	public int getType() {
		return type;
	}

	public void setType(int newValue) {
		fireBeforePropertyChange(P_Type, this.type, newValue);
		int old = type;
		this.type = newValue;
		firePropertyChange(P_Type, old, this.type);
	}

	public String getTypeAsString() {
		if (isNull(P_Type)) {
			return "";
		}
		String s = hubType.getAt(getType());
		if (s == null) {
			s = "";
		}
		return s;
	}

	@OAProperty(displayName = "In API", displayLength = 5)
	@OAColumn(sqlType = java.sql.Types.BOOLEAN)
	public boolean getInAPI() {
		return inAPI;
	}

	public void setInAPI(boolean newValue) {
		fireBeforePropertyChange(P_InAPI, this.inAPI, newValue);
		boolean old = inAPI;
		this.inAPI = newValue;
		firePropertyChange(P_InAPI, old, this.inAPI);
	}

	@OAMany(displayName = "Application Type Commands", toClass = ApplicationTypeCommand.class, reverseName = ApplicationTypeCommand.P_Command, createMethod = false)
	private Hub<ApplicationTypeCommand> getApplicationTypeCommands() {
		// oamodel has createMethod set to false, this method exists only for annotations.
		return null;
	}

	@OAMany(displayName = "MRAD Server Commands", toClass = MRADServerCommand.class, reverseName = MRADServerCommand.P_Command, createMethod = false)
	private Hub<MRADServerCommand> getMRADServerCommands() {
		// oamodel has createMethod set to false, this method exists only for annotations.
		return null;
	}

	public void load(ResultSet rs, int id) throws SQLException {
		this.id = id;
		java.sql.Date date;
		date = rs.getDate(2);
		if (date != null) {
			this.created = new OADate(date);
		}
		this.seq = (int) rs.getInt(3);
		if (rs.wasNull()) {
			OAObjectInfoDelegate.setPrimitiveNull(this, Command.P_Seq, true);
		}
		this.name = rs.getString(4);
		this.description = rs.getString(5);
		this.commandLine = rs.getString(6);
		this.type = (int) rs.getInt(7);
		if (rs.wasNull()) {
			OAObjectInfoDelegate.setPrimitiveNull(this, Command.P_Type, true);
		}
		this.inAPI = rs.getBoolean(8);
		if (rs.wasNull()) {
			OAObjectInfoDelegate.setPrimitiveNull(this, Command.P_InAPI, true);
		}
		if (rs.getMetaData().getColumnCount() != 8) {
			throw new SQLException("invalid number of columns for load method");
		}

		changedFlag = false;
		newFlag = false;
	}
}
