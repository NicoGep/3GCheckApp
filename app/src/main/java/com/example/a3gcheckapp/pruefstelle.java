package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

public class pruefstelle extends AppCompatActivity {

    private ImageButton informationBtn;
    private ImageView informationBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruefstelle);

        informationBtn = (ImageButton) findViewById(R.id.pruefInformationButton);
        informationBtn.setOnClickListener(v -> openInformation());

        informationBackground = (ImageView) findViewById(R.id.pruefInformationBackground);
    }
    public void openInformation(){
        Intent intent = new Intent(this, pruef_Information.class);
        startActivity(intent);

    }
}