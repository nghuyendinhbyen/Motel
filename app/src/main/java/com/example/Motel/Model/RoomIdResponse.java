package com.example.Motel.Model;

import com.google.gson.annotations.SerializedName;

public class RoomIdResponse {
    @SerializedName("id_rooms")
    private int idRooms;

    public int getIdRooms() {
        return idRooms;
    }
}
