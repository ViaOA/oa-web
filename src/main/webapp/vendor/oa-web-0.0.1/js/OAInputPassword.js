
import { OAClient } from '@oa-web/index.js';

export default class OAInputPassword {

    /** @type {HTMLElement} */
    element;
    /** @type {number} server side assigned seq Id */
    id;
    /** @type {string} */
    oldValue = '';
    /** @type {number} allows size and maxSize to give a min and max width (in chars) that adjusts based on value */
    maxSize;
    /** @type {number} original size */
    holdSize;


    /**
     * @param {HTMLElement} element
     * @param {number} id
     */
    constructor(element, id) {
        this.element = element;
        this.id = id;

        element.addEventListener('focus', () => {
            this.oldValue = element.value;
        });

        element.addEventListener('change', (event) => {
            let newValue = this.element.value;
            let obj = {
                id: this.id,
                type: 'change',
                oldValue: this.oldValue,
                newValue: newValue
            }
            OAClient.sendEventToServer(obj);
            this.oldValue = newValue;
        });

        this.element.addEventListener('input', this.adjustSize);
        this.adjustSize();
    }

    adjustSize = () => {
        if (!this.maxSize) return;

        if (!this.holdSize) this.holdSize = this.element.size;

        const valueLength = this.element.value.length;

        const newSize = Math.min(this.maxSize, Math.max(this.holdSize, valueLength + 1));
        this.element.size = newSize;
        this.element.style.width = 'auto'; // 'auto' to allow the size attribute to take control
    }

    setMaxSize(x = 0) {
        if (x !== this.maxSize) {
            this.maxSize = x;
            this.adjustSize();
        }
    }
}
