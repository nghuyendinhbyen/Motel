package com.example.Motel.ui.customer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.MotionEvent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.Motel.Adapter.CustomerRepository;
import com.example.Motel.Adapter.RoomRepository;
import com.example.Motel.Adapter.loadImage;
import com.example.Motel.Model.Customer;
import com.example.Motel.Model.CustomerResponse;
import com.example.Motel.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;

public class CustomerEditActivity extends AppCompatActivity {
    private TextInputEditText txtDate;
    private TextInputEditText txtName;
    private TextInputEditText txtPhone;
    private TextInputEditText txtAddress;
    private Button btnDel;
    private Button btnUpdate;
    private CustomerRepository customerRepository;
    private RoomRepository roomRepository;

    private int roomId; // Biến để lưu ID phòng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit);

        ConstraintLayout constraintLayout = findViewById(R.id.cus_edit);
        String img_bgr = "images/bgr.jpg";
        loadImage.load_Image(this, img_bgr, constraintLayout);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        int customerId = intent.getIntExtra("id_cus", -1);

        Spinner spinnerRooms = findViewById(R.id.spinnerRooms);

        btnDel = findViewById(R.id.btnDel);
        btnUpdate = findViewById(R.id.btnUpdate);
        txtDate = findViewById(R.id.txtDate);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        customerRepository = new CustomerRepository();
        roomRepository = new RoomRepository();
        txtDate.setOnClickListener(v -> showDatePickerDialog());

        if (customerId != -1) {
            customerRepository.getCustomerById(customerId, new CustomerRepository.CustomerCallback() {
                @Override
                public void onSuccess(Customer customer) {
                    // Hiển thị thông tin khách hàng lên giao diện
                    if (customer != null) {
                        txtName.setText(customer.getName());
                        txtPhone.setText(customer.getPhone());
                        txtAddress.setText(customer.getAddress());
                        txtDate.setText(customer.getDate());
                        roomId = customer.getIdRooms(); // Lưu ID phòng

                        int room = customer.getRooms();
                        String roomString = String.valueOf(room);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomerEditActivity.this, android.R.layout.simple_spinner_item, new String[]{roomString});
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerRooms.setAdapter(adapter);
                    } else {
                        // Hiển thị thông báo lỗi khi dữ liệu khách hàng null
                        Toast.makeText(CustomerEditActivity.this, "Dữ liệu khách hàng không tồn tại.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error) {
                    // Hiển thị thông báo lỗi khi lấy dữ liệu khách hàng thất bại
                    Toast.makeText(CustomerEditActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Hiển thị thông báo lỗi khi ID khách hàng không hợp lệ
            Toast.makeText(this, "ID khách hàng không hợp lệ", Toast.LENGTH_SHORT).show();
        }

        spinnerRooms.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Nếu dữ liệu phòng chưa được tải, tải dữ liệu phòng từ API
                if (spinnerRooms.getAdapter().getCount() <= 1) { // Kiểm tra nếu chỉ có item mặc định
                    roomRepository.getRoom(new RoomRepository.RoomCallback() {
                        @Override
                        public void onSuccess(List<String> rooms) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomerEditActivity.this, android.R.layout.simple_spinner_item, rooms);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerRooms.setAdapter(adapter);
                        }

                        @Override
                        public void onError(String error) {
                            // Hiển thị thông báo lỗi khi lấy dữ liệu phòng thất bại
                            Toast.makeText(CustomerEditActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            return false; // Cho phép sự kiện tiếp tục
        });

        btnDel.setOnClickListener(v -> {
            if (customerId != -1) {
                customerRepository.deleteCustomer(customerId, new CustomerRepository.CustomerCallback() {
                    @Override
                    public void onSuccess(Customer customer) {
                        // Thông báo xóa khách hàng thành công
                        Toast.makeText(CustomerEditActivity.this, "Khách hàng đã được xóa thành công.", Toast.LENGTH_SHORT).show();

                        // Cập nhật trạng thái phòng thành trống
                        roomRepository.updateRoomStatus(roomId, "Trống", new RoomRepository.idRoomCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Toast.makeText(CustomerEditActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent resultIntent = new Intent();
                                setResult(RESULT_OK, resultIntent);
                                finish(); // Quay lại màn hình trước
                            }

                            @Override
                            public void onError(String error) {
                                // Hiển thị thông báo lỗi khi cập nhật trạng thái phòng
                                Toast.makeText(CustomerEditActivity.this, "Cập nhật trạng thái phòng thất bại: " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        // Thông báo lỗi khi xóa khách hàng thất bại
                        Toast.makeText(CustomerEditActivity.this, "Xóa khách hàng thất bại: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Thông báo lỗi khi ID khách hàng không hợp lệ
                Toast.makeText(this, "ID khách hàng không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdate.setOnClickListener(v -> {
            // Lấy thông tin từ các trường nhập liệu
            String name = txtName.getText().toString().trim();
            String phone = txtPhone.getText().toString().trim();
            String address = txtAddress.getText().toString().trim();
            String date = txtDate.getText().toString().trim();
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                return;
            }

            String newRoomName = spinnerRooms.getSelectedItem() != null ? spinnerRooms.getSelectedItem().toString() : null; // Tên phòng mới
            int roomName = Integer.parseInt(newRoomName); // Chuyển đổi String sang int
            // Tạo đối tượng Customer để cập nhật
            Customer customer = new Customer();
            customer.setIdCus(customerId);
            customer.setName(name);
            customer.setPhone(phone);
            customer.setAddress(address);
            customer.setRooms(roomName);
            customer.setDate(date);

            // Lấy ID phòng mới từ tên phòng
            roomRepository.getRoomIdByName(newRoomName, new RoomRepository.RoomIdCallback() {
                @Override
                public void onSuccess(int newRoomId) {
                    customer.setIdRooms(newRoomId);

                    // Cập nhật thông tin khách hàng
                    customerRepository.updateCustomer(customer, new CustomerRepository.UpdateCustomerCallback() {
                        @Override
                        public void onSuccess(CustomerResponse response) {
                            // Nếu cập nhật thành công, tiếp tục cập nhật trạng thái phòng
                            roomRepository.updateRoomStatus(roomId, "Trống", new RoomRepository.idRoomCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    // Cập nhật trạng thái phòng mới
                                    roomRepository.updateRoomStatus(newRoomId, "Đã thuê", new RoomRepository.idRoomCallback() {
                                        @Override
                                        public void onSuccess(String message) {
                                            Toast.makeText(CustomerEditActivity.this, "Cập nhật trạng thái phòng thành công", Toast.LENGTH_SHORT).show();
                                            setResult(RESULT_OK);
                                            finish();
                                        }

                                        @Override
                                        public void onError(String error) {
                                            // Hiển thị thông báo lỗi khi cập nhật trạng thái phòng mới
                                            Toast.makeText(CustomerEditActivity.this, "Lỗi cập nhật trạng thái phòng mới: " + error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onError(String error) {
                                    // Hiển thị thông báo lỗi khi cập nhật trạng thái phòng cũ
                                    Toast.makeText(CustomerEditActivity.this, "Lỗi cập nhật trạng thái phòng cũ: " + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            // Hiển thị thông báo lỗi khi cập nhật khách hàng thất bại
                            Toast.makeText(CustomerEditActivity.this, "Cập nhật khách hàng thất bại: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    // Hiển thị thông báo lỗi khi không lấy được ID phòng mới
                    Toast.makeText(CustomerEditActivity.this, "Lỗi lấy ID phòng: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CustomerEditActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    txtDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}
