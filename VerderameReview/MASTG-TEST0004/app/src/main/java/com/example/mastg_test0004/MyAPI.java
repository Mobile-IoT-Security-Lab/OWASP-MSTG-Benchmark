package com.example.mastg_test0004;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyAPI {
    @POST("/")
    Call<Void> sendData(@Body RequestBody data);
}