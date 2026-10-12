package com.vendorshield.controller;

import com.vendorshield.domain.model.AuditLog;
import com.vendorshield.dto.response.ApiResponse;
import com.vendorshield.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditLogController {

	private final AuditLogService auditLogService;

	public AuditLogController(AuditLogService auditLogService) {
		this.auditLogService = auditLogService;
	}

	@GetMapping("/entity/{entityName}/{entityId}")
	public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditTrailForRecord(@PathVariable String entityName,
			@PathVariable Long entityId) {

		List<AuditLog> logs = auditLogService.getAuditLogsForEntity(entityName.toUpperCase(), entityId);
		return ResponseEntity.ok(ApiResponse.ok(logs));
	}

	@GetMapping("/entity/{entityName}")
	public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogsByEntity(@PathVariable String entityName,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<AuditLog> logs = auditLogService.getAuditLogsByEntity(entityName.toUpperCase(), pageable);
		return ResponseEntity.ok(ApiResponse.ok(logs));
	}
}