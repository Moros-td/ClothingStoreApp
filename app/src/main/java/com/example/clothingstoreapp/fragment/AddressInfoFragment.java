package com.example.clothingstoreapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.clothingstoreapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressInfoFragment extends Fragment {
    private Spinner spinnerProvince, spinnerDistrict, spinnerVillage;
    private List<String> provinceList, districtList, villageList;
    private ArrayAdapter<String> provinceAdapter, districtAdapter, villageAdapter;
    private int temp = 0;

    public AddressInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address_info, container, false);
        EditText editTextStreet = view.findViewById(R.id.editTextStreet);
        // Ánh xạ Spinner từ layout
        spinnerProvince = view.findViewById(R.id.spinner_province);
        spinnerDistrict = view.findViewById(R.id.spinner_district);
        spinnerVillage = view.findViewById(R.id.spinner_village);

        // Khởi tạo danh sách dữ liệu
        provinceList = new ArrayList<>();
        districtList = new ArrayList<>();
        villageList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String address = bundle.getString("address");
            String[] parts = address.split(",");
            editTextStreet.setText(parts[0]);
            String province = parts[3].trim();
            String district = parts[2].trim();
            String village = parts[1].trim();
            setupCustom(province, district, village);

        } else {
            setupCustom(null, null, null);
        }
        return view;
    }

    private void setupCustom(String province, String district, String village) {
        provinceList.add("Chọn tỉnh/thành phố");
        districtList.add("Chọn quận/huyện");
        villageList.add("Chọn phường/xã");
        loadProvinces();
        if(province!=null) {
            for (int i = 0; i < provinceList.size(); i++) {
                if (provinceList.get(i).equals(province)) {
                    spinnerProvince.setSelection(i);
                }
            }
        }else spinnerProvince.setSelection(0);

        // Khởi tạo và thiết lập adapter cho Spinner của quận/huyện và phường/xã
        districtAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, districtList);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);

        villageAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, villageList);
        villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVillage.setAdapter(villageAdapter);

        // Thiết lập sự kiện chọn mục cho Spinner của tỉnh/thành phố
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = (String) parent.getItemAtPosition(position);
                loadDistricts(selectedProvince);
                if (temp == 0 && province != null) {
                    for (int i = 0; i < districtList.size(); i++) {
                        if (districtList.get(i).equals(district)) {
                            spinnerDistrict.setSelection(i);
                        }
                    }
                    temp++;
                } else spinnerDistrict.setSelection(0);

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
                loadVillages(selectedDistrict);
                if (temp == 1 && village!= null) {
                    for (int i = 0; i < villageList.size(); i++) {
                        if (villageList.get(i).equals(village)) {
                            spinnerVillage.setSelection(i);
                        }
                    }
                } else spinnerVillage.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

}