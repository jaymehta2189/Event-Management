package com.example.eventx20;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            editTextTeamLeaderName.setError("Team Leader's ID is required");
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
        for (int i = 0; i < dynamicFieldsContainer.getChildCount(); i += 2) {
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
        }

        // If all validations pass, show a success message
        Toast.makeText(this, "Form Submitted Successfully!", Toast.LENGTH_SHORT).show();

        // Here you can handle the collected data, like sending it to a server, storing in a database, etc.
        // For now, we just clear the form after submission
//        clearForm();
        Intent intent =new Intent(Requirement_collection.this, Qrcodegenerator.class);
        intent.putExtra("qrcode",editTextTeamLeaderId.getText().toString().trim());
        startActivity(intent);
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
