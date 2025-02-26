package com.viaoa.web.html.oa;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.viaoa.hub.*;
import com.viaoa.object.*;
import com.viaoa.util.*;

public class OATreeNode {

    public int bDebug;

    String fullPath; // full path of node
    Hub hub; // could be set by OATree
    boolean titleFlag;
    public Method[] methodsToHub; // methods to find Hub
    public boolean bRecursive;
    public OATreeNode originalNode;

    protected Def def = new Def();
    private HubListener hlSelected;

    class Def { // so that nodes can be reused/recursive
        boolean showAll = true; // if false then only the activeObject is shown, if true then all objects in hub are used
        String propertyPath; // property path after the path to a hub ex: emps.dept.name => dept.name
        Method methodToObject; // method to get to object object. ex: dept.name => dept
        public Method[] methodsToProperty; // methods to get property value from object => name
        public boolean methodsToPropertyNotUsed;
        Hub updateHub; // hub to be notified by OATree when selection is changed
        Hub hubSelected; // hub that has selected/checked tree node items
        // flag to create a hubMerger (if 2 or more hubs in path) - need to also garbage collect it when not used
        // will have to put object in separate Hub to create merger from
        boolean bRequiresHubMerger;
        OATreeNode[] treeNodeChildren = new OATreeNode[0];
        OATree tree; // set by OATree
        String backgroundColorProperty;
        String foregroundColorProperty;
        String imagePath;
        String imageProperty;
        String iconColorProperty;
        String toolTipText;
        String toolTipTextProperty;
        Method[] methodsToFont;
        Method[] methodsToImage;
        Method[] methodsToBackgroundColor;
        Method[] methodsToForegroundColor;
        Method[] methodsToToolTipText;
        Method[] methodsToIconColor;
        String[] dependentProperties;
        String suffix;
        Object updateObject, updateValue;
        Method updateMethod;
        boolean bUseIcon = true;
        int width; // number of chars
        protected int maxImageHeight, maxImageWidth;
        boolean lazyLoad;
    }

    private OATreeNode() {
        // used internally by add(..) when adding itself (recursive)
    }

    public OATreeNode(String path) {
        this(path, null, null);
    }

    public OATreeNode(String path, Hub hub) {
        this(path, hub, null);
    }
    
    public OATreeNode(String path, Hub hub, Hub updateHub) {
        this.fullPath = path;
        this.def.propertyPath = path;
        this.hub = hub;
        setUpdateHub(updateHub);
    }
    
    
    public OATreeNode(OATreeNode originalNode) {
        this.originalNode = originalNode;
    }
    
    public boolean getLazyLoad() {
        return this.def.lazyLoad;
    }
    public void setLazyLoad(boolean b) {
        this.def.lazyLoad = b;
    }

    // used by Hub2TreeNode when checking propertyChanges
    public void setDependentProperties(String... props) {
        def.dependentProperties = props;
    }

    public String[] getDependentProperties() {
        return def.dependentProperties;
    }

    public Method[] getMethodsToHub() {
        return methodsToHub;
    }

    public Method[] getMethodsToProperty() {
        return def.methodsToProperty;
    }

    public Method getMethodToObject() {
        return def.methodToObject;
    }

    public String getPropertyPath() {
        return def.propertyPath;
    }

    public OATree getTree() {
        return def.tree;
    }


    /**
     * Hub that retains list of nodes that have been checked.
     */
    public void setSelectedHub(Hub hub) {
        
        if (hub != null) {
            /*
            OATreeNode tnx = def.tree.findNode(new OAFilter<OATreeNode>() {
                @Override
                public boolean isUsed(OATreeNode tn) {
                    return tn.getSelectedHub() != null;
                }
            });
            if (tnx != null && tnx != this) {
                throw new RuntimeException("can only have one selection hub for a tree");
            }
            */
        }
        
        if (hlSelected != null && this.def.hubSelected != null) {
            this.def.hubSelected.removeHubListener(hlSelected);
            hlSelected = null;
        }

        this.def.hubSelected = hub;
        if (hub != null) {
            hlSelected = new HubListenerAdapter() {
                void updateTree() {
                    if (OATreeNode.this.def.tree != null) {
                        OATreeNode.this.def.tree.addChange(OATree.TreeNodeDataChange.createUpdateCheckboxes());
                    }
                }
                @Override
                public void afterAdd(HubEvent e) {
                    updateTree();
                }
                @Override
                public void afterRemove(HubEvent e) {
                    updateTree();
                }
                @Override
                public void afterRemoveAll(HubEvent e) {
                    updateTree();
                }
                @Override
                public void afterInsert(HubEvent e) {
                    updateTree();
                }
                @Override
                public void onNewList(HubEvent e) {
                    updateTree();
                }
            };
            hub.addHubListener(hlSelected);
        }
    }

