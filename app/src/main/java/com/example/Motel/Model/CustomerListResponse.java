package com.example.Motel.Model;

import java.util.List;

public class CustomerListResponse {
    private List<Customer> customers;

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
