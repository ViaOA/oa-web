
import { OAClient } from '@oa-web/index.js';
import OATree, { OATreeNode, OATreeNodeData } from './OATree.js';


export default class OATreeController {
    
    constructor(tree) {
        /** @type {OATree} */
        this.tree = tree;
        /** @type {HTMLElement} */
        this.spanActive;
        /** @type {boolean} */
        this.isThrottled;
    }

    close() {
        //qqqqqqqqqqqqqqqqq
    }
    
    initialize() {
        /** @type {HTMLElement} */
        const eleTree = this.tree.element;
        
        eleTree.addEventListener('shown.bs.collapse', async (/** @type {Event} */ event) => {
            let /** @type {HTMLElement} */ele = event.target;
            if (ele.tagName === 'DIV') {
                ele = ele.previousElementSibling; // div
                ele = ele.querySelector('span');

            }
            /** @type {OATreeNodeData} */
            let tnd = this.tree.getTreeNodeData(ele);
            tnd.isExpanded = true;
            event.stopPropagation();

            let icon = ele.previousElementSibling;
            if (icon.tagName === 'I') icon = icon.previousElementSibling; // <a>
            icon = icon.firstElementChild; // <i>

            let lazyLoad = false;
            if (!tnd.childrenLoaded) {
                if (!tnd.treeNodeDatas?.length) {
                    if (tnd.treeNode.lazyLoad) {
                        lazyLoad = true;
                    }
                }
            }
                                    
            icon.classList.remove('bi-chevron-right');
            
            if (lazyLoad) {
                icon.classList.add('bi-arrow-clockwise', 'oa-spinner');
            }
            else icon.classList.add('bi-chevron-down');
            
            if (!tnd.childrenLoaded) {
                tnd.childrenLoaded = true;
                if (lazyLoad) {
                    await OAClient.sendEventToServer(
                        {
                            id: this.tree.id,
                            type: 'loadchildren',
                            treePath: !tnd ? '' : '' + tnd.getIndexPathFromRoot()
                        }
                    );
                    
                    setTimeout(() => 
                        {               
                            icon.classList.remove('bi-arrow-clockwise', 'oa-spinner');
                            icon.classList.add('bi-chevron-down');
                        },
                        300
                    );
                }
            }
        });
        eleTree.addEventListener('hidden.bs.collapse', (event) => {
            let /** @type {HTMLElement} */ele = event.target;
            if (ele.tagName === 'DIV') {
                ele = ele.previousElementSibling; // div
                ele = ele.querySelector('span');
               
            }
            let tnd = this.tree.getTreeNodeData(ele);
            tnd.isExpanded = false;
            event.stopPropagation();

            // was: const icon = ele.previousElementSibling.firstElementChild; // <a><i> </a>
            let icon = ele.previousElementSibling;
            if (icon.tagName === 'I') icon = icon.previousElementSibling; // <a>
            icon = icon.firstElementChild; // <i>
            
            icon.classList.remove('bi-chevron-down');
            icon.classList.add('bi-chevron-right'); 
        });


        // get clicks on span or checkbox.  Note: click on <a> is handled by bootstrap
        eleTree.addEventListener('click', (event) => {
            if (event.target.tagName === 'SPAN') {
                eleTree.focus();
                this.setActiveSpanElement(event.target);
    
                const liFound = event.target.closest('li');
                if (!liFound) return;
    
                const ulFound = liFound.closest('ul');
                if (!ulFound) return;
    
                event.preventDefault();
                event.stopPropagation();
                return false;
            }
            if (event.target.classList.contains('oa-tree-node-check')) {
                let tnd = this.tree.getTreeNodeData(event.target);
                
                OAClient.sendEventToServer(
                    {
                        id: this.tree.id,
                        type: 'checkboxclicked',
                        treePath: !tnd ? '' : '' + tnd.getIndexPathFromRoot()
                    }
                );
                
                event.preventDefault();
                event.stopPropagation();
                return false;
            }
        });

        eleTree.addEventListener('dblclick', (event) => {
            if (event.target.tagName !== 'SPAN') {
                return;
            }
            event.stopPropagation();
            event.preventDefault();

            let eleA = event.target.previousElementSibling;
            if (eleA.tagName !== 'A') eleA = eleA.previousElementSibling;
            if (eleA) eleA.click();
        });


        // Add a keyboard listener to the span element
        eleTree.addEventListener('keydown', (/** @type {KeyboardEvent} */event) => {
            this._onKeydown(event);
        });
        
        
//qqqqqqqqqqqqqqqqqqq NEXT: context menu qqqqqqqqqqqqqqq   testing qqqqqqqqqqqqqqqqqq     
        eleTree.addEventListener('contextmenu', (event) => {
            event.preventDefault(); // Prevent the default right-click menu

            const contextMenu = document.querySelector('[data-oa-name="menuTree"]');
            const { clientX: mouseX, clientY: mouseY } = event;

            // Position the menu
            contextMenu.style.top = `${mouseY}px`;
            contextMenu.style.left = `${mouseX}px`;
            contextMenu.style.display = 'block';

            // Hide the menu when clicking elsewhere
            document.addEventListener('click', () => {
                contextMenu.style.display = 'none';
            }, { once: true });
        });        
        
        
    }

    
    /** @type {HTMLSpanElement} */
    setActiveSpanElement(ele, bIsFromServer) {
        if (this.spanActive) this.spanActive.classList.remove('oa-selected');
        this.spanActive = ele;
        this.spanActive.classList.add('oa-selected');

        this._scrollIntoView();
        const tnd = this.tree.getTreeNodeData(ele);
        if (!bIsFromServer) {
            OAClient.sendEventToServer(
                {
                    id: this.tree.id,
                    type: 'selected',
                    treePath: '' + tnd.getIndexPathFromRoot()
                }
            );
        }
    }
    
    
    /**
     * @private
     */
    _scrollIntoView() {
        if (this.isThrottled) return;
        this.isThrottled = true;
        window.requestAnimationFrame(() => {
            this.isThrottled = false;
            if (!this.spanActive) return;
            this.spanActive.scrollIntoView({
                behavior: 'auto', 
                block: 'nearest',
                inline: 'nearest'
            });
        });
    }

    
        
    
    /**
     * @private
     * @param {KeyboardEvent} event
     */
    _onKeydown(event) {

        /** @type {OATreeNodeData} */
        const tndSpanActive = this.spanActive ? this.tree.getTreeNodeData(this.spanActive) : null;

        if ((event.ctrlKey && event.key === 'ArrowUp') || event.key === 'ArrowLeft') {
            if (!tndSpanActive) return;
            event.stopPropagation();
            event.preventDefault();
            if (tndSpanActive.isExpanded && tndSpanActive.treeNode.getTreeNodes().length > 0) {
                let eleA = this.spanActive.previousElementSibling;
                if (eleA.tagName !== 'A') eleA = eleA.previousElementSibling;
                if (eleA) eleA.click();
            }
        }
        else if (event.key === 'ArrowUp') {
            if (!tndSpanActive) return;
            event.stopPropagation();
            event.preventDefault();

            let tnd = this.getPreviousTreeNodeData(tndSpanActive);
            if (!tnd) tnd = tndSpanActive;

            let eleNext = this.tree.getSpanElement(tnd);
            if (eleNext !== this.spanActive) {
                this.setActiveSpanElement(eleNext);
            }
            else {
                const eleScroll = this.findScrollingParent(this.spanActive);
                if (eleScroll) {
                    eleScroll.scrollTo({
                        top: 0,
                        behavior: 'smooth'
                    });
                }
            }
        }
        else if ((event.ctrlKey && event.key === 'ArrowDown') || event.key === 'ArrowRight') {
            if (!tndSpanActive) return;
            event.stopPropagation();
            event.preventDefault();
            if (!tndSpanActive.isExpanded && tndSpanActive.treeNode.getTreeNodes().length > 0) {
                let eleA = this.spanActive.previousElementSibling;
                if (eleA.tagName !== 'A') eleA = eleA.previousElementSibling;
                if (eleA) eleA.click();
            }
        }
        else if (event.key === 'ArrowDown') {
            event.stopPropagation();
            event.preventDefault();
            
            let tnd;
            if (tndSpanActive) {
                tnd = this.getNextTreeNodeData(tndSpanActive);
            }
            else {
                if (!this.tree.treeNodeDataRoot.treeNodeDatas?.length) return;
                tnd = this.tree.treeNodeDataRoot.treeNodeDatas[0];
            }
            if (!tnd) tnd = tndSpanActive;

            let eleNext = this.tree.getSpanElement(tnd);
            if (eleNext !== this.spanActive) {
                this.setActiveSpanElement(eleNext);
            }
            else { // bottom node
                const eleScroll = this.findScrollingParent(this.spanActive);
                if (eleScroll) {
                    eleScroll.scrollTo({
                        top: eleScroll.scrollHeight - eleScroll.clientHeight,
                        behavior: 'smooth'
                    });
                }
            }
        }
        else if (event.key === 'Enter' || event.key === ' ') {
            if (!tndSpanActive) return;
            event.stopPropagation();
            event.preventDefault();
            let eleA = this.spanActive.previousElementSibling;
            if (eleA.tagName !== 'A') eleA = eleA.previousElementSibling;
            if (eleA) eleA.click();
        }
        else if (event.key === 'PageUp') {
            if (!tndSpanActive) return;
            event.stopPropagation();
            event.preventDefault();

            /** @type {HTMLElement} */
            const eleScroll = this.findScrollingParent(this.spanActive);
            if (!eleScroll) {
                let ele = this.tree.getSpanElement(this.tree.treeNodeDataRoot.treeNodeDatas[0]);
                if (ele === this.spanActive) return;
                this.setActiveSpanElement(ele);
                return;
            }

            const yScrollTop = Math.max(this.getTopRelativeTo(this.spanActive, eleScroll) - eleScroll.clientHeight, 0);

            /** @type {HTMLElement} */
            let ele = this.spanActive;

            let tnd = this.tree.getTreeNodeData(ele);
            for (; tnd.tndParent;) {

                let ySpan = this.getTopRelativeTo(ele, eleScroll);
                if (ySpan <= yScrollTop) break;

                let pos = tnd.tndParent.treeNodeDatas.indexOf(tnd);
                if (pos > 0) {
                    tnd = tnd.tndParent.treeNodeDatas[pos - 1];
                    for (; tnd.isExpanded;) {
                        let x = tnd.treeNodeDatas.length;
                        if (x === 0) break;
                        tnd = tnd.treeNodeDatas[x - 1];
                    }
                }
                else {
                    if (!tnd.tndParent.tndParent) break;
                    tnd = tnd.tndParent;
                }
                ele = this.tree.getSpanElement(tnd);
            }

            if (ele !== this.spanActive) {
                this.setActiveSpanElement(ele);
            }
            else {
                if (tnd === this.tree.treeNodeDataRoot.treeNodeDatas[0]) {
                    eleScroll.scrollTo({
                        top: 0,
                        behavior: 'smooth'
                    });
                }
            }
        }
        else if (event.key === 'PageDown') {
            if (!tndSpanActive) return;
            event.stopPropagation();
            event.preventDefault();

            /** @type {HTMLElement} */
            const eleScroll = this.findScrollingParent(this.spanActive);

            let y;
            if (eleScroll) {
                y = this.getTopRelativeTo(this.spanActive, eleScroll);
                y += eleScroll.clientHeight;
            }

            const yScrollTop = !eleScroll ? -1 : Math.min(y, eleScroll.scrollHeight);

            let tnd = tndSpanActive;
            let ele;

            for (; ;) {
                let tndx = this.getNextTreeNodeData(tnd);
                if (!tndx) break;
                tnd = tndx;

                ele = this.tree.getSpanElement(tnd);
                if (yScrollTop >= 0) {
                    let ySpan = this.getTopRelativeTo(ele, eleScroll);
                    if (ySpan > yScrollTop) break;
                }
            }
            if (!ele) ele = this.tree.getSpanElement(tnd);

            if (ele === this.spanActive) {
                if (eleScroll) {
                    eleScroll.scrollTo({
                        top: Math.max(0, eleScroll.scrollHeight - eleScroll.clientHeight),
                        behavior: 'smooth'
                    });
                }
                return;
            }

            this.setActiveSpanElement(ele);
        }
        else if (event.key === 'Home') {
            event.stopPropagation();
            event.preventDefault();

            const tnd = this.tree.treeNodeDataRoot.treeNodeDatas[0];
            const ele = this.tree.getSpanElement(tnd);

            const eleScroll = this.findScrollingParent(ele);
            if (!eleScroll) {
                let ele = this.tree.getSpanElement(this.tree.treeNodeDataRoot.treeNodeDatas[0]);
                if (ele === this.spanActive) return;

                this.setActiveSpanElement(ele);
                return;
            }

            if (ele === this.spanActive) {
                eleScroll.scrollTo({
                    top: 0,
                    behavior: 'smooth'
                });
            }
            else {
                this.setActiveSpanElement(ele);
            }
        }
        else if (event.key === 'End') {
            event.stopPropagation();
            event.preventDefault();

            /** @type {HTMLElement} */
            const eleScroll = this.findScrollingParent(this.spanActive);


            let tnd = tndSpanActive;
            if (!tnd) tnd = this.tree.treeNodeDataRoot.treeNodeDatas[0];
            let ele;

            for (; ;) {
                let tndx = this.getNextTreeNodeData(tnd);
                if (!tndx) break;
                tnd = tndx;
                ele = this.tree.getSpanElement(tnd);
            }
            if (!ele) ele = this.tree.getSpanElement(tnd);

            if (ele === this.spanActive) {
                if (eleScroll) {
                    eleScroll.scrollTo({
                        top: Math.max(0, eleScroll.scrollHeight - eleScroll.clientHeight),
                        behavior: 'smooth'
                    });
                }
                return;
            }
            this.setActiveSpanElement(ele);
        }
    }

