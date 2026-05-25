package com.example.audit.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Auditor {
  public static String actor() {
    Authentication a = SecurityContextHolder.getContext().getAuthentication();
    if (a == null || a.getName() == null) return "anonymous";
    return a.getName();
  }

  public static String ip(HttpServletRequest req) {
    String xf = req.getHeader("X-Forwarded-For");
    if (xf != null && !xf.isBlank()) return xf.split(",")[0].trim();
    return req.getRemoteAddr();
  }

  public static String ua(HttpServletRequest req) {
    String ua = req.getHeader("User-Agent");
    return ua == null ? "" : ua;
  }
}
