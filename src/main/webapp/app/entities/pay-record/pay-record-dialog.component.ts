import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { PayRecord } from './pay-record.model';
import { PayRecordPopupService } from './pay-record-popup.service';
import { PayRecordService } from './pay-record.service';

@Component({
    selector: 'jhi-pay-record-dialog',
    templateUrl: './pay-record-dialog.component.html'
})
export class PayRecordDialogComponent implements OnInit {

    payRecord: PayRecord;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private payRecordService: PayRecordService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.payRecord.id !== undefined) {
            this.subscribeToSaveResponse(
                this.payRecordService.update(this.payRecord));
        } else {
            this.subscribeToSaveResponse(
                this.payRecordService.create(this.payRecord));
        }
    }

    private subscribeToSaveResponse(result: Observable<PayRecord>) {
        result.subscribe((res: PayRecord) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: PayRecord) {
        this.eventManager.broadcast({ name: 'payRecordListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-pay-record-popup',
    template: ''
})
export class PayRecordPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private payRecordPopupService: PayRecordPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.payRecordPopupService
                    .open(PayRecordDialogComponent as Component, params['id']);
            } else {
                this.payRecordPopupService
                    .open(PayRecordDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
