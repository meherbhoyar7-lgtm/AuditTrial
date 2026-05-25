package com.example.audit.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

  private final Key signingKey;
  private final long expirationMinutes;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expirationMinutes}") long expirationMinutes
  ) {
    byte[] keyBytes;
    try {
      keyBytes = Decoders.BASE64.decode(secret);
    } catch (IllegalArgumentException ex) {
      keyBytes = secret.getBytes();
    }
    this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    this.expirationMinutes = expirationMinutes;
  }

  public String generateToken(String username, String role) {
    Instant now = Instant.now();
    Instant exp = now.plus(expirationMinutes, ChronoUnit.MINUTES);

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(exp))
        .addClaims(Map.of("role", role))
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims parseClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(signingKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
