import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing-module';
import { AppComponent } from './app.component';
import { MaterialModule } from './shared/material.module';

import { VendorListComponent } from './features/vendors/vendor-list/vendor-list.component';
import { VendorDialogComponent } from './features/vendors/vendor-dialog/vendor-dialog.component';
import { VendorFormComponent } from './features/vendors/vendor-form/vendor-form.component';

@NgModule({
  declarations: [
    AppComponent,
    VendorListComponent,
    VendorDialogComponent,
    VendorFormComponent
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
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}