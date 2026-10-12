import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../models/api-response.model';
import { Vendor, VendorRequest, VendorStatus } from '../models/vendor.model';

@Injectable({
  providedIn: 'root'
})
export class VendorService {
  private readonly baseUrl = 'http://localhost:8080/api/v1/vendors';

  constructor(private http: HttpClient) {}

  getVendors(
    page: number = 0,
    size: number = 10,
    search?: string,
    status?: VendorStatus
  ): Observable<ApiResponse<PageResponse<Vendor>>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search && search.trim()) {
      params = params.set('search', search.trim());
    }
    if (status) {
      params = params.set('status', status);
    }

    return this.http.get<ApiResponse<PageResponse<Vendor>>>(this.baseUrl, { params });
  }

  getVendorById(id: number): Observable<ApiResponse<Vendor>> {
    return this.http.get<ApiResponse<Vendor>>(`${this.baseUrl}/${id}`);
  }

  createVendor(payload: VendorRequest): Observable<ApiResponse<Vendor>> {
    return this.http.post<ApiResponse<Vendor>>(this.baseUrl, payload);
  }

  updateVendor(id: number, payload: VendorRequest): Observable<ApiResponse<Vendor>> {
    return this.http.put<ApiResponse<Vendor>>(`${this.baseUrl}/${id}`, payload);
  }

  updateVendorStatus(id: number, status: VendorStatus): Observable<ApiResponse<Vendor>> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<ApiResponse<Vendor>>(`${this.baseUrl}/${id}/status`, null, { params });
  }

  deleteVendor(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }
}