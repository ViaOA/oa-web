
import { OAClient } from '@oa-web/index.js';


/**
 * Used to create a one time visible observer and send event to server, 
 * which will then create components.
 */
export default class OAVisibleObserverElement {
    /**
     * @param {HTMLElement} element
     * @param {number} id assigned by server
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;

        OAClient.createVisibleObserver(this.element, async () => {
            OAClient.sendEventToServer(
                {
                    id: this.id,
                    type: 'visible'
                }
            );
        });

    }
}
