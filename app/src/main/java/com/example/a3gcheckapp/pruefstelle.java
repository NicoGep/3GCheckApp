package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

//The class MainActivity contains the main logic of our "Prüfstelle" application.
public class pruefstelle extends AppCompatActivity {

    private ImageButton informationBtn;
    private ImageView informationBackground;
    private ImageButton controllButton;


    @Override
    //The method generates the overview page of the application "Prüfstelle".
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruefstelle);

        informationBtn = (ImageButton) findViewById(R.id.controlInformationButton);
        informationBtn.setOnClickListener(v -> openInformation());

        informationBackground = (ImageView) findViewById(R.id.pruefInformationBackground);

        controllButton = (ImageButton) findViewById(R.id.certificateControlButton);
        controllButton.setOnClickListener(v -> openValidation());
    }
    //The method opens the information page.
    public void openInformation(){
        Intent intent = new Intent(this, pruef_Information.class);
        startActivity(intent);

    }
    //The method opens the scan page
    public void openValidation(){
        Intent intent = new Intent(this, PruefstellenScan.class);
        startActivity(intent);
    }
}