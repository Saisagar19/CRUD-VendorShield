package com.vendorshield.service.impl;

import com.vendorshield.domain.model.AuditLog;
import com.vendorshield.repository.AuditLogRepository;
import com.vendorshield.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuditLogServiceImpl implements AuditLogService {

	private final AuditLogRepository auditLogRepository;

	public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
		this.auditLogRepository = auditLogRepository;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void log(String entityName, Long entityId, String action, String performedBy, String details) {
		AuditLog auditLog = new AuditLog(entityName, entityId, action, performedBy, details);
		auditLogRepository.save(auditLog);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void log(String entityName, Long entityId, String action, String details) {
		log(entityName, entityId, action, "system", details);
	}

	@Override
	public List<AuditLog> getAuditLogsForEntity(String entityName, Long entityId) {
		return auditLogRepository.findByEntityNameAndEntityIdOrderByTimestampDesc(entityName, entityId);
	}

	@Override
	public Page<AuditLog> getAuditLogsByEntity(String entityName, Pageable pageable) {
		return auditLogRepository.findByEntityNameOrderByTimestampDesc(entityName, pageable);
	}
}