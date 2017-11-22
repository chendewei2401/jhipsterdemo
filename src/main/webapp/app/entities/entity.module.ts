import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { Demo1PayRecordModule } from './pay-record/pay-record.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        Demo1PayRecordModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Demo1EntityModule {}
