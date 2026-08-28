
import { OAClient } from '@oa-web/index.js';

export default class OATable {

    /** @type {HTMLDivElement} wrapper for table */
    element;

    /** @type {number} server side assigned seq Id */
    id;

    /** @type {HTMLTableElement} */
    eleTable;

    /** @type {HTMLBodyElement} */
    eleTBody;
    /** @type {HTMLBodyElement} */
    eleTHead;
    /** @type {HTMLBodyElement} */
    eleTFoot;


    /** @type {OATableColumn[]} tableColumns */
    tableColumns = [];

    /** @type {OATableRow[]} tableRows */
    tableRows = [];

    
    /** @type {number} */
    currentRow;

    /** @type {number} */
    currentCol;
    
    /** @type {number} */
    newRow;

    /** @type {number} */
    newCol;
    
    /** @type {boolean} */
    editMode;
        

    /** @type {boolean} */
    isThrottled;


    /**
      * @param {HTMLElement} element
      * @param {number} id
      */
    constructor(element, id) {
        this.element = element;
        this.id = id;
    }

    close() {
        this.eleTable.removeEventListener('click', this.handleClickEvent);
        this.eleTable.removeEventListener('keydown', this.handleKeyDownEvent, true);
        this.element.removeEventListener('focus', this.handleFocus);
    }

    initialize() {
        if (!this.element || this.element.tagName !== 'DIV') {
            throw new Error("element must be a Div that wraps a Table");
        }

        this.eleTable = this.element.firstElementChild;
        if (!this.eleTable || this.eleTable.tagName !== 'TABLE') {
            throw new Error("first child element must be a Table");
        }

        this.eleTHead = this.eleTable.querySelector(':scope thead');
        this.eleTBody = this.eleTable.querySelector(':scope tbody');
        this.eleTFoot = this.eleTable.querySelector(':scope tfoot');

        this.eleTable.addEventListener('click', this.handleClickEvent);
        this.eleTable.addEventListener('keydown', this.handleKeyDownEvent, true);
        this.element.addEventListener('focus', this.handleFocusEvent);
    }

    /** @param {OATableColumn}  tc */
    addColumn(tc) {
        this.tableColumns.push(tc);
    }

    /** @param {FocusEvent} event */
    handleFocusEvent = (event) => {
        this.eleTable.focus();
    }
        
