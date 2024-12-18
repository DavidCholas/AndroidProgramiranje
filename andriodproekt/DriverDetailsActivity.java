package com.example.andriodproekt;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DriverDetailsActivity extends AppCompatActivity {
    private TextView carTextView, licenseTextView, freeTimeTextView, usernameTextView;
    private DBHelper dbHelper;
    private String driverUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        carTextView = findViewById(R.id.carTextView);
        licenseTextView = findViewById(R.id.licenseTextView);
        freeTimeTextView = findViewById(R.id.freeTimeTextView);
        usernameTextView = findViewById(R.id.usernameTextView);

        dbHelper = new DBHelper(this);

        driverUsername = getIntent().getStringExtra("driver_username");

        User driver = dbHelper.getDriverInfo(driverUsername);

        if (driver != null) {

            carTextView.setText("Car: " + driver.getCarDetails());
            licenseTextView.setText("License: " + driver.getLicenseCredentials());
            freeTimeTextView.setText("Free Time: " + driver.getFreeTime());
            usernameTextView.setText("Username: " + driver.getUsername());
        } else {
            Toast.makeText(this, "No details found for this driver", Toast.LENGTH_SHORT).show();
        }
    }
    public void goBack(View view) {
        onBackPressed();
    }
}
