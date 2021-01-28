package com.example.duitku.user;

public class User {

    private String _id;
    private String user_name;
    private String user_email;
    private String user_status;
    private String user_first_time;
    private String user_passcode;

    public User() {

    }

    public User(String _id, String user_name, String user_email, String user_status, String user_first_time, String user_passcode) {
        this._id = _id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_status = user_status;
        this.user_first_time = user_first_time;
        this.user_passcode = user_passcode;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String name) {
        this.user_name = name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String email) {
        this.user_email = email;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String status) {
        this.user_status = status;
    }

    public String getUser_first_time() {
        return user_first_time;
    }

    public void setUser_first_time(String firstTime) {
        this.user_first_time = firstTime;
    }

    public String getUser_passcode() {
        return user_passcode;
    }

    public void setUser_passcode(String passcode) {
        this.user_passcode = passcode;
    }
}
