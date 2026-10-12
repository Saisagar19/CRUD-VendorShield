import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { VendorService } from '../../../core/services/vendor.service';
import { Vendor, VendorRequest, VendorStatus } from '../../../core/models/vendor.model';
import { VendorDialogComponent } from '../vendor-dialog/vendor-dialog.component';

@Component({
  selector: 'app-vendor-list',
  templateUrl: './vendor-list.component.html',
  styleUrls: ['./vendor-list.component.scss'],
  standalone: false
})
export class VendorListComponent implements OnInit {
  // displayedColumns: string[] = ['id', 'name', 'taxIdentifier', 'contactEmail', 'tier', 'status', 'actions'];
  displayedColumns: string[] = ['name', 'taxIdentifier', 'contactEmail', 'tier', 'status', 'actions'];
  vendors: Vendor[] = [];

  totalElements = 0;
  pageSize = 10;
  currentPage = 0;

  searchQuery = '';
  statusFilter: VendorStatus | undefined;
  isLoading = false;

  // ── Inline add-row state ────────────────────────────────────────────────────
  showInlineForm = false;
  inlineForm!: FormGroup;
  isSavingInline = false;

  constructor(
    private fb: FormBuilder,
    private vendorService: VendorService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadVendors();
    this._buildInlineForm();
  }

  // Build a fresh inline form (called on init and after each successful save)
  private _buildInlineForm(): void {
    this.inlineForm = this.fb.group({
      name:         ['', [Validators.required, Validators.maxLength(150)]],
      taxIdentifier:['', [Validators.required, Validators.maxLength(50)]],
      contactEmail: ['', [Validators.required, Validators.email]],
      tier:         ['TIER_3', Validators.required],
      status:       ['UNDER_REVIEW', Validators.required]
    });
  }

  // Show the inline row and reset the form
  openInlineForm(): void {
    this._buildInlineForm();
    this.showInlineForm = true;
  }

  // Cancel and hide the inline row
  cancelInlineForm(): void {
    this.showInlineForm = false;
    this.inlineForm.reset();
  }

  // Save the inline row via the existing createVendor API call
  saveInlineVendor(): void {
    if (this.inlineForm.invalid) {
      this.inlineForm.markAllAsTouched();
      return;
    }
    this.isSavingInline = true;
    const payload: VendorRequest = this.inlineForm.value;
    this.vendorService.createVendor(payload).subscribe({
      next: () => {
        this.snackBar.open('Vendor registered successfully', 'Close', { duration: 2500 });
        this.showInlineForm = false;
        this.isSavingInline = false;
        this.loadVendors();
      },
      error: (err) => {
        this.snackBar.open(err.error?.message || 'Registration failed', 'Close', { duration: 3000 });
        this.isSavingInline = false;
      }
    });
  }

  loadVendors(): void {
    this.isLoading = true;
    this.vendorService.getVendors(this.currentPage, this.pageSize, this.searchQuery, this.statusFilter)
      .subscribe({
        next: (res) => {
          this.vendors = res.data?.content || [];
          this.totalElements = res.data?.totalElements || 0;
          this.isLoading = false;
        },
        error: (err) => {
          this.snackBar.open(err.error?.message || 'Failed to load vendors', 'Close', { duration: 3000 });
          this.isLoading = false;
        }
      });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadVendors();
  }

  applyFilter(): void {
    this.currentPage = 0;
    this.loadVendors();
  }

  openCreateDialog(): void {
    const ref = this.dialog.open(VendorDialogComponent, { width: '500px', data: null });
    ref.afterClosed().subscribe((result: VendorRequest) => {
      if (result) {
        this.vendorService.createVendor(result).subscribe({
          next: () => {
            this.snackBar.open('Vendor registered successfully', 'Close', { duration: 2500 });
            this.loadVendors();
          },
          error: (err) => this.snackBar.open(err.error?.message || 'Creation failed', 'Close', { duration: 3000 })
        });
      }
    });
  }

  openEditDialog(vendor: Vendor): void {
    const ref = this.dialog.open(VendorDialogComponent, { width: '500px', data: vendor });
    ref.afterClosed().subscribe((result: VendorRequest) => {
      if (result) {
        this.vendorService.updateVendor(vendor.id, result).subscribe({
          next: () => {
            this.snackBar.open('Vendor updated successfully', 'Close', { duration: 2500 });
            this.loadVendors();
          },
          error: (err) => this.snackBar.open(err.error?.message || 'Update failed', 'Close', { duration: 3000 })
        });
      }
    });
  }

  deleteVendor(id: number): void {
    if (confirm('Are you sure you want to delete this vendor? This will record an audit trail.')) {
      this.vendorService.deleteVendor(id).subscribe({
        next: () => {
          this.snackBar.open('Vendor deleted', 'Close', { duration: 2500 });
          this.loadVendors();
        },
        error: (err) => this.snackBar.open(err.error?.message || 'Delete failed', 'Close', { duration: 3000 })
      });
    }
  }
}