import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { Loan } from './model/Loan';
import { LoanPage } from './model/LoanPage';
import { LOAN_DATA } from './model/mock-loans';
import { HttpClient } from '@angular/common/http';
import { LoanSearchDto } from './model/LoanSearchDto';
import { FormattedLoan } from './model/FormattedLoan';

@Injectable({
    providedIn: 'root'
})
export class LoanService {
    
    url: string = 'http://localhost:8080/loan';
    
    constructor(
        private http: HttpClient
    ) { }

    
    getLoans(searchDto: LoanSearchDto): Observable<LoanPage> {
        return this.http.post<LoanPage>(this.url, searchDto);
    }

    saveLoan(loan: FormattedLoan): Observable<any> {
        return this.http.put(this.url, loan);
      }

    deleteLoan(idLoan : number): Observable<void> {
        return this.http.delete<void>(this.url+ '/'+ idLoan);
    }
    
    private composeFindUrl(gameTitle?: String, clientId?: number, date?: Date) : string {
        let params = '';

        if (gameTitle != null) {
            params += 'game='+gameTitle;
        }

        if (clientId != null) {
            if (params != '') params += "&";
            params += "client="+clientId;
        }

        if (date != null) {
            if (params != '') params += "&";
            params += "date="+date;
        }


        if (params == '') return this.url;
        else return this.url + '?'+params;
    }
}