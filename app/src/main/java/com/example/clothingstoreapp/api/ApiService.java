package com.example.clothingstoreapp.api;

import com.example.clothingstoreapp.entity.AddressEntity;
import com.example.clothingstoreapp.entity.CartItemEnity;
import com.example.clothingstoreapp.entity.CategoryEntity;
import com.example.clothingstoreapp.entity.CustomerEntity;
import com.example.clothingstoreapp.entity.OrderEntity;
import com.example.clothingstoreapp.entity.ProductEntity;
import com.example.clothingstoreapp.response.BooleanResponse;
import com.example.clothingstoreapp.response.CartCodeResponse;
import com.example.clothingstoreapp.response.CheckItemResponse;
import com.example.clothingstoreapp.response.LoginResponse;
import com.example.clothingstoreapp.response.OrderResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

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
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
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
    Call<LoginResponse> loginOut(@Header("Authorization") String token,
                                 @Field("email") String email);

    @FormUrlEncoded
    @POST("/Auth/Register")
    Call<LoginResponse> register(@Field("email") String email, @Field("fullname") String fullName,
                                 @Field("password") String password, @Field("retype_password") String retype_password,
                                 @Field("phone") String phone, @Field("address") String address);

    @FormUrlEncoded
    @POST("/Auth/Verify")
    Call<LoginResponse> verify(@Field("verifyCode") String verifyCode, @Field("token") String token);

    @FormUrlEncoded
    @POST("/Auth/ForgotPassword")
    Call<LoginResponse> forgotPassword(@Field("email") String email);

    @GET("/Product/getAllProducts")
    Call<List<ProductEntity>> getAllProducts();

    @GET("/Category/getAllCategories")
    Call<List<CategoryEntity>> getAllCategories();

    @FormUrlEncoded
    @POST("/Cart/AddProduct")
    Call<BooleanResponse> AddProduct(@Header("Authorization") String token,
                                     @Field("cart_code") String cart_code,
                                     @Field("product_code") String product_code,
                                     @Field("quantity") int quantity,
                                     @Field("size") String size,
                                     @Field("total_price") Double total_price);

    @FormUrlEncoded
    @POST("/Cart/LoadCartItem")
    Call<List<CartItemEnity>> getAllCartItems(@Header("Authorization") String token,
                                              @Field("email") String email);

    @FormUrlEncoded
    @POST("/Cart/FindCartCode")
    Call<CartCodeResponse> findCartCode(@Header("Authorization") String token,
                                        @Field("email") String email);

    @FormUrlEncoded
    @POST("/Cart/CheckItem")
    Call<CheckItemResponse> checkItem(@Header("Authorization") String token,
                                      @Field("email") String email);

    @FormUrlEncoded
    @POST("/Cart/RemoveProduct")
    Call<BooleanResponse> RemoveProduct(@Header("Authorization") String token,
                                        @Field("cart_code") String cart_code,
                                        @Field("product_code") String product_code,
                                        @Field("quantity") int quantity,
                                        @Field("size") String size,
                                        @Field("total_price") Double total_price);

    @FormUrlEncoded
    @POST("Cart/DeleteItem")
    Call<BooleanResponse> DeleteItem(@Header("Authorization") String token,
                                     @Field("cart_code") String cart_code,
                                     @Field("product_code") String product_code,
                                     @Field("size") String size);

    @FormUrlEncoded
    @POST("/Address/LoadAllAddress")
    Call<List<AddressEntity>> LoadAllAddress(@Header("Authorization") String token,
                                             @Field("email") String email);

    @FormUrlEncoded
    @POST("/Address/SetAddressDefault")
    Call<BooleanResponse> SetAddressDefault(@Header("Authorization") String token,
                                            @Field("email") String email,
                                            @Field("address_id") int address_id);

    @FormUrlEncoded
    @POST("/Order/AddOrder")
    Call<OrderResponse> AddOrder(@Header("Authorization") String token,
                                 @Field("state") String state,
                                 @Field("total_price") Double total_price,
                                 @Field("email") String email,
                                 @Field("address") String address);

    @FormUrlEncoded
    @POST("/Order/AddOrderItem")
    Call<BooleanResponse> AddOrderItem(@Header("Authorization") String token,
                                       @Field("order_code") String order_code,
                                       @Field("product_code") String product_code,
                                       @Field("quantity") int quantity,
                                       @Field("size") String size, @Field("total_price") Double total_price);

    @FormUrlEncoded
    @POST("/Cart/DeleteAllItem")
    Call<BooleanResponse> DeleteAllItem(@Header("Authorization") String token, @Field("cart_code") String cart_code);

    @FormUrlEncoded
    @POST("/Product/GetProductByCategory")
    Call<List<ProductEntity>> GetProductByCategory(@Field("category_id") int category_id);
    @FormUrlEncoded
    @POST("/Customer/getCustomerInfo")
    Call<CustomerEntity> getCustomerInfo(@Header("Authorization") String token,
                                   @Field("email") String email);

    @FormUrlEncoded
    @POST("/Customer/updateCustomerInfo")
    Call<LoginResponse> updateCustomerInfo(@Header("Authorization") String token,
                                         @Field("email") String email, @Field("fullName") String fullName,
                                         @Field("phone") String phone);

    @FormUrlEncoded
    @POST("/Customer/updateCustomerPassword")
    Call<LoginResponse> updateCustomerPassword(@Header("Authorization") String token,
                                           @Field("email") String email, @Field("password") String password,
                                           @Field("newPassword") String newPassword);

    @FormUrlEncoded
    @POST("/Order/getAllOrders")
    Call<List<OrderEntity>> getAllOrders(@Header("Authorization") String token, @Field("email") String email);

    @FormUrlEncoded
    @POST("/Order/cancelOrder")
    Call<LoginResponse> cancelOrder(@Header("Authorization") String token, @Field("orderCode") String orderCode);
}
