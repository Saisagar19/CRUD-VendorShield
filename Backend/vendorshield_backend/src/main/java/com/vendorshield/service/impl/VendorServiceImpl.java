package com.vendorshield.service.impl;

import com.vendorshield.domain.enums.VendorStatus;
import com.vendorshield.domain.model.Vendor;
import com.vendorshield.dto.request.VendorRequest;
import com.vendorshield.dto.response.VendorResponse;
import com.vendorshield.exception.BusinessRuleException;
import com.vendorshield.exception.ResourceNotFoundException;
import com.vendorshield.repository.VendorRepository;
import com.vendorshield.service.AuditLogService;
import com.vendorshield.service.VendorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VendorServiceImpl implements VendorService {

	private final VendorRepository vendorRepository;
	private final AuditLogService auditLogService;

	public VendorServiceImpl(VendorRepository vendorRepository, AuditLogService auditLogService) {
		this.vendorRepository = vendorRepository;
		this.auditLogService = auditLogService;
	}

	@Override
	public VendorResponse createVendor(VendorRequest request) {
		if (vendorRepository.existsByTaxIdentifier(request.taxIdentifier())) {
			throw new BusinessRuleException(
					"Vendor with tax identifier " + request.taxIdentifier() + " already exists");
		}

		Vendor vendor = new Vendor();
		vendor.setName(request.name());
		vendor.setTaxIdentifier(request.taxIdentifier());
		vendor.setContactEmail(request.contactEmail());
		vendor.setTier(request.tier() != null ? request.tier() : "TIER_3");
		vendor.setStatus(request.status() != null ? request.status() : VendorStatus.UNDER_REVIEW);

		Vendor saved = vendorRepository.save(vendor);
		auditLogService.log("VENDOR", saved.getId(), "CREATE", "Created vendor: " + saved.getName());

		return VendorResponse.fromEntity(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public VendorResponse getVendorById(Long id) {
		return vendorRepository.findById(id).map(VendorResponse::fromEntity)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "id", id));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<VendorResponse> getVendors(VendorStatus status, String search, Pageable pageable) {
		String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;

		Page<Vendor> vendorsPage;
		if (cleanSearch != null && status != null) {
			vendorsPage = vendorRepository.findByNameContainingIgnoreCaseAndStatus(cleanSearch, status, pageable);
		} else if (cleanSearch != null) {
			vendorsPage = vendorRepository.findByNameContainingIgnoreCase(cleanSearch, pageable);
		} else if (status != null) {
			vendorsPage = vendorRepository.findByStatus(status, pageable);
		} else {
			vendorsPage = vendorRepository.findAll(pageable);
		}

		return vendorsPage.map(VendorResponse::fromEntity);
	}

	@Override
	public VendorResponse updateVendor(Long id, VendorRequest request) {
		Vendor vendor = vendorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "id", id));

		if (vendorRepository.existsByTaxIdentifierAndIdNot(request.taxIdentifier(), id)) {
			throw new BusinessRuleException(
					"Tax identifier " + request.taxIdentifier() + " is already assigned to another vendor");
		}

		vendor.setName(request.name());
		vendor.setTaxIdentifier(request.taxIdentifier());
		vendor.setContactEmail(request.contactEmail());
		if (request.tier() != null)
			vendor.setTier(request.tier());
		if (request.status() != null)
			vendor.setStatus(request.status());

		Vendor updated = vendorRepository.save(vendor);
		auditLogService.log("VENDOR", updated.getId(), "UPDATE", "Updated details for vendor: " + updated.getName());

		return VendorResponse.fromEntity(updated);
	}

	@Override
	public VendorResponse updateVendorStatus(Long id, VendorStatus status) {
		Vendor vendor = vendorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "id", id));

		VendorStatus oldStatus = vendor.getStatus();
		vendor.setStatus(status);
		Vendor updated = vendorRepository.save(vendor);

		auditLogService.log("VENDOR", id, "STATUS_CHANGE",
				String.format("Status changed from %s to %s", oldStatus, status));

		return VendorResponse.fromEntity(updated);
	}

	@Override
	public void deleteVendor(Long id) {
		Vendor vendor = vendorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "id", id));

		vendorRepository.delete(vendor);
		auditLogService.log("VENDOR", id, "DELETE", "Deleted vendor: " + vendor.getName());
	}
}