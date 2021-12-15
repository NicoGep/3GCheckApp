package com.example.a3gcheckapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

//The class CitizenMainActivity contains the main logic of our "Bürger" application.
public class CitizenMainActivity extends AppCompatActivity {

    private ImageButton informationButton;
    private ImageButton scanPageButton;
    private LinearLayout horizontalScrollView;
    String fileName;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    //The method generates the overview page of the application "Bürger".
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horizontalScrollView = findViewById(R.id.horizontalScrollLayout);

        informationButton = (ImageButton) findViewById(R.id.checkInformationButton);
        informationButton.setOnClickListener(v -> openInformation());

        scanPageButton = (ImageButton) findViewById(R.id.certificateCheckButton);
        scanPageButton.setOnClickListener(v -> openScanPage());

        File root = new File(Environment.getExternalStorageDirectory(), "note.txt");
        try {
            root.createNewFile();
            Path p = root.toPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadFiles();

    }

    //The method creates iterations through all saved certificates including the functionalities to read out, to classify und to display.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadFiles() {
        FileInputStream fis = null;
        String filename;
        for(File file : this.getFilesDir().listFiles()) {

            filename = file.getName();

            if(filename.startsWith("zertifikat")) {
                String subQRCode = filename.substring(10, 20);
                String QRPath = "/data/data/com.example.a3gcheckapp/files/QRCodes";
                File QRCodeFile = new File(QRPath + "/QRCode" + subQRCode + ".jpg");

                try {
                    fis = openFileInput(file.getName());
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    String text = br.readLine();
                    Certificate certificate = QRCodeHandler.parseCertificateXMLToCertificate(text);
                    if (certificate instanceof CertificateVaccination) {
                        CertificateVaccination vaxcertificate = (CertificateVaccination) certificate;
                        createNewVaxView(filename, vaxcertificate, QRCodeFile);
                    } else if (certificate instanceof CertificateTest) {
                        CertificateTest testcertificate = (CertificateTest) certificate;
                        createNewTestView(filename, testcertificate, QRCodeFile);
                    } else if (certificate instanceof CertificateRecovery) {
                        CertificateRecovery reccertificate = (CertificateRecovery) certificate;
                        createNewRecoveryView(filename, reccertificate, QRCodeFile);
                    }

                } catch (Exception e) {
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException ie) {
                        }
                    }
                }
            }
        }
    }

    //The method generates a new TextView and fills it with data that has been read from the QR code of a vaccination certificate.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNewVaxView(String filename, CertificateVaccination vaxcert, File qrFile){

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.vaxcertificateview, null);
        ViewGroup main = (ViewGroup) findViewById(R.id.horizontalScrollLayout);
        main.addView(view, 0);

        TextView certviewTitle = (TextView) horizontalScrollView.findViewById(R.id.certviewTitleText);
        certviewTitle.setText("IMPFNACHWEIS");
        TextView certviewDescription = (TextView) horizontalScrollView.findViewById(R.id.certviewDescription);
        certviewDescription.setText(vaxcert.getFirstName() + " " + vaxcert.getLastName() + "\n");
        certviewDescription.setText("Geburtsdatum: "  + vaxcert.getBirthdateAsString() + "\n");
        certviewDescription.append("Impfdatum: " + vaxcert.getVaccinationDateAsString() + "\n");
        certviewDescription.append("Erstellungsdatum: " + vaxcert.getIssueDateAsString() + "\n");
        certviewDescription.append("Impfstoff: " + vaxcert.getVaccine() + "\n");
        certviewDescription.append(vaxcert.getVaccinationCount() + ". Impfung");
        ImageView certviewQRCode = (ImageView) horizontalScrollView.findViewById(R.id.certviewQRCode);
        Bitmap qrBitmap = BitmapFactory.decodeFile(qrFile.getAbsolutePath());
        certviewQRCode.setImageBitmap(qrBitmap);
        ImageButton certviewDelete = (ImageButton) horizontalScrollView.findViewById(R.id.certviewDelete);
        certviewDelete.setOnClickListener(v -> deleteCertificate(filename));

    }

    //The method generates a new TextView and fills it with data that has been read from the QR code of a test certificate.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNewTestView(String filename, CertificateTest testCert, File qrFile){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.vaxcertificateview, null);
        ViewGroup main = (ViewGroup) findViewById(R.id.horizontalScrollLayout);
        main.addView(view, 0);

        TextView certviewTitle = (TextView) horizontalScrollView.findViewById(R.id.certviewTitleText);
        certviewTitle.setText("TESTNACHWEIS");
        TextView certviewDescription = (TextView) horizontalScrollView.findViewById(R.id.certviewDescription);
        certviewDescription.setText(testCert.getFirstName() + " " + testCert.getLastName() + "\n");
        certviewDescription.setText("Geburtsdatum: "  + testCert.getBirthdateAsString() + "\n");
        certviewDescription.append("Testdatum: "+ testCert.getTestDateAsString() + "\n");
        certviewDescription.append("Testtyp: " + testCert.getTestType());
        ImageView certviewQRCode = (ImageView) horizontalScrollView.findViewById(R.id.certviewQRCode);
        Bitmap qrBitmap = BitmapFactory.decodeFile(qrFile.getAbsolutePath());
        certviewQRCode.setImageBitmap(qrBitmap);
        ImageButton certviewDelete = (ImageButton) horizontalScrollView.findViewById(R.id.certviewDelete);
        certviewDelete.setOnClickListener(v -> deleteCertificate(filename));

    }

    ////The method fills the new generated TextView with data that has been read from the QR code of a proof of recovery certificate.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNewRecoveryView(String filename, CertificateRecovery recCert, File qrFile){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.vaxcertificateview, null);
        ViewGroup main = (ViewGroup) findViewById(R.id.horizontalScrollLayout);
        main.addView(view, 0);

        TextView certviewTitle = (TextView) horizontalScrollView.findViewById(R.id.certviewTitleText);
        certviewTitle.setText("GENESENENNACHWEIS");
        TextView certviewDescription = (TextView) horizontalScrollView.findViewById(R.id.certviewDescription);
        certviewDescription.setText(recCert.getFirstName() + " " + recCert.getLastName() + "\n");
        certviewDescription.setText("Geburtsdatum: "  + recCert.getBirthdateAsString() + "\n");
        certviewDescription.append("Testdatum: "+ recCert.getTestDateAsString() + "\n");
        ImageView certviewQRCode = (ImageView) horizontalScrollView.findViewById(R.id.certviewQRCode);
        Bitmap qrBitmap = BitmapFactory.decodeFile(qrFile.getAbsolutePath());
        certviewQRCode.setImageBitmap(qrBitmap);
        ImageButton certviewDelete = (ImageButton) horizontalScrollView.findViewById(R.id.certviewDelete);
        certviewDelete.setOnClickListener(v -> deleteCertificate(filename));

    }

    //The method opens the CitizenInformation page.
    public void openInformation() {
        Intent intent = new Intent(this, CitizenInformation.class);
        startActivity(intent);
    }

    //The method opens the page containing the scan functionality.
    public void openScanPage() {
        Intent intent = new Intent(this, CitizenScan.class);
        startActivity(intent);
    }

    //This method deletes a specific certificate and its matching QRCode.
    public void deleteCertificate(String certName) {
        String deletesubQRCode = certName.substring(10, 20);
        String deleteQRPath = "/data/data/com.example.a3gcheckapp/files/QRCodes";
        File QRCodeFile = new File(deleteQRPath + "/QRCode" + deletesubQRCode + ".jpg");

        File deleteFile = new File("/data/data/com.example.a3gcheckapp/files/" + certName);
        boolean deleted = deleteFile.delete();
        boolean deletedQRCode = QRCodeFile.delete();
        recreate();
    }
}
