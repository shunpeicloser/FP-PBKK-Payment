package me.pyradian.ojackpayment.service;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.Map;

public interface NotificationAPICall {
    @POST("/notification")
    Call<Map<String, Object>> sendNotification(@Header("Authorization") String token,
                                               @Body JsonObject object);
}
