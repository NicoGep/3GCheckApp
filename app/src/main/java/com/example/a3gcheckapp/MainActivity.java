package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton informationenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        informationenButton = (ImageButton) findViewById(R.id.informationButton);
        informationenButton.setOnClickListener(v -> openInformation());
    }
    public void openInformation(){
        Intent intent = new Intent(this, information.class);
        startActivity(intent);
    }
}