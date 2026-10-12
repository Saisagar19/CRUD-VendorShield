package com.vendorshield.repository;

import com.vendorshield.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Retrieve full audit trail for a specific entity record
    List<AuditLog> findByEntityNameAndEntityIdOrderByTimestampDesc(String entityName, Long entityId);

    // Paginated audit logs filtered by entity type (e.g. "VENDOR", "CONTRACT")
    Page<AuditLog> findByEntityNameOrderByTimestampDesc(String entityName, Pageable pageable);
}