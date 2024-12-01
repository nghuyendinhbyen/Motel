package com.example.Motel.ui.rooms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.Motel.Adapter.RoomRepository;
import com.example.Motel.Model.Room;

import java.util.List;

public class RoomsViewModel extends ViewModel {

    private final MutableLiveData<List<Room>> roomsList;
    private final RoomRepository repository;

    public RoomsViewModel() {
        repository = new RoomRepository(); // Khởi tạo repository
        roomsList = new MutableLiveData<>();
        loadRooms();
    }

    public LiveData<List<Room>> getRoomsList() {
        return roomsList;
    }

    public void loadRooms() {
        repository.getRooms(new RoomRepository.RoomListCallback() {
            @Override
            public void onSuccess(List<Room> rooms) {
                if (rooms != null && !rooms.isEmpty()) {
                    roomsList.setValue(rooms);
                }
            }
            @Override
            public void onError(String error) {
            }
        });
    }
}
