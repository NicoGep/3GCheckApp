package com.example.a3gcheckapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    private Button buttonCertificates;
    String fileName;
    //number of already certificates
    static int savedCertNr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XMLParser xmlparser = new XMLParser();

        informationenButton = (ImageButton) findViewById(R.id.pruefInformationButton);
        informationenButton.setOnClickListener(v -> openInformation());

        scanPageButton = (ImageButton) findViewById(R.id.ZertifikatPruefButton);
        scanPageButton.setOnClickListener(v -> openScanPage());
        buttonCertificates = (Button) findViewById(R.id.buttonCertificates);
        //xmlparser.parseXML();
        //nur 2 Methodenaufrufe für Testzwecke
        save("Test");
        load();
    }

    ;


    public void openInformation() {
        Intent intent = new Intent(this, information.class);
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

        buttonCertificates.setText(builder.toString());
    }

    //save XML Strings in files
    public void save(String certText) {
        //for (int i = 0; i <= MainActivity.savedCertNr; i++) {
        FileOutputStream fos = null;
        try {
            MainActivity.savedCertNr++;
            fileName = "zertfikat" + MainActivity.savedCertNr + ".xml";
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
        try {
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text = br.readLine();
            buttonCertificates.setText(text);
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
