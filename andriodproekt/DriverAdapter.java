package com.example.andriodproekt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {
    private List<User> drivers;
    private OnItemClickListener onItemClickListener;

    public DriverAdapter(List<User> drivers, OnItemClickListener onItemClickListener) {
        this.drivers = drivers;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_item, parent, false);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        User driver = drivers.get(position);

        holder.usernameTextView.setText(driver.getUsername());

        holder.driverButton.setOnClickListener(v -> onItemClickListener.onItemClick(driver));
    }


    @Override
    public int getItemCount() {
        return drivers.size();
    }
    public static class DriverViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        Button driverButton;
        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            driverButton = itemView.findViewById(R.id.driverButton);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User driver);
    }
}
