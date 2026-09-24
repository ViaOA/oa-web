import { OAClient, OAHtmlSelect } from '@oa-web/index.js';

export default class OATableComboBox extends OAHtmlSelect {
	constructor(element, id) {
	    super(element, id);

	    this.trigger = element.querySelector(
	        ':scope > [data-bs-toggle="dropdown"]'
	    );

	    this.trigger?.addEventListener('shown.bs.dropdown', () => {
	        requestAnimationFrame(() => {
	            this.scrollActiveRowIntoView();
	        });
	    });
	}
	
	scrollActiveRowIntoView() {
	    const tableElement = this.element.querySelector(
	        ':scope > .dropdown-menu > [data-oa-name="table"]'
	    );
	    if (!tableElement) return;

	    for (const component of OAClient.mapIdToComponent.values()) {
	        if (component.element !== tableElement) continue;

	        if (component.eleTBody && component.currentRow >= 0) {
	            component._scrollIntoView();
	        }
	        break;
	    }
	}
	
	hidePopup() {
	    const trigger = this.element.querySelector(
	        '[data-bs-toggle="dropdown"]'
	    );
	    if (!trigger) return;

	    bootstrap.Dropdown.getOrCreateInstance(trigger).hide();
	    trigger.focus();
	}
	
}
