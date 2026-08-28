
import { OAClient } from '@oa-web/index.js';

export default class OAInputButton {

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
        
        element.addEventListener('click', (event) => {
            let obj = {
                id: this.id,
                type: 'click',
            }
            OAClient.sendEventToServer(obj);
        });
    }
}
