package com.vendorshield.controller;

import com.vendorshield.domain.enums.VendorStatus;
import com.vendorshield.dto.request.VendorRequest;
import com.vendorshield.dto.response.ApiResponse;
import com.vendorshield.dto.response.VendorResponse;
import com.vendorshield.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

	private final VendorService vendorService;

	public VendorController(VendorService vendorService) {
		this.vendorService = vendorService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<VendorResponse>> createVendor(@Valid @RequestBody VendorRequest request) {
		VendorResponse response = vendorService.createVendor(request);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.ok("Vendor registered successfully", response));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<VendorResponse>> getVendorById(@PathVariable Long id) {
		VendorResponse response = vendorService.getVendorById(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<Page<VendorResponse>>> getVendors(
			@RequestParam(required = false) VendorStatus status, @RequestParam(required = false) String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "desc") String direction) {

		Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<VendorResponse> vendors = vendorService.getVendors(status, search, pageable);
		return ResponseEntity.ok(ApiResponse.ok(vendors));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<VendorResponse>> updateVendor(@PathVariable Long id,
			@Valid @RequestBody VendorRequest request) {

		VendorResponse response = vendorService.updateVendor(id, request);
		return ResponseEntity.ok(ApiResponse.ok("Vendor updated successfully", response));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<VendorResponse>> updateVendorStatus(@PathVariable Long id,
			@RequestParam VendorStatus status) {

		VendorResponse response = vendorService.updateVendorStatus(id, status);
		return ResponseEntity.ok(ApiResponse.ok("Vendor status updated successfully", response));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteVendor(@PathVariable Long id) {
		vendorService.deleteVendor(id);
		return ResponseEntity.ok(ApiResponse.ok("Vendor deleted successfully", null));
	}
}