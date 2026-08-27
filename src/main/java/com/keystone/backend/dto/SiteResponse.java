package com.keystone.backend.dto;

import java.time.LocalDateTime;

public class SiteResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private String name;
    private String address;
    private LocalDateTime createdAt;

    public SiteResponse(Long id, Long customerId, String customerName,
                        String name, String address, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}