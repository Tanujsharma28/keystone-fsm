package com.keystone.backend.controller;

import com.keystone.backend.dto.PartUsageRequest;
import com.keystone.backend.dto.PartUsageResponse;
import com.keystone.backend.service.PartUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/part-usages")
@RequiredArgsConstructor
public class PartUsageController {

    private final PartUsageService partUsageService;

    @GetMapping("/work-order/{workOrderId}")
    @PreAuthorize("hasAnyRole('MANAGER','DISPATCHER','TECHNICIAN')")
    public ResponseEntity<List<PartUsageResponse>> getUsageByWorkOrder(
            @PathVariable Long workOrderId) {
        return ResponseEntity.ok(partUsageService.getUsageByWorkOrder(workOrderId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER','TECHNICIAN')")
    public ResponseEntity<PartUsageResponse> recordUsage(
            @RequestBody PartUsageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(partUsageService.recordUsage(request));
    }
}