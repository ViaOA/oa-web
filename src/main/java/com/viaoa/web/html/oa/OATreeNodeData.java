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
package com.viaoa.web.html.oa;


import java.util.*;

import com.viaoa.util.*;
import com.viaoa.hub.*;


/**
 * Used by OATree Model and Hub2TreeNode to maintain the data and listeners for the OATreeNodes.
 */
public class OATreeNodeData {

	public OATreeNode node;
    public Object object;
    public OATreeNodeData parent;
    public volatile List<OATreeNodeData> alTreeNodeData;
    public int checkType; // 0=unchecked, 1=halfchecked, 2=checked
    Hub2TreeNode[] hub2TreeNodes;  // used to listen to all of the Hubs for the children that this is the parent of.
    
    public OATreeNodeData(OATreeNode node, Object obj, OATreeNodeData parent) {
        this.node = node;
        this.object = obj;
        this.parent = parent;
    }
    
    public OATreeNodeData getParent() {
        return parent;
    }

    protected void close(OATreeNode node) {
        
    	if (this.node == node) close();
    	else {
        	if (alTreeNodeData != null) {
        		for (OATreeNodeData tnd : alTreeNodeData) {
    				tnd.close(node);
    			}
        	}
    	}
    }    
    protected void close() {
    	closeChildren();
    }
    public void closeChildren() {
		for (int i=0; hub2TreeNodes != null && i < hub2TreeNodes.length; i++) {
			if (hub2TreeNodes[i] != null) {
				hub2TreeNodes[i].close();
				hub2TreeNodes[i] = null;
			}
		}

    	if (alTreeNodeData != null) {
    	    
    		for (int i=0;i<alTreeNodeData.size(); i++) {
    		    try {
    		        OATreeNodeData tnd = alTreeNodeData.get(i);
    		        tnd.close();
    		    }
    		    catch (Throwable t) {
    		        break;
    		    }
			}
    		alTreeNodeData = null;
    	}
    }

    
    public void loadChildren() {
        if (alTreeNodeData != null) return;
		synchronized (this) {
	        if (alTreeNodeData != null) return;
			alTreeNodeData = new ArrayList<OATreeNodeData>(5);
		}

        Object object = this.object;
        for (int i=0; i < node.getChildrenTreeNodes().length; i++) {
            OATreeNode tn = node.getChildrenTreeNodes()[i];

            if (i == 0) {
            	hub2TreeNodes = new Hub2TreeNode[node.getChildrenTreeNodes().length];
            }
            
            if (tn.getTitleFlag()) {
            	OATreeNodeData tnd = new OATreeNodeData(tn, this.object, this);  // note: make sure that object is "passed down"
        		addChild(tnd, true);
                continue;
            }

            if (tn.getHub() != null) {
                if (!tn.getShowAll()) {
                	Object obj = tn.getHub().getActiveObject();
                    if (obj == null) {
                        hub2TreeNodes[i] = new Hub2TreeNode(tn.getHub(), tn, this, null);
                        continue;
                    }
                    OATreeNodeData tnd = new OATreeNodeData(tn,obj,this);
            		addChild(tnd, true);
            		hub2TreeNodes[i] = new Hub2TreeNode(tn.getHub(), tn, this, null);
                }
                else {
            		Hub h = tn.getHub();
            		HubFilter hf = tn.getHubFilter(tn.getHub());
                    if (hf != null) h = hf.getHub();
            		for (int i2=0; ;i2++) {
            			Object obj = h.elementAt(i2);
            			if (obj == null) break;
            			OATreeNodeData tnd = new OATreeNodeData(tn, obj, this);
                		addChild(tnd, true);
            		}
            		hub2TreeNodes[i] = new Hub2TreeNode(h, tn, this, hf);
                }
                continue;
            }

            if (object == null) continue;
            
            if (tn.getMethodsToHub() == null && (tn.bRecursive || tn.getMethodsToProperty() == null)) {
                tn.findMethods(object.getClass(), true);
            }
            
            if (tn.getMethodsToHub() != null) {
                Hub h = getHubToChild(tn);
                
                HubFilter hf = tn.getHubFilter(this, h);
                if (hf != null) h = hf.getHub();
                if (h == null) {
                    continue;
                }
        		for (int i2=0; ;i2++) {
        			Object obj = h.elementAt(i2);
        			if (obj == null) break;
        			OATreeNodeData tnd = new OATreeNodeData(tn, obj, this);
            		addChild(tnd, true);
        		}
        		hub2TreeNodes[i] = new Hub2TreeNode(h, tn, this, hf);
            }
            else {
                if (tn.getMethodsToProperty() == null) tn.findMethods(object.getClass(), false);
                if (tn.getMethodToObject() != null) {
                    Object obj = OAReflect.getPropertyValue(object, tn.getMethodToObject());
                    OATreeNodeData tnd = new OATreeNodeData(tn, obj, this);
                	addChild(tnd, true);
                }
                else {
                	OATreeNodeData tnd = new OATreeNodeData(tn,object,this);
            		addChild(tnd, true);
                }
            }
        }
    }

