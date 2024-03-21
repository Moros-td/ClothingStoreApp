package com.example.clothingstoreapp.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.clothingstoreapp.entity.CustomerEntity;
import com.example.clothingstoreapp.entity.ProductEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
//    String jwt = null;
//    Interceptor interceptor = chain -> {
//        Request request = chain.request();
//        Request.Builder builder = request.newBuilder();
//        builder.addHeader("Authorization", "Bearer " + jwt);
//        return chain.proceed(builder.build());
//    };

    HttpLoggingInterceptor httpLogging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            //.addInterceptor(interceptor)
            .addInterceptor(httpLogging);

    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8096/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okhttpBuilder.build())
            .build()
            .create(ApiService.class);


    @FormUrlEncoded
    @POST("/Auth/Login")
    Call<LoginResponse> logIn(@Field("email") String email,  // Dữ liệu email
                              @Field("password") String password);  // Dữ liệu password);

    @FormUrlEncoded
    @POST("/Auth/Logout")
    Call<LoginResponse> loginOut(@Header ("Authorization") String token,
                                 @Field("email") String email);

    @GET("/Product/getAllProducts")
    Call<List<ProductEntity>> getAllProducts();
}
