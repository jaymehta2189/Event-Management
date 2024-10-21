package com.example.eventx20;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventx20.Database.Callback.FindByModel;
import com.example.eventx20.Database.DataModel.Event;
import com.example.eventx20.Database.UserDataManager;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUserLogin;
    private EditText editTextPasswordLogin;
    private Button buttonLogin;
//    private TextView forgotPasswordTextView;
    TextView textViewLogin;
    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextUserLogin = findViewById(R.id.usernameEditText);
        editTextPasswordLogin = findViewById(R.id.passwordEditText);
        buttonLogin = findViewById(R.id.button);
        textViewLogin = findViewById(R.id.textViewLogin);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        if(sharedPreferences.contains("Key")){
            if(sharedPreferences.getString("Key","").equals("xyz")){
                Intent intent = new Intent(MainActivity.this, admin_dash.class);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
            }

        }
//
//        String registeredEmail = sharedPreferences.getString("Username", "");
//        String registeredPassword = sharedPreferences.getString("Password", "");

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextUserLogin.getText().toString();
                String password = editTextPasswordLogin.getText().toString();
                if(name.equals("admin") &&password.equals("admin")){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("Key","xyz");
                    editor.commit();
                    Intent intent100=new Intent(MainActivity.this,admin_dash.class);
                    startActivity(intent100);
                    finish();
                }else{
                if (name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Retrieve user data from SharedPreferences
//
//
//                    String registeredPassword = sharedPreferences.getString("Password", "");

                    // Authentication logic
//                    if (name.equals(registeredName) && password.equals(registeredPassword)) {
                    UserDataManager.FindByName(name, password, new FindByModel() {
                        @Override
                        public void onSuccess(Object model) {
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Key",(String)model);
//                            editor.putString("qrCode", (String)model);
                            editor.commit();

                            UserDataManager.BindUserToEvent((String) model, new FindByModel() {
                                @Override
                                public void onSuccess(Object model) {
                                    Event event = (Event)model;
                                    String qrdata=sharedPreferences.getString("Key","") + " " +event.getKey();
                                    editor.putString("qrdata",qrdata);
                                    String fooddata=sharedPreferences.getString("Key","") + " " +event.getKey()+" F";
                                    editor.putString("fooddata",fooddata);
                                    editor.commit();
                                    Intent intent1=new Intent(MainActivity.this,Dashboard.class);
                                    startActivity(intent1);
                                    finish();
                                }

                                @Override
                                public void onFailure() {
                                    Intent intent = new Intent(MainActivity.this, currentevents.class);
//                            intent.putExtra("qrcode", (String)model);
                                    startActivity(intent);
                                    finish();
                                }
                            });



                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(MainActivity.this, "Invalid name or password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }}
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register_Activity.class);
                startActivity(intent);
            }
   });
    }
}