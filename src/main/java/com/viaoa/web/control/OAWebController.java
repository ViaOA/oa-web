/*  Copyright 1999 Vince Via vvia@viaoa.com
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.viaoa.web.control;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.viaoa.annotation.OAOne;
import com.viaoa.datasource.OADataSource;
import com.viaoa.hub.Hub;
import com.viaoa.hub.HubChangeListener;
import com.viaoa.hub.HubChangeListener.HubProp;
import com.viaoa.hub.HubDetailDelegate;
import com.viaoa.hub.HubEvent;
import com.viaoa.hub.HubLinkDelegate;
import com.viaoa.hub.HubListenerAdapter;
import com.viaoa.hub.HubTemp;
import com.viaoa.image.ColorIcon;
import com.viaoa.image.MultiIcon;
import com.viaoa.image.OAImageUtil;
import com.viaoa.image.ScaledImageIcon;
import com.viaoa.model.oa.VString;
import com.viaoa.object.OAFinder;
import com.viaoa.object.OALinkInfo;
import com.viaoa.object.OAObject;
import com.viaoa.object.OAObjectCallback;
import com.viaoa.object.OAObjectCallbackDelegate;
import com.viaoa.object.OAObjectInfo;
import com.viaoa.object.OAObjectInfoDelegate;
import com.viaoa.object.OAObjectReflectDelegate;
import com.viaoa.object.OAPropertyInfo;
import com.viaoa.object.OAThreadLocalDelegate;
import com.viaoa.template.OATemplate;
import com.viaoa.undo.OAUndoableEdit;
import com.viaoa.util.OACompare;
import com.viaoa.util.OAConv;
import com.viaoa.util.OAConverter;
import com.viaoa.util.OADate;
import com.viaoa.util.OANullObject;
import com.viaoa.util.OAPropertyPath;
import com.viaoa.util.OAString;
import com.viaoa.web.OALabel;
import com.viaoa.web.OAResizePanel;
import com.viaoa.web.OATabbedPane;
import com.viaoa.web.OATable;
import com.viaoa.web.OATableComponent;
import com.viaoa.web.OATextField;
import com.viaoa.web.OAWebComponent;
import com.viaoa.web.OAWebUtil;
import com.viaoa.web.swing.JLabel;
import com.viaoa.web.swing.JTabbedPane;
import com.viaoa.web.swing.JTable;
import com.viaoa.web.swing.JTextField;

/**
 * Base controller class for OAWeb components. 
 * Implements the HubListener and provides most of the methods required for creating
 * controller Classes (Model/View/Controller) for UI components.
 */
public class OAWebController extends HubListenerAdapter {
	private static Logger LOG = Logger.getLogger(OAWebController.class.getName());

	public boolean DEBUG; // used for debugging a single component. ex: ((OALabel)lbl).setDebug(true)
	public static boolean DEBUGUI = false; // used by debug() to show info

	protected final OAWebComponent component;

	protected Hub hub;
	protected final boolean bAoOnly;
	protected String propertyPath;
	protected OAPropertyPath oaPropertyPath;

	protected Class endPropertyFromClass; // oaObj class (same as hub, or class for pp end)
	protected String endPropertyName;
	protected Class endPropertyClass;

	protected String hubListenerPropertyName;

	protected Object hubObject; // single object, that will be put in temp hub
	protected Hub hubTemp;

	protected Hub hubLink; // link hub for Hub
	protected String linkPropertyName;

	protected boolean bUseLinkHub;
	protected final boolean bUseObjectCallback;

	protected HubChangeListener.Type hubChangeListenerType;

	protected boolean bIsHubCalc;
	protected boolean bListenToHubSize;
	protected boolean bEnableUndo = true;
	protected String undoDescription;

	protected Hub hubSelect;

	protected String format;
	protected Font font;
	protected String fontPropertyPath;
	protected Color colorBackground;
	protected String backgroundColorPropertyPath;
	protected Color colorForeground;
	protected String foregroundColorPropertyPath;
	protected Color colorIcon;
	protected String iconColorPropertyPath;

	private String confirmMessage;

    private String enabledMessage;
    private String visibleMessage;
	
	// Image sizing
	protected int maxImageHeight, maxImageWidth;

	protected Image image;
	protected String imageDirectory;
	protected String imageClassPath;
	protected Class rootImageClassPath;;
	protected String imagePropertyPath;

	protected String toolTipTextPropertyPath;
	protected String nullDescription = "";
	protected boolean bHtml;

	// display collumn width
	private int columns;
	private int propertyInfoDisplayColumns = -2;

	// mini column/chars
	private int minColumns;
	// max column/chars
	private int maxColumns;
	private int maxInput;
	private int propertyInfoMaxColumns = -2;
	private int dataSourceMaxColumns = -2;

	private OALabel label;
	// should label always match comp.enabled? Default=false: will not disable label if AO!=null (view only mode)
	private boolean bLabelAlwaysMatchesComponentEnabled;
	private Hub hubForLabel; // hub that controls label.enabled

	private ColorIcon myColorIcon;
	private MultiIcon myMultiIcon;


	/** HTML used for displaying in some components (label, combo, list, autocomplete), and used for table cell rendering */
	protected String displayTemplate;
	private OATemplate templateDisplay;

	protected String toolTipTextTemplate;
	private OATemplate templateToolTipText;

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
	public OAWebController(Hub hub, Object object, String propertyPath, OAWebComponent comp, HubChangeListener.Type type,
			final boolean bUseLinkHub, final boolean bUseObjectCallback) {
		this(hub, object, propertyPath, true, comp, type, bUseLinkHub, bUseObjectCallback);
	}

	public OAWebController(Hub hub, Object object, String propertyPath, boolean bAoOnly, OAWebComponent comp, HubChangeListener.Type type,
			final boolean bUseLinkHub, final boolean bUseObjectCallback) {
		this.hub = hub;
		this.hubObject = object;
		this.propertyPath = propertyPath;
		this.bAoOnly = bAoOnly;
		this.component = comp;
		this.bUseLinkHub = bUseLinkHub;
		this.bUseObjectCallback = bUseObjectCallback;
		this.hubChangeListenerType = type;

		reset();
	}

	// used to track the last values used by reset
	private Hub hubLast;
	private Object hubObjectLast;
	private HubChangeListener.HubProp hubChangeListenerTypeLast;
	private boolean bIgnoreUpdate;

	protected void reset() {
		try {
			bIgnoreUpdate = true;
			_reset();
		} finally {
			bIgnoreUpdate = false;
		}
		update();
	}

