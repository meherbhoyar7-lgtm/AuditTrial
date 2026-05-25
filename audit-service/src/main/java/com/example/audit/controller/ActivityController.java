package com.example.audit.controller;

import com.example.audit.dto.ActivityCreateRequest;
import com.example.audit.dto.ActivityResponse;
import com.example.audit.dto.PageResponse;
import com.example.audit.service.ActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
public class ActivityController {

  private final ActivityService service;

  public ActivityController(ActivityService service) {
    this.service = service;
  }

  @PostMapping
  public ActivityResponse create(@Valid @RequestBody ActivityCreateRequest req, HttpServletRequest http) {
    return service.create(req, http);
  }

  @GetMapping
  public PageResponse<ActivityResponse> list(
      @RequestParam(required = false) String actor,
      @RequestParam(required = false) String action,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    return service.list(actor, action, page, size);
  }

  @GetMapping("/{id}")
  public ActivityResponse get(@PathVariable Long id) {
    return service.get(id);
  }
}
