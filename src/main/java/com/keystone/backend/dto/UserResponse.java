package com.keystone.backend.dto;

public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private Long customerId;

    public UserResponse(Long id, String fullName, String email, Long customerId) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.customerId = customerId;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public Long getCustomerId() { return customerId; }
}