package com.stocklab.service;

import com.stocklab.model.AuditLog;
import com.stocklab.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void log(String eventType, String actor, String details) {
        AuditLog log = new AuditLog();
        log.setEventType(eventType);
        log.setActor(actor == null ? "anonymous" : actor);
        log.setDetails(details);
        auditLogRepository.save(log);
    }
}
