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
    private List<User> studentsList;
    private Group groupDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        recyclerViewGroups = findViewById(R.id.recyclerViewGroups);
        recyclerViewGroups.setLayoutManager(new LinearLayoutManager(this));

        studentsList = new ArrayList<>();
        groupAdapter = new GroupAdapter(studentsList);
        recyclerViewGroups.setAdapter(groupAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String studentKey = sharedPreferences.getString("key", "");

        // Fetch group details using studentKey
        GroupDataManager.FindGroup(studentKey, new FindByModel() {
            @Override
            public void onSuccess(Object model) {
                Object[] result = (Object[]) model;
                studentsList.addAll((List<User>) result[0]); // List of students
                groupDetails = (Group) result[1];            // Group details

                // Notify adapter about the data change
                groupAdapter.setGroup(groupDetails);
                groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                Toast.makeText(group_list.this, "Failed to fetch group data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
