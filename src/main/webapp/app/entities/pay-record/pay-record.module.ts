import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Demo1SharedModule } from '../../shared';
import {
    PayRecordService,
    PayRecordPopupService,
    PayRecordComponent,
    PayRecordDetailComponent,
    PayRecordDialogComponent,
    PayRecordPopupComponent,
    PayRecordDeletePopupComponent,
    PayRecordDeleteDialogComponent,
    payRecordRoute,
    payRecordPopupRoute,
    PayRecordResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...payRecordRoute,
    ...payRecordPopupRoute,
];

@NgModule({
    imports: [
        Demo1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PayRecordComponent,
        PayRecordDetailComponent,
        PayRecordDialogComponent,
        PayRecordDeleteDialogComponent,
        PayRecordPopupComponent,
        PayRecordDeletePopupComponent,
    ],
    entryComponents: [
        PayRecordComponent,
        PayRecordDialogComponent,
        PayRecordPopupComponent,
        PayRecordDeleteDialogComponent,
        PayRecordDeletePopupComponent,
    ],
    providers: [
        PayRecordService,
        PayRecordPopupService,
        PayRecordResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Demo1PayRecordModule {}
