export interface Invoice {
    invoiceId: string;
    unitConsumption: number;
    billDuration: string;
    billDueDate: string;
    amountDue: number;
    paymentStatus: 'PAID' | 'UNPAID';
    generatedAt: Date
}
