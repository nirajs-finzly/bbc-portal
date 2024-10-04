import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from '../../types/customer';


@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private baseUrl = 'https://localhost:8080/api/customers'; 

  constructor(private http: HttpClient) {}

  // Fetch customers with pagination
  getCustomers(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}?page=${page}&size=${size}`);
  }

  // Fetch a single customer by ID
  getCustomerById(customerId: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.baseUrl}/${customerId}`);
  }

  // Create a new customer
  createCustomer(name: string,phone: string,email: string,address: string): Observable<Customer> {
    const customerRequest = {
        name: name,
        phone: phone,
        email: email,
        address: address
      };
    return this.http.post<Customer>(this.baseUrl, customerRequest);
  }

  // Update an existing customer
  updateCustomer(customer: Customer): Observable<Customer> {
    const updateRequest = {
        name: customer.name,
        phone: customer.phone,
        address: customer.address
      };
      return this.http.put<Customer>(`${this.baseUrl}/${customer.meterNo}`, updateRequest);
  }

  // Delete a customer by meter no
  deleteCustomer(meterNo: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${meterNo}`);
  }

  bulkUploadCustomer(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<any>(`${this.baseUrl}/upload`, formData);
  }
}
