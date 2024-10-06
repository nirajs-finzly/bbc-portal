import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class InvoiceService {
  private apiUrl = 'http://localhost:8080/api/invoices'; // Backend URL

  constructor(private http: HttpClient) {}

  // Get total count of all invoices
  getAllInvoicesCount(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/statistics/all-invoices-count`);
  }

  // Get total count of all unpaid invoices
  getAllUnpaidInvoicesCount(): Observable<any> {
    return this.http.get<any>(
      `${this.apiUrl}/statistics/all-unpaid-invoices-count`
    );
  }

  // Get invoices status data
  getInvoiceStatusData(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/statistics/invoices-status-data`);
  }

  // Get total count of all invoices for a customer by meter number
  getAllCustomerInvoicesCount(meterNo: string): Observable<any> {
    return this.http.get<any>(
      `${this.apiUrl}/statistics/customer-invoices-count/${meterNo}`
    );
  }

  // Get total count of unpaid invoices for a customer by meter number
  getCustomerUnpaidInvoicesCount(meterNo: string): Observable<any> {
    return this.http.get<any>(
      `${this.apiUrl}/statistics/customer-unpaid-invoices-count/${meterNo}`
    );
  }

  // Get the last payment amount for a customer by meter number
  getLastPaymentAmount(meterNo: string): Observable<any> {
    return this.http.get<any>(
      `${this.apiUrl}/statistics/last-payment-amount/${meterNo}`
    );
  }

  // Get the average unit consumption for a customer by meter number
  getAverageUnitConsumptionOfCustomer(meterNo: string): Observable<any> {
    return this.http.get<any>(
      `${this.apiUrl}/statistics/average-unit-consumption/${meterNo}`
    );
  }

  // Get the average unit consumption for all customers
  getAverageUnitConsumption(): Observable<any> {
    return this.http.get<any>(
      `${this.apiUrl}/statistics/average-unit-consumption`
    );
  }
  
  // Get all invoices with pagination
  getAllInvoices(page: number = 0, size: number = 5): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}`, { params });
  }

  // Create a new invoice
  createInvoice(request: {
    meterNo: string;
    unitsConsumed: number;
    billDuration: string;
    billDueDate: string;
  }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}`, request);
  }

  // Bulk upload invoices from a file
  bulkUploadInvoice(
    dataFile: File,
    billDuration: string,
    billDueDate: string
  ): Observable<any> {
    const formData = new FormData();
    formData.append('file', dataFile);
    formData.append('billDuration', billDuration);
    formData.append('billDueDate', billDueDate);

    return this.http.post<any>(`${this.apiUrl}/upload`, formData);
  }

  // Get invoices by customer name with pagination
  getInvoicesByCustomerName(
    customerName: string,
    page: number = 0,
    size: number = 5
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/customer/${customerName}`, {
      params,
    });
  }

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

  viewInvoicePDF(pdfData: string, invoiceId: string): void {
    const dataUri = `data:application/pdf;base64,${pdfData}`;
    const blob = this.dataURItoBlob(dataUri);
    const url = window.URL.createObjectURL(blob);

    // Open the PDF in a new tab
    window.open(url);

    // Optionally, revoke the object URL after a certain time to free up memory
    setTimeout(() => {
      window.URL.revokeObjectURL(url);
    }, 100);
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
