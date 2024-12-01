package com.example.Motel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Motel.Api.APIService;
import com.example.Motel.Api.RetrofitClient;
import com.example.Motel.Model.Constants;
import com.example.Motel.Model.User;
import com.example.Motel.Adapter.loadImage;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private TextInputLayout tilUsername, tilPassword, tilConfirmPassword;
    private TextInputEditText txtUsername, txtPassword, txtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView imageView = findViewById(R.id.imageView);
        String imagePath = "images/img_home.webp";
        loadImage.load_Image(this, imagePath, imageView);

        LinearLayout layout = findViewById(R.id.register_layout);
        String img_bgr = "images/bgr.jpg";
        loadImage.load_Image(this, img_bgr, layout);

        // Khởi tạo TextInputLayout và TextInputEditText
        tilUsername = findViewById(R.id.til_Username);
        txtUsername = findViewById(R.id.txtUsername);
        tilPassword = findViewById(R.id.til_Pass);
        txtPassword = findViewById(R.id.txtPassword);
        tilConfirmPassword = findViewById(R.id.til_ConfirmPass);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);

        Button btnRegister = findViewById(R.id.btn_register);

        // Thêm TextWatcher để theo dõi thay đổi trong mật khẩu và xác nhận mật khẩu
        setupTextWatcher(txtPassword, tilPassword, "Chưa nhập mật khẩu");
        setupTextWatcher(txtConfirmPassword, tilConfirmPassword, "Chưa nhập xác nhận mật khẩu");
        setupTextWatcher(txtUsername, tilUsername, "Chưa nhập tên người dùng");

        // Khởi tạo Retrofit instance
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(Constants.SERVER_IP);
        APIService apiService = retrofit.create(APIService.class);

        btnRegister.setOnClickListener(v -> {
            String username = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String confirmPassword = txtConfirmPassword.getText().toString().trim();

            // Kiểm tra tên người dùng đã được nhập
            if (username.isEmpty()) {
                tilUsername.setError("Chưa nhập tên người dùng");
                tilUsername.setBoxStrokeColor(getResources().getColor(R.color.red));
                return;
            } else {
                tilUsername.setError(null);
                tilUsername.setBoxStrokeColor(getResources().getColor(R.color.defaultHintColor));
            }

            // Kiểm tra mật khẩu đã được nhập
            if (password.isEmpty()) {
                tilPassword.setError("Chưa nhập mật khẩu");
                tilPassword.setBoxStrokeColor(getResources().getColor(R.color.red));
                return;
            } else {
                tilPassword.setError(null);
                tilPassword.setBoxStrokeColor(getResources().getColor(R.color.defaultHintColor));
            }

            // Kiểm tra xác nhận mật khẩu
            if (confirmPassword.isEmpty()) {
                tilConfirmPassword.setError("Chưa nhập xác nhận mật khẩu");
                tilConfirmPassword.setBoxStrokeColor(getResources().getColor(R.color.red));
                return;
            } else {
                tilConfirmPassword.setError(null);
                tilConfirmPassword.setBoxStrokeColor(getResources().getColor(R.color.defaultHintColor));
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng User
            User newUser = new User();
            newUser.setName(username);
            newUser.setPass(password);

            // Gửi yêu cầu đăng ký
            Call<User> call = apiService.register(newUser);  // Giả sử API có phương thức register
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User registeredUser = response.body();
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void setupTextWatcher(TextInputEditText editText, TextInputLayout textInputLayout, String errorMessage) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    textInputLayout.setError(errorMessage);
                    textInputLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
                } else {
                    textInputLayout.setError(null);
                    textInputLayout.setBoxStrokeColor(getResources().getColor(R.color.defaultHintColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
