// Generated by OABuilder
package test.hifive.model.oa.propertypath;
 
import test.hifive.model.oa.*;
 
public class ProgramPageInfoPP {
    private static ImageStorePPx imageStore;
    private static PageInfoPPx pageInfo;
    private static ProgramPPx program;
    private static ProgramDocumentPPx programDocument;
     

    public static ImageStorePPx imageStore() {
        if (imageStore == null) imageStore = new ImageStorePPx(ProgramPageInfo.P_ImageStore);
        return imageStore;
    }

    public static PageInfoPPx pageInfo() {
        if (pageInfo == null) pageInfo = new PageInfoPPx(ProgramPageInfo.P_PageInfo);
        return pageInfo;
    }

    public static ProgramPPx program() {
        if (program == null) program = new ProgramPPx(ProgramPageInfo.P_Program);
        return program;
    }

    public static ProgramDocumentPPx programDocument() {
        if (programDocument == null) programDocument = new ProgramDocumentPPx(ProgramPageInfo.P_ProgramDocument);
        return programDocument;
    }

    public static String id() {
        String s = ProgramPageInfo.P_Id;
        return s;
    }

    public static String created() {
        String s = ProgramPageInfo.P_Created;
        return s;
    }
}
 
