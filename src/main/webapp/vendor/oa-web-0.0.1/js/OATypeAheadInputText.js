
import { OAClient } from '@oa-web/index.js';

/*
 * TODO:
 * use a cache
 * add an line at the bottom, LI, for message
 *    when only max amount is returned, etc.
 *      "(listing max 35)"
 * 
 * highlight matching
 * 
 */

/*
HTML:

<input data-oa-name="taSchool" 
  type="text" 
  class="form-control dropdown-toggle" 
  placeholder="Search..." 
  data-bs-toggle="dropdown" 
  data-oa-web="type-ahead"
  autocomplete="off"
>
<ul class="dropdown-menu" data-oa-web="type-ahead"></ul>

<ul> is optional

*/

/**
 * Used for type ahead searches.
 */
export default class OATypeAheadInputText {

    /** @type {HTMLElement} */
    element;
    /** @type {number} server side assigned seq Id */
    id;

    /** @type {HTMLUListElement} */
    elementUL;

    /** @type {HTMLLIElement} */
    elementLiSelected;
    
        
    /** @type {number} allows size and maxSize to give a min and max width (in chars) that adjusts based on input value */
    maxSize;
    /** @type {number} original size */
    holdSize;

    dropdownInstance;
    /** @type {object[]} json objects from server on server. */ 
    objs;    

