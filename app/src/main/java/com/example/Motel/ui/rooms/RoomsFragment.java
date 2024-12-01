package com.example.Motel.ui.rooms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Motel.Adapter.RoomAdapter;
import com.example.Motel.R;
import com.example.Motel.databinding.FragmentRoomsBinding;
import com.example.Motel.Model.Room; // Đảm bảo đã nhập đúng model Room

import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment {
    private static final int EDIT_ROOMS_REQUEST_CODE = 1;
    private static final int ADD_ROOMS_REQUEST_CODE = 1;
    private FragmentRoomsBinding binding;
    private RoomsViewModel roomsViewModel;
    private RoomAdapter roomAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setHasOptionsMenu(true);

        // Khởi tạo ViewModel
        roomsViewModel = new ViewModelProvider(this).get(RoomsViewModel.class);

        // Khởi tạo RecyclerView
        RecyclerView recyclerView = binding.rcvCus;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo Adapter và thiết lập cho RecyclerView


        // Quan sát dữ liệu từ ViewModel
        roomsViewModel.getRoomsList().observe(getViewLifecycleOwner(), this::updateRoomList);


        roomAdapter = new RoomAdapter(new ArrayList<>(), idRooms -> {
            // Khi nhấn vào phần tử, mở RoomsEditActivity
            Intent intent = new Intent(getActivity(), RoomsEditActivity.class);
            intent.putExtra("id_rooms", idRooms); // Truyền id_rooms vào Intent
            startActivityForResult(intent, EDIT_ROOMS_REQUEST_CODE); // Gọi startActivityForResult
        });
        recyclerView.setAdapter(roomAdapter);

        roomsViewModel.getRoomsList().observe(getViewLifecycleOwner(), this::updateRoomList);

        ImageView imageView2 = binding.btnAddRoom; // Sử dụng binding
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RoomsAddActivity.class);
                startActivityForResult(intent, ADD_ROOMS_REQUEST_CODE);
            }
        });

        refreshData();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ROOMS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Tải lại danh sách khách hàng từ ViewModel
            refreshData();
        } else if (requestCode == ADD_ROOMS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Nếu bạn muốn tải lại danh sách khi thêm khách hàng
            refreshData();
        }
    }


    private void updateRoomList(List<Room> rooms) {
        if (rooms != null) {
            roomAdapter.setRoomList(rooms);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Giải phóng binding
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu); // Thêm menu từ file
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCustomers(newText); // Gọi phương thức filter trong CustomerFragment
                return true;
            }
        });
    }

    public void filterCustomers(String query) {
        if (roomAdapter != null) {
            roomAdapter.filter(query); // Gọi phương thức filter trong adapter
        }
    }

    public void refreshData() {
        roomsViewModel.loadRooms(); // Giả sử bạn có phương thức này để tải lại dữ liệu
    }
}
