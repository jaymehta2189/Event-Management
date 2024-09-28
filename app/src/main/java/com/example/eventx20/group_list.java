//package com.example.eventx20;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Toast;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.eventx20.Database.Callback.FindByModel;
//import com.example.eventx20.Database.DataModel.Group;
//import com.example.eventx20.Database.DataModel.User;
//import com.example.eventx20.Database.GroupDataManager;
//import java.util.ArrayList;
//import java.util.List;
//
//public class group_list extends AppCompatActivity {
//
//    private RecyclerView recyclerViewGroups;
//    private GroupAdapter groupAdapter;
//    private List<User> studentsList;
//    private Group groupDetails;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_group_list);
//
//        recyclerViewGroups = findViewById(R.id.recyclerViewGroups);
//        recyclerViewGroups.setLayoutManager(new LinearLayoutManager(this));
//
//        studentsList = new ArrayList<>();
//        groupAdapter = new GroupAdapter(studentsList);
//        recyclerViewGroups.setAdapter(groupAdapter);
//
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//        String studentKey = sharedPreferences.getString("key", "");
//
//        // Fetch group details using studentKey
//        GroupDataManager.FindGroup(studentKey, new FindByModel() {
//            @Override
//            public void onSuccess(Object model) {
//                Object[] result = (Object[]) model;
//                studentsList.addAll((List<User>) result[0]); // List of students
//                groupDetails = (Group) result[1];            // Group details
//
//                // Notify adapter about the data change
//                groupAdapter.setGroup(groupDetails);
//                groupAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure() {
//                Toast.makeText(group_list.this, "Failed to fetch group data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
package com.example.eventx20;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eventx20.Database.GroupDataManager;
import com.example.eventx20.Database.UserDataManager;
import com.example.eventx20.Database.Callback.FindByModel;
import com.example.eventx20.Database.DataModel.Group;
import com.example.eventx20.Database.DataModel.User;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class group_list extends AppCompatActivity {

    private TextView studentNameTextView, studentEmailTextView, groupNameTextView, projectDetailsTextView;
    LinearLayout dynamic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        // Initializing the views
//        studentNameTextView = findViewById(R.id.student_name_label);
//        studentEmailTextView = findViewById(R.id.student_email_label);
        groupNameTextView = findViewById(R.id.textView8);
        projectDetailsTextView = findViewById(R.id.textView9);
            dynamic=findViewById(R.id.dynamicFieldsContainer);
        // Get the student key from Intent or Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String studentKey = sharedPreferences.getString("Key","");

        if (studentKey != null) {
            fetchStudentDetails(studentKey);
        }
    }

    private void fetchStudentDetails(String studentKey) {
        // Fetch the student details
        UserDataManager.FindByKey(studentKey, new FindByModel() {
            @Override
            public void onSuccess(Object model) {
                User student = (User) model;
//                displayStudentDetails(student);
                fetchGroupDetails(student.getGroupId());
            }

            @Override
            public void onFailure() {
                // Handle failure (e.g., show error message)
            }
        });
    }

    private void fetchGroupDetails(String groupId) {
        GroupDataManager.FindByKey(groupId, new FindByModel() {
            @Override
            public void onSuccess(Object model) {
                Group group = (Group) model;
                if (group != null) {
                    // Now fetch the group and users
                    UserDataManager.FindByGroup(group, new FindByModel() {
                        @Override
                        public void onSuccess(Object listOfGroup) {
                            Object[] list = (Object[]) listOfGroup;
                            List<User> students = (List<User>) list[0];
                            Group group = (Group) list[1];

                            // Display group details
                            displayGroupDetails(group,students);
                            // Display the list of students dynamically
//                            displayGroupDetails(students);
                        }

                        @Override
                        public void onFailure() {
                            // Handle failure
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
                // Handle failure
            }
        });
    }

//    private void displayStudentDetails(User student) {
//        studentNameTextView.setText(student.getName());
//        studentEmailTextView.setText(student.getEmail());
//    }

    private void displayGroupDetails(Group group,List<User>student) {
//        groupNameTextView.setText(group.getGroupName());
//        projectDetailsTextView.setText(group.getProjectName());
        
        groupNameTextView.append(group.getGroupName());

        projectDetailsTextView.append(group.getProjectName());

        for(User st:student){
            TextView t1=new TextView(this);
            t1.setText(st.getName());
            t1.setTextColor(Color.BLACK);  // You can change the color as needed

            // Set text size
            t1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            t1.setTypeface(null, Typeface.BOLD);
            dynamic.addView(t1);
        }

    }
}
