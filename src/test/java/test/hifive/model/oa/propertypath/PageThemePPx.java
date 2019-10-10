// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import java.io.Serializable;

import test.hifive.model.oa.*;
 
public class PageThemePPx implements PPxInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public final String pp;  // propertyPath
    private LocationPPx locations;
    private PageThemePageInfoPPx pageThemePageInfos;
    private ProgramPPx programs;
     
    public PageThemePPx(String name) {
        this(null, name);
    }

    public PageThemePPx(PPxInterface parent, String name) {
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

    public LocationPPx locations() {
        if (locations == null) locations = new LocationPPx(this, PageTheme.P_Locations);
        return locations;
    }

    public PageThemePageInfoPPx pageThemePageInfos() {
        if (pageThemePageInfos == null) pageThemePageInfos = new PageThemePageInfoPPx(this, PageTheme.P_PageThemePageInfos);
        return pageThemePageInfos;
    }

    public ProgramPPx programs() {
        if (programs == null) programs = new ProgramPPx(this, PageTheme.P_Programs);
        return programs;
    }

    public String id() {
        return pp + "." + PageTheme.P_Id;
    }

    public String name() {
        return pp + "." + PageTheme.P_Name;
    }

    public String cssFileName() {
        return pp + "." + PageTheme.P_CssFileName;
    }

    @Override
    public String toString() {
        return pp;
    }
}
 