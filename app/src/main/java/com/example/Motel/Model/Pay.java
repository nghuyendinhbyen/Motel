package com.example.Motel.Model;

public class Pay {
    private int id_pay;
    private int room;
    private int electricNumber;
    private int waterNumber;
    private int internet;
    private String dateStart;
    private String dateEnd;
    private String price;
    private String status;

    public Pay() {}

    // Constructor
    public Pay(int id_pay, int room, int electricNumber, int waterNumber, int internet, String dateStart, String dateEnd,String price, String status) {
        this.id_pay = id_pay;
        this.room = room;
        this.electricNumber = electricNumber;
        this.waterNumber = waterNumber;
        this.internet = internet;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.price = price;
        this.status = status;
    }

    public int getId_pay() {
        return id_pay;
    }

    public void setId_pay(int id_pay) {
        this.id_pay = id_pay;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getElectricNumber() {
        return electricNumber;
    }

    public void setElectricNumber(int electricNumber) {
        this.electricNumber = electricNumber;
    }

    public int getWaterNumber() {
        return waterNumber;
    }

    public void setWaterNumber(int waterNumber) {
        this.waterNumber = waterNumber;
    }

    public int getInternet() {
        return internet;
    }

    public void setInternet(int internet) {
        this.internet = internet;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    @Override
    public String toString() {
        return "Pay{" +
                "id_pay=" + id_pay +
                ", room=" + room +
                ", electricNumber=" + electricNumber +
                ", waterNumber=" + waterNumber +
                ", internet=" + internet +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", price=" + price +
                ", price=" + status +
                '}';
    }


}
