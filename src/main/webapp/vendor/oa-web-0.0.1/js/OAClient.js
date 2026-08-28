

// js sent by server, that is used by eval() and will need these classes.
import * as OA from './index.js'; // requires all the objects to use prefix "OA."

export const OAClient = {
    mapIdToComponent: new Map(),  // Id from server : oaHtml*.js component
    mapDataNameToElement: new Map(),  // dataName.path : htmlElement
	mapElementToDataName: new Map(), // htmlElment : dataName.path
    sendingEventToServer: false,
	eventQueue: [],
        
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
            return template ? template.innerHTML : null; // Return template content
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
    }
}

/* qqqqqqqqqqqqqqqq

const script = document.createElement('script');
    script.textContent = event.data;
    document.body.appendChild(script);
    // Script executes as it's appended.

*/

