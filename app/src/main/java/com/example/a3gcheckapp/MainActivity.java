package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//The class MainActivity contains the main logic of our "Bürger" application.
public class MainActivity extends AppCompatActivity {

    private ImageButton informationButton;
    private ImageButton scanPageButton;
    static ImageButton buttonCertificates;
    private LinearLayout scrollLayout;
    String fileName;
    XMLParser parser = new XMLParser();
    //number of already certificates
    static int savedCertNr;
    static int btnIndex = 1;

    @Override
    //The method generates the overview page of the application "Bürger".
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dynamic Button
        scrollLayout = findViewById(R.id.scrollLayout);

        XMLParser xmlparser = new XMLParser();

        informationButton = (ImageButton) findViewById(R.id.controlInformationButton);
        informationButton.setOnClickListener(v -> openInformation());

        scanPageButton = (ImageButton) findViewById(R.id.certificateControlButton);
        scanPageButton.setOnClickListener(v -> openScanPage());
        //buttonCertificates = (ImageButton) findViewById(R.id.buttonCert);
        //xmlparser.parseXML();
        //save("Test");
        String QRContent = "<nachweis type = 'impfung'>  <forename>Celine</forename>  <lastname>Hoeckh</lastname>  <birthdate>01-02-2000</birthdate>  <issueDate>01-09-2021</issueDate>  <vaccinationDate>01-09-2021</vaccinationDate> </nachweis>";
        String QRContent2 = "<nachweis type = 'genesung'>  <forename>Celine</forename>  <lastname>Hoeckh</lastname>  <birthdate>01-02-2000</birthdate>  <issueDate>01-09-2021</issueDate>  <recDate>01-09-2021</recDate> </nachweis>";

//        save(QRContent);
//        save(QRContent2);

        loadFiles();
        //Checks how many files are in the Assets folder and accordingly creates many vaccButtons on the main page.
//        for (int size = 0; size < this.getFilesDir().listFiles().length; size++) {
//            createNewImpfText("Veronika", "Taranek", "Vollständiger Impfschutz", "10.10.2021");
//        }
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
                    createNewVaxText(vaxcertificate.getForname(), vaxcertificate.getLastname(), vaxcertificate.getBirthdate(), vaxcertificate.getIssuedate());
                } else if (certificate instanceof Testnachweis ) {
                    Testnachweis testcertificate = (Testnachweis) certificate;
                    //createNewSchnelltestText(testnachweis.getForname(), testnachweis.getLastname(), testnachweis.getTestDate(), testnachweis.getTestTime());
                } else if (certificate instanceof Genesenennachweis ){
                    Genesenennachweis reccertificate = (Genesenennachweis) certificate;
                    createNewRecoveryText(reccertificate.getForname(), reccertificate.getLastname(), reccertificate.getRecDate());
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
    //The method creates a new View for scanned certificates.
    //public void createNewVaccView(String forename, String lastname, String vaccstatus, String date){
        //ImageView vaccView = new ImageView(this);
        //vaccView.setId(btnIndex++);
        //vaccView.setImageResource(R.drawable.certificate_small);
        //vaccView.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
        //scrollLayout.addView(vaccView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        //vaccView.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v){
                //openDetail();
           // }
       // });

  //  }

    //The method generates a new View and fills it with data that has been read from the QR code of a vaccination certificate.
    public void createNewVaxText(String forename, String lastname, String vaxstatus, String date){
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
        vaxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDetail();
            }
        });
    }
    ////The method generates a new View and fills it with data that has been read from the QR code of a test certificate.
    public void createNewTestText(String forename, String lastname, String testdate, String testtime){
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
        testTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDetail();
            }
        });
    }
    ////The method fills the new generated View with data that has been read from the QR code of a proof of recovery certificate.
    public void createNewRecoveryText(String forename, String lastname, String testdate){
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
        recTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDetail();
            }
        });
    }





/**
    public void createNewImpfButton(String forename, String lastname, String impfstatus, String datum){
        //Erstellt den ImageButton
        ImageButton certButton = new ImageButton(this);
        //Gibt den Buttons eine fortlaufende ID
        certButton.setId(btnIndex++);
        //Gibt dem Button die Ressource aus dem Resource Manager
        certButton.setImageResource(R.drawable.certificate_small);
        //Macht den Hintergrund durchsichtig
        certButton.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
        //fügt die Button zum scrollLayout hinzu
        scrollLayout.addView(certButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


      //_________________________________________________________

        //AB HIER ALLES NICHT FUNKTIONAL !!!!

        //Todos:-TextViews zu den Button hinzufügen und an ein Layout "hängen" (vgl. Zeile: 97)
        //      -Titel Textview soll IMPFZERTIFIKAT zeigen
        //      -Textview von Name, Impfstatus und Datum sollen Werte der Methodenimputs übernehmen und anzeigen
     //_________________________________________________________


        LinearLayout buttonLayout = new LinearLayout(this);
        TextView titelView = new TextView(buttonLayout.getContext());
        buttonLayout.addView(titelView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        titelView.setText("Test");
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
    public void openDetail(){
        Intent intent = new Intent(this, detail.class);
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

        //MainActivity.buttonCertificates.setText(builder.toString());
        //buttonCertificates.setText("String");
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
        for (int i = 0; i <= MainActivity.savedCertNr; i++) {
            fileName = "zertifikat" + MainActivity.savedCertNr + ".xml";
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
