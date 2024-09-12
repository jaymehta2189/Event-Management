package com.example.eventx20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventx20.Database.Callback.InsertModel;
import com.example.eventx20.Database.DataModel.User;
import com.example.eventx20.Database.UserDataManager;

public class Register_Activity extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword;
    Button buttonRegister;
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.usernameEditText);
        editTextEmail = findViewById(R.id.emailedittext);
        editTextPassword = findViewById(R.id.passwordEditText);
        buttonRegister = findViewById(R.id.button);
        textViewLogin = findViewById(R.id.textViewLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Register_Activity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Save user data using SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Username", username);
                    editor.putString("Email", email);
                    editor.putString("Password", password);

                    User UserRegister = new User(username,email,password);

                    UserDataManager.InsertData(UserRegister, new InsertModel() {
                        @Override
                        public void onComplete() {
                            editor.apply();
                            Toast.makeText(Register_Activity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register_Activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(Register_Activity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}