package com.example.eventx20;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;


public class Qrfargment extends Fragment {
ImageView qrCodeImageView;
    public Qrfargment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_qrfargment, container, false);
        qrCodeImageView = view.findViewById(R.id.qrCodeImageView);
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
//        String qrcode= getIntent().getStringExtra("qrcode");
        SharedPreferences sharedPreferences=requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String qrdata=sharedPreferences.getString("qrdata","");
        generateQRCode(qrdata);
        return view;
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