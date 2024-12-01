package com.example.Motel.Api;

import com.example.Motel.Model.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface APIService {
    // Phương thức GET để lấy danh sách người dùng
    @GET("motel/getData.php")
    Call<UserResponse> getUsers();  // Đổi kiểu trả về thành UserResponse

    @POST("motel/getData.php")
    Call<User> login(@Body LoginRequest loginRequest);

    // Phương thức POST để đăng ký người dùng mới
    @POST("motel/getData.php")
    Call<User> register(@Body User user);

    @POST("motel/addCus.php") // Đường dẫn đến file PHP
    Call<Void> addCustomer(@Body Customer customer);

    @GET("motel/getData.php")
    Call<CustomerListResponse> getCustomers();

    @GET("motel/getData.php")
    Call<CustomerResponse> getCustomerById(@Query("id") int id_cus);

    @GET("motel/getData.php")
    Call<RoomIdResponse> getRoomIdByName(@Query("room_name") String roomName);
    @GET("motel/getRoomsById.php") // Đường dẫn API để lấy phòng theo ID
    Call<RoomsByIdResponse> getRoomById(@Query("id_rooms") int id_rooms); // Phương thức lấy phòng theo ID

    @GET("motel/getData.php")
    Call<RoomResponse> getRoom();
    @DELETE("motel/getData.php")
    Call<Void> deleteCustomer(@Query("id") int id_cus);

    @POST("motel/updateStatus.php")
    Call<Void> updateRoomStatus(@Body RoomUpdateRequest request);

    @POST("motel/updateCus.php")
    Call<CustomerResponse> updateCustomer(@Body Customer customer);

    @GET("motel/get_rooms.php") // Thay bằng đường dẫn API đúng
    Call<List<Room>> getRooms();

    @DELETE("motel/deleteRooms.php")
    Call<Void> deleteRoomById(@Query("id_rooms") int id_rooms);

    @POST("motel/updateRooms.php")
    Call<Void> updateRoom(@Body Room room);

    @POST("motel/addRooms.php")
    Call<Void> addRoom(@Body Room room);

    @POST("motel/pay.php")
    Call<Void> processPayment(@Body Pay pay);

    @GET("motel/getPayData.php")
    Call<List<Pay>> getPayData();

    @GET("motel/getPayDataById.php")
    Call<Pay> getPayDataById(@Query("id_pay") int idPay);

    @POST("motel/updatePaymentStatus.php") // Đường dẫn đến API PHP
    Call<Void> updatePayStatus(@Body PayStatusRequest request);


}
