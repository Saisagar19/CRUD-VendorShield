package com.vendorshield.service;

import com.vendorshield.domain.enums.VendorStatus;
import com.vendorshield.dto.request.VendorRequest;
import com.vendorshield.dto.response.VendorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorService {
	VendorResponse createVendor(VendorRequest request);

	VendorResponse getVendorById(Long id);

	Page<VendorResponse> getVendors(VendorStatus status, String search, Pageable pageable);

	VendorResponse updateVendor(Long id, VendorRequest request);

	void deleteVendor(Long id);

	VendorResponse updateVendorStatus(Long id, VendorStatus status);
}