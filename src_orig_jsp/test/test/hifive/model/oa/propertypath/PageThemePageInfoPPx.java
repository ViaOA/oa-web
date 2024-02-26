// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class PageThemePageInfoPPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private ImageStorePPx imageStore;
    private PageInfoPPx pageInfo;
    private PageThemePPx pageTheme;
    private ProgramDocumentPPx programDocument;
     
    public PageThemePageInfoPPx(String name) {
        this(null, name);
    }

    public PageThemePageInfoPPx(PPxInterface parent, String name) {
        String s = null;
        if (parent != null) {
            s = parent.toString();
        }
        if (s == null) s = "";
        if (name != null) {
            if (s.length() > 0) s += ".";
            s += name;
        }
        pp = s;
    }

    public ImageStorePPx imageStore() {
        if (imageStore == null) imageStore = new ImageStorePPx(this, PageThemePageInfo.P_ImageStore);
        return imageStore;
    }

    public PageInfoPPx pageInfo() {
        if (pageInfo == null) pageInfo = new PageInfoPPx(this, PageThemePageInfo.P_PageInfo);
        return pageInfo;
    }

    public PageThemePPx pageTheme() {
        if (pageTheme == null) pageTheme = new PageThemePPx(this, PageThemePageInfo.P_PageTheme);
        return pageTheme;
    }

    public ProgramDocumentPPx programDocument() {
        if (programDocument == null) programDocument = new ProgramDocumentPPx(this, PageThemePageInfo.P_ProgramDocument);
        return programDocument;
    }

    public String id() {
        return pp + "." + PageThemePageInfo.P_Id;
    }

    public String created() {
        return pp + "." + PageThemePageInfo.P_Created;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 