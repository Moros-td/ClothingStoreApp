package com.example.clothingstoreapp.fragment.fragmentOfAuthenticationActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AuthenticationActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.LoginResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    Button registerBtn;
    TextView errorTextView;
    EditText emailEditText, passwordEditText, retypePasswordEditText, phoneEditText, nameEditText, editTextStreet;
    AuthenticationActivity authenticationActivity;

    Spinner spinnerProvince, spinnerDistrict, spinnerVillage;

    Dialog dialog;

    private List<String> provinceList, districtList, villageList;
    private ArrayAdapter<String> provinceAdapter, districtAdapter, villageAdapter;
    SessionManager sessionManager;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_register, container, false);
        authenticationActivity = (AuthenticationActivity) getContext();
        errorTextView = mView.findViewById(R.id.errorTextView);
        emailEditText = mView.findViewById(R.id.emailEditText);
        passwordEditText = mView.findViewById(R.id.passwordEditText);
        retypePasswordEditText = mView.findViewById(R.id.retypePasswordEditText);
        phoneEditText = mView.findViewById(R.id.phoneEditText);
        nameEditText = mView.findViewById(R.id.nameEditText);
        editTextStreet = mView.findViewById(R.id.editTextStreet);
        spinnerProvince = mView.findViewById(R.id.spinner_province);
        spinnerDistrict = mView.findViewById(R.id.spinner_district);
        spinnerVillage = mView.findViewById(R.id.spinner_village);
        registerBtn = mView.findViewById(R.id.registerBtn);

        // thiết lập adapter chọn địa chỉ
        provinceList = new ArrayList<>();
        districtList = new ArrayList<>();
        villageList = new ArrayList<>();

        provinceList.add("Chọn tỉnh/thành phố");
        districtList.add("Chọn quận/huyện");
        villageList.add("Chọn phường/xã");

        // Khởi tạo và thiết lập adapter cho Spinner của quận/huyện và phường/xã
        districtAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, districtList);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);

        villageAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, villageList);
        villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVillage.setAdapter(villageAdapter);

        loadProvinces();

        // Thiết lập sự kiện chọn mục cho Spinner của tỉnh/thành phố
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = (String) parent.getItemAtPosition(position);
                spinnerDistrict.setSelection(0);
                loadDistricts(selectedProvince);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Thiết lập sự kiện chọn mục cho Spinner của quận/huyện
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistrict = (String) parent.getItemAtPosition(position);
                spinnerVillage.setSelection(0);
                loadVillages(selectedDistrict);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // kết thúc thiết lập adapter chọn địa chỉ

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String retypePassword = retypePasswordEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                String province = spinnerProvince.getSelectedItem().toString();
                String district = spinnerDistrict.getSelectedItem().toString();
                String village = spinnerVillage.getSelectedItem().toString();
                String street = editTextStreet.getText().toString();
                boolean check = true;

                // Kiểm tra định dạng email
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Email không hợp lệ");
                    check = false;
                }

                // Kiểm tra mật khẩu
                if (password.isEmpty()) {
                    passwordEditText.setError("Không được bỏ trống password");
                    check = false;
                }

                if (retypePassword.isEmpty()) {
                    retypePasswordEditText.setError("Yêu cầu nhập lại password");
                    check = false;
                }

                if (phone.isEmpty()) {
                    phoneEditText.setError("Không được để trống số điện thoại");
                    check = false;
                }

                if (name.isEmpty()) {
                    nameEditText.setError("Không được để trống họ tên");
                    check = false;
                }

                if(province.contains("/")){
                    TextView errView = (TextView) spinnerProvince.getSelectedView();
                    errView.setError("");
                    errView.setTextColor(Color.RED);
                    errView.setText("Vui lòng chọn tỉnh/thành phố");
                    check = false;
                }

                if(district.contains("/")){
                    TextView errView = (TextView) spinnerDistrict.getSelectedView();
                    errView.setError("");
                    errView.setTextColor(Color.RED);
                    errView.setText("Vui lòng chọn quận/huyện");
                    check = false;
                }

                if(village.contains("/")){
                    TextView errView = (TextView) spinnerVillage.getSelectedView();
                    errView.setError("");
                    errView.setTextColor(Color.RED);
                    errView.setText("Vui lòng chọn phường/xã");
                    check = false;
                }

                if (street.isEmpty()) {
                    editTextStreet.setError("Không được để trống đường");
                    check = false;
                }

                if(!check)
                    return;

                // Kiểm tra mật khẩu nhập lại có trùng khớp với mật khẩu đã nhập không
                if (!password.equals(retypePassword)) {
                    retypePasswordEditText.setError("Mật khẩu nhập lại không khớp");
                    return;
                }

                if (checkPhone(phone)) {
                    phoneEditText.setError("Số điện thoại không hợp lệ");
                    return;
                }

                dialog = BaseActivity.openLoadingDialog(authenticationActivity);
                String address = street + ", " + village + ", " + district + ", " + province;

                callApiRegister(email, password, retypePassword, name, phone, address);
            }
        });
        return mView;
    }

    private void loadProvinces() {
        // Tạo danh sách tỉnh/thành phố
        provinceList.add("Hồ Chí Minh");
        provinceList.add("Hà Nội");
        // Thêm các tỉnh/thành phố vào Spinner tỉnh/thành phố
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, provinceList);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(provinceAdapter);
    }

    private void loadDistricts(String selectedProvince) {
        // Tạo một Map để lưu trữ danh sách quận/huyện tương ứng với mỗi thành phố
        Map<String, List<String>> districtMap = new HashMap<>();

        // Thêm dữ liệu quận/huyện cho mỗi thành phố vào Map
        List<String> hoChiMinhDistricts = new ArrayList<>();
        hoChiMinhDistricts.add("Quận 1");
        hoChiMinhDistricts.add("Quận 2");
        // Thêm các quận/huyện của thành phố Hồ Chí Minh vào Map
        districtMap.put("Hồ Chí Minh", hoChiMinhDistricts);

        List<String> haNoiDistricts = new ArrayList<>();
        haNoiDistricts.add("Quận Ba Đình");
        haNoiDistricts.add("Quận Hoàn Kiếm");
        // Thêm các quận/huyện của thành phố Hà Nội vào Map
        districtMap.put("Hà Nội", haNoiDistricts);

        // Tải danh sách quận/huyện tương ứng với thành phố đã chọn từ Map
        List<String> selectedDistricts = districtMap.get(selectedProvince);

        districtList.clear();
        districtList.add("Chọn quận/huyện");
        villageList.clear();
        villageList.add("Chọn phường/xã");
        // Cập nhật danh sách quận/huyện vào Spinner quận/huyện
        if (selectedDistricts != null) {
            districtList.addAll(selectedDistricts);
            districtAdapter.notifyDataSetChanged();
            villageAdapter.notifyDataSetChanged();
        }
    }

    private void loadVillages(String selectedDistrict) {
        // Tạo một Map để lưu trữ danh sách phường/xã tương ứng với mỗi quận/huyện
        Map<String, List<String>> villageMap = new HashMap<>();

        // Thêm dữ liệu phường/xã cho mỗi quận/huyện vào Map
        List<String> quan1Villages = new ArrayList<>();

        // Thêm các phường/xã của Quận 1 vào Map
        quan1Villages.add("Phường Tân Định");
        quan1Villages.add("Phường Đa Kao");
        villageMap.put("Quận 1", quan1Villages);

        // Thêm các phường/xã của Quận 2 vào Map
        List<String> quan2Villages = new ArrayList<>();
        quan2Villages.add("Phường Thảo Điền");
        quan2Villages.add("Phường An Phú");
        villageMap.put("Quận 2", quan2Villages);

        // Thêm các phường/xã của Quận Ba Đình vào Map
        List<String> baDinhVillages = new ArrayList<>();
        baDinhVillages.add("Phường Kim Mã");
        baDinhVillages.add("Phường Giảng Võ");
        villageMap.put("Quận Ba Đình", baDinhVillages);

        // Thêm các phường/xã của Quận Hoàn Kiếm vào Map
        List<String> hoanKiemVillages = new ArrayList<>();
        hoanKiemVillages.add("Phường Phan Chu Trinh");
        hoanKiemVillages.add("Phường Hàng Bài");
        villageMap.put("Quận Hoàn Kiếm", hoanKiemVillages);

        // Tải danh sách phường/xã tương ứng với quận/huyện đã chọn từ Map
        List<String> selectedVillages = villageMap.get(selectedDistrict);
        villageList.clear();
        villageList.add("Chọn phường/xã");
        // Cập nhật danh sách phường/xã vào Spinner phường/xã
        if (selectedVillages != null) {
            villageList.addAll(selectedVillages);
            villageAdapter.notifyDataSetChanged();
        }
    }

    private void resetEditText(){
        emailEditText.setText("");
        phoneEditText.setText("");
        retypePasswordEditText.setText("");
        nameEditText.setText("");
        passwordEditText.setText("");
        spinnerProvince.setSelection(0);
        editTextStreet.setText("");
    }

    private void callApiRegister(String email, String password, String retypePassword, String name, String phone,String address) {
        ApiService.apiService.register(email, name, password, retypePassword, phone, address)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        LoginResponse loginResponse = response.body();
                        if(loginResponse != null){
                            if(loginResponse.getErr() != null)
                                BaseActivity.openErrorDialog(getContext(), loginResponse.getErr());
                            else if (loginResponse.getToken() != null){
                                sessionManager = new SessionManager(authenticationActivity);
                                sessionManager.saveCustom("verify_token", loginResponse.getToken());
                                resetEditText();
                                authenticationActivity.openFragment(AuthenticationActivity.FRAGMENT_VERIFICATION);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        BaseActivity.openErrorDialog(getContext(), "Không thể truy cập api");
                    }
                });
    }

    private boolean checkPhone(String phone) {

        if(!phone.startsWith("0")){
            return true;
        }
        if(phone.length() < 10){
            return true;
        }

        return false;
    }
}