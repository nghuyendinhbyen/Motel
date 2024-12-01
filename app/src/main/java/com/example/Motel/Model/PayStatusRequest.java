package com.example.Motel.Model;

public class PayStatusRequest {
    private int id_pay;
    private String status;

    public PayStatusRequest(int id_Pay, String status) {
        this.id_pay = id_Pay;
        this.status = status;
    }

    public int getIdPay() {
        return id_pay;
    }

    public String getStatus() {
        return status;
    }
}
