package com.internship.tool.audit;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.internship.tool.entity.AuditLog;
import com.internship.tool.repository.AuditLogRepository;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditLogRepository auditRepo;

    // 🔥 trigger on ANY service method
    @AfterReturning(
        pointcut = "execution(* com.internship.tool.service..*(..))",
        returning = "result"
    )
    public void logAll(Object result) {

        if (result == null) return;

        try {
            AuditLog log = new AuditLog();
            log.setEntityName(result.getClass().getSimpleName());
            log.setAction("ACTION");
            log.setNewValue(result.toString());
            log.setChangedBy("SYSTEM");

            auditRepo.save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}