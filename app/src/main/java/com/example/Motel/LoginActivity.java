package com.example.Motel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Motel.Api.APIService;
import com.example.Motel.Api.RetrofitClient;
import com.example.Motel.Model.Constants;
import com.example.Motel.Model.LoginRequest;
import com.example.Motel.Model.User;
import com.example.Motel.Model.UserResponse;
import com.example.Motel.Adapter.loadImage;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameEditText = findViewById(R.id.editTextTextPhone);
        TextInputLayout passwordInputLayout = findViewById(R.id.til_Pass); // TextInputLayout cho mật khẩu
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);
        Button loginButton = findViewById(R.id.button);
        TextView txtRegister = findViewById(R.id.txt_register);

        ImageView imageView = findViewById(R.id.imageView);
        String imagePath = "images/img_home.webp";
        loadImage.load_Image(this, imagePath, imageView);

        LinearLayout layout = findViewById(R.id.login_layout);
        String img_bgr = "images/bgr.jpg";
        loadImage.load_Image(this, img_bgr, layout);

        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(Constants.SERVER_IP);
        APIService apiService = retrofit.create(APIService.class);

        // Thêm TextWatcher để theo dõi thay đổi trong mật khẩu
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    // Hiển thị lỗi cho TextInputLayout và thay đổi màu viền sang đỏ
                    passwordInputLayout.setError("Chưa nhập mật khẩu");
                    passwordInputLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
                } else {
                    // Xóa lỗi nếu mật khẩu đã được nhập
                    passwordInputLayout.setError(null);
                    passwordInputLayout.setBoxStrokeColor(getResources().getColor(R.color.defaultHintColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Kiểm tra nhập tên người dùng
            if (username.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập tên người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu đã được nhập ở đây không cần lặp lại kiểm tra màu viền
            if (password.isEmpty()) {
                // Hiển thị lỗi cho TextInputLayout và thay đổi màu viền sang đỏ
                passwordInputLayout.setError("Chưa nhập mật khẩu");
                passwordInputLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
                return;
            }

            LoginRequest loginRequest = new LoginRequest(username, password);

            // Gửi yêu cầu GET để lấy danh sách người dùng
            Call<UserResponse> call = apiService.getUsers();
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<User> users = response.body().getAccounts();  // Lấy danh sách từ UserResponse
                        boolean isValidUser = false;
                        for (User user : users) {
                            if (username.equals(user.getName()) && password.equals(user.getPass())) {
                                isValidUser = true;
                                break;
                            }
                        }
                        if (isValidUser) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Log.e(TAG, "Error: " + t.getMessage());
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
