import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { VendorListComponent } from './features/vendors/vendor-list/vendor-list.component';
import { VendorFormComponent } from './features/vendors/vendor-form/vendor-form.component';
import { AuthGuard } from './core/auth/guards/auth.guard';
import { RoleGuard } from './core/auth/guards/role.guard';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'vendors',
    component: VendorListComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'vendors/new',
    component: VendorFormComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] }
  },
  {
    path: '',
    redirectTo: 'vendors',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'vendors'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}