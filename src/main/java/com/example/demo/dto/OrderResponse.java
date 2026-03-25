package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderResponse {

    private final Long id;
    private final LocalDate orderDate;
    private final BigDecimal totalAmount;
    private final Long customerId;

    public OrderResponse(Long id, LocalDate orderDate, BigDecimal totalAmount, Long customerId) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }
}
