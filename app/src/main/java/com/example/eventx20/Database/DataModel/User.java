package com.example.eventx20.Database.DataModel;

import androidx.annotation.NonNull;

public class User {
    private String Key;
    private String Name;
    private String Email;
    private String Password;
    private boolean isJoinEvent;
    private boolean isPresent;
    private boolean isFood;
    private String GroupId;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name,String email,String password){
        this.Name = name;
        this.Email = email;
        this.Password = password;
        this.isFood = false;
        this.isPresent = false;
        this.isJoinEvent = false;
        this.GroupId = "";
        this.Key = "";
    }

    public boolean isJoinEvent() {
        return isJoinEvent;
    }

    public void setJoinEvent(boolean joinEvent) {
        isJoinEvent = joinEvent;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public boolean isFood() {
        return isFood;
    }

    public void setFood(boolean food) {
        isFood = food;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }


    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "Name='" + Name + '\'' +
                ", Email='" + Email + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}
