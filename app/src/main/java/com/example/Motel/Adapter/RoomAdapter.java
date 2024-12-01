package com.example.Motel.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Motel.Model.Room;
import com.example.Motel.databinding.ItemRoomBinding; // Đảm bảo đường dẫn đúng tới layout

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private List<Room> filteredList; // Danh sách sau khi lọc
    private OnItemClickListener onItemClickListener;

    // Interface truyền id_rooms
    public interface OnItemClickListener {
        void onItemClick(int idRooms); // Truyền id_rooms
    }

    // Constructor
    public RoomAdapter(List<Room> roomList, OnItemClickListener onItemClickListener) {
        this.roomList = roomList;
        this.filteredList = new ArrayList<>(roomList); // Sao chép danh sách ban đầu
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRoomBinding binding = ItemRoomBinding.inflate(inflater, parent, false);
        return new RoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = filteredList.get(position); // Lấy từ danh sách đã lọc
        holder.bind(room); // Gọi phương thức bind để thiết lập dữ liệu và sự kiện nhấn chuột
    }

    @Override
    public int getItemCount() {
        return filteredList != null ? filteredList.size() : 0; // Tránh NullPointerException
    }

    public void setRoomList(List<Room> rooms) {
        this.roomList = rooms;
        this.filteredList = new ArrayList<>(rooms);
        notifyDataSetChanged();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        private final ItemRoomBinding binding;

        public RoomViewHolder(ItemRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Room room) {
            binding.tvRoomName.setText(room.getName()); // Hiển thị tên phòng
            binding.tvRoomStatus.setText(room.getStatus()); // Hiển thị trạng thái phòng

            // Xử lý sự kiện nhấn vào item
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int roomId = room.getId_rooms(); // Lấy id_rooms
                    onItemClickListener.onItemClick(roomId);
                }
            });
        }
    }

    public void filter(String query) {
        if (query.isEmpty()) {
            filteredList.clear();
            filteredList.addAll(roomList); // Nếu không có từ khóa tìm kiếm, hiển thị tất cả phòng
        } else {
            List<Room> tempList = new ArrayList<>();
            for (Room room : roomList) {
                // Tìm kiếm theo tên phòng
                if (room.getName().toLowerCase().contains(query.toLowerCase())) {
                    tempList.add(room);
                }
            }
            filteredList.clear();
            filteredList.addAll(tempList);
        }
        notifyDataSetChanged(); // Cập nhật giao diện sau khi lọc
    }
}
