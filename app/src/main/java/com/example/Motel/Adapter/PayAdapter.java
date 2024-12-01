    package com.example.Motel.Adapter;

    import android.content.Context;
    import android.content.Intent;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.ImageView;
    import android.widget.Spinner;
    import android.widget.TextView;
    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;
    import com.example.Motel.Api.APIService;
    import com.example.Motel.Api.RetrofitClient;
    import com.example.Motel.Model.Constants;
    import com.example.Motel.Model.Pay;
    import com.example.Motel.Model.PayStatusRequest;
    import com.example.Motel.R;
    import com.example.Motel.ui.pay.SentMailPayActivity;

    import java.util.ArrayList;
    import java.util.List;

    public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {

        private Context context;
        private List<Pay> payList;

        public PayAdapter(Context context, List<Pay> payList) {
            this.context = context;
            this.payList = payList;
        }

        @NonNull
        @Override
        public PayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pay, parent, false);
            return new PayViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PayViewHolder holder, int position) {
            Pay pay = payList.get(position);
            holder.txtRoom.setText(String.valueOf(pay.getRoom()));
            holder.txtPrice.setText(String.valueOf(pay.getPrice()));

            // Set the image from drawable
            holder.ivCustomerImage.setImageResource(R.drawable.credit_card);
            String status = pay.getStatus();

            // Lưu trạng thái ban đầu
            final String initialStatus = status;

            List<String> statusList = new ArrayList<>();
            statusList.add("Đã thanh toán");
            statusList.add("Chưa thanh toán");

            // Tạo ArrayAdapter cho Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, statusList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Gán Adapter cho Spinner
            holder.spinnerPay.setAdapter(adapter);

            // Chọn giá trị status trong Spinner
            if (status.equals("Đã thanh toán")) {
                holder.spinnerPay.setSelection(0); // Chọn "Đã thanh toán"
            } else if (status.equals("Chưa thanh toán")) {
                holder.spinnerPay.setSelection(1); // Chọn "Chưa thanh toán"
            }

            holder.spinnerPay.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                    String selectedStatus = statusList.get(position);

                    // Chỉ gửi yêu cầu cập nhật nếu trạng thái thay đổi
                    if (!selectedStatus.equals(initialStatus)) {
                        sendStatusToServer(pay.getId_pay(), selectedStatus);
                    }
                }

                @Override
                public void onNothingSelected(android.widget.AdapterView<?> parent) {
                    // Không cần xử lý
                }
            });

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, SentMailPayActivity.class);
                intent.putExtra("id_pay", pay.getId_pay()); // Gửi id_pay sang SentMailPayActivity
                context.startActivity(intent);
            });
        }


        private void sendStatusToServer(int idPay, String status) {
            // Khởi tạo đối tượng PayStatusRequest
            PayStatusRequest request = new PayStatusRequest(idPay, status);


            Log.d("API Request", "Gửi yêu cầu cập nhật trạng thái: ID_PAY = " + idPay + ", Status = " + status);

            // Sử dụng Retrofit để gọi API
            APIService apiService = RetrofitClient.getRetrofitInstance(Constants.SERVER_IP).create(APIService.class);
            apiService.updatePayStatus(request).enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Xử lý khi cập nhật thành công
                        android.widget.Toast.makeText(context, "Trạng thái đã được cập nhật!", android.widget.Toast.LENGTH_SHORT).show();
                        Log.d("API Response", "Cập nhật thành công. Mã phản hồi: " + response.code());
                    } else {
                        // Log chi tiết khi API trả về lỗi
                        String errorMessage = "Cập nhật thất bại. Mã lỗi: " + response.code() + " - " + response.message();
                        android.widget.Toast.makeText(context, errorMessage, android.widget.Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "Cập nhật thất bại. Mã lỗi: " + response.code() + " - " + response.message());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    // Xử lý khi xảy ra lỗi kết nối
                    android.widget.Toast.makeText(context, "Lỗi: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return payList.size();
        }

        public class PayViewHolder extends RecyclerView.ViewHolder {
            TextView txtRoom, txtPrice;
            ImageView ivCustomerImage;
            Spinner spinnerPay; // Khai báo Spinner

            public PayViewHolder(@NonNull View itemView) {
                super(itemView);
                txtRoom = itemView.findViewById(R.id.tv_customerIdd);
                txtPrice = itemView.findViewById(R.id.tv_customerNamee);
                ivCustomerImage = itemView.findViewById(R.id.img_cuss);
                spinnerPay = itemView.findViewById(R.id.spinnerPay);

            }
        }
    }
