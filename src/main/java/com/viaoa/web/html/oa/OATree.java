package com.viaoa.web.html.oa;

import java.util.*;

import com.viaoa.hub.*;
import com.viaoa.util.*;
import com.viaoa.web.html.*;
import com.viaoa.web.html.oa.OATree.TreeNodeDataChange.Type;

public class OATree extends HtmlElement {
    
    protected final OATreeNode tnRoot;
    protected final OATreeNodeData tndRoot;

    // tracks server side changes to send to client
    protected final List<TreeNodeDataChange> alTreeNodeDataChange = new ArrayList<>();
    protected final Map<OATreeNodeData, TreeNodeDataChange> hmTreeNodeDataChange = new HashMap<>();

    private boolean bIsInitialized;
    private volatile boolean bIgnoreChangeAO;
    
    
    public OATree(String elementIdentifier) {
        super(elementIdentifier);
        
        tnRoot = new OATreeNode("");
        tnRoot.def.tree = this;
        tndRoot = new OATreeNodeData(tnRoot, null, null);
    }
    
    
    /**
     * Returns the <i>invisible</i> root node. The children of this node are the <i>real</i> root nodes for the tree.
     */
    public OATreeNode getRootTreeNode() {
        return tnRoot;
    }

    public OATreeNodeData getRootTreeNodeData() {
        return tndRoot;
    }

    public void add(OATreeNode tn) {
        if (tn.hub == null && !tn.titleFlag) {
            throw new RuntimeException("OATree.add() node is not a TitleNode and does not have a Hub assigned");
        }
        tnRoot.add(tn);
    }

    // recursive search for a treeNode
    public OATreeNode findNode(final OAFilter<OATreeNode> f) {
        OATreeNode tnFound = null;
        for (OATreeNode tn : tnRoot.getChildrenTreeNodes()) {
            tnFound = _findNode(f, tn);
            if (tnFound != null) break;
        }
        return tnFound;
    }
    protected OATreeNode _findNode(final OAFilter<OATreeNode> f, OATreeNode tn) {
        if (f.isUsed(tn)) return tn;
        for (OATreeNode tnx : tn.getChildrenTreeNodes()) {
            OATreeNode tn2 =  _findNode(f, tnx);
            if (tn2 != null) return tn2;
        }
        return null;
    }
    
    
    /**
     * called by JS client.
     */
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        String[] ss = map.get("treePath").split(","); // ex: "0,0,1,4"
        OATreeNodeData tnd;
        if (ss.length == 0)  tnd = null;
        else {
            tnd = this.tndRoot;
            for (String s : ss) {
                int i = OAConv.toInt(s.trim());
                tnd = tnd.getChild(i);
            }
        }
        
