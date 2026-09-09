package com.keystone.backend.service;

import com.keystone.backend.domain.AppUser;
import com.keystone.backend.domain.WorkOrder;
import com.keystone.backend.domain.WorkOrderStatus;
import com.keystone.backend.dto.ReportSummaryResponse;
import com.keystone.backend.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final WorkOrderRepository workOrderRepository;

    private static final Set<WorkOrderStatus> ACTIVE_STATUSES = Set.of(
            WorkOrderStatus.NEW, WorkOrderStatus.ASSIGNED,
            WorkOrderStatus.IN_PROGRESS, WorkOrderStatus.ON_HOLD
    );

    private static final Set<WorkOrderStatus> DONE_STATUSES = Set.of(
            WorkOrderStatus.COMPLETED, WorkOrderStatus.CLOSED
    );

    public ReportSummaryResponse getSummary() {
        List<WorkOrder> all = workOrderRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        // 1. Status ke hisaab se count — brief criteria 1
        Map<String, Long> statusCounts = all.stream()
                .collect(Collectors.groupingBy(
                        wo -> wo.getStatus().name(),
                        Collectors.counting()
                ));

        // 2. Overdue = abhi active hai lekin SLA nikal chuki hai — criteria 1
        long overdueCount = all.stream()
                .filter(wo -> ACTIVE_STATUSES.contains(wo.getStatus()))
                .filter(wo -> wo.getSlaDueAt() != null && wo.getSlaDueAt().isBefore(now))
                .count();

        // 3. SLA compliance % — sirf finished jobs (COMPLETED/CLOSED) ke against calculate karte hain,
        //    kyunki abhi chal rahe jobs ka "compliance" abhi decide nahi hua
        List<WorkOrder> doneJobs = all.stream()
                .filter(wo -> DONE_STATUSES.contains(wo.getStatus()))
                .toList();

        double slaCompliancePercent = doneJobs.isEmpty()
                ? 100.0
                : (doneJobs.stream().filter(wo -> !wo.isSlaBreached()).count() * 100.0) / doneJobs.size();

        // 4. Technician ke hisaab se breakdown (sirf active jobs) — criteria 3
        Map<String, Long> technicianLoadMap = all.stream()
                .filter(wo -> ACTIVE_STATUSES.contains(wo.getStatus()))
                .filter(wo -> wo.getAssignedTechnician() != null)
                .collect(Collectors.groupingBy(
                        wo -> wo.getAssignedTechnician().getFullName(),
                        Collectors.counting()
                ));

        List<ReportSummaryResponse.TechnicianLoad> byTechnician = technicianLoadMap.entrySet().stream()
                .map(e -> new ReportSummaryResponse.TechnicianLoad(e.getKey(), e.getValue()))
                .toList();

        return new ReportSummaryResponse(statusCounts, overdueCount, slaCompliancePercent, byTechnician);
    }
}