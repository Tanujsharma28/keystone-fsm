package com.keystone.backend.service;

import com.keystone.backend.domain.Part;
import com.keystone.backend.domain.PartUsage;
import com.keystone.backend.domain.WorkOrder;
import com.keystone.backend.dto.PartUsageRequest;
import com.keystone.backend.dto.PartUsageResponse;
import com.keystone.backend.exception.BusinessException;
import com.keystone.backend.exception.ResourceNotFoundException;
import com.keystone.backend.repository.PartRepository;
import com.keystone.backend.repository.PartUsageRepository;
import com.keystone.backend.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartUsageService {

    private final PartUsageRepository partUsageRepository;
    private final WorkOrderRepository workOrderRepository;
    private final PartRepository partRepository;

    private PartUsageResponse toResponse(PartUsage pu) {
        return new PartUsageResponse(
                pu.getId(),
                pu.getWorkOrder().getId(),
                pu.getWorkOrder().getTitle(),
                pu.getPart().getId(),
                pu.getPart().getName(),
                pu.getPart().getSku(),
                pu.getQuantityUsed(),
                pu.getPart().getStockQuantity(),
                pu.getUsedAt()
        );
    }

    public List<PartUsageResponse> getUsageByWorkOrder(Long workOrderId) {
        return partUsageRepository.findByWorkOrderId(workOrderId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PartUsageResponse recordUsage(PartUsageRequest request) {
        if (request.getQuantityUsed() <= 0) {
            throw new BusinessException("Quantity must be greater than 0");
        }

        WorkOrder workOrder = workOrderRepository.findById(request.getWorkOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder not found: " + request.getWorkOrderId()));

        Part part = partRepository.findById(request.getPartId())
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + request.getPartId()));

        if (part.getStockQuantity() < request.getQuantityUsed()) {
            throw new BusinessException(
                "Insufficient stock for part: " + part.getName() +
                ". Available: " + part.getStockQuantity() +
                ", Requested: " + request.getQuantityUsed()
            );
        }

        PartUsage usage = new PartUsage();
        usage.setWorkOrder(workOrder);
        usage.setPart(part);
        usage.setQuantityUsed(request.getQuantityUsed());

        part.setStockQuantity(part.getStockQuantity() - request.getQuantityUsed());
        partRepository.save(part);

        return toResponse(partUsageRepository.save(usage));
    }
}