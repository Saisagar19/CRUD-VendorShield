import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = localStorage.getItem('access_token');

    // Skip Authorization header for auth endpoints (e.g. login)
    const isAuthEndpoint = request.url.includes('/api/v1/auth/');

    if (token && !isAuthEndpoint) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 && !isAuthEndpoint) {
          localStorage.removeItem('access_token');
          localStorage.removeItem('current_user');
          this.snackBar.open('Session expired or unauthorized. Please log in again.', 'Close', {
            duration: 4000,
            horizontalPosition: 'right',
            verticalPosition: 'top'
          });
          this.router.navigate(['/login']);
        } else if (error.status === 403) {
          this.snackBar.open('Access denied: You do not have permission for this action.', 'Close', {
            duration: 4000,
            horizontalPosition: 'right',
            verticalPosition: 'top',
            panelClass: ['warning-snackbar']
          });
        }
        return throwError(() => error);
      })
    );
  }
}