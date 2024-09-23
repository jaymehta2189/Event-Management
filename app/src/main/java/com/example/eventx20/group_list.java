package com.example.eventx20;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventx20.Database.Callback.FindByModel;
import com.example.eventx20.Database.DataModel.Group;
import com.example.eventx20.Database.DataModel.User;
import com.example.eventx20.Database.GroupDataManager;

import java.util.ArrayList;
import java.util.List;

public class group_list extends AppCompatActivity {

    private RecyclerView recyclerViewGroups;
    private GroupAdapter groupAdapter;
    private List<GroupData> groupDataList;  // List to hold group and student data

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        recyclerViewGroups = findViewById(R.id.recyclerViewGroups);
        recyclerViewGroups.setLayoutManager(new LinearLayoutManager(this));

        groupDataList = new ArrayList<>();
        groupAdapter = new GroupAdapter(groupDataList);
        recyclerViewGroups.setAdapter(groupAdapter);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Fetch group details using studentKey
        String studentKey = sharedPreferences.getString("key","");
        GroupDataManager.FindGroup(studentKey, new FindByModel() {
            @Override
            public void onSuccess(Object model) {
                Object[] listOf = (Object[]) model;
                List<User> students = (List<User>) listOf[0]; // List of students
                Group group = (Group) listOf[1];              // Group details

                // Create a new GroupData object to hold both group and students
                GroupData groupData = new GroupData(group, students);

                // Add groupData to the list and notify adapter
                groupDataList.add(groupData);
                groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                Toast.makeText(group_list.this, "Failed to fetch groups", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Inner class to hold both group and student data
    public static class GroupData {
        private Group group;
        private List<User> students;

        public GroupData(Group group, List<User> students) {
            this.group = group;
            this.students = students;
        }

        public Group getGroup() {
            return group;
        }

        public List<User> getStudents() {
            return students;
        }
    }
}
