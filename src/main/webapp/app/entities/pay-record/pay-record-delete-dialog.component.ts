import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PayRecord } from './pay-record.model';
import { PayRecordPopupService } from './pay-record-popup.service';
import { PayRecordService } from './pay-record.service';

@Component({
    selector: 'jhi-pay-record-delete-dialog',
    templateUrl: './pay-record-delete-dialog.component.html'
})
export class PayRecordDeleteDialogComponent {

    payRecord: PayRecord;

    constructor(
        private payRecordService: PayRecordService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.payRecordService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'payRecordListModification',
                content: 'Deleted an payRecord'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pay-record-delete-popup',
    template: ''
})
export class PayRecordDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private payRecordPopupService: PayRecordPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.payRecordPopupService
                .open(PayRecordDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
