package com.example.Motel.ui.pay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Motel.Adapter.PayAdapter;
import com.example.Motel.Api.APIService;
import com.example.Motel.Model.Constants;
import com.example.Motel.Model.Pay;
import com.example.Motel.R;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PayFragment extends Fragment {

    private RecyclerView recyclerView;
    private PayAdapter payAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);

        recyclerView = view.findViewById(R.id.rcv_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPayData();

        return view;
    }

    private void loadPayData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_IP) // Địa chỉ IP của server
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService payService = retrofit.create(APIService.class);
        Call<List<Pay>> call = payService.getPayData();

        call.enqueue(new Callback<List<Pay>>() {
            @Override
            public void onResponse(Call<List<Pay>> call, Response<List<Pay>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pay> payList = response.body();
                    payAdapter = new PayAdapter(getContext(), payList);
                    recyclerView.setAdapter(payAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Pay>> call, Throwable t) {
                // Xử lý lỗi khi gọi API
            }
        });
    }
}
