package com.rednorte.servicio_auditoria.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "audit_events")
public class AuditEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(nullable = false)
    private Instant receivedAt;

    @Column(nullable = false, length = 4000)
    private String payload;

    public AuditEvent() {
    }

    public AuditEvent(String eventType, Instant occurredAt, Instant receivedAt, String payload) {
        this.eventType = eventType;
        this.occurredAt = occurredAt;
        this.receivedAt = receivedAt;
        this.payload = payload;
    }

    public Long getId() { return id; }
    public String getEventType() { return eventType; }
    public Instant getOccurredAt() { return occurredAt; }
    public Instant getReceivedAt() { return receivedAt; }
    public String getPayload() { return payload; }
    public void setId(Long id) { this.id = id; }
}
