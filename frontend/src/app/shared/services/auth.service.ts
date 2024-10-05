import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { User } from '../../types/user';
import { HotToastService } from '@ngxpert/hot-toast';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api';

  private authStateSubject: BehaviorSubject<boolean>;

  public authState$: Observable<boolean>;

  constructor(private http: HttpClient, private toast: HotToastService, private router: Router) {
    this.authStateSubject = new BehaviorSubject<boolean>(
      this.hasTokenAndRole()
    );
    this.authState$ = this.authStateSubject.asObservable();
  }

  sendOtp(identifier: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/send-otp`, { identifier });
  }

  login(identifier: string, otp: string): Observable<any> {
    return this.http
      .post(`${this.apiUrl}/auth/login`, { identifier, otp })
      .pipe(
        tap((response: any) => {
          if (response.success) {
            this.setToken(response.token);
            this.setRole(response.role);
            this.authStateSubject.next(true);
            this.toast.success('Login Successful!');

            // Fetch the user data after successful login
            this.getUser()
              .pipe(
                tap((res: any) => {
                  if (res.user) {
                    this.setUser(res.user);

                    const role = this.getRole(); // Fetch the role

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
              )
              .subscribe();
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
    localStorage.setItem('token', token);
  }

  private setRole(role: string): void {
    localStorage.setItem('role', role);
  }

  private setUser(user: User): void {
    const stringifiedUser = JSON.stringify(user);
    localStorage.setItem('user', stringifiedUser);
  }

  public getToken(): string | null {
    return localStorage.getItem('token');
  }

  public getUserData(): User | null {
    const userData = localStorage.getItem('user');
    return userData ? JSON.parse(userData) : null;
  }

  public getRole(): string | null {
    return localStorage.getItem('role');
  }

  private hasTokenAndRole(): boolean {
    return !!this.getToken() && !!this.getRole();
  }

  public isAuthenticated(): boolean {
    return this.hasTokenAndRole();
  }
}
