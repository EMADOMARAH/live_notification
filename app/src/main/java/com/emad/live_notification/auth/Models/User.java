package com.emad.live_notification.auth.Models;


public class User {

    private String name;
    private String user_id;
    private String device_token;


    public User(String name, String user_id,String device_token) {
        this.name = name;
        this.user_id = user_id;
        this.device_token = device_token;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", device_token='" + device_token + '\'' +
                '}';
    }
}
