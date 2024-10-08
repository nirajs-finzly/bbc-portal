import { User } from "./user";

export interface Customer {
    user: User;
    meterNo: String;
    address: String; 
}

export interface CreateCustomerRequest {
    name: string;
    phone: string;
    email: string;
    address: string;
}

export interface UpdateCustomerRequest {
    name: string;
    email: string,
    phone: string;
    address: string;
}
  
export interface CustomersResponse {
    message: string;
    customers: Customer[];
    success: boolean;
}

export interface MessageResponse {
    message: string;
    success: boolean;
  }

  export interface SingleCustomerResponse {
    message: string;
    customer: Customer; 
    success: boolean;
  }
  