    public Hub getSelectedHub() {
        return this.def.hubSelected;
    }

    private static AtomicInteger aiXcnt = new AtomicInteger();
    
    protected boolean needsCheckBox() {
        return _needsCheckBox(aiXcnt.incrementAndGet());

    }

    private boolean _needsCheckBox(int x) {
        if (def.hubSelected != null) {
            return true;
        }
        if (xcnt >= x) {
            return false;
        }
        xcnt = x;
        for (OATreeNode child : def.treeNodeChildren) {
            if (child._needsCheckBox(x)) {
                return true;
            }
        }
        return false;
    }


    protected void checkBoxClicked(OATreeNodeData tnd) {
        if (tnd == null) {
            return;
        }
        if (this.def.hubSelected == null) {
            parentCheckBoxClicked(tnd);
        }
        else {
            if (this.def.hubSelected.contains(tnd.object)) {
                this.def.hubSelected.remove(tnd.object);
            }
            else {
                this.def.hubSelected.add(tnd.object);
            }
        }
    }

    protected void parentCheckBoxClicked(OATreeNodeData tnd) {
        boolean b = _isAnyChecked(tnd);
        _setChildrenCheck(tnd, !b);
    }

    private void _setChildrenCheck(OATreeNodeData tnd, boolean bChecked) {
        tnd.loadChildren();
        for (OATreeNodeData child : tnd.alTreeNodeData) {
            if (child.node.def.hubSelected != null) {
                if (bChecked) {
                    if (!child.node.def.hubSelected.contains(child.object)) {
                        child.node.def.hubSelected.add(child.object);
                    }
                }
                else if (child.node.def.hubSelected.contains(child.object)) {
                    child.node.def.hubSelected.remove(child.object);
                }
            }
            _setChildrenCheck(child, bChecked);
        }
    }

    private boolean _isAnyChecked(OATreeNodeData tnd) {
        if (tnd.alTreeNodeData == null) {
            return false;
        }
        for (OATreeNodeData child : tnd.alTreeNodeData) {
            if (child.node.def.hubSelected != null) {
                if (child.node.def.hubSelected.contains(child.object)) {
                    return true;
                }
            }
            else if (!child.node.needsCheckBox()) {
                continue;
            }
            if (_isAnyChecked(child)) {
                return true;
            }
        }
        return false;
    }

    private static int CHECK_NONE = 0;
    private static int CHECK_EMPTY = 0;
    private static int CHECK_HALF = 1;
    private static int CHECK_FULL = 2;

    protected int getCheckType(final OATreeNodeData tnd) {
        if (!needsCheckBox()) {
            return CHECK_NONE;
        }
        if (tnd.node.def.hubSelected != null) {
            if (tnd.node.def.hubSelected.contains(tnd.object)) {
                return CHECK_FULL;
            }
            else {
                return CHECK_EMPTY;
            }
        }

        int x = _getParentCheckType(tnd, CHECK_NONE);
        if (x == CHECK_NONE) {
            x = CHECK_EMPTY;
        }
        return x;
    }

    private int _getParentCheckType(OATreeNodeData tnd, int result) {
        if (tnd.alTreeNodeData == null) {
            if (!needsCheckBox()) {
                return CHECK_NONE;
            }
            tnd.loadChildren();
        }
        for (OATreeNodeData child : tnd.alTreeNodeData) {
            if (child.node.def.hubSelected != null) {
                if (child.node.def.hubSelected.contains(child.object)) {
                    if (result == CHECK_NONE) {
                        result = CHECK_FULL;
                    }
                    else if (result != CHECK_FULL) {
                        result = CHECK_HALF;
                    }
                }
                else {
                    if (result == CHECK_NONE) {
                        result = CHECK_EMPTY;
                    }
                    else if (result != CHECK_EMPTY) {
                        result = CHECK_HALF;
                    }
                }
            }
            else if (!child.node.needsCheckBox()) {
                continue;
            }
            result = _getParentCheckType(child, result);
        }
        return result;
    }

    public HubFilter getHubFilter(OATreeNodeData parentTnd, Hub hub) {
        return getHubFilter(hub);
    }

    public HubFilter getHubFilter(Hub hubMaster) {
        return null;
    }

