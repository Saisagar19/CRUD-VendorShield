import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Vendor } from '../../../core/models/vendor.model';

@Component({
  selector: 'app-vendor-dialog',
  templateUrl: './vendor-dialog.component.html',
  styleUrls: ['./vendor-dialog.component.scss'],
  standalone: false
})
export class VendorDialogComponent implements OnInit {
  vendorForm!: FormGroup;
  isEditMode = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<VendorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Vendor | null
  ) {}

  ngOnInit(): void {
    this.isEditMode = !!this.data;

    this.vendorForm = this.fb.group({
      name: [this.data?.name || '', [Validators.required, Validators.maxLength(150)]],
      taxIdentifier: [this.data?.taxIdentifier || '', [Validators.required, Validators.maxLength(50)]],
      contactEmail: [this.data?.contactEmail || '', [Validators.required, Validators.email]],
      tier: [this.data?.tier || 'TIER_3', Validators.required],
      status: [this.data?.status || 'UNDER_REVIEW', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.vendorForm.valid) {
      this.dialogRef.close(this.vendorForm.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}