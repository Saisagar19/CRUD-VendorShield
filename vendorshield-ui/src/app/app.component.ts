import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './core/auth/services/auth.service';
import { AuthUser } from './core/models/auth.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  standalone: false
})
export class AppComponent implements OnInit {
  title = 'VendorShield';
  currentUser$: Observable<AuthUser | null>;

  constructor(public authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
  }

  ngOnInit(): void {}

  getRoleBadge(role: string | undefined): string {
    if (!role) return '';
    return role.replace(/^ROLE_/, '');
  }

  getRoleClass(role: string | undefined): string {
    if (!role) return '';
    const clean = role.replace(/^ROLE_/, '').toUpperCase();
    switch (clean) {
      case 'ADMIN':
        return 'role-badge--admin';
      case 'MANAGER':
        return 'role-badge--manager';
      case 'VIEWER':
        return 'role-badge--viewer';
      default:
        return 'role-badge--default';
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
