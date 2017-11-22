/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { Demo1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PayRecordDetailComponent } from '../../../../../../main/webapp/app/entities/pay-record/pay-record-detail.component';
import { PayRecordService } from '../../../../../../main/webapp/app/entities/pay-record/pay-record.service';
import { PayRecord } from '../../../../../../main/webapp/app/entities/pay-record/pay-record.model';

describe('Component Tests', () => {

    describe('PayRecord Management Detail Component', () => {
        let comp: PayRecordDetailComponent;
        let fixture: ComponentFixture<PayRecordDetailComponent>;
        let service: PayRecordService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [Demo1TestModule],
                declarations: [PayRecordDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PayRecordService,
                    JhiEventManager
                ]
            }).overrideTemplate(PayRecordDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PayRecordDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PayRecordService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PayRecord(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.payRecord).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
