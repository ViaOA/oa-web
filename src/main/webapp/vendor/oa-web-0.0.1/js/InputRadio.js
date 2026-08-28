
import { OAClient } from '@oa-web/index.js';

export default class InputRadio {

    /** @type {HTMLElement} */
    element;
    /** @type {number} */
    id;
    
    /**
     * @param {HTMLElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;
        
        // NOTE:  for radio buttons, an event is only sent when checked=true 
        
        element.addEventListener('change', (event) => {
            let obj = {
                id: this.id,
                type: 'change'  // only sent if checked=true
            }
            OAClient.sendEventToServer(obj);
        });

    }

}
