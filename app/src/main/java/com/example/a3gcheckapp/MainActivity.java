package com.example.a3gcheckapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.io.*;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ImageButton informationenButton;
    private ImageButton scanPageButton;
    static ImageButton buttonCertificates;
    private LinearLayout scrollLayout;
    String fileName;
    XMLParser parser = new XMLParser();
    //number of already certificates
    static int savedCertNr;
    static int btnIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dynamic Button
        scrollLayout = findViewById(R.id.scrollLayout);

        XMLParser xmlparser = new XMLParser();

        informationenButton = (ImageButton) findViewById(R.id.pruefInformationButton);
        informationenButton.setOnClickListener(v -> openInformation());

        scanPageButton = (ImageButton) findViewById(R.id.ZertifikatPruefButton);
        scanPageButton.setOnClickListener(v -> openScanPage());
        //buttonCertificates = (ImageButton) findViewById(R.id.buttonCert);
        //xmlparser.parseXML();
        //save("Test");
        String QRContent = "<nachweis type = 'impfung'>  <forename>Celine</forename>  <lastname>Hoeckh</lastname>  <birthdate>01-02-2000</birthdate>  <issueDate>01-09-2021</issueDate>  <vaccinationDate>01-09-2021</vaccinationDate> </nachweis>";
        String QRContent2 = "<nachweis type = 'genesung'>  <forename>Celine</forename>  <lastname>Hoeckh</lastname>  <birthdate>01-02-2000</birthdate>  <issueDate>01-09-2021</issueDate>  <recDate>01-09-2021</recDate> </nachweis>";

        save(QRContent);
        save(QRContent2);

        loadFiles();

        //Checkt wie viele Dateien im Assets Ordner sind und erstellt dementsprechend viele ImpfButtons auf der Main Page
//        for (int size = 0; size < this.getFilesDir().listFiles().length; size++) {
//            createNewImpfText("Veronika", "Taranek", "Vollständiger Impfschutz", "10.10.2021");
//        }
    }

    //Iteration durch alle gespeicherten Zertifikate, Auslesen, Klassifizierung und Anzeige
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
                Certificate certificate = QRCodeHandler.parseNachweisXMLToCertificate(text);
                if(certificate instanceof Impfnachweis){
                    Impfnachweis impfnachweis = (Impfnachweis) certificate;
                    createNewImpfText(impfnachweis.getForname(), impfnachweis.getLastname(), impfnachweis.getBirthdate(), impfnachweis.getErstelldatum());
                } else if (certificate instanceof Testnachweis ) {
                    Testnachweis testnachweis = (Testnachweis) certificate;
                    //createNewSchnelltestText(testnachweis.getForname(), testnachweis.getLastname(), testnachweis.getTestDate(), testnachweis.getTestTime());
                } else if (certificate instanceof Genesenennachweis ){
                    Genesenennachweis genesenennachweis = (Genesenennachweis) certificate;
                    createNewGenesenText(genesenennachweis.getForname(), genesenennachweis.getLastname(), genesenennachweis.getRecDate());
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

    public void createNewImpfView(String forename, String lastname, String impfstatus, String datum){
        ImageView impfView = new ImageView(this);
        impfView.setId(btnIndex++);
        impfView.setImageResource(R.drawable.certificate_small);
        impfView.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
        scrollLayout.addView(impfView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        impfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDetail();
            }
        });

    }

    //IMPFNACHWEIS der Main Activity
    public void createNewImpfText(String forename, String lastname, String impfstatus, String datum){
        TextView impfTextView = new TextView(this);
        impfTextView.setId(btnIndex++);
        impfTextView.setBackgroundResource(R.drawable.certificate_small);
        scrollLayout.addView(impfTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        impfTextView.setPadding(100 , 45, 0, 0);
        impfTextView.setTextSize(40);
        impfTextView.setLineSpacing(75, 0);
        //impfTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        impfTextView.setText("IMPFNACHWEIS\n");
        impfTextView.append(forename + " " + lastname + "\n");
        impfTextView.append(impfstatus + "\n");
        impfTextView.append("Impfdatum: " + datum + "");
        impfTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDetail();
            }
        });
    }
    //SCHNELLTESTNACHWEIS der Main Activity
    public void createNewSchnelltestText(String forename, String lastname, String testdatum, String testzeit){
        TextView impfTextView = new TextView(this);
        impfTextView.setId(btnIndex++);
        impfTextView.setBackgroundResource(R.drawable.certificate_small);
        scrollLayout.addView(impfTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        impfTextView.setPadding(100 , 45, 0, 0);
        impfTextView.setTextSize(40);
        impfTextView.setLineSpacing(75, 0);
        impfTextView.setText("TESTNACHWEIS\n");
        impfTextView.append(forename + " " + lastname + "\n");
        impfTextView.append("Testdatum: "+ testdatum + "\n");
        impfTextView.append("Testzeit: " + testzeit);
        impfTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDetail();
            }
        });
    }
    //GENESENENNACHWEIS der Main Activity
    public void createNewGenesenText(String forename, String lastname, String testdatum){
        TextView impfTextView = new TextView(this);
        impfTextView.setId(btnIndex++);
        impfTextView.setBackgroundResource(R.drawable.certificate_small);
        scrollLayout.addView(impfTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        impfTextView.setPadding(100 , 45, 0, 0);
        impfTextView.setTextSize(40);
        impfTextView.setLineSpacing(75, 0);
        impfTextView.setText("GENESENENNACHWEIS\n");
        impfTextView.append(forename + " " + lastname + "\n");
        impfTextView.append("Testdatum: "+ testdatum + "\n");
        impfTextView.setOnClickListener(new View.OnClickListener() {
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




    public void openInformation() {
        Intent intent = new Intent(this, information.class);
        startActivity(intent);
    }

    public void openDetail(){
        Intent intent = new Intent(this, detail.class);
        startActivity(intent);
    }

    public void openScanPage() {
        Intent intent = new Intent(this, scan.class);
        startActivity(intent);
    }

    public void printCertificates(ArrayList<Certificate> certificates) {
        StringBuilder builder = new StringBuilder();
        for (Certificate certificate : certificates) {
            builder.append(certificate.getForname()).append(" ").append(certificate.getLastname()).append("\n").append("Erstelldatum: ").append(certificate.getErstelldatum()).append("\n\n");
        }

        //MainActivity.buttonCertificates.setText(builder.toString());
        //buttonCertificates.setText("String");
    }

    //save XML Strings in files
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

    //Load texts from saved xml files
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
