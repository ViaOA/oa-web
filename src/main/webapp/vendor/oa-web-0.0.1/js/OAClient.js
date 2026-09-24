

// js sent by server, that is used by eval() and will need these classes.
import * as OA from './index.js'; // requires all the objects to use prefix "OA."

export const OAClient = {
	    
	mapIdToComponent: new Map(),  // Id from server : oaHtml*.js component
    mapDataNameToElement: new Map(),  // dataName.path : htmlElement
	mapElementToDataName: new Map(), // htmlElment : dataName.path
    sendingEventToServer: false,
	eventQueue: [],
	templateCounter: 0,
	  
    /**
     * @param {String} dataNamePath
     * @returns {HTMLElement} found element
     */
    getElement(dataNamePath) {
        let ele;
        

				
        // alternate using parent element.serverId + dataOAName
        if (arguments.length === 2 && (typeof arguments[0] === 'number' ) && (typeof arguments[1] === 'string')) {
            dataNamePath = arguments[1];
            
            let eleParent = this.getRegisteredComponent(arguments[0]).element;
            if (!eleParent) return ele;
			
			
console.log(`> getElement(${dataNamePath})`); //qqqqqqqqqqq
			
			
            if (dataNamePath.charAt(0) === '#' && dataNamePath.indexOf('.') < 0) {
                ele = document.getElementById(dataNamePath.substring(1)); 
            }
            else {
                const parts = dataNamePath.split('.');
                ele = eleParent;
                for (const part of parts) {                
                    ele = this._findChildWithDataOAName(ele, part);
                    if (!ele) break;
                }
                //was: ele = eleParent.querySelector(`:scope [data-oa-name="${dataNamePath}"]`);
            }
            if (!ele) {
                console.error(`ERROR: OAClient.getElement parent=${this.mapElementToDataName.get(eleParent)}, Id=${arguments[0]}, child name=${arguments[1]} not found`);
                throw new Error("component not found");
            }
			else {
				

//qqqqqqqqqqqqqqqqq TEST THIS qqqqqqqqqqqqqqqq will need to use weakrefs soon qqqqqqqqqqq				
								
//qqqqqqqqqqqqqqqqqq put in maps qqqqqqqqqqqqqqqqq
this.mapDataNameToElement.set(this.mapElementToDataName.get(eleParent) + '.' + dataNamePath, ele);
this.mapElementToDataName.set(ele, this.mapElementToDataName.get(eleParent) + '.' + dataNamePath);
				
			}            
            return ele;        
        }

		
		
        if (!dataNamePath) return;
        if (typeof dataNamePath !== 'string') return;

        ele = this.mapDataNameToElement.get(dataNamePath);
        if (ele) return ele;

        if (dataNamePath.charAt(0) === '#' && dataNamePath.indexOf('.') < 0) {
            ele = document.getElementById(dataNamePath.substring(1)); 
        }
        else {
            const parts = dataNamePath.split('.');
            ele = document.body;
            for (const part of parts) {                
                ele = this._findChildWithDataOAName(ele, part);
                if (!ele) break;
            }
        }
        if (ele) {
			this.mapDataNameToElement.set(dataNamePath, ele);
			this.mapElementToDataName.set(ele, dataNamePath);
		}
        else {
            console.error(`ERROR: OAClient.getElement name=${dataNamePath} not found`);
            throw new Error("component not found");
        }            
        return ele;
    },
    _findChildWithDataOAName(el, targetName) {
      for (const child of el.children) {
        const name = child.getAttribute('data-oa-name');
        if (name) {
          if (name === targetName) return child;
          continue;
        }
        const found = this._findChildWithDataOAName(child, targetName);
        if (found) return found;
      }
      return null;
    },    
    show(dataNamePath) {
        this.getElement(dataNamePath).classList.add('show');
    },
    hide(dataNamePath) {
        this.getElement(dataNamePath).classList.remove('show');
    },
	
	async createNewSessionOnServer() {
	    try {
	        const response = await fetch(`/jsp/oa-web-app.jsp`);

			if (!response.ok) {
	            throw new Error(`HTTP error! status: ${response.status}`);
	        }
	        let js = await response.text();
	        console.log("OAClient.getJavaScriptFromServer: response="+js);
			js = "(async () => { " + js + "})();";		
	        await eval(js);
	    } catch (error) {
	        console.error('ERROR: OAClient.createNewSessionOnServer', error);
	    }
	},	
	
    async getServerTemplate(url, templateName) {
        try {
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`reponse for getServerTemplate ${templateName} was not OK`);
            }
            const html = await response.text(); // Fetch the HTML as text
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const template = doc.getElementById(templateName); // Extract the specific template
			
			if (!template) return null;
			this.templateCounter++;
			return template.innerHTML.replaceAll('OAKEY-', 'OA' + this.templateCounter + '-');
        } catch (error) {
            console.error('Error fetching or parsing HTML:', error);
            return null;
        }
    },
    async loadTemplateFromServer(ele, url, templateName) {
        let html = await this.getServerTemplate(url, templateName);
        ele.innerHTML = html;
    },
    
    // called by return JS code from server.  'id' is assigned on server
    registerComponentFromServer(id, component) {
        this.mapIdToComponent.set(id, component);
    },
    getRegisteredComponent(id) {
        return this.mapIdToComponent.get(id);  
    },
    // called by return JS code from server
    updateElementsFromServer(jsonArray) {
        // [ { id: 123, changes: {} }, {..} ]
        for (let obj of jsonArray) {
            let comp = OAClient.getRegisteredComponent(obj.id);
            OAClient._updateElementFromServer(comp.element, obj.changes);
        }
    },
    _updateElementFromServer(element, jsonObject) {
        // updates Attributes and Styles for HtmlElement
        if (!element || !jsonObject) return;
        for (const [key, value] of Object.entries(jsonObject)) {
            if (key in element) {
                // Directly set standard properties (e.g., 'value', 'disabled', 'placeholder')
                element[key] = value;
            }
            else if (value === null || value === undefined) {
                element.removeAttribute(key);
            } else { 
                element.setAttribute(key, value);
            }
        }
    },
    async sendEventToServer(event) {
        if (this.sendingEventToServer) {
			this.eventQueue.push(event);
			return;
            // console.error('WARNING: OAClient.sendEventToServer ... sendingEventToServer is true');
        }
        this.sendingEventToServer = true;
        //qqqqqqqqqqqq
        console.log("OAClient.sendEventToServer: request="+JSON.stringify(event));

        try {
            const response = await fetch('/jsp/oa-web-event.jsp', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(event)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            let js = await response.text();
            console.log("OAClient.sendEventToServer: response="+js);
console.log("START eval --------------------------------------------------------");
			js = "(async () => { " + js + "})();";			
			await eval(js);
        } catch (error) {
            console.error('ERROR: OAClient.sendEventToServer', error);
        }
        finally {
            this.sendingEventToServer = false;
			
			if (this.eventQueue.length > 0) {
			    const next = this.eventQueue.shift();
			    this.sendEventToServer(next);
			}
        }
console.log("END eval ==========================================================");
    },
    createVisibleObserver: function(ele, callback) {
        let observer = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (!entry.isIntersecting) return;
                if (entry.target !== ele) return;
                callback();
                observer.unobserve(ele);
            });
        });
        observer.observe(ele);
    },
    escapeHTML(str) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#39;'
        };
        return str.replace(/[&<>"']/g, match => map[match]);
    },
    async getJsonObjectFromServer(url) {
        const response = await fetch(url);
        const json = await response.text();
        const obj = JSON.parse(json);
        return obj;
    },
	async addNotifyMessage(title, text, msgType = "Info") {
		const modal = document.getElementById("oaNotifyModal");
		const titleElement = modal?.querySelector("#oaNotifyTitleText");
		const messageElement = modal?.querySelector("#oaNotifyMessage");
		const icon = modal?.querySelector("#oaNotifyIcon");
		const header = modal?.querySelector(".modal-header");

		if (!modal || !titleElement || !messageElement || !icon || !header ||
		    !window.bootstrap?.Modal) {
		    console.error("OAClient.notify: Notify markup or Bootstrap is missing.");
		    return;
		}

		let color = "info";
		let iconClass = "bi-info-circle";
		let defaultTitle = "Information";

		if (msgType === "Warn") {
		    color = "warning";
		    iconClass = "bi-exclamation-triangle";
		    defaultTitle = "Warning";
		} else if (msgType === "Error") {
		    color = "danger";
		    iconClass = "bi-exclamation-circle";
		    defaultTitle = "Error";
		}

		titleElement.textContent = String(title ?? "").trim() || defaultTitle;
		messageElement.textContent = String(text ?? "");
		messageElement.style.whiteSpace = "pre-wrap";

		icon.classList.remove(
		    "bi-info-circle", "bi-exclamation-triangle", "bi-exclamation-circle",
		    "text-info-emphasis", "text-warning-emphasis", "text-danger-emphasis"
		);
		header.classList.remove(
		    "bg-info-subtle", "bg-warning-subtle", "bg-danger-subtle"
		);

		icon.classList.add(iconClass, `text-${color}-emphasis`);
		header.classList.add(`bg-${color}-subtle`);
		icon.setAttribute("aria-hidden", "true");
		modal.setAttribute("aria-describedby", "oaNotifyMessage");

		const instance = window.bootstrap.Modal.getOrCreateInstance(modal, {
		    backdrop: "static",
		    keyboard: true
		});

		return new Promise((resolve, reject) => {
		    const onHide = () => {
		        const focused = document.activeElement;

		        if (focused instanceof HTMLElement && modal.contains(focused)) {
		            focused.blur();
		        }
		    };

		    const onHidden = () => {
		        cleanup();

		        // Restore focus to the originating component here,
		        // if it is available, visible, and enabled.

		        resolve();
		    };

		    const cleanup = () => {
		        modal.removeEventListener("hide.bs.modal", onHide);
		        modal.removeEventListener("hidden.bs.modal", onHidden);
		    };

		    modal.addEventListener("hide.bs.modal", onHide);
		    modal.addEventListener("hidden.bs.modal", onHidden);

		    try {
		        instance.show();
		    } catch (error) {
		        cleanup();
		        reject(error);
		    }
		});
		
	},
	addToastMessage(title, text, msgType = "Info") {
		const container = document.getElementById("oaToastContainer");
		const Toast = window.bootstrap?.Toast;

		if (!container || !Toast) {
		    console.error("OAClient.addToastMessage: Toast container or Bootstrap is missing.");
		    return;
		}

		let color = "info";
		let iconClass = "bi-info-circle";
		let defaultTitle = "Information";

		if (msgType === "Warn") {
		    color = "warning";
		    iconClass = "bi-exclamation-triangle";
		    defaultTitle = "Warning";
		} else if (msgType === "Error") {
		    color = "danger";
		    iconClass = "bi-exclamation-circle";
		    defaultTitle = "Error";
		}

		const element = document.createElement("div");
		element.className = "toast";
		element.setAttribute("role", msgType === "Error" ? "alert" : "status");
		element.setAttribute("aria-live", msgType === "Error" ? "assertive" : "polite");
		element.setAttribute("aria-atomic", "true");

		const header = document.createElement("div");
		header.className = `toast-header bg-${color}-subtle`;

		const icon = document.createElement("i");
		icon.className = `bi ${iconClass} text-${color}-emphasis me-2`;
		icon.setAttribute("aria-hidden", "true");

		const titleElement = document.createElement("strong");
		titleElement.className = "me-auto";
		titleElement.textContent = String(title ?? "").trim() || defaultTitle;

		const closeButton = document.createElement("button");
		closeButton.type = "button";
		closeButton.className = "btn-close";
		closeButton.setAttribute("data-bs-dismiss", "toast");
		closeButton.setAttribute("aria-label", "Close");

		const body = document.createElement("div");
		body.className = "toast-body";
		body.style.whiteSpace = "pre-wrap";
		body.textContent = String(text ?? "");

		header.append(icon, titleElement, closeButton);
		element.append(header, body);
		container.appendChild(element);

		const toast = new Toast(element);

		element.addEventListener("hidden.bs.toast", () => {
		    toast.dispose();
		    element.remove();
		}, { once: true });

		toast.show();
	},
	async confirm(title, text, msgType = "Info") {
	    const element = document.getElementById("oaConfirmModal");
	    const titleElement = element?.querySelector("#oaConfirmTitleText");
	    const messageElement = element?.querySelector("#oaConfirmMessage");
	    const icon = element?.querySelector("#oaConfirmIcon");
	    const header = element?.querySelector(".modal-header");
	    const yesButton = element?.querySelector("#oaConfirmYes");
	    const noButton = element?.querySelector("#oaConfirmNo");
	    const Modal = window.bootstrap?.Modal;

	    if (!element || !titleElement || !messageElement || !icon ||
	        !header || !yesButton || !noButton || !Modal) {
	        console.error("OAClient.confirm: Confirm markup or Bootstrap is missing.");
	        return false;
	    }

	    if (this._confirmPending) {
	        console.warn("OAClient.confirm: another confirmation is already open.");
	        return false;
	    }

	    let color = "info";
	    let iconClass = "bi-info-circle";

	    if (msgType === "Warn") {
	        color = "warning";
	        iconClass = "bi-exclamation-triangle";
	    } else if (msgType === "Error") {
	        color = "danger";
	        iconClass = "bi-exclamation-circle";
	    }

	    titleElement.textContent = String(title ?? "").trim() || "Confirm";
	    messageElement.textContent = String(text ?? "");
	    messageElement.style.whiteSpace = "pre-wrap";

	    icon.classList.remove(
	        "bi-info-circle", "bi-exclamation-triangle", "bi-exclamation-circle",
	        "text-info-emphasis", "text-warning-emphasis", "text-danger-emphasis"
	    );
	    header.classList.remove(
	        "bg-info-subtle", "bg-warning-subtle", "bg-danger-subtle"
	    );

	    icon.classList.add(iconClass, `text-${color}-emphasis`);
	    header.classList.add(`bg-${color}-subtle`);
	    icon.setAttribute("aria-hidden", "true");
	    element.setAttribute("aria-describedby", "oaConfirmMessage");

	    const modal = Modal.getOrCreateInstance(element, {
	        backdrop: "static",
	        keyboard: true
	    });

	    const previousFocus = document.activeElement;
	    this._confirmPending = true;

	    return new Promise((resolve) => {
	        let proceed = false;
	        let ready = false;
	        let closing = false;

	        const onShown = () => {
	            ready = true;
	            noButton.focus();
	        };

	        const choose = (value) => {
	            if (!ready || closing) return;
	            closing = true;
	            proceed = value;
	            modal.hide();
	        };

	        const onYes = () => choose(true);
	        const onNo = () => choose(false);

	        const cleanup = () => {
	            yesButton.removeEventListener("click", onYes);
	            noButton.removeEventListener("click", onNo);
	            element.removeEventListener("shown.bs.modal", onShown);
	            element.removeEventListener("hidden.bs.modal", onHidden);
	            this._confirmPending = false;
	        };

	        const onHidden = () => {
	            cleanup();

	            if (previousFocus instanceof HTMLElement &&
	                previousFocus.isConnected) {
	                previousFocus.focus();
	            }

	            resolve(proceed);
	        };

	        yesButton.addEventListener("click", onYes);
	        noButton.addEventListener("click", onNo);
	        element.addEventListener("shown.bs.modal", onShown);
	        element.addEventListener("hidden.bs.modal", onHidden);

	        try {
	            modal.show();
	        } catch (error) {
	            cleanup();
	            console.error("OAClient.confirm: unable to show confirmation.", error);
	            resolve(false);
	        }
	    });
	}

	
}

/* qqqqqqqqqqqqqqqq

const script = document.createElement('script');
    script.textContent = event.data;
    document.body.appendChild(script);
    // Script executes as it's appended.

*/




















