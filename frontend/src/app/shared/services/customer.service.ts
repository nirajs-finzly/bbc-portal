import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateCustomerRequest, CustomersResponse, MessageResponse, SingleCustomerResponse, UpdateCustomerRequest } from '../../types/customer';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/api/customers'; // Backend URL

  constructor(private http: HttpClient) {}

  // Get all customers with pagination
  getAllCustomers(page: number = 0, size: number = 5): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}?page=${page}&size=${size}`);
  }

  
  // Create a customer
  createCustomer(request: CreateCustomerRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(this.apiUrl, request);
  }

  // Update an existing customer
  updateCustomer(meterNo: string, request: UpdateCustomerRequest): Observable<MessageResponse> {
    return this.http.put<MessageResponse>(`${this.apiUrl}/${meterNo}`, request);
  }

  // Delete a customer by meter number
  deleteCustomer(meterNo: string): Observable<MessageResponse> {
    return this.http.delete<MessageResponse>(`${this.apiUrl}/${meterNo}`);
  }

  // Bulk upload customers from a file
  bulkUploadCustomer(file: File): Observable<MessageResponse> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    
    return this.http.post<MessageResponse>(`${this.apiUrl}/upload`, formData);
  }

  // Get customers by meter no with pagination
  getCustomerByMeterNo(meterNo: string): Observable<SingleCustomerResponse> {
    return this.http.get<SingleCustomerResponse>(`${this.apiUrl}/${meterNo}`);
  }

  
}