    public void setSuffix(String s) {
        def.suffix = s;
    }

    public String getSuffix() {
        return def.suffix;
    }

    public Hub getHub() {
        return hub;
    }

    public Hub getUpdateHub() {
        return def.updateHub;
    }

    public void setUpdateHub(Hub updateHub) {
        if (updateHubListener != null && this.def.updateHub != null) {
            this.def.updateHub.removeHubListener(updateHubListener);
        }
        this.def.updateHub = updateHub;
        setupUpdateHub();
    }

    /**
     * Object to update whenever node is selected.
     */
    public void setUpdateObject(Object object, String property, Object newValue) {
        if (object != null && property != null) {
            def.updateMethod = OAReflect.getMethod(object.getClass(), property);
        }
        else {
            def.updateMethod = null;
        }
    }

    private HubListener updateHubListener;

    protected void setupUpdateHub() {
        if (def.updateHub == null) {
            return;
        }
        updateHubListener = new HubListenerAdapter() {
            private boolean bSkip;

            public @Override void afterChangeActiveObject(HubEvent e) {
                if (bSkip) {
                    return;
                }
                if (def.tree == null) {
                    return; // error
                }
                if (def.tree.shouldIgnoreChangeAO()) {
                    return;
                }

                if (hub != null && !HubShareDelegate.isUsingSameSharedHub(def.updateHub, hub)) {
                    return;
                }

                Object obj = e.getObject();
                Hub h;
                if (OATreeNode.this.hub != null) {
                    h = OATreeNode.this.hub;
                }
                else {
                    // this will "hit" the same hub that the OATreeNodeData is listening to - since it is listening to the "real" Hub.
                    h = HubShareDelegate.getMainSharedHub(def.updateHub);
                }
                if (obj != h.getAO()) {
                    // 2l0190311 dont set AO, send hub event instead
                    try {
                        bSkip = true;
                        HubEventDelegate.fireAfterChangeActiveObjectEvent(h, obj, h.getPos(obj), false);
                    }
                    finally {
                        bSkip = false;
                    }
                    // was: HubAODelegate.setActiveObject(h, obj);
                }
            }
        };
        def.updateHub.addHubListener(updateHubListener);
    }

    public void setImageProperty(String s) {
        def.imageProperty = s;
        def.methodsToImage = null;
    }

    public String getImageProperty() {
        return def.imageProperty;
    }

    /**
     * Path to find the image. Use "/", and include a leading "/" if it is from the beginning of the classpath, or root directory. Will use the
     * classloader to also search jar files.
     */
    public void setImagePath(String s) {
        if (s != null) {
            s += "/";
            s = OAString.convert(s, "\\", "/");
            s = OAString.convert(s, "//", "/");
        }
        def.imagePath = s;
    }

    public String getImagePath() {
        return def.imagePath;
    }

    public void setBackgroundColorProperty(String s) {
        def.backgroundColorProperty = s;
        def.methodsToBackgroundColor = null;
    }

    public String getBackgroundColorProperty() {
        return def.backgroundColorProperty;
    }

    public void setIconColorProperty(String s) {
        def.iconColorProperty = s;
        def.methodsToIconColor = null;
    }

    public String getIconColorProperty() {
        return def.iconColorProperty;
    }

    public void setForegroundColorProperty(String s) {
        def.foregroundColorProperty = s;
        def.methodsToForegroundColor = null;
    }

    public String getForegroundColorProperty() {
        return def.foregroundColorProperty;
    }

    public void setToolTipTextProperty(String s) {
        def.toolTipTextProperty = s;
        def.methodsToToolTipText = null;
    }

    public String getToolTipTextProperty() {
        return def.toolTipTextProperty;
    }

    public void setToolTipText(String s) {
        def.toolTipText = s;
    }

    public String getToolTipText() {
        return def.toolTipText;
    }

    private int xcnt; // used when "visiting" nodes, so that recursive nodes dont cause a loop

    public void setTree(OATree tree) {
        setTree2(tree);
        // only one top node can have "hub" set
        if (tree != null) {
            tree.tnRoot.setupTopHub(aiXcnt.incrementAndGet(), false);
        }
    }

    private void setTree2(OATree tree) {
        if (this.def.tree == null) {
            this.def.tree = tree;
            for (int i = 0; i < def.treeNodeChildren.length; i++) {
                if (def.treeNodeChildren[i].def.tree == null) {
                    def.treeNodeChildren[i].setTree2(tree);
                }
            }
        }
    }

