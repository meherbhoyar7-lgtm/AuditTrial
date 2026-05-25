package com.example.audit.controller;

import com.example.audit.dto.AuthRequest;
import com.example.audit.dto.AuthResponse;
import com.example.audit.dto.RegisterRequest;
import com.example.audit.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public void register(@Valid @RequestBody RegisterRequest req) {
    authService.register(req);
  }

  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody AuthRequest req) {
    return authService.login(req);
  }
}
