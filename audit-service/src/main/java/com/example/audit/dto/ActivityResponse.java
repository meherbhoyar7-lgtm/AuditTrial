package com.example.audit.dto;

import java.time.Instant;

public class ActivityResponse {
  public Long id;
  public String actor;
  public String action;
  public String entityType;
  public String entityId;
  public String details;
  public String ipAddress;
  public String userAgent;
  public Instant createdAt;
}