	// called when hub, property, etc is changed.
	// does not include resetting HubChangeListeners (changeListener, visibleChangeListener, enabledChangeListener)
	protected void _reset() {
		// note: dont call close, want to keep visibleChangeListener, enabledChangeListener
		if (hubLast != null) {
			hubLast.removeHubListener(this);
		}
		if (hubObjectLast != null) {
			HubTemp.deleteHub(hubObjectLast);
		}
		if (changeListenerEnabled != null && hubChangeListenerTypeLast != null) {
			changeListenerEnabled.remove(hubChangeListenerTypeLast);
			hubChangeListenerTypeLast = null;
		}
		if (changeListener != null) {
			changeListener.close();
			changeListener = null;
		}

		if (hub != null) {
			this.hubTemp = null;
			this.hubObject = null;
		} else {
			if (hubObject == null) {
				this.hub = null;
				this.hubTemp = null;
			} else {
				this.hub = this.hubTemp = HubTemp.createHub(hubObject);
			}
		}

		hubObjectLast = hubObject;
		hubLast = this.hub;

		if (this.hub == null) {
			return;
		}

		if (propertyPath != null && propertyPath.indexOf('.') >= 0) {
			hubListenerPropertyName = propertyPath.replace('.', '_'); // (com.cdi.model.oa.WebItem)B_WebPart_Title
			hub.addHubListener(this, hubListenerPropertyName, new String[] { propertyPath }, bAoOnly);
		} else {
			hubListenerPropertyName = propertyPath;
			if (OAString.isNotEmpty(hubListenerPropertyName)) {
				hub.addHubListener(this, hubListenerPropertyName, bAoOnly);
			} else {
				hub.addHubListener(this);
			}
		}

		oaPropertyPath = new OAPropertyPath(hub.getObjectClass(), propertyPath);
		final String[] properties = oaPropertyPath.getProperties();
		endPropertyName = (properties == null || properties.length == 0) ? null : properties[properties.length - 1];

		if (hubChangeListenerType != null) { // else: this class already is listening to hub
			if (hubChangeListenerType == HubChangeListener.Type.HubNotEmpty || hubChangeListenerType == HubChangeListener.Type.HubEmpty) {
				bListenToHubSize = true;
			}
			hubChangeListenerTypeLast = getEnabledChangeListener().add(hub, hubChangeListenerType);
		}

		// 20190112
		if (oaPropertyPath.getEndLinkInfo() != null && properties != null && properties.length == 1) {
			OAOne oaOne = oaPropertyPath.getOAOneAnnotation();
			if (oaOne != null) {
				if (OAString.isNotEmpty(oaOne.defaultPropertyPath())) {
					if (!oaOne.defaultPropertyPathCanBeChanged()) {
						getEnabledChangeListener().addPropertyNull(hub, properties[0]);
					}
				}
			}
		}

		Method[] ms = oaPropertyPath.getMethods();
		endPropertyFromClass = hub.getObjectClass();
		if (ms != null && ms.length > 0) {
			Class[] cs = ms[ms.length - 1].getParameterTypes();
			bIsHubCalc = cs.length == 1 && cs[0].equals(Hub.class);
			endPropertyClass = ms[ms.length - 1].getReturnType();

			if (ms.length > 1) {
				endPropertyFromClass = ms[ms.length - 2].getReturnType();
			}
		} else {
			bIsHubCalc = false;
			endPropertyClass = String.class;
		}
		bDefaultFormat = false;

		if (!bUseLinkHub) {
			if (bUseObjectCallback) {
				Class cz = hub.getObjectClass();
				String ppPrefix = "";
				int cnt = 0;
				for (String prop : properties) {
					if (cnt == 0) {
						addEnabledObjectCallbackCheck(hub, prop);
						addVisibleObjectCallbackCheck(hub, prop);
					} else {
						OAObjectCallbackDelegate.addObjectCallbackChangeListeners(	hub, cz, prop, ppPrefix, getEnabledChangeListener(),
																					true);
						OAObjectCallbackDelegate.addObjectCallbackChangeListeners(	hub, cz, prop, ppPrefix, getVisibleChangeListener(),
																					false);
					}
					ppPrefix += prop + ".";
					cz = oaPropertyPath.getClasses()[cnt++];
				}

				if (cnt == 0) {
					addEnabledObjectCallbackCheck(hub, null);
					addVisibleObjectCallbackCheck(hub, null);
				}
			}
		} else {
			hubLink = hub.getLinkHub(true);

			if (hubLink != null) {
				linkPropertyName = hub.getLinkPath(true);
			} else {
				Hub hubx = HubDetailDelegate.getMasterHub(hub);
				if (hubx != null) {
					OALinkInfo li = HubDetailDelegate.getLinkInfoFromMasterToDetail(hub);
					if (li != null && li.getType() == li.TYPE_ONE) {
						hubLink = hubx;
						linkPropertyName = li.getName();
					}
				}
			}

			if (hubLink != null) {
				getEnabledChangeListener().add(hubLink, HubChangeListener.Type.AoNotNull);
				if (bUseObjectCallback) {
					addEnabledObjectCallbackCheck(hubLink, linkPropertyName);
					addVisibleObjectCallbackCheck(hubLink, linkPropertyName);
				}
			}
		}
	}

	public void bind(Hub hub, String propertyPath, boolean bUseLinkHub) {
		this.hub = hub;
		this.propertyPath = propertyPath;
		this.bUseLinkHub = bUseLinkHub;
		reset();
	}

	public void bind(Hub hub, String propertyPath) {
		this.hub = hub;
		this.propertyPath = propertyPath;
		reset();
	}

	public Hub getHub() {
		return hub;
	}

	public void setHub(Hub hub) {
		this.hub = hub;
		reset();
	}

	public Object getObject() {
		return hubObject;
	}

	public String getPropertyPath() {
		return propertyPath;
	}

	public void setPropertyPath(String propPath) {
		propertyPath = propPath;
		reset();
	}

	public OAWebComponent getComponent() {
		return component;
	}

	public String getEndPropertyName() {
		return endPropertyName;
	}

	public Class getEndPropertyClass() {
		return endPropertyClass;
	}

	public Class getEndPropertyFromClass() {
		return endPropertyFromClass;
	}

