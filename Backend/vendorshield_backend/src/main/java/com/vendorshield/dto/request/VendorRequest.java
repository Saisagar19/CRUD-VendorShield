package com.vendorshield.dto.request;

import com.vendorshield.domain.enums.VendorStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VendorRequest(
		@NotBlank(message = "Vendor name is required") @Size(max = 150, message = "Name cannot exceed 150 characters") String name,

		@NotBlank(message = "Tax identifier is required") @Size(max = 50, message = "Tax ID cannot exceed 50 characters") String taxIdentifier,

		@NotBlank(message = "Contact email is required") @Email(message = "Invalid email format") String contactEmail,

		@Pattern(regexp = "^(TIER_1|TIER_2|TIER_3)$", message = "Tier must be TIER_1, TIER_2, or TIER_3") String tier,

		VendorStatus status) {

}
