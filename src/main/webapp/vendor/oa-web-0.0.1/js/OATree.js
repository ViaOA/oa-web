
import { OAClient } from '@oa-web/index.js';
import OATreeController from './OATreeController.js';


/* 
Html structure:

ul.list-group[data-oa-web="tree"]
li 
  div 
    a 
      i expandIcon 
    i check
    span $label
  div
    ul
      .. li ..
      
--------- leaf
li
  div
    (no <a>) 
    i check
    span $label
*/

/**
 * @class
 */
export default class OATree {
    static cntTreeNode = 0;

    /**
     * @param {HTMLElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;

        if (element.tagName !== 'UL') {
            throw new Exception("OATree must use a UL html element");
        }
        
        /** @type {OATreeNodeData} */
        this.treeNodeDataRoot = new OATreeNodeData();
        this.treeNodeDataRoot.treeNode = new OATreeNode(); // root
        
        /** @type {OATreeController} */
        this.control;
    }

    initialize() {
        if (this.control) return;
        // called after creating treeNodes
        this.control = new OATreeController(this);
        
        for (let tn of this.treeNodeDataRoot.treeNode.treeNodes) {
            this._updateCheckable(tn);
        }
        
        this.control.initialize();
    }

    // makes sure that parent tn are checkable.
    _updateCheckable(tn) {
        let b = tn.checkable;
        if (tn.treeNodes) {
            for (let tnx of tn.treeNodes) {
                if (this._updateCheckable(tnx)) b = true;
            }
        }
        tn.checkable = b;
        return b;
    }
    
    /** @param {OATreeNodeData} tnd */
    setSelectedFromServer(tnd) {
        this.expand(tnd);
        const ele = this.getSpanElement(tnd);
        this.control.setActiveSpanElement(ele, true);            
    }

    /** @param {OATreeNodeData} tnd */
    expand(tnd) {
        for (let tndx=tnd; tnd.parent ; tndx = tndx.tndParent) {
            this.expand(tndx)
        }
        const ele = this.getSpanElement(tnd);
        if (!ele) return;
        let collapseInstance = bootstrap.Collapse.getInstance(ele);
        if (collapseInstance) collapseInstance.show();
    }
    

    /**
     * @returns {OATreeNode}
     */
    getRootTreeNode() {
        return this.treeNodeDataRoot.treeNode;
    }

    /**
     * @param {OATreeNode} tn
     */
    addTreeNode(tn) {
        this.getRootTreeNode().addTreeNode(tn);
    }
    /**
     * @param {OATreeNode} tn
     */
    removeTreeNode(tn) {
        this.getRootTreeNode().removeTreeNode(tn);
    }

    
    
    
    /**
     * @param {OATreeNodeData[]} tnd
     */
    getRootTreeNodeData() {
        return this.treeNodeDataRoot;
    }

    /** @returns {OATreeNodeData} */
    getSelectedTreeNodeData() {
        /** @type {OATreeNodeData} */
        let tnd = this.getTreeNodeData(this.control.spanActive);
        return tnd;
    }

    /**
     * @param {OATreeNodeData} tnd
     * @returns {HTMLElement | undefined} ele span or li
     */
    getSpanElement(tnd) {
        if (!tnd) return;

        let path = [];
        while (tnd.tndParent) {
            let pos = tnd.tndParent.treeNodeDatas.indexOf(tnd);
            if (pos < 0) return;
            path.unshift(pos);
            tnd = tnd.tndParent;
        }

        /** @type {HTMLElement} */
        let ele;

        for (const pos of path) {
            if (!ele) ele = this.element;
            else ele = ele.querySelector(':scope > div:nth-of-type(2) > ul');
            if (!ele) break;
            ele = ele.querySelector(`:scope > li:nth-of-type(${pos + 1})`);
            if (!ele) break;
        }

        if (ele) {
            ele = ele.querySelector('div > span');
        }

        return ele;
    }
    
    
    
    
    /**
     * Converts Java OATree generated objects.  This does not update DOM.
     * @see addTreNodeDatas to add to tree and dom.
     * @param {Object[]} objs from server JSON
     * @param {OATreeNodeData} tndParent
     * @returns {OATreeNodeData[]} that can be added to an existing tnd
     */
    convertServerResponseToTreeNodeDatas(objs, tndParent) {
        if (!tndParent) {
            tndParent = this.treeNodeDataRoot;
        }
        let tndsNew = [];
        for (let obj of objs) {
            const tndNew = new OATreeNodeData();
            tndNew.label = obj.label;
            tndNew.checkedType = obj.checkedType;

            let pos = 0;
            if (obj.tn !== undefined) pos = obj.tn;

            tndNew.treeNode = tndParent.treeNode.treeNodes[pos];
            
            tndsNew.push(tndNew);
            tndNew.tndParent = tndParent;
            tndParent._addTreeNodeData(tndNew);
            
            if (obj.tnds) {
                 tndNew.treeNodeDatas = this.convertServerResponseToTreeNodeDatas(obj.tnds, tndNew);
                 tndNew.childrenLoaded = true; 
            }
        }
        return tndsNew;
    }
    

    /** 
     * @param {OATreeNodeData[]} tnds 
     * @param {OATreeNodeData} tndParent 
     */
    addTreeNodeDatas(tnds, tndParent) {
        if (!tndParent) {
            tndParent = this.treeNodeDataRoot;
        }
        for (let tnd of tnds) {
            this.addTreeNodeData(tnd, tndParent);
        }
    }    
        
    /** 
     * @param {OATreeNodeData} tnd 
     * @param {OATreeNodeData} tndParent 
     */
    addTreeNodeData(tnd, tndParent) {
        /** @type {HTMLElement} */
        let ele;

        if (!tndParent) tndParent = this.treeNodeDataRoot;
        if (!tndParent.tndParent) {
            ele = this.element;  // tree = 'UL'
        }
        else {
            ele = this.getSpanElement(tndParent);
            while (ele?.tagName !== 'DIV') {
                ele = ele.parentElement;
            }
            ele = ele.nextElementSibling;  // div
            ele = ele.firstElementChild;  // UL
        }

        tnd.tndParent = tndParent;
        tndParent._addTreeNodeData(tnd);
        
        if (tnd.treeNode.treeNodes?.length) {
            let cntId = OATree.cntTreeNode++;
            ele.insertAdjacentHTML('beforeend', this.getHtml1(tnd, cntId));
        }
        else { // leaf 
            ele.insertAdjacentHTML('beforeend', this.getHtml2(tnd));
        }

        if (tnd.treeNode.treeNodes?.length) {
            ele = this.getSpanElement(tnd);
            ele = ele.previousElementSibling;
            const newCollapse = new bootstrap.Collapse(ele, {
                toggle: false // Prevent it from auto-expanding on initialization
            });
        }

        if (tnd.treeNodeDatas) {
            tndParent.childrenLoaded = true;
            for (let tndx of tnd.treeNodeDatas) {
                this.addTreeNodeData(tndx, tnd);
            }
        }        
        else if (tnd.treeNode.treeNodes?.length) { // auto create any title nodes
            for (let tn of tnd.treeNode.treeNodes) {
                if (tn.isTitle) {
                    if (tnd.treeNodeDatas.length === 0) {
                        let tndx = new OATreeNodeData(tn.label, tn);
                        this.addTreeNodeData(tndx, tnd);
                    }
                }
            }
        }
        
        // add tooltips
        ele = this.getSpanElement(tnd)
        let options = {
            title: tnd.label,
            placement: 'right',
            delay: { "show": 500, "hide": 50 }
        };  
        const tooltip = new bootstrap.Tooltip(ele, options)
    }




    /** 
     * @param {OATreeNodeData} tnd 
     * @param {OATreeNodeData} tndBefore 
     */
    insertTreeNodeData(tnd, tndBefore) {
        if (!tndBefore.tndParent) return;
        if (tndBefore.treeNode.isTitle) return;

        tnd.tndParent = tndBefore.tndParent;
        tnd.treeNode = tndBefore.treeNode;


        /** @type {HTMLElement} */
        let ele = this.getSpanElement(tndBefore);
        while (ele?.tagName !== 'LI') {
            ele = ele.parentElement;
        }

        if (tnd.treeNode.treeNodes?.length) {
            let cntId = OATree.cntTreeNode++;
            ele.insertAdjacentHTML('beforebegin', this.getHtml1(tnd, cntId));
        }
        else { // leaf 
            ele.insertAdjacentHTML('beforebegin', this.getHtml2(tnd));
        }
       
        /** @type {number} */
        let pos = tndBefore.tndParent.treeNodeDatas.indexOf(tndBefore);
        tndBefore.tndParent._insertTreeNodeData(tnd, pos);

        if (tnd.treeNode.treeNodes?.length) {
            ele = this.getSpanElement(tnd);
            ele = ele.previousElementSibling;
            const newCollapse = new bootstrap.Collapse(ele, {
                toggle: false
            });
        }

        if (tnd.treeNodeDatas) {
            tnd.tndParent.childrenLoaded = true;
            for (let tndx of tnd.treeNodeDatas) {
                this.addTreeNodeData(tndx, tnd);
            }
        }        
        else if (tnd.treeNode.treeNodes?.length) { // auto create any title nodes
            for (let tn of tnd.treeNode.treeNodes) {
                if (tn.isTitle) {
                    if (tnd.treeNodeDatas?.length) {
                        let tndx = new OATreeNodeData(tn.label, tn);
                        this.addTreeNodeData(tndx, tnd);
                    }
                }
            }
        }
    }

    /** @param {OATreeNodeData} tnd */
    removeTreeNodeData(tnd) {
        if (!tnd) return;

        if (tnd.treeNode.isTitle) return;

        /** @type {HTMLElement} */
        let ele = this.getSpanElement(tnd);
        tnd.tndParent._removeTreeNodeData(tnd);

        while (ele?.tagName !== 'LI') {
            ele = ele.parentElement;
        }
        if (ele) ele.remove();
    }

    /** @param {OATreeNodeData} tnd to update 
     *  @param {String} label treenode label
    */
    updateTreeNodeDataLabel(tnd, label) {
        /** @type {HTMLElement} */
        let ele = this.getSpanElement(tnd);

        if (label !== undefined) tnd.label = label;
        ele.innerText = tnd.label;
        
        const tooltip = bootstrap.Tooltip.getInstance(ele);
        tooltip.setContent({ '.tooltip-inner': ele.innerText })        
        
    }

    /** @param {OATreeNodeData} tnd to update 
     *  @param {number} checkedType 0=unchecked, 1=halfchecked, 2=checked 
    */
    updateTreeNodeDataCheck(tnd, checkedType) {
        
        if (!tnd.treeNode.checkable) return;
        tnd.checkedType = checkedType;
        
        /** @type {HTMLElement} */
        let ele = this.getSpanElement(tnd);
        ele = ele.previousElementSibling;  // i
        ele.classList.remove('bi-check-square');
        ele.classList.remove('bi-dash-square');
        ele.classList.remove('bi-square');
     
       let bi;
       if (tnd.checkedType == 2) bi = 'bi-check-square';    
       else if (tnd.checkedType == 1) bi = 'bi-dash-square';
       else bi = 'bi-square';    
       ele.classList.add(bi);
 

       // recurse children
        if (checkedType == 0 || checkedType == 2) {
            for (let tndx of tnd.treeNodeDatas) {
                this.updateTreeNodeDataCheck(tndx, checkedType);
            }
        } 
    }
    
    
    
    
    /**
     * @param {OATreeNodeData} tnd
     * @param {number} pos after move is done
     */
    moveTreeNodeData(tnd, pos) {
        /** @type {HTMLSpanElement} */
        let eleSpan = this.getSpanElement(tnd);
        let eleLI = eleSpan.parentElement.parentElement;
        let eleUL = eleLI.parentElement;
        eleUL.removeChild(eleLI)
        
        if (pos >= eleUL.children.length) {
            eleUL.appendChild(eleLI);
        }
        else {
            eleUL.insertBefore(eleLI, eleUL.children[pos]);            
        }    
        
        let tndParent = tnd.tndParent;
        let currentPos = tndParent.treeNodeDatas.indexOf(tnd);
        tndParent.treeNodeDatas.splice(currentPos, 1);
        if (pos >= tndParent.treeNodeDatas.length) {
            tndParent.push(tnd);
        }
        else tndParent.treeNodeDatas.splice(pos, 0, tnd);
    }

    
    /**
     * Inserted HTML for tree nodes with children.
     * @param {OATreeNodeData} tnd
     * @param {number} cntId seq Id
     */
    getHtml1(tnd, cntId) { // parent node
        let html =
        `<li class="list-group-item">
          <div class="d-flex align-items-center">
            <a data-bs-toggle="collapse" href="#oatnd${cntId}" role="button" tabindex="-1"> <i class="bi bi-chevron-right me-2"></i></a>`; 
    
           if (tnd.treeNode.checkable) {
              let bi;
              if (tnd.checkedType == 2) bi = 'bi-check-square';    
              else if (tnd.checkedType == 1) bi = 'bi-dash-square';
              else bi = 'bi-square';    
            
              html += `<i class="oa-tree-node-check bi ${bi} me-2" tabindex="0"></i>`;
           }
            
           html += ` 
            <span class="oa-tree-node">${tnd.label}</span>
          </div>
          <div class="collapse" id="oatnd${cntId}">
            <ul class="list-group ms-3">
            </ul>
          </div>
        </li>`;
        return html;
    }

    /**
     * Inserted HTML for leaf tree nodes
     * @param {OATreeNodeData} tnd
     */
    getHtml2(tnd) { // leaf node
        let html = `<li class="list-group-item ms-3">
            <div class="d-flex align-items-center">`;
        
        if (tnd.treeNode.checkable) {
            let bi;
            if (tnd.checkedType == 2) bi = 'bi-check-square';    
            else if (tnd.checkedType == 1) bi = 'bi-dash-square';
            else bi = 'bi-square';    
            html += `<i class="oa-tree-node-check bi ${bi} me-2" tabindex="0"></i>`;
        }
        html += `<span class="oa-tree-node">${tnd.label}</span></div></li>`;
        return html;
    }

    
    
    /**
     * @param {...(HTMLElement|number|number[])} args
     * @returns {OATreeNodeData}
     */
    getTreeNodeData(...args) {
        let integers;
        // Check if the first argument is an array
        if (args.length === 1 && Array.isArray(args[0])) {
            integers = args[0]; // Use the array directly
        }
        else if (args.length === 1 && args[0] instanceof HTMLElement) {
            return this._getTreeNodeDataHTMLElement(args[0]);
        } else {
            integers = args;
        }

        /** @type {OATreeNodeData} */
        let tnd = this.treeNodeDataRoot;
        for (const pos of integers) {
            if (pos >= tnd.treeNodeDatas.length) break;
            tnd = tnd.treeNodeDatas[pos];
        }
        return tnd;
    }

    /**
     * @private
     * @param {HTMLElement} ele span or li
     * @returns {OATreeNodeData} tnd
     */
    _getTreeNodeDataHTMLElement(ele) {
        if (!ele) return;

        /** @type {HTMLElement} */
        let eleLi;

        if (ele.tagName === 'I') {
            ele = ele.parentElement;
            if (!ele) return;
        }
        if (ele.tagName === 'SPAN' || ele.tagName === 'A') {
            eleLi = ele.parentElement;
        }
        else eleLi = ele;

        if (eleLi?.tagName === 'DIV') {
            eleLi = eleLi.parentElement;
        }
        
        if (!eleLi || eleLi.tagName !== 'LI') return;


        const eleTree = this.element;
        if (eleTree.tagName !== 'UL') return;


        let path = [];

        for (; ;) {
            /** @type {HTMLElement} */
            let eleUl = eleLi.parentElement;

            if (!eleUl || eleUl.tagName !== 'UL') return;

            let index = Array.from(eleUl.children).findIndex(el => el === eleLi);
            path.push(index);

            if (eleUl === eleTree) break;

            let eleDiv = eleUl.parentElement;
            if (!eleDiv || eleDiv.tagName !== 'DIV') return;
            eleLi = eleDiv.parentElement;
        }

        path = path.reverse();
        let tnd = this.treeNodeDataRoot;
        path.forEach((pos) => {
            tnd = tnd.treeNodeDatas[pos];
        });

        return tnd;
    }

    /**
     * Clear treeNodeData
     */
    clearTreeNodeData() {
        this.element.innerText = '';
        this.treeNodeDataRoot.treeNodeDatas.length = 0;
        this.treeNodeDataRoot.childrenLoaded = false;
    }

    /** load tnd from dom, ** needs testing to make sure html matches current html1, html2 */
    loadTreeNodeDataFromDOM() {
        this.control.loadTreeNodeDataFromDOM();
    }
}


