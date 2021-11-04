package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class pruef_Information extends AppCompatActivity {

    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruef_information);

        back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(v -> openPruef());
    }
    public void openPruef(){
        Intent intent = new Intent(this, pruefstelle.class);
        startActivity(intent);
    }
}