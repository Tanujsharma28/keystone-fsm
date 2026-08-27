package com.keystone.backend.dto;

import java.time.LocalDateTime;

public class PartUsageResponse {

    private Long id;
    private Long workOrderId;
    private String workOrderTitle;
    private Long partId;
    private String partName;
    private String partSku;
    private int quantityUsed;
    private int remainingStock;
    private LocalDateTime usedAt;

    public PartUsageResponse(Long id, Long workOrderId, String workOrderTitle,
                             Long partId, String partName, String partSku,
                             int quantityUsed, int remainingStock, LocalDateTime usedAt) {
        this.id = id;
        this.workOrderId = workOrderId;
        this.workOrderTitle = workOrderTitle;
        this.partId = partId;
        this.partName = partName;
        this.partSku = partSku;
        this.quantityUsed = quantityUsed;
        this.remainingStock = remainingStock;
        this.usedAt = usedAt;
    }

    public Long getId() { return id; }
    public Long getWorkOrderId() { return workOrderId; }
    public String getWorkOrderTitle() { return workOrderTitle; }
    public Long getPartId() { return partId; }
    public String getPartName() { return partName; }
    public String getPartSku() { return partSku; }
    public int getQuantityUsed() { return quantityUsed; }
    public int getRemainingStock() { return remainingStock; }
    public LocalDateTime getUsedAt() { return usedAt; }
}