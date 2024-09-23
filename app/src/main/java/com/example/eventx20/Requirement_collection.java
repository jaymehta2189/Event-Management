package com.example.eventx20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventx20.Database.Callback.GroupToEvent;
import com.example.eventx20.Database.DataModel.Group;
import com.example.eventx20.Database.GroupDataManager;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class Requirement_collection extends AppCompatActivity {

    private EditText editTextProjectTitle, editTextTeamName, editTextTeamLeaderName, editTextContactNumber,editTextTeamLeaderId;
    private EditText editTextNumOfMembers;
    private LinearLayout dynamicFieldsContainer;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_collection);

        // Initializing views
        editTextProjectTitle = findViewById(R.id.editTextProjectTitle);
        editTextTeamName = findViewById(R.id.editTextTeamName);
        editTextTeamLeaderName = findViewById(R.id.editTextTeamLeaderName);
        editTextContactNumber = findViewById(R.id.editTextContactNumber);
        editTextNumOfMembers = findViewById(R.id.editTextNumOfMembers);
        dynamicFieldsContainer = findViewById(R.id.dynamicFieldsContainer);
        submitButton = findViewById(R.id.submitButton);
        editTextTeamLeaderId=findViewById(R.id.editTextTeamLeaderId);
        // Set up the event listener for the Number of Team Members field
        editTextNumOfMembers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    addDynamicFields();
                }
            }
        });

        // Setting up the Submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void addDynamicFields() {
        // Clear any previous dynamic fields
        dynamicFieldsContainer.removeAllViews();

        // Get the number of team members
        String numOfMembersStr = editTextNumOfMembers.getText().toString().trim();
        if (TextUtils.isEmpty(numOfMembersStr)) {
            return;
        }

        int numOfMembers = Integer.parseInt(numOfMembersStr) - 1; // Subtract 1 for the leader

        // Create dynamic fields for student names and IDs
        for (int i = 0; i < numOfMembers; i++) {
            // Create and add a TextView for the student's name
            EditText studentName = new EditText(this);
            studentName.setHint("Enter Student Name " + (i + 1));
            dynamicFieldsContainer.addView(studentName);

            // Create and add a TextView for the student's ID
            EditText studentID = new EditText(this);
            studentID.setHint("Enter Student ID " + (i + 1));
            dynamicFieldsContainer.addView(studentID);
        }
    }

    private void submitForm() {
        // Retrieve input data
        String projectTitle = editTextProjectTitle.getText().toString().trim();
        String teamName = editTextTeamName.getText().toString().trim();
        String teamLeaderName = editTextTeamLeaderName.getText().toString().trim();
        String teamLeaderID = editTextTeamLeaderId.getText().toString().trim();
        String contactNumber = editTextContactNumber.getText().toString().trim();
        String numOfMembers = editTextNumOfMembers.getText().toString().trim();

        // Validate the main fields
        if (TextUtils.isEmpty(projectTitle)) {
            editTextProjectTitle.setError("Project Title is required");
            return;
        }

        if (TextUtils.isEmpty(teamName)) {
            editTextTeamName.setError("Team Name is required");
            return;
        }

        if (TextUtils.isEmpty(teamLeaderName)) {
            editTextTeamLeaderName.setError("Team Leader's Name is required");
            return;
        }
        if (TextUtils.isEmpty(teamLeaderID)) {
            editTextTeamLeaderId.setError("Team Leader's ID is required");
            return;
        }
        if (TextUtils.isEmpty(contactNumber)) {
            editTextContactNumber.setError("Contact Number is required");
            return;
        }

        if (TextUtils.isEmpty(numOfMembers)) {
            editTextNumOfMembers.setError("Number of Team Members is required");
            return;
        }

        // Validate dynamic fields
        List<String> studentemail = new ArrayList<>();
        studentemail.add(teamLeaderID);
//        for (int i = 0; i < dynamicFieldsContainer.getChildCount(); i += 2) {
//            EditText studentName = (EditText) dynamicFieldsContainer.getChildAt(i);
//            EditText studentID = (EditText) dynamicFieldsContainer.getChildAt(i + 1);
//
//            if (TextUtils.isEmpty(studentName.getText().toString().trim())) {
//                studentName.setError("Student Name " + (i / 2 + 1) + " is required");
//                return;
//            }
//
//            if (TextUtils.isEmpty(studentID.getText().toString().trim())) {
//                studentID.setError("Student ID " + (i / 2 + 1) + " is required");
//                return;
//            }
//            studentemail.add(studentID.getText().toString().trim());
//        }
        int childCount = dynamicFieldsContainer.getChildCount();
        for (int i = 0; i < childCount; i += 2) {
            EditText studentName = (EditText) dynamicFieldsContainer.getChildAt(i);
            EditText studentID = (EditText) dynamicFieldsContainer.getChildAt(i + 1);

            if (TextUtils.isEmpty(studentName.getText().toString().trim())) {
                studentName.setError("Student Name " + (i / 2 + 1) + " is required");
                return;
            }

            if (TextUtils.isEmpty(studentID.getText().toString().trim())) {
                studentID.setError("Student ID " + (i / 2 + 1) + " is required");
                return;
            }
            studentemail.add(studentID.getText().toString().trim());
        }

        Group group = new Group(projectTitle,teamName, getIntent().getStringExtra("EVENT_NAME"),contactNumber,studentemail);

        // SEE This Later :)

//
//        GroupDataManager.InsertUserInGroup(group, new GroupToEvent() {
//            @Override
//            public void onSuccess(Group group, String key) {
//                // Form is successfully submitted, navigate to next activity
//                Intent intent = new Intent(Requirement_collection.this, Dashboard.class);
//                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//                intent.putExtra("qrcode", sharedPreferences.getString("Key", ""));
//                startActivity(intent);
//
//                // Show success message only when form submission is successful
//                Toast.makeText(Requirement_collection.this, "Form Submitted Successfully!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailed() {
//                Toast.makeText(getBaseContext(), "Failed to submit form!", Toast.LENGTH_SHORT).show();
//            }
//        });

        GroupDataManager.InsertUserInGroup(group, new GroupToEvent() {
            @Override
            public void onSuccess(Group group, String key) {


                Toast.makeText(getBaseContext(), "Form Submitted Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent =new Intent(Requirement_collection.this, Dashboard.class);
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String qrdata=sharedPreferences.getString("Key","") + " " + key;
//                intent.putExtra("qrcode", qrdata);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("qrdata",qrdata);
                editor.commit();
                startActivity(intent);
            }

            @Override
            public void onFailed() {
                Toast.makeText(getBaseContext(), "failed!", Toast.LENGTH_SHORT).show();
            }
        });

        // If all validations pass, show a success message


//         Here you can handle the collected data, like sending it to a server, storing in a database, etc.
//         For now, we just clear the form after submission
        clearForm();

    }

    private void clearForm() {
        editTextProjectTitle.setText("");
        editTextTeamName.setText("");
        editTextTeamLeaderName.setText("");
        editTextContactNumber.setText("");
        editTextNumOfMembers.setText("");
        editTextTeamLeaderId.setText("");
        dynamicFieldsContainer.removeAllViews(); // Clear dynamic fields
    }
}
