

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

	    element.addEventListener("hide.bs.modal", () => {
	        const focused = document.activeElement;

	        if (focused instanceof HTMLElement &&
	            this.element.contains(focused)) {
	            focused.blur();
	        }
	    });
		
		element.addEventListener("click", (event) => {
		    const maximizeButton = event.target.closest("[data-oa-maximize]");
		
		    if (!maximizeButton || !this.element.contains(maximizeButton)) {
		        return;
		    }
		
		    const dialog = this.element.querySelector(".modal-dialog");
		
		    if (!dialog) {
		        return;
		    }
		
		    const maximized =
		        dialog.classList.toggle("modal-fullscreen");
		
		    maximizeButton.setAttribute(
		        "aria-pressed",
		        String(maximized)
		    );
		
		    maximizeButton.setAttribute(
		        "aria-label",
		        maximized ? "Restore dialog" : "Maximize dialog"
		    );
		
		    const icon = maximizeButton.querySelector("i");
		    if (icon) {
		        icon.classList.toggle(
		            "bi-arrows-fullscreen",
		            !maximized
		        );
		        icon.classList.toggle(
		            "bi-fullscreen-exit",
		            maximized
		        );
		    }
		
		    this.myModal?.handleUpdate();
		});
	}
	
    show() {
        if (!this.myModal) this.myModal = new bootstrap.Modal(this.element, 
            {
                
            }
        );
        this.myModal.show();
    }
    
    hide() {
        if (!this.myModal) return;
        this.myModal.hide();
    }
    
}


