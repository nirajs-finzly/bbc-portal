export interface Invoice {
    invoiceId: string;
    unitConsumption: number;
    billDuration: string;
    billDueDate: string;
    currentAmountDue: number;
    totalAmountDue: number;
    paymentStatus: 'PAID' | 'UNPAID';
    generatedAt: Date;
    invoicePdf: string;
}
