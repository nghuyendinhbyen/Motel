package com.example.Motel.ui.rooms;

import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.Motel.Adapter.RoomRepository;
import com.example.Motel.Adapter.loadImage;
import com.example.Motel.Model.Room;
import com.example.Motel.R;
import com.google.android.material.textfield.TextInputEditText;

public class RoomsEditActivity extends AppCompatActivity {
    private TextInputEditText txtStatus;
    private TextInputEditText txtName;
    private TextInputEditText txtCategory;
    private TextInputEditText txtPrice;
    private Button btnDel;
    private Button btnUpdate;
    private RoomRepository roomRepository;
    private int roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_edit);

        btnDel = findViewById(R.id.btnDel);
        btnUpdate = findViewById(R.id.btnUpdate);
        txtName = findViewById(R.id.txtName);
        txtStatus = findViewById(R.id.txtStatus);
        txtCategory = findViewById(R.id.txtCategory);
        txtPrice = findViewById(R.id.txtPrice);
        roomRepository = new RoomRepository();

        ConstraintLayout constraintLayout = findViewById(R.id.rooms_edit);
        String img_bgr = "images/bgr.jpg";
        loadImage.load_Image(this, img_bgr, constraintLayout);

        // Lấy id_rooms từ Intent
        Intent intent = getIntent();
        roomId = intent.getIntExtra("id_rooms", -1);

        // Kiểm tra xem roomId có hợp lệ không
        if (roomId != -1) {
            loadRoomData(roomId);
        } else {
            Toast.makeText(this, "ID phòng không hợp lệ", Toast.LENGTH_SHORT).show();
        }

        // Gọi phương thức xóa khi nhấn vào nút Xóa
        btnDel.setOnClickListener(v -> {
            deleteRoom(roomId);
        });

        btnUpdate.setOnClickListener(view -> {
            // Tạo đối tượng Room từ thông tin người dùng nhập
            updateoom(roomId);
        });
    }

    private void loadRoomData(int roomId) {
        roomRepository.getRoomById(roomId, new RoomRepository.RoomByIdCallback() {
            @Override
            public void onSuccess(Room room) {
                if (room != null) {
                    txtName.setText(room.getName());
                    txtStatus.setText(room.getStatus());
                    txtCategory.setText(room.getCategory());
                    txtPrice.setText(room.getPrice());
                } else {
                    Toast.makeText(RoomsEditActivity.this, "Không tìm thấy phòng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(RoomsEditActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm xóa phòng
    private void deleteRoom(int roomId) {
        if (roomId != -1) {
            roomRepository.deleteRoomById(roomId, new RoomRepository.DeleteRoomCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(RoomsEditActivity.this, "Phòng đã được xóa", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Quay lại màn hình trước sau khi xóa thành công
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(RoomsEditActivity.this, "Lỗi khi xóa: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "ID phòng không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateoom(int roomId){
        Room updatedRoom = new Room(roomId,
                txtName.getText().toString(),
                txtCategory.getText().toString(),
                txtStatus.getText().toString(),
                txtPrice.getText().toString());

        // Gọi phương thức updateRoom để cập nhật thông tin phòng
        roomRepository.updateRoom(updatedRoom, new RoomRepository.RoomUpdateCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(RoomsEditActivity.this, message, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
                // Có thể quay lại màn hình trước đó hoặc cập nhật UI
            }

            @Override
            public void onError(String error) {
                Toast.makeText(RoomsEditActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
