
import { OAClient } from '@oa-web/index.js';

export default class OAHtmlButton {

    /** @type {HTMLElement} */
    element;
    /** @type {number} */
    id;
    
	/** @type {string} */
	confirmMessage = "";

		
    /**
     * @param {HTMLElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;
        
        element.addEventListener('click', async (event) => {
			event.preventDefault();
			this.element.disabled = true;

			if (this.confirmMessage.trim()) {			
				const proceed = await OAClient.confirm(
				    this.element.title || "Confirm",
				    this.confirmMessage,
				    "Info"
				);
				if (!proceed) {
					this.element.disabled = false;
					return;
				}			
			}
			
            let obj = {
                id: this.id,
                type: 'click',
            }
            OAClient.sendEventToServer(obj);
        });

    }

}