        if (Event_Selected.equals(type)) {
            try {
                bIgnoreChangeAO = true;
                _setActiveObjects(tnd);
            }
            finally {
                bIgnoreChangeAO = false;
            }
            onSelectedEvent(tnd);
            if (tnd != null) tnd.node.onSelectedEvent();
        }
        else if (Event_LoadChildren.equals(type)) {
            onLoadChildrenEvent(tnd);
        }
        else if (Event_CheckBoxClicked.equals(type)) {
            onCheckBoxClickedEvent(tnd);
        }
    }

    protected boolean shouldIgnoreChangeAO() {
        return bIgnoreChangeAO;
    }
    
    protected void _setActiveObjects(OATreeNodeData tnd) {
        if (tnd == null) return;
        _setActiveObjects(tnd.parent);
        OATreeNode tn = tnd.node;
        Hub h = tn.getUpdateHub();

        if (h != null) {
            h.setAO(tnd.getObject());
        }
    }
    

    // this can be used to "know" when a tnd is selected.  Hub.AO is already set before calling.
    protected void onSelectedEvent(OATreeNodeData tnd) {
    }
    
    
    /**
     * Used for lazyloading nodes.  This will automatically respond.
     */
    protected void onLoadChildrenEvent(OATreeNodeData tnd) {
        addChange(TreeNodeDataChange.createLoadChildren(tnd));
    }
    
    
    protected void onCheckBoxClickedEvent(OATreeNodeData tnd) {
        int checkTypeNew;
        // toogle existing value
        if (tnd.checkType == 0) checkTypeNew = 2;
        else checkTypeNew = 0;

        _onCheckBoxClickedEvent(tnd, checkTypeNew);
        addChange(OATree.TreeNodeDataChange.createUpdateCheckboxes());
    }
    protected void _onCheckBoxClickedEvent(OATreeNodeData tnd, int checkType) {
        if (tnd.node.getSelectedHub() != null) {
            if (checkType == 0) tnd.node.getSelectedHub().remove(tnd.object);
            else if (checkType == 2) tnd.node.getSelectedHub().add(tnd.object);
        }
        else {
            for (OATreeNodeData tndx : tnd.getChildren()) {
                _onCheckBoxClickedEvent(tndx, checkType);
            }
        }
    }
    
    public boolean isInitialized() {
        return bIsInitialized;
    }
    
    
    @Override
    public String getJavaScriptForClient(final Set<String> hsVars, boolean bHasChanges) { //qqqqqq send Set<String> for all types that need to be created qqqqq
        bIsInitialized = getOAHtmlComponent().isInitialized();

        String js = null;
        if (!bIsInitialized) {
            hsVars.add("objs");
            hsVars.add("tnds");
            hsVars.add("tree");
            
            final StringBuilder sb = new StringBuilder();
            sb.append("tree = comp;\n");
            
            int cnt = 0;
            Set<OATreeNode> hsTreeNode = new HashSet<>();            
            for (OATreeNode tn : tnRoot.getChildrenTreeNodes()) {
                _getJavaScriptForClient_tn(hsVars, tn, sb, 0, hsTreeNode); 
            }
            
            sb.append("tree.clearTreeNodeData();\n");
            sb.append("tree.initialize();\n");
            
            sb.append("objs = [\n");
            int x = tndRoot.getChildCount();
            for (int i=0; i<x; i++) {
                OATreeNodeData tnd = tndRoot.getChild(i);
                _getJavaScriptForClient_tnd(hsVars, tnd, sb, 0); 
            }
            sb.append("];\n");
            sb.append("tnds = tree.convertServerResponseToTreeNodeDatas(objs);\n");
            sb.append("tree.addTreeNodeDatas(tnds);\n");

            js = sb.toString();
            
            synchronized (alTreeNodeDataChange) {
                alTreeNodeDataChange.clear();
                hmTreeNodeDataChange.clear();
            }
        }
        else {
            js = getJavaScriptForClient_changes(hsVars);
        }
        
        bHasChanges |= OAStr.isNotEmpty(js);
        String s = getOAHtmlComponent().getJavaScriptForClient(hsVars, bHasChanges);
        js = OAStr.concat(s, js, "\n");
        
        this.bIsInitialized = getOAHtmlComponent().isInitialized();
        return js;
    }

    
    protected void _getJavaScriptForClient_tn(final Set<String> hsVars, final OATreeNode tn, final StringBuilder sb, final int cnt, final Set<OATreeNode> hsTreeNode) {
        String vname = "tn" + (cnt == 0 ? "" : (""+cnt));
        hsVars.add(vname);
        if (tn.getTitleFlag()) {
            sb.append(vname + " = new OA.OATreeNode();\n");
            sb.append(vname + ".label = '"+tn.fullPath+"';\n");
            sb.append(vname + ".isTitle = true;\n");
        }
        else {
            sb.append(vname + " = new OA.OATreeNode(); // "+tn.fullPath+"\n");
        }
        if (tn.getSelectedHub() != null) {
            sb.append(vname + ".checkable = true;\n");
        }
        if (tn.getLazyLoad()) sb.append(vname + ".lazyLoad = true;\n");
        if (cnt == 0) sb.append("tree.addTreeNode("+vname+");\n");
        else {
            sb.append("tn"+ (cnt==1?"":(""+(cnt-1))) +".addTreeNode("+vname+");\n");
        }

        if (hsTreeNode.add(tn)) {
            for (OATreeNode tnx : tn.getChildrenTreeNodes()) {
                _getJavaScriptForClient_tn(hsVars, tnx, sb, cnt+1, hsTreeNode);
            }
        }

    }

    protected void _getJavaScriptForClient_tnd(final Set<String> hsVars, final OATreeNodeData tnd, final StringBuilder sb, final int cnt) {
        int x = tnd.getChildCount();

        int pos = OAArray.indexOf(tnd.parent.node.getChildrenTreeNodes(), tnd.node);
        String tnpos = pos == 0 ? "" : (" tn: "+pos+", ");

        sb.append(String.format("{%slabel: '%s'", 
            tnpos,
            OAStr.escapeJSON(tnd.toString())
        ));
        
        if (tnd.node.needsCheckBox()) { // 0=unchecked, 1=halfchecked, 2=checked
            int ct = tnd.node.getCheckType(tnd);
            tnd.checkType = ct;
            if (ct > 0) sb.append(String.format(",checkedType: '%d'", ct)); 
        }
        
        
        if (x > 0 && (!tnd.node.getLazyLoad() || cnt == 0)) {
            sb.append(", tnds: [\n");
            for (int i=0; i<x; i++) {
                OATreeNodeData tndx = tnd.getChild(i);
                _getJavaScriptForClient_tnd(hsVars, tndx, sb, cnt+1);
                if (i+1 != x) sb.append(",\n");    
            }
            sb.append("]}");    
        }
        else {
            sb.append("}"); 
        }
    }
    
    
    /**
     * Server side events that affect the client.
     */
    protected static class TreeNodeDataChange {
        OATreeNodeData tnd;
        int posTn;  // index of the TreeNode to use.
        int pos;
        String path;  // ex: "0,0,1,2" - path from tndRoot to find tnd.  For Add|Insert, this will be the tnd.parent
        boolean cancel;

        public static TreeNodeDataChange createAdd(final OATreeNodeData tndNew) {
            TreeNodeDataChange c =  new TreeNodeDataChange(tndNew, Type.add);
            return c;
        }
        public static TreeNodeDataChange createInsert(final OATreeNodeData tndNew) {
            TreeNodeDataChange c =  new TreeNodeDataChange(tndNew, Type.insert);
            return c;
        }
        public static TreeNodeDataChange createRemove(final OATreeNodeData tnd) {
            TreeNodeDataChange c =  new TreeNodeDataChange(tnd, Type.remove);
            return c;
        }
        public static TreeNodeDataChange createChange(final OATreeNodeData tnd) {
            TreeNodeDataChange c =  new TreeNodeDataChange(tnd, Type.change);
            return c;
        }
        public static TreeNodeDataChange createMove(final OATreeNodeData tnd, int newPos) {
            TreeNodeDataChange c =  new TreeNodeDataChange(tnd, Type.move);
            c.pos = newPos;  // position where tnd needs to be after the move.
            return c;
        }
        public static TreeNodeDataChange createLoadChildren(final OATreeNodeData tnd) {
            TreeNodeDataChange c =  new TreeNodeDataChange(tnd, Type.loadChildren);
            return c;
        }
        public static TreeNodeDataChange createExpand(final OATreeNodeData tnd) {
            TreeNodeDataChange c =  new TreeNodeDataChange(tnd, Type.expand);
            return c;
        }
        public static TreeNodeDataChange createSelect(final OATreeNodeData tnd) {
            TreeNodeDataChange c =  new TreeNodeDataChange(tnd, Type.select);
            return c;
        }
        public static TreeNodeDataChange createNewList(final OATreeNodeData tnd) {
            TreeNodeDataChange c =  new TreeNodeDataChange(tnd, Type.newList);
            return c;
        }
        public static TreeNodeDataChange createUpdateCheckboxes() {
            TreeNodeDataChange c =  new TreeNodeDataChange(null, Type.updateCheckboxes);
            return c;
        }
        
        protected TreeNodeDataChange(OATreeNodeData tnd, Type type) {
            this.tnd = tnd;
            this.type = type;
            
            if (tnd != null) {
                this.posTn = OAArray.indexOf(tnd.parent.node.getChildrenTreeNodes(), tnd.node);
                
                OATreeNodeData tndx = tnd;
                if (type == Type.add || type == Type.insert) tndx = tnd.parent;
                
                for (; tndx != null && tndx.parent != null; tndx = tndx.parent) {
                    this.path = OAStr.concat(tndx.parent.getChildIndex(tndx)+"", this.path, ",");
                }
            }
        }
        
        public enum Type {
            add,  // path=parent  
            insert, // path=parent
            remove, // path=node
            change, // path=node
            move, // path=node
            loadChildren, // path=node, load children nodes (lazy load)
            expand, // path=node
            select, // path=node
            newList, // path=node
            updateCheckboxes 
        }
        Type type;

    }

    protected void addChange(TreeNodeDataChange c) {
        if (!isInitialized()) return;
        synchronized (alTreeNodeDataChange) {
            
            
            if (c.type == Type.updateCheckboxes) {
                for (TreeNodeDataChange cx : alTreeNodeDataChange) {
                    if (cx.type == Type.updateCheckboxes) {
                        cx.cancel = true;
                    }
                }
            }
            else {
                TreeNodeDataChange cx = hmTreeNodeDataChange.get(c.tnd);
                if (cx != null) {
                    if (cx.type == Type.add || cx.type == Type.insert || cx.type == Type.loadChildren || cx.type == Type.newList) {
                        if (c.type != Type.move) {
                            return;
                        }
                    }
                }
            }
            alTreeNodeDataChange.add(c);
            if (c.tnd != null) hmTreeNodeDataChange.put(c.tnd, c);
        }
    }
 
    protected String getJavaScriptForClient_changes(final Set<String> hsVars) {
        TreeNodeDataChange[] cs;
        if (alTreeNodeDataChange.size() == 0) return null;
        synchronized (alTreeNodeDataChange) {
            cs = alTreeNodeDataChange.toArray(new TreeNodeDataChange[0]);
            alTreeNodeDataChange.clear();
            hmTreeNodeDataChange.clear();
        }
        
        final StringBuilder sb = new StringBuilder();
        boolean bUpdateCheckboxes = false;
        for (TreeNodeDataChange c : cs) {
            if (c.cancel) continue;

            boolean bLoadChildren = false;
            switch (c.type) {
                case add:
                case insert:
                    bLoadChildren = true;
                    bUpdateCheckboxes = true;
                    hsVars.add("tndParent");
                    sb.append("tndParent = tree.getTreeNodeData(["+c.path+"]);\n");
                    sb.append("tnd = new OA.OATreeNodeData();\n");
                    sb.append("tnd.treeNode = tndParent.treeNode.treeNodes["+c.posTn+"];\n");
                    sb.append("tnd.tndParent = tndParent;\n");
                    sb.append("tnd.label = '"+c.tnd.toString()+"';\n");
                    
                    if (c.type == Type.add) {
                        sb.append("tree.addTreeNodeData(tnd, tndParent);\n");
                    }
                    else {
                        sb.append("tree.insertTreeNodeData(tnd, tndParent.treeNodeDatas["+c.pos+"]);\n");
                    }
                    break;
                case remove:
                    bUpdateCheckboxes = true;
                    sb.append("tnd = tree.getTreeNodeData("+c.path+");\n");
                    sb.append("tree.removeTreeNodeData(tnd);\n");
                    break;
                case change:  
                    sb.append("tnd = tree.getTreeNodeData("+c.path+");\n");
                    sb.append("tnd.label = '" + OAStr.escapeJS(c.tnd.toString(), '\'') + "';\n");
                    sb.append("tree.updateTreeNodeDataLabel(tnd);\n");
                    break;
                case move:
                    sb.append("tnd = tree.getTreeNodeData("+c.path+");\n");
                    sb.append("tree.moveTreeNodeData(tnd, "+c.pos+");\n"); 
                    break;
                case loadChildren:
                    bLoadChildren = true;
                    sb.append("tnd = tree.getTreeNodeData("+c.path+");\n");
                    break;
                case expand:
                    sb.append("tnd = tree.getTreeNodeData("+c.path+");\n");
                    sb.append("tree.expand(tnd);\n");
                    break;
                case select:
                    sb.append("tnd = tree.getTreeNodeData("+c.path+");\n");
                    sb.append("tree.setSelectedFromServer(tnd);\n");
                    break;
                case newList:
                    sb.append("tnd = tree.getTreeNodeData("+c.path+");\n");
                    bLoadChildren = true;
                    bUpdateCheckboxes = true;
//qqqqqqqq
//qqqqqqqqqqqqqqqq remove children & repopulate
                    break;
                case updateCheckboxes:
                    bUpdateCheckboxes = true;
                    break;
                    
            }

            if (bLoadChildren) {
                hsVars.add("objs");
                sb.append("objs = [\n");
                int x = c.tnd.getChildCount();
                for (int i=0; i<x; i++) {
                    OATreeNodeData tnd = c.tnd.getChild(i);
                    _getJavaScriptForClient_tnd(hsVars, tnd, sb, 0); 
                    if (i+1 != x) sb.append(",\n");    
                }
                sb.append("];\n");
                
                hsVars.add("tnds");
                sb.append("tnds = tree.convertServerResponseToTreeNodeDatas(objs, tnd);\n");
                sb.append("tree.addTreeNodeDatas(tnds, tnd);\n");                
            }
        }
        
        if (bUpdateCheckboxes && tnRoot.needsCheckBox()) {
            for (OATreeNodeData tnd : tndRoot.alTreeNodeData) {
                _getJavaScriptForClient_checkChanges(tnd, hsVars, sb, true);
            }
        }
        
        String js = null;
        if (sb.length() > 0) {
            hsVars.add("tree");
            hsVars.add("tnd");
            
            js = "tree = comp;\n";
            js += sb.toString();
        }
        
        return js;
    }
    
    protected void _getJavaScriptForClient_checkChanges(final OATreeNodeData tnd, final Set<String> hsVars, final StringBuilder sb, boolean bChangeOnClient) {
        int ct = tnd.node.getCheckType(tnd);
        
        if (ct != tnd.checkType) {
            tnd.checkType = ct;

            OATreeNodeData tndx = tnd;
            String path = "";
            for (; tndx != null && tndx.parent != null; tndx = tndx.parent) {
                path = OAStr.concat(tndx.parent.getChildIndex(tndx)+"", path, ",");
            }
            if (bChangeOnClient) {
                sb.append("tnd = tree.getTreeNodeData("+path+");\n");
                sb.append("tree.updateTreeNodeDataCheck(tnd, "+ct+");\n");
                if (ct == 0 || ct == 2) bChangeOnClient = false;  // client will change all of the children
            }
        }
        if (tnd.alTreeNodeData !=null) {
            for (OATreeNodeData tndx : tnd.alTreeNodeData) {
                _getJavaScriptForClient_checkChanges(tndx, hsVars, sb, bChangeOnClient);
            }
        }
    }
    
    
    
    public void close() {
        super.close();
        tndRoot.close();
        tnRoot.close();
    }
}
