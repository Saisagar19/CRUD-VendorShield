package com.vendorshield.service.impl;

import com.vendorshield.domain.model.AuditLog;
import com.vendorshield.domain.model.User;
import com.vendorshield.repository.AuditLogRepository;
import com.vendorshield.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
		String actor = (performedBy != null && !performedBy.isBlank() && !"system".equalsIgnoreCase(performedBy))
				? performedBy
				: resolveCurrentAuditorRole();

		AuditLog auditLog = new AuditLog(entityName, entityId, action, actor, details);
		auditLogRepository.save(auditLog);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void log(String entityName, Long entityId, String action, String details) {
		log(entityName, entityId, action, resolveCurrentAuditorRole(), details);
	}

	@Override
	public List<AuditLog> getAuditLogsForEntity(String entityName, Long entityId) {
		return auditLogRepository.findByEntityNameAndEntityIdOrderByTimestampDesc(entityName, entityId);
	}

	@Override
	public Page<AuditLog> getAuditLogsByEntity(String entityName, Pageable pageable) {
		return auditLogRepository.findByEntityNameOrderByTimestampDesc(entityName, pageable);
	}

	/**
	 * Extracts the role of the currently authenticated principal from SecurityContextHolder.
	 * If no authentication or anonymous, falls back to "system".
	 */
	private String resolveCurrentAuditorRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			// 1. If principal is our custom User model, retrieve the assigned role directly
			if (auth.getPrincipal() instanceof User user && user.getRole() != null && !user.getRole().isBlank()) {
				return user.getRole();
			}

			// 2. Otherwise extract the first granted authority (e.g. ROLE_ADMIN, ROLE_MANAGER)
			if (!auth.getAuthorities().isEmpty()) {
				return auth.getAuthorities().iterator().next().getAuthority();
			}

			// 3. Fallback to principal username if no authorities present
			if (auth.getName() != null && !auth.getName().isBlank()) {
				return auth.getName();
			}
		}

		return "system";
	}
}