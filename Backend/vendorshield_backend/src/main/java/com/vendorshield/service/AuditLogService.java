package com.vendorshield.service;

import com.vendorshield.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuditLogService {
	void log(String entityName, Long entityId, String action, String performedBy, String details);

	void log(String entityName, Long entityId, String action, String details);

	List<AuditLog> getAuditLogsForEntity(String entityName, Long entityId);

	Page<AuditLog> getAuditLogsByEntity(String entityName, Pageable pageable);
}