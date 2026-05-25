package com.example.audit.service;

import com.example.audit.config.JwtService;
import com.example.audit.dto.AuthRequest;
import com.example.audit.dto.AuthResponse;
import com.example.audit.dto.RegisterRequest;
import com.example.audit.entity.UserAccount;
import com.example.audit.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserAccountRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(UserAccountRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public void register(RegisterRequest req) {
    if (userRepo.existsByUsername(req.username)) {
      throw new IllegalArgumentException("Username already exists");
    }
    UserAccount u = new UserAccount();
    u.setUsername(req.username);
    u.setPasswordHash(passwordEncoder.encode(req.password));
    u.setRole("USER");
    userRepo.save(u);
  }

  public AuthResponse login(AuthRequest req) {
    UserAccount u = userRepo.findByUsername(req.username)
        .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

    if (!passwordEncoder.matches(req.password, u.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid username or password");
    }

    String token = jwtService.generateToken(u.getUsername(), u.getRole());
    return new AuthResponse(token, u.getUsername(), u.getRole());
  }
}
