package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

// The class anmeldung includes the generation of the homepage.
public class anmeldung extends AppCompatActivity {

    private ImageButton citizenBtn;
    private ImageButton testcenterBtn;
    private ImageButton informationBtn;

    //The method generates the homepage, which contains two buttons, each leading to one of the two applications and a button leading to the information page.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        citizenBtn = (ImageButton) findViewById(R.id.citizen_btn);
        citizenBtn.setOnClickListener(v -> openCitizen());

        testcenterBtn = (ImageButton) findViewById(R.id.testcenter_btn);
        testcenterBtn.setOnClickListener(v -> openTestcenter());

        informationBtn = (ImageButton) findViewById(R.id.registrationInformationButton);
        informationBtn.setOnClickListener(v -> openInformation());
    }

    //The method opens the application "Bürger".
    public void openCitizen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //The method opens the application "Prüfstelle".
    public void openTestcenter() {
        Intent intent = new Intent(this, pruefstelle.class);
        startActivity(intent);
    }
    //The method opens the page "Information".
    public void openInformation(){
        Intent intent = new Intent(this, information.class);
        startActivity(intent);
        }

}


