package com.example.Motel.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.Motel.Model.Customer;
import com.example.Motel.Adapter.CustomerRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<Customer>> customerList;
    private final CustomerRepository repository;

    public HomeViewModel() {
        repository = new CustomerRepository(); // Khởi tạo repository
        customerList = new MutableLiveData<>();
        loadCustomers();
    }

    public LiveData<List<Customer>> getCustomerList() {
        return customerList;
    }

    public void loadCustomers() {
        repository.getCustomers(new CustomerRepository.CustomerListCallback() {
            @Override
            public void onSuccess(List<Customer> customers) {
                if (customers != null && !customers.isEmpty()) {
                    customerList.setValue(customers);
                }

            }

            @Override
            public void onError(String error) {
            }
        });
    }

}
