
import { OAClient } from '@oa-web/index.js';

/**
 * Used for HTML Select elements.  Allows single or multi selection.
 * @see OAHtmlSelect
 */
export default class HtmlSelect {

    /** @type {HTMLElement} */
    element;
    /** @type {number} */
    id;
    
    /**
     * @param {HTMLSelectElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;
        
        element.addEventListener('change', (event) => {
            /** @type {HTMLOptionsCollection} */
            const opts = Array.from(this.element.selectedOptions);
            const indexes = opts.map(opt => opt.index); 
            
            let obj = {
                id: this.id,
                type: 'change', 
                selectedIndexes: indexes.join(',')
            }
            OAClient.sendEventToServer(obj);
        });
    }
    
    add(text, value) {
        const newOption = new Option(text, value)
        this.element.add(newOption);
    }
    
    insert(text, value, pos) {
        const newOption = new Option(text, value)
        this.element.add(newOption, pos);
    }

    remove(pos) {
        this.element.remove(pos);
    }
    
    setOptions(options) {
        this.clear();
        options.forEach(({ text, value }) => {
            const newOption = new Option(text, value);
            this.element.add(newOption);
        });
    }
    
    clear() {
        this.element.options.length = 0;
    }
    
 
    /**
     * @param {number[]} positionsToSelect
     */
    setSelected(positionsToSelect) {
        positionsToSelect = Array.isArray(positionsToSelect) ? positionsToSelect : [positionsToSelect];
        let x = this.element.options.length;
        for (let i=0; i<x; i++) {
            this.element.options[i].selected = positionsToSelect.includes(i);
        }
    }
    
    
}
