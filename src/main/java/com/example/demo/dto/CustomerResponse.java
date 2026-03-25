package com.example.demo.dto;

public class CustomerResponse {

    private final Long id;
    private final String name;

    public CustomerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
