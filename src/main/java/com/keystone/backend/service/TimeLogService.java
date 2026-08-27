package com.keystone.backend.service;

import com.keystone.backend.domain.AppUser;
import com.keystone.backend.domain.Role;
import com.keystone.backend.domain.TimeLog;
import com.keystone.backend.domain.WorkOrder;
import com.keystone.backend.dto.TimeLogRequest;
import com.keystone.backend.dto.TimeLogResponse;
import com.keystone.backend.exception.BusinessException;
import com.keystone.backend.exception.ResourceNotFoundException;
import com.keystone.backend.repository.AppUserRepository;
import com.keystone.backend.repository.TimeLogRepository;
import com.keystone.backend.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final WorkOrderRepository workOrderRepository;
    private final AppUserRepository appUserRepository;

    private TimeLogResponse toResponse(TimeLog tl) {
        return new TimeLogResponse(
                tl.getId(),
                tl.getWorkOrder().getId(),
                tl.getWorkOrder().getTitle(),
                tl.getTechnician().getId(),
                tl.getTechnician().getFullName(),
                tl.getStartTime(),
                tl.getEndTime(),
                tl.getDurationMinutes()
        );
    }

    public List<TimeLogResponse> getLogsByWorkOrder(Long workOrderId) {
        return timeLogRepository.findByWorkOrderId(workOrderId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TimeLogResponse createLog(TimeLogRequest request) {
        WorkOrder workOrder = workOrderRepository.findById(request.getWorkOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found: " + request.getWorkOrderId()));

        AppUser technician = appUserRepository.findById(request.getTechnicianId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getTechnicianId()));

        if (technician.getRole() != Role.TECHNICIAN) {
            throw new BusinessException("User is not a TECHNICIAN: " + technician.getId());
        }

        if (request.getStartTime() == null) {
            throw new BusinessException("startTime is required");
        }

        Integer duration = null;
        if (request.getEndTime() != null) {
            if (!request.getEndTime().isAfter(request.getStartTime())) {
                throw new BusinessException("endTime must be after startTime");
            }
            duration = (int) Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();
        }

        TimeLog log = new TimeLog();
        log.setWorkOrder(workOrder);
        log.setTechnician(technician);
        log.setStartTime(request.getStartTime());
        log.setEndTime(request.getEndTime());
        log.setDurationMinutes(duration);

        return toResponse(timeLogRepository.save(log));
    }
}