    /** @param {KeyboardEvent} event */
    handleKeyDownEvent = (event) => {
        const row = this.getCurrentRow();
        if (row < 0) return;
        const rowMax = this.eleTBody.childElementCount - 1;

        /** @type {OATableColumn} */
        let tcFound;
        for (let /** @type {OATableColumn} */ tc of this.tableColumns) {
            if (tc.eleEditor === event.target) {
                tcFound = tc;
                break;
            }
        }

        /** @type {HTMLElement} */
        let eleTarget = event.target;
        for (; ;) {
            if (eleTarget.tagName === 'TD' || eleTarget.tagName === 'TH' || eleTarget.tagName === 'TABLE') {
                break;
            }
            eleTarget = eleTarget.parentElement;
            if (!eleTarget) break;
        }
        const col = this.getCurrentColumn();
        const colMax = (this.tableColumns?.length ?? 0) - 1;
        
        if (event.key === 'ArrowDown') {
            if (tcFound && document.activeElement === tcFound.eleEditor) {
                if (tcFound.usesUpDownArrowKeys && !event.ctrlKey) {
                    return;
                }  
            } 
            event.preventDefault();
            event.stopPropagation();
            if (row < rowMax) {
                this.setActiveThrottled(row + 1, col);
            }
        }
        else if (event.key === 'ArrowUp') {
            if (tcFound && document.activeElement === tcFound.eleEditor) {
                if (tcFound.usesUpDownArrowKeys && !event.ctrlKey) {
                    return;
                }  
            } 
            event.preventDefault();
            event.stopPropagation();
            if (row > 0) {
                this.setActiveThrottled(row - 1, col);
            }
        }
        else if (event.key === 'PageDown') {
            event.preventDefault();
            event.stopPropagation();
            if (row == rowMax) return;
            let h = this.element.clientHeight - (this.eleTHead.clientHeight + this.eleTFoot.clientHeight);
            let r = row;
            let y = 0;

            for (;;) {
                if (r == rowMax) break;
                y += this.eleTBody.children[r].clientHeight;
                if (y >= h) break;
                r++;
            }
            this.setActiveThrottled(r, col);
        }
        else if (event.key === 'PageUp') {
            event.preventDefault();
            event.stopPropagation();
            if (row == 0) return;
            
            let h = this.element.clientHeight - (this.eleTHead.clientHeight + this.eleTFoot.clientHeight);
            let r = row;
            let y = 0;

            r--;
            for (;;) {
                if (r == 0) break;
                y += this.eleTBody.children[r].clientHeight;
                if (y >= h) break;
                r--;
            }
            this.setActiveThrottled(r, col);
            
        }
        else if (event.key === 'ArrowLeft') {
            if (tcFound && document.activeElement === tcFound.eleEditor && tcFound.eleEditor.tagName === "INPUT" && ["text", "password", "email", "search", "url", "tel"].includes(tcFound.eleEditor.type)) {
                const p1 = tcFound.eleEditor.selectionStart;
                const p2 = tcFound.eleEditor.selectionEnd;
                if (p1 !== 0 && p2 !== 0) {
                    return;
                }                
            }

            event.preventDefault();
            event.stopPropagation();
            if (col > 0) {
                if (this.editMode) this.setActiveEdit(row, col-1, true);
                else this.setActiveThrottled(row, col-1);
            }
        }
        else if (event.key === 'ArrowRight') {
            if (tcFound && document.activeElement === tcFound.eleEditor && tcFound.eleEditor.tagName === "INPUT" && ["text", "password", "email", "search", "url", "tel"].includes(tcFound.eleEditor.type)) {
                const p1 = tcFound.eleEditor.selectionStart;
                const p2 = tcFound.eleEditor.selectionEnd;
                let val = tcFound.eleEditor.value;
                if (p1 !== val.length && p2 !== val.length) {
                    return;
                }                
            }

            event.preventDefault();
            event.stopPropagation();
            if (col < colMax) {
                if (this.editMode) this.setActiveEdit(row, col+1, true);
                else this.setActiveThrottled(row, col+1);
            }
        }
        else if (event.key === " " || event.key === "Enter") {
            if (!this.editMode || !tcFound?.eleEditor) {
                event.preventDefault();
                event.stopPropagation();
                if (!this.editMode) {   
                    this.setActiveEdit(this.getCurrentRow(), this.getCurrentColumn());
                }
                else this.setActive(this.getCurrentRow(), this.getCurrentColumn(), false);
            }          
        }
        
    }


    /** @param {MouseEvent} event */
    handleClickEvent = (event) => {  // use so that "this" refers to class.
        /** @type {HtmlElment} */
        let eleTarget = event.target;
        
        if (eleTarget.tagName === 'I') {
            let ele = eleTarget.parentElement;
            if (ele.tagName === 'TH') {
                let c = Array.from(ele.parentElement.children).indexOf(ele);
                if (c !== 1) return;
                ele = ele.parentElement;
                if (ele.tagName !== 'TR') return;
                if (ele.parentElement?.tagName !== 'THEAD') return;
                
                let obj = {
                    id: this.id,
                    type: 'clickheadercheckbox',
                };
                OAClient.sendEventToServer(obj);
                return;
            }
        }
        
        
        
        for (; ;) {
            if (eleTarget.tagName === 'TD' || eleTarget.tagName === 'TH') {
                break;
            }
            eleTarget = eleTarget.parentElement;
            if (!eleTarget) break;
        }
        if (!eleTarget) return;

        if (eleTarget.tagName === 'TD' || eleTarget.tagName === 'TH') {
            const eleRow = eleTarget.parentElement;
            if (eleRow.tagName !== 'TR') return;
            if (eleRow.parentElement?.tagName !== 'TBODY') return;
            
            const row = Array.from(eleRow.parentElement.children).indexOf(eleRow);
            const col = Array.from(eleRow.children).indexOf(eleTarget);

            this.setActiveEdit(row, col);
        }
    };

