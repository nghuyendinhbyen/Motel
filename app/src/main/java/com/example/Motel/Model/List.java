package com.example.Motel.Model;

public class List {
    private int customerId;
    private String customerName;

    private int customerImageResId;

    public List(int customerId, String customerName, int customerImageResId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerImageResId = customerImageResId;
    }

    public List() {

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public int getCustomerImageResId() {
        return customerImageResId;
    }

    public void setCustomerImageResId(int customerImageResId) {
        this.customerImageResId = customerImageResId;
    }
}
