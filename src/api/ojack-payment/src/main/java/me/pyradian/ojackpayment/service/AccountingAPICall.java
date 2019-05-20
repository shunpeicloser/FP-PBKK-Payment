package me.pyradian.ojackpayment.service;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.Map;

public interface AccountingAPICall {
    @POST("/pendapatan")
    Call<Map<String, Object>> createPendapatan(@Header("Authorization") String token,
                                               @Body JsonObject object);

    @POST("/pengeluaran")
    Call<Map<String, Object>> createPengeluaran(@Header("Authorization") String token,
                                                @Body JsonObject object);
}
