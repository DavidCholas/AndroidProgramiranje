package com.example.andriodproekt;

import android.os.Bundle;

import android.content.Intent;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.View;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private DBHelper dbHelper;
    private Spinner SpinnerChoices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpinnerChoices = findViewById(R.id.SpinnerChoices);

        List<String> choices = new ArrayList<>();
        choices.add("Driver");
        choices.add("Passenger");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerChoices.setAdapter(adapter);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        dbHelper = new DBHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
                transferDriversToDriversTable();
            }
        });
    }

    private void transferDriversToDriversTable() {
        List<User> drivers = dbHelper.getDrivers();
        if(drivers != null && !drivers.isEmpty()) {
            dbHelper.addDriversToDriverTable(drivers);
        } else {
            Toast.makeText(MainActivity.this, "There are no drivers to register", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String userChoice = SpinnerChoices.getSelectedItem().toString().toLowerCase();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all empty bars", Toast.LENGTH_SHORT).show();
        } else {
            if (dbHelper.checkUserExists(username)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = dbHelper.addUser(username, password,userChoice);

                if (success) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginPage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Registration failed, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
