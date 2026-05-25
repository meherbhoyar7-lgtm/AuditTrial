package com.example.audit.dto;

import jakarta.validation.constraints.NotBlank;

public class ActivityCreateRequest {
  @NotBlank public String action;

  public String entityType;
  public String entityId;
  public String details;
}
