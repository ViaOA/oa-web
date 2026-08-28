
import { OAClient } from '@oa-web/index.js';

export default class OAInputDate {

    /** @type {HTMLElement} */
    element;
    /** @type {number} server side assigned seq Id */
    id;
    /** @type {string} */
    oldValue = '';


    /**
     * @param {HTMLElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;

        element.addEventListener('focus', () => {
            this.oldValue = element.value;
        });

        element.addEventListener('change', (event) => {
            let newValue = this.element.value;
            let obj = {
                id: this.id,
                type: 'change',
                oldValue: this.oldValue,
                newValue: newValue
            }
            OAClient.sendEventToServer(obj);
            this.oldValue = newValue;
        });
    }
}
