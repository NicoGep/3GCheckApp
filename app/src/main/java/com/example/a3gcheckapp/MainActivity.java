package com.example.a3gcheckapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

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

        detail detailInst = new detail();

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
        scrollLayout = findViewById(R.id.verticalScrollLayout);
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
        for(File file : this.getFilesDir().listFiles()) {
            try {
                fis = openFileInput(file.getName());
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text = br.readLine();
                //Certificate certificate = parser.parseXML(text);
                Certificate certificate = QRCodeHandler.parseCertificateXMLToCertificate(text);
                if(certificate instanceof Impfnachweis){
                    Impfnachweis vaxcertificate = (Impfnachweis) certificate;
                    createNewVaxView(vaxcertificate, vaxcertificate.getForname(), vaxcertificate.getLastname(), vaxcertificate.getBirthdate(), vaxcertificate.getIssuedate());
                } else if (certificate instanceof Testnachweis ) {
                    Testnachweis testcertificate = (Testnachweis) certificate;
                    //createNewSchnelltestText(testcertificate, testnachweis.getForname(), testnachweis.getLastname(), testnachweis.getTestDate(), testnachweis.getTestTime());
                } else if (certificate instanceof Genesenennachweis ){
                    Genesenennachweis reccertificate = (Genesenennachweis) certificate;
                    createNewRecoveryText(reccertificate, reccertificate.getForname(), reccertificate.getLastname(), reccertificate.getRecDate());
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


    //The method generates a new View and fills it with data that has been read from the QR code of a vaccination certificate.
    public void createNewVaxText(Impfnachweis vaxcertificate, String forename, String lastname, String vaxstatus, String date){
        TextView vaxTextView = new TextView(this);
        vaxTextView.setId(btnIndex++);
        vaxTextView.setBackgroundResource(R.drawable.certificate_small);
        scrollLayout.addView(vaxTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        vaxTextView.setPadding(100 , 45, 0, 0);
        vaxTextView.setTextSize(40);
        vaxTextView.setLineSpacing(75, 0);
        //vaccTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        vaxTextView.setText("IMPFNACHWEIS\n");
        vaxTextView.append(forename + " " + lastname + "\n");
        vaxTextView.append(vaxstatus + "\n");
        vaxTextView.append("Impfdatum: " + date + "");
        Intent intent = new Intent(this, detail.class);
        vaxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Gson gson = new Gson();
                String myJson = gson.toJson(vaxcertificate);
                intent.putExtra("vaxCert", myJson);
                openDetail(intent);
            }
        });
    }
    public void createNewVaxView(Impfnachweis vaxcertificate, String forename, String lastname, String vaxstatus, String date){
        TextView vaxTextView = new TextView(this);
        vaxTextView.setId(btnIndex++);
        vaxTextView.setBackgroundResource(R.drawable.certview);
        horizontalScrollView.addView(vaxTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        vaxTextView.setPadding(100 , 45, 0, 0);
        vaxTextView.setTextSize(40);
        vaxTextView.setLineSpacing(75, 0);
        //vaccTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        vaxTextView.setText("IMPFNACHWEIS\n");
        vaxTextView.append(forename + " " + lastname + "\n");
        vaxTextView.append(vaxstatus + "\n");
        vaxTextView.append("Impfdatum: " + date + "");
        Intent intent = new Intent(this, detail.class);
        vaxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Gson gson = new Gson();
                String myJson = gson.toJson(vaxcertificate);
                intent.putExtra("vaxCert", myJson);
            }
        });
    }
    ////The method generates a new View and fills it with data that has been read from the QR code of a test certificate.
    public void createNewTestText(Testnachweis testcertificate, String forename, String lastname, String testdate, String testtime){
        TextView testTextView = new TextView(this);
        testTextView.setId(btnIndex++);
        testTextView.setBackgroundResource(R.drawable.certificate_small);
        scrollLayout.addView(testTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        testTextView.setPadding(100 , 45, 0, 0);
        testTextView.setTextSize(40);
        testTextView.setLineSpacing(75, 0);
        testTextView.setText("TESTNACHWEIS\n");
        testTextView.append(forename + " " + lastname + "\n");
        testTextView.append("Testdatum: "+ testdate + "\n");
        testTextView.append("Testzeit: " + testtime);
        Intent intent = new Intent(this, detail.class);
        testTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Gson gson = new Gson();
                String myJson = gson.toJson(testcertificate);
                intent.putExtra("testCert", myJson);
                openDetail(intent);
            }
        });
    }
    ////The method fills the new generated View with data that has been read from the QR code of a proof of recovery certificate.
    public void createNewRecoveryText(Genesenennachweis reccertificate, String forename, String lastname, String testdate){
        TextView recTextView = new TextView(this);
        recTextView.setId(btnIndex++);
        recTextView.setBackgroundResource(R.drawable.certificate_small);
        scrollLayout.addView(recTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        recTextView.setPadding(100 , 45, 0, 0);
        recTextView.setTextSize(40);
        recTextView.setLineSpacing(75, 0);
        recTextView.setText("GENESENENNACHWEIS\n");
        recTextView.append(forename + " " + lastname + "\n");
        recTextView.append("Testdatum: "+ testdate + "\n");
        Intent intent = new Intent(this, detail.class);
        recTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Gson gson = new Gson();
                String myJson = gson.toJson(reccertificate);
                intent.putExtra("recCert", myJson);
                openDetail(intent);
            }
        });
    }



        //AB HIER ALLES NICHT FUNKTIONAL !!!!

        //Todos:-TextViews zu den Button hinzufügen und an ein Layout "hängen" (vgl. Zeile: 97)
        //      -Titel Textview soll IMPFZERTIFIKAT zeigen
        //      -Textview von Name, Impfstatus und Datum sollen Werte der Methodenimputs übernehmen und anzeigen
     //_________________________________________________________
//
//
//        LinearLayout buttonLayout = new LinearLayout(this);
//        TextView titelView = new TextView(buttonLayout.getContext());
//        buttonLayout.addView(titelView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        titelView.setText("Test");
        /**

        titelView.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextView nameView = new TextView(this);
        nameView.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextView statusView = new TextView(this);
        statusView.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextView datumView = new TextView(this);
        datumView.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        titelView.setText("TITLE");
        nameView.setText("NAME");
        statusView.setText("Status");
        datumView.setText("DATUM");
         **/



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
        Intent intent = new Intent(this, scan.class);
        startActivity(intent);
    }
    //The method prints certificates.
    public void printCertificates(ArrayList<Certificate> certificates) {
        StringBuilder builder = new StringBuilder();
        for (Certificate certificate : certificates) {
            builder.append(certificate.getForname()).append(" ").append(certificate.getLastname()).append("\n").append("Erstelldatum: ").append(certificate.getIssuedate()).append("\n\n");
        }
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
