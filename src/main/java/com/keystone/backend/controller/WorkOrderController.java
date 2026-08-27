package com.keystone.backend.controller;

import com.keystone.backend.domain.WorkOrderStatus;
import com.keystone.backend.dto.WorkOrderRequest;
import com.keystone.backend.dto.WorkOrderResponse;
import com.keystone.backend.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','DISPATCHER','TECHNICIAN')")
    public ResponseEntity<List<WorkOrderResponse>> getAllWorkOrders() {
        return ResponseEntity.ok(workOrderService.getAllWorkOrders());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','DISPATCHER','TECHNICIAN')")
    public ResponseEntity<WorkOrderResponse> getWorkOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(workOrderService.getWorkOrderById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER','DISPATCHER')")
    public ResponseEntity<WorkOrderResponse> createWorkOrder(@RequestBody WorkOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workOrderService.createWorkOrder(request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MANAGER','DISPATCHER','TECHNICIAN')")
    public ResponseEntity<WorkOrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam WorkOrderStatus newStatus) {
        return ResponseEntity.ok(workOrderService.updateStatus(id, newStatus));
    }

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('MANAGER','DISPATCHER')")
    public ResponseEntity<WorkOrderResponse> assignTechnician(
            @PathVariable Long id,
            @RequestParam Long technicianId) {
        return ResponseEntity.ok(workOrderService.assignTechnician(id, technicianId));
    }
}