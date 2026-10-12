export type VendorStatus = 'ACTIVE' | 'UNDER_REVIEW' | 'SUSPENDED';

export interface Vendor {
  id: number;
  name: string;
  taxIdentifier: string;
  contactEmail: string;
  tier: string;
  status: VendorStatus;
  createdAt: string;
  updatedAt: string;
}

export interface VendorRequest {
  name: string;
  taxIdentifier: string;
  contactEmail: string;
  tier: string;
  status: VendorStatus;
}