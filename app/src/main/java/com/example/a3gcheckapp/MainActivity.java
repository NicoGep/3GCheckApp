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
import java.util.ArrayList;

//The class MainActivity contains the main logic of our "Bürger" application.
public class MainActivity extends AppCompatActivity {

    private ImageButton informationButton;
    private ImageButton scanPageButton;
    static ImageButton buttonCertificates;
    private LinearLayout scrollLayout;
    private LinearLayout horizontalScrollView;
    String fileName;
    XMLParser parser = new XMLParser();
    //number of already certificates
    static int savedCertNr ;
    static int btnIndex = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    //The method generates the overview page of the application "Bürger".
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        File root = new File(Environment.getExternalStorageDirectory(), "note.txt");
        try {
            root.createNewFile();
            Path p = root.toPath();
        } catch (IOException e) {
            e.printStackTrace();
        }



        File file = new File(this.getFilesDir(), "test.txt");
        System.out.println(this.getFilesDir());

        //Dynamic Button
        horizontalScrollView = findViewById(R.id.horizontalScrollLayout);

        XMLParser xmlparser = new XMLParser();

        informationButton = (ImageButton) findViewById(R.id.controlInformationButton);
        informationButton.setOnClickListener(v -> openInformation());

        scanPageButton = (ImageButton) findViewById(R.id.certificateControlButton);
        scanPageButton.setOnClickListener(v -> openScanPage());