    private void setupTopHub(int cnt, boolean bHubSet) {
        if (this.xcnt != cnt) {
            this.xcnt = cnt;
            if (!bHubSet) {
                if (hub != null) {
                    bHubSet = true;
                }
            }
            else {
                if (def.updateHub == null) {
                    setUpdateHub(hub);
                }
                hub = null;
            }
            // set tree in children
            for (int i = 0; i < def.treeNodeChildren.length; i++) {
                if (def.treeNodeChildren[i].xcnt != cnt) {
                    def.treeNodeChildren[i].setupTopHub(cnt, bHubSet);
                }
            }
        }
    }

    /**
     * Used to create a recursive node, with a different propertyPath
     * 
     * @param node
     * @param propertyPath new propertyPath to use
     */
    public void add(OATreeNode node, String propertyPath) {
        _add(node, propertyPath);
    }

    public void add(OATreeNode node) {
        _add(node, null);
    }

    private void _add(final OATreeNode originalNode, String propertyPath) {
        OATreeNode node = originalNode;
        if (node == this) {
            if (this.hub != null || !OAString.isEmpty(propertyPath)) {
                // need to create another node, that uses the link property to find hub
                node = new OATreeNode(originalNode);
                node.def = this.def;
                node.bRecursive = true;
                if (OAStr.isNotEmpty(propertyPath)) {
                    node.fullPath = propertyPath;
                }
            }
        }
        else if (!OAString.isEmpty(propertyPath)) {
            OATreeNode origNode = node;
            node = new OATreeNode();
            node.def = origNode.def;
            node.bRecursive = true;
            node.fullPath = propertyPath;
        }
        else {
            if (this.hub != null) {
                if (node.hub != null && node.def.updateHub == null && !(this instanceof OATreeTitleNode)) { 
                    node.setUpdateHub(node.hub);
                }
            }
        }

        int x = def.treeNodeChildren.length;
        OATreeNode[] temp = new OATreeNode[x + 1];
        System.arraycopy(def.treeNodeChildren, 0, temp, 0, x);
        def.treeNodeChildren = temp;
        def.treeNodeChildren[x] = node;
        if (def.tree != null) {
            node.setTree(def.tree);
        }
    }

    public OATreeNode[] getChildrenTreeNodes() {
        return def.treeNodeChildren;
    }

    public boolean getShowAll() {
        return def.showAll;
    }

    public void setShowAll(boolean b) {
        def.showAll = b;
    }

    public Hub getHubForNodeData(OATreeNodeData tnd) {
        if (titleFlag) {
            return null;
        }
        if (hub != null) {
            return hub;
        }
        // 20101208
        if (tnd.parent == null) {
            return null;
        }
        OATreeNode node = tnd.parent.node;
        // was: OATreeNode node = treeNodeParent;
        if (node == null) {
            return null;
        }

        // get Object to get Hub from
        if (tnd.parent == null) {
            return null;
        }
        Object object = tnd.parent.object;

        if (methodsToHub == null && bRecursive) {
            Class clazz = object.getClass();
            OAObjectInfo oi = OAObjectInfoDelegate.getOAObjectInfo(clazz);
            OALinkInfo li = oi.getRecursiveLinkInfo(OALinkInfo.MANY);

            // find method
            Method method = OAReflect.getMethod(clazz, li.getName());
            if (method == null) {
                throw new RuntimeException("OATreeNode.getHubForNodeData() cant find recursive method for " + clazz.getName() + "." + li.getName());
            }

            fullPath = li.getName() + "." + def.propertyPath;
            methodsToHub = new Method[] { method };
        }

        if (methodsToHub == null && def.methodsToProperty == null) {
            findMethods(object.getClass(), true);
        }
        Hub h = null;
        if (methodsToHub != null) {
            h = (Hub) OAReflect.getPropertyValue(object, methodsToHub);
        }
        return h;
    }

