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

import java.util.concurrent.atomic.AtomicInteger;

import com.viaoa.object.*;
import com.viaoa.util.OAArray;
import com.viaoa.hub.*;

/** 
    Used by OATreeNodeData for each node in the tree that uses a Hub.
    @see OATreeNode
*/
public class Hub2TreeNode extends HubListenerAdapter {
    OATreeNode node;
    OATreeNodeData parent;
    Hub hub;
    HubFilter hubFilter;  // this is so that HubFilter.close() will be called when this.close() is called.  
    private static AtomicInteger aiCnt = new AtomicInteger();
    
    public Hub2TreeNode(Hub hub, OATreeNode node, OATreeNodeData parent, HubFilter hf) {
        String pp = node.getPropertyPath(); 
    	if (pp != null) {
    	    if (pp.indexOf(".") < 0) {
                hub.addHubListener(this, pp);
    	    }
    	    else {
                hub.addHubListener(this, "hub2TreeNode"+aiCnt.getAndIncrement(), new String[] {pp});
    	    }
    	}
    	else hub.addHubListener(this);
        
        this.hub = hub;
        this.node = node;
        this.parent = parent;
        this.hubFilter = hf;
    }

    public void close() {
    	if (hub != null) {
        	if (node.getPropertyPath() != null && node.getPropertyPath().indexOf(".") < 0) {
                hub.removeHubListener(this);
        	}
        	else hub.removeHubListener(this);
    	}
    	if (hubFilter != null) hubFilter.close();
    }
    
    
    public @Override void afterPropertyChange(HubEvent e) {
        if (e.getObject() instanceof Hub) return;
        String s = e.getPropertyName();
        if (s == null) return;
        if (s.equalsIgnoreCase("changed") || s.equalsIgnoreCase("new")) return;

        if (node == null) {
            return;
        }
        
        String pp = node.getPropertyPath();
        if (pp == null) return;
        
        boolean b;
        if (pp.indexOf('.') >= 0) b = pp.toUpperCase().endsWith("." + s.toUpperCase());
        else b = pp.equalsIgnoreCase(s);
            
        if (!b) {
        	if (node.getIconColorProperty() == null || node.getIconColorProperty().toUpperCase().indexOf(s.toUpperCase()) < 0) {
            	if (node.getImageProperty() == null || node.getImageProperty().toUpperCase().indexOf(s.toUpperCase()) < 0) {
            	    if (node.getDependentProperties() == null) return;
            	    for (String sx : node.getDependentProperties()) {
            	        if (sx != null && sx.toUpperCase().indexOf(s.toUpperCase()) >= 0) {
            	            b = true;
            	            break;
            	        }
            	    }
            		if (!b) return;
            	}
        	}
        }
        
        int pos = parent.getChildIndex(e.getObject(), node);
        if (pos >= 0) {
            OATreeNodeData tnd = parent.getChild(node, e.getObject());
            parent.changeChild(tnd);
        }            
    }

    public @Override void afterAdd(final HubEvent e) {
        if (node != null && node.getShowAll()) {
    		if (parent.getChild(node, e.getObject()) == null) {
    			OATreeNodeData tnd = new OATreeNodeData(node, e.getObject(),parent);
    			int pos = getStartPos()+e.getPos();
    			parent.addChild(tnd, false);
    		}
        }
    }

    
    public @Override void afterInsert(HubEvent e) {
        if (node != null && node.getShowAll()) {
            if (parent.getChild(node, e.getObject()) == null) {
                OATreeNodeData tnd = new OATreeNodeData(node, e.getObject(), parent);
                int pos = getStartPos()+e.getPos();
                parent.insertChild(pos, tnd);
            }
        }
    }

    public @Override void beforeRemove(HubEvent e) {
		if (node.getShowAll()) {
			OATreeNodeData tnd = parent.getChild(node, e.getObject());
			if (tnd != null) {
        		int pos = parent.getChildIndex(tnd);
        		parent.removeChild(tnd);
    		}
        }
    }

    // from start of parent, not top of tree
    protected int getStartPos() {
    	int nodePos = 0;
    	for (int i=0; i < parent.node.getChildrenTreeNodes().length; i++) {
            OATreeNode tn = parent.node.getChildrenTreeNodes()[i];
            if (tn == node) {
            	nodePos = i;
            	break;
            }
        }
    	nodePos++;
    	
    	int i = 0;
    	parent.loadChildren();
    	for (; i < parent.alTreeNodeData.size(); i++) {
    		OATreeNodeData dx = (OATreeNodeData) parent.alTreeNodeData.get(i);
    		if (dx.node == node) return i;
        	for (int i2=nodePos; i2 < parent.node.getChildrenTreeNodes().length; i2++) {
                if (parent.node.getChildrenTreeNodes()[i2] == dx.node) return i; 
            }
    	}
    	return i;
    }
    
    
    public @Override void afterMove(HubEvent e) {
    	if (node.getShowAll()) {
            int pos1 = e.getFromPos();
            int pos2 = e.getToPos();
            
            int startPos = getStartPos();
			parent.moveChild(startPos+pos1, startPos+pos2);
        }
    }

    private Object[] getObjectPath(int amt, Object object) {
        // amt = number of object from the top of the tree to get. -1 to get all
        int i,x;
        OATreeNodeData tnd = parent;

        for (x=0 ;tnd != null; x++ ) tnd = tnd.parent;
        
        if (amt < 0 || amt > x) amt = x;

        if (object != null) amt++;
        
        Object[] objs = new Object[amt];
        tnd = parent;
        for (i=0; i<x; i++ ) {
            if (x-i <= amt) objs[x-i-1] = tnd;
            tnd = tnd.parent;
        }

        if (object != null) {
            x = parent.getChildCount();
            for (i=0; i<x; i++) {
                tnd = parent.getChild(i);
                if (tnd.object == object) {
                    objs[objs.length-1] = tnd;
                }
            }
        }        
        return objs;
    }
    

    /** if the "node" has an updateHub, then OATreeNode will keep the activeObject in sync
        and this will update the tree.
    */
    public @Override void afterChangeActiveObject(HubEvent e) {
        if (node.getTree().shouldIgnoreChangeAO()) return;
        final int pos = e.getPos();
        if (pos < 0) return;
        HubEvent hcae = e;
        if (!node.getShowAll()) {
            return;
        }

        // root node has updateHub set to null, so we need to use source hub if rootNode.
        Hub hubUpdate = node.getUpdateHub();
        if (hubUpdate == null) {
            OATreeNode root = node.getTree().getRootTreeNode();
            OATreeNode[] tns = root.getChildrenTreeNodes();
            if (OAArray.contains(tns, node)) hubUpdate = hub;
        }
        
        if (hubUpdate != null && (HubShareDelegate.isUsingSameSharedHub(hubUpdate, hub)) ) {
            OATreeNodeData tnd = parent.getChild(pos);
            tnd.select();
        }
    }

    public @Override void afterSort(HubEvent e) {
        onNewList(e);
    }
    public @Override void onNewList(HubEvent e) {
        if (parent == null) return;
        parent.closeChildren();
        parent.newList();
    }

}

