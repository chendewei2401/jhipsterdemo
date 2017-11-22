import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { PayRecord } from './pay-record.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PayRecordService {

    private resourceUrl = SERVER_API_URL + 'api/pay-records';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/pay-records';

    constructor(private http: Http) { }

    create(payRecord: PayRecord): Observable<PayRecord> {
        const copy = this.convert(payRecord);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(payRecord: PayRecord): Observable<PayRecord> {
        const copy = this.convert(payRecord);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<PayRecord> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to PayRecord.
     */
    private convertItemFromServer(json: any): PayRecord {
        const entity: PayRecord = Object.assign(new PayRecord(), json);
        return entity;
    }

    /**
     * Convert a PayRecord to a JSON which can be sent to the server.
     */
    private convert(payRecord: PayRecord): PayRecord {
        const copy: PayRecord = Object.assign({}, payRecord);
        return copy;
    }
}
