import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { PayRecord } from './pay-record.model';
import { PayRecordService } from './pay-record.service';

@Component({
    selector: 'jhi-pay-record-detail',
    templateUrl: './pay-record-detail.component.html'
})
export class PayRecordDetailComponent implements OnInit, OnDestroy {

    payRecord: PayRecord;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private payRecordService: PayRecordService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPayRecords();
    }

    load(id) {
        this.payRecordService.find(id).subscribe((payRecord) => {
            this.payRecord = payRecord;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPayRecords() {
        this.eventSubscriber = this.eventManager.subscribe(
            'payRecordListModification',
            (response) => this.load(this.payRecord.id)
        );
    }
}
