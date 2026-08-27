package com.keystone.backend.service;

import com.keystone.backend.domain.*;
import com.keystone.backend.dto.WorkOrderRequest;
import com.keystone.backend.dto.WorkOrderResponse;
import com.keystone.backend.exception.BusinessException;
import com.keystone.backend.exception.ResourceNotFoundException;
import com.keystone.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final CustomerRepository customerRepository;
    private final SiteRepository siteRepository;
    private final AppUserRepository appUserRepository;

    private static final Map<WorkOrderStatus, Set<WorkOrderStatus>> VALID_TRANSITIONS = Map.of(
        WorkOrderStatus.NEW,         Set.of(WorkOrderStatus.ASSIGNED, WorkOrderStatus.CANCELLED),
        WorkOrderStatus.ASSIGNED,    Set.of(WorkOrderStatus.IN_PROGRESS, WorkOrderStatus.ON_HOLD, WorkOrderStatus.CANCELLED),
        WorkOrderStatus.IN_PROGRESS, Set.of(WorkOrderStatus.ON_HOLD, WorkOrderStatus.COMPLETED, WorkOrderStatus.CANCELLED),
        WorkOrderStatus.ON_HOLD,     Set.of(WorkOrderStatus.IN_PROGRESS, WorkOrderStatus.CANCELLED),
        WorkOrderStatus.COMPLETED,   Set.of(WorkOrderStatus.CLOSED),
        WorkOrderStatus.CLOSED,      Set.of(),
        WorkOrderStatus.CANCELLED,   Set.of()
    );

    private static final Map<Priority, Integer> SLA_HOURS = Map.of(
        Priority.URGENT, 4,
        Priority.HIGH,   24,
        Priority.MEDIUM, 72,
        Priority.LOW,    168
    );

    private WorkOrderResponse toResponse(WorkOrder wo) {
        AppUser tech = wo.getAssignedTechnician();
        return new WorkOrderResponse(
                wo.getId(),
                wo.getCustomer().getId(),
                wo.getCustomer().getName(),
                wo.getSite().getId(),
                wo.getSite().getName(),
                tech != null ? tech.getId() : null,
                tech != null ? tech.getFullName() : null,
                wo.getStatus(),
                wo.getTitle(),
                wo.getDescription(),
                wo.getPriority(),
                wo.getSlaDueAt(),
                wo.getCreatedAt(),
                wo.getUpdatedAt()
        );
    }

    public List<WorkOrderResponse> getAllWorkOrders() {
        return workOrderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public WorkOrderResponse getWorkOrderById(Long id) {
        WorkOrder wo = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found: " + id));
        return toResponse(wo);
    }

    @Transactional
    public WorkOrderResponse createWorkOrder(WorkOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.getCustomerId()));
        Site site = siteRepository.findById(request.getSiteId())
                .orElseThrow(() -> new ResourceNotFoundException("Site not found: " + request.getSiteId()));

        WorkOrder wo = new WorkOrder();
        wo.setCustomer(customer);
        wo.setSite(site);
        wo.setTitle(request.getTitle());
        wo.setDescription(request.getDescription());

        Priority priority = request.getPriority() != null ? request.getPriority() : Priority.MEDIUM;
        wo.setPriority(priority);
        wo.setSlaDueAt(LocalDateTime.now().plusHours(SLA_HOURS.get(priority)));

        if (request.getAssignedTechnicianId() != null) {
            AppUser tech = appUserRepository.findById(request.getAssignedTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getAssignedTechnicianId()));
            if (tech.getRole() != Role.TECHNICIAN) {
                throw new BusinessException("User is not a TECHNICIAN: " + tech.getId());
            }
            wo.setAssignedTechnician(tech);
            wo.setStatus(WorkOrderStatus.ASSIGNED);
        }

        return toResponse(workOrderRepository.save(wo));
    }

    @Transactional
    public WorkOrderResponse updateStatus(Long id, WorkOrderStatus newStatus) {
        WorkOrder wo = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found: " + id));

        WorkOrderStatus currentStatus = wo.getStatus();
        Set<WorkOrderStatus> allowed = VALID_TRANSITIONS.get(currentStatus);

        if (!allowed.contains(newStatus)) {
            throw new BusinessException(
                "Invalid status transition: " + currentStatus + " -> " + newStatus
            );
        }

        wo.setStatus(newStatus);
        return toResponse(workOrderRepository.save(wo));
    }

    @Transactional
    public WorkOrderResponse assignTechnician(Long workOrderId, Long technicianId) {
        WorkOrder wo = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found: " + workOrderId));
        AppUser tech = appUserRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + technicianId));

        if (tech.getRole() != Role.TECHNICIAN) {
            throw new BusinessException("User is not a TECHNICIAN: " + tech.getId());
        }

        wo.setAssignedTechnician(tech);
        if (wo.getStatus() == WorkOrderStatus.NEW) {
            wo.setStatus(WorkOrderStatus.ASSIGNED);
        }

        return toResponse(workOrderRepository.save(wo));
    }
}