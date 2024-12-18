package com.example.andriodproekt;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        dbHelper = new DBHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            boolean isValidUser = dbHelper.checkUser(username, password);

            if (isValidUser) {
                String userChoice = dbHelper.getUserChoice(username);

                if (userChoice != null) {
                    if ("driver".equals(userChoice)) {
                        Intent driverIntent = new Intent(LoginPage.this, DriverActivity.class);
                        startActivity(driverIntent);
                        finish();
                    } else if ("passenger".equals(userChoice)) {
                        Intent passengerIntent = new Intent(LoginPage.this, PassengerActivity.class);
                        startActivity(passengerIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid user choice", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error retrieving user choice", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
