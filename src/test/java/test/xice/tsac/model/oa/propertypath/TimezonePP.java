// Generated by OABuilder
package test.xice.tsac.model.oa.propertypath;
 
import test.xice.tsac.model.oa.*;
 
public class TimezonePP {
    private static SitePPx sites;
     

    public static SitePPx sites() {
        if (sites == null) sites = new SitePPx(Timezone.P_Sites);
        return sites;
    }

    public static String id() {
        String s = Timezone.P_Id;
        return s;
    }

    public static String name() {
        String s = Timezone.P_Name;
        return s;
    }

    public static String utcOffset() {
        String s = Timezone.P_UTCOffset;
        return s;
    }

    public static String displayName() {
        String s = Timezone.P_DisplayName;
        return s;
    }
}
 
