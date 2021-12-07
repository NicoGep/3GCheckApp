package com.example.a3gcheckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

//The class scan includes the whole functionalities to scan a certificate.
public class scan extends AppCompatActivity  {
    public String BarcodeContent;
    private ImageButton backButton;
    private CodeScanner mCodeScanner;
    private ProgressBar progressBar;
    MainActivity main = new MainActivity();

    @Override
    //The method generates the page containing the scan functionality with a camera and can read the given information out of a QR code of a scanned certificate.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        XMLParser parser = new XMLParser();

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> openMain());

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(scan.this, result.getText(), Toast.LENGTH_SHORT).show();
                        BarcodeContent = result.getText();
                        mCodeScanner.stopPreview();
                        //progressBar.setVisibility(View.VISIBLE);
                        scannerView.setVisibility(View.INVISIBLE);
                        //process QR Code
                        BarcodeContent = "<nachweis>    <lastname>Hoeckh</lastname>    <forname>Celine</forname>    <erstelldatum>01-02-2000</erstelldatum> <lastname>Hoeckh</lastname>    <forname>Celine</forname>    <erstelldatum>01-02-2000</erstelldatum> </nachweis>";
                        main.save(BarcodeContent);

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
            }
        });

    }
    //The method opens the class MainActivity
    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    //The method sets the scan on resume.
    protected void onResume(){
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    //The method sets the scan on pause.
    protected void onPause(){
        mCodeScanner.releaseResources();
        super.onPause();
    }
}