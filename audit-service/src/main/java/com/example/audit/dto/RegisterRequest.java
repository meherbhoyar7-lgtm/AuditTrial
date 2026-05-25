package com.example.audit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
  @NotBlank
  @Size(min = 3, max = 80)
  public String username;

  @NotBlank
  @Size(min = 6, max = 120)
  public String password;
}
