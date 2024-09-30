package com.example.eventx20;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventx20.Database.Callback.QrChangeData;
import com.example.eventx20.Database.Callback.QrStudent;
import com.example.eventx20.Database.DataModel.User;
import com.example.eventx20.QrScannRes.QrScan;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

// implements onClickListener for the onclick behaviour of button
public class ScanQr extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn;
    TextView messageText, messageFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);
        messageFormat = findViewById(R.id.textFormat);

        // adding listener to the button
        scanBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                String str = intentResult.getContents();
                messageText.setText(str);
                if(str.endsWith("F")){
                    new QrScan(str).ProcessQrData(new QrChangeData() {
                        @Override
                        public void onChange(Object model) {
                            User user = (User) model;
                            user.setFood(true);
                        }

                        @Override
                        public boolean oncondition(Object model) {
                            User user = (User)model;
                            return user.isFood();
                        }
                    }, new QrStudent() {
                        @Override
                        public void onSuccess(User user) {
                            messageFormat.setText("Enjoy your meal");
                        }

                        @Override
                        public void onAllReadyPresent(User user) {
                            messageFormat.setText("You have already availed your meal");
                        }

                        @Override
                        public void onFailed() {

                        }
                    });

                }else{
                    new QrScan(str).ProcessQrData(new QrChangeData() {
                        @Override
                        public void onChange(Object model) {
                            User user = (User) model;
                            user.setPresent(true);
                        }

                        @Override
                        public boolean oncondition(Object model) {
                            User user = (User)model;
                            return user.isPresent();
                        }
                    }, new QrStudent() {
                        @Override
                        public void onSuccess(User user) {
                            messageFormat.setText("Successfully Entered");
                        }

                        @Override
                        public void onAllReadyPresent(User user) {
                            messageFormat.setText("Already visited");
                        }

                        @Override
                        public void onFailed() {

                        }
                    });

                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
