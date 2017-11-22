import { BaseEntity } from './../../shared';

export class PayRecord implements BaseEntity {
    constructor(
        public id?: number,
        public payOrderid?: string,
        public orderInfo?: string,
    ) {
    }
}
