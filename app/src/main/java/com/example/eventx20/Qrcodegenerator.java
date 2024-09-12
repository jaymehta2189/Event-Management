package com.example.eventx20;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class Qrcodegenerator extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private EditText inputEditText;
    private Button generateQrButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodegenerator);

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
//        inputEditText = findViewById(R.id.inputEditText);
//        generateQrButton = findViewById(R.id.generateQrButton);

//        generateQrButton.setOnClickListener(v -> {
//            String inputText = inputEditText.getText().toString().trim();
//
//            if (TextUtils.isEmpty(inputText)) {
//                Toast.makeText(Qrcodegenerator.this, "Please enter some text", Toast.LENGTH_SHORT).show();
//            } else {
//                generateQRCode(inputText);
//            }
//        });
        String qrcode= getIntent().getStringExtra("qrcode");
        generateQRCode(qrcode);
    }

    private void generateQRCode(String text) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            int size = 512;
            Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, ((BitMatrix) bitMatrix).get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            qrCodeImageView.setImageBitmap(bmp);
        } catch (WriterException e) {
            Log.e("Tag", e.toString());
        }
    }
}