    public void findMethods(Class clazz, boolean allowHub) {
        if (titleFlag) {
            return;
        }
        if (def.methodsToPropertyNotUsed) {
            return; // 20150710
        }

        // 20110802 recursive nodes
        if (methodsToHub == null && bRecursive) {
            OAObjectInfo oi = OAObjectInfoDelegate.getOAObjectInfo(clazz);
            OALinkInfo li = oi.getRecursiveLinkInfo(OALinkInfo.MANY);

            // find method
            Method method = OAReflect.getMethod(clazz, "get" + li.getName());
            if (method == null) {
                throw new RuntimeException("OATreeNode.getHubForNodeData() cant find recursive method for " + clazz.getName() + "." + li.getName());
            }

            fullPath = li.getName() + "." + def.propertyPath;
            methodsToHub = new Method[] { method };
        }

        int pos, prev;
        String path = allowHub ? fullPath : def.propertyPath;
        if (path == null) {
            path = "";
        }

        if (path.indexOf("(") >= 0) { // 20160720 need to use propPath
            def.methodsToPropertyNotUsed = true;
            def.propertyPath = path;
            return;
        }

        Vector vec = new Vector();
        for (pos = prev = 0; pos >= 0; prev = pos + 1) {
            String name;

            pos = path.indexOf('.', prev);
            if (pos >= 0) {
                name = "get" + path.substring(prev, pos);
            }
            else {
                name = path.substring(prev);
                if (name.length() == 0) {
                    name = "toString";
                }
                else {
                    name = "get" + name;
                }
            }

            // find method
            Method method = OAReflect.getMethod(clazz, name);
            if (method == null) {
                if (OAObject.class.equals(clazz)) {
                    // 20150612
                    // caused by generics, will need to call getPropety(name)
                    break;
                }
                else {
                    throw new RuntimeException(
                        "OATreeNode.getMethods() cant find method for \"" + name + "\" in PropertyPath \"" + fullPath + "\", from class=" + clazz.getSimpleName());
                }
            }
            vec.addElement(method);
            clazz = method.getReturnType();
            if (Hub.class.isAssignableFrom(clazz)) {
                if (!allowHub) {
                    throw new RuntimeException("OATreeNode.getMethods(): path: " + path + " has more then one hub in it");
                }
                break;
            }
        }
        if (Hub.class.isAssignableFrom(clazz)) {
            def.propertyPath = path.substring(pos + 1);
            methodsToHub = new Method[vec.size()];
            vec.copyInto(methodsToHub);
            def.methodsToProperty = null;
        }
        else if (OAObject.class.isAssignableFrom(clazz)) {
            // 20150612
            def.propertyPath = path;
            def.methodsToPropertyNotUsed = true;
        }
        else {
            def.propertyPath = path;
            if (methodsToHub == null && vec.size() > 1 && hub == null) {
                def.methodToObject = (Method) vec.elementAt(0);
                vec.removeElementAt(0);
            }
            def.methodsToProperty = new Method[vec.size()];
            vec.copyInto(def.methodsToProperty);
        }
    }

    public boolean getTitleFlag() {
        return titleFlag;
    }

    public String toString(OATreeNodeData tnd) {
        if (titleFlag) {
            return fullPath;
        }

        String s;
        if (tnd.object == null) {
            s = "";
        }
        else {
            if (def.methodsToProperty == null) {
                findMethods(tnd.object.getClass(), false);
            }

            Object obj;
            if (def.methodsToProperty == null) {
                obj = ((OAObject) tnd.object).getProperty(def.propertyPath);
            }
            else {
                obj = OAReflect.getPropertyValue(tnd.object, def.methodsToProperty);
            }

            Class c = (obj == null) ? null : obj.getClass();
            s = OAConverter.toString(obj);
            if (s == null) {
                s = "";
            }
        }
        if (def.suffix != null) {
            s += def.suffix;
        }
        if (def.width > 0) {
            int x = s.length();
            if (x < def.width) {
                for (int i = x; i < def.width; i++) {
                    s += " ";
                }
            }
            else if (x > def.width) {
                s = s.substring(0, (def.width > 3) ? def.width - 2 : def.width) + "...";
            }
        }
        int x = s.length();
        if (x < 3) {
            for (int i = x; i < 3; i++) {
                s += " ";
            }
        }
        return s;
    }

    public String getText(Object object, String text) {
        return text;
    }

    protected void close() {
        Set<OATreeNode> hs = new HashSet<>();
        close(hs);
    }

    protected void close(Set<OATreeNode> hs) {
        if (!hs.add(this)) return;
        
        if (hlSelected != null && this.def.hubSelected != null) {
            this.def.hubSelected.removeHubListener(hlSelected);
            hlSelected = null;
        }

        if (updateHubListener != null && this.def.updateHub != null) {
            this.def.updateHub.removeHubListener(updateHubListener);
        }
        
        
        for (OATreeNode tn : def.treeNodeChildren) {
            tn.close(hs);
        }
    }

    // called by client, to make adjustments in UI layout changes
    public void onSelectedEvent() {
        
    }
}


