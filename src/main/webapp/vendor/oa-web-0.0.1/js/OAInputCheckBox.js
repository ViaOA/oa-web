
import { OAClient } from '@oa-web/index.js';

export default class OAInputCheckBox {

    /** @type {HTMLElement} */
    element;
    /** @type {number} */
    id;
    /** @type {boolean} */
    oldChecked = '';
    
    /**
     * @param {HTMLElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;
    
        element.addEventListener('focus', () => {
            this.oldChecked = element.checked;
        });
            
        element.addEventListener('change', (event) => {
            let newChecked = this.element.checked;
            let obj = {
                id: this.id,
                type: 'change',
                oldValue: this.oldChecked,
                newValue: newChecked
            }
            OAClient.sendEventToServer(obj);
            this.oldChecked = newChecked;
        });
    }
}
