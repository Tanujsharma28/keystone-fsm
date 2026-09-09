package com.keystone.backend.dto;

import com.keystone.backend.domain.Priority;
import com.keystone.backend.domain.WorkOrderStatus;
import java.time.LocalDateTime;

public class WorkOrderResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private Long siteId;
    private String siteName;
    private Long assignedTechnicianId;
    private String assignedTechnicianName;
    private WorkOrderStatus status;
    private String title;
    private String description;
    private Priority priority;
    private LocalDateTime slaDueAt;
    private boolean slaBreached;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WorkOrderResponse(Long id, Long customerId, String customerName,
                             Long siteId, String siteName,
                             Long assignedTechnicianId, String assignedTechnicianName,
                             WorkOrderStatus status, String title, String description,
                             Priority priority, LocalDateTime slaDueAt, boolean slaBreached,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.siteId = siteId;
        this.siteName = siteName;
        this.assignedTechnicianId = assignedTechnicianId;
        this.assignedTechnicianName = assignedTechnicianName;
        this.status = status;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.slaDueAt = slaDueAt;
        this.slaBreached = slaBreached;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public Long getSiteId() { return siteId; }
    public String getSiteName() { return siteName; }
    public Long getAssignedTechnicianId() { return assignedTechnicianId; }
    public String getAssignedTechnicianName() { return assignedTechnicianName; }
    public WorkOrderStatus getStatus() { return status; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public LocalDateTime getSlaDueAt() { return slaDueAt; }
    public boolean isSlaBreached() { return slaBreached; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}