package com.example.Motel.Adapter;

import com.example.Motel.Api.APIService;
import com.example.Motel.Model.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class RoomRepository {

    private APIService apiService;

    public RoomRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_IP) // Thay đổi base URL nếu cần
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
    }

    public void getRoom(final RoomCallback callback) {
        Call<RoomResponse> call = apiService.getRoom();
        call.enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getRoom());  // Trả về danh sách phòng
                } else {
                    callback.onError("Không thể lấy dữ liệu: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {
                callback.onError("Lỗi: " + t.getMessage());
            }
        });
    }

    public void updateRoomStatus(int idRooms, String status, idRoomCallback callback) {
        RoomUpdateRequest request = new RoomUpdateRequest(idRooms, status);
        Call<Void> call = apiService.updateRoomStatus(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Cập nhật trạng thái phòng thành công");
                } else {
                    // In thông báo lỗi chi tiết từ phản hồi API
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Không có thông tin lỗi";
                        callback.onError("Phản hồi không thành công: " + response.message() + " - " + errorBody);
                    } catch (Exception e) {
                        callback.onError("Lỗi khi đọc thông tin lỗi: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void getRoomIdByName(String roomName, final RoomIdCallback callback) {
        Call<RoomIdResponse> call = apiService.getRoomIdByName(roomName);
        call.enqueue(new Callback<RoomIdResponse>() {
            @Override
            public void onResponse(Call<RoomIdResponse> call, Response<RoomIdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int roomId = response.body().getIdRooms();
                    callback.onSuccess(roomId);
                } else {
                    // Log lỗi và thông báo lỗi
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            callback.onError("Lỗi từ server: " + errorResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                            callback.onError("Lỗi xử lý phản hồi.");
                        }
                    } else {
                        callback.onError("Phản hồi không hợp lệ.");
                    }
                }
            }

            @Override
            public void onFailure(Call<RoomIdResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void getRooms(RoomListCallback callback) {
        apiService.getRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Không có dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


    public void deleteRooms(int roomsId, final RoomRepository.RoomsCallback callback) {
        Call<Void> call = apiService.deleteCustomer(roomsId);
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

    public void getRoomById(int id_rooms, final RoomByIdCallback callback) {
        Call<RoomsByIdResponse> call = apiService.getRoomById(id_rooms);
        call.enqueue(new Callback<RoomsByIdResponse>() {
            @Override
            public void onResponse(Call<RoomsByIdResponse> call, Response<RoomsByIdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Room room = response.body().getRoom(); // Giả sử RoomsByIdResponse chứa đối tượng Room

                    callback.onSuccess(room);
                } else {
                    callback.onError("Không tìm thấy phòng.");
                }
            }

            @Override
            public void onFailure(Call<RoomsByIdResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void deleteRoomById(int id_rooms, final DeleteRoomCallback callback) {
        Call<Void> call = apiService.deleteRoomById(id_rooms);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa thành công
                    callback.onSuccess();
                } else {
                    // Xóa không thành công
                    callback.onError("Failed to delete room.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xảy ra lỗi trong quá trình gọi API
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateRoom(Room room, final RoomUpdateCallback callback) {
        // Gọi API để cập nhật thông tin phòng
        Call<Void> call = apiService.updateRoom(room);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cập nhật thành công
                    callback.onSuccess("Cập nhật phòng thành công");
                } else {
                    // Xử lý lỗi từ phản hồi API
                    callback.onError("Cập nhật phòng thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi trong quá trình gọi API
                callback.onError("Lỗi khi cập nhật phòng: " + t.getMessage());
            }
        });
    }

    public void addRoom(Room room, final RoomAddCallback callback) {
        Call<Void> call = apiService.addRoom(room);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Thêm phòng thành công");
                } else {
                    callback.onError("Thêm phòng thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Lỗi khi thêm phòng: " + t.getMessage());
            }
        });
    }

    public void processPayment(Pay pay, final PaymentCallback callback) {
        Call<Void> call = apiService.processPayment(pay);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Thanh toán thành công");
                } else {
                    callback.onError("Thanh toán thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Lỗi: " + t.getMessage());
            }
        });
    }

    public void getPayDataByID(int idPay, final PayIdCallback callback) {
        Call<Pay> call = apiService.getPayDataById(idPay);
        call.enqueue(new Callback<Pay>() {
            @Override
            public void onResponse(Call<Pay> call, Response<Pay> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body()); // Trả về dữ liệu thanh toán
                } else {
                    callback.onError("No data found"); // Không có dữ liệu
                }
            }

            @Override
            public void onFailure(Call<Pay> call, Throwable t) {
                callback.onError(t.getMessage()); // Xử lý lỗi
            }
        });
    }

    public interface PayIdCallback {
        void onSuccess(Pay pay);
        void onError(String error);
    }

    public interface PaymentCallback {
        void onSuccess(String message);
        void onError(String error);
    }
    public interface RoomAddCallback {
        void onSuccess(String message);
        void onError(String error);
    }


    public interface RoomUpdateCallback {
        void onSuccess(String message);
        void onError(String error);
    }


    // Giao diện callback để xử lý kết quả
    public interface DeleteRoomCallback {
        void onSuccess();

        void onError(String errorMessage);
    }


    // Interface callback
    public interface RoomByIdCallback {
        void onSuccess(Room room);
        void onError(String error);
    }

    public interface RoomsCallback {
        void onSuccess(Customer customer);
        void onError(String error);
    }



    public interface RoomListCallback {
        void onSuccess(List<Room> rooms);

        void onError(String error);
    }


    public interface RoomIdCallback {
        void onSuccess(int roomId);

        void onError(String error);
    }


    public interface idRoomCallback {
        void onSuccess(String message);

        void onError(String error);
    }

    public interface RoomCallback {
        void onSuccess(List<String> rooms);

        void onError(String error);
    }
}