        //save("Test");
        // String QRContent = "<nachweis type = 'impfung'>  <forename>Celine</forename>  <lastname>Hoeckh</lastname>  <birthdate>01-02-2000</birthdate>  <issueDate>01-09-2021</issueDate>  <vaccinationDate>01-09-2021</vaccinationDate> </nachweis>";
        // String QRContent2 = "<nachweis type = 'genesung'>  <forename>Celine</forename>  <lastname>Hoeckh</lastname>  <birthdate>01-02-2000</birthdate>  <issueDate>01-09-2021</issueDate>  <recDate>01-09-2021</recDate> </nachweis>";
//        save(QRContent2);
        loadFiles();

    }

    //The method creates iterations through all saved certificates including the functionalities to read out, to classify und to display.
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
                    StringBuilder sb = new StringBuilder();
                    String text = br.readLine();
                    //Certificate certificate = parser.parseXML(text);
                    Certificate certificate = QRCodeHandler.parseCertificateXMLToCertificate(text);
                    if (certificate instanceof Impfnachweis) {
                        Impfnachweis vaxcertificate = (Impfnachweis) certificate;
                        createNewVaxView(filename, vaxcertificate.getForname(), vaxcertificate.getLastname(), vaxcertificate.getBirthdate(), vaxcertificate.getIssuedate(), QRCodeFile);
                    } else if (certificate instanceof Testnachweis) {
                        Testnachweis testcertificate = (Testnachweis) certificate;
                        //createNewSchnelltestView(String filename, testnachweis.getForname(), testnachweis.getLastname(), testnachweis.getTestDate(), testnachweis.getTestTime(), QRCodeFile);
                    } else if (certificate instanceof Genesenennachweis) {
                        Genesenennachweis reccertificate = (Genesenennachweis) certificate;
                        createNewRecoveryView(filename, reccertificate.getForname(), reccertificate.getLastname(), reccertificate.getRecDate(), QRCodeFile);
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
    public void createNewVaxView(String filename, String forename, String lastname, String vaxstatus, String date, File qrFile){

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.vaxcertificateview, null);
        ViewGroup main = (ViewGroup) findViewById(R.id.horizontalScrollLayout);
        main.addView(view, 0);

        TextView certviewTitle = (TextView) horizontalScrollView.findViewById(R.id.certviewTitleText);
        certviewTitle.setText("IMPFNACHWEIS"); // HIER KOMMT DER TITEL REIN!!
        TextView certviewDescription = (TextView) horizontalScrollView.findViewById(R.id.certviewDescription);
        certviewDescription.setText(forename + " " + lastname + "\n");
        certviewDescription.append(vaxstatus + "\n");
        certviewDescription.append("Impfdatum: " + date + "");
        ImageView certviewQRCode = (ImageView) horizontalScrollView.findViewById(R.id.certviewQRCode);
        Bitmap qrBitmap = BitmapFactory.decodeFile(qrFile.getAbsolutePath());
        certviewQRCode.setImageBitmap(qrBitmap);
        ImageButton certviewDelete = (ImageButton) horizontalScrollView.findViewById(R.id.certviewDelete);
        certviewDelete.setOnClickListener(v -> deleteCertificate(filename));

    }

    //The method generates a new TextView and fills it with data that has been read from the QR code of a test certificate.
    public void createNewTestView(String filename, String forename, String lastname, String testdate, String testtime, File qrFile){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.vaxcertificateview, null);
        ViewGroup main = (ViewGroup) findViewById(R.id.horizontalScrollLayout);
        main.addView(view, 0);

        TextView certviewTitle = (TextView) horizontalScrollView.findViewById(R.id.certviewTitleText);
        certviewTitle.setText("TESTNACHWEIS"); // HIER KOMMT DER TITEL REIN!!
        TextView certviewDescription = (TextView) horizontalScrollView.findViewById(R.id.certviewDescription);
        certviewDescription.setText(forename + " " + lastname + "\n");
        certviewDescription.append("Testdatum: "+ testdate + "\n");
        certviewDescription.append("Testzeit: " + testtime);
        ImageView certviewQRCode = (ImageView) horizontalScrollView.findViewById(R.id.certviewQRCode);
        Bitmap qrBitmap = BitmapFactory.decodeFile(qrFile.getAbsolutePath());
        certviewQRCode.setImageBitmap(qrBitmap);
        ImageButton certviewDelete = (ImageButton) horizontalScrollView.findViewById(R.id.certviewDelete);
        certviewDelete.setOnClickListener(v -> deleteCertificate(filename));

    }

    ////The method fills the new generated TextView with data that has been read from the QR code of a proof of recovery certificate.
    public void createNewRecoveryView(String filename, String forename, String lastname, String testdate, File qrFile){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.vaxcertificateview, null);
        ViewGroup main = (ViewGroup) findViewById(R.id.horizontalScrollLayout);
        main.addView(view, 0);

        TextView certviewTitle = (TextView) horizontalScrollView.findViewById(R.id.certviewTitleText);
        certviewTitle.setText("GENESENENNACHWEIS"); // HIER KOMMT DER TITEL REIN!!
        TextView certviewDescription = (TextView) horizontalScrollView.findViewById(R.id.certviewDescription);
        certviewDescription.setText(forename + " " + lastname + "\n");
        certviewDescription.append("Testdatum: "+ testdate + "\n");
        ImageView certviewQRCode = (ImageView) horizontalScrollView.findViewById(R.id.certviewQRCode);
        Bitmap qrBitmap = BitmapFactory.decodeFile(qrFile.getAbsolutePath());
        certviewQRCode.setImageBitmap(qrBitmap);
        ImageButton certviewDelete = (ImageButton) horizontalScrollView.findViewById(R.id.certviewDelete);
        certviewDelete.setOnClickListener(v -> deleteCertificate(filename));

    }


    //The method opens the information page.
    public void openInformation() {
        Intent intent = new Intent(this, information.class);
        startActivity(intent);
    }

    //The method opens the page containing the detailed information of a scanned certificate.
    public void openDetail(Intent intent){
        startActivity(intent);
    }

    //The method opens the page containing the scan functionality.
    public void openScanPage() {
        Intent intent = new Intent(this, Scan.class);
        startActivity(intent);
    }
    //The method prints certificates.
    public void printCertificates(ArrayList<Certificate> certificates) {
        StringBuilder builder = new StringBuilder();
        for (Certificate certificate : certificates) {
            builder.append(certificate.getForname()).append(" ").append(certificate.getLastname()).append("\n").append("Erstelldatum: ").append(certificate.getIssuedate()).append("\n\n");
        }
    }
    //This method deletes a specific certificate and its matching QRCode
    public void deleteCertificate(String certName){
        String deletesubQRCode = certName.substring(10, 20);
        String deleteQRPath = "/data/data/com.example.a3gcheckapp/files/QRCodes";
        File QRCodeFile = new File(deleteQRPath + "/QRCode" + deletesubQRCode + ".jpg");

        File deleteFile = new File("/data/data/com.example.a3gcheckapp/files/" + certName);
        boolean deleted = deleteFile.delete();
        boolean deletedQRCode = QRCodeFile.delete();
        recreate();
    }

//    //The method saves XML Strings in files.
//    public void save(String certText) {
//        //for (int i = 0; i <= MainActivity.savedCertNr; i++) {
//        FileOutputStream fos = null;
//        try {
//            MainActivity.savedCertNr++;
//            fileName = "zertifikat" + MainActivity.savedCertNr + ".xml";
//            fos = openFileOutput(fileName, MODE_PRIVATE);
//            fos.write(certText.getBytes());
//        } catch (Exception e) {
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException ie) {
//                }
//
//            }
//
//        }
//
//    }

    //The method loads texts from saved XML files.
    public void load() {
        FileInputStream fis = null;
        for (int i = 0; i <= savedCertNr; i++) {
            fileName = "zertifikat" + savedCertNr + ".xml";
            try {
                fis = openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text = br.readLine();
                parser.parseXML(text);

            } catch (Exception e) {
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ie) { }
                }
            }
        }


    }
}
