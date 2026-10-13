import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { ApiResponse } from '../../models/api-response.model';
import { AuthResponse, AuthUser, LoginRequest } from '../../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly authUrl = 'http://localhost:8080/api/v1/auth';
  private currentUserSubject: BehaviorSubject<AuthUser | null>;
  public currentUser$: Observable<AuthUser | null>;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    const savedUser = this.getStoredUser();
    this.currentUserSubject = new BehaviorSubject<AuthUser | null>(savedUser);
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): AuthUser | null {
    return this.currentUserSubject.value;
  }

  public get isAuthenticated(): boolean {
    const user = this.currentUserValue;
    const token = this.getToken();
    return !!(user && token);
  }

  public getToken(): string | null {
    return localStorage.getItem('access_token');
  }

  public login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<ApiResponse<AuthResponse>>(`${this.authUrl}/login`, credentials)
      .pipe(
        map((response) => {
          if (!response.data || !response.data.accessToken) {
            throw new Error(response.message || 'Authentication failed');
          }
          const authData = response.data;
          const user: AuthUser = {
            username: authData.username,
            role: authData.role,
            accessToken: authData.accessToken
          };

          localStorage.setItem('access_token', authData.accessToken);
          localStorage.setItem('current_user', JSON.stringify(user));
          this.currentUserSubject.next(user);

          return authData;
        })
      );
  }

  public logout(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('current_user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  public getRole(): string | null {
    return this.currentUserValue?.role ?? null;
  }

  public hasRole(roles: string | string[]): boolean {
    const currentRole = this.getRole();
    if (!currentRole) {
      return false;
    }

    const roleList = Array.isArray(roles) ? roles : [roles];
    const normalizedCurrent = currentRole.toUpperCase().replace(/^ROLE_/, '');

    return roleList.some((r) => {
      const normalizedTarget = r.toUpperCase().replace(/^ROLE_/, '');
      return normalizedCurrent === normalizedTarget;
    });
  }

  public isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  public isManager(): boolean {
    return this.hasRole('MANAGER');
  }

  public isViewer(): boolean {
    return this.hasRole('VIEWER');
  }

  public canManageVendors(): boolean {
    return this.hasRole(['ADMIN', 'MANAGER']);
  }

  public canDeleteVendors(): boolean {
    return this.hasRole('ADMIN');
  }

  private getStoredUser(): AuthUser | null {
    try {
      const userStr = localStorage.getItem('current_user');
      const token = localStorage.getItem('access_token');
      if (userStr && token) {
        const user = JSON.parse(userStr) as AuthUser;
        if (user.accessToken === token) {
          return user;
        }
      }
    } catch {
      localStorage.removeItem('current_user');
      localStorage.removeItem('access_token');
    }
    return null;
  }
}