    /** @type {boolean} */
    isThrottled;
    
    
    /**
     * @param {HTMLElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;

        this.init();
    }
    
    init() {

        this.element.dataset.oaWeb = "type-ahead";
                
        this.elementUL = this.element.nextElementSibling;
        if (!this.elementUL || this.elementUL.tagName !== 'UL') {
            this.elementUL = document.createElement("ul");
            this.elementUL.classList.add("dropdown-menu");
            this.element.parentElement.insertBefore(this.elementUL, this.element.nextElementSibling);        
        }
        this.elementUL.dataset.oaWeb = "type-ahead";


        const funcLookup = this.throttleLookup();
        
        this.element.addEventListener('input', () => {
            if (funcLookup()) this.showPopup();
        });
        
        this.element.addEventListener('click', (/** @type {}*/ event) => {
            funcLookup();
            this.showPopup();
        });
        
                
        this.element.addEventListener('keydown', (/** @type {KeyboardEvent} */ event) => {
            if (event.key === 'ArrowDown') {
                event.preventDefault();
                event.stopPropagation();
                if (!this.isPopupShowing()) {
                    this.showPopup();
                }
                else if (!this.elementLiSelected) {
                    if (this.elementUL.children?.length) {
                        this.setSelected(this.elementUL.children[0]);
                    }
                }
                else {
                    let li = this.elementLiSelected.nextElementSibling;
                    if (li) this.setSelected(li);
                }
            }
            else if (event.key === 'ArrowUp') {
                event.preventDefault();
                event.stopPropagation();
                if (!this.isPopupShowing()) {
                    this.showPopup();
                }
                else if (this.elementLiSelected) {
                    let li = this.elementLiSelected.previousElementSibling;
                    if (li) this.setSelected(li);
                }
            }
            else if (event.key === 'PageDown') {
                event.preventDefault();
                event.stopPropagation();
                if (!this.isPopupShowing()) {
                    this.showPopup();
                    return;
                }
                if (!this.elementLiSelected) {
                    if (this.elementUL.children?.length) {
                        this.setSelected(this.elementUL.children[0]);
                    }
                    return;
                }
                
                let top = this.elementLiSelected.getBoundingClientRect().top;
                top += this.elementUL.clientHeight;
                
                let eleLi = this.elementLiSelected;
                for ( ; ; ) {
                    const e = eleLi.nextElementSibling;
                    if (!e) break;
                    let t = e.getBoundingClientRect().top;
                    if (t > top) break;
                    eleLi = e;
                }
                this.setSelected(eleLi);
            }
            else if (event.key === 'PageUp') {
                event.preventDefault();
                event.stopPropagation();
                if (!this.isPopupShowing()) {
                    this.showPopup();
                    return;
                }
                if (!this.elementLiSelected) {
                    if (this.elementUL.children?.length) {
                        this.setSelected(this.elementUL.children[0]);
                    }
                    return;
                }
                
                let top = this.elementLiSelected.getBoundingClientRect().top;
                top -= this.elementUL.clientHeight;
                top = Math.max(0, top);
                
                let eleLi = this.elementLiSelected;
                for ( ; ; ) {
                    let e = eleLi.previousElementSibling;
                    if (!e) break;
                    let t = e.getBoundingClientRect().top;
                    if (t < top) break;
                    eleLi = eleLi.previousElementSibling;
                }
                this.setSelected(eleLi);
            }
            else if (event.key === 'Enter') {
                if (!this.isPopupShowing()) {
                }
                else if (this.elementLiSelected) {
                    event.preventDefault();
                    event.stopPropagation();
                }
            }
            else if (event.key === 'Escape') {
                if (!this.isPopupShowing()) {
                    event.preventDefault();
                    event.stopPropagation();
                    this.hidePopup();
                }
            }
            else if (event.key === 'Delete' && this.element.selectionStart == 0) {
                if (this.isPopupShowing()) {
                    this.hidePopup();
                }
                this.element.selectionEnd = this.element.value.length;
                this.element.selectionDirection = 'forward';
            }
        });

        this.element.addEventListener('keyup', (/** @type {KeyboardEvent} */ event) => {
            if (event.key === 'ArrowLeft' || event.key === 'ArrowRight' || event.key === 'End') {
                funcLookup();
                this.showPopup();
                return;
            }
            else if (event.key === 'Enter') {
                if (!this.isPopupShowing()) {
                }
                else if (this.elementLiSelected) {
                    this.elementLiSelected.click();
                    event.preventDefault();
                    event.stopPropagation();
                }
            }
            else if (event.key === 'PageUp' || event.key === 'PageDown') {
                event.preventDefault();
                event.stopPropagation();
            }
        });        
        
        
        // note: also called when [enter] keydown
        this.elementUL.addEventListener('click', (/** @type {}*/ event) => {
            if (event.target.tagName !== 'LI') return;

            event.preventDefault();
            event.stopPropagation();

            const liElements = Array.from(this.elementUL.children);
            const pos = liElements.indexOf(event.target);
            const objId = this.objs[pos].id;

            this.element.value = event.target.textContent;
            this.adjustSize();
            this.hidePopup();
            this.update([ this.objs[pos] ]);
                         
            const obj = {
              id: this.id,
              type: 'select',
              objId: objId
            };
            OAClient.sendEventToServer(obj);
        });
        
        
        this.adjustSize();
    }
    
    setSelected(eleLi) {
        if (this.elementLiSelected) {
            this.elementLiSelected.classList.remove('oa-selected');
        }    
        if (eleLi) {
            eleLi.classList.add('oa-selected');
        }
        this.elementLiSelected = eleLi;
        this._scrollIntoView()
    }

    isPopupShowing() {
        this.getPopup();
        return this.elementUL.classList.contains('show');
    }
    getPopup() {
        if (!this.dropdownInstance) {
            this.dropdownInstance = bootstrap.Dropdown.getInstance(this.element);
        }
        return this.dropdownInstance;        
    }
    showPopup() {
        this.getPopup();
        if (this.dropdownInstance) {
            this.dropdownInstance.show();
        }
    }
    hidePopup() {
        this.getPopup();
        if (this.dropdownInstance) {
            this.dropdownInstance.hide();
        }
    }
    
    throttleLookup() {
      let timer;
      return () => {
        let bShow = true;
        clearTimeout(timer);
        
        let pos = this.element.selectionEnd;
        let value = this.element.value; 
        if (pos < value.length) value = value.substring(0, pos);
        
        if (value.length < 3) {
            this.update([]);
            bShow = false;
        }
        else {
            timer = setTimeout(() => {
              const obj = {
                id: this.id,
                type: 'search',
                search: value 
              };
              OAClient.sendEventToServer(obj);
            }, 200); // ms delay
        }        
        this.adjustSize();
        return bShow;
      };
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
    
    
    adjustSize() {
        if (!this.maxSize) return;

        if (!this.holdSize) this.holdSize = this.element.size;

        const valueLength = this.element.value.length;

        const newSize = Math.min(this.maxSize, Math.max(this.holdSize, valueLength + 1));
        this.element.size = newSize;
        this.element.style.width = 'auto'; // 'auto' to allow the size attribute to take control
    }

    
    update(objs) {
        // [ {id, display, dropDownDisplay} ]
        this.objs = objs;
        
        this.setSelected(null);        
        
        let lis = '';
        for (let obj of this.objs) {
            lis += '<li class="dropdown-item">' + obj.display + '</li>'; 
            //qqqq needs to show cursor:hand
            //or: lis += '<li><a class="dropdown-item" href="#">' + obj.display + '</a></li>';
        }
        
        this.elementUL.innerHTML = lis;
    }
    
    setMaxSize(x = 0) {
        if (x !== this.maxSize) {
            this.maxSize = x;
            this.adjustSize();
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
            if (!this.elementLiSelected) return;
            this.elementLiSelected.scrollIntoView({
                behavior: 'auto', 
                block: 'nearest',
                inline: 'nearest'
            });
        });
    }

    
    
/*qqqqqqqqqqqqqqqqqqqq
    highlightMatches(input, items) {
      const regex = new RegExp(`(${input})`, 'gi');
      items.forEach(li => {
        li.innerHTML = li.textContent.replace(regex, '<span class="highlight">$1</span>');
      });
    }
*/
}
