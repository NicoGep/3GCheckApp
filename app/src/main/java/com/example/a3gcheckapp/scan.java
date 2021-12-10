package com.example.a3gcheckapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.WriterException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


//The class scan contains all necessary functionalities to scan certificates with a camera.
public class scan extends AppCompatActivity {
    public String BarcodeContent;
    private ImageButton backButton;
    private CodeScanner mCodeScanner;
    private ProgressBar progressBar;
    Bitmap bMap;
    QRGEncoder qrgEncoder;
    MainActivity main;
    private static String timestamp;

    @Override
    //The method generates the page containing the scan functionality with a camera and can read the given information out of a QR code of a scanned certificate.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = new MainActivity();
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

                        // Creates a Bitmap of QR-Code String
                        bMap = createQRBitmap(result.getText(), 400, 400);
                        //Safes Bitmap as PNG in Internal Storage
                        SaveImage(bMap);

                        //Get Timestamp
                        Long tsLong = System.currentTimeMillis()/1000;
                        timestamp = tsLong.toString();


                        mCodeScanner.stopPreview();
                        //progressBar.setVisibility(View.VISIBLE);
                        scannerView.setVisibility(View.INVISIBLE);
                        //process QR Code
                        try {
                            //Seperates Barcode into a Map
                            Map<String, String> map = QRCodeHandler.parseQRdataToStringMap(BarcodeContent);
                            String xmlCert = map.get("nachweis");
                            //Safes SML in Internal Storage
                            save(xmlCert);

                            // Creates a Bitmap of QR-Code String
                            bMap = createQRBitmap(result.getText(), 400, 400);
                            //Safes Bitmap as PNG in Internal Storage
                            SaveImage(bMap);

                            //Go back to MainPage
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

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    //The method opens the class MainActivity.
    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    String fileName;

    //The method saves XML Strings in files.
    public void save(String certText) {
        //for (int i = 0; i <= MainActivity.savedCertNr; i++) {
        FileOutputStream fos = null;
        try {
            // MainActivity.savedCertNr++;
            //fileName = "zertifikat" + this.getFilesDir().listFiles().length + ".xml";
            fileName = "zertifikat" + timestamp + ".xml";
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

    //The methode creates a Bitmap from String input
    private Bitmap createQRBitmap(String qrString, int width, int heigth){
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try{
            matrix = writer.encode(qrString, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e){
            e.printStackTrace();
        }
        Bitmap bitmap = Bitmap.createBitmap(width, heigth, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < heigth; y++){
                bitmap.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    //The methode safes the created Bitmap into Internal Storage
    private static void SaveImage(Bitmap finalBitmap) {

        String root = "/data/data/com.example.a3gcheckapp/files";
        File myDir = new File(root + "/QRCodes");
        myDir.mkdirs();

        //String fname = "Image1.jpg";
        String fname = "QRCode" + timestamp + ".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
