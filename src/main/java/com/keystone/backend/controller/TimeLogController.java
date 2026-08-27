package com.keystone.backend.controller;

import com.keystone.backend.dto.TimeLogRequest;
import com.keystone.backend.dto.TimeLogResponse;
import com.keystone.backend.service.TimeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time-logs")
@RequiredArgsConstructor
public class TimeLogController {

    private final TimeLogService timeLogService;

    @GetMapping("/work-order/{workOrderId}")
    @PreAuthorize("hasAnyRole('MANAGER','DISPATCHER','TECHNICIAN')")
    public ResponseEntity<List<TimeLogResponse>> getLogsByWorkOrder(
            @PathVariable Long workOrderId) {
        return ResponseEntity.ok(timeLogService.getLogsByWorkOrder(workOrderId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER','TECHNICIAN')")
    public ResponseEntity<TimeLogResponse> createLog(
            @RequestBody TimeLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(timeLogService.createLog(request));
    }
}