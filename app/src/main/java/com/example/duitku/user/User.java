package com.example.duitku.user;

public class User {

    private String name;
    private String email;
    private String status;
    private String firstTime;
    private String passcode;

    public User(String name, String email, String status, String firstTime, String passcode) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.firstTime = firstTime;
        this.passcode = passcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }
}
