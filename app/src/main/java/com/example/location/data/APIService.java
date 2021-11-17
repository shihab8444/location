package com.example.location.data;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAANmyuEns:APA91bEneM17howvprPp7_iWUnylEP34HguE_NoqMohh00K0iZ7sblv466c6JTZPOYQp6B-8Pdb1oMQik-8As60_CPwBlu8g30TqYx_zKHokW1UBFnJYbrrvOwQonAEkP4AewRuvQVEq" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
