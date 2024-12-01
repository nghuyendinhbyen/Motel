package com.example.Motel.ui.pay;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.Motel.Adapter.loadImage;
import com.example.Motel.Model.Pay;
import com.example.Motel.R;
import com.example.Motel.Adapter.RoomRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SentMailPayActivity extends AppCompatActivity {
    private Button btnSend;
    private TextInputEditText txtGmail; // TextInputEditText để nhập địa chỉ email người nhận
    private Pay payInfo; // Để lưu thông tin Pay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_mail_pay);

        btnSend = findViewById(R.id.btnSend);
        txtGmail = findViewById(R.id.txtGmail);

        ConstraintLayout constraintLayout = findViewById(R.id.sent_pay);
        String img_bgr = "images/bgr.jpg";
        loadImage.load_Image(this, img_bgr, constraintLayout);

        // Lấy thông tin Pay từ Intent
        int idPay = getIntent().getIntExtra("id_pay", -1);
        getPayDataByID(idPay); // Gọi phương thức để lấy dữ liệu thanh toán

        // Thiết lập sự kiện cho nút Send
        btnSend.setOnClickListener(v -> {
            if (payInfo != null) {
                // Gọi hàm gửi email với thông tin từ đối tượng Pay
                String subject = "Thông tin thanh toán";
                String message = createEmailMessage(payInfo);

                // Lấy địa chỉ email người nhận từ TextInputEditText
                String recipientEmail = txtGmail.getText().toString().trim();

                if (!recipientEmail.isEmpty()) {
                    sendEmail(recipientEmail, subject, message);
                } else {
                    Toast.makeText(this, "Vui lòng nhập địa chỉ email người nhận.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng kiểm tra thông tin thanh toán.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPayDataByID(int idPay) {
        RoomRepository roomRepository = new RoomRepository();
        roomRepository.getPayDataByID(idPay, new RoomRepository.PayIdCallback() {
            @Override
            public void onSuccess(Pay pay) {
                payInfo = pay; // Lưu thông tin Pay vào biến payInfo
            }

            @Override
            public void onError(String error) {
                Toast.makeText(SentMailPayActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String createEmailMessage(Pay pay) {
        return "Thông tin thanh toán:\n" +
                "Phòng: " + pay.getRoom() + "\n" +
                "Số điện: " + pay.getElectricNumber() + "\n" +
                "Số nước: " + pay.getWaterNumber() + "\n" +
                "Internet: " + pay.getInternet() + "\n" +
                "Ngày bắt đầu: " + pay.getDateStart() + "\n" +
                "Ngày kết thúc: " + pay.getDateEnd() + "\n" +
                "Giá: " + pay.getPrice();
    }

    private void sendEmail(String recipientEmail, String subject, String messageText) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            final String username = "chickeko2@gmail.com"; // Thay đổi thành email của bạn
            final String password = "smam zmsz jwdq trtu"; // Thay đổi thành mật khẩu ứng dụng của bạn

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject(subject);
                message.setText(messageText);
                Transport.send(message);

                runOnUiThread(() -> {
                    Toast.makeText(SentMailPayActivity.this, "Email đã được gửi thành công.", Toast.LENGTH_SHORT).show();
                });
            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(SentMailPayActivity.this, "Gửi email thất bại.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


}
