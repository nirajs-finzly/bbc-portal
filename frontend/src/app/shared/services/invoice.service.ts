import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class InvoiceService {
  private apiUrl = 'http://localhost:8080/api/invoices'; // Backend URL

  constructor(private http: HttpClient) {}

  getInvoicesByMeterNo(
    meterNo: string,
    page: number = 0,
    size: number = 10
  ): Observable<any> {
    // Set up query parameters for pagination
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    // Make a GET request to the backend
    return this.http.get<any>(`${this.apiUrl}/${meterNo}`, { params });
  }

  getInvoiceByCustomerBillDuration(
    meterNo: string,
    billDuration: string,
    page: number = 0,
    size: number = 10
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/${meterNo}/${billDuration}`, {
      params,
    });
  }

  getLatestUnpaidInvoiceByMeterNo(meterNo: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${meterNo}/latest`);
  }

  downloadInvoicePDF(pdfData: string, invoiceId: string): void {
    const dataUri = `data:application/pdf;base64,${pdfData}`;
    const blob = this.dataURItoBlob(dataUri);
    const url = window.URL.createObjectURL(blob);

    const a = document.createElement('a');
    a.href = url;
    a.download = `Invoice_${invoiceId}.pdf`;
    a.click();

    window.URL.revokeObjectURL(url);
  }

  // Helper method to convert base64 to Blob
  dataURItoBlob(dataURI: string): Blob {
    const byteString = atob(dataURI.split(',')[1]);
    const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);

    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }

    return new Blob([ab], { type: mimeString });
  }
}
