package com.keystone.backend.dto;

import java.time.LocalDateTime;

public class TimeLogRequest {

    private Long workOrderId;
    private Long technicianId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;  // optional — baad mein set kar sakte hain

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public Long getTechnicianId() { return technicianId; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}