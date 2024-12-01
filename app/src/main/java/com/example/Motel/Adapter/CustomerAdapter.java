package com.example.Motel.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Motel.Model.Customer;
import com.example.Motel.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private List<Customer> customers;
    private OnItemClickListener onItemClickListener;

    private List<Customer> filteredList; // Danh sách khách hàng đã lọ

    // Interface để xử lý sự kiện nhấp chuột
    public interface OnItemClickListener {
        void onItemClick(int customerId); // Chỉ truyền ID của khách hàng
    }

    // Constructor với listener
    public CustomerAdapter(List<Customer> customers, OnItemClickListener onItemClickListener) {
        this.customers = customers;
        this.onItemClickListener = onItemClickListener;
        this.filteredList = new ArrayList<>(customers);
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer, viewGroup, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder customerViewHolder, int position) {
        Customer customer = filteredList .get(position);
        if (customer == null) {
            return;
        }
        customerViewHolder.tvCustomerId.setText(String.valueOf(customer.getRooms()));
        customerViewHolder.tvCustomerName.setText(customer.getName());
        customerViewHolder.ivCustomerImage.setImageResource(R.drawable.user);

        // Thiết lập sự kiện nhấp chuột
        customerViewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(customer.getIdCus());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (filteredList  != null) ? filteredList .size() : 0;
    }

    public void setCustomerList(List<Customer> customers) {
        this.customers.clear();
        this.customers.addAll(customers);
        filteredList.clear();
        filteredList.addAll(customers);
        notifyDataSetChanged(); // Thông báo adapter để cập nhật dữ liệu
    }
    public void filter(String query) {
        if (query.isEmpty()) {
            filteredList.clear();
            filteredList.addAll(customers); // Nếu không có từ khóa tìm kiếm, hiển thị tất cả
        } else {
            List<Customer> tempList = new ArrayList<>();
            for (Customer customer : customers) {
                // Giả sử bạn tìm kiếm theo tên
                if (customer.getName().toLowerCase().contains(query.toLowerCase())) {
                    tempList.add(customer);
                }
            }
            filteredList.clear();
            filteredList.addAll(tempList);
        }
        notifyDataSetChanged(); // Cập nhật giao diện
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerId, tvCustomerName;
        private ImageView ivCustomerImage;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerId = itemView.findViewById(R.id.tv_customerId);
            tvCustomerName = itemView.findViewById(R.id.tv_customerName);
            ivCustomerImage = itemView.findViewById(R.id.img_cus);
        }
    }
}
