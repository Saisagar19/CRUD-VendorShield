package com.vendorshield.dto.response;

import com.vendorshield.domain.enums.VendorStatus;
import com.vendorshield.domain.model.Vendor;
import java.time.Instant;

public record VendorResponse(
    Long id,
    String name,
    String taxIdentifier,
    String contactEmail,
    String tier,
    VendorStatus status,
    Instant createdAt,
    Instant updatedAt
) {
    public static VendorResponse fromEntity(Vendor vendor) {
        return new VendorResponse(
            vendor.getId(),
            vendor.getName(),
            vendor.getTaxIdentifier(),
            vendor.getContactEmail(),
            vendor.getTier(),
            vendor.getStatus(),
            vendor.getCreatedAt(),
            vendor.getUpdatedAt()
        );
    }
}