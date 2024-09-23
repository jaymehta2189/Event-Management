package com.example.eventx20.Database.DataModel;


import androidx.annotation.NonNull;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Event {


    public Event(){

    }

    public Event(String name, String date, String key, String time, String location,List<String> groupId) {
        Name = name;
        this.date = date;
        this.Key = key;
        this.time = time;
        Location = location;
        this.GroupId = groupId;
    }

    public List<String> getGroupId() {
        return GroupId;
    }

    public void setGroupId(List<String> groupId) {
        GroupId = groupId;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    private  String Name;
    private  String Key;
    private  String date;
    private  String time;
    private  String Location;
    private  List<String> GroupId;


    @NonNull
    @Override
    public String toString() {
        return "Event{" +
                "Name='" + Name + '\'' +
                ", id='" + Key + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", Location='" + Location + '\'' +
                ", GroupId=" + GroupId +
                '}';
    }
}
