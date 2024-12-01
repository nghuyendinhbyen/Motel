package com.example.Motel.Model;

import java.util.List;

public class RoomResponse {
    private List<String> rooms; // Đúng kiểu dữ liệu của phòng trong JSON

    public List<String> getRoom() {
        return rooms;
    }

    public void setRoom(List<String> rooms) {
        this.rooms = rooms;
    }

}


