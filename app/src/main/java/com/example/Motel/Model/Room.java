package com.example.Motel.Model;

public class Room {
    private int id_rooms;
    private String name;
    private String category;
    private String status;
    private String price;

    // Constructor
    public Room(int id_rooms, String name, String category, String status, String price) {
        this.id_rooms = id_rooms;
        this.name = name;
        this.category = category;
        this.status = status;
        this.price = price;
    }

    // Getters and Setters
    public int getId_rooms() {
        return id_rooms;
    }

    public void setId_rooms(int id_rooms) {
        this.id_rooms = id_rooms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        return "Room{" +
                "id_rooms=" + id_rooms +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

}
