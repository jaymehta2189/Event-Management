package com.example.eventx20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class Dashboard extends AppCompatActivity {

    CardView cardTeam;
    CardView cardList;
    CardView cardQr;

    CardView cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cardTeam = findViewById(R.id.cardTeam);
        cardList = findViewById(R.id.cardList);
        cardQr = findViewById(R.id.cardQr);

        cardLogout = findViewById(R.id.cardLogout);

        cardTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Team Clicked");
                Intent intent =  new Intent(Dashboard.this,ViewTeam.class);
                startActivity(intent);
            }
        });
        cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast("Chat Clicked");
                Intent intent=new Intent(Dashboard.this, currentevents.class);

                startActivity(intent);

            }
        });
        cardQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast("Profile Clicked");
                Intent intent=new Intent(Dashboard.this, Qrcodegenerator.class);
                String qrcode= getIntent().getStringExtra("qrcode");
                intent.putExtra("qrcode",qrcode);
                startActivity(intent);
            }
        });
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Logout Clicked");
                Intent intent=new Intent(Dashboard.this,MainActivity.class);
                SharedPreferences sharedPreferences=getSharedPreferences("UserPrefs",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                startActivity(intent);
            }
        });


    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}