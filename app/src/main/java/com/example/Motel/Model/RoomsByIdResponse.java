package com.example.Motel.Model;

public class RoomsByIdResponse {
    private Room room; // Đối tượng Room

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "RoomsByIdResponse{" +
                "room=" + room +
                '}';
    }

}
