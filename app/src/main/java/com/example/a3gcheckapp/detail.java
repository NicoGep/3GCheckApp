package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

//The class detail generates the detailed view of the scanned certificates.
public class detail extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView trashIcon;

    @Override
    // The method generates the page including the detailed view of the scanned certificates with a back button.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> openMain());

        trashIcon = (ImageView) findViewById(R.id.trashIcon);
        trashIcon.setOnClickListener(v -> deleteCertificate());

    }
    // The method opens the class MainActivity.
    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void deleteCertificate(){

    }
}