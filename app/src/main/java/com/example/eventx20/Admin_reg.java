package com.example.eventx20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventx20.Database.AdminDataManager;
import com.example.eventx20.Database.Callback.InsertModel;
import com.example.eventx20.Database.DataModel.Event;

import java.util.Date;

public class Admin_reg extends AppCompatActivity {
    private EditText editTextEventName, editTextDate, editTextTime, editTextLocation;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reg);

        editTextEventName = findViewById(R.id.editTextText2);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextLocation = findViewById(R.id.editTextText);
        submitButton = findViewById(R.id.button2);

        // Setting onClickListener for the Submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = editTextEventName.getText().toString();
                String date = editTextDate.getText().toString();
                String time = editTextTime.getText().toString();
                String location = editTextLocation.getText().toString();

                Event event = new Event();
                event.setDate(date);
                event.setName(eventName);
                event.setLocation(location);
                event.setTime(time);

                AdminDataManager.InsertEvent("a", event, new InsertModel() {
                    @Override
                    public void onComplete() {
                        startActivity(new Intent(Admin_reg.this,admin_dash.class));
                        finish();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        });
    }

}