package com.example.Motel.Model;

import com.google.gson.annotations.SerializedName;

public class RoomUpdateRequest {
    @SerializedName("id_rooms")
    private int idRooms;

    @SerializedName("status")
    private String status;

    public RoomUpdateRequest(int idRooms, String status) {
        this.idRooms = idRooms;
        this.status = status;
    }

    // Getter và Setter
    public int getIdRooms() {
        return idRooms;
    }

    public void setIdRooms(int idRooms) {
        this.idRooms = idRooms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
