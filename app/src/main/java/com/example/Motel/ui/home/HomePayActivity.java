package com.example.Motel.ui.home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.Motel.Adapter.CustomerRepository;
import com.example.Motel.Adapter.RoomRepository;
import com.example.Motel.Adapter.loadImage;
import com.example.Motel.Model.Customer;
import com.example.Motel.Model.Pay;
import com.example.Motel.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class HomePayActivity extends AppCompatActivity {
    private TextInputEditText txtDateStart;
    private TextInputEditText txtDateEnd;
    private CustomerRepository customerRepository;
    private TextInputEditText txtRooms;
    private TextInputEditText txtInternet;
    private TextInputEditText txtElectricNumber;
    private TextInputEditText txtWaterNumber;
    private RoomRepository roomRepository;
    private TextInputEditText currentDateField; // Biến lưu trữ trường hiện tại


    private Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pay);

        ConstraintLayout constraintLayout = findViewById(R.id.home_pay);
        String img_bgr = "images/bgr.jpg";
        loadImage.load_Image(this, img_bgr, constraintLayout);

        btnPay = findViewById(R.id.btnPay);
        txtRooms = findViewById(R.id.txtRooms);
        txtInternet = findViewById(R.id.txtInternet);
        txtElectricNumber = findViewById(R.id.txtElectricNumber);
        txtWaterNumber = findViewById(R.id.txtWaterNumber);
        txtDateStart = findViewById(R.id.txtDateStart);
        txtDateEnd = findViewById(R.id.txtDateEnd);
        txtDateStart.setOnClickListener(v -> showDatePickerDialog(txtDateStart));
        txtDateEnd.setOnClickListener(v -> showDatePickerDialog(txtDateEnd));
        customerRepository = new CustomerRepository();
        roomRepository = new RoomRepository();


        Intent intent = getIntent();
        int customerId = intent.getIntExtra("id_cus", -1);

        customerRepository.getCustomerById(customerId, new CustomerRepository.CustomerCallback() {
            @Override
            public void onSuccess(Customer customer) {

                // Kiểm tra dữ liệu từ API
                if (customer != null) {
                    int room = customer.getRooms();  // Lấy giá trị phòng là kiểu int
                    String roomString = String.valueOf(room);
                    txtRooms.setText(roomString);
                }
            }


            @Override
            public void onError(String error) {
            }
        });

        btnPay.setOnClickListener(v -> processPayment(customerId));

    }

    private void processPayment(int customerId) {
        String dateStart = txtDateStart.getText().toString();
        String dateEnd = txtDateEnd.getText().toString();
        String roomName = txtRooms.getText().toString();
        int electricNumber;
        int waterNumber;
        int internet;

        // Kiểm tra xem người dùng đã nhập số điện, nước và Internet hay chưa
        try {
            electricNumber = Integer.parseInt(txtElectricNumber.getText().toString());
            waterNumber = Integer.parseInt(txtWaterNumber.getText().toString());
            internet = Integer.parseInt(txtInternet.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số điện, nước và Internet hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem các trường ngày có được nhập đầy đủ không
        if (dateStart.isEmpty() || dateEnd.isEmpty() || roomName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }



        // Tạo đối tượng Pay và gọi phương thức thanh toán
        Pay pay = new Pay();
        pay.setRoom(Integer.parseInt(roomName)); // Chuyển đổi roomName thành int
        pay.setElectricNumber(electricNumber);
        pay.setWaterNumber(waterNumber);
        pay.setInternet(internet);
        pay.setDateStart(dateStart);
        pay.setDateEnd(dateEnd);


        roomRepository.processPayment(pay, new RoomRepository.PaymentCallback() {
            @Override
            public void onSuccess(String message) { // Chuyển đổi tham số về String
                Toast.makeText(HomePayActivity.this, message, Toast.LENGTH_SHORT).show(); // Thông báo thành công
                finish(); // Quay lại màn hình trước
            }

            @Override
            public void onError(String error) {
                Toast.makeText(HomePayActivity.this, "Lỗi thanh toán: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog(TextInputEditText dateField) {
        currentDateField = dateField; // Lưu trường hiện tại

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // Sử dụng SimpleDateFormat để định dạng ngày
                    String formattedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    currentDateField.setText(formattedDate); // Cập nhật trường hiện tại
                }, year, month, day);
        datePickerDialog.show();
    }

}