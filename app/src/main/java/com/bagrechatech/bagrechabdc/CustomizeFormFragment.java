package com.bagrechatech.bagrechabdc;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CustomizeFormFragment extends Fragment {

    CardView addStationCv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customize_form, container, false);

        addStationCv = view.findViewById(R.id.addStationsCv);
        addStationCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddStationActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}