    getCurrentRow() {
        if (this.newRow !== null && this.newRow >= 0) return this.newRow;
        return this.currentRow;
    }
    getCurrentColumn() {
        if (this.newCol !== null && this.newCol >= 0) return this.newCol;
        return this.currentCol;
    }

    setActiveThrottled(row, col) {
        this.newRow = row;
        this.newCol = col;
        if (this.isThrottled) return;
        this.isThrottled = true;

        setTimeout(() => {
            if (this.newRow < 0) return;
            const r = this.newRow;
            const c = this.newCol;
            this.newRow = -1;
            this.newCol = -1;
            this.isThrottled = false;
            this.setActive(r, c, false);
            this._scrollIntoView();
        }, 120);
    }

    setActiveEdit(row, col) {
        this.newRow = -1;
        this.newCol = -1;
        this.setActive(row, col, true, false);
        this._scrollIntoView();
    }

    setActiveRow(row) {
        this.newRow = -1;
        this.newCol = -1;
        this.setActive(row, -1, false, false);
        this._scrollIntoView();
    }

    setActiveFromServer(row) {
        let b = row >= 0 && this.editMode;
        this.setActive(row, this.getCurrentColumn(), b, true);
        this._scrollIntoView();
    }
    

    /**
     * Main method for setting active row, column and editMode.
     * @param {boolean} newEditMode true if cell editor should be visible.
     */
    setActive(newRow, newCol, newEditMode, isFromServer) {
        //qqq console.log(`${newRow},${newCol} ${newEditMode}`);
        newRow ??= -1;
        newCol ??= -1;

        if (!isFromServer && newRow !== this.currentRow) {
            let obj = {
                id: this.id,
                type: 'setactiverow',
                oldValue: this.currentRow,
                newValue: newRow
            };
            OAClient.sendEventToServer(obj);
        }
                
        const prevRow = this.currentRow ?? -1;
        const prevCol = this.currentCol ?? -1;
        
        this.currentRow = newRow;
        this.currentCol = newCol;

        const elePrevRow = (prevRow >= 0) ? this.eleTBody.children[prevRow] : null;
        const eleNewRow = (newRow >= 0) ? this.eleTBody.children[newRow] : null;

        const elePrevCol = (elePrevRow && (prevCol >= 0)) ? elePrevRow.children[prevCol] : null;
        const eleNewCol = (eleNewRow && (newCol >= 0)) ? eleNewRow.children[newCol] : null;

        const tcPrev = (prevCol >= 0) ? this.tableColumns[prevCol] : null; 
        const tcNew = (newCol >= 0) ? this.tableColumns[newCol] : null; 
        
        const prevEditMode = this.editMode;
        this.editMode = newEditMode;
        
        if (newRow !== prevRow) {
            elePrevRow?.classList.remove("table-active");
            eleNewRow?.classList.add("table-active");
        }
            
        if (!tcPrev?.eleEditor || !prevEditMode) elePrevCol?.classList.remove('oa-selected');
        if (!tcNew?.eleEditor || !newEditMode) eleNewCol?.classList.add('oa-selected');
        
        /** @type {OATableColumn} */
        let tcFocus = null;
        
        if (newRow === prevRow) {
            let colPos = 0;
            for (let tc of this.tableColumns) {
                if (newEditMode) {
                    if (tc.eleEditor) {
                        if (!prevEditMode) {
                            while (eleNewRow?.children[colPos].firstChild) {
                                eleNewRow.children[colPos].removeChild(eleNewRow.children[colPos].firstChild);
                            }
                            eleNewRow.children[colPos].appendChild(tc.eleEditor);
                            tc.eleEditor.style.visibility = 'visible';
                        }
                        if (newCol === colPos) tcFocus = tc;
                    }
                    else {
                        if (newCol === colPos) {
                            if (document.activeElement !== this.eleTable) {
                                this.eleTable.focus();
                            }
                        }
                    }
                }
                else {
                    if (prevEditMode) {
                        if (tc.eleEditor && newRow >= 0) {
                            tc.eleEditor.style.visibility = 'hidden';
                            let val = this.tableRows[newRow].tableDatas[colPos].value;
                            eleNewRow.children[colPos].innerHTML = val;
                        }
                    }
                    if (newCol === colPos) {
                        if (document.activeElement !== this.eleTable) {
                            this.eleTable.focus();
                        }
                    }
                } 
                colPos++;
            }            
        }
        else {
            if (prevRow >= 0 && prevEditMode) {
                let colPos = 0;
                for (let tc of this.tableColumns) {
                    if (tc.eleEditor) {
                        tc.eleEditor.style.visibility = 'hidden';
                        if (elePrevRow) {
                            let val = this.tableRows[prevRow].tableDatas[colPos].value;
                            elePrevRow.children[colPos].innerHTML = val;
                        }
                    }
                    colPos++;
                }            
            }

            if (newEditMode && newRow >= 0) {
                let colPos = 0;
                for (let tc of this.tableColumns) {
                    if (tc.eleEditor) {
                        while (eleNewRow?.children[colPos].firstChild) {
                            eleNewRow.children[colPos].removeChild(eleNewRow.children[colPos].firstChild);
                        }
                        eleNewRow.children[colPos].appendChild(tc.eleEditor);
                        tc.eleEditor.style.visibility = 'visible';
                        if (newCol === colPos) tcFocus = tc;
                    }
                    else {
                        if (newCol === colPos) {
                            if (document.activeElement !== this.eleTable) {
                                this.eleTable.focus();
                            }
                        }
                    }
                    colPos++;
                }                
            }
            if (!newEditMode && newCol >= 0) {
                if (document.activeElement !== this.eleTable) {
                    this.eleTable.focus();
                }
            }
        }

        if (tcFocus && tcFocus.eleEditor) {
            tcFocus.eleEditor.focus();
            if (tcFocus.eleEditor.tagName === "INPUT" && ["text", "password", "email", "search", "url", "tel"].includes(tcFocus.eleEditor.type)) {
                let val = tcFocus.eleEditor.value;
                if (val) {
                    tcFocus.eleEditor.selectionStart = 0;
                    tcFocus.eleEditor.selectionEnd = val.length;
                    tcFocus.eleEditor.selectionDirection = 'forward';
                }
            }
        }
    }