export class OATreeNode {
    constructor() {
        /** @type {string} */
        this.label;
        
        /** @type {boolean} */
        this.isTitle = false;
        
        /** @type {boolean} */
        this.lazyLoad = false;
        
        /** @type {OATreeNode[]} */
        this.treeNodes = [];
        
        /** @type {boolean} */
        this.checkable;
    }
    
    addTreeNode(tn) {
        this.treeNodes.push(tn);
    }
    removeTreeNode(tn) {
        if (!tn) return;
        let pos = this.treeNodes.indexOf(tn);
        if (pos >= 0) this.treeNodes.splice(pos, 1);
    }
    getTreeNodes() {
        return this.treeNodes;
    }
}




export class OATreeNodeData {
    constructor(label, tn) {
        /** @type {OATreeNode} */
        this.treeNode = tn;
        
        /** @type {string} */
        this.label = label;
        
        /** @type {OATreeNodeData} */
        this.tndParent;

        /** @type {boolean} */
        this.isExpanded = false;
        
        /** @type {OATreeNodeData[]} */
        this.treeNodeDatas = [];
        
        /** @type {boolean} */
        this.childrenLoaded = false;
        
        /** @type {number} */
        this.checkedType = 0;  // 0=unchecked, 1=halfchecked, 2=checked   
    }
    
