package com.viaoa.web.ui.control;

import java.util.logging.Logger;

import com.viaoa.annotation.*;
import com.viaoa.object.*;
import com.viaoa.hub.*;
import com.viaoa.template.OATemplate;

import com.viaoa.util.*;
import com.viaoa.web.util.OAWebUtil;

/**
 */
public class OAWebUIController extends HubListenerAdapter {
	private static Logger LOG = Logger.getLogger(OAWebUIController.class.getName());

	public boolean DEBUG; // used for debugging a single component. ex: ((OALabel)lbl).setDebug(true)
	public static boolean DEBUGUI = false; // used by debug() to show info

	protected Hub hub;
	protected String propertyPath;
	protected OAPropertyPath oaPropertyPath;

	protected Hub hubSelect;

	protected String format;
    private boolean bDefaultFormat;
    private String defaultFormat;
	

	protected String toolTipTextPropertyPath;
	protected String nullDescription = "";

	// display collumn width
	private int columns;
	private int propertyInfoDisplayColumns = -2;

	// mini column/chars
	private int minColumns;
	// max column/chars
	private int maxColumns;
	private int maxInput;
	

	/** HTML used for displaying in some components (label, combo, list, autocomplete), and used for table cell rendering */
	protected String displayTemplate;
	private OATemplate templateDisplay;

	protected String toolTipTextTemplate;
	private OATemplate templateToolTipText;
	
	
    private Hub hubLast;
    private volatile boolean bIgnoreReset;
	

	/**
	 * Create new controller for Hub and Jfc component
	 *
	 * @param hub
	 * @param object             if hub is null, then this object will be put in temp hub and made the AO
	 * @param propertyPath       property used by component
	 * @param bAoOnly            should controller listen to propChange for all objects in hub, or just AO.
	 * @param comp
	 * @param type               default type of change listener
	 * @param bUseLinkHub        should setup also include setting up the link hub
	 * @param bUseObjectCallback use oaObjectCallback to determine enabled/visible.
	 */
	public OAWebUIController(Hub hub, String propertyPath, HubChangeListener.Type type) {
		this.hub = hub;
		this.propertyPath = propertyPath;
		this.hubChangeListenerType = type;
		reset();
	}


	protected void reset() {
		try {
			bIgnoreReset = true;
			_reset();
		} finally {
		    bIgnoreReset = false;
		}
	}

	protected void _reset() {
		// note: dont call close, want to keep visibleChangeListener, enabledChangeListener
		if (hubLast != null) {
			hubLast.removeHubListener(this);
		}
		hubLast = this.hub;

		if (this.hub == null) {
			return;
		}

		oaPropertyPath = new OAPropertyPath(hub.getObjectClass(), propertyPath);
		bDefaultFormat = false;
	}


	public Hub getHub() {
		return hub;
	}

	public void setHub(Hub hub) {
		this.hub = hub;
		reset();
	}

	public String getPropertyPath() {
		return propertyPath;
	}

	public void setPropertyPath(String propPath) {
		propertyPath = propPath;
		reset();
	}

	public void close() {
		if (hub != null) {
			hub.removeHubListener(this);
		}
	}

	/**
	 * Returns the Hub that this component will work with.
	 */
	public Hub getSelectHub() {
		return hubSelect;
	}

	/**
	 * Sets the MultiSelect that this component will work with.
	 */
	public void setSelectHub(Hub newHub) {
		this.hubSelect = newHub;
	}


	/**
	 * This will find the real object in this hub to use, in cases where a comp is added to a table, and the table.hub is different then the
	 * comp.hub, which could be a detail or link type relationship to the table.hub
	 */
	private Class fromParentClass;
	private String fromParentPropertyPath;

	protected Object getRealObject(Object fromObject) {
		if (fromObject == null || hub == null) {
			return fromObject;
		}
		Class c = hub.getObjectClass();
		if (c == null || c.isAssignableFrom(fromObject.getClass())) {
			return fromObject;
		}
		if (!(fromObject instanceof OAObject)) {
			return fromObject;
		}
		if (!OAObject.class.isAssignableFrom(getHub().getObjectClass())) {
			return fromObject;
		}

		if (fromParentClass == null || !fromParentClass.equals(fromObject.getClass())) {
			fromParentClass = fromObject.getClass();
			fromParentPropertyPath = OAObjectReflectDelegate.getPropertyPathFromMaster((OAObject) fromObject, getHub());
		}
		return OAObjectReflectDelegate.getProperty((OAObject) fromObject, fromParentPropertyPath);
	}

