package com.example.andriodproekt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PassengerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DriverAdapter driverAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        recyclerView = findViewById(R.id.recyclerViewDrivers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        List<User> drivers = dbHelper.getDrivers();

        if (drivers != null && !drivers.isEmpty()) {
            driverAdapter = new DriverAdapter(drivers, new DriverAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(User driver) {
                    displayDriverDetails(driver);
                }
            });
            recyclerView.setAdapter(driverAdapter);
        } else {
            Toast.makeText(this, "No drivers available", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayDriverDetails(User driver) {
        Intent intent = new Intent(PassengerActivity.this, DriverDetailsActivity.class);
        intent.putExtra("driver_username", driver.getUsername());
        startActivity(intent);
    }
}








