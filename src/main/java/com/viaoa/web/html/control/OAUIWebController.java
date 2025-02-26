package com.viaoa.web.html.control;


import java.io.IOException;

import com.viaoa.hub.*;
import com.viaoa.image.OAImageUtil;
import com.viaoa.object.OAObject;
import com.viaoa.uicontroller.*;
import com.viaoa.util.*;
import com.viaoa.web.html.HtmlElement;

/**
 * Used by oa-web components to bind with OAModel.
 */
public class OAUIWebController extends OAUIController {

    protected HtmlElement htmlElement;  
    private HtmlElement label;
    
    private String title;
    private String description;
    
    protected String font;
    protected String colorBackground;
    protected String colorForeground;
    protected String colorIcon;
    
    protected Image image;

    private String icon;
    
    
    public OAUIWebController(Hub hub, Object object, String propertyPath, boolean bAoOnly, HtmlElement htmlElement, HubChangeListener.Type type,
        final boolean bUseLinkHub, final boolean bUseObjectCallback) {
        super(hub, object, propertyPath, bAoOnly, type, bUseLinkHub, bUseObjectCallback);
        this.htmlElement = htmlElement;
    }
    
    @Override
    protected boolean onConfirmPropertyChangeShowOptionDialog(String confirmMessage, String confirmTitle) {
        //qqqqqqqq
        return true;
    }

    public boolean isParentEnabled() {
        //qqqqqqqqqqqqqqqq
        return true;
    }
 
    public void setFont(String font) {
        String old = this.font;
        this.font = font;
        if (OACompare.isNotEqual(this.font, old)) {
            callUpdate();
        }
    }

    public String getFont() {
        return this.font;
    }

    public String getFont(Object obj) {
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
        String font = OAConv.toString(objx);
        return font;
    }

    public void setForegroundColor(String c) {
        String old = this.colorForeground;
        this.colorForeground = c;
        if (OACompare.isNotEqual(this.colorForeground, old)) {
            callUpdate();
        }
    }

    public String getForegroundColor() {
        return this.colorForeground;
    }

    public String getForegroundColor(Object obj) {
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
        String color = OAConv.toString(objx);
        return color;
    }
    
    public void setBackgroundColor(String c) {
        String old = this.colorBackground;
        this.colorBackground = c;
        if (OACompare.isNotEqual(this.colorBackground, old)) {
            callUpdate();
        }
    }

    public String getBackgroundColor() {
        return this.colorBackground;
    }

    public String getBackgroundColor(Object obj) {
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
        String color = OAConv.toString(objx);
        return color;
    }

    public void setIconColor(String c) {
        String old = this.colorIcon;
        this.colorIcon = c;
        if (OACompare.isNotEqual(this.colorIcon, old)) {
            callUpdate();
        }
    }

    public String getIconColor() {
        return this.colorIcon;
    }
    
    public String getIconColor(Object obj) {
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
        String color = OAConv.toString(objx);
        return color;
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



    public String getIcon() {
        //qqqqqqq
        return null;
    }

    public Icon getIcon(Object obj) {
        /*qqqqq
        Icon icon = _getIcon(obj);
        if (icon != null && (maxImageWidth > 0 || maxImageHeight > 0)) {
            icon = new ScaledImageIcon(icon, maxImageWidth, maxImageHeight);
        }
        return icon;
        */
        return null;
    }

    @Override
    public boolean isVisibleOnScreen() {
        return true;
    }
    
    @Override
    public void enableVisibleListener(boolean b) {
        //qqqqq
    }
    @Override
    public boolean isVisibleListenerEnabled() {
        //qqqqqqqqq
        return false;
    }

    /**
     * Called when UI needs to be updated.
     */
    @Override
    public void updateComponent(Object object) {
        //qqqqqqqqqqqqqqqqqqqqq
    }
    
    @Override
    public void updateLabel(Object object) {
        //qqqqqqqqqqqqqqqqqqqqq
    }
    

    /**
     * Label that is used with component, so that enabled and visible will be applied.
     *
     * @see #setLabel(JLabel, boolean) to be able to set flag that will have the label disabled when this component is disabled.
     */
    public void setLabel(HtmlElement lbl) {
        this.label = lbl;
    }

    public HtmlElement getLabel() {
        return this.label;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }

}
