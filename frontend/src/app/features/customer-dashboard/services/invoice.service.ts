import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private apiUrl = 'http://localhost:8080/api/invoices'; // Backend URL

  constructor(private http: HttpClient) {}

  /**
   * Fetch invoices for a customer based on the meter number with pagination
   * @param meterNo The meter number of the customer
   * @param page The current page (defaults to 0)
   * @param size The number of records per page (defaults to 10)
   * @returns An observable of the invoice response
   */
  getInvoicesByMeterNo(meterNo: string, page: number = 0, size: number = 10): Observable<any> {
    // Set up query parameters for pagination
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    // Make a GET request to the backend
    return this.http.get<any>(`${this.apiUrl}/${meterNo}`, { params });
  }

  getLatestUnpaidInvoiceByMeterNo(meterNo: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${meterNo}/latest`);
  }
}
