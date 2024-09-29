import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { CryptoService } from '../../../shared/services/crypto.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { Router } from '@angular/router';
import { User } from '../../../types/user';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private apiUrl = 'http://localhost:8080/api';

    private authStateSubject: BehaviorSubject<boolean>;

    public authState$: Observable<boolean>;

    constructor(
        private http: HttpClient,
        private cryptoService: CryptoService,
        private toast: HotToastService,
        private router: Router
    ) {
        this.authStateSubject = new BehaviorSubject<boolean>(this.hasTokenAndRole());
        this.authState$ = this.authStateSubject.asObservable();
    }

    sendOtp(identifier: string): Observable<any> {
        return this.http.post(`${this.apiUrl}/auth/send-otp`, { identifier });
    }

    login(identifier: string, otp: string): Observable<any> {
        return this.http.post(`${this.apiUrl}/auth/login`, { identifier, otp }).pipe(
            tap((response: any) => {
                if (response.success) {
                    this.setToken(response.token);
                    this.setRole(response.role);
                    this.authStateSubject.next(true);
    
                    // Fetch the user data after successful login
                    this.getUser().pipe(
                        tap((res: any) => {
                            if (res.user) {
                                this.setUser(res.user);
                                
                                const role = this.getRole(); // Fetch the role
                                this.toast.success('Login Successful!');
                                
                                if (role === 'EMPLOYEE') {
                                    this.router.navigate(['/dashboard/o']);
                                } else if (role === 'CUSTOMER') {
                                    this.router.navigate(['/dashboard/u']);
                                }
                            }
                        }),
                        catchError((error) => {
                            console.error('Error fetching user details:', error);
                            return of(null);
                        })
                    ).subscribe();
                }
            })
        );
    }
    

    // Fetch the current user from backend
    getUser(): Observable<User | null> {
        return this.http.get<User>(`${this.apiUrl}/user`).pipe(
            catchError((error) => {
                console.error('Error fetching user:', error);
                return of(null); // Return null if there's an error
            })
        );
    }

    logout(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        localStorage.removeItem('user');
        this.authStateSubject.next(false);
    }

    private setToken(token: string): void {
        const encryptedToken = this.cryptoService.encrypt(token);
        localStorage.setItem('token', encryptedToken);
    }

    private setRole(role: string): void {
        const encryptedRole = this.cryptoService.encrypt(role);
        localStorage.setItem('role', encryptedRole);
    }
    
    private setUser(user: User): void {
        const stringifiedUser = JSON.stringify(user);
        const encryptedUser = this.cryptoService.encrypt(stringifiedUser);
        localStorage.setItem('user', encryptedUser);
    }

    public getToken(): string | null {
        const encryptedToken = localStorage.getItem('token');
        if (encryptedToken) {
            return this.cryptoService.decrypt(encryptedToken);
        }
        return null;
    }


    public getUserData(): User | null {
        const encryptedUser = localStorage.getItem('user');
        if (encryptedUser) {
             const stringifiedUser = this.cryptoService.decrypt(encryptedUser);
             return JSON.parse(stringifiedUser);
        }
        return null;
    }

    public getRole(): string | null {
        const encryptedRole = localStorage.getItem('role');
        if (encryptedRole) {
            return this.cryptoService.decrypt(encryptedRole);
        }
        return null;
    }

    private hasTokenAndRole(): boolean {
        return !!this.getToken() && !!this.getRole();
    }

    public isAuthenticated(): boolean {
        return this.hasTokenAndRole();
    }
}