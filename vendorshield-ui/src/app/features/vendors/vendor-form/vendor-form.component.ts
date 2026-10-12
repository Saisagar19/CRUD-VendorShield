import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { VendorService } from '../../../core/services/vendor.service';

@Component({
  selector: 'app-vendor-form',
  templateUrl: './vendor-form.component.html',
  styleUrls: ['./vendor-form.component.scss'],
  standalone: false
})
export class VendorFormComponent implements OnInit {
  vendorForm!: FormGroup;
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private vendorService: VendorService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.vendorForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(150)]],
      taxIdentifier: ['', [Validators.required, Validators.maxLength(50)]],
      contactEmail: ['', [Validators.required, Validators.email]],
      tier: ['TIER_3', Validators.required],
      status: ['UNDER_REVIEW', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.vendorForm.invalid) {
      return;
    }

    this.isSubmitting = true;
    this.vendorService.createVendor(this.vendorForm.value).subscribe({
      next: () => {
        this.snackBar.open('Vendor registered successfully!', 'Close', { duration: 3000 });
        this.router.navigate(['/vendors']);
      },
      error: (err) => {
        this.snackBar.open(err.error?.message || 'Failed to create vendor', 'Close', { duration: 3000 });
        this.isSubmitting = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/vendors']);
  }
}