    getNextTreeNodeData(tnd) {
        if (tnd.isExpanded && tnd.treeNodeDatas?.length) {
            tnd = tnd.treeNodeDatas[0];
            return tnd;
        }
        return this._getNextTreeNodeData(tnd, 0);
    }
    /**
     * @private
     */
    _getNextTreeNodeData(tnd, cnt) {
        if (cnt > 100) return;
        if (!tnd.tndParent) return;
        let pos = tnd.tndParent.treeNodeDatas.indexOf(tnd);
        if (pos + 1 < tnd.tndParent.treeNodeDatas.length) {
            return tnd.tndParent.treeNodeDatas[pos + 1];
        }
        tnd = tnd.tndParent;
        tnd = this._getNextTreeNodeData(tnd, cnt+1);
        return tnd;
    }

    getPreviousTreeNodeData(/** @type {OATreeNodeData}*/ tnd) {
        let pos = tnd.tndParent.treeNodeDatas.indexOf(tnd);
        if (pos === 0) {
            if (!tnd.tndParent.tndParent) return tnd;
            return tnd.tndParent;
        }
        tnd = tnd.tndParent.treeNodeDatas[pos - 1];
        return this._getBottomTreeNodeData(tnd, 0);
    }

    /**
     * @private
     */
    _getBottomTreeNodeData(tnd, cnt) {
        if (cnt > 100) return tnd;
        if (!tnd.isExpanded) return tnd;

        let x = tnd.treeNodeDatas.length;
        if (x === 0) return tnd;
        tnd = tnd.treeNodeDatas[x - 1];

        tnd = this._getBottomTreeNodeData(tnd, cnt+1);

        return tnd;
    }

