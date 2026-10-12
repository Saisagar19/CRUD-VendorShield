package com.vendorshield.domain.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_logs", indexes = { @Index(name = "idx_audit_logs_entity", columnList = "entity_name, entity_id") })
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "entity_name", nullable = false, length = 50)
	private String entityName;

	@Column(name = "entity_id", nullable = false)
	private Long entityId;

	@Column(name = "action", nullable = false, length = 30)
	private String action;

	@Column(name = "performed_by", length = 100)
	private String performedBy = "system";

	@Column(name = "details", columnDefinition = "TEXT")
	private String details;

	@CreationTimestamp
	@Column(name = "timestamp", nullable = false, updatable = false)
	private Instant timestamp;

	public AuditLog(String entityName, Long entityId, String action, String performedBy, String details) {
		this.entityName = entityName;
		this.entityId = entityId;
		this.action = action;
		this.performedBy = performedBy;
		this.details = details;
	}

}
