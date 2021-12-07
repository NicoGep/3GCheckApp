package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

public class anmeldung extends AppCompatActivity {

    private ImageButton buergerBtn;
    private ImageButton pruefstellenBtn;
    private ImageButton informationBtn;

    //Homepage is generated, which contains two buttons, each leading to one of the two applications
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anmeldung);

        buergerBtn = (ImageButton) findViewById(R.id.buerger_btn);
        buergerBtn.setOnClickListener(v -> openBuerger());

        pruefstellenBtn = (ImageButton) findViewById(R.id.pruefstellen_btn);
        pruefstellenBtn.setOnClickListener(v -> openPruefstelle());

        informationBtn = (ImageButton) findViewById(R.id.anmeldungInformationButton);
        informationBtn.setOnClickListener(v -> openInformation());
    }

    //Opens the Application "Bürger"
    public void openBuerger(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //Opens the Application "Prüfstelle"
    public void openPruefstelle() {
        Intent intent = new Intent(this, pruefstelle.class);
        startActivity(intent);
    }
    //Opens the Page "Information"
    public void openInformation(){
        Intent intent = new Intent(this, information.class);
        startActivity(intent);
        }

}


