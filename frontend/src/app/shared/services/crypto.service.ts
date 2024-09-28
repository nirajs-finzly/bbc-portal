import { Injectable } from '@angular/core';
import * as CryptoJS from 'crypto-js';

@Injectable({
    providedIn: 'root',
})
export class CryptoService {
    private secretKey = 'your-secret-key';

    constructor() {}

    encrypt(T: string): string {
        return CryptoJS.AES.encrypt(T, this.secretKey).toString();
    }

    decrypt(T: string): string {
        const bytes = CryptoJS.AES.decrypt(T, this.secretKey);
        return bytes.toString(CryptoJS.enc.Utf8);
    }
}
