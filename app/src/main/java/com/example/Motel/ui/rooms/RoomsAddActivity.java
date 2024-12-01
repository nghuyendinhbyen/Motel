package com.example.Motel.ui.rooms;

import android.app.Activity;
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

public class RoomsAddActivity extends AppCompatActivity {
    private RoomRepository roomRepository;
    private TextInputEditText txtStatus;
    private TextInputEditText txtName;
    private TextInputEditText txtCategory;
    private TextInputEditText txtPrice;
    private Button btnAdd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_add);

        ConstraintLayout constraintLayout = findViewById(R.id.rooms_add);
        String img_bgr = "images/bgr.jpg";
        loadImage.load_Image(this, img_bgr, constraintLayout);


        roomRepository = new RoomRepository();
        btnAdd = findViewById(R.id.btnAdd);
        txtName = findViewById(R.id.txtName);
        txtStatus = findViewById(R.id.txtStatus);
        txtCategory = findViewById(R.id.txtCategory);
        txtPrice = findViewById(R.id.txtPrice);

        btnAdd.setOnClickListener(view -> {
            Room newRoom = new Room(0, // ID sẽ được tạo tự động bởi cơ sở dữ liệu
                    txtName.getText().toString(),
                    txtCategory.getText().toString(),
                    txtStatus.getText().toString(),
                    txtPrice.getText().toString());

            roomRepository.addRoom(newRoom, new RoomRepository.RoomAddCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(RoomsAddActivity.this, message, Toast.LENGTH_SHORT).show();
                    // Có thể quay lại danh sách phòng hoặc cập nhật giao diện
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish(); // Quay lại màn hình trước
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(RoomsAddActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}