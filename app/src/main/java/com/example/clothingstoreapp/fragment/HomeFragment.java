package com.example.clothingstoreapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.adapter.PhotoAdapter;
import com.example.clothingstoreapp.entity.Photo;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class HomeFragment extends Fragment {

    private ViewPager viewPager;
    private CircleIndicator indicator;
    private PhotoAdapter photoAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        indicator = view.findViewById(R.id.indicator);

        photoAdapter = new PhotoAdapter(getContext(),getListPhoto());
        viewPager.setAdapter(photoAdapter);

        indicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(indicator.getDataSetObserver());
        return view;
    }
    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.home1));
        list.add(new Photo(R.drawable.home2));
        list.add(new Photo(R.drawable.home3));
        list.add(new Photo(R.drawable.home4));
        return list;
    }
}