

/**
 */
export default class OADialog {
    
    /** @type {HTMLElement} */
    element;
    /** @type {number} server side assigned seq Id */
    id;

    
    myModal;
    
    
    /**
     * @param {HTMLElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;
    }
    
    show() {
        if (!this.myModal) this.myModal = new bootstrap.Modal(this.element, 
            {
                
            }
        );
        this.myModal.show();
        
        // https://getbootstrap.com/docs/5.3/components/modal/ 
    }
    
    hide() {
        if (!this.myModal) return;
        this.myModal.hide();
        
    }
    
    //qqqqqqqqqqqq close    
    
}


