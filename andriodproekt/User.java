package com.example.andriodproekt;

public class User {
    private String username;
    private String password;
    private String userChoice;
    private String carDetails;
    private String licenseCredentials;
    private String freeTime;

    public User(String username, String carDetails, String licenseCredentials, String freeTime) {
        this.username = username;
        this.carDetails = carDetails;
        this.licenseCredentials = licenseCredentials;
        this.freeTime = freeTime;
    }
    public User(String username, String password, String userChoice) {
        this.username = username;
        this.password = password;
        this.userChoice = userChoice;
        this.carDetails = "";
        this.licenseCredentials = "";
        this.freeTime = "";
    }

    public String getUsername() {
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getCarDetails() {
        return carDetails;
    }

    public String getLicenseCredentials() {
        return licenseCredentials;
    }

    public String getFreeTime() {
        return freeTime;
    }
}
