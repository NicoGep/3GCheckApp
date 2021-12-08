package com.example.a3gcheckapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

//The class scan contains all necessary functionalities to scan certificates with a camera.
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
        //XMLParser parser = new XMLParser();

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> openMain());

        progressBar = (ProgressBar) findViewById(R.id.progressBar);



        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setFormats(CodeScanner.TWO_DIMENSIONAL_FORMATS);
        mCodeScanner.setScanMode(ScanMode.SINGLE);


        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        Toast.makeText(scan.this, result.getText(), Toast.LENGTH_SHORT).show();
                        BarcodeContent = result.getText();

                        File file = new File("/data/data/com.example.a3gcheckapp/files");
                        try {
                            MatrixToImageWriter.writeToPath(new MultiFormatWriter().encode(result.getText(), BarcodeFormat.QR_CODE, 200, 200), "png", file.toPath());
                        } catch (WriterException | IOException e) {
                            e.printStackTrace();
                        }

                        mCodeScanner.stopPreview();
                        //progressBar.setVisibility(View.VISIBLE);
                        scannerView.setVisibility(View.INVISIBLE);
                        //process QR Code
                        try {
                            Map<String, String> map = QRCodeHandler.parseQRdataToStringMap(BarcodeContent);
                            String xmlCert = map.get("nachweis");
                            save(xmlCert);
                            openMain();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


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
    //The method opens the class MainActivity.
    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause(){
        mCodeScanner.releaseResources();
        super.onPause();
    }
    String fileName;

    //The method saves XML Strings in files.
    public void save(String certText) {
        //for (int i = 0; i <= MainActivity.savedCertNr; i++) {
        FileOutputStream fos = null;
        try {
            MainActivity.savedCertNr++;
            fileName = "zertifikat" + MainActivity.savedCertNr + ".xml";
            fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(certText.getBytes());
        } catch (Exception e) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ie) {
                }

            }

        }

    }


}