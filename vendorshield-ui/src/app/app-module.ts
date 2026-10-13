import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing-module';
import { AppComponent } from './app.component';
import { MaterialModule } from './shared/material.module';

import { VendorListComponent } from './features/vendors/vendor-list/vendor-list.component';
import { VendorDialogComponent } from './features/vendors/vendor-dialog/vendor-dialog.component';
import { VendorFormComponent } from './features/vendors/vendor-form/vendor-form.component';
import { LoginComponent } from './features/auth/login/login.component';
import { AuthInterceptor } from './core/auth/interceptors/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    VendorListComponent,
    VendorDialogComponent,
    VendorFormComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    MaterialModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}