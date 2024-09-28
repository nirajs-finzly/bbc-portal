import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { z, ZodError } from 'zod';

@Injectable()
export class ValidationInterceptor implements HttpInterceptor {
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // Add your specific logic for certain endpoints if necessary
        return next.handle(req).pipe(
            catchError((error) => {
                if (error instanceof ZodError) {
                    // Handle Zod validation errors
                    return throwError(() => new Error('Validation error: ' + error.message));
                }
                return throwError(() => error);
            })
        );
    }
}
