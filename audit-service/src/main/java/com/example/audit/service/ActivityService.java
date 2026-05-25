package com.example.audit.service;

import com.example.audit.dto.ActivityCreateRequest;
import com.example.audit.dto.ActivityResponse;
import com.example.audit.dto.PageResponse;
import com.example.audit.entity.ActivityLog;
import com.example.audit.repository.ActivityLogRepository;
import com.example.audit.util.Auditor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

  private final ActivityLogRepository repo;

  public ActivityService(ActivityLogRepository repo) {
    this.repo = repo;
  }

  public ActivityResponse create(ActivityCreateRequest req, HttpServletRequest http) {
    ActivityLog log = new ActivityLog();
    log.setActor(Auditor.actor());
    log.setAction(req.action);
    log.setEntityType(req.entityType);
    log.setEntityId(req.entityId);
    log.setDetails(req.details);
    log.setIpAddress(Auditor.ip(http));
    log.setUserAgent(Auditor.ua(http));

    ActivityLog saved = repo.save(log);
    return toDto(saved);
  }

  public PageResponse<ActivityResponse> list(String actor, String action, int page, int size) {
    var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    var p = repo.findByActorContainingIgnoreCaseAndActionContainingIgnoreCase(
        actor == null ? "" : actor,
        action == null ? "" : action,
        pageable);

    var items = p.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(items, page, size, p.getTotalElements(), p.getTotalPages());
  }

  public ActivityResponse get(Long id) {
    return repo.findById(id).map(this::toDto)
        .orElseThrow(() -> new IllegalArgumentException("Not found"));
  }

  private ActivityResponse toDto(ActivityLog a) {
    ActivityResponse r = new ActivityResponse();
    r.id = a.getId();
    r.actor = a.getActor();
    r.action = a.getAction();
    r.entityType = a.getEntityType();
    r.entityId = a.getEntityId();
    r.details = a.getDetails();
    r.ipAddress = a.getIpAddress();
    r.userAgent = a.getUserAgent();
    r.createdAt = a.getCreatedAt();
    return r;
  }
}
