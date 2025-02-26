package com.viaoa.web.html.oa;

public class OATreeTitleNode extends OATreeNode {
    
    public OATreeTitleNode(String path) {
        super(path);
        this.titleFlag = true;
    }
    public String getTitle() {
        return fullPath;
    }
    public void setTitle(String title) {
        this.fullPath = title;
    }

    

}
