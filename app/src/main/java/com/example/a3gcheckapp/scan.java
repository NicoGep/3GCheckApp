package com.example.a3gcheckapp;

import android.content.Intent;
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
import com.google.zxing.Result;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;


//The class scan contains all necessary functionalities to scan certificates with a camera.
public class scan extends AppCompatActivity {
    public String BarcodeContent;
    private ImageButton backButton;
    private CodeScanner mCodeScanner;
    private ProgressBar progressBar;
    MainActivity main;

    @Override
    //The method generates the page containing the scan functionality with a camera and can read the given information out of a QR code of a scanned certificate.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = new MainActivity();
        setContentView(R.layout.activity_scan);
        //XMLParser parser = new XMLParser();

        //File file = new File(this.getFilesDir(), "test.txt");
        //System.out.println(this.getFilesDir());

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

                        /**
                        MultiFormatWriter mfw = new MultiFormatWriter();
                        Bitmap bitmap = null;
                        try {
                            mfw.encode(result.getText(), BarcodeFormat.QR_CODE, 200 , 200);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                        **/



                        /**
                         try {
                         //File file = new File(main.getFilesDir(), "test.png");
                         File file = new File("/data/user/0/com.example.a3gcheckapp/files", "test.png");
                         MatrixToImageWriter.writeToPath(new MultiFormatWriter().encode(result.getText(), BarcodeFormat.QR_CODE, 200, 200), "png", file.toPath());
                         } catch (WriterException | IOException e) {
                         e.printStackTrace();
                         }


                        QRGEncoder qrgEncoder = new QRGEncoder(result.getText(), null, QRGContents.Type.TEXT, 200);
                        Bitmap bitmap = qrgEncoder.getBitmap();


                        QRGSaver qrgSaver = new QRGSaver();
                        qrgSaver.save(main.getFilesDir(), result.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);

                        **/

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
    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
