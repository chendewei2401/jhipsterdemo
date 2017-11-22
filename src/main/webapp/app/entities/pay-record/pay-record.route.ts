import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PayRecordComponent } from './pay-record.component';
import { PayRecordDetailComponent } from './pay-record-detail.component';
import { PayRecordPopupComponent } from './pay-record-dialog.component';
import { PayRecordDeletePopupComponent } from './pay-record-delete-dialog.component';

@Injectable()
export class PayRecordResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const payRecordRoute: Routes = [
    {
        path: 'pay-record',
        component: PayRecordComponent,
        resolve: {
            'pagingParams': PayRecordResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'demo1App.payRecord.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pay-record/:id',
        component: PayRecordDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'demo1App.payRecord.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const payRecordPopupRoute: Routes = [
    {
        path: 'pay-record-new',
        component: PayRecordPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'demo1App.payRecord.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pay-record/:id/edit',
        component: PayRecordPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'demo1App.payRecord.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pay-record/:id/delete',
        component: PayRecordDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'demo1App.payRecord.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
