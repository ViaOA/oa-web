// Generated by OABuilder
package test.xice.tsam.model.oa.propertypath;
 
import test.xice.tsam.model.oa.MRADClientMessage;
import test.xice.tsam.model.oa.propertypath.MRADClientPPx;

import test.xice.tsam.model.oa.*;
 
public class MRADClientMessagePP {
    private static MRADClientPPx mradClient;
    private static MRADClientPPx mradClient2;
     

    public static MRADClientPPx mradClient() {
        if (mradClient == null) mradClient = new MRADClientPPx(MRADClientMessage.P_MRADClient);
        return mradClient;
    }

    public static MRADClientPPx mradClient2() {
        if (mradClient2 == null) mradClient2 = new MRADClientPPx(MRADClientMessage.P_MRADClient2);
        return mradClient2;
    }

    public static String id() {
        String s = MRADClientMessage.P_Id;
        return s;
    }

    public static String created() {
        String s = MRADClientMessage.P_Created;
        return s;
    }

    public static String text() {
        String s = MRADClientMessage.P_Text;
        return s;
    }

    public static String type() {
        String s = MRADClientMessage.P_Type;
        return s;
    }
}
 
