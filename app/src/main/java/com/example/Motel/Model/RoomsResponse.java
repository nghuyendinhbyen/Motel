package com.example.Motel.Model;

import java.util.List;

public class RoomsResponse {
    private List<Room> rooms; // Danh sách các phòng

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
