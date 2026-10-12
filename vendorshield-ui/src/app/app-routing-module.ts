import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VendorListComponent } from './features/vendors/vendor-list/vendor-list.component';
import { VendorFormComponent } from './features/vendors/vendor-form/vendor-form.component';

const routes: Routes = [
  { path: 'vendors', component: VendorListComponent },
  { path: 'vendors/new', component: VendorFormComponent },
  { path: '', redirectTo: 'vendors', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}