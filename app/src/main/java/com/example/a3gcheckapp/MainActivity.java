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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ImageButton informationenButton;
    private ImageButton scanPageButton;
    private Button buttonCertificates;

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

        };



    public void openInformation(){
        Intent intent = new Intent(this, information.class);
        startActivity(intent);
    }
    public void openScanPage(){
        Intent intent = new Intent(this, scan.class);
        startActivity(intent);
    }

    public void printCertificates (ArrayList<Certificate> certificates){
        StringBuilder builder = new StringBuilder();
        for (Certificate certificate : certificates ) {
            builder.append(certificate.getForname()).append(" ").append(certificate.getLastname()).append("\n").append("Erstelldatum: ").append(certificate.getErstelldatum()).append("\n\n");
        }

        buttonCertificates.setText(builder.toString());
    }

}

