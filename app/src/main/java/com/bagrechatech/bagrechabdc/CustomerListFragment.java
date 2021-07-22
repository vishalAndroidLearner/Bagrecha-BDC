package com.bagrechatech.bagrechabdc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class CustomerListFragment extends Fragment {

    private RecyclerView customerCardRecyclerView;
    private CustomerCardAdapter customerCardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);

        customerCardRecyclerView = view.findViewById(R.id.customerModelRecyclerView);
        customerCardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query query = FirebaseDatabase.getInstance().getReference().child("Customers").orderByChild("stationName");
        FirebaseRecyclerOptions<CustomerModel> options =
                new FirebaseRecyclerOptions.Builder<CustomerModel>()
                        .setQuery(query, CustomerModel.class)
                        .build();
        customerCardAdapter = new CustomerCardAdapter(options);
        customerCardRecyclerView.setAdapter(customerCardAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        customerCardAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        customerCardAdapter.stopListening();
    }
}