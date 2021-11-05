package com.example.a3gcheckapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ImageButton informationenButton;
    private ImageButton scanPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        informationenButton = (ImageButton) findViewById(R.id.pruefInformationButton);
        informationenButton.setOnClickListener(v -> openInformation());

        scanPageButton = (ImageButton) findViewById(R.id.ZertifikatPruefButton);
        scanPageButton.setOnClickListener(v -> openScanPage());
        };



    public void openInformation(){
        Intent intent = new Intent(this, information.class);
        startActivity(intent);
    }
    public void openScanPage(){
        Intent intent = new Intent(this, scan.class);
        startActivity(intent);
    }
}

