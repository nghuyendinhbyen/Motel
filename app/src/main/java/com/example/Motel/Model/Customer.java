package com.example.Motel.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Customer  implements Serializable {
    @SerializedName("id_cus")
    private int idCus;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("rooms")
    private int rooms;

    @SerializedName("date")
    private String date;

    @SerializedName("id_rooms")
    private int idRooms;


    // Getter và Setter
    public int getIdCus() {
        return idCus;
    }

    public void setIdCus(int idCus) {
        this.idCus = idCus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdRooms() {
        return idRooms;
    }

    public void setIdRooms(int idRooms) {
        this.idRooms = idRooms;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id_cus=" + idCus +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", rooms=" + rooms +
                ", date='" + date + '\'' +
                ", id_rooms=" + idRooms +
                '}';
    }
}
