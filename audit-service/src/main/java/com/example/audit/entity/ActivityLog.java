package com.example.audit.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "activity_log")
public class ActivityLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 80)
  private String actor;

  @Column(nullable = false, length = 120)
  private String action;

  @Column(name = "entity_type", length = 80)
  private String entityType;

  @Column(name = "entity_id", length = 80)
  private String entityId;

  @Column(columnDefinition = "TEXT")
  private String details;

  @Column(name = "ip_address", length = 80)
  private String ipAddress;

  @Column(name = "user_agent", columnDefinition = "TEXT")
  private String userAgent;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {
    if (createdAt == null) createdAt = Instant.now();
  }

  public Long getId() { return id; }
  public String getActor() { return actor; }
  public void setActor(String actor) { this.actor = actor; }
  public String getAction() { return action; }
  public void setAction(String action) { this.action = action; }
  public String getEntityType() { return entityType; }
  public void setEntityType(String entityType) { this.entityType = entityType; }
  public String getEntityId() { return entityId; }
  public void setEntityId(String entityId) { this.entityId = entityId; }
  public String getDetails() { return details; }
  public void setDetails(String details) { this.details = details; }
  public String getIpAddress() { return ipAddress; }
  public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
  public String getUserAgent() { return userAgent; }
  public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
  public Instant getCreatedAt() { return createdAt; }
}
