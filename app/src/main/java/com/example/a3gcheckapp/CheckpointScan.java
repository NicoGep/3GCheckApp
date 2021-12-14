package com.example.a3gcheckapp;

import static java.time.LocalDateTime.now;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;


public class CheckpointScan extends AppCompatActivity {

    private CodeScanner checkCodeScan;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint_scan);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> openCheckMainActivity());

        CodeScannerView codeScannerView = findViewById(R.id.scanner_view);
        checkCodeScan = new CodeScanner(this, codeScannerView);
        checkCodeScan.setFormats(CodeScanner.TWO_DIMENSIONAL_FORMATS);
        checkCodeScan.setScanMode(ScanMode.SINGLE);

        checkCodeScan.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String BarcodeContent = result.getText();
                        Toast.makeText(CheckpointScan.this, BarcodeContent, Toast.LENGTH_SHORT).show();
                        checkCodeScan.stopPreview();

                        openCheckMainActivity();

                        Map<String, String> map = null;
                        Certificate certificate = null;
                        try {
                            map = QRCodeHandler.parseQRdataToStringMap(BarcodeContent);
                            String xmlCert = map.get("nachweis");
                            certificate = QRCodeHandler.parseCertificateXMLToCertificate(xmlCert);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //boolean expired = checkExpirationDate(certificate);

                        /**
                        //PopUp Layout Inflate
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.validate_popup, null);

                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true;
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        popupWindow.showAtLocation(findViewById(R.id.scanner_view), Gravity.CENTER, 0, 0);

                        popupView.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                popupWindow.dismiss();
                                return true;
                            }
                        });
                         **/
                    }
                });
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkCodeScan.startPreview();}
        });
    }

//    private boolean checkExpirationDate(Certificate certificate) {
//        boolean expired = true;
//        LocalDateTime local = LocalDateTime.now();
//
//        //missing: Verschiedene Arten von Impfstoff abfragen
//        if (certificate instanceof CertificateVaccination) {
//            CertificateVaccination vaxcertificate = (CertificateVaccination) certificate;
//            if (vaxcertificate.getVaccDate().plusMonths(12).isBefore(local)){
//                expired = false;
//            }
//
//        } else if (certificate instanceof CertificateTest) {
//            CertificateTest testcertificate = (CertificateTest) certificate;
//            if(testcertificate.getTestType() == Testtype.PCR_Test) {
//                if (testcertificate.getTestDate().plusHours(48).isBefore(local)){
//                    expired = false;
//                }
//            } else if (testcertificate.getTestType() == Testtype.Schnelltest){
//                if (testcertificate.getTestDate().plusHours(24).isBefore(local)){
//                    expired = false;
//                }
//            }
//
//        } else if (certificate instanceof CertificateRecovery) {
//            CertificateRecovery reccertificate = (CertificateRecovery) certificate;
//            if(reccertificate.getRecDate().plusMonths(6).isBefore(local)){
//                expired = false;
//            }
//        }
//
//        return expired;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCodeScan.startPreview();
    }

    @Override
    protected void onPause() {
        checkCodeScan.releaseResources();
        super.onPause();
    }

    public void openCheckMainActivity(){
        Intent intent = new Intent(this, CheckpointMainActivity.class);
        startActivity(intent);
    }
}