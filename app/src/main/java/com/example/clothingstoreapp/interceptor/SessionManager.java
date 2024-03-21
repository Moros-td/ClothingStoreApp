package com.example.clothingstoreapp.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String SHARED_PREF_NAME = "jwt_shared_pref";
    private static final String KEY_JWT = "jwt";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Lưu JWT vào SharedPreferences
    public void saveJwt(String jwt) {
        editor.putString(KEY_JWT, "Bearer " + jwt);
        editor.apply();
    }

    public void saveCustom(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }

    public String getCustom(String key) {
        return sharedPreferences.getString(key, null);
    }


    // Lấy JWT từ SharedPreferences
    public String getJwt() {
        return sharedPreferences.getString(KEY_JWT, null);
    }

    // Kiểm tra xem người dùng đã đăng nhập hay chưa bằng cách kiểm tra xem JWT có tồn tại hay không
    public boolean isLoggedIn() {
        return getJwt() != null;
    }

    // Xóa JWT khỏi SharedPreferences khi đăng xuất
    public void logout() {
        editor.remove(KEY_JWT);
        editor.apply();
    }

    public void deleteCustom(String key){
        editor.remove(key);
        editor.apply();
    }
}
