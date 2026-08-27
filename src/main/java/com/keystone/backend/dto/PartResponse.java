package com.keystone.backend.dto;

import java.math.BigDecimal;

public class PartResponse {

    private Long id;
    private String name;
    private String sku;
    private int stockQuantity;
    private BigDecimal unitPrice;

    public PartResponse(Long id, String name, String sku,
                        int stockQuantity, BigDecimal unitPrice) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.stockQuantity = stockQuantity;
        this.unitPrice = unitPrice;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSku() { return sku; }
    public int getStockQuantity() { return stockQuantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}