package com.example.Motel.ui.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Motel.Adapter.CustomerAdapter;
import com.example.Motel.Adapter.CustomerRepository;
import com.example.Motel.R;
import com.example.Motel.databinding.FragmentCustomerBinding;
import com.example.Motel.ui.home.HomeViewModel;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class CustomerFragment extends Fragment {

    private static final int EDIT_CUSTOMER_REQUEST_CODE = 1; // Request code for starting CustomerEditActivity
    private static final int ADD_CUSTOMER_REQUEST_CODE = 1;
    private FragmentCustomerBinding binding;
    private CustomerViewModel customerViewModelViewModel;
    private HomeViewModel homeViewModelViewModel;
    private CustomerAdapter customerAdapter;
    private CustomerRepository customerRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Thiết lập ViewBinding
        binding = FragmentCustomerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        // Khởi tạo ViewModel và CustomerRepository
        customerViewModelViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        homeViewModelViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        customerRepository = new CustomerRepository(); // Khởi tạo CustomerRepository

        ImageView imageView2 = binding.btnAddCus; // Sử dụng binding
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CustomerAddActivity.class);
                startActivityForResult(intent, ADD_CUSTOMER_REQUEST_CODE);
            }
        });

        // Thiết lập RecyclerView
        RecyclerView recyclerView = binding.rcvCus;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo Adapter và thiết lập sự kiện nhấp chuột
        customerAdapter = new CustomerAdapter(new ArrayList<>(), id_cus -> {
            // Mở CustomerEditActivity khi nhấn vào phần tử
            Intent intent = new Intent(getActivity(), CustomerEditActivity.class);
            intent.putExtra("id_cus", id_cus); // Sử dụng id_cus
            startActivityForResult(intent, EDIT_CUSTOMER_REQUEST_CODE); // Gọi startActivityForResult
        });
        recyclerView.setAdapter(customerAdapter);

        // Quan sát dữ liệu từ ViewModel và cập nhật Adapter
        customerViewModelViewModel.getCustomerList().observe(getViewLifecycleOwner(), customers -> {
            if (customers != null && !customers.isEmpty()) {
                customerAdapter.setCustomerList(customers);
            }
        });
        refreshData();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_CUSTOMER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Tải lại danh sách khách hàng từ ViewModel
            refreshData();
        } else if (requestCode == ADD_CUSTOMER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Nếu bạn muốn tải lại danh sách khi thêm khách hàng
            refreshData();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu); // Thêm menu từ file
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCustomers(newText); // Gọi phương thức filter trong CustomerFragment
                return true;
            }
        });
    }

    public void refreshData() {
        customerViewModelViewModel.loadCustomers(); // Giả sử bạn có phương thức này để tải lại dữ liệu
    }

    public void filterCustomers(String query) {
        if (customerAdapter != null) {
            customerAdapter.filter(query); // Gọi phương thức filter trong adapter
        }
    }
}
