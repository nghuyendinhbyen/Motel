package com.example.Motel.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Motel.Adapter.CustomerAdapter;
import com.example.Motel.R;
import com.example.Motel.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private CustomerAdapter customerAdapter;
    private FragmentHomeBinding binding;
    private static final int HOME_PAY_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Thiết lập ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Khởi tạo ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Thiết lập RecyclerView
        RecyclerView recyclerView = binding.rcvItem;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        customerAdapter = new CustomerAdapter(new ArrayList<>(), id_cus -> {
            // Mở CustomerEditActivity khi nhấn vào phần tử
            Intent intent = new Intent(getActivity(), HomePayActivity.class);
            intent.putExtra("id_cus", id_cus); // Sử dụng id_cus
            startActivityForResult(intent, HOME_PAY_REQUEST_CODE); // Gọi startActivityForResult
        });
        recyclerView.setAdapter(customerAdapter);


        // Quan sát dữ liệu từ ViewModel và cập nhật Adapter
        homeViewModel.getCustomerList().observe(getViewLifecycleOwner(), customers -> {
            if (customers != null && !customers.isEmpty()) {
                customerAdapter.setCustomerList(customers);
            } else {
            }
        });

        // Tải lại dữ liệu khi fragment được tạo
        refreshData();

        return root;
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

    public void filterCustomers(String query) {
        if (customerAdapter != null) {
            customerAdapter.filter(query); // Gọi phương thức filter trong adapter
        }

    }

    public void refreshData() {
        homeViewModel.loadCustomers(); // Giả sử bạn có phương thức này để tải lại dữ liệu
    }
}
