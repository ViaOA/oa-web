

/**
 */
export default class OAPanel {
    
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
    }
       
}


