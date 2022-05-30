package com.aariyan.platformreturns.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aariyan.platformreturns.Model.StoreModel;
import com.aariyan.platformreturns.R;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private Context context;
    private List<StoreModel> listOfStore;
    public StoreAdapter(Context context, List<StoreModel> listOfStore) {
        this.context = context;
        this.listOfStore = listOfStore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.store_single_list, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreModel model = listOfStore.get(position);
        holder.accountCode.setText(model.getCustomerCode());
        holder.storeName.setText(model.getStoreName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfStore.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView storeName;
        private TextView accountCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.userName);
            accountCode = itemView.findViewById(R.id.accountCode);
        }
    }
}
