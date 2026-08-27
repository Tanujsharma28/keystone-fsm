package com.keystone.backend.dto;

import java.time.LocalDateTime;

public class TimeLogResponse {

    private Long id;
    private Long workOrderId;
    private String workOrderTitle;
    private Long technicianId;
    private String technicianName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;

    public TimeLogResponse(Long id, Long workOrderId, String workOrderTitle,
                           Long technicianId, String technicianName,
                           LocalDateTime startTime, LocalDateTime endTime,
                           Integer durationMinutes) {
        this.id = id;
        this.workOrderId = workOrderId;
        this.workOrderTitle = workOrderTitle;
        this.technicianId = technicianId;
        this.technicianName = technicianName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
    }

    public Long getId() { return id; }
    public Long getWorkOrderId() { return workOrderId; }
    public String getWorkOrderTitle() { return workOrderTitle; }
    public Long getTechnicianId() { return technicianId; }
    public String getTechnicianName() { return technicianName; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Integer getDurationMinutes() { return durationMinutes; }
}