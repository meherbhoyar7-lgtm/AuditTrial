package com.example.audit.config;

import com.example.audit.repository.UserAccountRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserAccountRepository userRepo;

  public JwtAuthFilter(JwtService jwtService, UserAccountRepository userRepo) {
    this.jwtService = jwtService;
    this.userRepo = userRepo;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (auth == null || !auth.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = auth.substring("Bearer ".length()).trim();
    try {
      Claims claims = jwtService.parseClaims(token);
      String username = claims.getSubject();
      String role = String.valueOf(claims.get("role"));

      userRepo.findByUsername(username).orElseThrow();

      var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
      var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);

      filterChain.doFilter(request, response);
    } catch (Exception ex) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\":\"Unauthorized\"}");
    }
  }
}
