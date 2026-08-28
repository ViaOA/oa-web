
import { OAClient } from '@oa-web/index.js';

/**
 */
export default class OATabPanel {
    /** @type {HTMLElement} */
    element;
    /** @type {number} server side assigned seq Id */
    id;
	
	eleTabs;
	cmdScrollLeft;
	cmdScrollRight;
	SCROLL_AMOUNT = 100; // px per click    
    
    /** @type {Tab[]} */    
    tabs = [];
    
    /** @type {Tab} */
    activeTab;
    
    /**
     * @param {HTMLElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;
		this.setupTabScrolling();
    }
    

	setupTabScrolling() {
		this.eleTabs = this.element.querySelector(`:scope ul[data-oa-name="tabs"]`);
		this.cmdScrollLeft = this.element.querySelector(`:scope button[data-oa-name="cmdScrollLeft"]`);
		this.cmdScrollRight = this.element.querySelector(`:scope button[data-oa-name="cmdScrollRight"]`);
		if (!this.eleTabs || !this.cmdScrollLeft || !this.cmdScrollRight) return;		

		this.eleTabs.addEventListener('scroll', () => this.updateTabScrolling());
		this.cmdScrollLeft.addEventListener('click',  () => this.scrollByAmount(-this.SCROLL_AMOUNT));
		this.cmdScrollRight.addEventListener('click', () => this.scrollByAmount(+this.SCROLL_AMOUNT));
		
		const ro = new ResizeObserver(() => this.updateTabScrolling());
		ro.observe(this.eleTabs);		
		
		this.updateTabScrolling();
	}
	
	scrollByAmount(px) {
	    this.eleTabs.scrollBy({ left: px, behavior: 'smooth' });
	}
	
	updateTabScrolling() {
		if (!this.eleTabs || !this.cmdScrollLeft || !this.cmdScrollRight) return;		
		if (this.eleTabs.scrollWidth < (this.eleTabs.clientWidth+1)) {
			this.cmdScrollLeft.style.display  = 'none';			
			this.cmdScrollRight.style.display  = 'none';
		}
		else {
			this.cmdScrollLeft.style.display  = 'inline-block';			
			this.cmdScrollRight.style.display  = 'inline-block';
			
			this.cmdScrollLeft.disabled = (this.eleTabs.scrollLeft < 1);
			this.cmdScrollRight.disabled = (this.eleTabs.scrollLeft + this.eleTabs.clientWidth) >= (this.eleTabs.scrollWidth-1);
		}
	}
	
	
    close() {
        for (let tab of this.tabs) {
            tab.close();
        }
    }
    
    addTab(name) {
        let button = this.element.querySelector(`:scope ul[data-oa-name="tabs"]>li[data-oa-name="${name}"]>button`);
        let s = `:scope div[data-oa-name="tabContent"]>div[data-oa-name="${name}"]`;
        let content = this.element.querySelector(s);
        if (!content) console.error(`OATabPanel addTab could not find: ${s}`);            
        
        
        
        /** @type {Tab} */
        let tab = new Tab(name, button, content, this.tabs.length);
        tab.onTabClicked = (pos) => {
            this.setActiveTab(pos);
        };
        tab.hide();
        this.tabs.push(tab);
        if (this.tabs.length == 1) {
            this.setActiveTab(0, false); // default is 0
        }
    }
	
    setActiveTab(pos, bSendServerEvent=true) {
        if (this.activeTab) this.activeTab.hide();
        else {
            for (let t of this.tabs) {
                t.hide();
            }
        }
        this.activeTab = this.tabs[pos];
        this.activeTab.show();
        
        if (bSendServerEvent) {
            let obj = {
                id: this.id,
                type: 'change',
                newValue: pos
            }
            OAClient.sendEventToServer(obj);
        }
		
		const rParent = this.eleTabs.getBoundingClientRect();
		const rChild = this.activeTab.button.getBoundingClientRect();
		
		let x = rChild.left - rParent.left;
		if (x < 0) this.scrollByAmount(x);
		else {
			x = rChild.right - rParent.right;
			if (x > 0) {
				this.scrollByAmount(x);
			}
		}

		this.updateTabScrolling();
    }
}

class Tab {
    /** @type {string} */
    name;
    /** @type {number} */
    pos;
    
    /** @type {HTMLElement} */
    button;
    /** @type {HTMLElement} */
    content;
    
    constructor(name, button, content, pos) {
        this.name = name;
        this.pos = pos;
        this.button = button;
        this.content = content;
        this.button.addEventListener("click", this._handleOnClickEvent);
    }
    
    close() {
        this.button.removeEventListener("click", this._handleOnClickEvent);
    }
    
    _handleOnClickEvent = () => {
        this.onTabClicked(this.pos);
    }

    onTabClicked(pos) {
    }
    
    show() {
        this.button.classList.add("active");
        this.content.classList.add("active", "show");
    }
    hide() {
        this.button.classList.remove("active");
        this.content.classList.remove("active", "show");
    }
}