	public Object getValue(Object obj) {
		obj = getRealObject(obj);
		if (obj == null) {
			return null;
		}

		if (OAString.isEmpty(propertyPath) && hubSelect != null) {
			return hubSelect.contains(obj);
		}
		
		if (oaPropertyPath == null) {
		    return obj;
		}

		if (oaPropertyPath.getDoesLastMethodHasHubParam()) {
			obj = OAObjectReflectDelegate.getProperty(getHub(), propertyPath);
		} else {
			if (OAString.isEmpty(propertyPath)) {
				return obj;
			}
			if (!(obj instanceof OAObject)) {
				return obj;
			}
			if (obj instanceof OAObject) {
				obj = ((OAObject) obj).getProperty(propertyPath);
			}
		}
		return obj;
	}

	public String getValueAsString(Object obj) {
		return getValueAsString(obj, getFormat());
	}

	public String getValueAsString(Object obj, String fmt) {
		obj = getValue(obj);
		String s = OAConv.toString(obj, fmt);
		return s;
	}

	// calls the set method on the actualHub.ao
	public void setValue(Object value) {
		String fmt = getFormat();
		Object obj = getHub().getAO();
		setValue(obj, value, fmt);
	}

	public void setValue(Object obj, Object value) {
		String fmt = getFormat();
		setValue(obj, value, fmt);
	}

    public void setValue(Object obj, Object value, String fmt) {
        if (obj == null) {
            return;
        }
        if (obj instanceof OAObject) {
            ((OAObject) obj).setProperty(propertyPath, value, fmt);
        }
    }


	/**
	 * Used to verify a property change.
	 * @return null if no errors, else error message
	 */
	public String isValid(final OAObject oaObj, Object newValue) {
		OAObjectCallback em = OAObjectCallbackDelegate.getVerifyPropertyChangeObjectCallback(OAObjectCallback.CHECK_ALL,
																								oaObj, propertyPath, null, newValue);
        String result = null;
		if (!em.getAllowed()) {
			result = em.getResponse();
			Throwable t = em.getThrowable();
			if (OAString.isEmpty(result)) {
				if (t != null) {
					for (; t != null; t = t.getCause()) {
						result = t.getMessage();
						if (OAString.isNotEmpty(result)) {
							break;
						}
					}
					if (OAString.isEmpty(result)) {
						result = em.getThrowable().toString();
					}
				} else {
					result = "invalid value";
				}
			}
		}
		return result;
	}


	/**
	 * Returns format to use for displaying value as a String.
	 *
	 * @see OADate#OADate see OAConverterNumber#OAConverterNumber
	 */
	public String getFormat() {
		if (format != null) {
			return format;
		}
		if (!bDefaultFormat) {
			bDefaultFormat = true;
			if (oaPropertyPath != null) {
				defaultFormat = oaPropertyPath.getFormat();
			}
		}

		Object objx = hub.getAO();
		if (objx instanceof OAObject) {
			String prop;
			if (oaPropertyPath != null && oaPropertyPath.hasLinks()) {
				objx = oaPropertyPath.getLastLinkValue(objx);
			}
			if (objx instanceof OAObject) {
				return OAObjectCallbackDelegate.getFormat((OAObject) objx, oaPropertyPath.getEndPropertyInfo().getName(), defaultFormat);
			}
		}
		
		return defaultFormat;
	}

	/**
	 * Format used to display this property. Used to format Date, Times and Numbers. set to "" (blank) for no formatting. If null, then the
	 * default format will be used.
	 *
	 * @see OADate#OADate see OAConverterNumber#OAConverterNumber
	 */
	public void setFormat(String fmt) {
		String old = this.format;
		this.format = fmt;
		bDefaultFormat = true;
		defaultFormat = null;
	}


	/**
	 * The "words" to use for the empty slot (null value). Example: "none of the above". Default: "" (blank). Set to null if none should
	 * be used
	 */
	public String getNullDescription() {
		return nullDescription;
	}

	public void setNullDescription(String s) {
		String old = this.nullDescription;
		this.nullDescription = s;
		if (OACompare.isNotEqual(this.nullDescription, old)) {
			callUpdate();
		}
	}


	
	public void setColumns(int x) {
		this.columns = x;
	} 

	public int getColumns() {
		if (component instanceof OATextField) {
			return ((OATextField) component).getColumns();
		}
		return this.columns;
	}

	public void setMinColumns(int x) {
		this.minColumns = x;
	}

