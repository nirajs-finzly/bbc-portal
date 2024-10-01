import { Component } from '@angular/core';
import { Invoice } from '../../../../types/invoice';
import { User } from '../../../../types/user';
import { InvoiceService } from '../../services/invoice.service';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
    selector: 'app-invoices-table',
    templateUrl: './invoices-table.component.html',
    styleUrl: './invoices-table.component.css',
})
export class InvoicesTableComponent {
    invoices: Invoice[] = []; // Initialize invoices as an empty array
    totalInvoices: number = 0;
    pageSize: number = 10;
    currentPage: number = 1;
    user: User | null = null;

    constructor(
        private invoiceService: InvoiceService,
        private authService: AuthService
    ) {}

    ngOnInit(): void {
        this.user = this.authService.getUserData();

        if (this.user) {
            this.getInvoices(this.currentPage, this.pageSize);
        } else {
            console.error('User not found');
        }
    }

    // Fetch invoices with server-side pagination
    getInvoices(page: number, size: number): void {
        if (this.user && this.user.meterNo) {
            this.invoiceService
                .getInvoicesByMeterNo(this.user.meterNo, page - 1, size)
                .subscribe((response: any) => {
                    this.invoices = response.invoices || [];
                    this.totalInvoices = response.totalInvoices || 0;

                    this.sortInvoicesByGeneratedAt();
                });
        } else {
            console.error('Meter number not available for user');
        }
    }

    downloadInvoicePdf(pdfData: string, invoiceId: string): void{
        this.invoiceService.downloadInvoicePDF(pdfData, invoiceId);
    }

    sortInvoicesByGeneratedAt(): void {
        this.invoices.sort((a: Invoice, b: Invoice) => {
            const dateA = new Date(a.generatedAt).getTime();
            const dateB = new Date(b.generatedAt).getTime();
            return dateB - dateA;
        });
    }

    // Method to calculate total pages based on totalInvoices and pageSize
    getTotalPages(): number {
        return Math.ceil(this.totalInvoices / this.pageSize);
    }

    // Method to generate page numbers for the pagination component
    getPageNumbers(): number[] {
        const totalPages = this.getTotalPages();
        const pageNumbers: number[] = [];

        for (let i = 1; i <= totalPages; i++) {
            pageNumbers.push(i);
        }

        return pageNumbers;
    }

    // Method to handle page change
    onPageChange(page: number): void {
        this.currentPage = page;
        this.getInvoices(this.currentPage, this.pageSize);
    }
}
