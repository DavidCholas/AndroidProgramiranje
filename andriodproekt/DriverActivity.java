package com.example.andriodproekt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import androidx.appcompat.app.AppCompatActivity;


public class DriverActivity extends AppCompatActivity {
    private EditText carEditText, licenseEditText, freeTimeEditText;
    private Button saveButton;
    private DBHelper dbHelper;
    private String currentUsername;
    private String currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        carEditText = findViewById(R.id.carEditText);
        licenseEditText = findViewById(R.id.licenseEditText);
        freeTimeEditText = findViewById(R.id.freeTimeEditText);
        saveButton = findViewById(R.id.saveButton);

        DBHelper dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        currentUsername = intent.getStringExtra("username");
        currentPassword = intent.getStringExtra("password");

        if (currentUsername == null || currentPassword == null) {
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
            return;
        }

        saveButton.setOnClickListener(v -> {
            String carDetails = carEditText.getText().toString();
            String licenseCredentials = licenseEditText.getText().toString();
            String freeTime = freeTimeEditText.getText().toString();

            saveDriverInformation(carDetails, licenseCredentials, freeTime);

            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveDriverInformation(String carDetails, String licenseCredentials, String freeTime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("car_details", carDetails);
        contentValues.put("license_credentials", licenseCredentials);
        contentValues.put("free_time", freeTime);

        int rowsAffected = db.update(DBHelper.TABLE_USER, contentValues, DBHelper.COLUMN_USERNAME + " = ?", new String[]{currentUsername});

        if (rowsAffected > 0) {
            Toast.makeText(DriverActivity.this, "Driver information saved successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DriverActivity.this, LoginPage.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(DriverActivity.this, "Failed to save information", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