    protected Hub getHubToChild(final OATreeNode tnChild) {
        Hub h = null;
        if (tnChild.getMethodsToHub() != null) {
            h = (Hub) OAReflect.getPropertyValue(object, tnChild.getMethodsToHub());
        }
        return h;
    }
    
    
    public OATreeNodeData getChild(OATreeNode node, Object obj) {
    	if (alTreeNodeData == null) return null;
    	for (int i=0; i < alTreeNodeData.size(); i++) {
    		OATreeNodeData data = (OATreeNodeData) alTreeNodeData.get(i);
    		if (data.node == node && data.object == obj) return data;
    	}
    	return null;
    }
    
    public OATree getTree() {
        return node.def.tree;
    }
    
    public void insertChild(int pos, OATreeNodeData tnd) {
		alTreeNodeData.add(pos, tnd);
		getTree().addChange(OATree.TreeNodeDataChange.createInsert(tnd)); // note: after add is done
    }

    public void addChild(OATreeNodeData tnd, boolean bIsLoadingChildren) {
		alTreeNodeData.add(tnd);
        if (!bIsLoadingChildren) getTree().addChange(OATree.TreeNodeDataChange.createAdd(tnd)); // note: after add is done
    }
    
    public void removeChild(OATreeNodeData tnd) {
        getTree().addChange(OATree.TreeNodeDataChange.createRemove(tnd)); // note: do this before remove is called (next line)
        alTreeNodeData.remove(tnd);
        tnd.close();
    }
    public void changeChild(OATreeNodeData tnd) {
        getTree().addChange(OATree.TreeNodeDataChange.createChange(tnd));
    }

    public void moveChild(int pos1, int pos2) {
        OATreeNodeData tnd = alTreeNodeData.get(pos1);
        getTree().addChange(OATree.TreeNodeDataChange.createMove(tnd, pos2)); // note: do this before move is called (next line)
        
        alTreeNodeData.remove(pos1);
        alTreeNodeData.add(pos2, tnd);
    }
    
    public void expand() {
        getTree().addChange(OATree.TreeNodeDataChange.createExpand(this)); 
    }
    
    public void select() {
        getTree().addChange(OATree.TreeNodeDataChange.createSelect(this)); 
    }

    public void newList() {
        getTree().addChange(OATree.TreeNodeDataChange.createNewList(this)); 
    }
    
    
    public int getChildIndex(OATreeNodeData tnd) {
        if (alTreeNodeData == null) return 0;
        return alTreeNodeData.indexOf(tnd);
    }
    
    public int getChildIndex(Object obj, OATreeNode node) {
        if (alTreeNodeData == null) loadChildren();
        for (int i=0;i<alTreeNodeData.size(); i++) {
            try {
                OATreeNodeData tnd = alTreeNodeData.get(i);
                if (tnd.node == node && tnd.object == obj) return i;
            }
            catch (Throwable t) {
                break;
            }
        }
        return -1;
    }
    
    public boolean areChildrenLoaded() {
        return alTreeNodeData != null;
    }
    
    // returns the Hub that this tnd.object belongs to
    public Hub getHub() {
        return node.getHubForNodeData(this);
    }
    public Object getObject() {
        return object;
    }
    public OATreeNode getNode() {
        return node;
    }
    public boolean getAreChildrenLoaded() {
        return isLeaf() || alTreeNodeData != null;
    }
    public int getChildCount() {
        if (alTreeNodeData == null) loadChildren();
        return alTreeNodeData.size();
    }

    public OATreeNodeData getChild(int index) {
        if (alTreeNodeData == null) loadChildren();
    	if (index < 0 || index >= alTreeNodeData.size()) return null;
    	return alTreeNodeData.get(index);
    }
            
    public List<OATreeNodeData> getChildren() {
        return alTreeNodeData;
    }
    
    public boolean isLeaf() {
        return (node.getChildrenTreeNodes().length == 0);
    }

    public String toString() {
        return (node.toString(this));
    }
}
