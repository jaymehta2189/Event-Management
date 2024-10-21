package com.example.eventx20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.window.SplashScreen;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView splashImageView = findViewById(R.id.imageView7);

        // Load the fade-out animation from the XML resource
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // Start the animation on the ImageView
        splashImageView.startAnimation(fadeOut);

        // Listen for when the animation finishes
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // You can do something when the animation starts (optional)
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // When the animation ends, start the next activity or remove the ImageView
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                if(sharedPreferences.contains("Key")){
                    if(sharedPreferences.getString("Key","").equals("xyz")){
                        Intent intent = new Intent(splash_screen.this, admin_dash.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(splash_screen.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Intent intent = new Intent(splash_screen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }


            @Override
            public void onAnimationRepeat(Animation animation) {
                // Not used here
            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//
//                if (sharedPreferences.contains("Key")) {
//                    Intent intent = new Intent(splash_screen.this, Dashboard.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Intent intent = new Intent(splash_screen.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        },5000);
    }
}