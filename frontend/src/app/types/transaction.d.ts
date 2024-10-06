export interface Transaction {
    transactionId: string;
    customerName: string;
    invoiceId: string;
    amount: number;
    paymentMethod: string;
    paymentIdentifier: string;
    transactionStatus: 'SUCCESS' | 'FAILED' | 'PENDING';
    transactionDate: Date;
    cardType?: string;
}