	public int getMinColumns() {
		return this.minColumns;
	}

	public void setMaxColumns(int x) {
		maxColumns = x;
	}

	public int getMaxColumns() {
		return maxColumns;
	}

	public void setMaxInput(int x) {
		maxInput = x;
	}

	public int getCalcMaxInput() {
		if (maxInput > 0) {
			return maxInput;
		}
		int x = getPropertyInfoMaxLength();
		return Math.max(0, x);
	}

	public int getCalcColumns() {
		int x = getColumns();
		if (x > 0) {
			return x;
		}
		x = getPropertyInfoDisplayColumns();
		return Math.max(0, x);
	}

	public int getPropertyInfoDisplayColumns() {
		if (propertyInfoDisplayColumns == -2) {
			getPropertyInfoMaxLength();
		}
		return propertyInfoDisplayColumns;
	}

	public OAPropertyInfo getPropertyInfo() {
        Hub h = getHub();
        if (h == null) {
            return null;
        }

        OAObjectInfo oi = OAObjectInfoDelegate.getOAObjectInfo(h.getObjectClass());
        OAPropertyInfo pi = oi.getPropertyInfo(oaPropertyPath.getLastPropertyName());
        return pi;
	}	    
	


	protected void afterChangeActiveObject() {
		callUpdate();
	}

	public void setDisplayTemplate(String s) {
		this.displayTemplate = s;
		templateDisplay = null;
	}

	public String getDisplayTemplate() {
		return displayTemplate;
	}

	public OATemplate getTemplateForDisplay() {
		if (OAString.isNotEmpty(getDisplayTemplate())) {
			if (templateDisplay == null) {
				templateDisplay = new OATemplate<>(getDisplayTemplate());
			}
		}
		return templateDisplay;
	}

	/**
	 * Used to display values, uses display template if defined.
	 */
	public String getDisplayText(Object obj, String defaultText) {
		obj = getRealObject(obj);
		if (!(obj instanceof OAObject)) {
			return defaultText;
		}

		String s = getDisplayTemplate();
		if (OAString.isEmpty(s)) {
			return defaultText;
		}

		defaultText = getTemplateForDisplay().process((OAObject) obj);
		if (defaultText != null && defaultText.indexOf('<') >= 0 && defaultText.toLowerCase().indexOf("<html>") < 0) {
			defaultText = "<html>" + defaultText;
		}

		return defaultText;
	}

	public void setToolTipTextTemplate(String s) {
		this.toolTipTextTemplate = s;
		templateToolTipText = null;
	}

	public String getToolTipTextTemplate() {
		return toolTipTextTemplate;
	}

	public OATemplate getTemplateForToolTipText() {
		if (OAString.isNotEmpty(getToolTipTextTemplate())) {
			if (templateToolTipText == null) {
				templateToolTipText = new OATemplate<>(getToolTipTextTemplate());
			}
		}
		return templateToolTipText;
	}

	public String getToolTipText(Object obj, String ttDefault) {
		obj = getRealObject(obj);

		if (obj instanceof OAObject) {
			if (OAString.isNotEmpty(toolTipTextPropertyPath)) {
				ttDefault = ((OAObject) obj).getPropertyAsString(toolTipTextPropertyPath);
			}

			String s = getToolTipTextTemplate();
			if (OAString.isNotEmpty(s)) {
				ttDefault = s;
			}

			String prop;
			if (oaPropertyPath != null && oaPropertyPath.hasLinks()) {
				objx = oaPropertyPath.getLastLinkValue(objx);
			}
			if (objx instanceof OAObject) {
				ttDefault = OAObjectCallbackDelegate.getToolTip((OAObject) objx, endPropertyName, ttDefault);
			}
		} else {
			if (OAString.isNotEmpty(toolTipTextPropertyPath) || OAString.isNotEmpty(getToolTipTextTemplate())) {
				ttDefault = null;
			}
		}

		if (ttDefault != null && ttDefault.indexOf("<%=") >= 0 && objx instanceof OAObject) {
			if (templateToolTipText == null || !ttDefault.equals(templateToolTipText.getTemplate())) {
				templateToolTipText = new OATemplate(ttDefault);
			}
			ttDefault = templateToolTipText.process((OAObject) obj, (OAObject) objx);
		}

		if (ttDefault != null && ttDefault.indexOf('<') >= 0 && ttDefault.toLowerCase().indexOf("<html>") < 0) {
			ttDefault = "<html>" + ttDefault;
		}

		return ttDefault;
	}


	
}