	public String getHubListenerPropertyName() {
		return hubListenerPropertyName;
	}

	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	public void close() {
		if (hubObject != null) {
			HubTemp.deleteHub(hubObject);
		}
		if (changeListener != null) {
			changeListener.close();
			changeListener = null;
		}
		if (changeListenerEnabled != null) {
			changeListenerEnabled.close();
			changeListenerEnabled = null;
		}
		if (changeListenerVisible != null) {
			changeListenerVisible.close();
			changeListenerVisible = null;
		}

		enableVisibleListener(false);
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

	public Hub getMultiSelectHub() {
		return hubSelect;
	}

	public boolean getAllowRemovingFromSelectHub() {
		return bAllowRemovingFromSelectHub;
	}

	/*
	 * flag to know if components can remove objects from selectHub, default: true
	 */
	private boolean bAllowRemovingFromSelectHub;

	/**
	 * Sets the MultiSelect that this component will work with.
	 */
	public void setSelectHub(Hub newHub, boolean bAllowRemovingFromSelectHub) {
		this.bAllowRemovingFromSelectHub = bAllowRemovingFromSelectHub;
		if (hubSelect != null) {
			getChangeListener().remove(hubSelect);
		}
		this.hubSelect = newHub;
		if (hubSelect != null) {
			getChangeListener().add(hubSelect);
		}
	}

	public void setSelectHub(Hub newHub) {
		setSelectHub(newHub, true);
	}

	public void setMultiSelectHub(Hub newHub) {
		setSelectHub(newHub, true);
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

		if (bIsHubCalc) {
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
	 * Flag to enable undo, default is true.
	 */
	public void setEnableUndo(boolean b) {
		bEnableUndo = b;
	}

	public boolean getEnableUndo() {
		return bEnableUndo;
	}

	/**
	 * Popup message used to confirm button click before running code.
	 */
	public void setConfirmMessage(String msg) {
		confirmMessage = msg;
	}

	/**
	 * Popup message used to confirm button click before running code.
	 */
	public String getConfirmMessage() {
		return confirmMessage;
	}

	@Override
	public void beforePropertyChange(HubEvent e) {
		// TODO Auto-generated method stub
		super.beforePropertyChange(e);
	}

	/*
	public void setNameValueHub(Hub<String> hub) {
	    this.hubNameValue = hub;
	}
	public Hub<String> getNameValueHub() {
	    return hubNameValue;
	}
	*/

	/**
	 * confirm a new change.
	 */
	protected boolean confirmPropertyChange(final Object obj, Object newValue) {
		String confirmMessage = getConfirmMessage();
		String confirmTitle = "Confirm";

		if (obj instanceof OAObject) {
			Object objx = obj;
			String prop;
			if (bUseLinkHub && hubLink != null) {
				prop = linkPropertyName;
			} else {
				if (oaPropertyPath != null && oaPropertyPath.hasLinks()) {
					prop = endPropertyName;
					objx = oaPropertyPath.getLastLinkValue(obj);
				} else {
					prop = propertyPath;
				}
			}
			if (objx instanceof OAObject) {
				OAObjectCallback em = OAObjectCallbackDelegate.getConfirmPropertyChangeObjectCallback(	(OAObject) objx, prop, newValue,
																										confirmMessage, confirmTitle);
				confirmMessage = em.getConfirmMessage();
				confirmTitle = em.getConfirmTitle();
			}
		}

		boolean result = true;
		if (OAString.isNotEmpty(confirmMessage)) {
			if (OAString.isEmpty(confirmTitle)) {
				confirmTitle = "Confirmation";
			}

			if (confirmMessage != null && confirmMessage.indexOf("<%=") >= 0 && obj instanceof OAObject) {
				OATemplate temp = new OATemplate(confirmMessage);
				temp.setProperty("newValue", newValue); // used by <%=$newValue%>
				confirmMessage = temp.process((OAObject) obj);
				if (confirmMessage != null && confirmMessage.indexOf('<') >= 0 && confirmMessage.toLowerCase().indexOf("<html>") < 0) {
					confirmMessage = "<html>" + confirmMessage;
				}
			}

			int x = JOptionPane.showOptionDialog(	getWindow(), confirmMessage, confirmTitle, 0, JOptionPane.QUESTION_MESSAGE, null,
													new String[] { "Yes", "No" }, "Yes");
			result = (x == 0);
		}
		return result;
	}

	protected Window getWindow() {
		if (component == null) {
			return null;
		}
		return OAWebUtil.getWindow(component);
	}

	/**
	 * Used to confirm changing AO when hub is link to another hub.
	 */
	protected boolean confirmHubChangeAO(final Object objNew) {
		if (!bUseLinkHub || hubLink == null) {
			return true;
		}
		if (!(objNew instanceof OAObject)) {
			return true;
		}
		return confirmPropertyChange(hubLink.getAO(), objNew);
	}

	/**
	 * Used to verify a property change.
	 *
	 * @return null if no errors, else error message
	 */
	protected String isValidHubChangeAO(final Object objNew) {
		if (!bUseLinkHub || hubLink == null) {
			return null;
		}
		if (!(objNew instanceof OAObject)) {
			return null;
		}

		Object obj = hubLink.getAO();
		if (!(obj instanceof OAObject)) {
			return null;
		}

		OAObject oaObj = (OAObject) obj;
		OAObjectCallback em = OAObjectCallbackDelegate.getVerifyPropertyChangeObjectCallback(	OAObjectCallback.CHECK_ALL, oaObj,
																								linkPropertyName, null, objNew);

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
	 * Converts a value to correct type needed for setMethod
	 */
	public Object getConvertedValue(Object value, String fmt) {
		if (hubLink == null && !bUseLinkHub) {
			value = OAConv.convert(endPropertyClass, value, fmt);
		}
		return value;
	}

	private HubProp hpViewOnly;

	public void setViewOnly(boolean b) {
		if (b) {
			if (hpViewOnly == null) {
				hpViewOnly = getEnabledChangeListener().addOnlySuperAdmin(); // viewOnly, unless OAContext.SuperAdmin=true
			}
		} else {
			if (hpViewOnly != null) {
				getEnabledChangeListener().remove(hpViewOnly);
				hpViewOnly = null;
			}
		}
	}

	public void setReadOnly(boolean b) {
		setViewOnly(b);
	}

	/**
	 * Used to verify a property change.
	 *
	 * @return null if no errors, else error message
	 */
	public String isValid(final Object obj, Object newValue) {
		if (!bUseObjectCallback) {
			return null;
		}
		if (!(obj instanceof OAObject)) {
			return null;
		}
		OAObject oaObj = (OAObject) obj;

		String fmt = getFormat();
		newValue = getConvertedValue(newValue, fmt);

		Object objx = obj;
		String prop;
		if (hubLink != null) {
			prop = linkPropertyName;
		} else {
			if (oaPropertyPath != null && oaPropertyPath.hasLinks()) {
				prop = endPropertyName;
				objx = oaPropertyPath.getLastLinkValue(obj);
			} else {
				prop = propertyPath;
			}
		}
		String result = null;
		if (objx instanceof OAObject) {
			OAObjectCallback em = OAObjectCallbackDelegate.getVerifyPropertyChangeObjectCallback(	OAObjectCallback.CHECK_ALL,
																									(OAObject) objx,
																									prop, null, newValue);
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
		}
		return result;
	}

	private boolean bDefaultFormat;
	private String defaultFormat;

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
			if (defaultFormat == null) {
				defaultFormat = OAConverter.getFormat(endPropertyClass);
			}
		}

		if (bUseObjectCallback) {
			Object objx = hub.getAO();
			if (objx instanceof OAObject) {
				String prop;
				if (oaPropertyPath != null && oaPropertyPath.hasLinks()) {
					objx = oaPropertyPath.getLastLinkValue(objx);
				}
				if (objx instanceof OAObject) {
					return OAObjectCallbackDelegate.getFormat((OAObject) objx, endPropertyName, defaultFormat);
				}
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
		if (OACompare.isNotEqual(this.format, old)) {
			callUpdate();
		}
	}

	/**
	 * Utility used to "see" if this component or any of its parent containers are disabled.
	 */
	public static boolean isParentEnabled(OAWebComponent comp) {
		// if any parent is disabled, then this must be disabled
		if (comp == null) {
			return false;
		}
		OAWebComponent cont = comp.getParent();
		for (; cont != null;) {
			if (!cont.isEnabled()) {
				return false;
			}
			cont = cont.getParent();
		}

		if (comp instanceof OATableComponent) {
			OATable tab = ((OATableComponent) comp).getTable();
			if (tab != null) {
				return isParentEnabled(tab);
			}
		}
		return true;
	}

	public void setFont(Font font) {
		Font old = this.font;
		this.font = font;
		if (OACompare.isNotEqual(this.font, old)) {
			callUpdate();
		}
	}

	public Font getFont() {
		return this.font;
	}

	public void setFontPropertyPath(String pp) {
		String old = this.fontPropertyPath;
		fontPropertyPath = pp;
		if (OAString.isNotEmpty(pp)) {
			getChangeListener().add(hub, pp);
		}
		if (OACompare.isNotEqual(this.fontPropertyPath, old)) {
			callUpdate();
		}
	}

	public String getFontProperty() {
		return fontPropertyPath;
	}

	public Font getFont(Object obj) {
		if (OAString.isEmpty(fontPropertyPath)) {
			return this.font;
		}
		obj = getRealObject(obj);
		if (obj == null || obj instanceof OANullObject) {
			return this.font;
		}
		if (!(obj instanceof OAObject)) {
			return this.font;
		}
		if (hub == null) {
			return this.font;
		}

		Object objx = ((OAObject) obj).getProperty(fontPropertyPath);
		Font font = (Font) OAConv.convert(Font.class, objx);
		return font;
	}

	public void setForegroundColor(Color c) {
		Color old = this.colorForeground;
		this.colorForeground = c;
		if (OACompare.isNotEqual(this.colorForeground, old)) {
			callUpdate();
		}
	}

	public Color getForegroundColor() {
		return this.colorForeground;
	}

	public void setForegroundColorPropertyPath(String pp) {
		String old = this.foregroundColorPropertyPath;
		this.foregroundColorPropertyPath = pp;
		if (OAString.isNotEmpty(pp)) {
			getChangeListener().add(hub, pp);
		}
		if (OACompare.isNotEqual(this.foregroundColorPropertyPath, old)) {
			callUpdate();
		}
	}

	public String getForegroundColorPropertyPath() {
		return foregroundColorPropertyPath;
	}

	public Color getForegroundColor(Object obj) {
		if (OAString.isEmpty(foregroundColorPropertyPath)) {
			return this.colorForeground;
		}
		obj = getRealObject(obj);
		if (obj == null || obj instanceof OANullObject) {
			return this.colorForeground;
		}
		if (!(obj instanceof OAObject)) {
			return this.colorForeground;
		}
		if (hub == null) {
			return this.colorForeground;
		}

		Object objx = ((OAObject) obj).getProperty(foregroundColorPropertyPath);
		Color color = (Color) OAConv.convert(Color.class, objx);
		return color;
	}

	public void setBackgroundColor(Color c) {
		Color old = this.colorBackground;
		this.colorBackground = c;
		if (OACompare.isNotEqual(this.colorBackground, old)) {
			callUpdate();
		}
	}

	public Color getBackgroundColor() {
		return this.colorBackground;
	}

	public void setBackgroundColorPropertyPath(String pp) {
		String old = this.backgroundColorPropertyPath;
		this.backgroundColorPropertyPath = pp;
		if (OAString.isNotEmpty(pp)) {
			getChangeListener().add(hub, pp);
		}
		if (OACompare.isNotEqual(this.backgroundColorPropertyPath, old)) {
			callUpdate();
		}
	}

	public String getBackgroundColorPropertyPath() {
		return backgroundColorPropertyPath;
	}

	public Color getBackgroundColor(Object obj) {
		if (OAString.isEmpty(backgroundColorPropertyPath)) {
			return this.colorBackground;
		}
		obj = getRealObject(obj);
		if (obj == null || obj instanceof OANullObject) {
			return this.colorBackground;
		}
		if (!(obj instanceof OAObject)) {
			return this.colorBackground;
		}
		if (hub == null) {
			return this.colorBackground;
		}

		Object objx = ((OAObject) obj).getProperty(backgroundColorPropertyPath);
		Color color = (Color) OAConv.convert(Color.class, objx);
		return color;
	}

	public void setIconColor(Color c) {
		Color old = this.colorIcon;
		this.colorIcon = c;
		if (OACompare.isNotEqual(this.colorIcon, old)) {
			callUpdate();
		}
	}

	public Color getIconColor() {
		return this.colorIcon;
	}

	public void setIconColorPropertyPath(String pp) {
		String old = iconColorPropertyPath;
		iconColorPropertyPath = pp;
		if (OAString.isNotEmpty(pp)) {
			getChangeListener().add(hub, pp);
		}
		if (OACompare.isNotEqual(this.iconColorPropertyPath, old)) {
			callUpdate();
		}
	}

	public String getIconColorPropertyPath() {
		return iconColorPropertyPath;
	}

	public Color getIconColor(Object obj) {
		if (OAString.isEmpty(iconColorPropertyPath)) {
			return this.colorIcon;
		}
		obj = getRealObject(obj);
		if (obj == null || obj instanceof OANullObject) {
			return this.colorIcon;
		}
		if (!(obj instanceof OAObject)) {
			return this.colorIcon;
		}
		if (hub == null) {
			return this.colorIcon;
		}

		Object objx = ((OAObject) obj).getProperty(iconColorPropertyPath);
		Color color = (Color) OAConv.convert(Color.class, objx);
		return color;
	}

	public void setToolTipTextPropertyPath(String pp) {
		String old = this.toolTipTextPropertyPath;
		this.toolTipTextPropertyPath = pp;
		if (OAString.isNotEmpty(pp)) {
			getChangeListener().add(hub, pp);
		}
		if (OACompare.isNotEqual(this.toolTipTextPropertyPath, old)) {
			callUpdate();
		}
	}

	public String getToolTipTextPropertyPath() {
		return toolTipTextPropertyPath;
	}

	/**
	 * Root directory path where images are stored.
	 */
	public void setImageDirectory(String s) {
		String old = this.imageDirectory;
		if (s != null) {
			s += "/";
			s = OAString.convert(s, "\\", "/");
			s = OAString.convert(s, "//", "/");
		}
		this.imageDirectory = s;
		if (OACompare.isNotEqual(this.imageDirectory, old)) {
			callUpdate();
		}
	}

	/**
	 * Root directory path where images are stored.
	 */
	public String getImageDirectory() {
		return imageDirectory;
	}

	/**
	 * Class path where images are stored.
	 */
	public void setImageClassPath(Class root, String path) {
		String old = this.imageClassPath;
		this.rootImageClassPath = root;
		this.imageClassPath = path;
		if (OACompare.isNotEqual(this.imageClassPath, old)) {
			callUpdate();
		}
	}

	public void setImage(Image img) {
		Image old = this.image;
		this.image = img;
		if (OACompare.isNotEqual(this.image, old)) {
			callUpdate();
		}
	}

	public Image getImage() {
		return this.image;
	}

	public void setImagePropertyPath(String pp) {
		String old = this.imagePropertyPath;
		this.imagePropertyPath = pp;
		if (OAString.isNotEmpty(pp)) {
			getChangeListener().add(hub, pp);
		}
		if (OACompare.isNotEqual(this.imagePropertyPath, old)) {
			callUpdate();
		}
	}

	public String getImagePropertyPath() {
		return imagePropertyPath;
	}

	public Icon getIcon() {
		if (this.image != null) {
			ImageIcon ii = new ImageIcon(this.image);
			return ii;
		}
		Color color = getIconColor();
		if (color == null) {
			return null;
		}

		ColorIcon ci = new ColorIcon();
		ci.setColor(color);
		return ci;
	}

	public Icon getIcon(Object obj) {
		Icon icon = _getIcon(obj);
		if (icon != null && (maxImageWidth > 0 || maxImageHeight > 0)) {
			icon = new ScaledImageIcon(icon, maxImageWidth, maxImageHeight);
		}
		return icon;
	}

	private Icon _getIcon(Object object) {
		object = getRealObject(object);
		if (object == null || object instanceof OANullObject) {
			return null;
		}
		if (!(object instanceof OAObject)) {
			return null;
		}
		if (hub == null) {
			return null;
		}

		OAObject obj = (OAObject) object;

		Icon icon = null;
		if (iconColorPropertyPath != null) {
			Color color = getIconColor(obj);
			if (color == null) {
				color = Color.white;
			}
			if (myColorIcon == null) {
				myColorIcon = new ColorIcon();
			}
			myColorIcon.setColor(color);
			icon = myColorIcon;
		}

		Icon icon2 = null;

		Object objx = obj.getProperty(propertyPath);

		if (objx instanceof Icon) {
			icon2 = (Icon) objx;
		} else if (objx instanceof byte[]) {
			byte[] bs = (byte[]) objx;
			try {
				Image img = OAImageUtil.convertToBufferedImage(bs);
				if (img != null) {
					icon2 = new ImageIcon(img);
				}
			} catch (IOException ex) {
			}
		} else if (OAString.isNotEmpty(getImageDirectory()) && objx instanceof String && ((String) objx).length() > 0) {
			String s = (String) objx;
			if (OAString.isNotEmpty(getImageDirectory())) {
				s = getImageDirectory() + "/" + s;
			}
			URL url = OAWebController.class.getResource(s);
			if (url != null) {
				icon2 = new ImageIcon(url);
			} else {
				s = OAString.convertFileName(s);
				icon2 = new ImageIcon(s);
			}
		}

		if (icon == null) {
			icon = icon2;
			icon2 = null;
		} else if (icon2 != null) {
			if (myMultiIcon == null) {
				myMultiIcon = new MultiIcon();
			}
			myMultiIcon.setIcon1(icon);
			myMultiIcon.setIcon2(icon2);
			icon = myMultiIcon;
		}
		return icon;
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



	public HubProp addEnabledCheck(Hub hub, String pp) {
		return getEnabledChangeListener().add(hub, pp);
	}

	public HubProp addEnabledCheck(Hub hub, String pp, Object value) {
		return getEnabledChangeListener().add(hub, pp, value);
	}

	public HubProp addEnabledCheck(Hub hub, String property, HubChangeListener.Type type) {
		return getEnabledChangeListener().add(hub, property, type);
	}

	public HubProp addEnabledCheck(Hub hub, HubChangeListener.Type type) {
		return getEnabledChangeListener().add(hub, type);
	}

	public HubProp addEnabledEditQueryCheck(Hub hub, String propertyName) {
		return getEnabledChangeListener().addObjectCallbackEnabled(hub, propertyName);
	}

	public HubProp addEnabledObjectCallbackCheck(Hub hub, String propertyName) {
		return getEnabledChangeListener().addObjectCallbackEnabled(hub, propertyName);
	}

	public HubProp addVisibleCheck(Hub hub, String pp) {
		return getVisibleChangeListener().add(hub, pp);
	}

	public HubProp addVisibleCheck(Hub hub, String pp, Object value) {
		return getVisibleChangeListener().add(hub, pp, value);
	}

	public HubProp addVisibleCheck(Hub hub, String property, HubChangeListener.Type type) {
		return getVisibleChangeListener().add(hub, property, type);
	}

	public HubProp addVisibleEditQueryCheck(Hub hub, String propertyName) {
		return getVisibleChangeListener().addObjectCallbackVisible(hub, propertyName);
	}

	public HubProp addVisibleObjectCallbackCheck(Hub hub, String propertyName) {
		return getVisibleChangeListener().addObjectCallbackVisible(hub, propertyName);
	}

	private HubEvent lastUpdateHubEvent;
	private final AtomicBoolean abUpdate = new AtomicBoolean(false);

	protected void callUpdate() {
		if (bIgnoreUpdate) {
			return;
		}
		update();
	}

	/**
	 * Called to have component update itself.
	 */
	public void update() {
		if (bIgnoreUpdate) {
			return;
		}
		final HubEvent he = OAThreadLocalDelegate.getCurrentHubEvent();
		if (lastUpdateHubEvent != null && (he == lastUpdateHubEvent)) {
			return;
		}

		lastUpdateHubEvent = he;

		if (hmVisibleListener != null) {
			// check to see if component is visible
			if (!isVisibleOnScreen()) {
				return;
			}
		}

		if (component == null) {
			return;
		}

		Object obj;
		if (hub != null) {
			obj = hub.getAO();
		} else {
			obj = null;
		}
		update(component, obj, true);
		updateEnabled();
		updateVisible();
		updateLabel(component, obj);
		debug();
	}

	protected void debug() {
		if ((!DEBUGUI && !DEBUG) || component == null) {
			return;
		}

		if (label != null) {
			label.setBorder(new LineBorder(Color.green, 2));
		} else {
			component.setBorder(new LineBorder(Color.green, 2));
		}

		String tt;
		if (component != null) {
			tt = "Comp=" + component.getClass().getSimpleName();
		} else {
			tt = "no component";
		}

		tt += "<br>Hub=" + OAString.trunc(getHub() + "", 80);
		if (OAString.isNotEmpty(propertyPath)) {
			tt += "<br>Prop=" + propertyPath;
		}

		if (changeListener != null) {
			String s = changeListener.getToolTipText();
			if (OAString.isNotEmpty(s)) {
				tt += "<br>" + "hcl=" + s;
			}
		}
		if (changeListenerEnabled != null) {
			String s = changeListenerEnabled.getToolTipText();
			if (OAString.isNotEmpty(s)) {
				tt += "<br>" + "Enabled=" + s;
			}
		}
		if (changeListenerVisible != null) {
			String s = changeListenerVisible.getToolTipText();
			if (OAString.isNotEmpty(s)) {
				tt += "<br>" + "Visible=" + s;
			}
		}
		if (label != null) {
			label.setToolTipText("<html>" + tt);
		} else {
			component.setToolTipText("<html>" + tt);
		}
	}

	public void updateLabel(final OAWebComponent comp, Object object) {
		OALabel lbl = getLabel();
		if (lbl != null) {
			OAObject oaobj = null;
			if (object instanceof OAObject) {
				oaobj = (OAObject) object;
			}
			String s = bUseLinkHub ? linkPropertyName : propertyPath;
			//qqqqqqqqq OAObjectCallbackDelegate.updateLabel(oaobj, s, lbl);
		}
	}

	/**
	 * @param comp can be used for this.component, or another, ex: an OAList renderer (label)
	 */
	public void update(final OAWebComponent comp, Object object, boolean bIncudeToolTip) {
		if (comp == null) {
			return;
		}
		object = getRealObject(object);
		Font font = getFont(object);
		if (font != null) {
			comp.setFont(font);
		}

		OALabel lblThis;
		if (comp instanceof OALabel) {
			lblThis = (OALabel) comp;
		} else {
			lblThis = null;
		}

		if (lblThis != null) {
			lblThis.setIcon(getIcon(object));
		}
		Color c = getBackgroundColor(object);
		if (c != null) {
			comp.setBackground(c);
		}
		c = getForegroundColor(object);

		if (c != null) {
			comp.setForeground(c);
		}

		// tooltip
		if (bIncudeToolTip) {
			String tt = component.getToolTipText();
			//was: String tt = comp.getToolTipText();
			tt = getToolTipText(object, tt);
			component.setToolTipText(tt);
			//was: comp.setToolTipText(tt);
			if (comp == component && label != null) {
				label.setToolTipText(tt);
			}
		}

		if (lblThis != null && (getPropertyPath() != null || object instanceof String) && !HubLinkDelegate.getLinkedOnPos(hub)) {
			String text;
			if (object == null) {
				text = "";
			} else {
				OATemplate temp = getTemplateForDisplay();
				if (temp != null && (object instanceof OAObject)) {
					text = templateDisplay.process((OAObject) object);
					if (text != null && text.indexOf('<') >= 0 && text.toLowerCase().indexOf("<html>") < 0) {
						text = "<html>" + text;
					}
				} else {
					if ((object instanceof OAObject) && oaPropertyPath != null && oaPropertyPath.getHasHubProperty()) {
						// 20190110 useFinder for pp with hubs
						VString vs = new VString();
						OAFinder finder = new OAFinder(oaPropertyPath.getPropertyPathLinksOnly()) {
							@Override
							protected void onFound(OAObject obj) {
								Object objx = obj.getProperty(oaPropertyPath.getLastPropertyName());
								String s = OAConv.toString(objx, getFormat());
								vs.setValue(OAString.concat(vs.getValue(), s, ", "));
							}
						};
						finder.find((OAObject) object);
						text = vs.getValue();
					} else {
						Object obj = getValue(object);
						text = OAConv.toString(obj, getFormat());
					}
				}
				if (text == null) {
					String s = getFormat();
					if (OAString.isNotEmpty(s)) {
						text = OAConv.toString(null, s);
					}
				}
			}
			lblThis.setText(text);

			if (object != null) {
				if (bUseObjectCallback) {
					try {
						if (object instanceof OAObject) {
							Object objx = object;
							if (oaPropertyPath != null && oaPropertyPath.hasLinks()) {
								objx = oaPropertyPath.getLastLinkValue(objx);
							}
							if (objx instanceof OAObject) {
							  //qqqqqqqqq OAObjectCallbackDelegate.renderLabel((OAObject) objx, endPropertyName, lblThis);
							}
						}
					} catch (Exception e) {
						System.out.println("OAJfcController.update exception: " + e);
					}
				}
				if (lblThis instanceof OAWebComponent) {
					int pos = getHub().getPos(object);
					//qqqqqqqqq ((OAWebComponent) lblThis).customizeRenderer(lblThis, object, object, false, false, pos, false, false);
				}
			}
		}
	}

	private void customizeRenderer(OALabel lblThis, Object object, Object object2, boolean b, boolean c, int pos, boolean d, boolean e) {
        // TODO Auto-generated method stub
        
    }


	public String getEnabledMessage() {
		return enabledMessage;
	}

	public String getVisibleMessage() {
		return visibleMessage;
	}

	private HubEvent lastUpdateEnabledHubEvent;
	private boolean bLastUpdateEnabled;

	public boolean updateEnabled() {
		final HubEvent he = OAThreadLocalDelegate.getCurrentHubEvent();
		if (he == null || he != lastUpdateEnabledHubEvent) {
			lastUpdateEnabledHubEvent = he;
			bLastUpdateEnabled = updateEnabled(component, hub == null ? null : hub.getAO());
		}
		return bLastUpdateEnabled;
	}

	public boolean updateEnabled(final OAWebComponent comp, final Object object) {
		if (comp == null) {
			return false;
		}
		boolean bEnabled = getEnabledChangeListener().getValue();
		bEnabled = isEnabled(bEnabled);

		final boolean bEnabledOrig = bEnabled;
		if (comp instanceof OATextField) {
		    OATextField txt = (OATextField) comp;
			if (!bEnabled) {
				// need to see if it should call setEditable(b) instead
				if (getHub().getAO() != null) {
					txt.setEditable(false);
					bEnabled = true;
				}
			} else {
				if (!txt.isEditable()) {
					txt.setEditable(true);
				}
			}
		}
		if (comp.isEnabled() != bEnabled) {
			if (!(comp instanceof OALabel)) {
				comp.setEnabled(bEnabled);
			}
		}
		if (comp instanceof OAWebComponent) {
		    OAWebController jc = ((OAWebComponent) comp).getController();
			if (jc != null) {
				OALabel lbl = jc.getLabel();
				boolean b = bEnabledOrig;
				if (!b && !bLabelAlwaysMatchesComponentEnabled
						&& ((hubChangeListenerType == null || hubChangeListenerType == HubChangeListener.Type.AoNotNull)
								|| (hubChangeListenerType == HubChangeListener.Type.HubValid))) {
					Hub h = getHub();
					if (h != null && h.isValid() && h.getAO() != null) {
						b = true;
					}
				}
				if (!b && hubForLabel != null) {
					b = hubForLabel.isValid() && hubForLabel.getAO() != null;
				}
				if (lbl != null && lbl.isEnabled() != b) {
					lbl.setEnabled(b);
				}
			}
		}
		return bEnabledOrig;
	}

	private OAWebController getController() {
        // TODO Auto-generated method stub
        return null;
    }

    // called by updateEnabled to allow it to be overwritten
	protected boolean isEnabled(boolean defaultValue) {
		return defaultValue;
	}

	private HubEvent lastUpdateVisibleHubEvent;
	private boolean bLastUpdateVisible;

	public boolean updateVisible() {
		final HubEvent he = OAThreadLocalDelegate.getCurrentHubEvent();
		if (he == null || he != lastUpdateVisibleHubEvent) {
			lastUpdateVisibleHubEvent = he;
			Object obj;
			if (bUseLinkHub && hubLink != null) {
				obj = hubLink.getAO();
			} else if (hub != null) {
				obj = hub.getAO();
			} else {
				obj = null;
			}
			bLastUpdateVisible = updateVisible(component, obj);
		}
		return bLastUpdateVisible;
	}

	public boolean updateVisible(final OAWebComponent comp, final Object object) {
		if (comp == null) {
			return false;
		}
		boolean bVisible = getVisibleChangeListener().getValue();
		bVisible = isVisible(bVisible);

		if (comp.isVisible() != bVisible) {
			comp.setVisible(bVisible);
		}
		if (comp == component) {
			OALabel lbl = getLabel();
			if (lbl != null && lbl.isVisible() != bVisible) {
				lbl.setVisible(bVisible);
			}
		} else if (comp instanceof OAWebComponent) {
		    OAWebController jc = ((OAWebComponent) comp).getController();
			if (jc != null) {
				OALabel lbl = jc.getLabel();
				if (lbl != null && lbl.isVisible() != bVisible) {
					lbl.setVisible(bVisible);
				}
			}
		}

		int i = 0;
		for (OAWebComponent cp = comp.getParent(); cp != null && i < 5; cp = cp.getParent(), i++) {
			if (!(cp instanceof OAResizePanel)) {
				continue;
			}
			OAResizePanel rp = (OAResizePanel) cp;
			boolean bRpVisible = rp.areAnyChildrenVisible();
			if (bRpVisible != rp.isVisible()) {
				boolean b = true;
				if (rp.getParent() instanceof OATabbedPane) {
					OATabbedPane tp = (OATabbedPane) rp.getParent();
					b = tp.getSelectedComponent() == rp;
				}
				if (b) {
					rp.setVisible(bRpVisible);
				}
			}
			break;
		}
		return bVisible;
	}

	// called by updateVisible to allow it to be overwritten
	protected boolean isVisible(boolean defaultValue) {
		return defaultValue;
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

	public int getPropertyInfoMaxLength() {
		if (propertyInfoMaxColumns == -2) {
			Hub h = getHub();
			if (h == null) {
				return propertyInfoMaxColumns;
			}

			OAObjectInfo oi = OAObjectInfoDelegate.getOAObjectInfo(h.getObjectClass());
			OAPropertyInfo pi = oi.getPropertyInfo(endPropertyName);

			propertyInfoMaxColumns = (pi == null) ? -1 : pi.getMaxLength();
			propertyInfoDisplayColumns = (pi == null) ? -1 : pi.getDisplayLength();

			if (endPropertyClass != null) {
				if (endPropertyClass.equals(String.class)) {
					if (propertyInfoMaxColumns > 254) {
						propertyInfoMaxColumns = 254;
					}
				} else {
					propertyInfoMaxColumns = -1;
				}
			}
		}
		return propertyInfoMaxColumns;
	}

	public int getDataSourceMaxColumns() {
		if (dataSourceMaxColumns == -2) {

			// annotation OAColumn has ds max length
			int x = getPropertyInfoMaxLength();
			if (x > 0) {
				dataSourceMaxColumns = x;
				return x;
			}

			Hub h = getHub();
			if (h == null) {
				return dataSourceMaxColumns;
			}
			OADataSource ds = OADataSource.getDataSource(h.getObjectClass());
			if (ds != null) {
				dataSourceMaxColumns = -1;
				dataSourceMaxColumns = ds.getMaxLength(h.getObjectClass(), endPropertyName);
				if (endPropertyClass != null) {
					if (endPropertyClass.equals(String.class)) {
						if (dataSourceMaxColumns > 254) {
							dataSourceMaxColumns = -1;
						}
					} else {
						dataSourceMaxColumns = -1;
					}
				}
			}
		}
		return dataSourceMaxColumns;
	}

	/**
	 * Label that is used with component, so that enabled and visible will be applied.
	 *
	 * @see #setLabel(JLabel, boolean) to be able to set flag that will have the label disabled when this component is disabled.
	 */
	public void setLabel(OALabel lbl) {
		setLabel(lbl, false, null);
	}

	public void setLabel(OALabel lbl, boolean bAlwaysMatchEnabled) {
		setLabel(lbl, bAlwaysMatchEnabled, null);
	}

	public void setLabel(OALabel lbl, Hub hubForLabel) {
		setLabel(lbl, false, hubForLabel);
	}

	public void setLabel(OALabel lbl, boolean bAlwaysMatchEnabled, Hub hubForLabel) {
		this.label = lbl;
		lbl.setLabelFor(component);
		this.hubForLabel = hubForLabel;
		this.bLabelAlwaysMatchesComponentEnabled = bAlwaysMatchEnabled;
		callUpdate();
	}

	public OALabel getLabel() {
		return this.label;
	}

	private Border borderFocus;

	public OAWebComponent getTableRenderer(OALabel label, OATable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (!isSelected && !hasFocus) {
		    /*qqqqqqqqqqq
			label.setForeground(UIManager.getColor(table.getForeground()));
			label.setBackground(UIManager.getColor(table.getBackground()));
			*/
		}

		Object obj = value;

		// label.setHorizontalTextPosition(label.getHorizontalTextPosition());
		label.setIcon(getIcon(getHub().elementAt(row)));

		Hub h = getHub(); // could be a link hub
		if (table instanceof OATable) {
			h = ((OATable) table).getHub();
		}

		obj = h.elementAt(row);
		update(label, obj, false);

		if (isSelected || hasFocus) {
			label.setForeground(UIManager.getColor("Table.selectionForeground"));
			label.setBackground(UIManager.getColor("Table.selectionBackground"));
		}
		if (hasFocus) {
			if (borderFocus == null) {
				borderFocus = new CompoundBorder(UIManager.getBorder("Table.focusCellHighlightBorder"),
						new LineBorder(UIManager.getColor("Table.focusCellBackground"), 1));
			}
			label.setBorder(borderFocus);
		} else {
			label.setBorder(null);
		}

		return label;
	}

	public int getMaxImageHeight() {
		return maxImageHeight;
	}

	public void setMaxImageHeight(int maxImageHeight) {
		int old = this.maxImageHeight;
		this.maxImageHeight = maxImageHeight;
		if (OACompare.isNotEqual(this.maxImageHeight, old)) {
			callUpdate();
		}
	}

	public int getMaxImageWidth() {
		return maxImageWidth;
	}

	public void setMaxImageWidth(int maxImageWidth) {
		int old = this.maxImageWidth;
		this.maxImageWidth = maxImageWidth;
		if (OACompare.isNotEqual(this.maxImageWidth, old)) {
			callUpdate();
		}
	}

	public void setHtml(boolean b) {
		this.bHtml = b;
	}

	public boolean getHtml() {
		return this.bHtml;
	}

	/**
	 * Description to use for Undo and Redo presentation names.
	 *
	 * @see OAUndoableEdit#setPresentationName
	 */
	public void setUndoDescription(String s) {
		undoDescription = s;
	}

	/**
	 * Description to use for Undo and Redo presentation names.
	 *
	 * @see OAUndoableEdit#setPresentationName
	 */
	public String getUndoDescription() {
		return undoDescription;
	}

	@Override
	public void afterAdd(HubEvent e) {
		if (bIsHubCalc) {
			callUpdate();
		} else if (bListenToHubSize) {
			if (getHub().size() == 1) {
				callUpdate();
			}
		}
	}

	@Override
	public void afterRemove(HubEvent e) {
		if (bIsHubCalc) {
			callUpdate();
		} else if (bListenToHubSize) {
			if (getHub().size() == 0) {
				callUpdate();
			}
		}
	}

	@Override
	public void afterRemoveAll(HubEvent e) {
		if (bIsHubCalc) {
			callUpdate();
		} else if (bListenToHubSize) {
			callUpdate();
		}
	}

	@Override
	public void onNewList(HubEvent e) {
		callUpdate();
	}

	@Override
	public void afterInsert(HubEvent e) {
		if (bIsHubCalc) {
			callUpdate();
		} else if (bListenToHubSize) {
			if (getHub().size() == 1) {
				callUpdate();
			}
		}
	}

	private final AtomicBoolean abAfterChangeAO = new AtomicBoolean(false);

	@Override
	public void afterChangeActiveObject(HubEvent e) {
		if (!SwingUtilities.isEventDispatchThread()) {
			if (!abAfterChangeAO.compareAndSet(false, true)) {
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					abAfterChangeAO.set(false);
					afterChangeActiveObject();
				}
			});
		} else {
			afterChangeActiveObject();
		}
	}

	@Override
	public void afterNewList(HubEvent e) {
		callUpdate();
	}

	private final AtomicInteger aiAfterPropChange = new AtomicInteger();

	@Override
	public void afterPropertyChange(final HubEvent e) {

		// 20190918
		Object ao = getHub().getAO();
		if (bAoOnly) {
			if (ao == null || e.getObject() != ao) {
				return;
			}
		}
		if (!isListeningTo(hub, e.getObject(), e.getPropertyName())) {
			return;
		}

		if (!SwingUtilities.isEventDispatchThread()) {
			final int x = aiAfterPropChange.incrementAndGet();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (x == aiAfterPropChange.get()) {
						_afterPropertyChange(e);
					}
				}
			});
		} else {
			_afterPropertyChange(e);
		}
	}

	private void _afterPropertyChange(HubEvent e) {
		afterPropertyChange();
		callUpdate();
	}

	// called if the actual property is changed in the actualHub.activeObject
	protected void afterPropertyChange() {
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

		OAWebComponent comp = getComponent();
		if (OAString.isEmpty(ttDefault) && comp instanceof OAWebComponent) {
			String s = ((OAWebComponent) comp).getToolTipText();
			if (OAString.isNotEmpty(s)) {
				ttDefault = s;
			}
		}

		Object objx = obj;

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

	/**
	 * Used when enabledVisibleListener(true) is set up. WIll be called when component is in a visible window/tab. default is to call
	 * update().
	 */
	protected void onVisibleListenerChange() {
		callUpdate();
	}

	private HashMap<OAWebComponent, Object> hmVisibleListener;

	/**
	 * for components that need to know if it is visible on the screen. currently checks tabbedPanels and windows.
	 */
	public void enableVisibleListener(boolean b) {
		if (b) {
			if (hmVisibleListener == null) {
				hmVisibleListener = new HashMap<>();
				isVisibleOnScreen();
			}
		} else {
			if (hmVisibleListener == null) {
				return;
			}
			for (Entry<OAWebComponent, Object> entry : hmVisibleListener.entrySet()) {
				OAWebComponent comp = entry.getKey();
				if (comp instanceof OATabbedPane) {
					OATabbedPane tp = (OATabbedPane) comp;
					//qqqqqqqq tp.removeChangeListener((ChangeListener) entry.getValue());
				}
				/*qqqqqqqqqq
				if (comp instanceof Window) {
					Window win = (Window) comp;
					win.removeComponentListener((ComponentListener) entry.getValue());
				}
				*/
			}
			hmVisibleListener = null;
		}
	}

	public boolean isVisibleOnScreen() {
		if (component == null) {
			return false;
		}
		// qqqqqqq see OAJfcController for previous code
		return true; //qqqqqqqqqqqqqq
	}


	
}
