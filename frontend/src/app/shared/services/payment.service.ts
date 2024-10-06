import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  private apiUrl = 'http://localhost:8080/api/payment'; // Backend URL

  constructor(private http: HttpClient) {}

  getAllTransactions(page: number = 0, size: number = 10): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/transactions`, { params });
  }

  getAllTransactionsByMeterNo(
    meterNo: string,
    page: number = 0,
    size: number = 10
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/transactions/${meterNo}`, {
      params,
    });
  }

  initiatePayment(meterNo: string, invoiceId: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/initiate`, {
      meterNo,
      invoiceId,
    });
  }

  validatePaymentInitiation(invoiceId: string): Observable<any> {
    return this.http.get<any>(
      `${this.apiUrl}/validate-initiation/${invoiceId}`
    );
  }

  makePayment(
    meterNo: string,
    invoiceId: string,
    paymentMethod: string,
    paymentDetails: string
  ): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/make-payment`, {
      meterNo,
      invoiceId,
      paymentMethod,
      paymentDetails
    });
  }

  confirmPayment(
    meterNo: string,
    invoiceId: string,
    paymentMethod: string,
    paymentDetails: string,
    otp: string,
    amount: string
  ): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/confirm`, {
      meterNo,
      invoiceId,
      paymentMethod,
      paymentDetails,
      otp,
      amount
    });
  }
}
