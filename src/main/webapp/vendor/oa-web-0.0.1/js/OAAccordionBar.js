
import { OAClient } from '@oa-web/index.js';

/**
 */
export default class OAAccordionBar {

    /** @type {HTMLElement} */
    element;
    /** @type {number} server side assigned seq Id */
    id;


    /**
     * @param {HTMLElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;

        element.addEventListener('show.bs.collapse', (event) => {
            if (event.target !== this.element) return;
            OAClient.sendEventToServer(
                {
                    id: this.id,
                    type: 'show'
                }
            );
        });
        element.addEventListener('hide.bs.collapse', (event) => {
            if (event.target !== this.element) return;
            OAClient.sendEventToServer(
                {
                    id: this.id,
                    type: 'hide'
                }
            );
        });

    }
}


