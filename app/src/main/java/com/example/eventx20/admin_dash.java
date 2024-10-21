package com.example.eventx20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class admin_dash extends AppCompatActivity {

    CardView cardTeam;
    CardView cardList;
    CardView cardQr;
    CardView cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        cardTeam = findViewById(R.id.cardTeam);
        cardList = findViewById(R.id.cardList);
        cardQr = findViewById(R.id.cardQr);
        cardLogout = findViewById(R.id.cardLogout);

        cardTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast("Team Clicked");
                Intent intent=new Intent(admin_dash.this, Admin_reg.class);
                startActivity(intent);

            }
        });
        cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast("Chat Clicked");
                Intent intent=new Intent(admin_dash.this, Event_fetch.class);

                startActivity(intent);

            }
        });
        cardQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast("Profile Clicked");
                Intent intent =  new Intent(admin_dash.this,ScanQr.class);
                startActivity(intent);
            }
        });
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Logout Clicked");
                Intent intent=new Intent(admin_dash.this,MainActivity.class);
                SharedPreferences sharedPreferences=getSharedPreferences("UserPrefs",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();

                startActivity(intent);
                finish();
            }
        });



    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}