    // bi-square,  bi-dash-square, bi-check-square, bi-plus-square
    // bi-chevron-right, bi-chevron-down
    // checkedType  0=unchecked, 1=halfchecked, 2=checked
    
    
    /** @returns {OATreeNodeData[]} */
    getTreeNodeDatas() {
        return this.treeNodeDatas;
    }
            
    /** @private 
     * @param {OATreeNodeData} tnd
    */
    _addTreeNodeData(tnd) {
        let pos = this.treeNodeDatas.indexOf(tnd);
        if (pos < 0) pos = this.treeNodeDatas.push(tnd);
        return pos;
    }
    /** @private 
     * @param {OATreeNodeData} tnd
     * @param {number} pos position to insert into
    */
    _insertTreeNodeData(tnd, pos) {
        this.treeNodeDatas.splice(pos, 0, tnd);
    }
    /** @private 
     * @param {OATreeNodeData} tnd to remove
    */
    _removeTreeNodeData(tnd) {
        if (!tnd) return;
        let pos = this.treeNodeDatas.indexOf(tnd);
        if (pos >= 0) this.treeNodeDatas.splice(pos, 1);
    }

    /** Returns array of numbers, for each treeNode position to follow from root to get to tnd.
     * @param {OATreeNodeData} tnd
     * @returns {number[]} */
    getIndexPathFromRoot() {
        let tnd = this;

        /** @type {number[]} */
        let nums = [];

        for (; tnd.tndParent;) {
            let pos = tnd.tndParent.treeNodeDatas.indexOf(tnd);
            nums.splice(0, 0, pos);
            tnd = tnd.tndParent;
        }
        return nums;
    }
}

