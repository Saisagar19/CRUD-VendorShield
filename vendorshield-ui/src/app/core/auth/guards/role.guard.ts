import { Injectable, inject } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
  UrlTree,
  CanActivateFn
} from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    _state: RouterStateSnapshot
  ): boolean | UrlTree {
    const expectedRoles = (route.data['roles'] as string[]) || [];

    if (expectedRoles.length === 0 || this.authService.hasRole(expectedRoles)) {
      return true;
    }

    this.snackBar.open(
      `Access Denied: Your role (${this.authService.getRole()}) does not have permission for this page.`,
      'Close',
      {
        duration: 4000,
        horizontalPosition: 'right',
        verticalPosition: 'top'
      }
    );

    return this.router.createUrlTree(['/vendors']);
  }
}

export const roleGuard: CanActivateFn = (route, _state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);

  const expectedRoles = (route.data['roles'] as string[]) || [];

  if (expectedRoles.length === 0 || authService.hasRole(expectedRoles)) {
    return true;
  }

  snackBar.open(
    `Access Denied: Your role (${authService.getRole()}) does not have permission for this page.`,
    'Close',
    {
      duration: 4000,
      horizontalPosition: 'right',
      verticalPosition: 'top'
    }
  );

  return router.createUrlTree(['/vendors']);
};
