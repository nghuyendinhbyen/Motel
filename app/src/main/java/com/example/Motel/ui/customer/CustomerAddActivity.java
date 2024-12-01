package com.example.Motel.ui.customer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.Motel.Adapter.CustomerRepository;
import com.example.Motel.Adapter.RoomRepository;
import com.example.Motel.Adapter.loadImage;
import com.example.Motel.Model.Customer;
import com.example.Motel.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;

public class CustomerAddActivity extends AppCompatActivity {
    private TextInputEditText txtDate;
    private TextInputEditText txtName;
    private TextInputEditText txtPhone;
    private TextInputEditText txtAddress;
    private Button btnSave;
    private RoomRepository roomRepository;
    private CustomerRepository customerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);

        ConstraintLayout constraintLayout = findViewById(R.id.cus_edit);
        String img_bgr = "images/bgr.jpg";
        loadImage.load_Image(this, img_bgr, constraintLayout);

        Spinner spinnerRooms = findViewById(R.id.spinnerRooms);

        txtDate = findViewById(R.id.txtDate);
        btnSave = findViewById(R.id.btnSave);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        roomRepository = new RoomRepository();
        customerRepository = new CustomerRepository();

        txtDate.setOnClickListener(v -> showDatePickerDialog());

        spinnerRooms.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Gọi dữ liệu phòng từ API khi người dùng chạm vào spinner
                roomRepository.getRoom(new RoomRepository.RoomCallback() {
                    @Override
                    public void onSuccess(List<String> rooms) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomerAddActivity.this, android.R.layout.simple_spinner_item, rooms);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerRooms.setAdapter(adapter);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(CustomerAddActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
            return false; // Cho phép sự kiện tiếp tục
        });

        btnSave.setOnClickListener(v -> saveCustomer());
    }

    private void saveCustomer() {
        Spinner spinnerRooms = findViewById(R.id.spinnerRooms);
        String name = txtName.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String date = txtDate.getText().toString().trim();
        String roomName = spinnerRooms.getSelectedItem().toString(); // Lấy tên phòng từ Spinner
        int rooms = Integer.parseInt(roomName); // Chuyển đổi String sang int

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy id_rooms từ tên phòng
        roomRepository.getRoomIdByName(roomName, new RoomRepository.RoomIdCallback() {
            @Override
            public void onSuccess(int roomId) {
                // Tạo đối tượng Customer
                Customer customer = new Customer();
                customer.setName(name);
                customer.setPhone(phone);
                customer.setAddress(address);
                customer.setDate(date);
                customer.setRooms(rooms);
                customer.setIdRooms(roomId); // Lưu id_rooms vào customer

                // Gọi phương thức thêm khách hàng
                customerRepository.addCustomer(customer, new CustomerRepository.AddCustomerCallback() {
                    @Override
                    public void onSuccess(String statusMessage) {
                        Toast.makeText(CustomerAddActivity.this, "Thêm khách hàng thành công: " + statusMessage, Toast.LENGTH_LONG).show();

                        // Cập nhật trạng thái phòng
                        roomRepository.updateRoomStatus(roomId, "Đã thuê", new RoomRepository.idRoomCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Toast.makeText(CustomerAddActivity.this, "Cập nhật trạng thái phòng thành công: " + message, Toast.LENGTH_LONG).show();
                                Intent resultIntent = new Intent();
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish(); // Quay lại màn hình trước
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(CustomerAddActivity.this, "Lỗi cập nhật trạng thái phòng: " + error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(CustomerAddActivity.this, "Lỗi thêm khách hàng: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Toast.makeText(CustomerAddActivity.this, "Lỗi lấy ID phòng: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // Sử dụng SimpleDateFormat để định dạng ngày
                    String formattedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    txtDate.setText(formattedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
