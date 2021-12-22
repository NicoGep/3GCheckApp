package com.example.a3gcheckapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import java.time.LocalDateTime;
import java.util.Map;


/**
 * The class CheckpointScan is responsible to scan the QRCodes for our checkpoint
 */
public class CheckpointScan extends AppCompatActivity {

    private CodeScanner checkCodeScan;
    private ImageButton backButton;
    private boolean isValidated = false;

    /**
     * This method is triggered at the creation of the Checkpoint scan
     *
     * @param savedInstanceState The saved state of the instance
     */
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
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        String BarcodeContent = result.getText();
                        checkCodeScan.stopPreview();

                        Map<String, String> map = null;
                        Certificate certificate = null;
                        try {

                            map = QRCodeHandler.parseQRdataToStringMap(BarcodeContent);
                            String xmlCert = map.get(QRCodeHandler.XML_QRCODE_CERTIFICATE);
                            String xmlSignatureCert = map.get(QRCodeHandler.XML_QRCODE_SIGNATURE);
                            String xmlX509Cert = map.get(QRCodeHandler.XML_QRCODE_X509);

                            //validates and verifies the signature and the certificate of the QRCode
                            isValidated = Validator.isValid(xmlX509Cert, xmlCert, xmlSignatureCert);

                            certificate = QRCodeHandler.parseCertificateXMLToCertificate(xmlCert);

                            //checks the expiration date
                            boolean expired = checkExpirationDate(certificate);

                            //opens the popup that shows if the scanned QRCode is "Gültig" or "Ungültig"
                            openPopUp(certificate, isValidated, expired);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Could not parse QRData to String map");
                        }
                    }
                });
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCodeScan.startPreview();
            }
        });
    }

    /**
     * This method checks the dates of the certificates
     *
     * @param certificate The Certificate that is checked
     * @return True, if expired; False, if not
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkExpirationDate(Certificate certificate) {
        boolean expired = true;
        LocalDateTime local = LocalDateTime.now();

        if (certificate instanceof CertificateVaccination) {
            CertificateVaccination vaxcertificate = (CertificateVaccination) certificate;
            if (vaxcertificate.getVaccinationDate().plusMonths(12).isAfter(local)) {
                expired = false;
            }

        } else if (certificate instanceof CertificateTest) {
            CertificateTest testcertificate = (CertificateTest) certificate;
            if (testcertificate.getTestType() == Testtype.PCR_Test) {
                if (testcertificate.getTestDate().plusHours(48).isAfter(local)) {
                    expired = false;
                }
            } else if (testcertificate.getTestType() == Testtype.Schnelltest) {
                if (testcertificate.getTestDate().plusHours(24).isAfter(local)) {
                    expired = false;
                }
            }

        } else if (certificate instanceof CertificateRecovery) {
            CertificateRecovery reccertificate = (CertificateRecovery) certificate;
            if (reccertificate.getTestDate().plusMonths(6).isAfter(local)) {
                expired = false;
            }
        }

        return expired;
    }

    /**
     * This method is triggered when you want to scan a new QRCode after you scanned one
     */
    @Override
    protected void onResume() {
        super.onResume();
        checkCodeScan.startPreview();
    }

    /**
     * This method is triggered when you pause the scanner
     */
    @Override
    protected void onPause() {
        checkCodeScan.releaseResources();
        super.onPause();
    }

    /**
     * This method opens the CheckpointMainActivity
     */
    public void openCheckMainActivity() {
        Intent intent = new Intent(this, CheckpointMainActivity.class);
        startActivity(intent);
    }

    /**
     * This method opens the validation popup
     *
     * @param certificate Certificate that has been checked
     * @param validation  boolean whether the certificate and the signature are valid and verified
     * @param expired     boolean whether the certificate is expired or not
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openPopUp(Certificate certificate, Boolean validation, Boolean expired) {
        //PopUp Layout Inflate
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.validate_popup, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(findViewById(R.id.scanner_view), Gravity.CENTER, 0, 0);

        TextView detailTxt = (TextView) popupView.findViewById(R.id.detailsTxt);
        detailTxt.setText(certificate.getFirstName() + " " + certificate.getLastName() + "\n");
        detailTxt.append("Geburtsdatum: " + certificate.getBirthdateAsString());
        TextView validTxt = (TextView) popupView.findViewById(R.id.validTxt);
        if (validation && !expired) {
            validTxt.setText("GÜLTIG");
            validTxt.setTextColor(Color.GREEN); //Color Green
        } else {
            validTxt.setText("UNGÜLTIG");
            validTxt.setTextColor(Color.RED); //Color Red
        }

        popupView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}