package com.bagrechatech.bagrechabdc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CustomerCardAdapter extends FirebaseRecyclerAdapter<CustomerModel,CustomerCardAdapter.ViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CustomerCardAdapter(@NonNull FirebaseRecyclerOptions<CustomerModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CustomerCardAdapter.ViewHolder holder, int position, @NonNull CustomerModel model) {
        String name = model.getCustomerName();
        String contactNumber = model.getContactNumber();
        holder.setCustomerCard(name,contactNumber);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_card,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName;
        TextView customerContactNo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name_tv);
            customerContactNo = itemView.findViewById(R.id.customer_no_tv);
        }
        private void setCustomerCard(String name, String contactNo){
            customerName.setText(name);
            customerContactNo.setText(contactNo);
        }
    }
}
