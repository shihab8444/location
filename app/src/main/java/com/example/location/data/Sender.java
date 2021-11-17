package com.example.location.data;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sender {
    private static final String TAG = "Sender";

    public static void sendNotifications(String userToken, String message,NotifyCallback callback) {
        Log.d(TAG, userToken);
        Data data = new Data("locationUpdates", message);
        NotificationSender sender = new NotificationSender(data, userToken);
        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().success != 1) {
                        callback.onFailed();
                        Log.d(TAG, "onResponse: failed");
                    }else {
                        callback.onSuccess();
                        Log.d(TAG, "onResponse: ss");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call,@NonNull Throwable t) {
                Log.d(TAG, "onResponse: ee");
                callback.onFailed();
            }
        });
    }

    public interface NotifyCallback{
        void onSuccess();
        void onFailed();
    }
}
