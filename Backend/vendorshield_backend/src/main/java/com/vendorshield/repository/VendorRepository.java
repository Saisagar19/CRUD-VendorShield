package com.vendorshield.repository;

import com.vendorshield.domain.enums.VendorStatus;
import com.vendorshield.domain.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    // Fast uniqueness check before insert/update
    boolean existsByTaxIdentifier(String taxIdentifier);

    boolean existsByTaxIdentifierAndIdNot(String taxIdentifier, Long id);

    // Filter by status with pagination
    Page<Vendor> findByStatus(VendorStatus status, Pageable pageable);

    // Search by name (case-insensitive) with pagination
    Page<Vendor> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Search by name (case-insensitive) and status with pagination
    Page<Vendor> findByNameContainingIgnoreCaseAndStatus(String name, VendorStatus status, Pageable pageable);
}