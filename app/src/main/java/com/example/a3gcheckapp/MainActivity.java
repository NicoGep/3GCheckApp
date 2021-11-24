package com.example.a3gcheckapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static ArrayList<Certificate> certificates;

    private ImageButton informationenButton;
    private ImageButton scanPageButton;
    private Button buttonCertificates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XMLParser xmlparser = new XMLParser();

        certificates = readFromSharedPreferences(this);

        if (certificates == null){
            certificates = new ArrayList<Certificate>();
        }

        informationenButton = (ImageButton) findViewById(R.id.pruefInformationButton);
        informationenButton.setOnClickListener(v -> openInformation());

        scanPageButton = (ImageButton) findViewById(R.id.ZertifikatPruefButton);
        scanPageButton.setOnClickListener(v -> openScanPage());

        xmlparser.parseXML("<nachweis>    <lastname>Hoeckh</lastname>    <forname>Celine</forname>    <erstelldatum>01-02-2000</erstelldatum> <lastname>Hoeckh</lastname>    <forname>Celine</forname>    <erstelldatum>01-02-2000</erstelldatum> </nachweis>");
        buttonCertificates = (Button) findViewById(R.id.buttonCertificates);

        printCertificates(certificates);
        };

    /**
     * SharedPreferences Methoden
     * ArrayList Wert hinzufügen
     * certificates.add(Certificate-Value)
     *
     * SharedPreferences Update:
     * writeToSharedPreferences(MainActivity.this, certificates);
     *
   **/


    public boolean writeToSharedPreferences(Context context, ArrayList<Certificate> arrayList){
        String file = "certList";
        SharedPreferences mPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        //Maybe gson.toJson(certList)
        String json = gson.toJson(arrayList);
        prefsEditor.putString("certList", json);
        prefsEditor.commit();
        return true;
    }
    public ArrayList<Certificate> readFromSharedPreferences(Context context){
        String file = "certList";
        SharedPreferences mPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("certList", "");
        //Certificate oder String... who knows?
        Type type = new TypeToken<List<Certificate>>(){
        }.getType();
        ArrayList<Certificate> certificateArrayList = gson.fromJson(json, type);
        return certificateArrayList;
    }

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
    public void updateArrayList(){
        this.writeToSharedPreferences(MainActivity.this, certificates);
        printCertificates(certificates);
    }


}

