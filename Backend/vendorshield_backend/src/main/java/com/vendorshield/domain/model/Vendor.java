package com.vendorshield.domain.model;

import com.vendorshield.domain.enums.VendorStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendors", indexes = { @Index(name = "idx_vendors_status", columnList = "status") })
@Getter
@Setter
@NoArgsConstructor
public class Vendor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, length = 150)
	private String name;

	@Column(name = "tax_identifier", nullable = false, unique = true, length = 50)
	private String taxIdentifier;

	@Column(name = "contact_email", nullable = false, length = 100)
	private String contactEmail;

	@Column(name = "tier", length = 20)
	private String tier = "TIER_3";

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 30)
	private VendorStatus status = VendorStatus.UNDER_REVIEW;

//	@OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Contract> contracts = new ArrayList<>();
//
//	@OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<ComplianceCertification> certifications = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Instant updatedAt;

//	// Helper synchronization methods for bidirectional relationships
//	public void addContract(Contract contract) {
//		contracts.add(contract);
//		contract.setVendor(this);
//	}
//
//	public void removeContract(Contract contract) {
//		contracts.remove(contract);
//		contract.setVendor(null);
//	}
//
//	public void addCertification(ComplianceCertification cert) {
//		certifications.add(cert);
//		cert.setVendor(this);
//	}
//
//	public void removeCertification(ComplianceCertification cert) {
//		certifications.remove(cert);
//		cert.setVendor(null);
//	}
}