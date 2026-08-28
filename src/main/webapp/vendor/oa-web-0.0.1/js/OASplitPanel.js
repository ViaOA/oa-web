
/**
 * Takes a DIV element with two children DIVs and
 * makes them into a split panel with a mouse drag bar.
 */
export default class OASplitPanel {
    
    /** @type {HTMLElement} */
    element;
    /** @type {number} server side assigned seq Id */
    id;

    
    bInit;        
    div = null;
    bFirstShouldGrow;
    observer;
    xInitMouse;
    yInitMouse;
    wPanel = -1;
    hPanel = -1;
    lastGridTemplate;
    isResizing = false;

    static SplitTypeVert = 1;
    static SplitTypeHort = 2;
    splitType;
    panOne;
    panTwo;
    isThrottled = false;

    /**
     * @param {HTMLDivElement} div to split, must have 2 children divs.
     * @param {boolean} bFirstShouldGrow true if first div should take extra space.
     */
    constructor(element, id, bFirstShouldGrow) {
        
        //qqqqqqqqqq new OA
        this.element = element;
        this.id = id;
        
//qqqqqqqq was:    constructor(div, bFirstShouldGrow) {
        this.div = element;
        this.bFirstShouldGrow = bFirstShouldGrow;  //qqqqqqqqq need to send
    }

    
    
    /**
     * @private
     */
    initialize(bNeedsObserver) {
        
        if (bNeedsObserver) {
            let bInit = false;
            this.resizeObserver = new window.ResizeObserver(() => {
                if (bInit) return;
                if (this.div.offsetHeight > 0) {
                    bInit = true;
                    this.resizeObserver.disconnect();
                    this._createSplitPanel();
                }
            });
            this.resizeObserver.observe(this.div);
        }
        else {
            this._createSplitPanel();
        }
    }

    /**
     * @private
     */
    _createSplitPanel() {

        if (this.div.children.length !== 2) {
            throw new Error('OASplitPanel requires exactly two child div elements.');
        }

        [this.panOne, this.panTwo] = this.div.children;

        if (this.panOne.offsetTop === this.panTwo.offsetTop) {
            this.splitType = OASplitPanel.SplitTypeHort;
            if (this.bFirstShouldGrow) {
                let w = this.panTwo.offsetWidth;
                this.lastGridTemplate = `minmax(20px, 1fr) 5px minmax(20px, ${w}px)`;
            }
            else {
                let w = this.panOne.offsetWidth;
                this.lastGridTemplate = `minmax(20px, ${w}px) 5px minmax(20px, 1fr)`;
            }
            this.div.style.gridTemplateColumns = this.lastGridTemplate;
        }
        else {
            this.splitType = OASplitPanel.SplitTypeVert;
            if (this.bFirstShouldGrow) {
                let h = this.panTwo.offsetHeight;
                this.lastGridTemplate = `minmax(20px, 1fr) 5px minmax(20px, ${h}px)`;
            }
            else {
                let h = this.panOne.offsetHeight;
                this.lastGridTemplate = `minmax(20px, ${h}px) 5px minmax(20px, 1fr)`;
            }
            this.div.style.gridTemplateRows = this.lastGridTemplate;
        }

        // add resizebar between two panels
        const resizeBar = document.createElement('div');
        if (this.splitType === OASplitPanel.SplitTypeVert) {
            resizeBar.classList.add('oa-resize-bar-hort');
        }
        else {
            resizeBar.classList.add('oa-resize-bar-vert');
        }
        this.div.insertBefore(resizeBar, this.panTwo);

        resizeBar.setAttribute('role', 'separator');
        resizeBar.setAttribute('aria-orientation', this.splitType === OASplitPanel.SplitTypeVert ? 'vertical' : 'horizontal');

        const resizeGrip = document.createElement('div');
        if (this.splitType === OASplitPanel.SplitTypeVert) {
            resizeGrip.classList.add('oa-resize-grip-hort');
        }
        else {
            resizeGrip.classList.add('oa-resize-grip-vert');
        }
        resizeBar.appendChild(resizeGrip);

        resizeBar.addEventListener('mousedown', (e) => {
            this.isResizing = true;
            this.xInitMouse = e.clientX;
            this.yInitMouse = e.clientY;

            if (this.bFirstShouldGrow) {
                this.wPanel = this.panTwo.clientWidth;
                this.hPanel = this.panTwo.clientHeight;
            }
            else {
                this.wPanel = this.panOne.clientWidth;
                this.hPanel = this.panOne.clientHeight;
            }
            document.addEventListener('mousemove', this.onMouseMove);
            document.addEventListener('mouseup', this.onMouseUp);
            e.stopPropagation();
            e.preventDefault();

            document.body.style.userSelect = 'none';

            return false;
        });
    }

    /**
     * @callback
     * @param {MouseEvent} e
     */
    onMouseMove = (e) => {
        if (!this.isResizing) return;

        e.stopPropagation();
        e.preventDefault();

        if (this.isThrottled) return;
        this.isThrottled = true;
        window.requestAnimationFrame(() => {
            this._onMouseMove(e);
        });
    };

    /**
     * @private
     * @param {MouseEvent} e
     */
    _onMouseMove = (e) => {
        this.isThrottled = false;

        let xMouse = e.clientX - this.xInitMouse;
        let yMouse = e.clientY - this.yInitMouse;

        if (this.splitType === OASplitPanel.SplitTypeHort) {
            let w;
            if (this.bFirstShouldGrow) {
                w = this.wPanel - xMouse;
                if (w < 20) return;
                if (w > (this.div.clientWidth - 20)) return;
                this.lastGridTemplate = `minmax(20px, 1fr) 5px minmax(20px, ${w}px)`;
            }
            else {
                w = this.wPanel + xMouse;
                if (w < 20) return;
                if (w > (this.div.clientWidth - 20)) return;
                this.lastGridTemplate = `minmax(20px, ${w}px) 5px minmax(20px, 1fr)`;
            }
        }
        else {
            let h;
            if (this.bFirstShouldGrow) {
                h = this.hPanel - yMouse;
                if (h < 20) return;
                if (h > (this.div.clientHeight - 20)) return;
                this.lastGridTemplate = `minmax(20px, 1fr) 5px minmax(20px, ${h}px)`;
            }
            else {
                h = this.hPanel + yMouse;
                if (h < 20) return;
                if (h > (this.div.clientHeight - 20)) return;
                this.lastGridTemplate = `minmax(20px, ${h}px) 5px minmax(20px, 1fr)`;
            }
        }

        if (this.splitType === OASplitPanel.SplitTypeHort) {
            this.div.style.gridTemplateColumns = this.lastGridTemplate;
        }
        else {
            this.div.style.gridTemplateRows = this.lastGridTemplate;
        }
    };

    /**
     * @callback
     * @param {MouseEvent} e
     */
    onMouseUp = (e) => {
        document.body.style.userSelect = 'auto';
        document.removeEventListener('mousemove', this.onMouseMove);
        document.removeEventListener('mouseup', this.onMouseUp);
        this.isResizing = false;
        this.isThrottled = false;

        e.stopPropagation();
        e.preventDefault();
        return false;
    };
}