    /**
     * @param {HTMLElement} element
     * @param {HTMLElement} container
     */
    getTopRelativeTo(element, container) {
        let t1 = container.getBoundingClientRect().top;

        let t2 = element.getBoundingClientRect().top;
        t2 += container.scrollTop;

        let top = t2 - t1;
        return top;
    }

    findScrollingParent(element) {
        let parent = element.parentElement;

        while (parent) {
            const overflowY = window.getComputedStyle(parent).overflowY;
            if (
                (overflowY === 'scroll' || overflowY === 'auto') &&
                parent.scrollHeight > parent.clientHeight
            ) {
                return parent; // Found a scrolling parent
            }

            parent = parent.parentElement; // Move up the DOM tree
        }
        return null; // No scrolling parent found
    }

    loadTreeNodeDataFromDOM() {
        /** @type {HTMLElement} */
        const eleTree = this.tree.element;
        if (eleTree.tagName !== 'UL') {
            throw new Error('HTML element tagName for OATree must be a UL');
        }

        this._loadTreeNodeDataFromUL(eleTree, this.tree.treeNodeDataRoot, this.tree.treeNodeDataRoot.treeNodes[0]);
    }

    /**
     * NOTE:  this  needs to be verified against the current html1, html2 in OATree.js
     * @private
     * @param {HTMLElement} ele
     * @param {OATreeNodeData} tnd
     * @param {OATreeNode} tn
     */
    _loadTreeNodeDataFromUL(ele, tnd, tn) {

        for (let /** @type {HTMLElement} */ eleChild of ele.children) {

            if (eleChild.tagName !== 'LI') continue;

            /** @type {HTMLElement} */
            let eleSpan = eleChild.children[0];
            if (eleSpan && eleSpan.tagName === 'SPAN') {
                /** @type{OATreeNodeData} */
                let tndx = new OATreeNodeData();
                tndx.treeNode = tn;
                tndx.tndParent = tnd;
                tndx.label = eleSpan.innerText.trim();
                tnd._addTreeNodeData(tndx);
                continue;
            }

            if (eleSpan.tagName !== 'DIV') continue;
            /** @type {HTMLDivElement} */
            let eleDiv = eleSpan;
            eleSpan = eleDiv.children[1];
            let tndx;
            if (eleSpan && eleSpan.tagName === 'SPAN') {
                tndx = new OATreeNodeData();
                tndx.treeNode = tn;
                tndx.tndParent = tnd;
                tndx.label = eleSpan.innerText.trim();
                tnd._addTreeNodeData(tndx);
                tnd.isExpanded = eleDiv.classList.contains('show');
            }

            // recurse
            if (tn.getTreeNodes()) {
                for (let /** @type {OATreeNode} */ tnChild of tn.getTreeNodes()) {
                    eleDiv = eleDiv.nextElementSibling;
                    if (eleDiv && eleDiv.tagName === 'DIV') {
                        let eleUL = eleDiv.children[0];
                        if (eleUL && eleUL.tagName === 'UL') {
                            this._loadTreeNodeDataFromUL(eleUL, tndx, tnChild);
                        }
                    }
                }
            }
        }
    }
}
