package com.example.Motel.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class loadImage {

    public static void load_Image(Context context, String imagePath, View view) {
        // Kiểm tra xem context có hợp lệ không
        if (context == null) {
            return;
        }

        // Lấy đối tượng StorageReference từ Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(imagePath);

        // Lấy URL của ảnh từ Firebase Storage
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();

            // Kiểm tra xem view là ImageView hay View khác
            if (view instanceof ImageView) {
                // Nếu là ImageView, tải ảnh vào ImageView
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(android.R.drawable.ic_menu_gallery)  // Sử dụng ảnh mặc định của Android
                        .error(android.R.drawable.stat_notify_error)      // Sử dụng ảnh lỗi mặc định của Android
                        .into((ImageView) view);

            } else {
                // Nếu là View khác (ví dụ: LinearLayout), tải ảnh và đặt làm background
                Picasso.get().load(imageUrl).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // Chuyển ảnh thành BitmapDrawable và đặt làm background
                        view.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        // Xử lý khi tải ảnh thất bại
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // Xử lý trong khi đang tải ảnh
                    }
                });
            }

        }).addOnFailureListener(e -> {
            // Nếu view là ImageView, đặt ảnh lỗi, nếu không, đặt background lỗi
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(android.R.drawable.stat_notify_error);
            } else {
                view.setBackgroundResource(android.R.drawable.stat_notify_error);
            }
        });
    }
}
