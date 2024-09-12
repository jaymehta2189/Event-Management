package com.example.eventx20.Database.DataModel;

import androidx.annotation.NonNull;

import java.util.List;

public class Group {
    private String ProjectName;
    private String GroupName;
    private String EventName;
    private String ContactNumber;
    private List<String> StudentId;

    public Group(){}

    public Group(String projectName, String groupName, String eventName, String contactNumber, List<String> studentEmailId) {
        ProjectName = projectName;
        GroupName = groupName;
        EventName = eventName;
        ContactNumber = contactNumber;
        StudentId = studentEmailId;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public List<String> getStudentEmailId() {
        return StudentId;
    }

    public void setStudentEmailId(List<String> studentEmailId) {
        StudentId = studentEmailId;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return "Group{" +
                "ProjectName='" + ProjectName + '\'' +
                ", GroupName='" + GroupName + '\'' +
                ", EventName='" + EventName + '\'' +
                ", ContactNumber='" + ContactNumber + '\'' +
                ", StudentEmailId=" + StudentId +
                '}';
    }
}
