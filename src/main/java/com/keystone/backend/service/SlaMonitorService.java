package com.keystone.backend.service;

import com.keystone.backend.domain.AppUser;
import com.keystone.backend.domain.Role;
import com.keystone.backend.domain.WorkOrder;
import com.keystone.backend.domain.WorkOrderStatus;
import com.keystone.backend.repository.AppUserRepository;
import com.keystone.backend.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

// Brief F7: "A scheduled job flags work orders at risk of, or in, breach" +
// "Breaches are visible to managers and trigger a notification"
@Service
@RequiredArgsConstructor
public class SlaMonitorService {

    private final WorkOrderRepository workOrderRepository;
    private final AppUserRepository appUserRepository;
    private final JavaMailSender mailSender;

    // Ye statuses reach ho gaye toh SLA ab matter nahi karta — job khatam ho chuka hai
    private static final Set<WorkOrderStatus> TERMINAL_STATUSES = Set.of(
            WorkOrderStatus.CLOSED, WorkOrderStatus.CANCELLED, WorkOrderStatus.COMPLETED
    );

    // Har 5 minute (300000 ms) mein check karta hai — seed-scale data ke liye findAll() kaafi hai
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void checkSlaBreaches() {
        LocalDateTime now = LocalDateTime.now();

        List<WorkOrder> newlyBreached = workOrderRepository.findAll().stream()
                .filter(wo -> !wo.isSlaBreached())
                .filter(wo -> !TERMINAL_STATUSES.contains(wo.getStatus()))
                .filter(wo -> wo.getSlaDueAt() != null && wo.getSlaDueAt().isBefore(now))
                .toList();

        if (newlyBreached.isEmpty()) {
            return;
        }

        List<String> managerEmails = appUserRepository.findByRole(Role.MANAGER).stream()
                .map(AppUser::getEmail)
                .toList();

        for (WorkOrder wo : newlyBreached) {
            wo.setSlaBreached(true);
            workOrderRepository.save(wo);
            notifyManagers(wo, managerEmails);
        }
    }

    private void notifyManagers(WorkOrder wo, List<String> managerEmails) {
        if (managerEmails.isEmpty()) {
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(managerEmails.toArray(new String[0]));
        message.setSubject("KEYSTONE — SLA Breach on Work Order #" + wo.getId());
        message.setText(
                "Work order \"" + wo.getTitle() + "\" has breached its SLA.\n\n" +
                "Customer: " + wo.getCustomer().getName() + "\n" +
                "Site: " + wo.getSite().getName() + "\n" +
                "Priority: " + wo.getPriority() + "\n" +
                "SLA was due at: " + wo.getSlaDueAt() + "\n" +
                "Current status: " + wo.getStatus()
        );
        try {
            mailSender.send(message);
        } catch (Exception e) {
            // Email fail ho jaye toh bhi scheduled job crash nahi hona chahiye
            System.err.println("SLA breach email failed: " + e.getMessage());
        }
    }
}