    _scrollIntoView() {
        let rowx = this.currentRow;
        if (rowx < 0) rowx = 0;

        const eleRow = this.eleTBody.children[rowx];
        if (!eleRow) return;
        
        const rec = this.element.getBoundingClientRect();
        const recRow = eleRow.getBoundingClientRect();
        const recHead = this.eleTHead.getBoundingClientRect();
        const recFoot = this.eleTFoot.getBoundingClientRect();

        const topOffset = recHead.height;  // Space occupied by the sticky header
        const bottomOffset = recFoot.height;  // Space occupied by the sticky footer

        const rowTop = recRow.top - rec.top; // Row position relative to wrapper
        const rowBottom = rowTop + recRow.height;

        if (rowTop < topOffset) {
            this.element.scrollTo({
                top: this.element.scrollTop - (topOffset - rowTop),
                behavior: 'auto'
            });
        } else if (rowBottom > rec.height - bottomOffset) {
            this.element.scrollTo({
                top: this.element.scrollTop + (rowBottom - (rec.height - bottomOffset)),
                behavior: 'auto'
            });
        }
    }

    clearRows() {
        this.tableRows.length = 0;
        this.eleTBody.innerHTML = "";
        this.setActive(-1, -1, false, true); // dont  send server event
        this._scrollIntoView();
    }

    /** @param {string[][]} data */
    loadRows(data) {
        this.clearRows();
        for (let ss of data) {
            let tr = this.createTableRow(ss);
            this.addRow(tr);
        }
        this._scrollIntoView();
    }
    
