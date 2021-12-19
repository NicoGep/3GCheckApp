package com.example.a3gcheckapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGEncoder;

import android.content.Intent;
import android.os.Build;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;


//The class Citizenscan contains all necessary functionalities to scan certificates with a camera.
public class CitizenScan extends AppCompatActivity {
    public String BarcodeContent;
    private ImageButton backButton;
    private CodeScanner mCodeScanner;
    private ProgressBar progressBar;
    Bitmap bMap;
    QRGEncoder qrgEncoder;
    CitizenMainActivity main;
    private static String timestamp;

    /**
     * The method generates the page containing the scan functionality with a camera and can read the given data out of a QR code of a scanned certificate.
     *
     * @param savedInstanceState The saved state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = new CitizenMainActivity();
        setContentView(R.layout.activity_scan);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> openCitizenMainActivity());
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


                        BarcodeContent = result.getText();

                        if (true) {

                            //Creates a Bitmap of QR-Code String
                            bMap = createQRBitmap(BarcodeContent, 400, 400);
                            //Safes Bitmap as PNG in Internal Storage
                            saveImage(bMap);

                            //Get Timestamp
                            Long tsLong = System.currentTimeMillis() / 1000;
                            timestamp = tsLong.toString();

                            mCodeScanner.stopPreview();
                            //progressBar.setVisibility(View.VISIBLE);
                            scannerView.setVisibility(View.INVISIBLE);
                            //process QR Code
                            try {
                                //Separates Barcode into a Map
                                Map<String, String> map = QRCodeHandler.parseQRdataToStringMap(BarcodeContent);
                                String xmlCert = map.get(QRCodeHandler.XML_QRCODE_CERTIFICATE);
                                //Safes SML in Internal Storage
                                save(xmlCert);

                                // Creates a Bitmap of QR-Code String
                                bMap = createQRBitmap(result.getText(), 400, 400);
                                //Safes Bitmap as PNG in Internal Storage
                                saveImage(bMap);

                                //Go back to MainPage
                                openCitizenMainActivity();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


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

    /**
     * Is triggered when you want to scan a new QRCode after you scanned one
     */
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    /**
     * Is triggered when you pause the scanner
     */
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    /**
     * The method opens the class CitizenMainActivity.
     */
    public void openCitizenMainActivity() {
        Intent intent = new Intent(this, CitizenMainActivity.class);
        startActivity(intent);
    }

    String fileName;

    /**
     * The method saves XML Strings in files.
     *
     * @param certText String containing the contents of the XML
     */
    public void save(String certText) {
        FileOutputStream fos = null;
        try {
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

    /**
     * The methode creates a Bitmap from String input.
     *
     * @param qrString  The content of the QRCode as a String
     * @param width     Width of the QRCode
     * @param height    Height of the QRCode
     * @return          Returns the QRCode as a Bitmap
     */
    public Bitmap createQRBitmap(String qrString, int width, int height){
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try{
            matrix = writer.encode(qrString, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e){
            e.printStackTrace();
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bitmap.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    /**
     * The methode saves the created Bitmap into Internal Storage.
     *
     * @param finalBitmap The bitmap(QRCode) containing the data of the QRCode
     */
    public static void saveImage(Bitmap finalBitmap) {

        String root = "/data/data/com.example.a3gcheckapp/files";
        File myDir = new File(root + "/QRCodes");
        myDir.mkdirs();

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

