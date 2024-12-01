package com.example.Motel.Adapter;

import com.example.Motel.Api.APIService;
import com.example.Motel.Api.RetrofitClient;
import com.example.Motel.Model.Constants;
import com.example.Motel.Model.Customer;
import com.example.Motel.Model.CustomerListResponse;
import com.example.Motel.Model.CustomerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class CustomerRepository {

    private APIService apiService;

    public CustomerRepository() {
        // Khởi tạo Retrofit và APIService
        apiService = RetrofitClient.getRetrofitInstance(Constants.SERVER_IP).create(APIService.class); // Thay đổi base URL nếu cần
    }

    public void getCustomers(final CustomerListCallback callback) {
        Call<CustomerListResponse> call = apiService.getCustomers();
        call.enqueue(new Callback<CustomerListResponse>() {
            @Override
            public void onResponse(Call<CustomerListResponse> call, Response<CustomerListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getCustomers());
                } else {
                    callback.onError("Không thể lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<CustomerListResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


    public void getCustomerById(int id_cus, final CustomerCallback callback) {
        Call<CustomerResponse> call = apiService.getCustomerById(id_cus);
        call.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Customer customer = response.body().getCustomer();
                    if (customer != null) {
                        callback.onSuccess(customer);
                    } else {
                        callback.onError("Không tìm thấy dữ liệu khách hàng.");
                    }
                } else {
                    callback.onError("Không thể lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void deleteCustomer(int customerId, final CustomerCallback callback) {
        Call<Void> call = apiService.deleteCustomer(customerId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null); // Xóa thành công
                } else {
                    callback.onError("Không thể xóa dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


    public void updateCustomer(Customer customer, final UpdateCustomerCallback callback) {
        Call<CustomerResponse> call = apiService.updateCustomer(customer);
        call.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMessage;
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string(); // Đọc nội dung từ ResponseBody
                        } catch (IOException e) {
                            errorMessage = "Lỗi khi đọc thông báo lỗi: " + e.getMessage();
                        }
                    } else {
                        errorMessage = "Cập nhật thất bại! Không có thông báo lỗi chi tiết.";
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }



    public void addCustomer(Customer customer, final AddCustomerCallback callback) {
        Call<Void> call = apiService.addCustomer(customer);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Thêm khách hàng thành công"); // Truyền thông điệp
                } else {
                    callback.onError("Lỗi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Lỗi: " + t.getMessage());
            }
        });
    }


    public interface AddCustomerCallback {
        void onSuccess(String message); // Thêm tham số message
        void onError(String error); // Gọi khi có lỗi xảy ra
    }



    public interface UpdateCustomerCallback {
        void onSuccess(CustomerResponse customerResponse);
        void onError(String errorMessage);
    }





    public interface CustomerCallback {
        void onSuccess(Customer customer);
        void onError(String error);
    }

    // Giao diện callback cho danh sách khách hàng
    public interface CustomerListCallback {
        void onSuccess(List<Customer> customers);
        void onError(String error);
    }
}
