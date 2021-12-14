package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

// The class Login includes the generation of the homepage.
public class Login extends AppCompatActivity {

    private ImageButton citizenBtn;
    private ImageButton checkpointBtn;
    private ImageButton informationBtn;

    //The method generates the homepage, which contains two buttons, each leading to one of the two applications and a button leading to the CitizenInformation page.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        citizenBtn = (ImageButton) findViewById(R.id.citizen_btn);
        citizenBtn.setOnClickListener(v -> openCitizen());

        checkpointBtn = (ImageButton) findViewById(R.id.checkpoint_btn);
        checkpointBtn.setOnClickListener(v -> openCheckpoint());

        informationBtn = (ImageButton) findViewById(R.id.registrationInformationButton);
        informationBtn.setOnClickListener(v -> openInformation());
    }

    //The method opens the application "Bürger".
    public void openCitizen(){
        Intent intent = new Intent(this, CitizenMainActivity.class);
        startActivity(intent);
    }
    //The method opens the application "Prüfstelle".
    public void openCheckpoint() {
        Intent intent = new Intent(this, CheckpointMainActivity.class);
        startActivity(intent);
    }
    //The method opens the page "CitizenInformation".
    public void openInformation(){
        Intent intent = new Intent(this, CitizenInformation.class);
        startActivity(intent);
        }

}