    /** @param {string[]} values for each column. */
    createTableRow(values) {
        let tds = [];
        for (let s of values) {
            let td = new OATableData(s);
            tds.push(td);
        }
        let tr = new OATableRow(tds);
        return tr;
    }

    /** @param {OATableRow} tableRow */
    addRow(tableRow) {
        this.tableRows.push(tableRow);

        let html = "<tr>";
        let col = 0;
        for (let td of tableRow.tableDatas) {
            let tc = this.tableColumns[col++];

            let classes = tc?.classes ? ` class="${tc.classes}"` : '';
            let styles = tc?.styles ? ` style="${tc.styles}"` : '';

            html += `<td${classes}${styles}>${td.value}</td>`;
        }
        html += "</tr>";

        this.eleTBody.insertAdjacentHTML('beforeend', html);
    }

    /** @param {OATableRow} tableRow */
    insertRow(tableRow, row) {
        this.tableRows.splice(row, 0, tableRow);
        
        let html = "<tr>";
        let col = 0;
        for (let td of tableRow.tableDatas) {
            let tc = this.tableColumns[col++];

            let classes = tc.classes ? ` class="${tc.classes}"` : '';
            let styles = tc.styles ? ` style="${tc.styles}"` : '';

            html += `<td${classes}${styles}>${td.value}</td>`;
        }
        html += "</tr>";
        this.eleTBody.children[row].insertAdjacentHTML('beforebegin', html);
    }

    /** @param {OATableRow} tableRow */
    updateRow(tableRow, row) {
        if (row < 0 || row >= this.eleTBody.children.length) return;
        
        this.tableRows[row] = tableRow;
        if (!this.editMode || row !== this.currentRow)  {
            let col = 0;
            for (let td of tableRow.tableDatas) {
                this.eleTBody.children[row].children[col++].innerHTML = td.value;
            }
        }
    }
        
    removeRow(row) {
        if (row >= 0 && row < this.eleTBody.children.length) {
            let tr = this.eleTBody.children[row];
            this.eleTBody.removeChild(tr);
            this.tableRows.splice(row, 1);
        }
    }
    
    renumberRows(beginRow) {
        let x = this.eleTBody.children.length;
        for (let i = beginRow; i<x; i++) {
            this.eleTBody.children[i].firstElementChild.innerHTML = (i+1);
        }
    }
    
    updateHeaderSelectCheckBox(checkedType) {
        let bi;
        if (checkedType === 2) bi = 'bi-check-square';    
        else if (checkedType === 1) bi = 'bi-dash-square';
        else bi = 'bi-square';    
        
        let ele = this.element.querySelector('thead>tr:nth-of-type(2)>th:nth-of-type(2)');
        
        let html = `<i class="bi ${bi} me-2" tabindex="0"></i>`;
        ele.innerHTML = html;
    }
}

export class OATableColumn {
    /** @type {string} */
    classes;

    /** @type {string} */
    styles;

    /** @type {boolean} */
    usesUpDownArrowKeys; // true if this component uses up/down arrows

    constructor(title, width,
        eleEditor, idEditor,
        eleFilter, idFilter
    ) {
        /** @type {string} */
        this.title = title;

        /** @type {number} */
        this.width = width;

        /** @type {HtmlElement} */
        this.eleEditor = eleEditor;

        /** @type {number} */
        this.idEditor = idEditor;

        /** @type {HtmlElement} */
        this.eleFilter = eleFilter;

        /** @type {number} */
        this.idFilter = idFilter;
    }
}

export class OATableRow {

    /** @type {OATableData[]} tableDatas */
    tableDatas = [];

    /**
     * @param {OATableData[]} tableDatas 
     */
    constructor(tableDatas) {
        this.tableDatas = tableDatas;
    }
}

export class OATableData {
    /**
     * @param {string} value 
     */
    constructor(value) {
        /**
         * @type {string} value 
         */
        this.value = value;
    }
}

